package com.xtoa.web.rabbitmq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutRabbitConfig {

    /**
     *  创建三个队列 ：fanout.dev   fanout.test  fanout.prod
     *  将三个队列都绑定在交换机 fanoutExchange 上
     *  因为是扇型交换机, 路由键无需配置,配置也不起作用
     */


    @Bean
    public Queue attendanceQueueDev() {
        return new Queue("attendanceFanout.dev");
    }

    @Bean
    public Queue attendanceQueueTest() {
        return new Queue("attendanceFanout.test");
    }

    @Bean
    public Queue attendanceQueueProd() {
        return new Queue("attendanceFanout.prod");
    }

    @Bean
    FanoutExchange attendanceFExchange() {
        return new FanoutExchange("attendanceFanoutExchange");
    }

    @Bean
    Binding bindingExchangeDev() {
        return BindingBuilder.bind(attendanceQueueDev()).to(attendanceFExchange());
    }

    @Bean
    Binding bindingExchangeTest() {
        return BindingBuilder.bind(attendanceQueueTest()).to(attendanceFExchange());
    }

    @Bean
    Binding bindingExchangeProd() {
        return BindingBuilder.bind(attendanceQueueProd()).to(attendanceFExchange());
    }

}
