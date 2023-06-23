package cn.tedu.mall.seckill.interceptor;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.pojo.seckill.dto.SeckillOrderAddDTO;
import cn.tedu.mall.seckill.service.impl.SeckillSkuServiceImpl;
import cn.tedu.mall.seckill.service.impl.SeckillSpuServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RandCodeInterceptor implements SeckillInterceptor{
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override public void seckillCommitCheck(SeckillOrderAddDTO seckillOrderAddDTO) {
        String randCodeKey= SeckillSpuServiceImpl.SECKILL_SPU_RAND_PREFIX+seckillOrderAddDTO.getSpuId();
        String randCode = (String) redisTemplate.opsForValue().get(randCodeKey);
        if (randCode == null){
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"秒杀路径不存在");
        }else if (!StringUtils.equals(randCode,seckillOrderAddDTO.getRandCode())){
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"秒杀路径不正确");
        }
    }
}
