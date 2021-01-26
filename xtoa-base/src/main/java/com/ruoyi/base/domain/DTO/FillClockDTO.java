package com.ruoyi.base.domain.DTO;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import java.util.Date;

@Data
public class FillClockDTO {

    private static final long serialVersionUID = 1L;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String status;

    /** 原因 */
    @Excel(name = "未打卡原因")
    private String reason;

    /** 补卡时间 */
    @Excel(name = "补卡时间", dateFormat = "yyyy-MM-dd HH:mm")
    private Date dates;

    /** 补卡班次 */
    @Excel(name = "补卡班次")
    private String classes;

    /** 第一次未打卡 */
    @Excel(name = "第一次未打卡", dateFormat = "yyyy-MM-dd HH:mm")
    private Date firstTime;

    /** 第二次未打卡 */
    @Excel(name = "第二次未打卡", dateFormat = "yyyy-MM-dd HH:mm")
    private Date secondTime;

    /** 第三次未打卡 */
    @Excel(name = "第三次未打卡", dateFormat = "yyyy-MM-dd HH:mm")
    private Date thirdTime;

    /** 发起人工号 */
    @Excel(name = "发起人工号")
    private String applyUserNum;

    /** 发起人姓名 */
    @Excel(name = "发起人姓名")
    private String applyUserName;

    /** 部门名称 */
    @Excel(name = "发起人部门")
    private String deptName;


}
