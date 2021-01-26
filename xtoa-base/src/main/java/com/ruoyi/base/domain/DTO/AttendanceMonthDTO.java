package com.ruoyi.base.domain.DTO;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 考勤统计接收对象
 *
 */
@Data
public class AttendanceMonthDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工姓名 */
    @Excel(name = "姓名")
    private String empName;

    @Excel(name = "考勤组")
    private String group;

    @Excel(name = "部门")
    private String deptName;

    @Excel(name = "工号")
    private String empNum;

    @Excel(name = "职位")
    private String postName;

    @Excel(name = "出勤天数")
    private Long workDays;

    @Excel(name = "休息天数")
    private Long restDays;

    @Excel(name = "工作时长(分钟)")
    private String workMinuts;

    @Excel(name = "迟到次数")
    private Long lateNums;

    @Excel(name = "迟到时长(分钟)")
    private Long lateMinutes;

    @Excel(name = "早退次数")
    private Long earlyNums;

    @Excel(name = "早退时长(分钟)")
    private Long earlyMinutes;

    @Excel(name = "上班缺卡次数")
    private Long toWorkNums;

    @Excel(name = "下班缺卡次数")
    private Long offWorkNums;

    @Excel(name = "旷工天数")
    private Long absentDays;

    @Excel(name = "出差时长")
    private Double onBusiness;

    @Excel(name = "外出时长")
    private Double goOut;

    @Excel(name = "加班总时长")
    private Double overtimes;

}
