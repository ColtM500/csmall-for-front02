package cn.tedu.mall.order.consumer;

import cn.tedu.mall.front.service.IFrontSkuService;
import cn.tedu.mall.order.mapper.OmsOrderMapper;
import cn.tedu.mall.pojo.order.model.OmsOrder;
import cn.tedu.mall.pojo.order.vo.OrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic="order-add-delay-delete",consumerGroup = "order-delay-delete-consumer",selectorExpression = "*")
public class OrderDelayConsumer implements RocketMQListener<String> {
    @Autowired
    private OmsOrderMapper orderMapper;
    @DubboReference
    private IFrontSkuService skuService;
    @Override public void onMessage(String message) {
        //如果发现订单超时,则关闭订单,并且回退库存
        OrderDetailVO order = orderMapper.selectOrderBySn(message);
        if (order == null) {
            log.error("存在错误订单消息,查询不到订单,sn:{}", message);
        } else {
            Integer state = order.getState();
            log.debug("订单状态:{},订单sn:{}", state,message);
            if (state == 0) {
                //未支付,则关闭订单,并且回退库存
                log.debug("关闭订单回退库存");
                OmsOrder omsOrder=new OmsOrder();
                omsOrder.setId(order.getId());
                omsOrder.setState(1);
                orderMapper.updateOrderById(omsOrder);
                skuService.returnStock(message);
            }
        }
    }
}
