package cn.tedu.mall.order.consumer;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.order.service.IOmsOrderService;
import cn.tedu.mall.order.service.impl.OrderAddServiceSelector;
import cn.tedu.mall.pojo.order.dto.OrderAddDTO;
import cn.tedu.mall.pojo.order.vo.OrderAddVO;
import com.alibaba.fastjson.JSON;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;
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
    private static final String MESSAGE_LOCK_ORDER_ADD_PREFIX="message:lock:order:add:";
    //分布式锁
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderAddServiceSelector orderAddServiceSelector;
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
        //抢锁
        String lockKey=MESSAGE_LOCK_ORDER_ADD_PREFIX+msgId;
        String randCode=new Random().nextInt(100000)+899999+"";
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, randCode,5, TimeUnit.SECONDS);
        if (!lock){
            //抢锁失败,等待5秒重试
            try {
                log.info("抢锁失败,等待5秒");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try{
            orderAddServiceSelector.addOrder(orderAddDTO);
        } catch (CoolSharkServiceException e) {
            log.error("新增订单失败:{}",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            log.error("新增订单失败:{}",e.getMessage());
            throw new CoolSharkServiceException(ResponseCode.BAD_REQUEST,"新增订单失败");
        }finally {
            String lockRandCode = (String) redisTemplate.opsForValue().get(lockKey);
            boolean unlocked= false;
            if (randCode.equals(lockRandCode)){
                //释放锁
                unlocked = redisTemplate.delete(lockKey);
            }
            if(unlocked){
                log.info("释放锁成功");
            }else {
                log.info("释放锁失败");
            }
        }
    }
}
