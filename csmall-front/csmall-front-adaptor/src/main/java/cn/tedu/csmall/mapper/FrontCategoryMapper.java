package cn.tedu.csmall.mapper;

import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import cn.tedu.mall.pojo.product.vo.CategoryStandardVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FrontCategoryMapper {
     @Select("select * from pms_category")
     List<FrontCategoryEntity> selectAll();

}
