package com.ruoyi.system.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * OA角色映射对接系统对象 centre_sys_role
 * 
 * @author xt
 * @date 2020-10-28
 */
@Table(name = "centre_sys_role")
public class CentreSysRole
{
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 中台系统配置自增id */
    @Column(name = "centre_sys_id")
    @Excel(name = "中台系统配置自增id")
    private String centreSysId;

    /** OA角色id */
    @Column(name = "oa_role_id")
    @Excel(name = "OA角色id")
    private Long oaRoleId;

    /** 角色名称 */
    @Column(name = "role_name")
    @Excel(name = "角色名称")
    private String roleName;

    /** 角色id */
    @Column(name = "role_id")
    @Excel(name = "角色id")
    private Long roleId;


    /** 更新者 */
    @ExcelIgnore
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time" ,insertable = false, updatable = false)
    @ExcelIgnore
    private Date updateTime;

    /** 备注 */
    @ExcelIgnore
    private String remark;


    /** 创建者 */
    @ExcelIgnore
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", insertable = false, updatable = false)
    @ExcelIgnore
    private Date createTime;

    @Transient
    private String oaRoleName;

    public String getOaRoleName() {
        return oaRoleName;
    }

    public void setOaRoleName(String oaRoleName) {
        this.oaRoleName = oaRoleName;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCentreSysId() {
        return centreSysId;
    }

    public void setCentreSysId(String centreSysId) {
        this.centreSysId = centreSysId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOaRoleId(Long oaRoleId) 
    {
        this.oaRoleId = oaRoleId;
    }

    public Long getOaRoleId() 
    {
        return oaRoleId;
    }
    public void setRoleName(String roleName) 
    {
        this.roleName = roleName;
    }

    public String getRoleName() 
    {
        return roleName;
    }
    public void setRoleId(Long roleId) 
    {
        this.roleId = roleId;
    }

    public Long getRoleId() 
    {
        return roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("oaRoleId", getOaRoleId())
            .append("roleName", getRoleName())
            .append("roleId", getRoleId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
