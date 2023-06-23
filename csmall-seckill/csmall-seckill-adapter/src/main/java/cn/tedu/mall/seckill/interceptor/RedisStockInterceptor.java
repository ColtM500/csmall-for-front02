package cn.tedu.mall.seckill.interceptor;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.pojo.seckill.dto.SeckillOrderAddDTO;
import cn.tedu.mall.seckill.service.impl.SeckillSkuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class RedisStockInterceptor implements SeckillInterceptor{
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override public void seckillCommitCheck(SeckillOrderAddDTO seckillOrderAddDTO) {
        String stockKey= SeckillSkuServiceImpl.SECKILL_SKU_STOCK_PREFIX+seckillOrderAddDTO.getSeckillOrderItemAddDTO().getSkuId();
        Long stock = redisTemplate.opsForValue().decrement(stockKey);
        if (stock<0){
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"秒杀商品已售罄");
        }
    }
}
