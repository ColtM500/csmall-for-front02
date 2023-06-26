package cn.tedu.csmall.mapper;

import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FrontSkuMapper {
//    @Select("select * from pms_sku where spu_id = #{spuId}")
    List<SkuStandardVO> listSkuBySpuId(@Param("spuId") Long id);
}
