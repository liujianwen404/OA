package com.ruoyi.base.dingTalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DingChatApi {
    private static final Logger log = LoggerFactory.getLogger(DingChatApi.class);
    @Autowired
    private DingConfig dingConfig;

    /**
     * 创建会话
     * @return
     */
    public OapiChatCreateResponse create(String owner,String name) {
        String create = "https://oapi.dingtalk.com/chat/create";
        try {
            DingTalkClient client = new DefaultDingTalkClient(create);
            OapiChatCreateRequest req = new OapiChatCreateRequest();
            req.setName(name);
            req.setOwner(owner);
            ArrayList<String> strings = new ArrayList<>();
            strings.add(owner);
            req.setUseridlist(strings);
            req.setSearchable(1L);
            req.setManagementType(1L);
            OapiChatCreateResponse rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(create + " : " + rsp.getBody());
            if (DingConfig.isSuccess(rsp)){
                return rsp;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + create);
    }


    /**
     * 修改会话
     * @return
     */
    public OapiChatUpdateResponse update(OapiChatUpdateRequest req) {
        String update = "https://oapi.dingtalk.com/chat/update";
        try {
            DingTalkClient client = new DefaultDingTalkClient(update);
            OapiChatUpdateResponse rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(update + " : " + rsp.getBody());
            if (DingConfig.isSuccess(rsp)){
                return rsp;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + update);
    }
    /**
     * 获取会话
     * @return
     */
    public OapiChatGetResponse get(String chatId) {
        String get = "https://oapi.dingtalk.com/chat/get";
        try {
            DingTalkClient client = new DefaultDingTalkClient(get);
            OapiChatGetRequest req = new OapiChatGetRequest();
            req.setChatid(chatId);
            req.setHttpMethod("GET");
            OapiChatGetResponse rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(get + " : " + rsp.getBody());
            if (DingConfig.isSuccess(rsp)){
                return rsp;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + get);
    }

}
