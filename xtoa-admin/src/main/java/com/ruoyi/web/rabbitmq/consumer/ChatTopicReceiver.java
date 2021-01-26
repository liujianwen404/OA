package com.ruoyi.web.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
//@RabbitListener(queues = "topic.chat")
public class ChatTopicReceiver {

    @RabbitHandler
    public void process(Map testMessage) {
//        System.out.println("ChatTopicReceiver  : " + testMessage.toString());
    }


}
