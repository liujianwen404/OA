package com.ruoyi.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 招聘申请对象 t_hr_recruit
 * 
 * @author cmw
 * @date 2020-05-11
 */
public class HrRecruit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 招聘id */
    private Long recruitId;

    /** 申请人姓名 */
    @Excel(name = "申请人姓名")
    private String empName;

    /** 需招聘部门id */
    @Excel(name = "需招聘部门id")
    private Long recruitDeptId;

    /** 需招聘岗位id */
    @Excel(name = "需招聘岗位id")
    private Long recruitPostId;

    /** 申请日期 */
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyDate;

    /** 申请到岗时间 */
    @Excel(name = "申请到岗时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyDutyDate;

    /** 招聘人数 */
    @Excel(name = "招聘人数")
    private Integer recruitCount;

    /** 入职人数 */
    @Excel(name = "入职人数")
    private Integer nonManagerCount;

    /** 待入职人数 */
    @Excel(name = "待入职人数")
    private Integer awaitCount;

    /** 岗位描述 */
    @Excel(name = "岗位描述")
    private String recruitDescription;

    /** 教育背景 */
    @Excel(name = "教育背景")
    private String recruitEducation;

    /** 年龄下限 */
    @Excel(name = "年龄下限")
    private Integer recruitMinAge;

    /** 年龄上限 */
    @Excel(name = "年龄上限")
    private Integer recruitMaxAge;

    /** 婚姻 */
    @Excel(name = "婚姻")
    private String recruitMarriage;

    /** 招聘性别（0男 1女 2未知） */
    @Excel(name = "招聘性别", readConverterExp = "0=男,1=女,2=未知")
    private String recruitSex;

    /** 专业限制 */
    @Excel(name = "专业限制")
    private String recruitMajor;

    /** 外语要求 */
    @Excel(name = "外语要求")
    private String recruitEfl;

    /** 需要技能 */
    @Excel(name = "需要技能")
    private String recruitSkill;

    /** 薪资下限 */
    @Excel(name = "薪资下限")
    private Double recruitMinSalary;

    /** 薪资上限 */
    @Excel(name = "薪资上限")
    private Double recruitMaxSalary;

    /** 试用期 */
    @Excel(name = "试用期")
    private String recruitProbationDate;

    /** 试用期薪资 */
    @Excel(name = "试用期薪资")
    private Double recruitProbationSalary;

    /** 工作经验 */
    @Excel(name = "工作经验")
    private String recruitJobDescription;

    /** 招聘类型（0：需求，1：储备） */
    @Excel(name = "招聘类型", readConverterExp = "0=：需求，1：储备")
    private Integer recruitType;

    /** 展示类型（0：需求，1：储备） */
    @Excel(name = "展示类型", readConverterExp = "0=：需求，1：储备")
    private Integer showType;

    /** 发布状态（0：保存，1：提交） */
    @Excel(name = "发布状态", readConverterExp = "0=：保存，1：提交")
    private Integer releaseStatus;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    public Integer getAwaitCount() {
        return awaitCount;
    }

    public void setAwaitCount(Integer awaitCount) {
        this.awaitCount = awaitCount;
    }

    public void setRecruitId(Long recruitId)
    {
        this.recruitId = recruitId;
    }

    public Long getRecruitId() 
    {
        return recruitId;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getApplyDate() 
    {
        return applyDate;
    }
    public void setApplyDutyDate(Date applyDutyDate) 
    {
        this.applyDutyDate = applyDutyDate;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    public void setNonManagerCount(Integer nonManagerCount) 
    {
        this.nonManagerCount = nonManagerCount;
    }

    public Integer getNonManagerCount() 
    {
        return nonManagerCount;
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
            .append("recruitId", getRecruitId())
            .append("empName", getEmpName())
            .append("recruitDeptId", getRecruitDeptId())
            .append("recruitPostId", getRecruitPostId())
            .append("applyDate", getApplyDate())
            .append("applyDutyDate", getApplyDutyDate())
            .append("recruitCount", getRecruitCount())
            .append("nonManagerCount", getNonManagerCount())
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
