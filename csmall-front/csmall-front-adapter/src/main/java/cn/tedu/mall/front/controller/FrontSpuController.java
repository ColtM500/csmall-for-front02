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

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontAttributeService;
import cn.tedu.mall.front.service.IFrontSpuService;
import cn.tedu.mall.pojo.product.vo.AttributeStandardVO;
import cn.tedu.mall.pojo.product.vo.SpuListItemVO;
import cn.tedu.mall.pojo.product.vo.SpuStandardVO;
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

@RestController
@RequestMapping("/front/spu")
@Api(tags = "前台商品SPU模块")
public class FrontSpuController {
    @Autowired
    private IFrontSpuService frontSpuService;
    @Autowired
    private IFrontAttributeService frontAttributeService;
    /**
     * 根据分类id查询商品spu列表,利用更新时间倒序 Used
     */
    @GetMapping("/list/{categoryId}")
    @ApiOperation(value = "根据分类查询spu列表")
    @ApiImplicitParams({
        @ApiImplicitParam(value = "页码", name = "page", required = true, dataType = "int"),
        @ApiImplicitParam(value = "每页记录数", name = "pageSize", required = true, dataType = "int"),
        @ApiImplicitParam(name = "categoryId", value = "分类id", paramType = "path", required = true, dataType = "long")
    })
    public JsonResult<JsonPage<SpuListItemVO>> listSpuByCategoryId(
        @PathVariable(name = "categoryId") Long categoryId, Integer page, Integer pageSize) {
        JsonPage<SpuListItemVO> jsonPage = frontSpuService.listSpuByCategoryId(categoryId, page, pageSize);
        return JsonResult.ok(jsonPage);
    }
    /**
     * 根据id查询spu数据 Used
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "前台根据SpuID查询SPU信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "spu id", paramType = "path", required = true, dataType = "long")
    })
    public JsonResult<SpuStandardVO> getFrontSpuById(@PathVariable(value = "id") Long id) {
        SpuStandardVO spuStandardVO = frontSpuService.getFrontSpuById(id);
        return JsonResult.ok(spuStandardVO);
    }
    /**
     * 前台SPU页面查看当前SPU的所有属性和值 Used
     */
    @GetMapping("/template/{spuId}")
    @ApiOperation(value = "前台SPU页面查看当前SPU的所有属性和值")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "spuId", value = "spu id", paramType = "path", required = true, dataType = "long")
    })
    public JsonResult<List<AttributeStandardVO>> getSpuAttributeBySpuId(@PathVariable(value = "spuId") Long spuId) {
        List<AttributeStandardVO> frontAttributeVOLi = frontAttributeService.getSpuAttributesBySpuId(spuId);
        return JsonResult.ok(frontAttributeVOLi);
    }
}
