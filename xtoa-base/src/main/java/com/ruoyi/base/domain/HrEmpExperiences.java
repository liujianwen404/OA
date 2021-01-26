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
 * 员工工作经历对象 t_hr_emp_experiences
 * 
 * @author liujianwen
 * @date 2021-01-11
 */
@Table(name = "t_hr_emp_experiences")
public class HrEmpExperiences extends BaseEntity
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

    /** 入职日期 */
    @Column(name = "entry_date")
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryDate;

    /** 离职日期 */
    @Column(name = "quit_date")
    @Excel(name = "离职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date quitDate;

    /** 公司 */
    @Column(name = "company")
    @Excel(name = "公司")
    private String company;

    /** 职位 */
    @Column(name = "position")
    @Excel(name = "职位")
    private String position;

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
    public void setEntryDate(Date entryDate) 
    {
        this.entryDate = entryDate;
    }

    public Date getEntryDate() 
    {
        return entryDate;
    }
    public void setQuitDate(Date quitDate) 
    {
        this.quitDate = quitDate;
    }

    public Date getQuitDate() 
    {
        return quitDate;
    }
    public void setCompany(String company) 
    {
        this.company = company;
    }

    public String getCompany() 
    {
        return company;
    }
    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getPosition()
    {
        return position;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("name", getName())
            .append("type", getType())
            .append("entryDate", getEntryDate())
            .append("quitDate", getQuitDate())
            .append("company", getCompany())
            .append("position", getPosition())
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
