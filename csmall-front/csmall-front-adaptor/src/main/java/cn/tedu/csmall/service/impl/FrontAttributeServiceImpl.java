package cn.tedu.csmall.service.impl;

import cn.tedu.csmall.mapper.FrontSpuMapper;
import cn.tedu.mall.front.service.IFrontAttributeService;
import cn.tedu.mall.pojo.front.vo.SelectAttributeVO;
import cn.tedu.mall.pojo.product.vo.AttributeStandardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontAttributeServiceImpl implements IFrontAttributeService {
    @Override
    public SelectAttributeVO getAllRelatedAttirbutes(Long categoryId) {
        return null;
    }
    @Autowired
    private FrontSpuMapper frontSpuMapper;
    @Override
    public List<AttributeStandardVO> getSpuAttributesBySpuId(Long id) {
        return frontSpuMapper.selectAttributesBySpuId(id);
    }
}
