package com.ruoyi.base.domain.DTO;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 考勤统计导入数据接收对象
 *
 */
@Data
public class HolidayDTO
{
    private static final long serialVersionUID = 1L;

    @ColumnWidth(15)
    @ExcelProperty(index = 0,value = "员工工号")
    private String empNum;

    @ExcelIgnore
    private Long empId;

    @ColumnWidth(15)
    @ExcelProperty(index = 1,value = "假期类型")
    private String typeStr;

    @ColumnWidth(15)
    @ExcelProperty(index = 2,value = "假期时长（单位小时）")
    private Double hours;

    @DateTimeFormat("yyyy/MM/dd")
    @ColumnWidth(15)
    @ExcelProperty(index = 3,value = "开始日期")
    private String startDate;

    @DateTimeFormat("yyyy/MM/dd")
    @ColumnWidth(15)
    @ExcelProperty(index = 4,value = "结束日期")
    private String endDate;

}
