package cn.tedu.csmall.service.impl;

import cn.tedu.csmall.mapper.FrontSpuDetailMapper;
import cn.tedu.mall.front.service.IFrontSpuDetailService;
import cn.tedu.mall.pojo.product.vo.SpuDetailStandardVO;
import org.springframework.beans.factory.annotation.Autowired;

public class FrontSpuDetailServiceImpl implements IFrontSpuDetailService {

    @Autowired
    private FrontSpuDetailMapper frontSpuDetailMapper;

    @Override
    public SpuDetailStandardVO getSpuDetail(Long spuId) {
        SpuDetailStandardVO spuDetailBySpuId = frontSpuDetailMapper.getSpuDetailBySpuId(spuId);
        return spuDetailBySpuId;
    }
}
