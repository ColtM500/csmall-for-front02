package cn.tedu.mall.pojo.seckill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 包含spu所有属性同时
 * 包含start_time end_time 和 展示秒杀价格
 */
@ApiModel(value = "秒杀展示SPU的VO",description = "一个包含了spu所有属性和起始结束时间和秒杀价的秒杀spu")
@Data
public class SeckillSpuVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "spu的主键id")
    private Long id;
    /**
     * SPU名称
     */
    @ApiModelProperty(value = "SPU名称")
    private String name;
    /**
     * SPU编号
     */
    @ApiModelProperty(value = "SPU编号")
    private String typeNumber;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 简介
     */
    @ApiModelProperty(value = "简介")
    private String description;
    /**
     * 价格（显示在列表中）
     */
    @ApiModelProperty(value = "原价(展示在原价后)")
    private BigDecimal listPrice;
    /**
     * 秒杀价钱
     */
    @ApiModelProperty(value="秒杀价(展示在列表)")
    private BigDecimal seckillListPrice;
    /**
     * 当前库存（冗余）
     */
    @ApiModelProperty(value = "当前库存（冗余）")
    private Integer stock;
    /**
     * 库存预警阈值（冗余）
     */
    @ApiModelProperty(value = "库存预警阈值（冗余）")
    private Integer stockThreshold;
    /**
     * 计件单位
     */
    @ApiModelProperty(value = "计件单位")
    private String unit;
    /**
     * 品牌id
     */
    @ApiModelProperty(value = "品牌id")
    private Long brandId;
    /**
     * 品牌名称（冗余）
     */
    @ApiModelProperty(value = "品牌名称（冗余）")
    private String brandName;
    /**
     * 类别id
     */
    @ApiModelProperty(value = "类别id")
    private Long categoryId;
    /**
     * 类别名称（冗余）
     */
    @ApiModelProperty(value = "类别名称（冗余）")
    private String categoryName;
    /**
     * 相册id
     */
    @ApiModelProperty(value = "相册id")
    private Long albumId;
    /**
     * 组图URLs，使⽤JSON格式表示
     */
    @ApiModelProperty(value = "组图URLs，使⽤JSON格式表示")
    private String pictures;
    /**
     * 关键词列表，各关键词使⽤英⽂的逗号分隔
     */
    @ApiModelProperty(value = "关键词列表，各关键词使⽤英⽂的逗号分隔")
    private String keywords;
    /**
     * 标签列表，各标签使⽤英⽂的逗号分隔，原则上最多3个
     */
    @ApiModelProperty(value = "标签列表，各标签使⽤英⽂的逗号分隔，原则上最多3个")
    private String tags;
    /**
     * 销量（冗余）
     */
    @ApiModelProperty(value = "销量（冗余）")
    private Integer sales;
    /**
     * 买家评论数量总和（冗余）
     */
    @ApiModelProperty(value = "买家评论数量总和（冗余）")
    private Integer commentCount;
    /**
     * 买家好评数量总和（冗余）
     */
    @ApiModelProperty(value = "买家好评数量总和（冗余）")
    private Integer positiveCommentCount;
    /**
     * ⾃定义排序序号
     */
    @ApiModelProperty(value = "⾃定义排序序号")
    private Integer sort;
    /**
     * 是否标记为删除，1=已删除，0=未删除
     */
    @ApiModelProperty(value = "是否标记为删除，1=已删除，0=未删除")
    private Integer isDelete;
    /**
     * 是否上架（发布），1=已上架，0=未上架 （下架）
     */
    @ApiModelProperty(value = "是否上架（发布），1=已上架，0=未上架 （下架）")
    private Integer isPublish;
    /**
     * 是否新品，1=新品，0=非新品
     */
    @ApiModelProperty(value = "是否新品，1=新品，0=非新品")
    private Integer isNew;
    /**
     * 是否推荐，1=推荐，0=不推荐
     */
    @ApiModelProperty(value = "是否推荐，1=推荐，0=不推荐")
    private Integer isRecommend;

    /**
     * 是否已审核，1=已审核，0=未审核
     */
    @ApiModelProperty(value = "是否已审核，1=已审核，0=未审核")
    private Integer isChecked;

    /**
     * 审核⼈（冗余）
     */
    @ApiModelProperty(value = "审核⼈（冗余）")
    private String checkUser;

    /**
     * 审核通过时间（冗余）
     */
    @ApiModelProperty(value = "审核通过时间（冗余）")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtCheck;

    /**
     * 秒杀开始时间
     */
    @ApiModelProperty(value = "秒杀开始时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;

    /**
     * 秒杀结束时间
     */
    @ApiModelProperty(value = "秒杀结束时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;
    /**
     * 隐藏的秒杀url地址
     * 如果没有到开始秒杀的时间,这个地址值是空的
     */
    @ApiModelProperty(value="隐藏的秒杀url地址,如果没有到开始秒杀的时间,这个地址值是空的")
    private String url;


}
