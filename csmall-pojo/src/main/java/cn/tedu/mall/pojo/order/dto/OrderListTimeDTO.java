package cn.tedu.mall.pojo.order.dto;

import cn.tedu.mall.pojo.valid.order.OrderRegExpression;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value="查询订单时间条件DTO")
public class OrderListTimeDTO implements OrderRegExpression, Serializable {
    @ApiModelProperty(value="查询订单起始时间",example = "",dataType = "LocalDateTime")
    private LocalDateTime startTime;
    @ApiModelProperty(value="查询订单结束时间",example = "",dataType = "LocalDateTime")
    private LocalDateTime endTime;
    @ApiModelProperty(value="用户id",example = "1")
    private Long userId;
    @ApiModelProperty(value="页数",example = "1")
    private Integer page;
    @ApiModelProperty(value="条数",example = "20")
    private Integer pageSize;
}
