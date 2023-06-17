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
package cn.tedu.mall.front.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontSkuService;
import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
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
 * 前台sku相关业务功能
 */
@RestController
@RequestMapping("/front/sku")
@Api(tags = "前台商品SKU模块")
public class FrontSkuController {
    @Autowired
    private IFrontSkuService frontSkuService;
    /**
     * 根据spu_id查询sku列表 Used
     */
    @GetMapping("/{spuId}")
    @ApiImplicitParams(
        @ApiImplicitParam(name = "spuId", value = "spu id", paramType = "path", required = true, dataType = "long")
    )
    @ApiOperation(value = "前台在SPU列表中根据SPUID查询所有SKU")
    public JsonResult<List<SkuStandardVO>> getFrontSkusBySpuId(@PathVariable(name = "spuId") Long spuId) {
        List<SkuStandardVO> frontSkus = frontSkuService.getSkus(spuId);
        return JsonResult.ok(frontSkus);
    }
}
