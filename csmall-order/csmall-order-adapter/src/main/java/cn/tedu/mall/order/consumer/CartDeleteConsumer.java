package cn.tedu.mall.order.consumer;

import cn.tedu.mall.order.mapper.OmsOrderItemMapper;
import cn.tedu.mall.order.mapper.OmsOrderMapper;
import cn.tedu.mall.order.service.impl.OmsCartServiceImpl;
import cn.tedu.mall.pojo.order.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic="tx-order-add-topic",consumerGroup = "cart-delete-local-consumer",selectorExpression = "*")
@Slf4j
public class CartDeleteConsumer implements RocketMQListener<String> {
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private OmsOrderItemMapper orderItemMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override public void onMessage(String message) {
        int selectOrderSNtimes=0;
        String sn=null;
        OrderDetailVO order =null;
        do {
            sn=message;
            log.debug("获取sn:{}",sn);
            //先用sn查询order
            order = orderMapper.selectOrderBySn(sn);
            log.debug("查询到的order:{}", JSON.toJSONString(order,true));
            if (order == null){
                log.debug("当前异步流程没有查询到订单:",sn);
                selectOrderSNtimes++;
            }else{
                break;
            }
            if (selectOrderSNtimes>0){
                log.error("订单没有查询到,可能订单新增出现问题,sn:{}",sn);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }while(selectOrderSNtimes<3);
        log.debug("查询到的order:{}", JSON.toJSONString(order,true));
        //拿到userId
        String userId=order.getUserId()+"";
        String cartKey= OmsCartServiceImpl.ORDER_CART_PREFIX+userId;
        //拿到商品skuId
        List<String> skuIds = orderItemMapper.selectSkuIdsByOrderId(order.getId());
        String[] hashKeys = new String[skuIds.size()];
        if (skuIds==null|| skuIds.size()==0){
            return;
        }
        skuIds.toArray(hashKeys);
        log.debug("参数数组是:{}",hashKeys);
        //删除购物车
        HashOperations operations = redisTemplate.opsForHash();
        operations.delete(cartKey,hashKeys);
    }
}
