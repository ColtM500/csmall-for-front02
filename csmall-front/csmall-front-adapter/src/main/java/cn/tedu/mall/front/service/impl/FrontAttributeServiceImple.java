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
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.mapper.FrontAttributeMapper;
import cn.tedu.mall.front.service.IFrontAttributeService;
import cn.tedu.mall.pojo.front.vo.SelectAttributeVO;
import cn.tedu.mall.pojo.product.vo.AttributeStandardVO;
import cn.tedu.mall.pojo.product.vo.BrandStandardVO;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FrontAttributeServiceImple extends AbstractFrontCacheService<AttributeStandardVO> implements IFrontAttributeService {
    private static final String FRONT_ATTRIBUTE_PREFIX = "front:attribute:";
    private static final String FRONT_ATTRIBUTE_LIST_SPU_PREFIX = "front:attribute:list:";
    @Autowired
    private FrontAttributeMapper frontAttributeMapper;
    @Override
    public SelectAttributeVO getAllRelatedAttirbutes(Long categoryId) {

        return null;
    }

    @Override
    public List<AttributeStandardVO> getSpuAttributesBySpuId(Long id) {
        String attributesKey=FRONT_ATTRIBUTE_LIST_SPU_PREFIX+id;
        List<AttributeStandardVO> cache = getListCache(attributesKey);
        if (cache!=null){
            log.debug("从缓存中获取到了属性列表");
            return cache;
        }
        List<AttributeStandardVO> attributeStandardVOs = frontAttributeMapper.selectAttributesBySpuId(id);
        if (attributeStandardVOs == null || attributeStandardVOs.size()==0){
            log.error("根据spuId:{},获取属性列表失败",id);
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"根据spuId:"+id+",获取属性列表失败");
        }
        setListCache(attributesKey,attributeStandardVOs,24L+new Random().nextInt(5), TimeUnit.HOURS);
        return attributeStandardVOs;
    }
}
