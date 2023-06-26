package cn.tedu.csmall.mapper;

import cn.tedu.mall.pojo.product.vo.SpuDetailStandardVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface FrontSpuDetailMapper {

//    @Select("select * from pms_spu_detail where spu_id = #{spuId}")
    SpuDetailStandardVO getSpuDetailBySpuId(@Param("spuId") Long spuId);
}
