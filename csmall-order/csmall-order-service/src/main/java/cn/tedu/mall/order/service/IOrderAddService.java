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

import cn.tedu.mall.pojo.order.dto.OrderAddCondition;

/**
 * <p>
 * 订单数据表 服务类
 * </p>
 *
 * @since 2022-02-16
 */
public interface IOrderAddService {
    /**
     * 新增订单
     *
     * @param OrderAddCondition
     * @return 订单编号
     */
    void addOrder(OrderAddCondition orderAddCondition);
}
