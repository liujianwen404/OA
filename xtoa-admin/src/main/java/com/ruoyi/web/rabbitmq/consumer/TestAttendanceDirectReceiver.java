package com.ruoyi.web.rabbitmq.consumer;

import com.ruoyi.hr.service.IHrAttendanceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 钉钉打卡回调事件消息监听
 * */
@Component
//@RabbitListener(queues = "TestAttendanceDirectQueue")//监听的队列名称,钉钉打卡回调事件测试环境队列
public class TestAttendanceDirectReceiver {
    private static final Logger log = LoggerFactory.getLogger(TestAttendanceDirectReceiver.class);

    @Autowired
    private IHrAttendanceInfoService attendanceInfoService;

    @RabbitHandler
    public void process(Map map) {
        try{
            log.error("钉钉打卡回调事件消费消息成功，参数map{}" +  map.toString());
            String userId = (String) map.get("userId");
            attendanceInfoService.saveAttendanceByDingAPI(userId);
        }catch (Exception e){
            log.error("钉钉打卡回调事件消费消息失败，参数map{}" +  map.toString());
            e.printStackTrace();
        }


    }

}
