package cn.tedu.mall.seckill.interceptor;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.pojo.seckill.dto.SeckillOrderAddDTO;
import cn.tedu.mall.seckill.service.impl.SeckillSkuServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@Slf4j
public class ReSeckillInterceptor implements SeckillInterceptor{
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override public void seckillCommitCheck(SeckillOrderAddDTO seckillOrderAddDTO) {
        String reSecKey= SeckillSkuServiceImpl.SECKILL_RESEC_SKU_PREFIX+seckillOrderAddDTO.getUserId()+":"+seckillOrderAddDTO.getSeckillOrderItemAddDTO().getSkuId();
        Long increment = redisTemplate.opsForValue().increment(reSecKey);
        if (increment>1){
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"重复秒杀");
        }
    }
}
