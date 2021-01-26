package com.ruoyi.base.domain.DTO;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;


/**
 * 加班数据接收类
 */
@Data
public class OverTimeDTO {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty("审批状态")
    private String status;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty("发起时间")
    private String  applyTime;

    @ExcelProperty("发起人工号")
    private String applyUserNum;

    @ExcelProperty("发起人姓名")
    private String applyUserName;

    @ExcelProperty("发起人部门")
    private String deptName;

    @DateTimeFormat("yyyy-MM-dd HH:mm")
    @ExcelProperty("开始时间")
    private String  startTime;

    @DateTimeFormat("yyyy-MM-dd HH:mm")
    @ExcelProperty("结束时间")
    private String  endTime;

    @ExcelProperty("时长")
    private String totalTimes;

    @ExcelProperty("加班原因")
    private String reason;

    @ExcelProperty("是否法定节假日")
    private String type;


}
