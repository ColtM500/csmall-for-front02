package cn.tedu.csmall.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class FrontCacheServiceImpl<T> {
    @Autowired
    protected RedisTemplate redisTemplate;

    //单个对象存储 获取
    public T getCache(String key) {
        T t = null;
        //打个桩 exists key
        if (redisTemplate.hasKey(key)) {
            log.debug("命中缓存,key:{}", key);
            t = (T) redisTemplate.opsForValue().get(key);
        }
        return t;
    }

    ;

    public void setCache(String key, T t) {
        //内部私有方法 方法重载 方便外界调用
        this.setCache(key, t, null, null);
    }

    public void setCache(String key, T t, Long expiredTime, TimeUnit timeUnit) {
        log.debug("即将存储缓存, key:{}", key);
        //根据传递参数expiredTime 和 timeUnit 都是空的话 永久数据 都不是空 设置超时条件
        if (expiredTime != null && timeUnit != null) {
            redisTemplate.opsForValue().set(key, t, expiredTime, timeUnit);
        } else if (expiredTime != null && timeUnit == null) {
            log.error("超时时间数据 不规范");
        } else if (expiredTime == null && timeUnit != null) {
            log.error("超时时间数据 不规范");
        } else {
            redisTemplate.opsForValue().set();
        }
    }

    //list对象存储和获取
    //TODO: 分布式锁 tryLock
    //TODO: 分布式锁 释放锁
}
