/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.tedu.mall.pojo.front.vo;

import com.fasterxml.jackson.annotation.JsonRawValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@ApiModel(value = "前台sku 的vo对象")
@Data
public class FrontSkuSimpleVO implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "sku的id")
    private Long id;

    /**
     * SPU id
     */
    @ApiModelProperty(value = "SPU id")
    private Long spuId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 条型码
     */
    @ApiModelProperty(value = "条型码")
    private String barCode;

    /**
     * 属性模版id
     */
    @ApiModelProperty(value = "属性模版id")
    private Long attributeTemplateId;

    /**
     * 全部属性，使用JSON格式表示（冗余）
     */
    @ApiModelProperty(value = "全部属性，使用JSON格式表示（冗余）")
    @JsonRawValue
    private String specifications;

    /**
     * 相册id
     */
    @ApiModelProperty(value = "相册id")
    private Long albumId;

    /**
     * 组图URLs，使用JSON格式表示
     */
    @ApiModelProperty(value = "组图URLs，使用JSON格式表示")
    @JsonRawValue
    private String pictures;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    /**
     * 当前库存
     */
    @ApiModelProperty(value = "当前库存")
    private Integer stock;

    /**
     * 库存预警阈值
     */
    @ApiModelProperty(value = "库存预警阈值")
    private Integer stockThreshold;

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
     * 自定义排序序号
     */
    @ApiModelProperty(value = "自定义排序序号")
    private Integer sort;
}
