package com.ruoyi.base.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目对象 t_project
 * 
 * @author xt
 * @date 2020-06-30
 */
@Table(name = "t_project")
public class Project extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 名称 */
    @Column(name = "name")
    @Excel(name = "名称")
    private String name;

    /** 描述 */
    @Column(name = "content_describe")
    @Excel(name = "描述")
    private String contentDescribe;

    /** 负责人 */
    @Column(name = "emp_id")
    @Excel(name = "负责人id")
    private Long empId;
    /** 负责人 */
    @Excel(name = "负责人")
    @Transient
    private String empName;

    /** 状态(0:创建，1:完成，2:关闭) */
    @Column(name = "status")
    @Excel(name = "状态(0:创建，1:完成，2:关闭)")
    private Integer status;

    /** 状态(0:创建，1:完成，2:关闭) */
    @Column(name = "chat_id")
    private String chatId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setContentDescribe(String contentDescribe)
    {
        this.contentDescribe = contentDescribe;
    }

    public String getContentDescribe()
    {
        return contentDescribe;
    }
    public void setEmpId(Long empId) 
    {
        this.empId = empId;
    }

    public Long getEmpId() 
    {
        return empId;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("contentDescribe", getContentDescribe())
            .append("empId", getEmpId())
            .append("status", getStatus())
            .append("createId", getCreateId())
            .append("updateId", getUpdateId())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
