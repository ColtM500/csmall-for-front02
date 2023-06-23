package cn.tedu.mall.order.service.impl;

import cn.tedu.mall.order.service.IOrderAddService;
import cn.tedu.mall.pojo.order.dto.OrderAddCondition;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderAddServiceSelector implements IOrderAddService {
    private static Map<String,IOrderAddService> MAPPER=new HashMap<>();

    @Override public void addOrder(OrderAddCondition orderAddCondition) {
        String name = orderAddCondition.getClass().getName();
        MAPPER.get(name).addOrder(orderAddCondition);
    }
    public static void register(String className, IOrderAddService object ){
        MAPPER.put(className,object);
    }
}
