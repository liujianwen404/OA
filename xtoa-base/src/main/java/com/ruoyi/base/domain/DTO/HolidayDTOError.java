package com.ruoyi.base.domain.DTO;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 考勤统计导入数据接收对象
 *
 */
@Data
public class HolidayDTOError extends HolidayDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ColumnWidth(15)
    @ExcelProperty(index = 5,value = "错误信息")
    private String errorInfo;

}
