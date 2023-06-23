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
package cn.tedu.mall.seckill.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.service.IFrontSpuDetailService;
import cn.tedu.mall.front.service.IFrontSpuService;
import cn.tedu.mall.pojo.product.vo.SpuDetailStandardVO;
import cn.tedu.mall.pojo.product.vo.SpuStandardVO;
import cn.tedu.mall.pojo.seckill.model.SeckillSpu;
import cn.tedu.mall.pojo.seckill.vo.SeckillSpuDetailSimpleVO;
import cn.tedu.mall.pojo.seckill.vo.SeckillSpuVO;
import cn.tedu.mall.seckill.service.ISeckillSpuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @since 2022-02-23
 */
@Service
@Slf4j
public class SeckillSpuServiceImpl extends AbstractSeckillServiceImpl<SeckillSpuVO> implements ISeckillSpuService {
    public static final String SECKILL_SPUS="seckill:spus";
    public static final String SECKILL_SPU_PREFIX="seckill:spu:";
    public static final String SECKILL_SPU_RAND_PREFIX="seckill:spu:rand:";
    public static final String SECKILL_SPU_RAND_MAP_PREFIX="seckill:spu:rand:map:";
    @Override
    public JsonPage<SeckillSpuVO> listSeckillSpus(Integer page, Integer pageSize) {
        List<SeckillSpuVO> cache = getListCache(SECKILL_SPUS);
        if(cache == null|| cache.size()==0) {
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"当前系统没有秒杀活动");
        }
        return JsonPage.defaultPage(cache);
    }
//    @Autowired
//    private RedisBloomUtils redisBloomUtils;

    /**
     * 查询spuId的秒杀商品,考虑缓存穿透的问题
     *
     * @param spuId
     * @return
     */
    @Override
    public SeckillSpuVO getSeckillSpu(Long spuId) {
        //TODO 布隆过滤器
        SeckillSpuVO cache = getCache(SECKILL_SPU_PREFIX + spuId);
        if (cache == null) {
            log.error("秒杀商品:{},没有成功写入缓存,请检查数据",spuId);
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"当前商品没有缓存");
        }
        //计算系统时间是否开始秒杀
        String randCodeKey=SECKILL_SPU_RAND_PREFIX+spuId;
        LocalDateTime nowTime = LocalDateTime.now();//系统时间
        //后面的小于前面的,返回值是负数,反之,正数
        Duration afterStart = Duration.between(nowTime, cache.getStartTime());
        Duration beforeEnd = Duration.between(cache.getEndTime(), nowTime);
        if (afterStart.isNegative() && beforeEnd.isNegative()) {
            //秒杀活动已经开始
            cache.setUrl("/seckill/" + redisTemplate.opsForHash().get(SECKILL_SPU_RAND_MAP_PREFIX,randCodeKey));
        }
        return cache;
    }

    /**
     * 根据spuId查询spu detail
     *
     * @param spuId
     * @return
     */
    @DubboReference
    private IFrontSpuDetailService spuDetailService;
    @Override
    public SeckillSpuDetailSimpleVO getSeckillSpuDetail(Long spuId) {
        SpuDetailStandardVO detail = spuDetailService.getSpuDetail(spuId);
        SeckillSpuDetailSimpleVO seckillSpuDetailSimpleVO = new SeckillSpuDetailSimpleVO();
        BeanUtils.copyProperties(detail, seckillSpuDetailSimpleVO);
        return seckillSpuDetailSimpleVO;
    }
}
