package com.ruoyi.system.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.SysRoleMenuDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 角色拥有的菜单与部门权限关联 数据层
 *
 * @author liujianwen
 * @date 2020-09-22
 */
public interface SysRoleMenuDeptMapper extends MyBaseMapper<SysRoleMenuDept> {

    /**
     * 查询角色拥有的菜单与部门权限关联列表
     *
     * @param sysRoleMenuDept 角色拥有的菜单与部门权限关联
     * @return 角色拥有的菜单与部门权限关联集合
     */
    public List<SysRoleMenuDept> selectSysRoleMenuDeptList(SysRoleMenuDept sysRoleMenuDept);

    /**
     * 删除角色拥有的菜单与部门权限关联
     *
     * @param roleId 角色拥有的菜单与部门权限关联ID
     * @return 结果
     */
    public int deleteSysRoleMenuDeptById(Long roleId);

    /**
     * 批量删除角色拥有的菜单与部门权限关联
     *
     * @param roleIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysRoleMenuDeptByIds(String[] roleIds);

    void deleteSysRoleMenuDeptById(@Param("roleId") Long roleId,@Param("menuId") Long menuId);

    /**
     * 批量新增角色菜单部门信息
     *
     * @param list 角色菜单部门列表
     * @return 结果
     */
    int batchRoleMenuDept(List<SysRoleMenuDept> list);
}