package com.ruoyi.hr.domain;

import java.util.Date;
import java.util.List;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.process.utils.SpringUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import lombok.Data;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 调动申请对象 t_hr_job_transfer
 * 
 * @author xt
 * @date 2020-05-22
 */
@Table(name = "t_hr_job_transfer")
@ColumnWidth(20)
@Data
public class HrJobTransfer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 调动申请id */
    @Id
    @KeySql(useGeneratedKeys = true)
    @ExcelIgnore
    private Long jobTransferId;

    /** 调动人姓名 */
    @Column(name = "emp_id")
    @ExcelIgnore
    private Long empId;

    /** 调动人姓名 */
    @Column(name = "emp_name")
    @ExcelProperty(index = 0,value = "调动人姓名")
    private String empName;

    /** 入职日期 */
    @Column(name = "non_manager_Date")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(index = 1,value = "入职日期")
    private Date nonManagerDate;

    /** 调动日期 */
    @Column(name = "job_transferr_Date")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(index = 7,value = "调动日期")
    private Date jobTransferrDate;

    /** 现岗位id */
    @Column(name = "current_post_id")
    @ExcelIgnore
    private Long currentPostId;

    /** 现部门id */
    @Column(name = "current_dept_id")
    @ExcelIgnore
    private Long currentDeptId;

    @Transient
    @ExcelProperty(index = 2,value = "现部门岗位")
    private String current;

    /** 调动岗位id */
    @Column(name = "job_transfer_post_id")
    @ExcelIgnore
    private Long jobTransferPostId;

    /** 调动部门id */
    @Column(name = "job_transfer_dept_id")
    @ExcelIgnore
    private Long jobTransferDeptId;

    @Transient
    @ExcelProperty(index = 3,value = "新部门岗位")
    private String transfer;


    /** 调动原因 */
    @Column(name = "job_transfer_description")
    @ExcelIgnore
    private String jobTransferDescription;

    /** 调动类型（0正式调动 1临时调动 2晋升 3降级） */
    @Column(name = "job_transfer_type")
    @ExcelProperty(index = 4,value = "调动类型")
    private String jobTransferType;

    /** 现领导意见 */
    @Column(name = "current_description")
    @ExcelIgnore
    private String currentDescription;

    /** 调动领导意见 */
    @Column(name = "transfer_description")
    @ExcelIgnore
    private String transferDescription;

    /** 现领导Id */
    @Column(name = "current_leader_id")
    @ExcelIgnore
    private String currentLeaderId;

    /** 调动领导Id */
    @Column(name = "transfer_leader_id")
    @ExcelIgnore
    private String transferLeaderId;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销） */
    @Column(name = "audit_status")
    @ExcelIgnore
    private Integer auditStatus;

    /** 提交状态（0=：保存，1：提交） */
    @Column(name = "status")
    @ExcelIgnore
    private String status;

    /** 流程实例id */
    @Column(name = "instance_id")
    @ExcelIgnore
    private String instanceId;

    @Transient
    @ExcelIgnore
    private String nowLeaderName;
    @Transient
    @ExcelIgnore
    private String newleaderName;


    /** 申请人 */
    @Column(name = "apply_user")
    @Excel(name = "申请人")
    @ExcelIgnore
    private String applyUser;


    /** 发起人姓名 */
    @Column(name = "apply_user_name")
    @ExcelIgnore
    private String applyUserName;

    /** 附件 */
    @Column(name = "attachment")
    @ExcelIgnore
    private String attachment;

    @Transient
    @ExcelProperty(index = 5,value = "当前处理人")
    private String todoUserNameExcel;


    @Transient
    @ExcelProperty(index = 6,value = "审核状态")
    private String auditStatusExcel;


    /** 申请时间 */
    @Column(name = "apply_time")
    @ExcelProperty(index = 8,value = "申请时间")
    private Date applyTime;

    @Transient
    @ExcelIgnore
    private Long deptId;

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Long getDeptId() {
        if (this.currentDeptId == null){
            return this.deptId;
        }
        return this.currentDeptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    @Override
    public String getApplyUserName() {
        return applyUserName;
    }

    @Override
    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getNowLeaderName() {
        return nowLeaderName;
    }

    public void setNowLeaderName(String nowLeaderName) {
        this.nowLeaderName = nowLeaderName;
    }

    public String getNewleaderName() {
        return newleaderName;
    }

    public void setNewleaderName(String newleaderName) {
        this.newleaderName = newleaderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getJobTransferrDate() {
        return jobTransferrDate;
    }

    public void setJobTransferrDate(Date jobTransferrDate) {
        this.jobTransferrDate = jobTransferrDate;
    }

    public String getCurrentLeaderId() {
        return currentLeaderId;
    }

    public void setCurrentLeaderId(String currentLeaderId) {
        this.currentLeaderId = currentLeaderId;
    }

    public String getTransferLeaderId() {
        return transferLeaderId;
    }

    public void setTransferLeaderId(String transferLeaderId) {
        this.transferLeaderId = transferLeaderId;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setJobTransferId(Long jobTransferId)
    {
        this.jobTransferId = jobTransferId;
    }

    public Long getJobTransferId() 
    {
        return jobTransferId;
    }
    public void setEmpId(Long empId) 
    {
        this.empId = empId;
    }

    public Long getEmpId() 
    {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public void setNonManagerDate(Date nonManagerDate)
    {
        this.nonManagerDate = nonManagerDate;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getNonManagerDate() 
    {
        return nonManagerDate;
    }
    public void setCurrentPostId(Long currentPostId) 
    {
        this.currentPostId = currentPostId;
    }

    public Long getCurrentPostId() 
    {
        return currentPostId;
    }
    public void setCurrentDeptId(Long currentDeptId) 
    {
        this.currentDeptId = currentDeptId;
    }

    public Long getCurrentDeptId() 
    {
        return currentDeptId;
    }
    public void setJobTransferPostId(Long jobTransferPostId) 
    {
        this.jobTransferPostId = jobTransferPostId;
    }

    public Long getJobTransferPostId() 
    {
        return jobTransferPostId;
    }
    public void setJobTransferDeptId(Long jobTransferDeptId) 
    {
        this.jobTransferDeptId = jobTransferDeptId;
    }

    public Long getJobTransferDeptId() 
    {
        return jobTransferDeptId;
    }
    public void setJobTransferDescription(String jobTransferDescription) 
    {
        this.jobTransferDescription = jobTransferDescription;
    }

    public String getJobTransferDescription() 
    {
        return jobTransferDescription;
    }
    public void setJobTransferType(String jobTransferType) 
    {
        this.jobTransferType = jobTransferType;
    }

    public String getJobTransferType() 
    {
        return jobTransferType;
    }
    public void setCurrentDescription(String currentDescription) 
    {
        this.currentDescription = currentDescription;
    }

    public String getCurrentDescription() 
    {
        return currentDescription;
    }
    public void setTransferDescription(String transferDescription) 
    {
        this.transferDescription = transferDescription;
    }

    public String getTransferDescription() 
    {
        return transferDescription;
    }
    @Override
    public String getTodoUserName() {
        return SpringUtil.getHandleNmae(getInstanceId(), getAuditStatus());
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobTransferId", getJobTransferId())
            .append("empId", getEmpId())
            .append("empName", getEmpName())
            .append("nonManagerDate", getNonManagerDate())
            .append("currentPostId", getCurrentPostId())
            .append("currentDeptId", getCurrentDeptId())
            .append("jobTransferPostId", getJobTransferPostId())
            .append("jobTransferDeptId", getJobTransferDeptId())
            .append("jobTransferDescription", getJobTransferDescription())
            .append("jobTransferType", getJobTransferType())
            .append("currentDescription", getCurrentDescription())
            .append("transferDescription", getTransferDescription())
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
