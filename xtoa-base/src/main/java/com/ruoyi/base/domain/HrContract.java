package com.ruoyi.base.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.InputStream;
import java.util.Date;

/**
 * 劳动合同对象 t_hr_contract
 * 
 * @author xt
 * @date 2020-06-17
 */
@Table(name = "t_hr_contract")
@ContentRowHeight(80)
@ColumnWidth(20)
public class HrContract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 劳动合同id */
    @Id
    @KeySql(useGeneratedKeys = true)
    @ExcelIgnore
    private Long id;

    /** 员工id */
    @Column(name = "emp_id")
    @ExcelProperty("员工id")
    private Long empId;


    @Transient
    @ExcelProperty("员工")
    private String empName;

    /** 合同主体 */
    @Column(name = "subject_contract")
    @ExcelProperty("合同主体")
    private String subjectContract;

    /** 合同年限 */
    @Column(name = "contract_year")
    @ExcelProperty( "合同年限")
    private Integer contractYear;

    /** 合同开始 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    @Column(name = "contract_star")
    @ExcelProperty("合同开始")
    private Date contractStar;

    /** 合同结束 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    @Column(name = "contract_end")
    @ExcelProperty("合同结束")
    private Date contractEnd;

    /** 续签次数 */
    @Column(name = "renew_count")
    @ExcelProperty( "续签次数")
    private Integer renewCount;

    /** 流程实例id */
    @Column(name = "instance_id")
    @ExcelIgnore
    private String instanceId;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销） */
    @Column(name = "audit_status")
    @ExcelIgnore
    private Integer auditStatus;

    @Column(name = "contract_url")
    @ExcelIgnore
    private String contractUrl;

    @Transient
    @ExcelProperty("到期情况")
    private String overdueStr;

    @Transient
    @ExcelProperty( "合同")
    private InputStream inputStream;

    @Transient
    @ExcelIgnore
    private Integer overdue;


    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getOverdueStr() {
        return overdueStr;
    }

    public void setOverdueStr(String overdueStr) {
        this.overdueStr = overdueStr;
    }

    public Integer getOverdue() {
        return overdue;
    }

    public void setOverdue(Integer overdue) {
        this.overdue = overdue;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
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
    public void setEmpId(Long empId) 
    {
        this.empId = empId;
    }

    public Long getEmpId() 
    {
        return empId;
    }
    public void setSubjectContract(String subjectContract) 
    {
        this.subjectContract = subjectContract;
    }

    public String getSubjectContract() 
    {
        return subjectContract;
    }
    public void setContractYear(Integer contractYear) 
    {
        this.contractYear = contractYear;
    }

    public Integer getContractYear() 
    {
        return contractYear;
    }
    public void setContractStar(Date contractStar) 
    {
        this.contractStar = contractStar;
    }

    public Date getContractStar() 
    {
        return contractStar;
    }
    public void setContractEnd(Date contractEnd) 
    {
        this.contractEnd = contractEnd;
    }

    public Date getContractEnd() 
    {
        return contractEnd;
    }
    public void setRenewCount(Integer renewCount) 
    {
        this.renewCount = renewCount;
    }

    public Integer getRenewCount() 
    {
        return renewCount;
    }
    public void setInstanceId(String instanceId) 
    {
        this.instanceId = instanceId;
    }

    public String getInstanceId() 
    {
        return instanceId;
    }
    public void setAuditStatus(Integer auditStatus)
    {
        this.auditStatus = auditStatus;
    }

    public Integer getAuditStatus()
    {
        return auditStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("empId", getEmpId())
            .append("subjectContract", getSubjectContract())
            .append("contractYear", getContractYear())
            .append("contractStar", getContractStar())
            .append("contractEnd", getContractEnd())
            .append("renewCount", getRenewCount())
            .append("instanceId", getInstanceId())
            .append("auditStatus", getAuditStatus())
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
