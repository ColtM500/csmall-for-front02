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
package cn.tedu.mall.order.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ListConvertUtils {
    public static <T> List<T> stringToList(String json, Class<T> object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, object);
            List<T> list = objectMapper.readValue(json, listType);
            return list;
        } catch (JsonProcessingException e) {
            log.info("将当前类:{}转化list时失败",object.getName());
            e.printStackTrace();
        }
        return null;
    }
}
