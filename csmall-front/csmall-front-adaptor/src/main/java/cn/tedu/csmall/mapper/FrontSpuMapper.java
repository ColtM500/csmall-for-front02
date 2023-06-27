package cn.tedu.csmall.mapper;

import cn.tedu.mall.pojo.product.vo.AttributeStandardVO;
import cn.tedu.mall.pojo.product.vo.SpuListItemVO;
import cn.tedu.mall.pojo.product.vo.SpuStandardVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FrontSpuMapper {
//    @Select("select * from pms_spu where category_id = #{categoryId}")
    List<SpuListItemVO> selectSpusByCategoryId(@Param("categoryId") Long categoryId);

//    @Select("select * from pms_spu where id = #{id}")
    SpuStandardVO getBySpuId(@Param("id") Long id);

    @Select("select pms_attribute.* from pms_spu\n" +
            "left join pms_category on pms_spu.category_id=pms_category.id\n" +
            "left join pms_category_attribute_template on pms_category.id=pms_category_attribute_template.category_id\n" +
            "left join pms_attribute_template on pms_category_attribute_template.attribute_template_id=pms_attribute_template.id\n" +
            "left join pms_attribute on pms_attribute.template_id=pms_attribute_template.id\n" +
            "where pms_spu.id=#{spuId}")
    List<AttributeStandardVO>
    selectAttributesBySpuId(@Param("spuId")Long id);
}
