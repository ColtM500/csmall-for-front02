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
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.pojo.seckill.dto.SeckillOrderItemAddDTO;
import cn.tedu.mall.pojo.seckill.model.SeckillStockLog;
import cn.tedu.mall.pojo.seckill.vo.SeckillSkuVO;
import cn.tedu.mall.seckill.mapper.SeckillSkuMapper;
import cn.tedu.mall.seckill.service.ISeckillSkuService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @since 2022-02-23
 */

@DubboService
@Service
@Slf4j
public class SeckillSkuServiceImpl extends AbstractSeckillServiceImpl<SeckillSkuVO> implements ISeckillSkuService {
    public static final String SECKILL_SKU_BLOOM_KEY="seckill:sku:bloom";
    public static final String SECKILL_SKUS_PREFIX = "seckill:skus:";
    public static final String SECKILL_SKU_STOCK_PREFIX="seckill:sku:stock:";
    public static final String SECKILL_RESEC_SKU_PREFIX="seckill:resec:sku:";
    @Autowired
    private SeckillSkuMapper seckillSkuMapper;
    /**
     * 只从缓存获取数据
     * @param spuId
     * @return
     */
    @Override
    public List<SeckillSkuVO> listSeckillSkus(Long spuId) {
        //TODO 布隆
        List<SeckillSkuVO> cache = getListCache(SECKILL_SKUS_PREFIX + spuId);
        if (cache == null || cache.size()==0) {
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"没有秒杀商品");
        }
        return cache;
    }

    @Override public void redusSeckillSkuCount(SeckillOrderItemAddDTO dto, String sn) {
        Integer quantity = dto.getQuantity();
        Long skuId= dto.getSkuId();
        seckillSkuMapper.decrSkuStock(skuId,quantity);
        seckillSkuMapper.insertStockLog(sn,skuId,quantity);
    }

    @Override public void returnStock(String sn) {
        SeckillStockLog stockLog=seckillSkuMapper.selectStockLogBySn(sn);
        seckillSkuMapper.incrSkuStock(stockLog.getSkuId(),stockLog.getQuantity());
    }
}
