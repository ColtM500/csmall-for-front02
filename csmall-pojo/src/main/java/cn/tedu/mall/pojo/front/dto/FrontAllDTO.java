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
package cn.tedu.mall.pojo.front.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 使用做为前台全性能接口DTO
 */
@Data
@ApiModel("前台全功能DTO")
public class FrontAllDTO {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "管理员用户名", required = false)
    //@NotNull(message = "请提交用户名")
    private String username;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "管理员昵称")
    private String nickname;

    @ApiModelProperty(value = "用户密码", required = true)
    // @NotNull(message = "请提交密码")
    private String password;

    @ApiModelProperty(value = "用户确认密码", required = true)
    //@NotNull(message = "请提交确认密码")
    private String passwordAct;
    /**
     * 头像URL
     */
    @ApiModelProperty(value = "管理员头像url")
    private String avatar;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "管理员手机号")
    //@NotNull(message = "请提交手机号")
    private String phone;

    /**
     * 电子邮箱
     */
    @ApiModelProperty(value = "管理员电子邮箱")
    private String email;

    /**
     * 描述
     */
    @ApiModelProperty(value = "管理员描述")
    private String description;

    /**
     * 是否启用，1=启用，0=未启用
     */
    @ApiModelProperty(value = "是否启用，1=启用，0=未启用")
    private Integer enable;

    /**
     * 控制台测试打印
     *
     * @return
     */
    @Override
    public String toString() {
        return "FrontAllDTO{" +
            "username='" + username + '\'' +
            ", nickname='" + nickname + '\'' +
            ", password='" + password + '\'' +
            ", passwordAct='" + passwordAct + '\'' +
            ", avatar='" + avatar + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", description='" + description + '\'' +
            ", enable=" + enable +
            '}';
    }
}
