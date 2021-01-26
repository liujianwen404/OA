package com.ruoyi.base.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 转正申请对象 t_hr_regular
 * 
 * @author liujianwen
 * @date 2020-05-15
 */
@Data
@Table(name = "t_hr_regular")
public class HrRegular extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 标题 */
    @Column(name = "title")
    private String title;

    /** 原因 */
    @Column(name = "reason")
    private String reason;

    /** 转正时间 */
    @Column(name = "regular_time")
    private Date regularTime;

    @Column(name = "non_manager_date")
    @Excel(name = "入职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date nonManagerDate;

    @Column(name = "trial")
    @Excel(name = "试用期限")
    private String trial;

    @Column(name = "sum_up")
    @Excel(name = "试用期内对工作的总结")
    private String sumUp;

    @Column(name = "suggest")
    @Excel(name = "对公司的意见和建议")
    private String suggest;

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

    /** 流程实例ID */
    @Column(name = "instance_id")
    private String instanceId;

    /** 申请人 */
    @Column(name = "apply_user")
    private String applyUser;

    /** 申请人姓名 */
    @Column(name = "apply_user_name")
    @Excel(name = "申请人")
    private String applyUserName;

    /** 申请时间 */
    @Column(name = "apply_time")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销) */
    @Column(name = "audit_status")
    @Excel(name = "审核状态",readConverterExp = "0=待提交,1=审核中,2=通过,3=拒绝,4=撤销")
    private String auditStatus;

    /** 流程所属员工id */
    @Column(name = "emp_id")
    private Long empId;

    /** 附件 */
    @Column(name = "attachment")
    private String attachment;

}
