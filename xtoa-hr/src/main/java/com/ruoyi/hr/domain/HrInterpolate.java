package com.ruoyi.hr.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.process.utils.SpringUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 内推申请对象 t_hr_interpolate
 * 
 * @author vivi07
 * @date 2020-05-12
 */
@Table(name = "t_hr_interpolate")
public class HrInterpolate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 内推id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long interpolateId;

    /** 推荐人姓名 */
    @Column(name = "emp_name")
    @Excel(name = "推荐人姓名")
    private String empName;

    /** 推荐关系 */
    @Column(name = "interpolate_relation")
    @Excel(name = "推荐关系")
    private String interpolateRelation;

    /** 被推荐人姓名 */
    @Column(name = "interpolate__name")
    @Excel(name = "被推荐人姓名")
    private String interpolateName;

    /** 生日 */
    @Column(name = "birthday")
    @Excel(name = "生日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 内推性别（0男 1女 2未知） */
    @Column(name = "interpolate_sex")
    @Excel(name = "内推性别", readConverterExp = "0=男,1=女,2=未知")
    private String interpolateSex;

    /** 籍贯 */
    @Column(name = "interpolate_native_place")
    @Excel(name = "籍贯")
    private String interpolateNativePlace;

    /** 教育背景 */
    @Column(name = "interpolate_education")
    @Excel(name = "教育背景")
    private String interpolateEducation;

    /** 工作年限 */
    @Column(name = "interpolate_job_year")
    @Excel(name = "工作年限")
    private String interpolateJobYear;

    /** 推荐岗位id */
    @Column(name = "interpolate_post_id")
    @Excel(name = "推荐岗位id")
    private Long interpolatePostId;

    /** 手机号码 */
    @Column(name = "phonenumber")
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 现居住地 */
    @Column(name = "interpolate_residence")
    @Excel(name = "现居住地")
    private String interpolateResidence;

    /** 简历url */
    @Column(name = "resume")
    @Excel(name = "简历url")
    private String resume;

    /** 审核人id */
    @Column(name = "audit_id")
    @Excel(name = "审核人id")
    private String auditId;

    /** 审核人姓名 */
    @Column(name = "audit_name")
    @Excel(name = "审核人姓名")
    private String auditName;

    /**
     * 是否在岗(0:否；1：是)
     */
    @Column(name = "duty_flag")
    @Excel(name = "是否在岗")
    private String dutyFlag;

    /**
     * 审核状态(0：待审核，1：审核中，2：通过，3：拒绝，4：撤销)
     */
    @Column(name = "audit_status")
    @Excel(name = "审核状态")
    private String auditStatus;

    // '流程实例id',
    @Column(name = "instance_id")
    private String instanceId;

/*    *//** 推荐部门id *//*
    @Column(name = "interpolate_dept_id")
    private Long interpolateDeptId;*/


    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

 /*   public Long getInterpolateDeptId() {
        return interpolateDeptId;
    }

    public void setInterpolateDeptId(Long interpolateDeptId) {
        this.interpolateDeptId = interpolateDeptId;
    }*/

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getDutyFlag() {
        return dutyFlag;
    }

    public void setDutyFlag(String dutyFlag) {
        this.dutyFlag = dutyFlag;
    }

    public void setInterpolateId(Long interpolateId)
    {
        this.interpolateId = interpolateId;
    }

    public Long getInterpolateId() 
    {
        return interpolateId;
    }
    public void setEmpName(String empName) 
    {
        this.empName = empName;
    }

    public String getEmpName() 
    {
        return empName;
    }
    public void setInterpolateRelation(String interpolateRelation) 
    {
        this.interpolateRelation = interpolateRelation;
    }

    public String getInterpolateRelation() 
    {
        return interpolateRelation;
    }
    public void setInterpolateName(String interpolateName) 
    {
        this.interpolateName = interpolateName;
    }

    public String getInterpolateName() 
    {
        return interpolateName;
    }
    public void setBirthday(Date birthday) 
    {
        this.birthday = birthday;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getBirthday() 
    {
        return birthday;
    }
    public void setInterpolateSex(String interpolateSex) 
    {
        this.interpolateSex = interpolateSex;
    }

    public String getInterpolateSex() 
    {
        return interpolateSex;
    }
    public void setInterpolateNativePlace(String interpolateNativePlace) 
    {
        this.interpolateNativePlace = interpolateNativePlace;
    }

    public String getInterpolateNativePlace() 
    {
        return interpolateNativePlace;
    }
    public void setInterpolateEducation(String interpolateEducation) 
    {
        this.interpolateEducation = interpolateEducation;
    }

    public String getInterpolateEducation() 
    {
        return interpolateEducation;
    }
    public void setInterpolateJobYear(String interpolateJobYear) 
    {
        this.interpolateJobYear = interpolateJobYear;
    }

    public String getInterpolateJobYear() 
    {
        return interpolateJobYear;
    }
    public void setInterpolatePostId(Long interpolatePostId) 
    {
        this.interpolatePostId = interpolatePostId;
    }

    public Long getInterpolatePostId() 
    {
        return interpolatePostId;
    }
    public void setPhonenumber(String phonenumber) 
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() 
    {
        return phonenumber;
    }
    public void setInterpolateResidence(String interpolateResidence) 
    {
        this.interpolateResidence = interpolateResidence;
    }

    public String getInterpolateResidence() 
    {
        return interpolateResidence;
    }
    public void setResume(String resume) 
    {
        this.resume = resume;
    }

    public String getResume() 
    {
        return resume;
    }
    public void setAuditId(String auditId) 
    {
        this.auditId = auditId;
    }

    public String getAuditId() 
    {
        return auditId;
    }
    public void setAuditName(String auditName) 
    {
        this.auditName = auditName;
    }

    public String getAuditName() 
    {
        return auditName;
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
    public String getTodoUserName() {
        return SpringUtil.getHandleNmae(getInstanceId(), Integer.parseInt(getAuditStatus() == null? "0" : getAuditStatus()));
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("interpolateId", getInterpolateId())
            .append("empName", getEmpName())
            .append("interpolateRelation", getInterpolateRelation())
            .append("interpolateName", getInterpolateName())
            .append("birthday", getBirthday())
            .append("interpolateSex", getInterpolateSex())
            .append("interpolateNativePlace", getInterpolateNativePlace())
            .append("interpolateEducation", getInterpolateEducation())
            .append("interpolateJobYear", getInterpolateJobYear())
            .append("interpolatePostId", getInterpolatePostId())
            .append("phonenumber", getPhonenumber())
            .append("interpolateResidence", getInterpolateResidence())
            .append("resume", getResume())
            .append("auditId", getAuditId())
            .append("auditName", getAuditName())
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
