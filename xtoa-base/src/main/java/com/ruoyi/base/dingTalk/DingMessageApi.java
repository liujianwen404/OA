package com.ruoyi.base.dingTalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.ruoyi.base.dingTalk.dingCallBackDTO.RobotSendMsgDTO;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DingMessageApi {
    @Autowired
    private DingConfig dingConfig;

    /**
     * 新增待办事项
     * @return
     */
    public OapiMessageCorpconversationAsyncsendV2Response asyncsend_v2(OapiMessageCorpconversationAsyncsendV2Request req) {
        String asyncsend_v2 = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
        try {
            DingTalkClient client = new DefaultDingTalkClient(asyncsend_v2);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(req, dingConfig.getAccessToken());
            log.info(asyncsend_v2 + " : " + rsp.getBody());
            return rsp;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("钉钉接口调用异常，请重试操作:：" + asyncsend_v2);
    }

    /**
     * 发送消息到群组
     * @param dto
     * @throws ApiException
     */
    public void sendTextToGroupMsg(RobotSendMsgDTO dto) throws ApiException {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/chat/send");
        OapiChatSendRequest req = new OapiChatSendRequest();
        req.setChatid(dto.getChatId());
        req.setMsgtype("markdown");
        OapiChatSendRequest.Markdown markdown = new OapiChatSendRequest.Markdown();
        markdown.setTitle(dto.getTitle());
        markdown.setText("标题："+dto.getTitle()+"  \n  "+"所属项目："+dto.getProjectName()
                +"  \n  "+"负责人："+dto.getEmpName()+"  \n  开始时间："+dto.getStartTime()+"  \n  结束时间："+dto.getEndTime()+"  \n  状态："+dto.getStatus());
        req.setMarkdown(markdown);

        OapiChatSendResponse rsp = client.execute(req, dingConfig.getAccessToken());
//        log.info(rsp.getBody());

    }

}
