package com.ruoyi.employee.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 员工教育记录对象 employee_education_record
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public class EmployeeEducationRecord extends BaseEntity
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

    /** 学校 */
    @Excel(name = "学校")
    private String school;

    /** 专业 */
    @Excel(name = "专业")
    private String specialities;

    /** 入学日期 */
    @Excel(name = "入学日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entranceDate;

    /** 毕业日期 */
    @Excel(name = "毕业日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date graduateDate;

    /** 文凭 */
    @Excel(name = "文凭")
    private String diploma;

    /** 文凭扫描件 */
    @Excel(name = "文凭扫描件")
    private String diplomaImage;

    /** 文凭性质 */
    @Excel(name = "文凭性质")
    private String diplomaType;

    /** 状态：0未验证，1已验证，2验证不通过 */
    @Excel(name = "状态：0未验证，1已验证，2验证不通过")
    private String status;

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
    public void setSchool(String school) 
    {
        this.school = school;
    }

    public String getSchool() 
    {
        return school;
    }
    public void setSpecialities(String specialities) 
    {
        this.specialities = specialities;
    }

    public String getSpecialities() 
    {
        return specialities;
    }
    public void setEntranceDate(Date entranceDate) 
    {
        this.entranceDate = entranceDate;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEntranceDate() 
    {
        return entranceDate;
    }
    public void setGraduateDate(Date graduateDate) 
    {
        this.graduateDate = graduateDate;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getGraduateDate() 
    {
        return graduateDate;
    }
    public void setDiploma(String diploma) 
    {
        this.diploma = diploma;
    }

    public String getDiploma() 
    {
        return diploma;
    }
    public void setDiplomaImage(String diplomaImage) 
    {
        this.diplomaImage = diplomaImage;
    }

    public String getDiplomaImage() 
    {
        return diplomaImage;
    }
    public void setDiplomaType(String diplomaType) 
    {
        this.diplomaType = diplomaType;
    }

    public String getDiplomaType() 
    {
        return diplomaType;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("school", getSchool())
            .append("specialities", getSpecialities())
            .append("entranceDate", getEntranceDate())
            .append("graduateDate", getGraduateDate())
            .append("diploma", getDiploma())
            .append("diplomaImage", getDiplomaImage())
            .append("diplomaType", getDiplomaType())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
