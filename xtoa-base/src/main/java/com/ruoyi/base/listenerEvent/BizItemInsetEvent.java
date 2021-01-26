package com.ruoyi.base.listenerEvent;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.ApplicationEvent;

public class BizItemInsetEvent extends ApplicationEvent {

    private JSONObject jsonObject;

    public BizItemInsetEvent(Object source,JSONObject jsonObject) {
        super(source);
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
