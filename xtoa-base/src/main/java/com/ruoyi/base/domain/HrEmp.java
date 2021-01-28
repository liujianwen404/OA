package com.ruoyi.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 员工对象 t_hr_emp
 * 
 * @author vivi07
 * @date 2020-05-07
 */
@Data
public class HrEmp extends BaseEntity
{
    private static final long serialVersionUID = 1L;



    /** 岗位id */
    @Excel(name = "工号")
    @Pattern(regexp = "^[A-Za-z0-9]+$",message = "工号格式不正确")
    private String empNum;
    /** 工作地 */
    @Excel(name = "工作地")
    private String city;

    @Excel(name = "区域")
    private String area;//Excel

    @Excel(name = "在职/离职状态", readConverterExp = "0=在职,1=在职,2=在职,3=在职,4=离职")
    private Integer empStatus2;//Excel
    private String empStatus2VO;

    /** 员工姓名 */
    @Excel(name = "员工姓名")
    private String empName;

    /** 部门id */
//    @Excel(name = "部门id")
    private Long deptId;
//    @Excel(name = "部门")
    private String deptName;

    private List<String> deptNameVOList;
    @Excel(name = "一级部门")
    private String deptNameVO1;
    @Excel(name = "二级部门")
    private String deptNameVO2;

    /** 岗位id */
//    @Excel(name = "岗位id")
    private Long postId;
    @Excel(name = "职务")
    private String postName;

    private String postLevel;

    private String postRank;


    @Excel(name = "入职日期", dateFormat = "yyyy-MM-dd")
    private Date nonManagerDate;


    /** 用户性别（0男 1女 2未知） */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;
    private String sexVO;
    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;
    private String phonenumberOther;
    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    private String email;


    /** 身份证号码 */
    @Excel(name = "身份证号码")
    private String idNumber;


    /** 生日 */
    @Excel(name = "出生年月日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;
    private Date birthdayVO;


    @Excel(name = "年龄")
    private String age;


    @Excel(name = "司龄")
    private String siLing;


    /** 员工状态（0：试用期，1：转正，2：实习，3：返聘,4=离职） */
    @Excel(name = "员工状态", readConverterExp = "0=试用期,1=转正,2=实习,3=返聘,4=离职")
    private Integer empStatus;
    private String empStatusVO;


    /** 教育背景 */
    @Excel(name = "最高学历")
    private String education;
    /** 毕业院校 */
    @Excel(name = "毕业院校")
    private String graduation;

    /** 专业 */
    @Excel(name = "所学专业")
    private String major;

    /** 籍贯 */
    @Excel(name = "籍贯")
    private String nationality;
    /** '户口性质' */
    @Excel(name = "户口性质" )
    private String categor;


    /** 身份证住址 */
    @Excel(name = "身份证住址")
    private String address;

    /** 婚姻 */
    @Excel(name = "婚姻")
    private String marriage;

    /** 联系人关系 */
    @Excel(name = "紧急联系人")
    private String contactsName;
    /** 联系人关系 */
    @Excel(name = "与联系人关系")
    private String contactsRelation;
    /** 联系人电话 */
    @Excel(name = "联系人电话")
    private String contactsPhone;

    /** 社保账号 */
    @Excel(name = "社保账号")
    private String socialSecurity;
    /** 公积金账号 */
    @Excel(name = "公积金账号")
    private String providentFund;
    /** 开户行支行 */
    @Excel(name = "开户行支行")
    private String bankBranch;
    /** 银行卡号 */
    @Excel(name = "银行卡号")
    private String bankNumber;

    /** '试用期薪资' */
    @Excel(name = "试用期薪资" )
    private BigDecimal trialSalary;
    /** '转正薪资' */
    @Excel(name = "转正薪资" )
    private BigDecimal conversionSalary;

    /** '招聘专员' */
    @Excel(name = "招聘专员" )
    private String recruiter;
    /** '内部推荐人' */
    @Excel(name = "内部推荐人" )
    private String internaler;
    /** '离职原因' */
    @Excel(name = "离职原因")
    private String quitReason;
    /** '离职手续是否办结' */
    @Excel(name = "离职手续是否办结" , readConverterExp = "0=未办理,1=已办理")
    private String isQuit;
    private String isQuitVO;

    /** 离职日期 */
    @Excel(name = "离职日期", dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date quitDate;




    /** 民族 */
    private String ethnic;

    /** 现居住地 */
    private String nowResidence;

    /** 健康状态 */
    private String health;

    /** 基本情况 */
    private String information;

    /** 家庭成员 */
    private String familyMember;

    /** 联系人地址 */
    private String contactsAddresss;


    /** 毕业时间 */
    private Date graduationDate;
    private String seniority;
    /** 持有证书 */
    private String certificate;
    /** 特长描述 */
    private String description;
    /** 试用期 */
    private String trial;
    /** 试用期结束时间 */
    private Date trialEnd;
    /** 合同主体 */
    private String subjectContract;
    /** 合同年限 */
    private Integer contractYear;
    /** 合同开始 */
    private Date contractStar;
    /** 合同结束 */
    private Date contractEnd;
    private List<HrContract> contractList;

    /** 员工id */
    private Long empId;

    private Date positiveDate;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    /** 用户id */
    private Long userId;
    private String dingUserIdVO;

    /** 当月综合薪资 */
    private BigDecimal comprehensive;

    /** 基本工资 */
    private BigDecimal basic;

    private BigDecimal overtimePay;

    /** 岗位津贴 */
    private BigDecimal allowance;

    /** 其他补贴 */
    private BigDecimal otherSubsidies;

    /** 绩效奖金标准 */
    private BigDecimal performanceBonus;

    /** 薪酬等级 */
    private String payGrade;

    /** 职级 */
    private String rank;

    private List<SalaryStructure> salaryStructures;

    private String photo;

    @Transient
    private Date nonManagerStartDate;

    @Transient
    private Date nonManagerEndDate;

    @Transient
    private Date positiveStartDate;

    @Transient
    private Date positiveEndDate;

    private List<HrEmpExperiences> HrEmpExperiencesList;

    private List<HrEmpTransfers> HrEmpTransfersList;

    public String getEmpStatus2VO() {
        //0=在职,1=在职,2=在职,3=在职,4=离职
        if(empStatus == null){
            return "";
        }
        if (empStatus == 4){
            return "离职";
        }

        return "在职";
    }

    public String getSexVO() {
        if(sex == null){
            return "";
        }
        //0=男,1=女,2=未知
        if ("0".equals(sex)){
            return "男";
        }
        if ("1".equals(sex)){
            return "女";
        }
        return "未知";
    }

    public String getEmpStatusVO() {
        if(empStatus == null){
            return "";
        }
        //0=试用期,1=转正,2=实习,3=返聘,4=离职
        if (empStatus == 0){
            return "试用期";
        }
        if (empStatus == 1){
            return "转正";
        }
        if (empStatus == 2){
            return "实习";
        }
        if (empStatus == 3){
            return "返聘";
        }
        return "离职";
    }

    public String getIsQuitVO() {
        if(isQuit == null){
            return "";
        }
        //0=未办理,1=已办理
        if ("1".equals(isQuit)){
            return "已办理";
        }
        return "未办理";
    }

    public Date getBirthdayVO() {
        return birthday;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getNonManagerDate() {
        return nonManagerDate;
    }

    public void setNonManagerDate(Date nonManagerDate) {
        this.nonManagerDate = nonManagerDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getPositiveDate() {
        return positiveDate;
    }

    public void setPositiveDate(Date positiveDate) {
        this.positiveDate = positiveDate;
    }

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getBirthday() 
    {
        return birthday;
    }
    public void setHealth(String health)
    {
        this.health = health;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

}
