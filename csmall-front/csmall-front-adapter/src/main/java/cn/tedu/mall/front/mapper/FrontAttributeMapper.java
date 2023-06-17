package cn.tedu.mall.front.mapper;

import cn.tedu.mall.pojo.product.vo.AttributeStandardVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FrontAttributeMapper {
    List<AttributeStandardVO> selectAttributesBySpuId(@Param("spuId")Long id);
}
