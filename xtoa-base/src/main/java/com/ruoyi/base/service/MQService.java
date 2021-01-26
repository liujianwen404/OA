package com.ruoyi.base.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
@Slf4j
public class MQService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, Map map){
        rabbitTemplate.convertAndSend(exchange,null,map);
    }
}
