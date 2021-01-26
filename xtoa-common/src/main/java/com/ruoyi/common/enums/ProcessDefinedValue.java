package com.ruoyi.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.enums.ValueTextable;
import com.ruoyi.common.utils.enums.ValueTexts;

import java.util.Map;

import static cn.hutool.core.collection.CollUtil.newArrayList;

/**
 * 操作状态
 * 
 * @author
 */
public class ProcessDefinedValue
{
    /**
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum UserTaskAssigneeForDeptLeader implements ValueTextable<String> {
        deptLeader01("deptLeader01", "第1级部门负责人"),
        deptLeader02("deptLeader02", "第2级部门负责人"),
        deptLeader03("deptLeader03", "第3级部门负责人"),
        deptLeader04("deptLeader04", "第4级部门负责人"),
        deptLeader05("deptLeader05", "第5级部门负责人"),
        deptLeader06("deptLeader06", "第6级部门负责人")
        ;
        UserTaskAssigneeForDeptLeader(String value, String text) {
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
        private static Map<String, String> map = ValueTexts.asMap(newArrayList(UserTaskAssigneeForDeptLeader.values()));
        public static Map<String, String> asMap() {
            return map;
        }
    }


}
