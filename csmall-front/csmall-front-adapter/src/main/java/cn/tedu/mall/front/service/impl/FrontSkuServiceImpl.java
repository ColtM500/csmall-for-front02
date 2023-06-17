package cn.tedu.mall.front.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.mapper.FrontSkuMapper;
import cn.tedu.mall.front.service.IFrontSkuService;
import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrontSkuServiceImpl extends AbstractFrontCacheService<SkuStandardVO> implements IFrontSkuService {
    private static final String FRONT_SKU_PREFIX="front:sku:";
    private static final String FRONT_SKU_LIST_PREFIX="front:sku:list:";
    @Autowired
    private FrontSkuMapper skuMapper;
    @Override public List<SkuStandardVO> getSkus(Long id) {
        String frontSkuKey=FRONT_SKU_PREFIX+id;
        List<SkuStandardVO> cache = getListCache(frontSkuKey);
        if (cache != null) {
            return cache;
        }
        List<SkuStandardVO> skus = skuMapper.listSkuBySpuId(id);
        if (skus !=null && skus.size()>0) {
            setListCache(frontSkuKey,skus,24L+new Random().nextInt(5), TimeUnit.HOURS);
        }else {
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"该spu下没有任何sku");
        }
        return skus;
    }
}
