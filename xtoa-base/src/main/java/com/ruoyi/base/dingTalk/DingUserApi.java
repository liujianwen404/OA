package com.ruoyi.base.dingTalk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiUserGetByMobileRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserGetByMobileResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.ruoyi.base.domain.VO.DingDeptVO;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DingUserApi {
    private static final Logger log = LoggerFactory.getLogger(DingUserApi.class);
    @Autowired
    private DingConfig dingConfig;


    /**
     * 根据手机号获取userid
     * @return
     */
    public OapiUserGetByMobileResponse getByMobile(String mobile) {
        String get_by_mobile = "https://oapi.dingtalk.com/user/get_by_mobile";
        try {
            DingTalkClient client = new DefaultDingTalkClient(get_by_mobile);
            OapiUserGetByMobileRequest req = new OapiUserGetByMobileRequest();
            req.setMobile(mobile);
            req.setHttpMethod("GET");
            OapiUserGetByMobileResponse rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(get_by_mobile + " : " + rsp.getBody());
            return rsp;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + get_by_mobile);
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
        }
        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + get);
    }


    /**
     * 获取dd部门列表
     */
    public void getDingDeptList() throws ApiException {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest req = new OapiDepartmentListRequest();
        req.setFetchChild(false);
        req.setId("1");
        req.setFetchChild(true);
        req.setHttpMethod("GET");
        OapiDepartmentListResponse rsp = client.execute(req, dingConfig.getAccessToken());
        log.info(rsp.getBody());
        JSONObject jsonObject= JSON.parseObject(rsp.getBody());
        List<DingDeptVO> list=JSONArray.parseArray(jsonObject.get("department").toString(),DingDeptVO.class);
        //获取集团下所有公司ids
        Set<Long> branchIds= list.stream().filter(nico->nico.getParentid()==342734972).map(DingDeptVO::getId).collect(Collectors.toSet());

        //TODO　预留


        log.info("ids===="+branchIds);


    }


}
