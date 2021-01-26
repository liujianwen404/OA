package com.ruoyi.base.domain.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


@Data
public class ProjectPlanTaskVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 任务id */
    private Long id;

    /** 项目id */
    private Long projectId;

    private String projectName;

    /** 计划id */
    private Long projectPlanId;

    private String projectPlanName;

    /** 名称 */
    private String name;

    /** 描述 */
    private String contentDescribe;

    private String recordId;

    /** 负责人 */
    private Long empId;

    private String empName;

    /**
     *提出人
     */
    private Long introducerId;

    private String introducerName;

    /** 开始时间 */
    @JsonFormat(pattern="yyyy年MM月dd日",timezone="GMT+8")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern="yyyy年MM月dd日",timezone="GMT+8")
    private Date endTime;

    /** 状态(0:创建，1:完成，2:关闭) */
    private Integer status;

    /** 优先级(0:普通，1:中等，2:高，3:加急) */
    private Integer priority;

    @JsonFormat(pattern="yyyy年MM月dd日",timezone="GMT+8")
    private Date createTime;
}
