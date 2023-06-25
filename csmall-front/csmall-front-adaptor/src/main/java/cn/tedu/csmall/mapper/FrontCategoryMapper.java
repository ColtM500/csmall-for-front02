package cn.tedu.csmall.mapper;

import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;

import java.util.List;

public interface FrontCategoryMapper {

     List<FrontCategoryStandardVO> selectAll();

}
