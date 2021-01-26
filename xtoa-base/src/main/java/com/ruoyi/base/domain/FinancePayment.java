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
 * 财务付款流程对象 t_finance_payment
 * 
 * @author liujianwen
 * @date 2020-10-26
 */
@Data
@Table(name = "t_finance_payment")
public class FinancePayment extends BaseEntity
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

    /** 付款事由 */
    @Column(name = "reason")
    @Excel(name = "付款事由")
    private String reason;

    /** 付款类型 */
    @Column(name = "type")
    @Excel(name = "付款类型",readConverterExp = "1=预付款,2=货到付款")
    private String type;

    /** 公司类型 */
    @Column(name = "company_type")
    @Excel(name = "公司类型",readConverterExp = "1=广州,2=东莞,3=中山,4=深圳,5=武汉")
    private String companyType;

    /** 付款总额 */
    @Column(name = "amount")
    @Excel(name = "付款总额")
    private Double amount;

    /** 付款方式 */
    @Column(name = "mode")
    @Excel(name = "付款方式")
    private String mode;

    /** 支付日期 */
    @Column(name = "payment_date")
    @Excel(name = "支付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date paymentDate;

    /** 支付对象 */
    @Column(name = "object")
    @Excel(name = "支付对象")
    private String object;

    /** 开户行 */
    @Column(name = "bank")
    @Excel(name = "开户行")
    private String bank;

    /** 银行账户 */
    @Column(name = "account")
    @Excel(name = "银行账户")
    private String account;

    /** 附件名称 */
    @Column(name = "attachment")
    private String attachment;

    /** 附件路径 */
    @Column(name = "path")
    private String path;

    /** 员工id */
    @Column(name = "emp_id")
    private Long empId;

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

    /** 申请人姓名 */
    @Column(name = "apply_user_name")
    @Excel(name = "申请人姓名")
    private String applyUserName;

    /** 审核状态（0：待提交，1：审核中，2：通过，3：拒绝，4：撤销) */
    @Column(name = "audit_status")
    @Excel(name = "审核状态", readConverterExp = "审核状态（0：待提交，1：审核中，2：通过，3：拒绝，4：撤销)")
    private String auditStatus;

}
