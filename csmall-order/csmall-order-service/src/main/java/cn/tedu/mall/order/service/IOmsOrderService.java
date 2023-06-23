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
package cn.tedu.mall.order.service;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.pojo.order.dto.OrderAddDTO;
import cn.tedu.mall.pojo.order.dto.OrderListTimeDTO;
import cn.tedu.mall.pojo.order.dto.OrderStateUpdateDTO;
import cn.tedu.mall.pojo.order.vo.OrderAddVO;
import cn.tedu.mall.pojo.order.vo.OrderDetailVO;
import cn.tedu.mall.pojo.order.vo.OrderListVO;

/**
 * <p>
 * 订单数据表 服务类
 * </p>
 *
 * @since 2022-02-16
 */
public interface IOmsOrderService {

    /**
     * 更新订单状态
     *
     * @param orderStateUpdateDTO
     */
    void updateOrderState(OrderStateUpdateDTO orderStateUpdateDTO);

    /**
     * 根据起始结束时间查询订单列表
     *
     * @param orderListTimeDTO
     */
    JsonPage<OrderListVO> listOrdersBetweenTimes(OrderListTimeDTO orderListTimeDTO);

    /**
     * 根据sn查询订单详细信息
     *
     * @param id
     * @return
     */
    OrderDetailVO getOrderDetail(Long id);
}
