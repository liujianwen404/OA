package com.ruoyi.base.dingTalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRecordRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DingAttendanceApi {
    private static final Logger log = LoggerFactory.getLogger(DingAttendanceApi.class);
    @Autowired
    private DingConfig dingConfig;

    /**
     * 根据当前登录员工钉钉ID查询考勤
     * @return
     */
    public OapiAttendanceListRecordResponse getAttendance(String dingUserid,String dateForm,String dateTo) {
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/listRecord");
            OapiAttendanceListRecordRequest req = new OapiAttendanceListRecordRequest();
            req.setUserIds(Arrays.asList(dingUserid));
            req.setCheckDateFrom(dateForm);
            req.setCheckDateTo(dateTo);
            OapiAttendanceListRecordResponse rsp = client.execute(req,dingConfig.getAccessToken());
            log.info("考勤数据{}" + rsp.getBody());
            return rsp;
        } catch (ApiException e) {
            e.printStackTrace();
            throw new RuntimeException("钉钉接口调用异常，请重试操作：" + "https://oapi.dingtalk.com/attendance/listRecord");
        }
    }
    /**
     * 获取用户详情
     * @return
     */
    public OapiUserGetResponse get(String dingUserid) {
        String get = "https://oapi.dingtalk.com/user/get";
        try {
            DingTalkClient client = new DefaultDingTalkClient(get);
            OapiUserGetRequest req = new OapiUserGetRequest();
            req.setUserid(dingUserid);
            req.setHttpMethod("GET");
            OapiUserGetResponse rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(get + " : " + rsp.getBody());
            return rsp;
        } catch (ApiException e) {
            e.printStackTrace();
            throw new RuntimeException("钉钉接口调用异常，请重试操作：" + get);
        }
    }
}
