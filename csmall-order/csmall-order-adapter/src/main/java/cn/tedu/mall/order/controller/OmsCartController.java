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
package cn.tedu.mall.order.controller;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.order.service.IOmsCartService;
import cn.tedu.mall.order.utils.WebConsts;
import cn.tedu.mall.pojo.order.dto.CartAddDTO;
import cn.tedu.mall.pojo.order.dto.CartUpdateDTO;
import cn.tedu.mall.pojo.order.vo.CartStandardVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 购物车数据表 前端控制器
 * </p>
 *
 * @since 2022-02-16
 */
@RestController
@RequestMapping("/oms/cart")
@Api(tags = "购物车功能")
public class OmsCartController {
    @Autowired
    private IOmsCartService cartService;

    @PostMapping("/add")
    @ApiOperation(value = "前台将选择好的SKU添加到购物车")
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult addCart(@Valid CartAddDTO cartDTO) {
        cartService.addCart(cartDTO);
        return JsonResult.ok();
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询当前用户对应的所有购物车对象")
    @ApiImplicitParams({
        @ApiImplicitParam(value = "页码", name = "page", dataType = "int"),
        @ApiImplicitParam(value = "每页记录数", name = "pageSize", dataType = "int")
    })
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult<JsonPage<CartStandardVO>> listCarts(
        @RequestParam(required = false, defaultValue = WebConsts.DEFAULT_PAGE) Integer page,
        @RequestParam(required = false, defaultValue = WebConsts.DEFAULT_PAGE_SIZE) Integer pageSize) {
        JsonPage<CartStandardVO> carts = cartService.listCarts(page, pageSize);
        return JsonResult.ok(carts);
    }

    //更新购物车数量
    @PostMapping("/update/quantity")
    @ApiOperation(value = "购物车更新SKU商品的购买数量")
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult updateQuantity(CartUpdateDTO cartUpdateDTO) {
        cartService.updateQuantity(cartUpdateDTO);
        return JsonResult.ok();
    }

}
