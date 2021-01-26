package com.xtoa.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@ComponentScan("com.ruoyi")
@ComponentScan("com.xtoa")
@EnableDiscoveryClient
@EnableRedisRepositories( basePackages = "com.ruoyi.system")
@EnableHystrix
public class WebApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApiApplication.class,args);
    }
}
