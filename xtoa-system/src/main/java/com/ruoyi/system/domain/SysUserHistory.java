package com.ruoyi.system.domain;

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
 * 用户历史信息对象 sys_user_h
 *
 * @author xt
 * @date 2020-06-23
 */
@Table(name = "sys_user_history")
public class SysUserHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 用户ID */
    @Column(name = "user_id")
    @Excel(name = "用户ID")
    private Long userId;

    /** 部门ID */
    @Column(name = "dept_id")
    @Excel(name = "部门ID")
    private Long deptId;

    /** 登录账号 */
    @Column(name = "login_name")
    @Excel(name = "登录账号")
    private String loginName;

    /** 用户昵称 */
    @Column(name = "user_name")
    @Excel(name = "用户昵称")
    private String userName;

    /** 用户类型（00系统用户） */
    @Column(name = "user_type")
    @Excel(name = "用户类型", readConverterExp = "0=0系统用户")
    private String userType;

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

    /** 头像路径 */
    @Column(name = "avatar")
    @Excel(name = "头像路径")
    private String avatar;

    /** 密码 */
    @Column(name = "password")
    @Excel(name = "密码")
    private String password;

    /** 盐加密 */
    @Column(name = "salt")
    @Excel(name = "盐加密")
    private String salt;

    /** 帐号状态（0正常 1停用） */
    @Column(name = "status")
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 最后登陆IP */
    @Column(name = "login_ip")
    @Excel(name = "最后登陆IP")
    private String loginIp;

    /** 最后登陆时间 */
    @Column(name = "login_date")
    @Excel(name = "最后登陆时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginDate;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getLoginName()
    {
        return loginName;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getUserType()
    {
        return userType;
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
    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    public String getSalt()
    {
        return salt;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public String getLoginIp()
    {
        return loginIp;
    }
    public void setLoginDate(Date loginDate)
    {
        this.loginDate = loginDate;
    }

    public Date getLoginDate()
    {
        return loginDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("deptId", getDeptId())
                .append("loginName", getLoginName())
                .append("userName", getUserName())
                .append("userType", getUserType())
                .append("email", getEmail())
                .append("phonenumber", getPhonenumber())
                .append("sex", getSex())
                .append("avatar", getAvatar())
                .append("password", getPassword())
                .append("salt", getSalt())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("loginIp", getLoginIp())
                .append("loginDate", getLoginDate())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}