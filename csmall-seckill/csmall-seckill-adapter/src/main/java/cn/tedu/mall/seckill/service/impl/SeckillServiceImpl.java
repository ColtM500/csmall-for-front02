/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.tedu.mall.seckill.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.pojo.domain.CsmallAuthenticationInfo;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.pojo.order.dto.OrderAddDTO;
import cn.tedu.mall.pojo.order.dto.OrderItemAddDTO;
import cn.tedu.mall.pojo.seckill.dto.SeckillOrderAddDTO;
import cn.tedu.mall.pojo.seckill.vo.SeckillCommitVO;
import cn.tedu.mall.seckill.interceptor.SeckillInterceptor;
import cn.tedu.mall.seckill.service.ISeckillService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SeckillServiceImpl implements ISeckillService {
    @Autowired
    private List<SeckillInterceptor> seckillInterceptors;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    /**
     * 秒杀提交.
     * 1.使用redis存储的库存,减库存
     * 2.发送消息到队列
     * 3.在消费者创建订单让用户支付
     */
    @Override
    public SeckillCommitVO commitSeckill(SeckillOrderAddDTO seckillOrderAddDTO) {
        //生成uuid
        String sn= UUID.randomUUID().toString();
        seckillOrderAddDTO.setSn(sn);
        seckillOrderAddDTO.setUserId(getUserInfo().getId());
        //拦截检测
        arroundCheck(seckillOrderAddDTO);
        //异步下单
        SendResult result = rocketMQTemplate.syncSend("seckill_order", seckillOrderAddDTO);
        if (result.getSendStatus().toString().equals("SEND_OK")) {
            SeckillCommitVO seckillCommitVO = new SeckillCommitVO();
            seckillCommitVO.setSn(sn);
            seckillCommitVO.setCreateTime(LocalDateTime.now());
            seckillCommitVO.setPayAmount(seckillOrderAddDTO.getAmountOfOriginalPrice());
            return seckillCommitVO;
        }

//        Long skuId = seckillOrderAddDTO.getSeckillOrderItemAddDTO().getSkuId();
//        //拿到userId
//        Long userId = getUserInfo().getId();
//        //先判断是否重复秒杀
//        String reSeckillKey = SeckillCacheUtils.getReseckillCheckKey(skuId, userId);
//        //如果重复秒杀返回值会大于1,第一次秒杀返回值是1
//        Long reSeckillTime = stringRedisTemplate.boundValueOps(reSeckillKey).increment();
//        //如果大于1,就是秒杀了多次
//        if (reSeckillTime > 1) {
//            throw new CoolSharkServiceException(ResponseCode.CONFLICT, "一个用户只能秒杀同一个商品一次");
//        }
//        //redis减库存的key值
//        String seckillSkuKey = SeckillCacheUtils.getStockKey(skuId);
//        //执行减库存,定时任务已经预先在redis存储该商品的stock库存，如果返回值大于等于0说明redis减库存成功
//        Long leftStock = stringRedisTemplate.boundValueOps(seckillSkuKey).decrement(1);
//        if (leftStock < 0) {
//            throw new CoolSharkServiceException(ResponseCode.NOT_ACCEPTABLE, "您秒杀的商品已经无货");
//        }
//        //库存扣成功,开始新增订单
//        //转化对象
//        OrderAddDTO orderAddDTO = convertSeckillOrderToOrder(seckillOrderAddDTO);
//        orderAddDTO.setUserId(userId);
//        //调用微服务写入数据 或者更快速处理可以发送消息，让订单系统消费处理
//        OrderAddVO orderAddVO = orderService.addOrder(orderAddDTO);
//        //异步执行success写入操作,我们封装Success对象传递数据,扣seckill库存
//        SeckillSuccess success = new SeckillSuccess();
//        //success: userId userPhone skuId mainPicture seckillPrice data order_sn title quantity barCode
//        //SeckillOrderItemDTO:skuId,mainPicture data barCode title quantity
//        BeanUtils.copyProperties(seckillOrderAddDTO.getSeckillOrderItemAddDTO(), success);
//        success.setSeckillPrice(seckillOrderAddDTO.getSeckillOrderItemAddDTO().getPrice());
//        success.setUserId(userId);
//        success.setOrderSn(orderAddVO.getSn());
//        rabbitTemplate.convertAndSend(RabbitMqComponentConfiguration.SECKILL_EX, RabbitMqComponentConfiguration.SECKILL_RK, success);
//        //转化返回对象
//        SeckillCommitVO seckillCommitVO = new SeckillCommitVO();
//        BeanUtils.copyProperties(orderAddVO, seckillCommitVO);
        return null;
    }

    private void arroundCheck(SeckillOrderAddDTO dto) {
        for (SeckillInterceptor seckillInterceptor : seckillInterceptors) {
            seckillInterceptor.seckillCommitCheck(dto);
        }
    }

    private OrderAddDTO convertSeckillOrderToOrder(SeckillOrderAddDTO seckillOrderAddDTO) {
        OrderAddDTO orderAddDTO = new OrderAddDTO();
        List<OrderItemAddDTO> orderItems = new ArrayList<>();
        OrderItemAddDTO orderItemAddDTO = new OrderItemAddDTO();
        orderAddDTO.setOrderItems(orderItems);
        //需要封装多个数据 订单sn userId,
        BeanUtils.copyProperties(seckillOrderAddDTO, orderAddDTO);
        BeanUtils.copyProperties(seckillOrderAddDTO.getSeckillOrderItemAddDTO(), orderItemAddDTO);
        orderItems.add(orderItemAddDTO);
        return orderAddDTO;
    }


    public CsmallAuthenticationInfo getUserInfo() {
        //从security环境获取username,先拿到authentication
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        //如果不是空的可以调用dubbo远程微服务获取adminVO
        if (authentication != null) {
            CsmallAuthenticationInfo csmallAuthenticationInfo = (CsmallAuthenticationInfo) authentication.getPrincipal();
            return csmallAuthenticationInfo;
        } else {
            throw new CoolSharkServiceException(ResponseCode.UNAUTHORIZED, "没有登录者信息");
        }
    }

    public Long getUserId() {
        CsmallAuthenticationInfo userInfo = getUserInfo();
        return userInfo.getId();
    }

}
