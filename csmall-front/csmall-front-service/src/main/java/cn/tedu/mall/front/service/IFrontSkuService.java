package cn.tedu.mall.front.service;

import cn.tedu.mall.pojo.order.dto.OrderItemAddDTO;
import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface IFrontSkuService {
    List<SkuStandardVO> getSkus(Long id);
    SkuStandardVO getSku(Long id);
    List<SkuStandardVO> listSkuByIds(List<Long> ids);

    void reduceSkusCounts(List<OrderItemAddDTO> items, String sn);

    void returnStock(String sn);
}
