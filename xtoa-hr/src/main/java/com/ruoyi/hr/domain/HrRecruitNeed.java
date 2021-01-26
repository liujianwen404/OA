package com.ruoyi.hr.domain;

import java.util.Date;
import com.ruoyi.process.utils.SpringUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 招聘需求对象 t_hr_recruit_need
 * 
 * @author xt
 * @date 2020-05-28
 */
@Table(name = "t_hr_recruit_need")
public class HrRecruitNeed extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 招聘id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long recruitNeedId;

    /** 申请人姓名 */
    @Column(name = "emp_name")
    @Excel(name = "申请人姓名")
    private String empName;

    /** 需招聘部门id */
    @Column(name = "recruit_dept_id")
    @Excel(name = "需招聘部门id")
    private Long recruitDeptId;

    /** 需招聘岗位id */
    @Column(name = "recruit_post_id")
    @Excel(name = "需招聘岗位id")
    private Long recruitPostId;

    /** 申请日期 */
    @Column(name = "apply_date")
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyDate;

    /** 申请到岗时间 */
    @Column(name = "apply_duty_date")
    @Excel(name = "申请到岗时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyDutyDate;

    /** 招聘人数 */
    @Column(name = "recruit_count")
    @Excel(name = "招聘人数")
    private Integer recruitCount;

    /** 岗位描述 */
    @Column(name = "recruit_description")
    @Excel(name = "岗位描述")
    private String recruitDescription;

    /** 教育背景 */
    @Column(name = "recruit_education")
    @Excel(name = "教育背景")
    private String recruitEducation;

    /** 年龄下限 */
    @Column(name = "recruit_min_age")
    @Excel(name = "年龄下限")
    private Integer recruitMinAge;

    /** 年龄上限 */
    @Column(name = "recruit_max_age")
    @Excel(name = "年龄上限")
    private Integer recruitMaxAge;

    /** 婚姻 */
    @Column(name = "recruit_marriage")
    @Excel(name = "婚姻")
    private String recruitMarriage;

    /** 招聘性别（0男 1女 2未知） */
    @Column(name = "recruit_sex")
    @Excel(name = "招聘性别", readConverterExp = "0=男,1=女,2=未知")
    private String recruitSex;

    /** 专业限制 */
    @Column(name = "recruit_major")
    @Excel(name = "专业限制")
    private String recruitMajor;

    /** 外语要求 */
    @Column(name = "recruit_EFL")
    @Excel(name = "外语要求")
    private String recruitEfl;

    /** 需要技能 */
    @Column(name = "recruit_skill")
    @Excel(name = "需要技能")
    private String recruitSkill;

    /** 薪资下限 */
    @Column(name = "recruit_min_salary")
    @Excel(name = "薪资下限")
    private Double recruitMinSalary;

    /** 薪资上限 */
    @Column(name = "recruit_max_salary")
    @Excel(name = "薪资上限")
    private Double recruitMaxSalary;

    /** 试用期 */
    @Column(name = "recruit_probation_date")
    @Excel(name = "试用期")
    private String recruitProbationDate;

    /** 试用期薪资 */
    @Column(name = "recruit_probation_salary")
    @Excel(name = "试用期薪资")
    private Double recruitProbationSalary;

    /** 工作经验 */
    @Column(name = "recruit_job_description")
    @Excel(name = "工作经验")
    private String recruitJobDescription;

    /** 招聘类型（0：需求，1：储备） */
    @Column(name = "recruit_type")
    @Excel(name = "招聘类型", readConverterExp = "0=：需求，1：储备")
    private Integer recruitType;

    /** 展示类型（0：所有人，1：非所有人） */
    @Column(name = "show_type")
    @Excel(name = "展示类型", readConverterExp = "0=：所有人，1：非所有人")
    private Integer showType;

    /** 发布状态（0：保存，1：提交） */
    @Column(name = "release_status")
    @Excel(name = "发布状态", readConverterExp = "0=：保存，1：提交")
    private Integer releaseStatus;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：未通过） */
    @Column(name = "audit_status")
    @Excel(name = "审核状态", readConverterExp = "0=：待提交，1：审核中，2：通过，3：未通过")
    private Integer auditStatus;

    // '流程实例id',
    @Column(name = "instance_id")
    private String instanceId;

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

    public void setRecruitNeedId(Long recruitNeedId)
    {
        this.recruitNeedId = recruitNeedId;
    }

    public Long getRecruitNeedId() 
    {
        return recruitNeedId;
    }
    public void setEmpName(String empName) 
    {
        this.empName = empName;
    }

    public String getEmpName() 
    {
        return empName;
    }
    public void setRecruitDeptId(Long recruitDeptId) 
    {
        this.recruitDeptId = recruitDeptId;
    }

    public Long getRecruitDeptId() 
    {
        return recruitDeptId;
    }
    public void setRecruitPostId(Long recruitPostId) 
    {
        this.recruitPostId = recruitPostId;
    }

    public Long getRecruitPostId() 
    {
        return recruitPostId;
    }
    public void setApplyDate(Date applyDate) 
    {
        this.applyDate = applyDate;
    }

    public Date getApplyDate() 
    {
        return applyDate;
    }
    public void setApplyDutyDate(Date applyDutyDate) 
    {
        this.applyDutyDate = applyDutyDate;
    }

    public Date getApplyDutyDate() 
    {
        return applyDutyDate;
    }
    public void setRecruitCount(Integer recruitCount) 
    {
        this.recruitCount = recruitCount;
    }

    public Integer getRecruitCount() 
    {
        return recruitCount;
    }
    public void setRecruitDescription(String recruitDescription) 
    {
        this.recruitDescription = recruitDescription;
    }

    public String getRecruitDescription() 
    {
        return recruitDescription;
    }
    public void setRecruitEducation(String recruitEducation) 
    {
        this.recruitEducation = recruitEducation;
    }

    public String getRecruitEducation() 
    {
        return recruitEducation;
    }
    public void setRecruitMinAge(Integer recruitMinAge) 
    {
        this.recruitMinAge = recruitMinAge;
    }

    public Integer getRecruitMinAge() 
    {
        return recruitMinAge;
    }
    public void setRecruitMaxAge(Integer recruitMaxAge) 
    {
        this.recruitMaxAge = recruitMaxAge;
    }

    public Integer getRecruitMaxAge() 
    {
        return recruitMaxAge;
    }
    public void setRecruitMarriage(String recruitMarriage) 
    {
        this.recruitMarriage = recruitMarriage;
    }

    public String getRecruitMarriage() 
    {
        return recruitMarriage;
    }
    public void setRecruitSex(String recruitSex) 
    {
        this.recruitSex = recruitSex;
    }

    public String getRecruitSex() 
    {
        return recruitSex;
    }
    public void setRecruitMajor(String recruitMajor) 
    {
        this.recruitMajor = recruitMajor;
    }

    public String getRecruitMajor() 
    {
        return recruitMajor;
    }
    public void setRecruitEfl(String recruitEfl) 
    {
        this.recruitEfl = recruitEfl;
    }

    public String getRecruitEfl() 
    {
        return recruitEfl;
    }
    public void setRecruitSkill(String recruitSkill) 
    {
        this.recruitSkill = recruitSkill;
    }

    public String getRecruitSkill() 
    {
        return recruitSkill;
    }
    public void setRecruitMinSalary(Double recruitMinSalary) 
    {
        this.recruitMinSalary = recruitMinSalary;
    }

    public Double getRecruitMinSalary() 
    {
        return recruitMinSalary;
    }
    public void setRecruitMaxSalary(Double recruitMaxSalary) 
    {
        this.recruitMaxSalary = recruitMaxSalary;
    }

    public Double getRecruitMaxSalary() 
    {
        return recruitMaxSalary;
    }
    public void setRecruitProbationDate(String recruitProbationDate) 
    {
        this.recruitProbationDate = recruitProbationDate;
    }

    public String getRecruitProbationDate() 
    {
        return recruitProbationDate;
    }
    public void setRecruitProbationSalary(Double recruitProbationSalary) 
    {
        this.recruitProbationSalary = recruitProbationSalary;
    }

    public Double getRecruitProbationSalary() 
    {
        return recruitProbationSalary;
    }
    public void setRecruitJobDescription(String recruitJobDescription) 
    {
        this.recruitJobDescription = recruitJobDescription;
    }

    public String getRecruitJobDescription() 
    {
        return recruitJobDescription;
    }
    public void setRecruitType(Integer recruitType) 
    {
        this.recruitType = recruitType;
    }

    public Integer getRecruitType() 
    {
        return recruitType;
    }
    public void setShowType(Integer showType) 
    {
        this.showType = showType;
    }

    public Integer getShowType() 
    {
        return showType;
    }
    public void setReleaseStatus(Integer releaseStatus) 
    {
        this.releaseStatus = releaseStatus;
    }

    public Integer getReleaseStatus() 
    {
        return releaseStatus;
    }


    /*@Autowired
    private IBizTodoItemService bizTodoItemService;

    @Autowired
    private TaskService taskService;*/

    @Override
    public String getTodoUserName() {
        return SpringUtil.getHandleNmae(getInstanceId(), getAuditStatus());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recruitNeedId", getRecruitNeedId())
            .append("empName", getEmpName())
            .append("recruitDeptId", getRecruitDeptId())
            .append("recruitPostId", getRecruitPostId())
            .append("applyDate", getApplyDate())
            .append("applyDutyDate", getApplyDutyDate())
            .append("recruitCount", getRecruitCount())
            .append("recruitDescription", getRecruitDescription())
            .append("recruitEducation", getRecruitEducation())
            .append("recruitMinAge", getRecruitMinAge())
            .append("recruitMaxAge", getRecruitMaxAge())
            .append("recruitMarriage", getRecruitMarriage())
            .append("recruitSex", getRecruitSex())
            .append("recruitMajor", getRecruitMajor())
            .append("recruitEfl", getRecruitEfl())
            .append("recruitSkill", getRecruitSkill())
            .append("recruitMinSalary", getRecruitMinSalary())
            .append("recruitMaxSalary", getRecruitMaxSalary())
            .append("recruitProbationDate", getRecruitProbationDate())
            .append("recruitProbationSalary", getRecruitProbationSalary())
            .append("recruitJobDescription", getRecruitJobDescription())
            .append("recruitType", getRecruitType())
            .append("showType", getShowType())
            .append("releaseStatus", getReleaseStatus())
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
