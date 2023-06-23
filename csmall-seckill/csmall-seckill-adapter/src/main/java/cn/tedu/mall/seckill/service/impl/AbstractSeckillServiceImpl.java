package cn.tedu.mall.seckill.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class AbstractSeckillServiceImpl<T>{
    @Autowired
    protected RedisTemplate redisTemplate;
    public T getCache(String key){
        T t = null;
        if (redisTemplate.hasKey(key)) {
            log.debug("缓存命中数据，key：{}",key);
            t = (T) redisTemplate.boundValueOps(key).get();
        }
        return t;
    }
    public List<T> getListCache(String key){
        ListOperations operations = redisTemplate.opsForList();
        if (redisTemplate.hasKey(key)) {
            log.debug("缓存命中数据,key：{}",key);
            return operations.range(key,0,-1);
        }
        return null;
    }
    public void setListCache(String key,List<T> listT,Long time, TimeUnit timeUnit){
        ListOperations operations = redisTemplate.opsForList();
        if (listT == null){
            log.error("缓存数组数据为空");
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"缓存数组数据为空");
        }
        if (time != null && timeUnit != null ) {
            operations.leftPushAll(key,listT);
            redisTemplate.expire(key,time,timeUnit);
            return;
        }
        operations.leftPushAll(key,listT);
    }
    public void setCache(String key,T t,Long time,TimeUnit timeUnit){
        log.debug("即将存储缓存，key:{}",key);
        redisTemplate.boundValueOps(key).set(t);
        if (time != null && timeUnit != null) {
            redisTemplate.expire(key,time,timeUnit);
        }
    }
    public void setCache(String key,T t){
        this.setCache(key,t,null,null);
    }
    //获取单个数据
    //获取多个数据
    //存储单个数据
    //存储多个数据
    public void clearCache(String key){
        redisTemplate.delete(key);
    }
}
