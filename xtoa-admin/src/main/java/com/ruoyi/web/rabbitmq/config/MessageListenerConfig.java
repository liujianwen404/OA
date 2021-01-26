package com.ruoyi.web.rabbitmq.config;

import com.ruoyi.web.rabbitmq.consumer.AttendanceAckReceiver;
import com.ruoyi.web.rabbitmq.consumer.ClockInReceiver;
import com.ruoyi.web.rabbitmq.consumer.WMSTopicReceiver;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MessageListenerConfig {
    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private AttendanceAckReceiver attendanceAckReceiver;//消息接收处理类

    @Autowired
    private WMSTopicReceiver wmsTopicReceiver;//消息接收处理类

     @Autowired
    private ClockInReceiver clockInReceiver;//员工打卡消息接收处理类

    @Value("${topic.wms}")
    private String queue;

    @Value("${queueNameForCallBack}")
    private String queueNameForCallBack;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // RabbitMQ默认是自动确认，这里改为手动确认消息
        //设置一个队列
        container.setQueueNames(queueNameForCallBack);
        //如果同时设置多个如下： 前提是队列都是必须已经创建存在的
//          container.setQueueNames("fanout.dev","fanout.test","fanout.prod");


        //另一种设置队列的方法,如果使用这种情况,那么要设置多个,就使用addQueues
        //container.setQueues(new Queue("TestDirectQueue",true));
        //container.addQueues(new Queue("TestDirectQueue2",true));
        //container.addQueues(new Queue("TestDirectQueue3",true));
        container.setMessageListener(attendanceAckReceiver);

        return container;
    }

    @Bean
    public SimpleMessageListenerContainer wmsMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // RabbitMQ默认是自动确认，这里改为手动确认消息
        //设置一个队列
        container.setQueueNames(queue);
        container.setMessageListener(wmsTopicReceiver);

        return container;
    }

    @Bean
    public SimpleMessageListenerContainer attendaceMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // RabbitMQ默认是自动确认，这里改为手动确认消息
        //设置一个队列
        container.setQueueNames("topic.attendance");
        container.setMessageListener(clockInReceiver);

        return container;
    }


}
