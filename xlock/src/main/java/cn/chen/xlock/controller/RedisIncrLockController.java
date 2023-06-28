package cn.chen.xlock.controller;

import cn.chen.xlock.service.IRedisIncrLockService;
import cn.chen.xlock.service.impl.RedisIncrLockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class RedisIncrLockController {

    @Autowired
    private IRedisIncrLockService redisIncrLockService;

    @GetMapping("/incr")
    public void testRedisLock(){
        redisIncrLockService.redisLockCD();
    }
}
