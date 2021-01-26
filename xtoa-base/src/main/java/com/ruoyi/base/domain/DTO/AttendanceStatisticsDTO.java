package com.ruoyi.base.domain.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 考勤统计导入数据接收对象
 *
 */
@Data
public class AttendanceStatisticsDTO
{
    private static final long serialVersionUID = 1L;

    @ExcelProperty(index = 0,value = "员工ID")
    private Long empId;

    @ExcelProperty(index = 1,value = "统计日期")
    private String statisticsDate;

    @ExcelProperty(index = 2,value = "上月累计余假")
    private Double previousHoliday;

    @ExcelProperty(index = 3,value = "本月存休")
    private Double overtimeHoliday;

    @ExcelProperty(index = 4,value = "本月累计余假")
    private Double allHoliday;

    @ExcelProperty(index = 5,value = "全勤奖")
    private BigDecimal attendanceBonus;

    @ExcelProperty(index = 6,value = "夜班补贴")
    private BigDecimal nightSubsidy;

    @ExcelProperty(index = 7,value = "餐补")
    private BigDecimal mealSubsidy;

    @ExcelProperty(index = 8,value = "其他补贴")
    private BigDecimal otherSubsidy;

    @ExcelProperty(index = 9,value = "迟到扣款")
    private BigDecimal lateDeduct;

    @ExcelProperty(index = 10,value = "缺卡扣款")
    private BigDecimal notClockDeduct;

    @ExcelProperty(index = 11,value = "早退扣款")
    private BigDecimal earlyDeduct;

    @ExcelProperty(index = 12,value = "水电扣款")
    private BigDecimal waterElectricityDeduct;

    @ExcelProperty(index = 13,value = "其他扣款")
    private BigDecimal otherDeduct;

}
