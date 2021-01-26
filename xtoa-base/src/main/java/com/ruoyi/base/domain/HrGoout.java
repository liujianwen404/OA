package com.ruoyi.base.domain;

import java.util.Date;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外出申请对象 t_hr_goout
 * 
 * @author liujianwen
 * @date 2020-07-06
 */
@Data
@Table(name = "t_hr_goout")
public class HrGoout extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 标题 */
    @Column(name = "title")
    @Excel(name = "标题")
    private String title;

    /** 事由 */
    @Column(name = "reason")
    private String reason;

    /** 开始时间 */
    @Column(name = "start_time")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @Column(name = "end_time")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 时长，单位小时 */
    @Column(name = "total_times")
    @Excel(name = "时长，单位小时")
    private Double totalTimes;

    /** 出行方式 */
    @Column(name = "type")
    @Excel(name = "出行方式",readConverterExp = "1=滴滴,2=地铁/公交,3=其他")
    private String type;

    /** 流程实例ID */
    @Column(name = "instance_id")
    private String instanceId;

    /** 部门ID */
    @Column(name = "dept_id")
    private Long deptId;

    /** 部门名称 */
    @Column(name = "dept_name")
    private String deptName;

    /** 岗位ID */
    @Column(name = "post_id")
    private Long postId;

    /** 岗位名称 */
    @Column(name = "post_name")
    private String postName;

    /** 申请人 */
    @Column(name = "apply_user")
    private String applyUser;

    /** 发起人姓名 */
    @Column(name = "apply_user_name")
    @Excel(name = "申请人")
    private String applyUserName;

    /** 申请时间 */
    @Column(name = "apply_time")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 发起人工号 */
    @Column(name = "apply_user_num")
    private String applyUserNum;

    /** 审核状态（0：待提交，1：审核中，2：通过，3：拒绝，4：撤销) */
    @Column(name = "audit_status")
    @Excel(name = "审核状态", readConverterExp = "0=待提交,1=审核中,2=通过,3=拒绝,4=撤销")
    private String auditStatus;

    @Column(name = "emp_id")
    private Long empId;

}
