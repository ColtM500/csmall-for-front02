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

import cn.tedu.mall.common.exception.CoolSharkServiceException;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.pojo.seckill.dto.SeckillOrderAddDTO;
import cn.tedu.mall.pojo.seckill.vo.SeckillCommitVO;
import cn.tedu.mall.seckill.service.ISeckillService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill")
@Api(tags = "秒杀发起模块")
public class SeckillController {
    private static final String SEKCILL_RAND_CODE_SPU="seckill:rand:code:spu:";
    @Autowired
    private ISeckillService seckillService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 返回时间毫秒数
     */
    @GetMapping("now")
    @ApiOperation("获取系统时间毫秒")
    public JsonResult getNow() {
        Long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return JsonResult.ok(now);
    }

    /**
     * 选择好购买商品后直接访问秒杀系统新增,产生订单.
     */
    @PostMapping("/{randCode}")
    @ApiOperation(value = "秒杀商品提交订单")
    @ApiImplicitParams({
        @ApiImplicitParam(value = "生成的随机路径", name = "randCode", paramType = "path", type = "string")
    })
    public JsonResult<SeckillCommitVO> commitSeckill(@PathVariable("randCode") String randCode,
        SeckillOrderAddDTO seckillOrderAddDTO) {
        seckillOrderAddDTO.setRandCode(randCode);
        SeckillCommitVO vo = seckillService.commitSeckill(seckillOrderAddDTO);
        return JsonResult.ok(vo);
    }
}
