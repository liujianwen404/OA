package com.ruoyi.base.domain.DTO;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 请假数据接收类
 */
@Data
public class HrLeaveDTO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "请假类型",readConverterExp = "1=调休,2=事假,3=病假,4=年假,5=丧假,6=婚假")
    private String type;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String status;

    @Excel(name = "标题")
    private String title;

    @Excel(name = "开始时间", dateFormat = "yyyy-MM-dd HH:mm")
    private Date startTime;

    @Excel(name = "结束时间", dateFormat = "yyyy-MM-dd HH:mm")
    private Date endTime;

    @Excel(name = "时长")
    private String totalHours;

    @Excel(name = "请假事由")
    private String reason;

    @Excel(name = "发起时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @Excel(name = "发起人工号")
    private String applyUserNum;

    @Excel(name = "发起人姓名")
    private String applyUserName;

    @Excel(name = "发起人部门")
    private String deptName;

}
