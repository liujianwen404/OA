package com.ruoyi.base.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目迭代任务操作记录对象 t_project_operation
 * 
 * @author xt
 * @date 2020-06-30
 */
@Table(name = "t_project_operation")
public class ProjectOperation extends BaseEntity
{



    private static final long serialVersionUID = 1L;

    /** 操作id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 项目id */
    @Column(name = "project_id")
    @Excel(name = "项目id")
    private Long projectId;

    /** 迭代id */
    @Column(name = "project_plan_id")
    @Excel(name = "迭代id")
    private Long projectPlanId;

    /** 任务id */
    @Column(name = "project_plan_task_id")
    @Excel(name = "任务id")
    private Long projectPlanTaskId;

    /** 描述 */
    @Column(name = "content_describe")
    @Excel(name = "描述")
    private String contentDescribe;

    /** 操作人 */
    @Column(name = "emp_id")
    @Excel(name = "操作人")
    private Long empId;

    /** 类型（0:项目，1:迭代，2:任务） */
    @Column(name = "type")
    @Excel(name = "类型", readConverterExp = "0=:项目，1:迭代，2:任务")
    private Integer type;

    /** 操作(0:创建，1:编辑（内容），2:人员调整，3:状态调整，4:完成，5:关闭) */
    @Column(name = "operation")
    @Excel(name = "操作(0:创建，1:编辑", readConverterExp = "内=容")
    private Integer operation;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }
    public void setProjectPlanId(Long projectPlanId) 
    {
        this.projectPlanId = projectPlanId;
    }

    public Long getProjectPlanId() 
    {
        return projectPlanId;
    }
    public void setProjectPlanTaskId(Long projectPlanTaskId) 
    {
        this.projectPlanTaskId = projectPlanTaskId;
    }

    public Long getProjectPlanTaskId() 
    {
        return projectPlanTaskId;
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
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setOperation(Integer operation) 
    {
        this.operation = operation;
    }

    public Integer getOperation() 
    {
        return operation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectId", getProjectId())
            .append("projectPlanId", getProjectPlanId())
            .append("projectPlanTaskId", getProjectPlanTaskId())
            .append("contentDescribe", getContentDescribe())
            .append("empId", getEmpId())
            .append("type", getType())
            .append("operation", getOperation())
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
