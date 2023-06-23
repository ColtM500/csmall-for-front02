package cn.tedu.mall.pojo.seckill.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SeckillStockLog implements Serializable {
    private Long id;
    private Long skuId;
    private String orderSn;
    private Integer quantity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
