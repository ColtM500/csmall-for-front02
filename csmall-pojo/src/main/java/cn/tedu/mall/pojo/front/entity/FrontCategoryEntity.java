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
package cn.tedu.mall.pojo.front.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 前台分类树封装分类实体
 *
 * @version 1.0.0
 */
@Data
@ApiModel(value = "商品分类树实体")
public class FrontCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 类别id®
     */
    @ApiModelProperty(value = "类别id")
    private Long id;

    /**
     * 类别名称
     */
    @ApiModelProperty(value = "类别名称")
    private String name;

    /**
     * 父级类别id，如果无父级，则为0
     */
    @ApiModelProperty(value = "父级类别id，如果无父级，则为0")
    private Long parentId;

    /**
     * 深度，最顶级类别的深度为1，次级为2，以此类推
     */
    @ApiModelProperty(value = "深度，最顶级类别的深度为1，次级为2，以此类推，如果parent是1，rank就是2，parents是2，rank就是3，parent的rank不能是3", required = true)
    private Integer depth;

    /**
     * 关键词列表，各关键词使用英文的逗号分隔
     */
    @ApiModelProperty(value = "关键词列表，各关键词使用英文的逗号分隔")
    private String keywords;

    /**
     * 自定义排序序号
     */
    @ApiModelProperty(value = "自定义排序序号")
    private Integer sort;

    /**
     * 图标图片的URL
     */
    @ApiModelProperty(value = "图标图片的URL")
    private String icon;

    /**
     * 是否为父级（是否包含子级），1=是父级，0=不是父级
     */
    @ApiModelProperty(value = "是否为父级（是否包含子级），1=是父级，0=不是父级")
    private Integer parent;

    /**
     * 是否启用，1=启用，0=未启用
     */
    @ApiModelProperty(value = "是否启用，1=启用，0=未启用")
    private Integer active;

    /**
     * 是否显示在导航栏中，1=启用，0=未启用
     */
    @ApiModelProperty(value = "是否显示在导航栏中，1=启用，0=未启用")
    private Integer display;

    /**
     * 如果当前isParent是1，则需要下级分类
     */
    private List<FrontCategoryEntity> childrens;

    /**
     * 控制台测试打印
     *
     * @return
     */
    @Override
    public String toString() {
        return "FrontCategoryEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", parentId=" + parentId +
            ", depth=" + depth +
            ", keywords='" + keywords + '\'' +
            ", sort=" + sort +
            ", icon='" + icon + '\'' +
            ", parent=" + parent +
            ", active=" + active +
            ", display=" + display +
            ", childrens=" + childrens +
            '}';
    }
}
