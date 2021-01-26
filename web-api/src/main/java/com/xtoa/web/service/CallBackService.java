package com.xtoa.web.service;

import com.ruoyi.base.dingTalk.DingCallBackApi;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.base.dingTalk.DingEventEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@Service
public class CallBackService implements ApplicationRunner {

    @Autowired
    private DingConfig dingConfig;

    @Autowired
    private DingCallBackApi dingCallBackApi;

    @Value("${dingCallBackUrl}")
    private String dingCallBackUrl;

    @Value("${openDingCallbackServer}")
    private Boolean openDingCallbackServer;

    @Value("${dingCallbackAttendanceUrl}")
    private String callbackAttendance;
    @Override
    public void run(ApplicationArguments args) {
        if(openDingCallbackServer){//一个企业只能注册一个接收回调的URL地址，所以在其他环境最好关闭此开关，以免影响线上正常数据的获取
            try {
                try{
                    //清除上一次的注册事件
                    dingCallBackApi.deleteCallBack();
                }catch (RuntimeException e){
                    e.printStackTrace();
                }

                //向钉钉注册回调事件
                ArrayList<String> callBackTag = new ArrayList<>();
                Set<Map.Entry<String, String>> entries = DingEventEnum.ChatCallBack.asMap().entrySet();
                entries.forEach(callBack -> {
                    callBackTag.add(callBack.getKey());
                } );
                //注册打卡回调
                Set<Map.Entry<String, String>> attendanceEntries = DingEventEnum.AttendanceCallBack.asMap().entrySet();
                attendanceEntries.forEach((attendanceMap) -> {
                    callBackTag.add(attendanceMap.getKey());
                });
                dingCallBackApi.registerCallBack(callBackTag,dingCallBackUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
