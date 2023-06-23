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
package cn.tedu.mall.seckill.mapper;


import cn.tedu.mall.pojo.seckill.model.SeckillSku;
import cn.tedu.mall.pojo.seckill.model.SeckillStockLog;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>  Mapper 接口</p>
 *
 * @since 2022-02-23
 */
public interface SeckillSkuMapper {
    List<SeckillSku> selectSeckillSkusBySpuId(Long spuId);

    void updateStockBySkuId(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);
    @Update("update seckill_sku set stock=stock-#{quantity} where id=#{id} and stock>=#{quantity}")
    void decrSkuStock(@Param("id")Long id, @Param("quantity")Integer quantity);
    @Insert("insert into seckill_stock_log (sku_id,order_sn,quantity,gmt_create,gmt_modified) values (#{skuId},#{orderSN},#{quantity},now(),now())")
    void insertStockLog(@Param("orderSN")String sn, @Param("skuId")Long id, @Param("quantity") Integer quantity);
    @Update("update seckill_sku set stock=stock+#{quantity} where id=#{skuId}")
    void incrSkuStock(@Param("skuId")Long id, @Param("quantity") Integer quantit);
    @Select("select * from seckill_stock_log where order_sn=#{sn}")
    SeckillStockLog selectStockLogBySn(String sn);
}
