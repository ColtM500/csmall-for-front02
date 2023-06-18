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
package cn.tedu.mall.order.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.pojo.domain.CsmallAuthenticationInfo;
import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.order.mapper.OmsOrderItemMapper;
import cn.tedu.mall.order.mapper.OmsOrderMapper;
import cn.tedu.mall.order.service.IOmsCartService;
import cn.tedu.mall.order.service.IOmsOrderService;
import cn.tedu.mall.pojo.order.dto.OrderAddDTO;
import cn.tedu.mall.pojo.order.dto.OrderItemAddDTO;
import cn.tedu.mall.pojo.order.dto.OrderListTimeDTO;
import cn.tedu.mall.pojo.order.dto.OrderStateUpdateDTO;
import cn.tedu.mall.pojo.order.model.OmsCart;
import cn.tedu.mall.pojo.order.model.OmsOrder;
import cn.tedu.mall.pojo.order.model.OmsOrderItem;
import cn.tedu.mall.pojo.order.vo.OrderAddVO;
import cn.tedu.mall.pojo.order.vo.OrderDetailVO;
import cn.tedu.mall.pojo.order.vo.OrderListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * 订单数据表 服务实现类
 * </p>
 *
 * @since 2022-02-16
 */
@DubboService
@Slf4j
public class OmsOrderServiceImpl implements IOmsOrderService {
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 注意实现方法的幂等效果,只要调用,sn重复就无法下单方法就结束了
     * @param orderAddDTO
     * @return
     */
    @Override
    public void addOrder(OrderAddDTO orderAddDTO) {
        String sn = orderAddDTO.getSn();
        //利用sn查询是否存在订单
        boolean existsOrder=orderMapper.selectExistBySn(sn);
        if (existsOrder){
            log.error( "订单已经存在,sn:{},重复下单",sn);
            throw new CoolSharkServiceException(ResponseCode.CONFLICT,"订单已经存在");
        }
        //订单不存在,消息事务
        //发送半消息事务
        Message<String> message= MessageBuilder.withPayload(sn).build();
        rocketMQTemplate.sendMessageInTransaction("tx-order-add-topic",message,orderAddDTO);
    }

    @Override
    public void updateOrderState(OrderStateUpdateDTO orderStateUpdateDTO) {
//        OmsOrder omsOrder = new OmsOrder();
//        BeanUtils.copyProperties(orderStateUpdateDTO, omsOrder);
//        orderMapper.updateOrderById(omsOrder);
    }

    @Override
    public JsonPage<OrderListVO> listOrdersBetweenTimes(OrderListTimeDTO orderListTimeDTO) {
//        //对参数的日期时间做一下格式化,有错误抛异常,没错误校验,订正添加默认时间
//        formatTimeValidAndReplenish(orderListTimeDTO);
//        //获取userId
//        Long userId = getUserId();
//        orderListTimeDTO.setUserId(userId);
//        //开启分页查询
//        PageHelper.startPage(orderListTimeDTO.getPage(), orderListTimeDTO.getPageSize());
//        List<OrderListVO> orders = orderMapper.selectOrdersBetweenTimes(orderListTimeDTO);
//        return JsonPage.restPage(new PageInfo<>(orders));
        return null;
    }

    @Override
    public OrderDetailVO getOrderDetail(Long id) {
//        OrderDetailVO orderDetailVO = orderMapper.selectOrderById(id);
//        return orderDetailVO;
        return null;
    }

    private void formatTimeValidAndReplenish(OrderListTimeDTO orderListTimeDTO) {
        //检查时间是否满足startTime<endTime
        LocalDateTime start = orderListTimeDTO.getStartTime();
        LocalDateTime end = orderListTimeDTO.getEndTime();
        if (start != null && end != null) {
            //起始时间必须小于结束时间
            if (start.toInstant(ZoneOffset.of("+8")).toEpochMilli() > end.toInstant(ZoneOffset.of("+8")).toEpochMilli()) {
                throw new CoolSharkServiceException(ResponseCode.CONFLICT, "查询订单时间段不正确,起始时间应该早于结束时间");
            }
        }
        //补充非空
        if (start == null || end == null) {
            //填写默认值,最近一个月订单情况
            end = LocalDateTime.now();
            start = end.minusMonths(1);
            orderListTimeDTO.setStartTime(start);
            orderListTimeDTO.setEndTime(end);
        }
    }

    public void replenishOrderItem(OmsOrderItem omsOrderItem) {
//        if (omsOrderItem.getId() == null) {
//            Long id = segmentService.getId("order_item").getId();
//            omsOrderItem.setId(id);
//        }
//        if (omsOrderItem.getSkuId() == null) {
//            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST, "新增订单商品SKUID不能为空");
//        }
    }

    public void replenishOrder(OmsOrder omsOrder) {
//        //补充orderId,这里还是需要校验的,因为spring-boot-starter-validation只局限在控制层校验
//        if (omsOrder.getId() == null) {
//            //Long id = IdGeneratorUtils.getDistributeId("order");
//            Long id = segmentService.getId("order").getId();
//            omsOrder.setId(id);
//        }
//        //补充userId
//        if (omsOrder.getUserId() == null) {
//            Long userId = getUserId();
//            omsOrder.setUserId(userId);
//        }
//        //生成sn
//        if (omsOrder.getSn() == null) {
//            omsOrder.setSn(UUID.randomUUID().toString());//暂定使用uuid填补sn订单编号
//        }
//        //默认状态未支付
//        if (omsOrder.getState() == null) {
//            omsOrder.setState(0);
//        }
//        //创建时间,后续时间以创建时间为准
//        if (omsOrder.getGmtCreate() == null) {
//            omsOrder.setGmtCreate(LocalDateTime.now());
//        }
//        if (omsOrder.getGmtModified() == null) {
//            omsOrder.setGmtModified(omsOrder.getGmtCreate());
//        }
//        if (omsOrder.getGmtOrder() == null) {
//            omsOrder.setGmtModified(omsOrder.getGmtCreate());
//        }
//        //金额调整,为null的金额 默认是0
//        if (omsOrder.getAmountOfDiscount() == null) {
//            omsOrder.setAmountOfDiscount(new BigDecimal(0.0));
//        }
//        if (omsOrder.getAmountOfFreight() == null) {
//            omsOrder.setAmountOfFreight(new BigDecimal(0.0));
//        }
//        if (omsOrder.getAmountOfOriginalPrice() == null) {
//            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST, "请前端利用商品价钱,数量计算商品总价");
//        }
//        if (omsOrder.getAmountOfActualPay() == null) {
//            log.debug("前端没有计算实际支付价钱,需要后台计算");
//            //拿到商品总价
//            BigDecimal originalPrice = omsOrder.getAmountOfOriginalPrice();
//            //拿到优惠价格和运费
//            BigDecimal disCount = omsOrder.getAmountOfDiscount();
//            BigDecimal freight = omsOrder.getAmountOfFreight();
//            //商品原价减去优惠,加上运费
//            BigDecimal actualPay = originalPrice.subtract(disCount).add(freight);
//            omsOrder.setAmountOfActualPay(actualPay);
//        }
    }

    public CsmallAuthenticationInfo getUserInfo() {
        //从security环境获取username,先拿到authentication
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        //如果不是空的可以调用dubbo远程微服务获取adminVO
        if (authentication != null) {
            CsmallAuthenticationInfo csmallAuthenticationInfo = (CsmallAuthenticationInfo) authentication.getCredentials();
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