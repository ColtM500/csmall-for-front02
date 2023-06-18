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

package cn.tedu.mall.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {
    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("订单购物车系统")
            .description("订单购物车系统接口文档")
            .termsOfServiceUrl("地址待定")
            .contact(new Contact("", "", ""))
            .version("1.0")
            .build();
    }

    @Bean
    public Docket createRestApi(ApiInfo apiInfo) {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo)
            .groupName("订单购物车系统")
            .select()
            .apis(RequestHandlerSelectors.basePackage("cn.tedu.mall.order.controller"))
            .paths(PathSelectors.any())
            .build();
    }



}
