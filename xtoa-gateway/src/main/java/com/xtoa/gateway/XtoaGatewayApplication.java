package com.xtoa.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class XtoaGatewayApplication {
    private static final Logger logger = LoggerFactory.getLogger(XtoaGatewayApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(XtoaGatewayApplication.class, args);
        logger.info("XtoaGatewayApplication=======================启动成功！");
    }

}
