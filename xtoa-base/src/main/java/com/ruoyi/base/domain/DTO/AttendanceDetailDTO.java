package com.ruoyi.base.domain.DTO;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 考勤月度统计接收对象
 *
 */
@Data
public class AttendanceDetailDTO
{
    private static final long serialVersionUID = 1L;

    /** 员工姓名 */
    @ExcelProperty(index = 0,value = "姓名")
    private String empName;

    @ExcelProperty(index = 1,value = "考勤组")
    private String group;

    @ExcelProperty(index = 2,value = "部门")
    private String deptName;

    @ExcelIgnore
    private String district;

    @ExcelProperty(index = 3,value = "工号")
    private String empNum;

    @ExcelProperty(index = 4,value = "职位")
    private String postName;

    @ExcelProperty(index = 6,value = "出勤天数")
    private Long workDays;

    @ExcelProperty(index = 7,value = "休息天数")
    private Long restDays;

    @ExcelProperty(index = 8,value = "工作时长(分钟)")
    private String workMinuts;

    @ExcelProperty(index = 9,value = "迟到次数")
    private Long lateNums;

    @ExcelProperty(index = 10,value = "迟到时长(分钟)")
    private Long lateMinutes;

    @ExcelProperty(index = 14,value = "早退次数")
    private Long earlyNums;

    @ExcelProperty(index = 15,value = "早退时长(分钟)")
    private Long earlyMinutes;

    @ExcelProperty(index = 16,value = "上班缺卡次数")
    private Long toWorkNums;

    @ExcelProperty(index = 17,value = "下班缺卡次数")
    private Long offWorkNums;

    @ExcelProperty(index = 18,value = "旷工天数")
    private Long absentDays;

    @ExcelProperty(index = 19,value = "出差时长")
    private Double onBusiness;

    @ExcelProperty(index = 20,value = "外出时长")
    private Double goOut;

    /** 年假 */
    @ExcelProperty(index = 21)
    private Double annualLeave;

    /** 事假 */
    @ExcelProperty(index = 22)
    private Double personalLeave;

    /** 病假 */
    @ExcelProperty(index = 23)
    private Double sickLeave;

    /** 调休 */
    @ExcelProperty(index = 24)
    private Double lieuLeave;

    @ExcelProperty(index = 25,value = "产假(天)")
    private Double maternityLeave;

    @ExcelProperty(index = 26,value = "陪产假(天)")
    private Double paternityLeave;

    @ExcelProperty(index = 27,value = "婚假(天)")
    private Double marriageLeave;

    @ExcelProperty(index = 28,value = "例假(天)")
    private Double girlsLeave;

    @ExcelProperty(index = 29,value = "丧假(天)")
    private Double funeralLeave;

    @ExcelProperty(index = 30,value = "哺乳假(小时)")
    private Double breastfeedingLeave;

    @ExcelProperty(index = 31,value = "加班总时长")
    private Double overtimes;

    @ExcelProperty(index = 32,value = "工作日（转调休）")
    private Double workLieu;

    @ExcelProperty(index = 33,value = "休息日（转调休）")
    private Double restLieu;

    @ExcelProperty(index = 34,value = "节假日（转调休）")
    private Double holidayLieu;

}
