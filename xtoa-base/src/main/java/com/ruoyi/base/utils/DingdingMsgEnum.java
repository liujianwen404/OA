package com.ruoyi.base.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.enums.ValueTextable;
import com.ruoyi.common.utils.enums.ValueTexts;

import java.util.Map;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public class DingdingMsgEnum {

	/**
     *
     * biz类型
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum MsgBizType implements ValueTextable<Integer> {
        projectTaskMsg(1, "项目任务代办提醒"),
        ;
        MsgBizType(Integer value, String text) {
            this.value = value;
            this.text = text;
        }
        private Integer value;
        private String text;
        private boolean selected;
        private String description;
        @Override
        public Integer getValue() {
            return value;
        }
        @Override
        public String getText() {
            return text;
        }
        @Override
        public String getDescription() {
            return description;
        }
        @Override
        public boolean isSelected() {
            return selected;
        }
        private static Map<Integer, String> map = ValueTexts.asMap(newArrayList(MsgBizType.values()));
        public static Map<Integer, String> asMap() {
            return map;
        }
    }

}
