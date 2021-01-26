package com.ruoyi.base.dingTalk;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.enums.ValueTextable;
import com.ruoyi.common.utils.enums.ValueTexts;

import java.util.Map;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public class DingEventEnum {

	/**
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum ChatCallBack implements ValueTextable<String> {
        chat_add_member("chat_add_member", "群会话添加人员"),
        chat_remove_member("chat_remove_member", "群会话删除人员"),
        chat_quit("chat_quit", "群会话用户主动退群"),
        chat_update_owner("chat_update_owner", "群会话更换群主"),
        chat_update_title("chat_update_title", "群会话更换群名称"),
        chat_disband("chat_disband", "群会话解散群");
        ChatCallBack(String value, String text) {
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
        private static Map<String, String> map = ValueTexts.asMap(newArrayList(ChatCallBack.values()));
        public static Map<String, String> asMap() {
            return map;
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum AttendanceCallBack implements ValueTextable<String> {
        attendance_check_record("attendance_check_record", "员工打卡事件");
        /*attendance_schedule_change("attendance_schedule_change", "员工排班变更事件"),
        attendance_overtime_duration("attendance_overtime_duration", "员工加班事件"),*/

        AttendanceCallBack(String value, String text) {
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
        private static Map<String, String> map = ValueTexts.asMap(newArrayList(AttendanceCallBack.values()));
        public static Map<String, String> asMap() {
            return map;
        }
    }


}
