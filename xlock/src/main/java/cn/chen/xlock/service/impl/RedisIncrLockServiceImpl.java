package cn.chen.xlock.service.impl;

import cn.chen.xlock.service.IRedisIncrLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisIncrLockServiceImpl implements IRedisIncrLockService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void redisLockCD() {
        String threadUuid = UUID.randomUUID().toString();
        //只要抢锁成功 就可以带上当前的过期时间 死锁：没有绑定过期时间
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", threadUuid, 30, TimeUnit.SECONDS);

        if (lock) {
            Integer stock = Integer.valueOf(redisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int resultStock = stock - 1;
                redisTemplate.opsForValue().set("stock", resultStock + "");
                System.out.println("库存充足 抢购成功 库存剩余" + resultStock);
            } else {
                System.out.println("库存不足 抢购失败");
            }

            String lua = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            redisTemplate.execute(new DefaultRedisScript<Long>(lua, Long.class), Arrays.asList("lock"), threadUuid);

//            String redisUuid = redisTemplate.opsForValue().get("stock");
//            if (threadUuid.equals(redisUuid)) {
//                redisTemplate.delete("lock");//删除锁
//            }
        } else {
            redisLockCD();
        }

    }

    @Override
    public void testRedission() {
        synchronized (RedisIncrLockServiceImpl.class){//外面的门
            System.out.println("我进来了");
            synchronized (RedisIncrLockServiceImpl.class){//内部的门
                System.out.println("我又进来了");
            }
        }

    }
}
