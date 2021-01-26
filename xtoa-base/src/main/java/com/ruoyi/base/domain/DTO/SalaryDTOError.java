package com.ruoyi.base.domain.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;


/**
 * 考勤统计导入数据接收对象
 *
 */
@Data
public class SalaryDTOError extends SalaryStructureDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    @ColumnWidth(15)
    @ExcelProperty(index = 13,value = "错误信息")
    private String errorInfo;

}
