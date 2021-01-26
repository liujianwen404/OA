package com.ruoyi.base.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * off流程对象 t_hr_off
 *
 * @author xt
 * @date 2020-07-28
 */
@Table(name = "t_hr_off")
@ColumnWidth(20)
public class HrOff extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * off ID
     */
    @Id
    @ExcelIgnore
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * off接收人名称
     */
    @Column(name = "to_off_name")
    @ExcelProperty(index = 0, value = "姓名")
    private String toOffName;

    /**
     * off接收人邮箱
     */
    @Column(name = "to_off_email")
    @ExcelProperty(index = 1, value = "off接收人邮箱")
    private String toOffEmail;

    /**
     * 手机号码
     */
    @Column(name = "phonenumber")
    @ExcelProperty(index = 2, value = "手机号码")
    private String phonenumber;

    /**
     * off附件地址
     */
    @Column(name = "adjunct")
    @ExcelIgnore
    private String adjunct;

    /**
     * 是否已发送（1：已发送）
     */
    @Column(name = "is_send")
    @ExcelIgnore
    private Integer isSend;

    /**
     * 是否已回复（1：已回复）
     */
    @Column(name = "is_revert")
    @ExcelIgnore
    private Integer isRevert;

    /**
     * 部门id
     */
    @Column(name = "dept_id")
    @ExcelIgnore
    private Long deptId;
    @Transient
    @ExcelProperty(index = 3,value = "部门")
    private String deptName;

    /**
     * 岗位id
     */
    @Column(name = "post_id")
    @ExcelIgnore
    private Long postId;
    @Transient
    @ExcelProperty(index = 4,value = "岗位")
    private String postName;

    /**
     * 流程实例id
     */
    @Column(name = "instance_id")
    @ExcelIgnore
    private String instanceId;


    /**
     * 入职城市
     */
    @Column(name = "city")
    @ExcelProperty(index = 5, value = "入职城市")
    private String city;

    /**
     * 入职日期
     */
    @Column(name = "non_manager_date")
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(index = 6,value = "入职日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nonManagerDate;


    /**
     * 试用期限
     */
    @Column(name = "probation_date")
    @ExcelProperty(index = 7,value = "试用期限")
    private String probationDate;

    /**
     * 薪酬级别审批
     */
    @Column(name = "salary_level")
    @ExcelProperty(index = 8,value = "薪酬级别审批")
    private String salaryLevel;

    /**
     * 薪酬标准审批
     */
    @Column(name = "salary_standard")
    @ExcelProperty(index = 9,value = "薪酬标准审批")
    private String salaryStandard;

    /**
     * 员工入职薪资告知单附件地址
     */
    @Column(name = "salary_adjunct")
    @ExcelIgnore
    private String salaryAdjunct;

    /**
     * 招聘员姓名
     */
    @Column(name = "recruiter_name")
    @ExcelProperty(index = 10,value = "招聘员姓名")
    private String recruiterName;

    @Transient
    @ExcelProperty(index = 12,value = "当前处理人")
    private String todoUserNameExcel;

    /**
     * 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销）
     */
    @Column(name = "audit_status")
    @ExcelIgnore
    private Integer auditStatus;
    @Transient
    @ExcelProperty(index = 13,value = "审核状态")
    private String auditStatusExcel;


    /**
     * 简历附件地址
     */
    @Column(name = "resume_adjunct")
    @ExcelIgnore
    private String resumeAdjunct;


    /**
     * 申请人
     */
    @Column(name = "apply_user")
    @ExcelIgnore
    private String applyUser;

    /**
     * 申请时间
     */
    @Column(name = "apply_time")
    @DateTimeFormat("yyyy-MM-dd HH:mm")
    @ExcelProperty(index = 11,value = "申请时间")
    private Date applyTime;

    /**
     * 发起人姓名
     */
    @Column(name = "apply_user_name")
    @ExcelIgnore
    private String applyUserName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getTodoUserNameExcel() {
        return todoUserNameExcel;
    }

    public void setTodoUserNameExcel(String todoUserNameExcel) {
        this.todoUserNameExcel = todoUserNameExcel;
    }

    public String getAuditStatusExcel() {
        return auditStatusExcel;
    }

    public void setAuditStatusExcel(String auditStatusExcel) {
        this.auditStatusExcel = auditStatusExcel;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSalaryLevel() {
        return salaryLevel;
    }

    public void setSalaryLevel(String salaryLevel) {
        this.salaryLevel = salaryLevel;
    }

    public String getSalaryStandard() {
        return salaryStandard;
    }

    public void setSalaryStandard(String salaryStandard) {
        this.salaryStandard = salaryStandard;
    }

    public String getSalaryAdjunct() {
        return salaryAdjunct;
    }

    public void setSalaryAdjunct(String salaryAdjunct) {
        this.salaryAdjunct = salaryAdjunct;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setToOffName(String toOffName) {
        this.toOffName = toOffName;
    }

    public String getToOffName() {
        return toOffName;
    }

    public void setToOffEmail(String toOffEmail) {
        this.toOffEmail = toOffEmail;
    }

    public String getToOffEmail() {
        return toOffEmail;
    }

    public void setAdjunct(String adjunct) {
        this.adjunct = adjunct;
    }

    public String getAdjunct() {
        return adjunct;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsRevert(Integer isRevert) {
        this.isRevert = isRevert;
    }

    public Integer getIsRevert() {
        return isRevert;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getNonManagerDate() {
        return nonManagerDate;
    }

    public void setNonManagerDate(Date nonManagerDate) {
        this.nonManagerDate = nonManagerDate;
    }

    public String getResumeAdjunct() {
        return resumeAdjunct;
    }

    public void setResumeAdjunct(String resumeAdjunct) {
        this.resumeAdjunct = resumeAdjunct;
    }

    public String getProbationDate() {
        return probationDate;
    }

    public void setProbationDate(String probationDate) {
        this.probationDate = probationDate;
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

    @Override
    public String toString() {
        return "HrOff{" +
                "id=" + id +
                ", toOffName='" + toOffName + '\'' +
                ", toOffEmail='" + toOffEmail + '\'' +
                ", adjunct='" + adjunct + '\'' +
                ", isSend=" + isSend +
                ", isRevert=" + isRevert +
                ", deptId=" + deptId +
                ", postId=" + postId +
                ", instanceId='" + instanceId + '\'' +
                ", auditStatus=" + auditStatus +
                ", city='" + city + '\'' +
                ", nonManagerDate=" + nonManagerDate +
                ", resumeAdjunct='" + resumeAdjunct + '\'' +
                ", probationDate='" + probationDate + '\'' +
                ", applyUser='" + applyUser + '\'' +
                ", applyTime=" + applyTime +
                ", applyUserName='" + applyUserName + '\'' +
                '}';
    }
}
