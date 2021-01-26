package com.ruoyi.web.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
//@RabbitListener(queues = "topic.attendance")
public class AttendanceTopicReceiver {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("AttendanceTopicReceiver  : " + testMessage.toString());
    }


}
