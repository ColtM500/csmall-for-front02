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
package cn.tedu.mall.order.mapper;

import cn.tedu.mall.pojo.order.model.OmsCart;
import cn.tedu.mall.pojo.order.vo.CartStandardVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p> 购物车数据表 Mapper 接口</p>
 *
 * @since 2022-02-16
 */
public interface OmsCartMapper {
    void saveCart(OmsCart omsCart);

    List<CartStandardVO> selectCartsByUserId(@Param("userId") Long userId);

    OmsCart selectExistCart(@Param("userId") Long userId, @Param("skuId") Long skuId);

    void updateQuantityById(OmsCart omsCartExist);

    /*int deleteSkuFromCartBySkuIdAndUserId(@Param("skuId")Long skuId, @Param("userId")Long userId);*/

    int deleteCartsByUserId(Long userId);

    /*int deleteCartsByUserIdAndSkuIds(@Param("skuIds")Long[] skuIds, @Param("userId")Long userId);*/

    int deleteCartsByIds(Long[] ids);

    void deleteCartBySkuIdAndUserId(OmsCart omsCart);
}
