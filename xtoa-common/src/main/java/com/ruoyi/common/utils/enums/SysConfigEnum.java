package com.ruoyi.common.utils.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Map;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public class SysConfigEnum {

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum SysConfig implements ValueTextable<String> {
        projectTaskImage("projectTask.image", "代办任务提醒图标"),
        shift_critical_point("shift_critical_point", "班次临界点"),
        processImage("process_image", "流程审批通知");

        SysConfig(String value, String text) {
            this.value = value;
            this.text = text;
        }
        private String value;
        private String text;
        private boolean selected;
        private String description;
        @Override
        public String getValue() {
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
        private static Map<String, String> map = ValueTexts.asMap(newArrayList(SysConfig.values()));
        public static Map<String, String> asMap() {
            return map;
        }
    }

}
