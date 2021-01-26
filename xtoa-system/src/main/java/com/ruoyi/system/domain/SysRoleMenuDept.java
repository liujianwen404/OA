package com.ruoyi.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 角色拥有的菜单与部门权限关联对象 sys_role_menu_dept
 * 
 * @author liujianwen
 * @date 2020-09-22
 */
@Table(name = "sys_role_menu_dept")
@Data
public class SysRoleMenuDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long roleId;

    /** 菜单ID */
    private Long menuId;

    /** 部门ID */
    @Column(name = "dept_id")
    @Excel(name = "部门ID")
    private Long deptId;

    /** 菜单权限标识 */
    @Column(name = "perms")
    @Excel(name = "菜单权限标识")
    private String perms;


}
