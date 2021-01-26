package com.ruoyi.common.enums;

import lombok.Data;
import lombok.Getter;

/**
 * 状态
 * 
 * @author
 */
@Getter
public enum ProjectPlanTaskStatus
{
    NOT_STARTED(0, "未开始"), PLANNED(1, "已计划"), PROGRAM(2, "设计中"),
    DEVELOPMENT(3, "研发中"), TEST_IN_PROCESS(4, "测试中"), APPROVED(5, "已验收"),
    RELEASED(6, "已发布"), CLOSED(7, "已关闭");

    private final Integer code;
    private final String value;

    ProjectPlanTaskStatus(Integer code, String value)
    {
        this.code = code;
        this.value = value;
    }

}
