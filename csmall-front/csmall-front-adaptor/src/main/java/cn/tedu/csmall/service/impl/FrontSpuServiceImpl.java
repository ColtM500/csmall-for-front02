package cn.tedu.csmall.service.impl;

import cn.tedu.csmall.mapper.FrontSpuMapper;
import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.service.IFrontSpuService;
import cn.tedu.mall.pojo.product.vo.SpuListItemVO;
import cn.tedu.mall.pojo.product.vo.SpuStandardVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service

public class FrontSpuServiceImpl extends FrontCacheServiceImpl<SpuStandardVO> implements IFrontSpuService {
    @Autowired(required = false)
    private FrontSpuMapper frontSpuMapper;

    public static final String FRONT_CAT_SPU_PAGE_PREFIX = "front:cat:spu:page:";
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public JsonPage<SpuListItemVO> listSpuByCategoryId(
            Long categoryId, Integer page, Integer pageSize) {
        String spusPageKey = FRONT_CAT_SPU_PAGE_PREFIX + categoryId + ":" + page + ":" + pageSize;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object o = valueOperations.get(spusPageKey);
        if (o == null) {
            //根据传递的分页数据 page pageSize 开启分页查询
            PageHelper.startPage(page, pageSize);//mybatis 被pageHelper做了一个拦截
            //将会在以下执行的第一个sql查询select语句中 添加limit (page-1),pageSize
            //查询结束后,查询count(*) 封装total总数 还会利用总数和total计算totalPage
            List<SpuListItemVO> spuListItemVOList =
                    frontSpuMapper.selectSpusByCategoryId(categoryId);
            //提供一个PageInfo 对象可以获取拦截拼接查询的分页数据
            PageInfo pageInfo = new PageInfo(spuListItemVOList);
            //有可能 当前测试环境,没有分类的spu
            if (spuListItemVOList == null || spuListItemVOList.size() == 0) {
                throw new CoolSharkServiceException
                        (ResponseCode.NOT_FOUND, "当前分类下没有任何spu");
            }
            valueOperations.set(spusPageKey, pageInfo);
            return JsonPage.restPage(pageInfo);
        }
        return JsonPage.restPage((PageInfo) o);

    }

    public static final String FRONT_SPU_PREFIX = "front:spu:";

    @Override
    public SpuStandardVO getFrontSpuById(Long id) {
        String spuKey = FRONT_SPU_PREFIX + id;
        SpuStandardVO cache = getCache(spuKey);
        if (cache == null) {
            SpuStandardVO frontBySpuId = frontSpuMapper.getBySpuId(id);
            if (frontBySpuId == null) {
                throw new CoolSharkServiceException(ResponseCode.NOT_FOUND, "当前spuId没有数据库数据");
            }
            //缓存雪崩问题 同时超时 给一个随机时间结尾24小时超时 随机演唱 1-5小时
            setCache(spuKey, frontBySpuId,
                    60 * 60 * 24 + (new Random().nextInt(5) + 1) * 60 * 60L, TimeUnit.SECONDS);
        }
        return cache;
    }
}
