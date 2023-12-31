package cn.tedu.csmall.service.impl;

import cn.tedu.csmall.mapper.FrontSkuMapper;
import cn.tedu.mall.front.service.IFrontSkuService;
import cn.tedu.mall.pojo.order.dto.OrderItemAddDTO;
import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FrontSkuServiceImpl implements IFrontSkuService {

    @Autowired
    private FrontSkuMapper frontSkuMapper;

    @Override
    public List<SkuStandardVO> getSkus(Long spuId) {
        //TODO: 布隆过滤器需求
        //TODO: 击穿处理 分布式抢锁
        //TODO: 缓存cache-aside 流程 使用list数据
        List<SkuStandardVO> skuStandardVOS = frontSkuMapper.listSkuBySpuId(spuId);
        return skuStandardVOS;
    }

    @Override
    public SkuStandardVO getSku(Long id) {
        return null;
    }

    @Override
    public List<SkuStandardVO> listSkuByIds(List<Long> ids) {
        return null;
    }

    @Override
    public void reduceSkusCounts(List<OrderItemAddDTO> items, String sn) {

    }

    @Override
    public void returnStock(String sn) {

    }
}
