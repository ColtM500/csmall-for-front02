package cn.tedu.mall.front.service;

import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import java.util.List;

public interface IFrontSkuService {
    List<SkuStandardVO> getSkus(Long id);
    SkuStandardVO getSku(Long id);
    List<SkuStandardVO> listSkuByIds(List<Long> ids);
}
