package com.ruoyi.base.domain.DTO;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 外出申请数据接收类
 * 
 * @author liujianwen
 */
@Data
public class GooutDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

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

    /** 开始时间 */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date startTime;

    /** 结束时间 */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm")
    private Date endTime;

    /** 时长，单位小时 */
    @Excel(name = "时长")
    private Double totalTimes;

    /** 原因 */
    @Excel(name = "外出事由")
    private String reason;

}
