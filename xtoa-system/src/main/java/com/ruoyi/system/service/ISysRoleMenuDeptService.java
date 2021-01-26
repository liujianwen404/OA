package com.ruoyi.system.service;

import java.util.List;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.system.domain.SysRoleMenuDept;

/**
 * 角色拥有的菜单与部门权限关联Service接口
 * 
 * @author liujianwen
 * @date 2020-09-22
 */
public interface ISysRoleMenuDeptService 
{
    /**
     * 查询角色拥有的菜单与部门权限关联
     * 
     * @param roleId 角色拥有的菜单与部门权限关联ID
     * @return 角色拥有的菜单与部门权限关联
     */
    public SysRoleMenuDept selectSysRoleMenuDeptById(Long roleId);

    /**
     * 查询角色拥有的菜单与部门权限关联列表
     * 
     * @param sysRoleMenuDept 角色拥有的菜单与部门权限关联
     * @return 角色拥有的菜单与部门权限关联集合
     */
    public List<SysRoleMenuDept> selectSysRoleMenuDeptList(SysRoleMenuDept sysRoleMenuDept);

    /**
     * 新增角色拥有的菜单与部门权限关联
     * 
     * @param sysRoleMenuDept 角色拥有的菜单与部门权限关联
     * @return 结果
     */
    public int insertSysRoleMenuDept(SysRoleMenuDept sysRoleMenuDept,Long[] deptIds);

    /**
     * 修改角色拥有的菜单与部门权限关联
     * 
     * @param sysRoleMenuDept 角色拥有的菜单与部门权限关联
     * @return 结果
     */
    public int updateSysRoleMenuDept(SysRoleMenuDept sysRoleMenuDept);

    /**
     * 批量删除角色拥有的菜单与部门权限关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysRoleMenuDeptByIds(String ids);

    /**
     * 删除角色拥有的菜单与部门权限关联信息
     * 
     * @param roleId 角色拥有的菜单与部门权限关联ID
     * @return 结果
     */
    public int deleteSysRoleMenuDeptById(Long roleId);

    SysRoleMenuDept selectSingleOneByExample(Example example);

    List<SysRoleMenuDept> selectByExample(Example example);

}
