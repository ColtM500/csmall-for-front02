package cn.tedu.csmall.mapper;

import cn.tedu.mall.pojo.product.vo.SpuListItemVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FrontSpuMapper {
    @Select("select * from pms_spu where category_id = #{categoryId}")
    List<SpuListItemVO> selectSpusByCategoryId(@Param("categoryId") Long categoryId);
}
