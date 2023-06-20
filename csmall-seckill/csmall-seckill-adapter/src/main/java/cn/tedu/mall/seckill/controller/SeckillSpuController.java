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

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.pojo.seckill.vo.SeckillSpuDetailSimpleVO;
import cn.tedu.mall.pojo.seckill.vo.SeckillSpuVO;
import cn.tedu.mall.seckill.service.ISeckillSpuService;
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
 * <p>
 * 前端控制器
 * </p>
 *
 * @since 2022-02-23
 */
@RestController
@RequestMapping("/seckill/spu")
@Api(tags = "秒杀SPU模块")
public class SeckillSpuController {
    @Autowired
    private ISeckillSpuService seckillSpuService;

    /**
     * 查询有秒杀价钱的spu列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询秒杀商品SPU列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "页码", name = "page", required = true, dataType = "int"), @ApiImplicitParam(value = "每页记录数", name = "pageSize", required = true, dataType = "int")})
    public JsonResult<JsonPage<SeckillSpuVO>> listSeckillSpus(Integer page, Integer pageSize) {
        JsonPage<SeckillSpuVO> seckillSpus = seckillSpuService.listSeckillSpus(page, pageSize);
        return JsonResult.ok(seckillSpus);
    }

    @GetMapping("/{spuId}")
    @ApiOperation(value = "利用spuId查询spu信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "spuId", value = "SPU id", paramType = "path", required = true, dataType = "int")})
    public JsonResult<SeckillSpuVO> getSeckillSpu(@PathVariable("spuId") Long spuId) {
        SeckillSpuVO seckillSpuVO = seckillSpuService.getSeckillSpu(spuId);
        return JsonResult.ok(seckillSpuVO);
    }

    @GetMapping("/{spuId}/detail")
    @ApiOperation(value = "利用SPUID查询spu详细信息detail")
    @ApiImplicitParams({@ApiImplicitParam(name = "spuId", value = "SPU id", paramType = "path", required = true, dataType = "int")})
    public JsonResult<SeckillSpuDetailSimpleVO> getSeckillSpuDetail(@PathVariable("spuId") Long spuId) {
        SeckillSpuDetailSimpleVO seckillSpuDetailVO = seckillSpuService.getSeckillSpuDetail(spuId);
        return JsonResult.ok(seckillSpuDetailVO);
    }

}
