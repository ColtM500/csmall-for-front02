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
package cn.tedu.mall.order.controller;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.order.service.IOmsOrderService;
import cn.tedu.mall.pojo.order.dto.OrderAddDTO;
import cn.tedu.mall.pojo.order.dto.OrderListTimeDTO;
import cn.tedu.mall.pojo.order.dto.OrderStateUpdateDTO;
import cn.tedu.mall.pojo.order.vo.OrderAddVO;
import cn.tedu.mall.pojo.order.vo.OrderDetailVO;
import cn.tedu.mall.pojo.order.vo.OrderListVO;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单数据表 前端控制器
 * </p>
 *
 * @since 2022-02-16
 */
@RestController
@RequestMapping("/oms/order")
@Api(tags = "订单功能")
public class OmsOrderController {
    @Autowired
    private IOmsOrderService orderService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 异步下单,消费调用订单新增
     * @param orderAddDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("新增一张订单")
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult<OrderAddVO> addOrder(OrderAddDTO orderAddDTO) {
        //生成sn
        String sn= UUID.randomUUID().toString();
        orderAddDTO.setSn(sn);
        //不生成订单,只异步发送
        String msg = JSON.toJSONString(orderAddDTO);
        rocketMQTemplate.convertAndSend("order-add-topic",msg);
        OrderAddVO orderAddVO=new OrderAddVO();
        orderAddVO.setSn(sn);
        orderAddVO.setCreateTime(LocalDateTime.now());
        return JsonResult.ok(orderAddVO);
    }

    @PostMapping("/update/state")
    @ApiOperation("更新订单状态")
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult updateOrderState(OrderStateUpdateDTO orderStateUpdateDTO) {
        orderService.updateOrderState(orderStateUpdateDTO);
        return JsonResult.ok();
    }

    /**
     * 根据用户id查询订单列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询当前用户订单列表")
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult<JsonPage<OrderListVO>> listUserOrders(OrderListTimeDTO orderListTimeDTO) {
        JsonPage<OrderListVO> orderVOJsonPage = orderService.listOrdersBetweenTimes(orderListTimeDTO);
        return JsonResult.ok(orderVOJsonPage);
    }
    @ApiOperation(value = "利用订单id查询订单")
    @GetMapping("/detail")
    @ApiImplicitParams({
        @ApiImplicitParam(value = "订单编号", name = "sn", required = true, example = "sn")
    })
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult<OrderDetailVO> getOrderDetail(Long id) {
        OrderDetailVO orderDetailVO = orderService.getOrderDetail(id);
        return JsonResult.ok(orderDetailVO);
    }
}
