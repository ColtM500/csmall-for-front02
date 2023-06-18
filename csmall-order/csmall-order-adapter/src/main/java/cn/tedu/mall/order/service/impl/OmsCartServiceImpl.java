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
package cn.tedu.mall.order.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.pojo.domain.CsmallAuthenticationInfo;
import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.service.IFrontSkuService;
import cn.tedu.mall.order.mapper.OmsCartMapper;
import cn.tedu.mall.order.service.IOmsCartService;
import cn.tedu.mall.pojo.order.dto.CartAddDTO;
import cn.tedu.mall.pojo.order.dto.CartUpdateDTO;
import cn.tedu.mall.pojo.order.model.OmsCart;
import cn.tedu.mall.pojo.order.vo.CartStandardVO;
import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车数据表 服务实现类
 * </p>
 *
 * @since 2022-02-16
 */
@Service
@Slf4j
public class OmsCartServiceImpl implements IOmsCartService {
    public static final String ORDER_CART_PREFIX = "cart:user:";
    @Autowired
    private OmsCartMapper omsCartMapper;
    @DubboReference
    private IFrontSkuService frontSkuService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 限制添加数量20以内
     *
     * @param cartDTO
     */
    @Override public void addCart(CartAddDTO cartDTO) {
        //获取userId
        Long userId = getUserId();
        //准备一个hash操作对象
        HashOperations operationsHash = redisTemplate.opsForHash();
        String cartKey = ORDER_CART_PREFIX + userId;
        //判断新增还是 增加商品数量 TODO
        Boolean existCart = operationsHash.hasKey(cartKey, cartDTO.getSkuId() + "");
        if (existCart) {
            Integer quantity = (Integer) operationsHash.get(cartKey, cartDTO.getSkuId() + "");
            quantity = quantity + cartDTO.getQuantity();
            operationsHash.put(cartKey, cartDTO.getSkuId() + "", quantity);
        } else {
            //检查当前数量是否已经达到20
            Set keys = operationsHash.keys(cartKey);
            if (keys.size() == 20) {
                throw new CoolSharkServiceException(ResponseCode.NOT_FOUND, "购物车已满");
            }
            //远程调用
            SkuStandardVO sku = frontSkuService.getSku(cartDTO.getSkuId());
            //验证存在
            if (sku == null) {
                throw new CoolSharkServiceException(ResponseCode.NOT_FOUND, "商品不存在");
            }
            //验证库存
            if (sku.getStock() < cartDTO.getQuantity()) {
                throw new CoolSharkServiceException(ResponseCode.NOT_FOUND, "库存不足");
            }
            //存储redis hash结构
            operationsHash.put(cartKey, cartDTO.getSkuId() + "", cartDTO.getQuantity());
        }
    }

    @Override public JsonPage<CartStandardVO> listCarts(Integer page, Integer pageSize) {
        String cartKey = ORDER_CART_PREFIX + getUserId();
        Boolean cartExists = redisTemplate.hasKey(cartKey);
        HashOperations operationsHash = redisTemplate.opsForHash();
        if (!cartExists) {
            log.error("购物车不存在,cartKey:{}", cartKey);
            return JsonPage.nullPage();
        } else {
            //拿到所有skuId
            Set keys = operationsHash.keys(cartKey);
            if (keys.isEmpty()) {
                log.error("购物车为空,cartKey:{}", cartKey);
                return JsonPage.nullPage();
            }
            //处理信息封装
            List<Long> skuIds = new ArrayList<>();
            for (Object key : keys) {
                //转化
                Long skuId = Long.parseLong((String) key);
                skuIds.add(skuId);
            }
            //远程调用,封装返回购物车
            List<SkuStandardVO> skuStandardVOS = frontSkuService.listSkuByIds(skuIds);
            //封装转化成cart
            List<CartStandardVO> cartStandardVOS = new ArrayList<>();
            for (SkuStandardVO skuStandardVO : skuStandardVOS) {
                //从sku查询来的
                CartStandardVO cartStandardVO = new CartStandardVO();
                cartStandardVO.setSkuId(skuStandardVO.getId());
                cartStandardVO.setData(skuStandardVO.getSpecifications());
                cartStandardVO.setBarCode(skuStandardVO.getBarCode());
                cartStandardVO.setTitle(skuStandardVO.getTitle());
                cartStandardVO.setMainPicture(skuStandardVO.getPictures().split(",")[0]);
                cartStandardVO.setPrice(skuStandardVO.getPrice());
                //本地计算封装的
                cartStandardVO.setQuantity((Integer) operationsHash.get(cartKey, skuStandardVO.getId() + ""));
                cartStandardVO.setUserId(getUserId());
                cartStandardVO.setId(cartStandardVO.getUserId() + ":" + cartStandardVO.getSkuId());
                cartStandardVOS.add(cartStandardVO);

            }
            return JsonPage.defaultPage(cartStandardVOS);
        }

    }

    @Override public void updateQuantity(CartUpdateDTO cartUpdateDTO) {
        //截取sku userId
        Long userId = null;
        Long skuId = null;
        if (cartUpdateDTO.getId() != null) {
            userId = Long.parseLong(cartUpdateDTO.getId().split(":")[0]);
            skuId = Long.parseLong(cartUpdateDTO.getId().split(":")[1]);
            log.debug("userId:{},skuId:{}", userId, skuId);
            log.debug("loginUserId:{}",getUserId());
        }
        if (skuId==null|| userId==null){
            throw new CoolSharkServiceException(ResponseCode.NOT_FOUND,"参数错误");
        }
        //购物车所属和当前登录一致
        if (userId==getUserId()){
            //准备一个hash操作对象
            HashOperations operationsHash = redisTemplate.opsForHash();
            String cartKey = ORDER_CART_PREFIX + userId;
            //判断购物车是否存在
            Boolean existsCart = operationsHash.hasKey(cartKey, skuId+"");
            if (existsCart){
                operationsHash.put(cartKey, skuId + "", cartUpdateDTO.getQuantity());
            }else{
                throw new CoolSharkServiceException(ResponseCode.NOT_FOUND,"购物车不存在");
            }
        }else{
            throw new CoolSharkServiceException(ResponseCode.CONFLICT,"登录用户和购物车用户不一致");
        }
    }

    public CsmallAuthenticationInfo getUserInfo() {

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            CsmallAuthenticationInfo csmallAuthenticationInfo = (CsmallAuthenticationInfo) authentication.getPrincipal();
            return csmallAuthenticationInfo;
        } else {
            throw new CoolSharkServiceException(ResponseCode.UNAUTHORIZED, "没有登录者信息");
        }
    }

    public Long getUserId() {
        CsmallAuthenticationInfo userInfo = getUserInfo();
        return userInfo.getId();
    }
}
