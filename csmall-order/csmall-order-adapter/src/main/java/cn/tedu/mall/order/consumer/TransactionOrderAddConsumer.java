package cn.tedu.mall.order.consumer;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.service.IFrontSkuService;
import cn.tedu.mall.order.mapper.OmsOrderItemMapper;
import cn.tedu.mall.order.mapper.OmsOrderMapper;
import cn.tedu.mall.pojo.order.dto.OrderAddDTO;
import cn.tedu.mall.pojo.order.dto.OrderItemAddDTO;
import cn.tedu.mall.pojo.order.model.OmsOrder;
import cn.tedu.mall.pojo.order.model.OmsOrderItem;
import cn.tedu.mall.pojo.order.vo.OrderDetailVO;
import com.alibaba.fastjson.JSON;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RocketMQTransactionListener
@Slf4j
public class TransactionOrderAddConsumer implements RocketMQLocalTransactionListener {
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private OmsOrderItemMapper orderItemMapper;
    @DubboReference
    private IFrontSkuService skuService;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    /**
     * 执行本地事务
     * @param msg
     * @param arg
     * @return
     */
    @Override public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        //转化对象
        OrderAddDTO orderAddDTO = (OrderAddDTO) arg;
        log.debug("事务消息处理逻辑获取携带object:{}",JSON.toJSONString(arg,true));
        //拿到商品和库存数量
        List<OrderItemAddDTO> items = orderAddDTO.getOrderItems();
        //减库存
        try{
            skuService.reduceSkusCounts(items,orderAddDTO.getSn());
        }catch (CoolSharkServiceException e){
            log.error("商品减库存失败,message:{}",e.getMessage());
            return RocketMQLocalTransactionState.ROLLBACK;
        }catch (Exception e){
            log.error("减库存出现系统异常,message:{}",e.getMessage());
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        //减库存成功 记录订单新增
        //转化对象
        OmsOrder omsOrder=new OmsOrder();
        try {
            BeanUtils.copyProperties(omsOrder,orderAddDTO);
            log.debug("转化了omsOrder对象:{}",JSON.toJSONString(omsOrder,true));
        } catch (IllegalAccessException e) {
            log.error("转化对象orderAddDTO到omsOrder失败,message:{}",e.getMessage());
            //发送UNKNOW 回调方法还库存
            return RocketMQLocalTransactionState.UNKNOWN;
        } catch (InvocationTargetException e) {
            log.error("转化对象orderAddDTO到omsOrder失败,message:{}",e.getMessage());
            //发送UNKNOW 回调方法还库存
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        log.debug("转化对象orderAddDTO到omsOrder成功:orderJson:{}", JSON.toJSONString(omsOrder));
        //补充缺少字段
        replenishOrder(omsOrder);

        try{
            orderMapper.insertOrder(omsOrder);
        }catch (Exception e){
            log.error("新增订单入库出现异常,message:{}",e.getMessage());
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        //转化和补充orderItems
        List<OmsOrderItem> orderItems=new ArrayList<>();
        for (OrderItemAddDTO item : items) {
            OmsOrderItem omsOrderItem=new OmsOrderItem();
            try {
                BeanUtils.copyProperties(omsOrderItem,item);
            } catch (IllegalAccessException e) {
                log.error("转化对象OrderItemAddDTO到OmsOrderItem失败,message:{}",e.getMessage());
                //发送UNKNOW 回调方法还库存
                return RocketMQLocalTransactionState.UNKNOWN;
            } catch (InvocationTargetException e) {
                log.error("转化对象OrderItemAddDTO到OmsOrderItem失败,message:{}",e.getMessage());
                //发送UNKNOW 回调方法还库存
                return RocketMQLocalTransactionState.UNKNOWN;
            }
            //补充orderId
            omsOrderItem.setOrderId(omsOrder.getId());
            orderItems.add(omsOrderItem);
        }
        try{
            orderItemMapper.insertOrderItems(orderItems);
        }catch (Exception e){
            log.error("新增订单商品入库出现异常,message:{}",e.getMessage());
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        //发送延迟队列消息,如果超时未支付,取消订单
        Message<String> message= MessageBuilder.withPayload(orderAddDTO.getSn()).
            setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL,2).build();
        rocketMQTemplate.send("order-add-delay-delete",message);
        return RocketMQLocalTransactionState.COMMIT;
    }
    /**
     * 回调
     * @param msg
     * @return
     */
    @Override public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        //检查一下订单,和订单商品是否存在
        String sn = (String) msg.getPayload();
        OrderDetailVO order = orderMapper.selectOrderBySn(sn);
        Long orderId=null;
        if (order!=null){
            orderId=order.getId();
            boolean exists=orderItemMapper.selectExistsByOrderId(orderId);
            if (exists) {
                return RocketMQLocalTransactionState.COMMIT;
            }
        }
        //上面的条件有一个不符,就直接还库存
        try{
            skuService.returnStock(sn);
        }catch (Exception e){
            log.error("库存系统出现异常,无法确认库存回滚,记录日志,手动处理,sn:{}",sn);
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }


    private void replenishOrder(OmsOrder omsOrder) {
        //默认状态未支付
        if (omsOrder.getState() == null) {
            omsOrder.setState(0);
        }
        //创建时间,后续时间以创建时间为准
        if (omsOrder.getGmtCreate() == null) {
            omsOrder.setGmtCreate(LocalDateTime.now());
        }
        if (omsOrder.getGmtModified() == null) {
            omsOrder.setGmtModified(omsOrder.getGmtCreate());
        }
        if (omsOrder.getGmtOrder() == null) {
            omsOrder.setGmtModified(omsOrder.getGmtCreate());
        }
        //金额调整,为null的金额 默认是0
        if (omsOrder.getAmountOfDiscount() == null) {
            omsOrder.setAmountOfDiscount(new BigDecimal(0.0));
        }
        if (omsOrder.getAmountOfFreight() == null) {
            omsOrder.setAmountOfFreight(new BigDecimal(0.0));
        }
        if (omsOrder.getAmountOfOriginalPrice() == null) {
            log.error("没有确定当前订单商品总价");
            omsOrder.setAmountOfOriginalPrice(new BigDecimal(0.0));
        }
        if (omsOrder.getAmountOfActualPay() == null) {
            log.debug("前端没有计算实际支付价钱,需要后台计算");
            //拿到商品总价
            BigDecimal originalPrice = omsOrder.getAmountOfOriginalPrice();
            //拿到优惠价格和运费
            BigDecimal disCount = omsOrder.getAmountOfDiscount();
            BigDecimal freight = omsOrder.getAmountOfFreight();
            //商品原价减去优惠,加上运费
            BigDecimal actualPay = originalPrice.subtract(disCount).add(freight);
            omsOrder.setAmountOfActualPay(actualPay);
        }
    }


}