package cn.tedu.mall.front.mapper;

import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FrontSkuMapper {
    List<SkuStandardVO> listSkuBySpuId(@Param("spuId")Long id);
}
