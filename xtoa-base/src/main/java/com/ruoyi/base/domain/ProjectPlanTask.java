package com.ruoyi.base.domain;

import java.util.Date;

import lombok.Data;
import lombok.ToString;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目计划任务对象 t_project_plan_task
 * 
 * @author xt
 * @date 2020-06-30
 */
@Data
@ToString
@Table(name = "t_project_plan_task")
public class ProjectPlanTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 项目id */
    @Column(name = "project_id")
    @Excel(name = "项目id")
    private Long projectId;
    @Transient
    private String projectName;

    /** 计划id */
    @Column(name = "project_plan_id")
    @Excel(name = "计划id")
    private Long projectPlanId;
    @Transient
    private String projectPlanName;

    /** 名称 */
    @Column(name = "name")
    @Excel(name = "名称")
    private String name;

    /** 描述 */
    @Column(name = "content_describe")
    @Excel(name = "描述")
    private String contentDescribe;

    @Column(name = "record_id")
    private String recordId;

    /** 负责人 */
    @Column(name = "emp_id")
    @Excel(name = "负责人")
    private Long empId;
    @Transient
    private String empName;

    /**
     *提出人
     */
    @Column(name = "introducer_id")
    private Long introducerId;

    @Transient
    private String introducerName;

    /** 开始时间 */
    @Column(name = "start_time")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @Column(name = "end_time")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 状态(0:创建，1:完成，2:关闭) */
    @Column(name = "status")
    @Excel(name = "状态(0:创建，1:完成，2:关闭)")
    private Integer status;

    /** 优先级(0:普通，1:中等，2:高，3:加急) */
    @Column(name = "priority")
    @Excel(name = "优先级(0:普通，1:中等，2:高，3:加急)")
    private Integer priority;

}
