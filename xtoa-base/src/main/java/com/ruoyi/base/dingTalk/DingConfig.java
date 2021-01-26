package com.ruoyi.base.dingTalk;

import cn.hutool.core.date.DateUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.Objects;

@PropertySource(value = "classpath:dingConfig.properties")
@ConfigurationProperties(prefix = "ding")
@Component
public class DingConfig {

    private static final Logger log = LoggerFactory.getLogger(DingConfig.class);


    private String appKey;

    private String appSecret;

    private Long agentId;

    private String corpId;

    private String ENCODING_AES_KEY;

    private String TOKEN;

    private static String accessToken;

    private static Date accessTokenExpired;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getENCODING_AES_KEY() {
        return ENCODING_AES_KEY;
    }

    public void setENCODING_AES_KEY(String ENCODING_AES_KEY) {
        this.ENCODING_AES_KEY = ENCODING_AES_KEY;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    /**
     * 获取企业凭证
     * @return
     */
    public String getAccessToken(){

        if (StringUtil.isEmpty(accessToken)){
            getToken();
        }else {
            if (accessTokenExpired.before(new Date())){
                getToken();
            }
        }

        return accessToken;
    }

    private void getToken() {
        String gettoken = "https://oapi.dingtalk.com/gettoken";
        try {
            Date date = new Date();
            DingTalkClient client = new DefaultDingTalkClient(gettoken);
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey(appKey);
            req.setAppsecret(appSecret);
            req.setHttpMethod("GET");
            OapiGettokenResponse rsp = client.execute(req);
            log.info(gettoken + " : " + rsp.getBody());
            if (DingConfig.isSuccess(rsp)){
                accessToken = rsp.getAccessToken();
                accessTokenExpired = DateUtil.offsetSecond(date,rsp.getExpiresIn().intValue() - 5);
                return;
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + gettoken);
    }

    public static Boolean isSuccess(TaobaoResponse response){
        if (!Objects.equals("0",response.getErrorCode())){
            return false;
        }
        return true;
    }


}
