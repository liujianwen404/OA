package com.ruoyi.base.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考勤统计对象 t_hr_attendance_statistics
 * 
 * @author liujianwen
 * @date 2020-08-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_hr_attendance_statistics")
public class HrAttendanceStatistics extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    @KeySql(useGeneratedKeys = true)
    @ExcelIgnore
    private Long id;

    /** 员工id */
    @Column(name = "emp_id")
    @ExcelIgnore
    private Long empId;

    /** 工号 */
    @Column(name = "emp_num")
    @ExcelProperty("工号")
    private String empNum;

    /** 员工姓名 */
    @Column(name = "emp_name")
    @ExcelProperty("员工姓名")
    private String empName;

    /** 一级部门id */
    @Column(name = "first_dept_id")
    @ExcelIgnore
    private Long firstDeptId;

    /** 一级部门名称 */
    @Column(name = "first_dept_name")
    @ExcelProperty("一级部门")
    private String firstDeptName;

    /** 二级部门id */
    @Column(name = "second_dept_id")
    @ExcelIgnore
    private Long secondDeptId;

    /** 二级部门名称 */
    @Column(name = "second_dept_name")
    @ExcelProperty("二级部门")
    private String secondDeptName;

    /** 部门id */
    @Column(name = "dept_id")
    @ExcelIgnore
    private Long deptId;

    /** 部门名称 */
    @Column(name = "dept_name")
    @ExcelIgnore
    private String deptName;

    /** 区域 */
    @Column(name = "district")
    @ExcelIgnore
    private String district;

    /** 岗位id */
    @Column(name = "post_id")
    @ExcelIgnore
    private Long postId;

    /** 岗位名称 */
    @Column(name = "post_name")
    @ExcelProperty("岗位")
    private String postName;

    /** 入职日期 */
    @Column(name = "non_manager_date")
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty({"起止日期","入职日期"})
    private Date nonManagerDate;

    /** 离职日期 */
    @Column(name = "quit_date")
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty({"起止日期","离职日期"})
    private Date quitDate;

    /** 结算日期 */
    @Column(name = "final_date")
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty({"起止日期","结束日期（核算工龄）"})
    private Date finalDate;

    /** 班次ID */
    @Column(name = "class_id")
    @ExcelIgnore
    private String classId;

    /** 班次名称 */
    @Column(name = "class_name")
    @ExcelProperty("上班班次")
    private String className;

    /** 应出勤小时数 */
    @Column(name = "should_attendance")
    @ExcelProperty({"出勤情况","应出勤工时"})
    private Double shouldAttendance;

    /** 应公休小时数 */
    @Column(name = "should_public_holiday")
    @ExcelProperty({"出勤情况","公休"})
    private Double shouldPublicHoliday;

    /** 实出勤小时数 */
    @Column(name = "actual_attendance")
    @ExcelProperty({"出勤情况","实出勤工时"})
    private Double actualAttendance;

    /** 享受法定假工时 */
    @Column(name = "legal_public_holiday")
    @ExcelProperty({"休假小时","法定假"})
    private Double legalPublicHoliday;

    /** 实公休小时数 */
    @Column(name = "actual_public_holiday")
    @ExcelProperty({"休假小时","公休"})
    private Double actualPublicHoliday;

    /** 病假 */
    @Column(name = "sick_leave")
    @ExcelProperty({"休假小时","病假"})
    private Double sickLeave;

    /** 事假 */
    @Column(name = "personal_leave")
    @ExcelProperty({"休假小时","事假"})
    private Double personalLeave;

    /** 调休 */
    @Column(name = "lieu_leave")
    @ExcelProperty({"休假小时","调休"})
    private Double lieuLeave;

    /** 产假，陪产假，婚假，丧假 */
    @Column(name = "other_leave")
    @ExcelProperty({"休假小时","产假陪产假婚假丧假"})
    private Double otherLeave;

    /** 年假 */
    @Column(name = "annual_leave")
    @ExcelProperty({"休假小时","年假"})
    private Double annualLeave;

    /** 旷工 */
    @Column(name = "absent_leave")
    @ExcelProperty({"休假小时","旷工"})
    private Double absentLeave;

    /** 计薪工时 */
    @Column(name = "pay_work_hour")
    @ExcelProperty("计薪工时")
    private Double payWorkHour;

    /** 实出勤天数 */
    @Column(name = "actual_attendance_day")
    @ExcelProperty("实出勤天数")
    private Double actualAttendanceDay;

    /** 实公休天数 */
    @Column(name = "actual_public_holiday_day")
    @ExcelIgnore
    private Double actualPublicHolidayDay;

    /** 平时加班 */
    @Column(name = "general_overtime")
    @ExcelProperty({"加班小时","平时加班"})
    private Double generalOvertime;

    /** 法定假加班 */
    @Column(name = "legal_overtime")
    @ExcelProperty({"加班小时","法定假加班"})
    private Double legalOvertime;

    /** 上班时长（几小时工作制） */
    @Column(name = "work_duration")
    @ExcelIgnore
    private Double workDuration;

    /** 上月累计余假 */
    @Column(name = "previous_holiday")
    @ExcelProperty({"存假统计","上月累计余假"})
    private Double previousHoliday;

    /** 本月加班存休 */
    @Column(name = "overtime_holiday")
    @ExcelProperty({"存假统计","本月存休"})
    private Double overtimeHoliday;

    /** 本月累计余假 */
    @Column(name = "all_holiday")
    @ExcelProperty({"存假统计","本月累计余假"})
    private Double allHoliday;

    /** 工龄 */
    @Column(name = "working_age")
    @ExcelProperty({"工龄年假","工龄"})
    private Double workingAge;

    /** 剩余年假 */
    @Column(name = "over_annual_leave")
    @ExcelProperty({"工龄年假","年假剩余"})
    private Double overAnnualLeave;

    /** 全勤奖 */
    @Column(name = "attendance_bonus")
    @ExcelProperty({"补贴类","全勤奖"})
    private BigDecimal attendanceBonus;

    /** 夜班补贴 */
    @Column(name = "night_subsidy")
    @ExcelProperty({"补贴类","夜班补贴"})
    private BigDecimal nightSubsidy;

    /** 餐补 */
    @Column(name = "meal_subsidy")
    @ExcelProperty({"补贴类","餐补"})
    private BigDecimal mealSubsidy;

    /** 其他补贴 */
    @Column(name = "other_subsidy")
    @ExcelProperty({"补贴类","其他"})
    private BigDecimal otherSubsidy;

    /** 迟到次数 */
    @Column(name = "late_nums")
    @ExcelIgnore
    private Long lateNums;

    /** 迟到扣费 */
    @Column(name = "late_deduct")
    @ExcelProperty({"出勤异常代扣","迟到扣款"})
    private BigDecimal lateDeduct;

    /** 未打卡扣费 */
    @Column(name = "not_clock_deduct")
    @ExcelProperty({"出勤异常代扣","缺卡扣款"})
    private BigDecimal notClockDeduct;

    /** 早退扣费 */
    @Column(name = "early_deduct")
    @ExcelProperty({"出勤异常代扣","早退扣款"})
    private BigDecimal earlyDeduct;

    /** 水电扣费 */
    @Column(name = "water_electricity_deduct")
    @ExcelProperty("水电扣款")
    private BigDecimal waterElectricityDeduct;

    /** 其他扣费 */
    @Column(name = "other_deduct")
    @ExcelProperty("其他扣款")
    private BigDecimal otherDeduct;

    /** 统计日期 */
    @Column(name = "statistics_date")
    @ExcelIgnore
    private String statisticsDate;

    /** 创建者ID */
    @ExcelIgnore
    private Long createId;

    /** 创建者 */
    @ExcelIgnore
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", insertable = false, updatable = false)
    @ExcelIgnore
    private Date createTime;

    /** 更新者ID */
    @ExcelIgnore
    private Long updateId;

    /** 更新者 */
    @ExcelIgnore
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time" ,insertable = false, updatable = false)
    @ExcelIgnore
    private Date updateTime;

    /** 备注 */
    @ExcelProperty("备注")
    private String remark;

    /** 删除标志（0代表存在 1代表删除） */
    @ExcelIgnore
    private String delFlag;

    /** 员工签名 */
    @Column(name = "emp_signature")
    @ExcelProperty("员工签名")
    private String empSignature;

    /** 状态 */
    @Column(name = "status")
    private String status;

}
