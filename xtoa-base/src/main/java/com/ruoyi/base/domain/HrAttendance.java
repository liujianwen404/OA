package com.ruoyi.base.domain;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 考勤统计对象 t_hr_attendance
 * 
 * @author liujianwen
 * @date 2020-06-09
 */
@Data
@Table(name = "t_hr_attendance")
public class HrAttendance implements Serializable
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

    @ExcelProperty("区域")
    @Column(name = "district")
    private String district;

    /** 岗位id */
    @Column(name = "post_id")
    @ExcelIgnore
    private Long postId;

    /** 岗位名称 */
    @Column(name = "post_name")
    @ExcelProperty("岗位")
    private String postName;

    /** 员工姓名 */
    @Column(name = "emp_name")
    @ExcelProperty("姓名")
    private String empName;

    /** 工号 */
    @Column(name = "emp_num")
    @ExcelIgnore
    private String empNum;

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
    @ExcelProperty({"起止日期","结束日期（核算工龄用）"})
    private Date finalDate;

    /** 部门id */
    @Column(name = "dept_id")
    @ExcelIgnore
    private Long deptId;

    /** 部门名称 */
    @Column(name = "dept_name")
    @ExcelIgnore
    private String deptName;

    /** 应出勤 */
    @Column(name = "should_attendance")
    @ExcelProperty("应出勤")
    private Double shouldAttendance;

    /** 应公休 */
    @Column(name = "should_public_holiday")
    @ExcelProperty("公休")
    private Double shouldPublicHoliday;

    /** 享受法定假工时 */
    @Column(name = "legal_public_holiday")
    @ExcelProperty("享受法定假工时")
    private Double legalPublicHoliday;

    /** 实出勤天数 */
    @Column(name = "actual_attendance_day")
    @ExcelProperty({"实出勤情况","实出勤天数"})
    private Double actualAttendanceDay;

    /** 实出勤工时 */
    @Column(name = "actual_attendance")
    @ExcelProperty({"实出勤情况","实出勤工时"})
    private Double actualAttendance;

    /** 实公休 */
    @Column(name = "actual_public_holiday")
    @ExcelProperty({"本月考勤明细","实际公休"})
    private Double actualPublicHoliday;

    /** 实公休 */
    @ExcelIgnore
    @Column(name = "actual_public_holiday_day")
    private Double actualPublicHolidayDay;

    /** 法定假加班 */
    @Column(name = "legal_overtime")
    @ExcelProperty({"本月考勤明细","法定假加班"})
    private Double legalOvertime;

    /** 平时加班 */
    @Column(name = "general_overtime")
    @ExcelProperty({"本月考勤明细","平时加班"})
    private Double generalOvertime;

    /** 调休 */
    @Column(name = "lieu_leave")
    @ExcelProperty({"本月考勤明细","调休"})
    private Double lieuLeave;

    /** 病假 */
    @Column(name = "sick_leave")
    @ExcelProperty({"本月考勤明细","病假"})
    private Double sickLeave;

    /** 事假 */
    @Column(name = "personal_leave")
    @ExcelProperty({"本月考勤明细","事假"})
    private Double personalLeave;

    /** 产假，陪产假，婚假，丧假 */
    @Column(name = "other_leave")
    @ExcelProperty({"本月考勤明细","产假 陪产假 婚假 丧假"})
    private Double otherLeave;

    /** 年假 */
    @Column(name = "annual_leave")
    @ExcelProperty({"本月考勤明细","年假"})
    private Double annualLeave;

    /** 旷工 */
    @Column(name = "absent_leave")
    @ExcelProperty({"本月考勤明细","旷工"})
    private Double absentLeave;

    /** 上月累计余假 */
    @Column(name = "previous_holiday")
    @ExcelProperty({"存休情况","上月累计余假"})
    private Double previousHoliday;

    /** 本月加班存休 */
    @Column(name = "overtime_holiday")
    @ExcelProperty({"存休情况","本月存休"})
    private Double overtimeHoliday;

    /** 本月累计余假 */
    @Column(name = "all_holiday")
    @ExcelProperty({"存休情况","本月累计余假"})
    private Double allHoliday;

    /** 计薪工时 */
    @Column(name = "pay_work_hour")
    @ExcelProperty("计薪工时")
    private Double payWorkHour;

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
    @ExcelProperty({"奖励/元","全勤奖"})
    private Double attendanceBonus;

    /** 夜班补贴 */
    @Column(name = "night_subsidy")
    @ExcelProperty({"奖励/元","夜班补贴"})
    private Double nightSubsidy;

    /** 餐补 */
    @Column(name = "meal_subsidy")
    @ExcelProperty({"奖励/元","餐补"})
    private Double mealSubsidy;

    /** 其他补贴 */
    @Column(name = "other_subsidy")
    @ExcelProperty({"奖励/元","其他"})
    private Double otherSubsidy;

    /** 迟到扣费 */
    @Column(name = "late_deduct")
    @ExcelProperty({"应扣","出勤异常代扣","迟到扣费"})
    private Double lateDeduct;

    /** 未打卡扣费 */
    @Column(name = "not_clock_deduct")
    @ExcelProperty({"应扣","出勤异常代扣","补卡/未打卡"})
    private Double notClockDeduct;

    /** 早退扣费 */
    @Column(name = "early_deduct")
    @ExcelProperty({"应扣","出勤异常代扣","早退"})
    private Double earlyDeduct;

    /** 水电扣费 */
    @Column(name = "water_electricity_deduct")
    @ExcelProperty({"应扣","水电扣费"})
    private Double waterElectricityDeduct;

    /** 其他扣费 */
    @Column(name = "other_deduct")
    @ExcelProperty({"应扣","其他扣费"})
    private Double otherDeduct;

    @Column(name = "remark")
    @ExcelProperty("备注")
    private String remark;

    /** 员工签名 */
    @Column(name = "emp_signature")
    @ExcelProperty("员工签名")
    private String empSignature;

    /** 上班时长（几小时工作制） */
    @ExcelIgnore
    @Column(name = "work_duration")
    private Double workDuration;

    /** 迟到次数 */
    @ExcelIgnore
    @Column(name = "late_nums")
    private Long lateNums;

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

    /** 删除标志（0代表存在 1代表删除） */
    @ExcelIgnore
    private String delFlag;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getNonManagerDate() {
        return nonManagerDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getQuitDate() {
        return quitDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getFinalDate() {
        return finalDate;
    }
}
