package cn.tedu.mall.seckill.interceptor;

import cn.tedu.mall.pojo.seckill.dto.SeckillOrderAddDTO;
import cn.tedu.mall.seckill.service.impl.SeckillSkuServiceImpl;
import cn.tedu.mall.seckill.utils.RedisBloomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
@Slf4j
public class BloomInterceptor implements SeckillInterceptor{
    @Autowired
    private RedisBloomUtils bloomUtils;
    @Override public void seckillCommitCheck(SeckillOrderAddDTO seckillOrderAddDTO) {
        Boolean bfexists = bloomUtils.bfexists(
            SeckillSkuServiceImpl.SECKILL_SKU_BLOOM_KEY,
            seckillOrderAddDTO.getSeckillOrderItemAddDTO().getSkuId().toString());
        if (!bfexists){
            log.error("您所秒杀的商品,不存在,skuId:{}",seckillOrderAddDTO.getSeckillOrderItemAddDTO().getSkuId());
        }
    }
}
