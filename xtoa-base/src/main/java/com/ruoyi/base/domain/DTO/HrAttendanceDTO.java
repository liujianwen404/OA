package com.ruoyi.base.domain.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 考勤每日统计接收对象
 *
 */
@Data
public class HrAttendanceDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    private Long id;

    /** 员工id */
    @Column(name = "emp_id")
    private Long empId;

    @ExcelProperty("姓名")
    private String empName;

    @ExcelProperty("部门")
    private String deptName;

    @ExcelProperty("工号")
    private String empNum;

    @ExcelProperty("职位")
    private String postName;

    @ExcelProperty("日期")
    private String dates;

    @ExcelProperty("班次")
    private String classes;

    @ExcelProperty("考勤组")
    private String group;

    @ExcelProperty("出勤天数")
    private Long workDays;

    @ExcelProperty("休息天数")
    private Long restDays;

    @ExcelProperty("工作时长(分钟)")
    private String workMinuts;

    @ExcelProperty("上班1打卡时间")
    private String startTime;

    @ExcelProperty("下班1打卡时间")
    private String endTime;

    @ExcelProperty("迟到次数")
    private Long late;

    @ExcelProperty("迟到时长(分钟)")
    private Long lateTimes;

    @ExcelProperty("早退次数")
    private Long early;

    @ExcelProperty("早退时长(分钟)")
    private Long earlyTimes;

    @ExcelProperty("上班缺卡次数")
    private Long noClockIn;

    @ExcelProperty("下班缺卡次数")
    private Long noClockOff;

    @ExcelProperty("旷工天数")
    private Long absentDays;

    @ExcelProperty("出差时长")
    private Double onBusiness;

    @ExcelProperty("外出时长")
    private Double goOut;

}
