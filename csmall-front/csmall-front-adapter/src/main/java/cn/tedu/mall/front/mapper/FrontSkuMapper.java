package cn.tedu.mall.front.mapper;

import cn.tedu.mall.pojo.front.entity.FrontStockLog;
import cn.tedu.mall.pojo.order.dto.OrderItemAddDTO;
import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FrontSkuMapper {
    List<SkuStandardVO> listSkuBySpuId(@Param("spuId")Long id);

    SkuStandardVO selectSkuById(@Param("id")Long id);

    List<SkuStandardVO> selectSkusByIds(List<Long> ids);

    //void updateSkuCounts(@Param("id")Long id, @Param("quantity")Integer quantity);

    void insertSkuStockLog(@Param("orderSN")String sn, @Param("skuId")Long id, @Param("quantity")Integer quantity);

    List<FrontStockLog> selectStockLogBySn(String sn);

    void decrStockCounts(@Param("id")Long id, @Param("quantity")Integer quantity);

    void incrStockCounts(@Param("id")Long id, @Param("quantity")Integer quantity);
}
