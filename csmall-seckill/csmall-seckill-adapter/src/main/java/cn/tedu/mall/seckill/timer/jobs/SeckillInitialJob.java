///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package cn.tedu.mall.seckill.timer.jobs;
//
//import cn.tedu.mall.common.pojo.seckill.model.SeckillSku;
//import cn.tedu.mall.common.pojo.seckill.model.SeckillSpu;
//import cn.tedu.mall.seckill.mapper.SeckillSkuMapper;
//import cn.tedu.mall.seckill.mapper.SeckillSpuMapper;
//import cn.tedu.mall.seckill.utils.SeckillCacheUtils;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.math.RandomUtils;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//
//@Slf4j
//public class SeckillInitialJob implements Job {
//    //存储对象
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    @Autowired
//    private SeckillSkuMapper seckillSkuMapper;
//    @Autowired
//    private SeckillSpuMapper seckillSpuMapper;
//    //准备一个表示交换机还没有创建的参数flag
//    private Boolean exInit = false;
//
//    /*
//    定时任务,初始化redis库存
//     */
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        try {
//            //设置5分钟后时间点
//            LocalDateTime time = LocalDateTime.now();
//            time.plusMinutes(5);
//            List<SeckillSpu> seckillSpus = seckillSpuMapper.selectSeckillSpusWithinTime(time);
//            //我们找到了所有距离开始时间不足5分钟的spu商品
//            for (SeckillSpu seckillSpu : seckillSpus) {
//                Long spuId = seckillSpu.getSpuId();
//                List<SeckillSku> seckillSkus = seckillSkuMapper.selectSeckillSkusBySpuId(spuId);
//                //我们要为每一个sku商品创建库存redis
//                for (SeckillSku seckillSku : seckillSkus) {
//                    log.info("开始为" + seckillSku.getSkuId() + "生成redis库存和队列");
//                    String seckillSkuStockKey = SeckillCacheUtils.getStockKey(seckillSku.getSkuId());
//                    if (!redisTemplate.hasKey(seckillSkuStockKey)) {
//                        stringRedisTemplate.boundValueOps(seckillSkuStockKey).set(seckillSku.getSeckillStock() + "", 1, TimeUnit.DAYS);
//                        log.info("库存不存在,创建库存:{}", seckillSkuStockKey);
//                    }
//
//                }
//                String randCodeKey = SeckillCacheUtils.getRandCodeKey(spuId);
//                if (!redisTemplate.hasKey(randCodeKey)) {
//                    log.info("当前spu随机code不存在,所以创建一个新的:{}", randCodeKey);
//                    int randCode = RandomUtils.nextInt(900000) + 100000;
//                    redisTemplate.boundValueOps(randCodeKey).set(randCode + "", 1, TimeUnit.DAYS);
//                }
//            }
//        } catch (Exception e) {
//            log.info("异常:", e);
//        }
//    }
//}
