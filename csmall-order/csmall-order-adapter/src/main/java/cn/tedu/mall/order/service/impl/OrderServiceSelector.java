package cn.tedu.mall.order.service.impl;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.order.service.IOmsOrderService;
import cn.tedu.mall.pojo.order.dto.OrderAddDTO;
import cn.tedu.mall.pojo.order.dto.OrderListTimeDTO;
import cn.tedu.mall.pojo.order.dto.OrderStateUpdateDTO;
import cn.tedu.mall.pojo.order.vo.OrderDetailVO;
import cn.tedu.mall.pojo.order.vo.OrderListVO;


public class OrderServiceSelector implements IOmsOrderService {

    @Override public void addOrder(OrderAddDTO orderAddDTO) {

    }

    @Override public void updateOrderState(OrderStateUpdateDTO orderStateUpdateDTO) {

    }

    @Override public JsonPage<OrderListVO> listOrdersBetweenTimes(OrderListTimeDTO orderListTimeDTO) {
        return null;
    }

    @Override public OrderDetailVO getOrderDetail(Long id) {
        return null;
    }
}
