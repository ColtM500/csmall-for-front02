package cn.tedu.mall.order.consumer;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;

public class SeckillOrderAddConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        // TODO Auto-generated method stub

    }
}
