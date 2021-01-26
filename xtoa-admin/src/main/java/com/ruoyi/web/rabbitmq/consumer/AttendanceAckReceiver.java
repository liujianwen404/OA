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
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class AttendanceAckReceiver implements ChannelAwareMessageListener {

    private static final Logger log = LoggerFactory.getLogger(AttendanceAckReceiver.class);
    @Autowired
    private IHrAttendanceInfoService attendanceInfoService;

    @Autowired
    private IProjectEmpService projectEmpService;

    @Resource(name = "ThreadPoolExecutorForCallBack")
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //因为传递消息的时候用的map传递,所以将Map从Message内取出需要做些处理
        String msg = message.toString();
        log.info("MQ onMessage:" + msg);
        try {
            String body = new String(message.getBody(),"UTF-8");
            log.info("MQ body:" + body);

            JSONObject msgMap = (JSONObject)JSON.parse(body);
            String obj = (String)msgMap.get("obj");
            JSONObject jsonObject = JSON.parseObject(obj);
            String eventType = (String)msgMap.get("eventType");

            callBack(jsonObject,eventType);
            log.info("消费成功  TestAttendanceDirectQueue  data:{}"+msgMap);
            log.info("消费的主题消息来自："+message.getMessageProperties().getConsumerQueue());
            channel.basicAck(deliveryTag, true);
//			channel.basicReject(deliveryTag, true);//为true会重新放回队列
        } catch (Exception e) {
            channel.basicReject(deliveryTag, false);
            e.printStackTrace();
            log.info("消费失败  TestAttendanceDirectQueue  data:{}"+msg);
        }

    }


    private void callBack(JSONObject obj, String eventType) {

        log.info("callBack  JSONObject:"+obj.toJSONString()+",eventType:"+eventType);

        if (DingEventEnum.ChatCallBack.chat_add_member.getValue().equals(eventType)) {

                try {
                    log.info(DingEventEnum.ChatCallBack.chat_add_member.getText() + obj.toJSONString() );
                    ChatCallBackDTO chatCallBackDTO = JSON.parseObject(obj.toJSONString(),ChatCallBackDTO.class);
                    projectEmpService.chatAddMember(chatCallBackDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }


        } else if (DingEventEnum.ChatCallBack.chat_remove_member.getValue().equals(eventType)) {
                try {
                    log.info(DingEventEnum.ChatCallBack.chat_remove_member.getText() + obj.toJSONString() );
                    ChatCallBackDTO chatCallBackDTO = JSON.parseObject(obj.toJSONString(),ChatCallBackDTO.class);
                    projectEmpService.chatRemoveMember(chatCallBackDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }
        }else if (DingEventEnum.ChatCallBack.chat_quit.getValue().equals(eventType)) {
                try {
                    log.info(DingEventEnum.ChatCallBack.chat_quit.getText() + obj.toJSONString() );
                    ChatCallBackDTO chatCallBackDTO = JSON.parseObject(obj.toJSONString(),ChatCallBackDTO.class);
                    projectEmpService.chatQuit(chatCallBackDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }
        }else if (DingEventEnum.ChatCallBack.chat_disband.getValue().equals(eventType)) {
                try {
                    log.info(DingEventEnum.ChatCallBack.chat_disband.getText() + obj.toJSONString() );
                    ChatCallBackDTO chatCallBackDTO = JSON.parseObject(obj.toJSONString(),ChatCallBackDTO.class);
                    projectEmpService.chatDisband(chatCallBackDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }
        }
    }

}
