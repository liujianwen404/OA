package com.ruoyi.web.test.config;

import com.ruoyi.web.test.EnableAutoConfigTest;
import com.ruoyi.web.test.service.ServiceBeanTest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(annotation = EnableAutoConfigTest.class)
public class AutoConfigurationTest {

    @Bean
    public ServiceBeanTest getServiceBeanTest(){
        return new ServiceBeanTest();
    }
}
