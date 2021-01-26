package com.ruoyi.base.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 员工状态异动信息对象 t_hr_emp_transfers
 * 
 * @author liujianwen
 * @date 2021-01-11
 */
@Table(name = "t_hr_emp_transfers")
public class HrEmpTransfers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** null */
    @Column(name = "parent_id")
    @Excel(name = "null")
    private Long parentId;

    /** null */
    @Column(name = "name")
    @Excel(name = "null")
    private String name;

    /** null */
    @Column(name = "type")
    @Excel(name = "null")
    private String type;

    /** 异动时间 */
    @Column(name = "transfer_time")
    @Excel(name = "异动时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date transferTime;

    /** 异动前状态 */
    @Column(name = "before_status")
    @Excel(name = "异动前状态")
    private String beforeStatus;

    /** 异动后状态 */
    @Column(name = "after_status")
    @Excel(name = "异动后状态")
    private String afterStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setTransferTime(Date transferTime) 
    {
        this.transferTime = transferTime;
    }

    public Date getTransferTime() 
    {
        return transferTime;
    }
    public void setBeforeStatus(String beforeStatus) 
    {
        this.beforeStatus = beforeStatus;
    }

    public String getBeforeStatus() 
    {
        return beforeStatus;
    }
    public void setAfterStatus(String afterStatus) 
    {
        this.afterStatus = afterStatus;
    }

    public String getAfterStatus() 
    {
        return afterStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("name", getName())
            .append("type", getType())
            .append("transferTime", getTransferTime())
            .append("beforeStatus", getBeforeStatus())
            .append("afterStatus", getAfterStatus())
            .append("createId", getCreateId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateId", getUpdateId())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
