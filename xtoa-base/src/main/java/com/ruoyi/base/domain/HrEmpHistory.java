package com.ruoyi.base.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 员工历史对象 t_hr_emp_h
 *
 * @author xt
 * @date 2020-06-23
 */
@Table(name = "t_hr_emp_history")
public class HrEmpHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 员工id */
    @Column(name = "emp_id")
    @Excel(name = "员工id")
    private Long empId;

    /** 用户id */
    @Column(name = "user_id")
    @Excel(name = "用户id")
    private Long userId;

    /** 工号 */
    @Column(name = "emp_num")
    @Excel(name = "工号")
    private String empNum;

    /** 员工状态（0：试用期，1：转正，2：实习，3：返聘） */
    @Column(name = "emp_status")
    @Excel(name = "员工状态", readConverterExp = "0=：试用期，1：转正，2：实习，3：返聘")
    private Integer empStatus;

    /** 员工姓名 */
    @Column(name = "emp_name")
    @Excel(name = "员工姓名")
    private String empName;

    /** 用户邮箱 */
    @Column(name = "email")
    @Excel(name = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Column(name = "phonenumber")
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 用户性别（0男 1女 2未知） */
    @Column(name = "sex")
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 婚姻 */
    @Column(name = "marriage")
    @Excel(name = "婚姻")
    private String marriage;

    /** 名族 */
    @Column(name = "ethnic")
    @Excel(name = "名族")
    private String ethnic;

    /** 生日 */
    @Column(name = "birthday")
    @Excel(name = "生日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 转正日期 */
    @Column(name = "positive_date")
    @Excel(name = "转正日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date positiveDate;

    /** 入职日期 */
    @Column(name = "non_manager_date")
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date nonManagerDate;

    /** 离职时间 */
    @Column(name = "quit_date")
    @Excel(name = "离职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date quitDate;

    /** 离职手续是否办结（0 未办理，1 已办理） */
    @Column(name = "is_quit")
    @Excel(name = "离职手续是否办结", readConverterExp = "0=,未=办理，1,已=办理")
    private String isQuit;

    /** 离职原因 */
    @Column(name = "quit_reason")
    @Excel(name = "离职原因")
    private String quitReason;

    /** 试用 */
    @Column(name = "trial")
    @Excel(name = "试用")
    private String trial;

    /** 试用期结束时间 */
    @Column(name = "trial_end")
    @Excel(name = "试用期结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trialEnd;

    /** 健康状态 */
    @Column(name = "health")
    @Excel(name = "健康状态")
    private String health;

    /** 现居住地 */
    @Column(name = "now_residence")
    @Excel(name = "现居住地")
    private String nowResidence;

    /** 教育背景 */
    @Column(name = "education")
    @Excel(name = "教育背景")
    private String education;

    /** 户籍 */
    @Column(name = "nationality")
    @Excel(name = "户籍")
    private String nationality;

    /** 户口性质 */
    @Column(name = "categor")
    @Excel(name = "户口性质")
    private String categor;

    /** 社保账号 */
    @Column(name = "social_security")
    @Excel(name = "社保账号")
    private String socialSecurity;

    /** 公积金账号 */
    @Column(name = "provident_fund")
    @Excel(name = "公积金账号")
    private String providentFund;

    /** 身份证号码 */
    @Column(name = "id_number")
    @Excel(name = "身份证号码")
    private String idNumber;

    /** 身份证住址 */
    @Column(name = "address")
    @Excel(name = "身份证住址")
    private String address;

    /** 基本情况 */
    @Column(name = "information")
    @Excel(name = "基本情况")
    private String information;

    /** 特长描述 */
    @Column(name = "description")
    @Excel(name = "特长描述")
    private String description;

    /** 所属城市 */
    @Column(name = "city")
    @Excel(name = "所属城市")
    private String city;

    /** 联系人关系 */
    @Column(name = "contacts_relation")
    @Excel(name = "联系人关系")
    private String contactsRelation;

    /** 紧急联系人名称 */
    @Column(name = "contacts_name")
    @Excel(name = "紧急联系人名称")
    private String contactsName;

    /** 联系人电话 */
    @Column(name = "contacts_phone")
    @Excel(name = "联系人电话")
    private String contactsPhone;

    /** 联系人地址 */
    @Column(name = "contacts_addresss")
    @Excel(name = "联系人地址")
    private String contactsAddresss;

    /** 家庭成员 */
    @Column(name = "family_member")
    @Excel(name = "家庭成员")
    private String familyMember;

    /** 专业 */
    @Column(name = "major")
    @Excel(name = "专业")
    private String major;

    /** 持有证书 */
    @Column(name = "certificate")
    @Excel(name = "持有证书")
    private String certificate;

    /** 毕业学校 */
    @Column(name = "graduation")
    @Excel(name = "毕业学校")
    private String graduation;

    /** 毕业时间 */
    @Column(name = "graduation_date")
    @Excel(name = "毕业时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date graduationDate;

    /** 开户行支行 */
    @Column(name = "bank_branch")
    @Excel(name = "开户行支行")
    private String bankBranch;

    /** 银行卡号 */
    @Column(name = "bank_number")
    @Excel(name = "银行卡号")
    private String bankNumber;

    /** 部门id */
    @Column(name = "dept_id")
    @Excel(name = "部门id")
    private Long deptId;

    /** 转正薪资 */
    @Column(name = "conversion_salary")
    @Excel(name = "转正薪资")
    private Double conversionSalary;

    /** 试用期薪资 */
    @Column(name = "trial_salary")
    @Excel(name = "试用期薪资")
    private Double trialSalary;

    /** 内部推荐人 */
    @Column(name = "internaler")
    @Excel(name = "内部推荐人")
    private String internaler;

    /** 招聘专员 */
    @Column(name = "recruiter")
    @Excel(name = "招聘专员")
    private String recruiter;

    /** 岗位id */
    @Column(name = "post_id")
    @Excel(name = "岗位id")
    private Long postId;

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
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setEmpNum(String empNum)
    {
        this.empNum = empNum;
    }

    public String getEmpNum()
    {
        return empNum;
    }
    public void setEmpStatus(Integer empStatus)
    {
        this.empStatus = empStatus;
    }

    public Integer getEmpStatus()
    {
        return empStatus;
    }
    public void setEmpName(String empName)
    {
        this.empName = empName;
    }

    public String getEmpName()
    {
        return empName;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getSex()
    {
        return sex;
    }
    public void setMarriage(String marriage)
    {
        this.marriage = marriage;
    }

    public String getMarriage()
    {
        return marriage;
    }
    public void setEthnic(String ethnic)
    {
        this.ethnic = ethnic;
    }

    public String getEthnic()
    {
        return ethnic;
    }
    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Date getBirthday()
    {
        return birthday;
    }
    public void setPositiveDate(Date positiveDate)
    {
        this.positiveDate = positiveDate;
    }

    public Date getPositiveDate()
    {
        return positiveDate;
    }
    public void setNonManagerDate(Date nonManagerDate)
    {
        this.nonManagerDate = nonManagerDate;
    }

    public Date getNonManagerDate()
    {
        return nonManagerDate;
    }
    public void setQuitDate(Date quitDate)
    {
        this.quitDate = quitDate;
    }

    public Date getQuitDate()
    {
        return quitDate;
    }
    public void setIsQuit(String isQuit)
    {
        this.isQuit = isQuit;
    }

    public String getIsQuit()
    {
        return isQuit;
    }
    public void setQuitReason(String quitReason)
    {
        this.quitReason = quitReason;
    }

    public String getQuitReason()
    {
        return quitReason;
    }
    public void setTrial(String trial)
    {
        this.trial = trial;
    }

    public String getTrial()
    {
        return trial;
    }
    public void setTrialEnd(Date trialEnd)
    {
        this.trialEnd = trialEnd;
    }

    public Date getTrialEnd()
    {
        return trialEnd;
    }
    public void setHealth(String health)
    {
        this.health = health;
    }

    public String getHealth()
    {
        return health;
    }
    public void setNowResidence(String nowResidence)
    {
        this.nowResidence = nowResidence;
    }

    public String getNowResidence()
    {
        return nowResidence;
    }
    public void setEducation(String education)
    {
        this.education = education;
    }

    public String getEducation()
    {
        return education;
    }
    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }

    public String getNationality()
    {
        return nationality;
    }
    public void setCategor(String categor)
    {
        this.categor = categor;
    }

    public String getCategor()
    {
        return categor;
    }
    public void setSocialSecurity(String socialSecurity)
    {
        this.socialSecurity = socialSecurity;
    }

    public String getSocialSecurity()
    {
        return socialSecurity;
    }
    public void setProvidentFund(String providentFund)
    {
        this.providentFund = providentFund;
    }

    public String getProvidentFund()
    {
        return providentFund;
    }
    public void setIdNumber(String idNumber)
    {
        this.idNumber = idNumber;
    }

    public String getIdNumber()
    {
        return idNumber;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setInformation(String information)
    {
        this.information = information;
    }

    public String getInformation()
    {
        return information;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }
    public void setContactsRelation(String contactsRelation)
    {
        this.contactsRelation = contactsRelation;
    }

    public String getContactsRelation()
    {
        return contactsRelation;
    }
    public void setContactsName(String contactsName)
    {
        this.contactsName = contactsName;
    }

    public String getContactsName()
    {
        return contactsName;
    }
    public void setContactsPhone(String contactsPhone)
    {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsPhone()
    {
        return contactsPhone;
    }
    public void setContactsAddresss(String contactsAddresss)
    {
        this.contactsAddresss = contactsAddresss;
    }

    public String getContactsAddresss()
    {
        return contactsAddresss;
    }
    public void setFamilyMember(String familyMember)
    {
        this.familyMember = familyMember;
    }

    public String getFamilyMember()
    {
        return familyMember;
    }
    public void setMajor(String major)
    {
        this.major = major;
    }

    public String getMajor()
    {
        return major;
    }
    public void setCertificate(String certificate)
    {
        this.certificate = certificate;
    }

    public String getCertificate()
    {
        return certificate;
    }
    public void setGraduation(String graduation)
    {
        this.graduation = graduation;
    }

    public String getGraduation()
    {
        return graduation;
    }
    public void setGraduationDate(Date graduationDate)
    {
        this.graduationDate = graduationDate;
    }

    public Date getGraduationDate()
    {
        return graduationDate;
    }
    public void setBankBranch(String bankBranch)
    {
        this.bankBranch = bankBranch;
    }

    public String getBankBranch()
    {
        return bankBranch;
    }
    public void setBankNumber(String bankNumber)
    {
        this.bankNumber = bankNumber;
    }

    public String getBankNumber()
    {
        return bankNumber;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setConversionSalary(Double conversionSalary)
    {
        this.conversionSalary = conversionSalary;
    }

    public Double getConversionSalary()
    {
        return conversionSalary;
    }
    public void setTrialSalary(Double trialSalary)
    {
        this.trialSalary = trialSalary;
    }

    public Double getTrialSalary()
    {
        return trialSalary;
    }
    public void setInternaler(String internaler)
    {
        this.internaler = internaler;
    }

    public String getInternaler()
    {
        return internaler;
    }
    public void setRecruiter(String recruiter)
    {
        this.recruiter = recruiter;
    }

    public String getRecruiter()
    {
        return recruiter;
    }
    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    public Long getPostId()
    {
        return postId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("empId", getEmpId())
                .append("userId", getUserId())
                .append("empNum", getEmpNum())
                .append("empStatus", getEmpStatus())
                .append("empName", getEmpName())
                .append("email", getEmail())
                .append("phonenumber", getPhonenumber())
                .append("sex", getSex())
                .append("marriage", getMarriage())
                .append("ethnic", getEthnic())
                .append("birthday", getBirthday())
                .append("positiveDate", getPositiveDate())
                .append("nonManagerDate", getNonManagerDate())
                .append("quitDate", getQuitDate())
                .append("isQuit", getIsQuit())
                .append("quitReason", getQuitReason())
                .append("trial", getTrial())
                .append("trialEnd", getTrialEnd())
                .append("health", getHealth())
                .append("nowResidence", getNowResidence())
                .append("education", getEducation())
                .append("nationality", getNationality())
                .append("categor", getCategor())
                .append("socialSecurity", getSocialSecurity())
                .append("providentFund", getProvidentFund())
                .append("idNumber", getIdNumber())
                .append("address", getAddress())
                .append("information", getInformation())
                .append("description", getDescription())
                .append("city", getCity())
                .append("contactsRelation", getContactsRelation())
                .append("contactsName", getContactsName())
                .append("contactsPhone", getContactsPhone())
                .append("contactsAddresss", getContactsAddresss())
                .append("familyMember", getFamilyMember())
                .append("major", getMajor())
                .append("certificate", getCertificate())
                .append("graduation", getGraduation())
                .append("graduationDate", getGraduationDate())
                .append("bankBranch", getBankBranch())
                .append("bankNumber", getBankNumber())
                .append("deptId", getDeptId())
                .append("conversionSalary", getConversionSalary())
                .append("trialSalary", getTrialSalary())
                .append("internaler", getInternaler())
                .append("recruiter", getRecruiter())
                .append("postId", getPostId())
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