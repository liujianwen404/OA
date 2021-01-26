package com.ruoyi.base.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
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
 * 补卡申请对象 t_hr_fill_clock
 *
 * @author liujianwen
 * @date 2020-06-24
 */

@Data
@ToString
@Table(name = "t_hr_fill_clock")
public class HrFillClock extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 原因 */
    @Column(name = "reason")
    @Excel(name = "未打卡原因")
    private String reason;

    /** 相关证明图片 */
    @Column(name = "img_urls")
    @Excel(name = "相关证明图片")
    private String imgUrls;

    /** 个人考勤记录表id */
    @Column(name = "attendance_info_id")
    @Excel(name = "个人考勤记录表id")
    private Long attendanceInfoId;

    /** 班次id */
    @Column(name = "group_id")
    @Excel(name = "考勤组id")
    private Long groupId;

    /** 班次id */
    @Column(name = "class_id")
    @Excel(name = "班次id")
    private Long classId;

    /** 班次日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "class_Date")
    @Excel(name = "班次日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date classDate;

    /** 补卡时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dates")
    @Excel(name = "补卡时间", dateFormat = "yyyy-MM-dd HH:mm")
    private Date dates;

    /** 补卡班次 */
    @Column(name = "classes")
    @Excel(name = "补卡班次")
    private String classes;

    /** 考勤类型(OnDuty：上班,OffDuty：下班) */
    @Column(name = "check_type")
    @Excel(name = "考勤类型(OnDuty：上班,OffDuty：下班)")
    private String checkType;


    /** 流程所属员工id */
    @Column(name = "emp_id")
    @Excel(name = "流程所属员工id")
    private Long empId;

    /** 发起人姓名 */
    @Column(name = "apply_user_name")
    @Excel(name = "发起人姓名")
    private String applyUserName;

    /** 流程实例ID */
    @Column(name = "instance_id")
    @Excel(name = "流程实例ID")
    private String instanceId;

    /** 部门ID */
    @Column(name = "dept_id")
    private Long deptId;

    /** 部门名称 */
    @Column(name = "dept_name")
    @Excel(name = "发起人部门")
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

    /** 申请时间 */
    @Column(name = "apply_time")
    private Date applyTime;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销) */
    @Column(name = "audit_status")
    private String auditStatus;

    @Transient
    private Integer count;
    @Transient
    private Integer expires;
    @Transient
    private String classDateStr;

    public String getClassDateStr() {
        return classDateStr;
    }

    public void setClassDateStr(String classDateStr) {
        this.classDateStr = classDateStr;
    }
}
