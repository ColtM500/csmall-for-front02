package cn.tedu.csmall.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * 存储缓存对象,
 * 使用泛型T 定义缓存的类型
 * 所有缓存的功能,集中使用这个cache类来解决
 * 使用缓存功能的实现业务类,只需要继承,定义泛型类型就可以了
 *
 * @param <T>
 */
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
            redisTemplate.opsForValue().set(key, t);
        }
    }

    //商品数据相关分布式锁 tryLock()
    public boolean tryLock(String lockKey, String randCode) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //setnx setIfAbsent
        return valueOperations.setIfAbsent(lockKey, randCode, 10, TimeUnit.SECONDS);
    }

    //释放锁
    public void releaseLock(String lockKey, String randCode) {
        //存在则判断randCode是否和锁的值相等 不存在或者不相等 不执行del
        if (redisTemplate.hasKey(lockKey)) {//锁是否存在
            //判断randCode是否为空值和存储的锁的值是否相等
            if (randCode != null && randCode.equals(redisTemplate.opsForValue().get(lockKey))) {
                redisTemplate.delete(lockKey);
            }
        }
    }

}
