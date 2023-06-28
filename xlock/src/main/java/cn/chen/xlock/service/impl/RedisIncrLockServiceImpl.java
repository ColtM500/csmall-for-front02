package cn.chen.xlock.service.impl;

import cn.chen.xlock.service.IRedisIncrLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisIncrLockServiceImpl implements IRedisIncrLockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void redisLockCD() {
        //只要抢锁成功 就可以带上当前的过期时间
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", "111", 30, TimeUnit.SECONDS);
        if (lock) {
            Integer stock = Integer.valueOf(redisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int resultStock = stock - 1;
                redisTemplate.opsForValue().set("stock", resultStock + "");
                System.out.println("库存充足 抢购成功 库存剩余" + resultStock);
            } else {
                System.out.println("库存不足 抢购失败");
            }
            redisTemplate.delete("lock");//删除锁
        } else {
            redisLockCD();
        }
    }
}
