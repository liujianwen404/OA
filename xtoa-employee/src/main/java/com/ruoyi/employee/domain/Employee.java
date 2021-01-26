package com.ruoyi.employee.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 员工对象 employee
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public class Employee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 性别，0女，1男 */
    @Excel(name = "性别，0女，1男")
    private Integer sex;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 民族 */
    @Excel(name = "民族")
    private String nation;

    /** 籍贯 */
    @Excel(name = "籍贯")
    private String nativePlace;

    /** 出生日期 */
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 健康状况 */
    @Excel(name = "健康状况")
    private String healthy;

    /** 现居地 */
    @Excel(name = "现居地")
    private String address;

    /** 教育背景 */
    @Excel(name = "教育背景")
    private String education;

    /** 电子邮件 */
    @Excel(name = "电子邮件")
    private String email;

    /** 照片 */
    @Excel(name = "照片")
    private String image;

    /** 国籍 */
    @Excel(name = "国籍")
    private String nationality;

    /** 身份证号码 */
    @Excel(name = "身份证号码")
    private String idCard;

    /** 基本情况 */
    @Excel(name = "基本情况")
    private String basicSituation;

    /** 特长描述 */
    @Excel(name = "特长描述")
    private String speciality;

    /** 状态：0入档，1在约，1待入职，2在岗，3离职中，4离职，5归档，6禁止 */
    @Excel(name = "状态：0入档，1在约，1待入职，2在岗，3离职中，4离职，5归档，6禁止")
    private Long status;

    /** 所属城市 */
    @Excel(name = "所属城市")
    private String city;

    /** 所属部门ID */
    @Excel(name = "所属部门ID")
    private String departmentId;

    /** 所属部门 */
    @Excel(name = "所属部门")
    private String departmentName;

    /** 职位 */
    @Excel(name = "职位")
    private String position;

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
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setSex(Integer sex) 
    {
        this.sex = sex;
    }

    public Integer getSex() 
    {
        return sex;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setNation(String nation) 
    {
        this.nation = nation;
    }

    public String getNation() 
    {
        return nation;
    }
    public void setNativePlace(String nativePlace) 
    {
        this.nativePlace = nativePlace;
    }

    public String getNativePlace() 
    {
        return nativePlace;
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
    public void setHealthy(String healthy) 
    {
        this.healthy = healthy;
    }

    public String getHealthy() 
    {
        return healthy;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setEducation(String education) 
    {
        this.education = education;
    }

    public String getEducation() 
    {
        return education;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }
    public void setNationality(String nationality) 
    {
        this.nationality = nationality;
    }

    public String getNationality() 
    {
        return nationality;
    }
    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }
    public void setBasicSituation(String basicSituation) 
    {
        this.basicSituation = basicSituation;
    }

    public String getBasicSituation() 
    {
        return basicSituation;
    }
    public void setSpeciality(String speciality) 
    {
        this.speciality = speciality;
    }

    public String getSpeciality() 
    {
        return speciality;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }
    public void setDepartmentId(String departmentId) 
    {
        this.departmentId = departmentId;
    }

    public String getDepartmentId() 
    {
        return departmentId;
    }
    public void setDepartmentName(String departmentName) 
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() 
    {
        return departmentName;
    }
    public void setPosition(String position) 
    {
        this.position = position;
    }

    public String getPosition() 
    {
        return position;
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
            .append("name", getName())
            .append("sex", getSex())
            .append("phone", getPhone())
            .append("nation", getNation())
            .append("nativePlace", getNativePlace())
            .append("birthday", getBirthday())
            .append("healthy", getHealthy())
            .append("address", getAddress())
            .append("education", getEducation())
            .append("email", getEmail())
            .append("image", getImage())
            .append("nationality", getNationality())
            .append("idCard", getIdCard())
            .append("basicSituation", getBasicSituation())
            .append("speciality", getSpeciality())
            .append("status", getStatus())
            .append("city", getCity())
            .append("departmentId", getDepartmentId())
            .append("departmentName", getDepartmentName())
            .append("position", getPosition())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}