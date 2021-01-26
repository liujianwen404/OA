package com.ruoyi.base.dingTalk.dingCallBackDTO;

import java.util.List;

public class ChatCallBackDTO {

    /**
     * 事件类型
     */
    private String EventType;
    /**
     * 时间戳
     */
    private Long TimeStamp;
    /**
     * 发生群会话变更的企业
     */
    private String CorpId;
    /**
     * 会话的ID
     */
    private String ChatId;
    /**
     * 用户发生变更的userid列表
     */
    private List<String> UserId;
    /**
     * 已经更新的新的群主的userid
     */
    private String Owner;
    /**
     * 已经更新的新的群标题
     */
    private String Title;
    /**
     * 操作人员的userid
     */
    private String Operator;
    /**
     * 群会话绑定的微应用agentId
     */
    private String agentId;

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getCorpId() {
        return CorpId;
    }

    public void setCorpId(String corpId) {
        CorpId = corpId;
    }

    public String getChatId() {
        return ChatId;
    }

    public void setChatId(String chatId) {
        ChatId = chatId;
    }

    public List<String> getUserId() {
        return UserId;
    }

    public void setUserId(List<String> userId) {
        UserId = userId;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "ChatCallBackDTO{" +
                "EventType='" + EventType + '\'' +
                ", TimeStamp=" + TimeStamp +
                ", CorpId='" + CorpId + '\'' +
                ", ChatId='" + ChatId + '\'' +
                ", UserId=" + UserId +
                ", Owner='" + Owner + '\'' +
                ", Title='" + Title + '\'' +
                ", Operator='" + Operator + '\'' +
                ", agentId='" + agentId + '\'' +
                '}';
    }
}
