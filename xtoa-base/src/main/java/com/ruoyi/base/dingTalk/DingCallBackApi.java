package com.ruoyi.base.dingTalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCallBackDeleteCallBackRequest;
import com.dingtalk.api.request.OapiCallBackRegisterCallBackRequest;
import com.dingtalk.api.response.OapiCallBackDeleteCallBackResponse;
import com.dingtalk.api.response.OapiCallBackRegisterCallBackResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DingCallBackApi {
    private static final Logger log = LoggerFactory.getLogger(DingCallBackApi.class);
    @Autowired
    private DingConfig dingConfig;

    /**
     * 注册回调事件
     * @return
     */
    public OapiCallBackRegisterCallBackResponse registerCallBack(List<String> callBackTag,String dingCallBackUrl) {
        String register_call_back = "https://oapi.dingtalk.com/call_back/register_call_back";
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/register_call_back");
            OapiCallBackRegisterCallBackRequest req = new OapiCallBackRegisterCallBackRequest();
            req.setCallBackTag(callBackTag);
            req.setToken(dingConfig.getTOKEN());
            req.setAesKey(dingConfig.getENCODING_AES_KEY());
            req.setUrl(dingCallBackUrl);
            OapiCallBackRegisterCallBackResponse rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(register_call_back + " : " + rsp.getBody());
            if (DingConfig.isSuccess(rsp)){
                return rsp;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + register_call_back);
    }
    /**
     * 删除企业回调
     * @return
     */
    public OapiCallBackDeleteCallBackResponse deleteCallBack() {
        String delete_call_back = "https://oapi.dingtalk.com/call_back/delete_call_back";
        try {
            DingTalkClient client = new DefaultDingTalkClient(delete_call_back);
            OapiCallBackDeleteCallBackRequest req = new OapiCallBackDeleteCallBackRequest();
            req.setHttpMethod("GET");
            OapiCallBackDeleteCallBackResponse rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(delete_call_back + " : " + rsp.getBody());
            if (DingConfig.isSuccess(rsp)){
                return rsp;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + delete_call_back);
    }

}
