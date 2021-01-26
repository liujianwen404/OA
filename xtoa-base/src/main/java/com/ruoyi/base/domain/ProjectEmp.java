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

import java.util.List;

/**
 * 项目迭代任务成员对象 t_project_emp
 * 
 * @author xt
 * @date 2020-06-30
 */
@Table(name = "t_project_emp")
public class ProjectEmp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 项目id */
    @Column(name = "project_id")
    @Excel(name = "项目id")
    private Long projectId;
    @Transient
    private String projectName;

    /** 迭代id */
    @Column(name = "project_plan_id")
    @Excel(name = "迭代id")
    private Long projectPlanId;
    @Transient
    private String projectPlanName;

    /** 任务id */
    @Column(name = "project_plan_task_id")
    @Excel(name = "任务id")
    private Long projectPlanTaskId;
    @Transient
    private String projectPlanTaskName;

    /** 成员id */
    @Column(name = "emp_id")
    @Excel(name = "成员id")
    private Long empId;

    @Transient
    private List<Long> empIds;

    @Transient
    private String empName;

    /** 类型（0:项目，1:迭代，2:任务） */
    @Column(name = "type")
    @Excel(name = "类型", readConverterExp = "0=:项目，1:迭代，2:任务")
    private Integer type;

    public List<Long> getEmpIds() {
        return empIds;
    }

    public void setEmpIds(List<Long> empIds) {
        this.empIds = empIds;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectPlanName() {
        return projectPlanName;
    }

    public void setProjectPlanName(String projectPlanName) {
        this.projectPlanName = projectPlanName;
    }

    public String getProjectPlanTaskName() {
        return projectPlanTaskName;
    }

    public void setProjectPlanTaskName(String projectPlanTaskName) {
        this.projectPlanTaskName = projectPlanTaskName;
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("projectId", getProjectId())
            .append("projectPlanId", getProjectPlanId())
            .append("projectPlanTaskId", getProjectPlanTaskId())
            .append("empId", getEmpId())
            .append("type", getType())
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
