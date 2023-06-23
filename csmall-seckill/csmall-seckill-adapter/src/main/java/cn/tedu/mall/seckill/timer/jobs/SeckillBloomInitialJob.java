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
//import cn.tedu.mall.seckill.mapper.SeckillSpuMapper;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Slf4j
//public class SeckillBloomInitialJob implements Job {
//
//    @Autowired
//    private SeckillSpuMapper seckillSpuMapper;
//
//    /**
//     * 该定时任务会生成每天不同批次的秒杀商品id的布隆过滤器,并且将商品写入到布隆过滤器中
//     * 布隆过滤器key值
//     * @param jobExecutionContext
//     * @throws JobExecutionException
//     */
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        //测试方法
//        allSpuIdsBloomFilter();
//        //生产方法
//        //spuIdsBloomFilter();
//    }
//
//    /**
//     * 测试方法
//     * 将当前seckill表格中所有的spuId 存放到布隆过滤器
//     * 这样在测试时就无需调整数据库的商品秒杀起始时间了
//     */
//    public void allSpuIdsBloomFilter() {
//        //遵循业务逻辑,存放两批布隆过滤器数据;一个是今天的spuIds,一个是明天的spuIds;
//        String bloomTodayKey = SeckillCacheUtils.getBloomFilterKey(LocalDate.now());
//        String bloomTomorrowKey = SeckillCacheUtils.getBloomFilterKey(LocalDate.now().plusDays(1));
//        //从seckill数据库查询所有spuIds;
//        Long[] spuIds = seckillSpuMapper.selectAllSeckillSpuIds();
//        String[] spuIdStrs = new String[spuIds.length];
//        for (int i = 0; i < spuIds.length; i++) {
//            spuIdStrs[i] = spuIds[i] + "";
//        }
//        redisBloomUtils.bfmadd(bloomTodayKey, spuIdStrs);
//        redisBloomUtils.bfmadd(bloomTomorrowKey, spuIdStrs);
//        log.info("已经将所有spuId放入布隆过滤器,当前测试不会拦截任何表中已有的spu");
//    }
//
//    /**
//     * 生产方法
//     * 和测试方法相反,这里会利用当前日期,生成
//     */
//    public void spuIdsBloomFilter() {
//        /*
//        将布隆过滤器存放今天的数据
//         */
//        //获取今日最早0点和最晚24点
//        LocalDateTime today = LocalDateTime.now();
//        LocalDateTime todayMin = LocalDateTime.of(today.toLocalDate(), LocalTime.MIN);
//        LocalDateTime todayMax = LocalDateTime.of(today.toLocalDate(), LocalTime.MAX);
//        log.info("今日最早时间点:{},最晚时间点:{}", todayMin.toLocalTime(), todayMax.toLocalTime());
//        //生成今日布隆过滤器key
//        String bloomTodayKey = SeckillCacheUtils.getBloomFilterKey(LocalDate.now());
//        Boolean hasBloomTodayFilter = redisBloomUtils.hasBloomFilter(bloomTodayKey);
//        if (!hasBloomTodayFilter) {
//            //没有布隆过滤器,初始化一下
//            redisBloomUtils.bfreserve(bloomTodayKey, 0.003, 10000);
//            log.info("创建了布隆过滤器:{}", bloomTodayKey);
//            //读取所有今日内秒杀的spuId 放入布隆过滤器
//            Long[] spuIds = seckillSpuMapper.selectSpuIdsInOneDay(todayMin, todayMax);
//            String[] spuIdStrs = new String[spuIds.length];
//            for (int i = 0; i < spuIds.length; i++) {
//                spuIdStrs[i] = spuIds[i] + "";
//            }
//            redisBloomUtils.bfmadd(bloomTodayKey, spuIdStrs);
//        }
//        //获取明天最早0点和最晚24点
//        LocalDateTime tomorrowMin = LocalDateTime.of(today.plusDays(1).toLocalDate(), LocalTime.MIN);
//        LocalDateTime tomorrowMax = LocalDateTime.of(today.plusDays(1).toLocalDate(), LocalTime.MAX);
//        log.info("明天最早时间点:{},最晚时间点:{}", tomorrowMin.toLocalTime(), tomorrowMax.toLocalTime());
//        //拿到明天布隆过滤器key值
//        String bloomTomorrowKey = SeckillCacheUtils.getBloomFilterKey(LocalDate.now().plusDays(1));
//        //判断存在
//        Boolean hasBloomTomorrowFilter = redisBloomUtils.hasBloomFilter(bloomTomorrowKey);
//        if (!hasBloomTomorrowFilter) {
//            redisBloomUtils.bfreserve(bloomTomorrowKey, 0.003, 10000);
//            log.info("创建了布隆过滤器:{}", bloomTomorrowKey);
//            //读取所有今日内秒杀的spuId 放入布隆过滤器
//            Long[] spuIds = seckillSpuMapper.selectSpuIdsInOneDay(tomorrowMin, tomorrowMax);
//            String[] spuIdStrs = new String[spuIds.length];
//            for (int i = 0; i < spuIds.length; i++) {
//                spuIdStrs[i] = spuIds[i] + "";
//            }
//            redisBloomUtils.bfmadd(bloomTomorrowKey, spuIdStrs);
//        }
//    }
//}
