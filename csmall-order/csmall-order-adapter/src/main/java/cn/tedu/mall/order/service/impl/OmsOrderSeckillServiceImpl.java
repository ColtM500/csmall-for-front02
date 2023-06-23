package cn.tedu.mall.order.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.order.mapper.OmsOrderMapper;
import cn.tedu.mall.order.service.IOrderAddService;
import cn.tedu.mall.pojo.order.dto.OrderAddCondition;
import cn.tedu.mall.pojo.seckill.dto.SeckillOrderAddDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.OrderedMap;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OmsOrderSeckillServiceImpl implements IOrderAddService, InitializingBean {
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Override public void addOrder(OrderAddCondition orderAddCondition) {
        SeckillOrderAddDTO orderAddDTO=(SeckillOrderAddDTO)orderAddCondition;
        String sn=orderAddDTO.getSn();
        //利用sn查询是否存在订单
        boolean existsOrder=orderMapper.selectExistBySn(sn);
        if (existsOrder){
            log.error( "订单已经存在,sn:{},重复下单",sn);
            throw new CoolSharkServiceException(ResponseCode.CONFLICT,"订单已经存在");
        }
        //订单不存在,消息事务
        //发送半消息事务
        Message<String> message= MessageBuilder.withPayload(sn)
            .setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL,2).build();
        rocketMQTemplate.sendMessageInTransaction("tx-order-seckill-topic",message,orderAddDTO);
    }

    @Override public void afterPropertiesSet() throws Exception {
        OrderAddServiceSelector.register(SeckillOrderAddDTO.class.getName(),this);
    }
}
