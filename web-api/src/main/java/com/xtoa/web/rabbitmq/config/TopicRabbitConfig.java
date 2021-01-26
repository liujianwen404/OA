package com.xtoa.web.rabbitmq.config;

import jdk.nashorn.internal.objects.annotations.Property;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {
    //绑定键
    public final static String chat = "topic.chat";
    public final static String attendance = "topic.attendance";
//    public final static String WMS = "topic.wms";
    public static String WMS;

    @Value("${topic.wms}")
    public void setWMS(String WMS) {
        TopicRabbitConfig.WMS = WMS;
    }

    @Bean
    public Queue chatQueue() {
        return new Queue(TopicRabbitConfig.chat);
    }

    @Bean
    public Queue attendanceQueue() {
        return new Queue(TopicRabbitConfig.attendance);
    }

    @Bean
    public Queue wmsQueue() {
        return new Queue(TopicRabbitConfig.WMS);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }


    //将firstQueue和topicExchange绑定,而且绑定的键值为topic.man
    //这样只要是消息携带的路由键是topic.man,才会分发到该队列
    @Bean
    Binding ChatbindingExchangeMessage() {
        return BindingBuilder.bind(chatQueue()).to(exchange()).with("topic.#");
    }

    //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    @Bean
    Binding AttendancebindingExchangeMessage() {
        return BindingBuilder.bind(attendanceQueue()).to(exchange()).with(attendance);
    }

    @Bean
    Binding WMSbindingExchangeMessage() {
        return BindingBuilder.bind(wmsQueue()).to(exchange()).with(WMS);
    }

}
