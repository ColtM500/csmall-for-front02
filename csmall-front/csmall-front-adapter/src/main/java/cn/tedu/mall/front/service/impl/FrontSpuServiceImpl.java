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
package cn.tedu.mall.front.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.mapper.FrontSpuMapper;
import cn.tedu.mall.front.service.IFrontSpuService;
import cn.tedu.mall.pojo.product.vo.SpuListItemVO;
import cn.tedu.mall.pojo.product.vo.SpuStandardVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrontSpuServiceImpl extends AbstractFrontCacheService<SpuStandardVO> implements IFrontSpuService {
    private static final String FRON_SPU_PREFIX = "front:spu:";
    @Autowired
    private FrontSpuMapper frontSpuMapper;
    @Override
    public JsonPage<SpuListItemVO> listSpuByCategoryId(Long categoryId, Integer page, Integer pageSize) {
        //变动数据比较复杂，不使用缓存，调用方法获取返回值
        PageHelper.startPage(page,pageSize);
        List<SpuListItemVO> spuListItemVOs=frontSpuMapper.getSpusByCategoryId(categoryId);
        JsonPage<SpuListItemVO> pageSpuListItemVOs = JsonPage.restPage(new PageInfo<>(spuListItemVOs));
        //异常检验
        if (pageSpuListItemVOs.getList().size() == 0) {
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST, "您当前分类下没有任何商品");
        }
        return pageSpuListItemVOs;
    }
    @Override
    public SpuStandardVO getFrontSpuById(Long id) throws CoolSharkServiceException {
        //TODO 防止穿透，需要布隆过滤器
        String spuVOKey = FRON_SPU_PREFIX + id;
        SpuStandardVO cache = getCache(spuVOKey);
        if (cache != null) {
            return cache;
        }
        SpuStandardVO spuStandardVO = null;
        spuStandardVO = frontSpuMapper.getSpuBySpuId(id);
        if (spuStandardVO == null) {
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST, "该Id没有对应spu");
        }
        setCache(spuVOKey, spuStandardVO,24l+new Random().nextInt(5), TimeUnit.HOURS);
        return spuStandardVO;
    }
}
