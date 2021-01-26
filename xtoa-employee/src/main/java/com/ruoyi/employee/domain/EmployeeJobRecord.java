package com.ruoyi.employee.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 员工工作记录对象 employee_job_record
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public class EmployeeJobRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 员工ID */
    @Excel(name = "员工ID")
    private Long employeeId;

    /** 员工姓名 */
    @Excel(name = "员工姓名")
    private String employeeName;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String company;

    /** 工作地点 */
    @Excel(name = "工作地点")
    private String city;

    /** 工作职位 */
    @Excel(name = "工作职位")
    private String position;

    /** 入职时间 */
    @Excel(name = "入职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryTime;

    /** 离职时间 */
    @Excel(name = "离职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date leaveTime;

    /** 离职证明 */
    @Excel(name = "离职证明")
    private String leaveProve;

    /** 薪酬 */
    @Excel(name = "薪酬")
    private Double salary;

    /** 联系人 */
    @Excel(name = "联系人")
    private String witness;

    /** 联系人号码 */
    @Excel(name = "联系人号码")
    private String witnessPhone;

    /** 状态：0未验证，1已验证，2验证不通过 */
    @Excel(name = "状态：0未验证，1已验证，2验证不通过")
    private String status;

    /** 离职原因 */
    @Excel(name = "离职原因")
    private String leaveReason;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEmployeeId(Long employeeId) 
    {
        this.employeeId = employeeId;
    }

    public Long getEmployeeId() 
    {
        return employeeId;
    }
    public void setEmployeeName(String employeeName) 
    {
        this.employeeName = employeeName;
    }

    public String getEmployeeName() 
    {
        return employeeName;
    }
    public void setCompany(String company) 
    {
        this.company = company;
    }

    public String getCompany() 
    {
        return company;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
    }
    public void setEntryTime(Date entryTime) 
    {
        this.entryTime = entryTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEntryTime() 
    {
        return entryTime;
    }
    public void setLeaveTime(Date leaveTime) 
    {
        this.leaveTime = leaveTime;
    }

    public Date getLeaveTime() 
    {
        return leaveTime;
    }
    public void setLeaveProve(String leaveProve) 
    {
        this.leaveProve = leaveProve;
    }

    public String getLeaveProve() 
    {
        return leaveProve;
    }
    public void setSalary(Double salary) 
    {
        this.salary = salary;
    }

    public Double getSalary() 
    {
        return salary;
    }
    public void setWitness(String witness) 
    {
        this.witness = witness;
    }

    public String getWitness() 
    {
        return witness;
    }
    public void setWitnessPhone(String witnessPhone) 
    {
        this.witnessPhone = witnessPhone;
    }

    public String getWitnessPhone() 
    {
        return witnessPhone;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setLeaveReason(String leaveReason) 
    {
        this.leaveReason = leaveReason;
    }

    public String getLeaveReason() 
    {
        return leaveReason;
    }
    @Override
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("employeeId", getEmployeeId())
            .append("employeeName", getEmployeeName())
            .append("company", getCompany())
            .append("city", getCity())
            .append("position", getPosition())
            .append("entryTime", getEntryTime())
            .append("leaveTime", getLeaveTime())
            .append("leaveProve", getLeaveProve())
            .append("salary", getSalary())
            .append("witness", getWitness())
            .append("witnessPhone", getWitnessPhone())
            .append("status", getStatus())
            .append("leaveReason", getLeaveReason())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
