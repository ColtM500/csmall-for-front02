package cn.tedu.mall.pojo.front.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FrontStockLog {
    private Long id;
    private Long skuId;
    private String orderSn;
    private Integer quantity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
