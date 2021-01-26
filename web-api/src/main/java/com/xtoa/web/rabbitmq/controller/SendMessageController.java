package com.xtoa.web.rabbitmq.controller;

import cn.hutool.core.lang.UUID;
import com.ruoyi.common.core.domain.AjaxResult;
import com.xtoa.web.rabbitmq.config.TopicRabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/webApi")
public class SendMessageController {
    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    @GetMapping("/sendDirectMessage")
    public AjaxResult sendDirectMessage() {
        Map<String,Object> map=new HashMap<>();
        map.put("userId","136010023426068513");
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestAttendanceDirectExchange", "TestAttendanceDirectRouting", map);
        return AjaxResult.success("ok");
    }

    @GetMapping("/sendTopicMessageChat")
    public AjaxResult sendTopicMessageChat() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: CHAT ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> chatMap = new HashMap<>();
        chatMap.put("messageId", messageId);
        chatMap.put("messageData", messageData);
        chatMap.put("createTime", createTime);
        rabbitTemplate.convertAndSend("topicExchange", "topic.chat", chatMap);
        return AjaxResult.success("ok");
    }

    @GetMapping("/sendTopicMessageAttendance")
    public AjaxResult sendTopicMessageAttendance() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: attendance";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> accendanceMap = new HashMap<>();
        accendanceMap.put("messageId", messageId);
        accendanceMap.put("messageData", messageData);
        accendanceMap.put("createTime", createTime);
        rabbitTemplate.convertAndSend("topicExchange", "topic.attendance", accendanceMap);
        return AjaxResult.success("ok");
    }

    @GetMapping("/sendTopicMessageWMS")
    public AjaxResult sendTopicMessageWMS() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: wms";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.WMS, map);
        return AjaxResult.success("ok");
    }


}
