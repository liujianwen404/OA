package com.ruoyi.base.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 加班申请对象 t_hr_overtime
 * 
 * @author liujianwen
 * @date 2020-06-11
 */
@Data
@Table(name = "t_hr_overtime")
public class HrOvertime extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 加班类型 */
    @Column(name = "type")
    @Excel(name = "加班类型",readConverterExp = "1=法定假加班,2=平时加班")
    private String type;

    /** 标题 */
    @Column(name = "title")
    @Excel(name = "标题")
    private String title;

    /** 原因 */
    @Column(name = "reason")
    @Excel(name = "原因")
    private String reason;

    /** 开始时间 */
    @Column(name = "start_time")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @Column(name = "end_time")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 加班时长，单位小时 */
    @Column(name = "total_times")
    @Excel(name = "加班时长，单位小时")
    private Double totalTimes;

    /** 流程实例ID */
    @Column(name = "instance_id")
    private String instanceId;

    /** 部门id */
    @Column(name = "dept_id")
    private Long deptId;

    /** 部门名称 */
    @Column(name = "dept_name")
    private String deptName;

    /** 岗位id */
    @Column(name = "post_id")
    private Long postId;

    /** 岗位名称 */
    @Column(name = "post_name")
    private String postName;

    /** 申请人 */
    @Column(name = "apply_user")
    private String applyUser;

    /** 申请时间 */
    @Column(name = "apply_time")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 发起人工号 */
    @Column(name = "apply_user_num")
    private String applyUserNum;

    /** 发起人姓名 */
    @Column(name = "apply_user_name")
    @Excel(name = "申请人")
    private String applyUserName;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销) */
    @Column(name = "audit_status")
    @Excel(name = "审核状态", readConverterExp = "0=待提交,1=审核中,2=通过,3=拒绝,4=撤销")
    private String auditStatus;

    @Column(name = "emp_ids")
    private String empIds;

    @Column(name = "emp_id")
    private Long empId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
