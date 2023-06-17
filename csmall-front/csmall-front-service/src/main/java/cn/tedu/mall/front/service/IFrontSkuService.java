package cn.tedu.mall.front.service;

import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import java.util.List;

public interface IFrontSkuService {
    List<SkuStandardVO> getSkus(Long id);
}
