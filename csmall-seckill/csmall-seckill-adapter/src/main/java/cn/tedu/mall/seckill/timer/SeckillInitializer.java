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
    //TODO 初始化布隆
    //初始化 SPU列表 KEY=DAY,SECKILL,SPUID
    public void initProductData() {
        List<SeckillSpu> spus = seckillSpuMapper.selectSeckillSpus();
        Map<String,Object> randCodes=new HashMap<>();
        if (spus == null || spus.size()== 0){
            log.error("初始化秒杀商品spu为空,请确保数据数据正确");
            return;
        }
        for (SeckillSpu spu : spus) {
            SeckillSpuVO spuVO=new SeckillSpuVO();
            BeanUtils.copyProperties(spu,spuVO);
            String spuRandomKey = SeckillSpuServiceImpl.SECKILL_SPU_RAND_PREFIX+spu.getId();
            String randCode=new Random().nextInt(900000)+1000+"";
            randCodes.put(spuRandomKey,randCode);
            String spuCacheKey=SeckillSpuServiceImpl.SECKILL_SPU_PREFIX+spu.getId();
            setCache(spuCacheKey,spuVO);
            initSkuList(spu.getId());
        }
        HashOperations operations = redisTemplate.opsForHash();
        //会覆盖
        operations.putAll(SeckillSpuServiceImpl.SECKILL_SPU_RAND_MAP_PREFIX,randCodes);
        //leftPush不会覆盖,需要清除
        clearCache(SeckillSpuServiceImpl.SECKILL_SPUS);
        setListCache(SeckillSpuServiceImpl.SECKILL_SPUS,spus,null,null);
    }
    //初始化 SKU数据
    private void initSkuList(Long spuId) {
        List<SeckillSku> skus = seckillSkuMapper.selectSeckillSkusBySpuId(spuId);
        List<SeckillSkuVO> skuVOS=new ArrayList<>();
        if (skus == null || skus.size() == 0){
            log.error("初始化秒杀商品sku为空,请确保数据数据正确");
            return;
        }
        for (SeckillSku sku : skus) {
            SeckillSkuVO skuVO=new SeckillSkuVO();
            BeanUtils.copyProperties(sku,skuVO);
            skuVOS.add(skuVO);
        }
        String skuListCacheKey= SeckillSkuServiceImpl.SECKILL_SKUS_PREFIX+spuId;
        clearCache(skuListCacheKey);
        setListCache(skuListCacheKey,skuVOS,null,null);
    }
}
