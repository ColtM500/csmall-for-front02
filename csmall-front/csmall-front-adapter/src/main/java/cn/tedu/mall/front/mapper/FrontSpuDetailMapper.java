package cn.tedu.mall.front.mapper;

import cn.tedu.mall.pojo.product.vo.SpuDetailStandardVO;

public interface FrontSpuDetailMapper {
    SpuDetailStandardVO getBySpuId(Long id);
}
