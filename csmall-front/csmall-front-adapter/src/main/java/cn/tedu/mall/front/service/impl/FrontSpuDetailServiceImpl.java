package cn.tedu.mall.front.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.mapper.FrontSpuDetailMapper;
import cn.tedu.mall.front.service.IFrontSpuDetailService;
import cn.tedu.mall.pojo.product.vo.SpuDetailStandardVO;
import java.util.concurrent.TimeUnit;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
@DubboService
@Service
public class FrontSpuDetailServiceImpl extends AbstractFrontCacheService<SpuDetailStandardVO> implements IFrontSpuDetailService {
    private static final String FRONT_SPU_DETAIL_PREFIX="front:spu:detail:";
    @Autowired
    private FrontSpuDetailMapper spuDetailMapper;
    @Override
    public SpuDetailStandardVO getSpuDetail(Long spuId) {
        String frontSpuDetailKey=FRONT_SPU_DETAIL_PREFIX+spuId;
        SpuDetailStandardVO cache = getCache(frontSpuDetailKey);
        if (cache != null) {
            return cache;
        }
        SpuDetailStandardVO spuDetailStandardVO = spuDetailMapper.getBySpuId(spuId);
        if (spuDetailStandardVO == null) {
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"该spu下没有任何详情");
        }
        setCache(frontSpuDetailKey,spuDetailStandardVO);
        return spuDetailStandardVO;
    }
}
