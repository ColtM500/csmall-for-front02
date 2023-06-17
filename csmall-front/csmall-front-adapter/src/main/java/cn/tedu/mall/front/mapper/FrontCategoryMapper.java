package cn.tedu.mall.front.mapper;

import cn.tedu.mall.pojo.product.vo.CategoryStandardVO;
import java.util.List;

public interface FrontCategoryMapper {
    List<CategoryStandardVO> selectAllCategories();
}
