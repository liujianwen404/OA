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
 * 出差申请对象 t_hr_business_trip
 * 
 * @author liujianwen
 * @date 2020-06-30
 */
@Data
@Table(name = "t_hr_business_trip")
public class HrBusinessTrip extends BaseEntity
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

    /** 出差事由 */
    @Column(name = "reason")
    private String reason;

    /** 出差总时长 */
    @Column(name = "days")
    @Excel(name = "出差总时长")
    private Double days;

    /** 同行人 */
    @Column(name = "partner")
    @Excel(name = "同行人")
    private String partner;

    /** 类型 */
    @Column(name = "type")
    @Excel(name = "类型", readConverterExp = "0=省内,1=省外")
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

    /** 审批编号 */
    @Column(name = "number")
    private String number;

    @Column(name = "emp_id")
    private Long empId;

}
