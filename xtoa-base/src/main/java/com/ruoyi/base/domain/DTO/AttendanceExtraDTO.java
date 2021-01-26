package com.ruoyi.base.domain.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 考勤统计接收对象额外的数据
 *
 */
@Data
public class AttendanceExtraDTO
{
    private static final long serialVersionUID = 1L;

    /** 员工姓名 */
    @ExcelProperty(index = 0,value = "姓名")
    private String empName;

    @ExcelProperty(index = 1,value = "工号")
    private String empNum;

    @ExcelProperty(index = 2,value = "上班时长")
    private Double workDuration;

    @ExcelProperty(index = 3,value = "夜班补贴")
    private Double nightSubsidy;

    @ExcelProperty(index = 4,value = "上月累计余假")
    private Double previousHoliday;

    @ExcelProperty(index = 5,value = "年假剩余")
    private Double overAnnualLeave;


}
