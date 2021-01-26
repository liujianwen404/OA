package com.ruoyi.base.dingTalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.request.OapiWorkrecordUpdateRequest;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.dingtalk.api.response.OapiWorkrecordUpdateResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DingWorkRecordApi {
    private static final Logger log = LoggerFactory.getLogger(DingWorkRecordApi.class);
    @Autowired
    private DingConfig dingConfig;

    /**
     * 新增待办事项
     * @return
     */
    public OapiWorkrecordAddResponse add(OapiWorkrecordAddRequest req) {
        String add = "https://oapi.dingtalk.com/topapi/workrecord/add";
        try {
            DingTalkClient client = new DefaultDingTalkClient(add);
            req.setSourceName("OA人力系统");
            OapiWorkrecordAddResponse rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(add + " : " + rsp.getBody());
            return rsp;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + add);
    }

    /**
     * 更新(移除)待办事项状态
     * @return
     */
    public OapiWorkrecordUpdateResponse update(String userId,String recordId) {
        String update = "https://oapi.dingtalk.com/topapi/workrecord/update";
        try {

            DingTalkClient client = new DefaultDingTalkClient(update);
            OapiWorkrecordUpdateRequest req = new OapiWorkrecordUpdateRequest();
            req.setUserid(userId);
            req.setRecordId(recordId);
            OapiWorkrecordUpdateResponse rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(update + " : " + rsp.getBody());
            return rsp;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + update);
    }
}
