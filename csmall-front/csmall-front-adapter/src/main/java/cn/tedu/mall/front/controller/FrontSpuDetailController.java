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
import cn.tedu.mall.front.service.IFrontSpuDetailService;
import cn.tedu.mall.pojo.product.vo.SpuDetailStandardVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询spu商品详细信息
 */
@RestController
@RequestMapping("/front/spu/detail")
@Api(tags = "前台spu详情查询")
public class FrontSpuDetailController {
    @Autowired
    private IFrontSpuDetailService spuDetailService;
    /**
     * 根据spuid查询spu detail Used
     */
    @GetMapping("/{spuId}")
    @ApiOperation(value = "前台根据SPUID查询SPU DETAILS详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "spuId", value = "spu id", paramType = "path", required = true, dataType = "long")
    })
    public JsonResult<SpuDetailStandardVO> getSpuDetail(@PathVariable("spuId") Long spuId) {
        SpuDetailStandardVO spuDetailStandardVO = spuDetailService.getSpuDetail(spuId);
        return JsonResult.ok(spuDetailStandardVO);
    }
}
