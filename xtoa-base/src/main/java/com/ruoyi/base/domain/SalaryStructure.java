package com.ruoyi.base.domain;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 薪资结构对象 t_salary_structure
 * 
 * @author xt
 * @date 2020-06-18
 */
@Table(name = "t_salary_structure")
@Data
public class SalaryStructure extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 薪资结构 */
    @Id
    @KeySql(useGeneratedKeys = true)
    @ExcelIgnore
    private Long id;

    /** 原始薪资结构id */
    @Column(name = "old_id")
    @ExcelIgnore
    private Long oldId;

    /** 员工id */
    @Column(name = "emp_id")
    @ExcelIgnore
    private Long empId;

    /** 归属公司 */
    @Transient
    @ColumnWidth(12)
    @ExcelProperty("归属公司")
    private String company = "深圳鲜恬";

    /** 工号 */
    @Transient
    @ColumnWidth(12)
    @ExcelProperty("工号")
    private String empNum;


    @Column(name = "emp_name")
    @ColumnWidth(12)
    @ExcelProperty("姓名")
    private String empName;

    /** 一级部门 */
    @Transient
    @ColumnWidth(12)
    @ExcelProperty("一级部门")
    private String deptName1;

    /** 二级部门 */
    @Transient
    @ColumnWidth(12)
    @ExcelProperty("二级部门")
    private String deptName2;

    /** 岗位 */
    @Transient
    @ColumnWidth(12)
    @ExcelProperty("岗位")
    private String postName;

    /** 入职日期 */
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ExcelIgnore
    private Date nonManagerDate;

    @Transient
    @ColumnWidth(12)
    @ExcelProperty("入职日期")
    private String nonManagerDateStr;
    public String getNonManagerDateStr() {
        return DateUtil.format(nonManagerDate,"yyyy-MM-dd");
    }

    /** 薪酬等级 */
    @Column(name = "pay_grade")
    @ColumnWidth(12)
    @ExcelProperty("薪酬等级")
    private String payGrade;

    /** 上班时间 */
    @Transient
    @ColumnWidth(12)
    @ExcelProperty("上班时间")
    private String attGroup;


    /** 调整日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "adjust_date")
    @ExcelIgnore
    private Date adjustDate;
    @Transient
    @ColumnWidth(12)
    @ExcelProperty("调整日期")
    private String adjustDateStr;
    public String getAdjustDateStr() {
        return DateUtil.format(adjustDate,"yyyy-MM-dd");
    }

    /** 数据所属月  */
    @JsonFormat(pattern = "yyyy-MM")
    @Column(name = "month_date")
    @ExcelIgnore
    private Date monthDate;
    @Transient
    @ColumnWidth(12)
    @ExcelProperty("数据所属月")
    private String monthDateStr;
    public String getMonthDateStr() {
        return DateUtil.format(monthDate,"yyyy-MM");
    }

    @Transient
    @ExcelIgnore
    private Date beginOfMonth;
    @Transient
    @ExcelIgnore
    private Date endOfMonth;


    /** 当月综合薪资 */
    @Column(name = "comprehensive")
    @ColumnWidth(12)
    @ExcelProperty("当月综合薪资")
    private BigDecimal comprehensive;


    /** 基本工资 */
    @Column(name = "basic")
    @ColumnWidth(12)
    @ExcelProperty("基本工资")
    private BigDecimal basic;

    /** 绩效奖金标准 */
    @Column(name = "performance_bonus")
    @ColumnWidth(12)
    @ExcelProperty("绩效奖金标准")
    private BigDecimal performanceBonus;

    /** 加班费 */
    @Column(name = "overtime_pay")
    @ColumnWidth(12)
    @ExcelProperty("加班费")
    private BigDecimal overtimePay;

    /** 岗位津贴 */
    @Column(name = "allowance")
    @ColumnWidth(12)
    @ExcelProperty("岗位津贴")
    private BigDecimal allowance;

    /** 其他补贴 */
    @Column(name = "other_subsidies")
    @ColumnWidth(12)
    @ExcelProperty("其他补贴")
    private BigDecimal otherSubsidies;



    /** 应出勤小时数 */
    @Transient
    @ExcelProperty("应出勤工时")
    private Double shouldAttendance;


    /** 实出勤小时数 */
    @Transient
    @ExcelProperty("实出勤工时")
    private Double actualAttendance;


    /** 基本工资(实际) */
    @Column(name = "basic_actual")
    @ColumnWidth(12)
    @ExcelProperty("基本工资(实际)")
    private BigDecimal basicActual;

    /** 加班费(实际) */
    @Column(name = "overtime_pay_actual")
    @ColumnWidth(12)
    @ExcelProperty("加班费(实际)")
    private BigDecimal overtimePayActual;

    /** 岗位津贴(实际) */
    @Column(name = "allowance_actual")
    @ColumnWidth(12)
    @ExcelProperty("岗位津贴(实际)")
    private BigDecimal allowanceActual;

    /** 绩效奖金标准(实际) */
    @Column(name = "performance_bonus_actual")
    @ColumnWidth(12)
    @ExcelProperty("绩效奖金标准(实际)")
    private BigDecimal performanceBonusActual;

    /** 其他补贴(实际) */
    @Column(name = "other_subsidies_actual")
    @ColumnWidth(12)
    @ExcelProperty("其他补贴(实际)")
    private BigDecimal otherSubsidiesActual;

    /** 提成 */
    @Column(name = "amortization")
    @ColumnWidth(12)
    @ExcelProperty("提成")
    private BigDecimal amortization;

    /** 全勤奖 */
    @Column(name = "attendance_bonus")
    @ColumnWidth(12)
    @ExcelProperty("全勤奖")
    private BigDecimal attendanceBonus;

    /** 夜班补贴 */
    @Column(name = "night_allowance")
    @ColumnWidth(12)
    @ExcelProperty("夜班补贴")
    private BigDecimal nightAllowance;

    /** 平时（加班费） */
    @Column(name = "overtime_actual")
    @ColumnWidth(12)
    @ExcelProperty("加班费")
    private BigDecimal overtimeActual;


    /** 应发合计 */
    @Column(name = "laballot")
    @ColumnWidth(12)
    @ExcelProperty("应发合计")
    private BigDecimal laballot;

    /** 水电费扣款 */
    @Column(name = "deduction_for_utilities")
    @ColumnWidth(12)
    @ExcelProperty("水电费扣款")
    private BigDecimal deductionForUtilities;

    /** 其他扣款 */
    @Column(name = "deduction_for_other")
    @ColumnWidth(12)
    @ExcelProperty("其他扣款")
    private BigDecimal deductionForOther;

    /** 税前所得 */
    @Column(name = "pretax_income")
    @ColumnWidth(12)
    @ExcelProperty("税前所得")
    private BigDecimal pretaxIncome;

    /** 个人社保 */
    @Column(name = "social_security")
    @ColumnWidth(12)
    @ExcelProperty("个人社保")
    private BigDecimal socialSecurity;

    /** 个人公积金 */
    @Column(name = "accumulation_fund")
    @ColumnWidth(12)
    @ExcelProperty("个人公积金")
    private BigDecimal accumulationFund;

    /** 个税 */
    @Column(name = "tallage")
    @ColumnWidth(12)
    @ExcelProperty("个税")
    private BigDecimal tallage;


    /** 实发工资 */
    @Column(name = "net_payroll")
    @ColumnWidth(12)
    @ExcelProperty("实发工资")
    private BigDecimal netPayroll;


    /** 是否为历史数据(0:不是，1：是) */
    @Column(name = "is_history")
    @ExcelIgnore
    private Integer isHistory;

    /** 调薪情况 */
    @Column(name = "salary_content")
    @ColumnWidth(12)
    @ExcelProperty("调薪情况")
    private String salaryContent;


    /** 流程实例id */
    @Column(name = "instance_id")
    @ExcelIgnore
    private String instanceId;
    /** 祖级部门(零时存储) */
    @Column(name = "ancestors")
    @ExcelIgnore
    private String ancestors;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销） */
    @Column(name = "audit_status")
    @ExcelIgnore
    private Integer auditStatus;

    @Transient
    @ExcelIgnore
    private Integer auditFlag;

    /** 申请人 */
    @Column(name = "apply_user")
    @ExcelIgnore
    private String applyUser;

    /** 申请人姓名 */
    @Column(name = "apply_user_name")
    @ExcelIgnore
    private String applyUserName;

    /** 申请时间 */
    @Column(name = "apply_time")
    @ExcelIgnore
    private Date applyTime;

    /** 部门id */
    @Transient
    @ExcelIgnore
    private Long deptId;

    /** 岗位id */
    @Transient
    @ExcelIgnore
    private Long postId;

    /** '是否为本月有效数据(0:不是，1：是) */
    @Column(name = "is_valid")
    @ExcelIgnore
    private Integer isValid;

    @Transient
    @ExcelIgnore
    private Integer count;

}
