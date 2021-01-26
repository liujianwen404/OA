package com.xtoa.web.dingBackCall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import com.dingtalk.oapi.lib.aes.Utils;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.base.service.MQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ISV 小程序回调信息处理
 */
@RestController
@RequestMapping("/webApi")
public class CallbackController {
    private static final Logger bizLogger = LoggerFactory.getLogger("BIZ_CALLBACKCONTROLLER");
    private static final Logger mainLogger = LoggerFactory.getLogger(CallbackController.class);
    /**
     * 创建应用，验证回调URL创建有效事件（第一次保存回调URL之前）
     */
    private static final String EVENT_CHECK_CREATE_SUITE_URL = "check_create_suite_url";
    /**
     * 创建应用，验证回调URL变更有效事件（第一次保存回调URL之后）
     */
    private static final String EVENT_CHECK_UPADTE_SUITE_URL = "check_update_suite_url";
    /**
     * suite_ticket推送事件
     */
    private static final String EVENT_SUITE_TICKET = "suite_ticket";
    /**
     * 企业授权开通应用事件
     */
    private static final String EVENT_TMP_AUTH_CODE = "tmp_auth_code";
    /**
     * 员工打卡事件
     */
    private static final String ATTENDANCE_CHECK_RECORD = "attendance_check_record";
    /**
     * 相应钉钉回调时的值
     */
    private static final String CALLBACK_RESPONSE_SUCCESS = "success";

    @Autowired
    private DingConfig dingConfig;

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Resource(name = "ThreadPoolExecutorForCallBack")
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private MQService mqService;

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> callback(@RequestParam(value = "signature", required = false) String signature,
                                        @RequestParam(value = "timestamp", required = false) String timestamp,
                                        @RequestParam(value = "nonce", required = false) String nonce,
                                        @RequestBody(required = false) JSONObject json) {
        String params = " signature:"+signature + " timestamp:"+timestamp +" nonce:"+nonce+" json:"+json;
        try {
            bizLogger.info("begin /callback"+params);

            String token = dingConfig.getTOKEN();
            String encoding_aes_key = dingConfig.getENCODING_AES_KEY();
            String corpId = dingConfig.getCorpId();

            bizLogger.info("token:"+ token +"~~~~~encoding_aes_key:"+ encoding_aes_key +"~~~~~corpId:"+ corpId);

            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(token, encoding_aes_key, corpId);
            //从post请求的body中获取回调信息的加密数据进行解密处理
            String encryptMsg = json.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encryptMsg);
            JSONObject obj = JSON.parseObject(plainText);

            bizLogger.info("json : " + obj.toJSONString());

            //根据回调数据类型做不同的业务处理
            String eventType = obj.getString("EventType");
            if (EVENT_CHECK_CREATE_SUITE_URL.equals(eventType)) {
                bizLogger.info("验证新创建的回调URL有效性: " + plainText);
            } else if (EVENT_CHECK_UPADTE_SUITE_URL.equals(eventType)) {
                bizLogger.info("验证更新回调URL有效性: " + plainText);
            } else if (EVENT_SUITE_TICKET.equals(eventType)) {
                //suite_ticket用于用签名形式生成accessToken(访问钉钉服务端的凭证)，需要保存到应用的db。
                //钉钉会定期向本callback url推送suite_ticket新值用以提升安全性。
                //应用在获取到新的时值时，保存db成功后，返回给钉钉success加密串（如本demo的return）
                bizLogger.info("应用suite_ticket数据推送: " + plainText);
            } else if (EVENT_TMP_AUTH_CODE.equals(eventType)) {
                //本事件应用应该异步进行授权开通企业的初始化，目的是尽最大努力快速返回给钉钉服务端。用以提升企业管理员开通应用体验
                //即使本接口没有收到数据或者收到事件后处理初始化失败都可以后续再用户试用应用时从前端获取到corpId并拉取授权企业信息，
                // 进而初始化开通及企业。
                bizLogger.info("企业授权开通应用事件: " + plainText);
            } else if (ATTENDANCE_CHECK_RECORD.equals(eventType)) {
                // 员工打卡事件处理
                bizLogger.info("员工打卡事件回调 : " + plainText);
                try {
                    Map<String,Object> map = new HashMap<>();
                    map.put("obj",obj.toJSONString());
                    map.put("eventType",eventType);
//                    mqService.sendMessage("attendanceFanoutExchange",map);
                    rabbitTemplate.convertAndSend("topicExchange", "topic.attendance", map);
                } catch (Exception e) {
                    e.printStackTrace();
                    bizLogger.info(e.getMessage());
                }
            } else {
                // 其他类型事件处理
                bizLogger.info("其他业务回调 : " + plainText);
                threadPoolExecutor.execute(() -> {
                    try {
                        Map<String,Object> map = new HashMap<>();
                        map.put("obj",obj.toJSONString());
                        map.put("eventType",eventType);
                        mqService.sendMessage("attendanceFanoutExchange",map);
                    } catch (Exception e) {
                        e.printStackTrace();
                        bizLogger.info(e.getMessage());
                    }
                });
            }
            // 返回success的加密信息表示回调处理成功
            return dingTalkEncryptor.getEncryptedMap(CALLBACK_RESPONSE_SUCCESS, System.currentTimeMillis(), Utils.getRandomStr(8));
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            mainLogger.error("process callback failed！"+params,e);
            return null;
        }
    }


}
