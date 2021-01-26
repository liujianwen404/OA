package com.ruoyi.web.rabbitmq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Component
@Slf4j
public class WMSTopicReceiver implements ChannelAwareMessageListener {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String body = new String(message.getBody(),"UTF-8");
        log.info("MQ body:" + body);
        JSONObject msgMap = (JSONObject) JSON.parse(body);
        try {
            String messageId = (String) msgMap.get("messageId");
            String redisMessageId = (String) redisTemplate.opsForValue().get(messageId);
            if(messageId.equals(redisMessageId)){
                log.info("此消息已被消费过============================WMSTopicReceiver  : {}" + msgMap);
                return;
            }
//            FinancePayment payment = new FinancePayment();
//            String loginName = (String) message.get("");
//            financePaymentService.submitApply(payment,loginName);
            channel.basicAck(deliveryTag,true);//是否批量.true:将一次性ack所有小于deliveryTag的消息。
            redisTemplate.opsForValue().set(messageId,messageId);
            log.info("消费成功============================WMSTopicReceiver  : {}" + msgMap);
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);//被拒绝的是否重新入队列
            e.printStackTrace();
            //手动事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("消费失败  ============================WMSTopicReceiver : {}"+msgMap);
        }
    }
}
