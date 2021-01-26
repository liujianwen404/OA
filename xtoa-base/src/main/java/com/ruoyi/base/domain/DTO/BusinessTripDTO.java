package com.ruoyi.base.domain.DTO;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * 出差申请数据接收类
 *
 * @author liujianwen
 */
@Data
public class BusinessTripDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 审批编号 */
    @Excel(name = "审批编号")
    private String number;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String status;

    /** 申请时间 */
    @Excel(name = "发起时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    /** 发起人工号 */
    @Excel(name = "发起人工号")
    private String applyUserNum;

    /** 发起人姓名 */
    @Excel(name = "发起人姓名")
    private String applyUserName;

    /** 部门名称 */
    @Excel(name = "发起人部门")
    private String deptName;

    /** 出差事由 */
    @Excel(name = "出差事由")
    private String reason;

    /** 交通工具（1：飞机，2：火车，3：汽车，4：其他) */
    @Excel(name = "交通工具")
    private String vehicle;

    /** 单程往返（1：单程，2：往返) */
    @Excel(name = "单程往返")
    private String journey;

    /** 出发城市 */
    @Excel(name = "出发城市")
    private String departCity;

    /** 目的城市 */
    @Excel(name = "目的城市")
    private String destinationCity;

    /** 开始时间 */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date startTime;

    /** 结束时间 */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date endTime;

    /** 时长，单位小时 */
    @Excel(name = "时长(小时)")
    private Double totalTimes;

    /** 出差总时长 */
    @Excel(name = "出差总时长")
    private Double days;

    /** 出差备注 */
    @Excel(name = "出差备注")
    private String remark;

    /** 同行人 */
    @Excel(name = "同行人")
    private String partner;

}
