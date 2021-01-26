package com.ruoyi.base.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工作计划对象 t_hr_work_plan
 * 
 * @author liujianwen
 * @date 2020-08-20
 */
@Table(name = "t_hr_work_plan")
@Data
public class HrWorkPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 内容 */
    @Column(name = "content")
    @Excel(name = "内容")
    private String content;

    /** 总结 */
    @Column(name = "summary")
    @Excel(name = "总结")
    private String summary;

    /** 开始时间 */
    @Column(name = "start_time")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @Column(name = "end_time")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 完成时间 */
    @Column(name = "completion_time")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completionTime;

    /** 是否提醒（0代表不提醒 1代表提醒） */
    @Column(name = "is_remind")
    @Excel(name = "是否提醒", readConverterExp = "0=代表不提醒,1=代表提醒")
    private String isRemind;

    /** 附件 */
    @Column(name = "attachment")
    private String attachment;

    @Column(name = "emp_id")
    private Long empId;

    /** 部门名称 */
    @Column(name = "emp_name")
    @Excel(name = "员工姓名")
    private String empName;


    @Column(name = "dept_id")
    private Long deptId;

    /** 部门名称 */
    @Column(name = "dept_name")
    @Excel(name = "部门名称")
    private String deptName;

    @Column(name = "post_id")
    private Long postId;

    /** 岗位名称 */
    @Column(name = "post_name")
    @Excel(name = "岗位名称")
    private String postName;

    /** 任务表ID */
    @Column(name = "job_id")
    @Excel(name = "任务表ID")
    private Long jobId;

    @Transient
    private List<String> fileList;

}
