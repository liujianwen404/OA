package com.ruoyi.base.domain.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * 考勤统计导入数据接收对象
 *
 */
@Data
public class SalaryStructureDTO
{
    private static final long serialVersionUID = 1L;

    @ExcelProperty(index = 0,value = "员工工号")
    private String empNum;

    @ExcelProperty(index = 1,value = "日期")
    private String monthDate;

    @ExcelProperty(index = 2,value = {"当月出勤工资核算","绩效工资"})
    private BigDecimal performanceBonusActual;

    @ExcelProperty(index = 3,value = {"当月出勤工资核算","全勤奖"})
    private BigDecimal attendanceBonus;

    @ExcelProperty(index = 4,value = {"当月出勤工资核算","夜班补贴"})
    private BigDecimal nightAllowance;

    @ExcelProperty(index = 5,value = {"当月出勤工资核算","平时加班费"})
    private BigDecimal overtimeActual;

    @ExcelProperty(index = 6,value = {"当月出勤工资核算","其他补贴"})
    private BigDecimal otherSubsidiesActual;

    @ExcelProperty(index = 7,value ={"当月出勤工资核算", "提成"})
    private BigDecimal amortization;

    @ExcelProperty(index = 8,value = {"扣款","水电费"})
    private BigDecimal deductionForUtilities;

    @ExcelProperty(index = 9,value = {"扣款","其他"})
    private BigDecimal deductionForOther;

    @ExcelProperty(index = 10,value = {"法定扣款","个人社保"})
    private BigDecimal socialSecurity;

    @ExcelProperty(index = 11,value = {"法定扣款","个人工资金"})
    private BigDecimal accumulationFund;

    @ExcelProperty(index = 12,value = {"法定扣款","个税"})
    private BigDecimal tallage;


}
