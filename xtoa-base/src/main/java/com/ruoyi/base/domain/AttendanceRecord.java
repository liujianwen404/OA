package com.ruoyi.base.domain;

import java.util.Date;

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
 * 考勤记录对象 t_attendance_record
 * 
 * @author liujianwen
 * @date 2020-06-17
 */
@Data
@Table(name = "t_attendance_record")
public class AttendanceRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 员工ID */
    @Column(name = "emp_id")
    @Excel(name = "员工ID")
    private Long empId;

    /** 工号 */
    @Column(name = "emp_num")
    @Excel(name = "工号")
    private String empNum;

    /** 员工姓名 */
    @Column(name = "emp_name")
    @Excel(name = "员工姓名")
    private String empName;

    /** 上班打卡时间 */
    @Column(name = "start_time")
    @Excel(name = "上班打卡时间")
    private String startTime;

    /** 下班打卡时间 */
    @Column(name = "end_time")
    @Excel(name = "下班打卡时间")
    private String endTime;

    /** 打卡日期 */
    @Column(name = "dates")
    @Excel(name = "打卡日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dates;

    /** 考勤班次 */
    @Column(name = "classes")
    @Excel(name = "考勤班次")
    private String classes;

    @Column(name = "type")
    private String type;

    @Column(name = "late")
    @Excel(name = "迟到")
    private Long late;

    @Column(name = "late_times")
    @Excel(name = "迟到时长(分钟)")
    private Long lateTimes;

    @Column(name = "late_num")
    @Excel(name = "迟到次数")
    private Long lateNum;

    @Column(name = "late_fines")
    @Excel(name = "迟到罚金")
    private Double lateFines;

    @Column(name = "early")
    @Excel(name = "早退")
    private Long early;

    @Column(name = "early_times")
    @Excel(name = "早退时长(分钟)")
    private Long earlyTimes;

    @Column(name = "early_num")
    @Excel(name = "迟到次数")
    private Long earlyNum;

    @Column(name = "early_fines")
    @Excel(name = "迟到罚金")
    private Double earlyFines;


    @Column(name = "no_clock_in")
    @Excel(name = "上班缺卡")
    private Long noClockIn;

    @Column(name = "no_clock_off")
    @Excel(name = "下班缺卡")
    private Long noClockOff;

    @Column(name = "night_subsidy")
    @Excel(name = "夜班补贴")
    private Double nightSubsidy;

    @Column(name = "meal_subsidy")
    @Excel(name = "餐补")
    private Double mealSubsidy;


}
