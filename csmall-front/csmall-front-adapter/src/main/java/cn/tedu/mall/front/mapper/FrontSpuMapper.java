package cn.tedu.mall.front.mapper;

import cn.tedu.mall.pojo.product.vo.SpuListItemVO;
import cn.tedu.mall.pojo.product.vo.SpuStandardVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FrontSpuMapper {
    List<SpuListItemVO> getSpusByCategoryId(@Param("categoryId")Long id);

    SpuStandardVO getSpuBySpuId(@Param("spuId")Long id);
}
