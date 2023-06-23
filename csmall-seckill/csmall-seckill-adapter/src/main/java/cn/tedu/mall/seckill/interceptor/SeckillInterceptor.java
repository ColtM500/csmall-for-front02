package cn.tedu.mall.seckill.interceptor;

import cn.tedu.mall.pojo.seckill.dto.SeckillOrderAddDTO;

public interface SeckillInterceptor {
    public void seckillCommitCheck(SeckillOrderAddDTO seckillOrderAddDTO);
}
