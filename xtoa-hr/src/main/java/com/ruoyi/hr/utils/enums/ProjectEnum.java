package com.ruoyi.hr.utils.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.enums.ValueTextable;
import com.ruoyi.common.utils.enums.ValueTexts;

import java.util.Map;

import static cn.hutool.core.collection.CollUtil.newArrayList;

public class ProjectEnum {

	/**
     *
     * 操作(0:创建，1:编辑（内容），2:人员调整，3:状态调整，4:完成，5:关闭)
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum ProjectOperation implements ValueTextable<Integer> {
        creation(0, "创建"), edit(1, "编辑（内容）"), turnover(2, "人员调整"), statusAdjustment(3, "状态调整"), finish(4, "完成"), close(5, "关闭");
        ProjectOperation(Integer value, String text) {
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
        private static Map<Integer, String> map = ValueTexts.asMap(newArrayList(ProjectOperation.values()));
        public static Map<Integer, String> asMap() {
            return map;
        }
    }
    /**
     *
     * 类型（0:项目，1:迭代，2:任务）
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum ProjectType implements ValueTextable<Integer> {
        project(0, "项目"), plan(1, "项目迭代"), task(2, "项目迭代任务");
        ProjectType(Integer value, String text) {
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
        private static Map<Integer, String> map = ValueTexts.asMap(newArrayList(ProjectType.values()));
        public static Map<Integer, String> asMap() {
            return map;
        }
    }

    /**
     *
     * 状态(0:创建，1:完成，2:关闭)
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum ProjectStatus implements ValueTextable<Integer> {
        creation(0, "创建"), finish(1, "完成"), close(2, "关闭");
        ProjectStatus(Integer value, String text) {
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
        private static Map<Integer, String> map = ValueTexts.asMap(newArrayList(ProjectStatus.values()));
        public static Map<Integer, String> asMap() {
            return map;
        }
    }
    /**
     *
     * 优先级(0:普通，1:中等，2:高，3:加急)
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum ProjectPriority implements ValueTextable<Integer> {
        common(0, "普通"), medium(1, "中等"), high(2, "高"), urgent(2, "加急");
        ProjectPriority(Integer value, String text) {
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
        private static Map<Integer, String> map = ValueTexts.asMap(newArrayList(ProjectPriority.values()));
        public static Map<Integer, String> asMap() {
            return map;
        }
    }

}
