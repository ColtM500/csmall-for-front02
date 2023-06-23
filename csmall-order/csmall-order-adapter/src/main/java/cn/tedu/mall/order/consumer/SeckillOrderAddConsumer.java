package cn.tedu.mall.order.consumer;

import cn.tedu.mall.pojo.seckill.dto.SeckillOrderAddDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic="seckill_order",consumerGroup = "order-add-seckill-consumer",selectorExpression = "*")
@Slf4j
public class SeckillOrderAddConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        String seckillOrderJson = new String(body);
        SeckillOrderAddDTO dto = JSON.toJavaObject(JSON.parseObject(seckillOrderJson), SeckillOrderAddDTO.class);

    }
}
