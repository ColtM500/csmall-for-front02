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
//package cn.tedu.mall.seckill.timer.config;
//
//import cn.tedu.mall.seckill.timer.jobs.SeckillBloomInitialJob;
//import cn.tedu.mall.seckill.timer.jobs.SeckillInitialJob;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.CronScheduleBuilder;
//import org.quartz.JobBuilder;
//import org.quartz.JobDetail;
//import org.quartz.Trigger;
//import org.quartz.TriggerBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
////@Configuration
//@Slf4j
//public class QuartzConfig {
//    @Bean
//    public JobDetail seckillInitialJobDetail() {
//        log.info("秒杀5分钟定时任务初始化Job");
//        return JobBuilder.newJob(SeckillInitialJob.class)//PrintTimeJob我们的业务类
//            .withIdentity("seckillInitialJob")//可以给该JobDetail起一个id
//            //每个JobDetail内都有一个Map，包含了关联到这个Job的数据，在Job类中可以通过context获取
//            .usingJobData("name", "internal 5 mins")//关联键值对
//            .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
//            .build();
//    }
//
//    @Bean
//    public Trigger seckillInitialTrigger() {
//        log.info("秒杀5分钟定时任务初始化Trigger,每分钟0秒执行一次");
//        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/1 * * * ? *");
//        return TriggerBuilder.newTrigger()
//            .forJob(seckillInitialJobDetail())//关联上述的JobDetail
//            .withIdentity("seckillInitialTrigger")//给Trigger起个名字
//            .withSchedule(cronScheduleBuilder)
//            .build();
//    }
//
//    @Bean
//    public JobDetail seckillBloomFilterInitialJobDetail() {
//        log.info("秒杀防止缓存穿透布隆过滤器定时Job");
//        return JobBuilder.newJob(SeckillBloomInitialJob.class)//PrintTimeJob我们的业务类
//            .withIdentity("seckillBloomInitial")//可以给该JobDetail起一个id
//            //每个JobDetail内都有一个Map，包含了关联到这个Job的数据，在Job类中可以通过context获取
//            .usingJobData("name", "bloom")//关联键值对
//            .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
//            .build();
//    }
//
//    @Bean
//    public Trigger seckillBloomFilterInitialTrigger() {
//        log.info("秒杀防止缓存穿透布隆过滤器定时Trigger,每天12点检查一次,23点50确认一次");
//        //CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 50 11,23 * * ? *");
//        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/1 * * * ? *");
//        return TriggerBuilder.newTrigger()
//            .forJob(seckillBloomFilterInitialJobDetail())//关联上述的JobDetail
//            .withIdentity("seckillBloomFilterInitialTrigger")//给Trigger起个名字
//            .withSchedule(cronScheduleBuilder)
//            .build();
//    }
//}