package cn.tedu.mall.pojo.seckill.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>SPU（Standard Product Unit）表</p>
 *
 * @author tedu.cn
 * @since 2021-11-30
 */
@Data
public class SeckillSpu implements Serializable {

    /**
     * 记录id
     */
    private Long id;

    /**
     * SPU名称
     */
    private String name;

    /**
     * SPU编号
     */
    private String typeNumber;

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String description;

    /**
     * 价格（显示在列表中）
     */
    private BigDecimal listPrice;
    private BigDecimal seckillListPrice;
    /**
     * 当前库存（冗余）
     */
    private Integer stock;

    /**
     * 库存预警阈值（冗余）
     */
    private Integer stockThreshold;

    /**
     * 计件单位
     */
    private String unit;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 品牌名称（冗余）
     */
    private String brandName;

    /**
     * 类别id
     */
    private Long categoryId;

    /**
     * 类别名称（冗余）
     */
    private String categoryName;

    /**
     * 属性模板id
     */
    private Long attributeTemplateId;

    /**
     * 相册id
     */
    private Long albumId;

    /**
     * 组图URLs，使⽤JSON格式表示
     */
    private String pictures;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /**
     * 数据创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 数据最后修改时间
     */
    private LocalDateTime gmtModified;

}