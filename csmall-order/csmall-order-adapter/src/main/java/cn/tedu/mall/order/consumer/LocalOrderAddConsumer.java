package cn.tedu.mall.order.consumer;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.order.service.IOmsOrderService;
import cn.tedu.mall.pojo.order.dto.OrderAddDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic="order-add-topic",consumerGroup = "order-add-local-consumer",selectorExpression = "*")
@Slf4j
public class LocalOrderAddConsumer implements RocketMQListener<MessageExt> {
    //分布式锁
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOmsOrderService orderService;
    @Override public void onMessage(MessageExt message) {
        //解析msg
        String msgId=message.getMsgId();
        //解析order对象
        byte[] body = message.getBody();
        String orderJson=new String(body);
        log.debug("获取到的消息id:{},消息内容:{}",msgId,orderJson);
        OrderAddDTO orderAddDTO =null;
        try{
            orderAddDTO = JSON.toJavaObject(JSON.parseObject(orderJson), OrderAddDTO.class);
            log.debug("解析order:{}",orderAddDTO);
        }catch (Exception e){
            log.error("解析order失败:{}",e.getMessage());
            //抛出异常,底层返回未消费正确
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"解析order失败");
        }
        orderService.addOrder(orderAddDTO);
    }
}
