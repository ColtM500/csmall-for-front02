package cn.tedu.mall.front.service;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.pojo.product.vo.SpuListItemVO;
import cn.tedu.mall.pojo.product.vo.SpuStandardVO;

public interface IFrontSpuService {
    JsonPage<SpuListItemVO> listSpuByCategoryId(Long id, Integer page, Integer size);
    SpuStandardVO getFrontSpuById(Long id);
}
