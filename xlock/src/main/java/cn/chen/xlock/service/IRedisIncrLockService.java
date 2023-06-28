package cn.chen.xlock.service;

public interface IRedisIncrLockService {

    void redisLockCD();

    void testRedission();
}
