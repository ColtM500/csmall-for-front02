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
package cn.tedu.mall.seckill.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.pojo.seckill.vo.SeckillSkuVO;
import cn.tedu.mall.seckill.service.ISeckillSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *

 */
@RestController
@RequestMapping("/seckill/sku")
@Api(tags = "秒杀SKU模块")
public class SeckillSkuController {
    @Autowired
    private ISeckillSkuService seckillSkuService;

    /**
     * 根据spuId查询秒杀sku列表数据
     */
    @GetMapping("/list/{spuId}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "spuId", value = "SPU id", paramType = "path", required = true, dataType = "int")
    })
    @ApiOperation(value = "根据SPUID查询秒杀SKU列表")
    public JsonResult<List<SeckillSkuVO>> listSeckillSkus(@PathVariable("spuId") Long spuId) {
        List<SeckillSkuVO> seckillSkuVOs = seckillSkuService.listSeckillSkus(spuId);
        return JsonResult.ok(seckillSkuVOs);
    }
}
