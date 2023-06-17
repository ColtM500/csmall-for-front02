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
import cn.tedu.mall.front.service.IFrontAttributeService;
import cn.tedu.mall.pojo.front.vo.SelectAttributeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实现前台属性查询的控制器
 */
@RestController
@RequestMapping("/front/attribute")
@Api(tags = "前台属性筛选")
public class FrontAttributeController {
    @Autowired
    private IFrontAttributeService frontAttributeService;
    /**
     * 利用分类id查询一个筛选的对象值 Used
     */
    @GetMapping(value = "/select")
    @ApiOperation(value = "查询分类属性筛选")
    public JsonResult<SelectAttributeVO> getAllRelatedAttributes(Long categoryId) {
        SelectAttributeVO selectAttributeVO
            = frontAttributeService.getAllRelatedAttirbutes(categoryId);
        return JsonResult.ok(selectAttributeVO);
    }
}
