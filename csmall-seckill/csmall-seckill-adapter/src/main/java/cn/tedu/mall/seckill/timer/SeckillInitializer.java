package cn.tedu.mall.seckill.timer;

import cn.tedu.mall.pojo.seckill.model.SeckillSku;
import cn.tedu.mall.pojo.seckill.model.SeckillSpu;
import cn.tedu.mall.pojo.seckill.vo.SeckillSkuVO;
import cn.tedu.mall.pojo.seckill.vo.SeckillSpuVO;
import cn.tedu.mall.seckill.mapper.SeckillSkuMapper;
import cn.tedu.mall.seckill.mapper.SeckillSpuMapper;
import cn.tedu.mall.seckill.service.impl.AbstractSeckillServiceImpl;
import cn.tedu.mall.seckill.service.impl.SeckillSkuServiceImpl;
import cn.tedu.mall.seckill.service.impl.SeckillSpuServiceImpl;
import cn.tedu.mall.seckill.utils.RedisBloomUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 初始化所有秒杀缓存数据包括
 * 布隆过滤器
 * 商品信息 spu sku
 */
@Component
@Slf4j
public class SeckillInitializer extends AbstractSeckillServiceImpl {
    @Autowired
    private SeckillSpuMapper seckillSpuMapper;
    @Autowired
    private SeckillSkuMapper seckillSkuMapper;
    @Autowired
    private RedisBloomUtils redisBloomUtils;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //初始化 SPU列表 KEY=DAY,SECKILL,SPUID
    public void startInit() {
        //查询spu
        List<SeckillSpu> spus = seckillSpuMapper.selectSeckillSpus();
        if (spus == null || spus.size()== 0){
            log.error("初始化秒杀商品spu为空,请确保数据数据正确");
            return;
        }
        //准备缓存对象
        List<SeckillSpuVO> spuVOS=new ArrayList<>();
        for (SeckillSpu spu : spus) {
            //封装缓存对象
            SeckillSpuVO spuVO=new SeckillSpuVO();
            BeanUtils.copyProperties(spu,spuVO);
            spuVOS.add(spuVO);
            //初始化spu布隆过滤器
            initBloomSpu(spu);
            //初始化spu秒杀路径随机字符串
            initRandCode(spu);
            //初始化单个spu对象
            initSpu(spuVO);
            //初始化spu下的sku数据
            initSkuData(spu.getId());
        }
        clearCache(SeckillSpuServiceImpl.SECKILL_SPUS);
        setListCache(SeckillSpuServiceImpl.SECKILL_SPUS,spuVOS,null,null);
    }

    private void initSpu(SeckillSpuVO spuVO) {
        String spuCacheKey=SeckillSpuServiceImpl.SECKILL_SPU_PREFIX+spuVO.getId();
        setCache(spuCacheKey,spuVO);
    }

    private void initRandCode(SeckillSpu spu) {
        String spuRandomKey = SeckillSpuServiceImpl.SECKILL_SPU_RAND_PREFIX+spu.getId();
        String randCode=new Random().nextInt(900000)+1000+"";
        stringRedisTemplate.opsForValue().set(spuRandomKey,randCode);
    }

    private void initBloomSpu(SeckillSpu spu) {
        String spuBloomKey=SeckillSpuServiceImpl.SECKILL_SPU_BLOOM;
        redisBloomUtils.bfadd(spuBloomKey,spu.getId()+"");
    }

    //初始化 SKU数据
    private void initSkuData(Long spuId) {
        List<SeckillSku> skus = seckillSkuMapper.selectSeckillSkusBySpuId(spuId);
        List<SeckillSkuVO> skuVOS=new ArrayList<>();
        if (skus == null || skus.size() == 0){
            log.error("初始化秒杀商品sku为空,请确保数据数据正确");
            return;
        }
        for (SeckillSku sku : skus) {
            //封装转化对象
            SeckillSkuVO skuVO=new SeckillSkuVO();
            BeanUtils.copyProperties(sku,skuVO);
            skuVOS.add(skuVO);
            //初始化sku布隆过滤器
            initBloomSku(sku);
            //初始化库存
            initSkuStock(sku);
        }
        String skuListCacheKey= SeckillSkuServiceImpl.SECKILL_SKUS_PREFIX+spuId;
        clearCache(skuListCacheKey);
        setListCache(skuListCacheKey,skuVOS,null,null);
    }

    private void initSkuStock(SeckillSku sku) {
        String stockKey= SeckillSkuServiceImpl.SECKILL_SKU_STOCK_PREFIX+sku.getId();
        stringRedisTemplate.opsForValue().set(stockKey,sku.getStock()+"");
    }

    private void initBloomSku(SeckillSku sku) {
        String skuBloomKey=SeckillSkuServiceImpl.SECKILL_SKU_BLOOM_KEY;
        redisBloomUtils.bfadd(skuBloomKey,sku.getId()+"");
    }

    private boolean enableSchedule=true;
    public void scheduleStock(){
        while (enableSchedule) {
            List<SeckillSpuVO> spuListCache = getListCache(SeckillSpuServiceImpl.SECKILL_SPUS);
            log.debug("查询到的秒杀商品spu列表:{}", spuListCache);
            for (SeckillSpuVO spuVO : spuListCache) {
                Long spuId = spuVO.getId();
                List<SeckillSku> skus = seckillSkuMapper.selectSeckillSkusBySpuId(spuId);
                for (SeckillSku sku : skus) {
                    //更新库存
                    String stockKey = SeckillSkuServiceImpl.SECKILL_SKU_STOCK_PREFIX + sku.getId();
                    initSkuStock(sku);
                    log.debug("更新库存:{},当前库存数:{}", stockKey, sku.getStock());
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
