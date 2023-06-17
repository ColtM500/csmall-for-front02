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
import cn.tedu.mall.front.mapper.FrontCategoryMapper;
import cn.tedu.mall.front.service.IFrontCategoryService;
import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import cn.tedu.mall.pojo.product.vo.CategoryStandardVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FrontCategoryServiceImpl extends AbstractFrontCacheService<FrontCategoryTreeVO> implements IFrontCategoryService {
    private static final String FRONT_CATEGORY_TREE_KEY = "front:category:tree";
    @Autowired
    private FrontCategoryMapper frontCategoryMapper;
    //调用pms查询所有分类
    private IFrontCategoryService frontCategoryService;
    @Override
    public FrontCategoryTreeVO categoryTree() {
        FrontCategoryTreeVO cache = getCache(FRONT_CATEGORY_TREE_KEY);
        if (cache != null) {
            return cache;
        }
        //查询数据库
        List<CategoryStandardVO> categoryStandardVOs = frontCategoryMapper.selectAllCategories();
        //工具类处理分类树封装
        FrontCategoryTreeVO<FrontCategoryEntity> frontCategoryTreeVO = initTree(categoryStandardVOs);
        setCache(FRONT_CATEGORY_TREE_KEY,frontCategoryTreeVO);
        return frontCategoryTreeVO;
    }

    /**
     * 将查询出来的全量分类树,整理成三级分类结构
     *
     * @param categoryStandardVOs
     * @return
     */
    public FrontCategoryTreeVO initTree(List<CategoryStandardVO> categoryStandardVOs) {
        FrontCategoryTreeVO frontCategoryTreeVO = new FrontCategoryTreeVO();
        /*
        这里这个map的作用是用父类ID做key,用父类下的子类list做value,
        最终形成父id:子列表结合的数据结构.
         */
        Map<Long, List<FrontCategoryEntity>> map = new HashMap<>();
        log.info("拿到了分类个数:{}", categoryStandardVOs.size());
        //循环全量分类
        for (CategoryStandardVO categorySimpleVO : categoryStandardVOs) {
            //准备一个转化后的对象
            FrontCategoryEntity frontCategoryEntity = new FrontCategoryEntity();
            //利用工具转化
            BeanUtils.copyProperties(categorySimpleVO, frontCategoryEntity);
            //拿到每一个分类的父级ID
            Long parentId = frontCategoryEntity.getParentId();
            //判断map中是否有这个父类id的key
            if (map.containsKey(parentId)) {
                //如果有,说明value list存在,将当前分类对象放入value的list集合
                map.get(parentId).add(frontCategoryEntity);
                //停止本次循环进入下一次循环
                continue;
            }
            //如果没有,说明这个父类id是第一次加入map,准备value对象,将分类加入value结合
            List<FrontCategoryEntity> value = new ArrayList<>();
            value.add(frontCategoryEntity);
            //加入map,下次这个id就能找到对应value集合
            map.put(parentId, value);
        }
        log.info("当前map数据包含父类id个数:{}" + map.size());
        /*
        到此为止map中记录了所有parentId和下级分类list关系
        下面我们要利用嵌套循环,将三级分类树结构封装完毕
         */
        //首先是利用parentId=0l的参数拿到map中的一级分类树.
        List<FrontCategoryEntity> firstLevelCategoryEntities = map.get(0L);
        if (firstLevelCategoryEntities == null) {
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST, "您访问的分类树没有父级");
        }
        //循环嵌套，封装二级，三级分类
        for (FrontCategoryEntity firstLevelCategoryEntity : firstLevelCategoryEntities) {
            //一级分类id，也就是二级分类的父id
            Long secondLevelParentId = firstLevelCategoryEntity.getId();
            List<FrontCategoryEntity> secondLevelCategoryEntities = map.get(secondLevelParentId);
            if (secondLevelCategoryEntities == null) {
                log.info("当前一级分类id:{}下没有二级分类", secondLevelParentId);
                continue;
            }
            for (FrontCategoryEntity secondLevelCategoryEntity : secondLevelCategoryEntities) {
                //二级分类id，也就是三级分类父id
                Long thirdLevelParentId = secondLevelCategoryEntity.getId();
                List<FrontCategoryEntity> thirdLevelCategoryEntities = map.get(thirdLevelParentId);
                if (thirdLevelCategoryEntities == null) {
                    log.info("当前二级分类id:{}下没有三级分类", thirdLevelParentId);
                    continue;
                }
                //写入当前父对象的属性中
                secondLevelCategoryEntity.setChildrens(thirdLevelCategoryEntities);
            }
            firstLevelCategoryEntity.setChildrens(secondLevelCategoryEntities);
        }
        frontCategoryTreeVO.setCategories(firstLevelCategoryEntities);
        return frontCategoryTreeVO;
    }
}
