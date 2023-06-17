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
import cn.tedu.mall.front.service.IFrontCategoryService;
import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0.0
 */
@RestController
@RequestMapping("/front/category")
@Api(tags = "前台分类树模块")
public class FrontCategoryController {
    @Autowired
    private IFrontCategoryService frontCategoryService;
    /**3700
     * 封装一个前台分类树 Used
     * 需要父子关系
     */
    @GetMapping(value = "/all")
    @ApiOperation(value = "查询三级分类树")
    public JsonResult<FrontCategoryTreeVO<FrontCategoryEntity>> categoryTree() {
        //查询分类树
        FrontCategoryTreeVO<FrontCategoryEntity> frontCategoryTreeVO = frontCategoryService.categoryTree();
        return JsonResult.ok(frontCategoryTreeVO);
    }
}
