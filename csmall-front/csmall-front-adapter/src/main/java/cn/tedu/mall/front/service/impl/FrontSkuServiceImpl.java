package cn.tedu.mall.front.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.mapper.FrontSkuMapper;
import cn.tedu.mall.front.service.IFrontSkuService;
import cn.tedu.mall.pojo.front.entity.FrontStockLog;
import cn.tedu.mall.pojo.order.dto.OrderItemAddDTO;
import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@DubboService
public class FrontSkuServiceImpl extends AbstractFrontCacheService<SkuStandardVO> implements IFrontSkuService {
    private static final String FRONT_SKU_PREFIX="front:sku:";
    private static final String FRONT_SKU_LIST_PREFIX="front:sku:list:";
    @Autowired
    private FrontSkuMapper skuMapper;
    @Override public List<SkuStandardVO> getSkus(Long id) {
        String frontSkuKey=FRONT_SKU_LIST_PREFIX+id;
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

    /**
     * 远程调用 给订单查询sku信息的接口
     * @param id
     * @return
     */
    @Override public SkuStandardVO getSku(Long id) {
        String skuKey=FRONT_SKU_PREFIX+id;
        SkuStandardVO cache = getCache(skuKey);
        if (cache != null) {
            return cache;
        }else{
            cache = skuMapper.selectSkuById(id);
            setCache(FRONT_SKU_LIST_PREFIX,cache,24L+new Random().nextInt(5),TimeUnit.HOURS);
        }
        return cache;
    }

    @Override public List<SkuStandardVO> listSkuByIds(List<Long> ids) {
        List<SkuStandardVO> standardVOS=new ArrayList<>();
        //便利读缓存和本地数据库
        for (Long id : ids) {
            String skuKey=FRONT_SKU_PREFIX+id;
            SkuStandardVO cache = getCache(skuKey);
            if (cache != null) {
                standardVOS.add(cache);
            }else{
                cache = skuMapper.selectSkuById(id);
                if (cache != null) {
                    standardVOS.add(cache);
                    setCache(skuKey,cache,24L+new Random().nextInt(5),TimeUnit.HOURS);
                }
            }
        }
        return standardVOS;
    }
    @Override public void reduceSkusCounts(List<OrderItemAddDTO> items, String sn) {
        //解析减库存的数据 需要sku 和quantity
        for (OrderItemAddDTO item : items) {
            Integer quantity=item.getQuantity();
            Long skuId=item.getSkuId();
            skuMapper.decrStockCounts(skuId,quantity);
            skuMapper.insertSkuStockLog(sn,skuId,quantity);
        }

    }

    @Override public void returnStock(String sn) {
        //查询记录的sn对应减库存数量,存在则补回去,删除已补充的记录
        List<FrontStockLog> frontStockLogs=skuMapper.selectStockLogBySn(sn);
        for (FrontStockLog frontStockLog : frontStockLogs) {
            skuMapper.incrStockCounts(frontStockLog.getSkuId(),frontStockLog.getQuantity());
        }
    }
}
