package com.ruoyi.web.rabbitmq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.ruoyi.base.dingTalk.DingEventEnum;
import com.ruoyi.base.dingTalk.dingCallBackDTO.ChatCallBackDTO;
import com.ruoyi.hr.service.IHrAttendanceInfoService;
import com.ruoyi.hr.service.IProjectEmpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class ClockInReceiver implements ChannelAwareMessageListener {

    private static final Logger log = LoggerFactory.getLogger(ClockInReceiver.class);

    @Autowired
    private IHrAttendanceInfoService attendanceInfoService;

    @Resource(name = "ThreadPoolExecutorForCallBack")
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void onMessage(Message message, Channel channel) {
        threadPoolExecutor.execute(()->{
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            //因为传递消息的时候用的map传递,所以将Map从Message内取出需要做些处理
            String msg = message.toString();
            log.info("MQ onMessage:{}" , msg);
            String body = null;
            try {
                body = new String(message.getBody(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            log.info("MQ body:{}" , body);

            JSONObject msgMap = (JSONObject)JSON.parse(body);
            String obj = (String)msgMap.get("obj");
            JSONObject jsonObject = JSON.parseObject(obj);
            String eventType = (String)msgMap.get("eventType");

            log.info("callBack  JSONObject:{},eventType:{}",jsonObject.toJSONString(),eventType);
            if (DingEventEnum.AttendanceCallBack.attendance_check_record.getValue().equals(eventType)) {
                try {
                    log.info(DingEventEnum.AttendanceCallBack.attendance_check_record.getText() + jsonObject.toJSONString());
                    JSONArray jsonArray = jsonObject.getJSONArray("DataList");
                    String dingUserId = jsonArray.getJSONObject(0).getString("userId");
                    attendanceInfoService.saveAttendanceByDingAPI(dingUserId);
                    log.info("消费成功  topic.attendance  data:{}",msgMap);
                    log.info("消费的主题消息来自：{}",message.getMessageProperties().getConsumerQueue());
                    try {
                        channel.basicAck(deliveryTag, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        channel.basicReject(deliveryTag, false);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    log.error("消费失败  topic.attendance  data:{}",msg);
                }
            }

        });

    }


}
