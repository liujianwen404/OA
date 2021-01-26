package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysRoleMenuDeptMapper;
import com.ruoyi.system.domain.SysRoleMenuDept;
import com.ruoyi.system.service.ISysRoleMenuDeptService;
import com.ruoyi.common.core.text.Convert;

import javax.annotation.Resource;

/**
 * 角色拥有的菜单与部门权限关联Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-09-22
 */
@Service
public class SysRoleMenuDeptServiceImpl implements ISysRoleMenuDeptService 
{

    private static final Logger logger = LoggerFactory.getLogger(SysRoleMenuDeptServiceImpl.class);

    @Resource
    private SysRoleMenuDeptMapper sysRoleMenuDeptMapper;

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 查询角色拥有的菜单与部门权限关联
     * 
     * @param roleId 角色拥有的菜单与部门权限关联ID
     * @return 角色拥有的菜单与部门权限关联
     */
    @Override
    public SysRoleMenuDept selectSysRoleMenuDeptById(Long roleId)
    {
        return sysRoleMenuDeptMapper.selectByPrimaryKey(roleId);
    }

    /**
     * 查询角色拥有的菜单与部门权限关联列表
     * 
     * @param sysRoleMenuDept 角色拥有的菜单与部门权限关联
     * @return 角色拥有的菜单与部门权限关联
     */
    @Override
    public List<SysRoleMenuDept> selectSysRoleMenuDeptList(SysRoleMenuDept sysRoleMenuDept)
    {
        sysRoleMenuDept.setDelFlag("0");
        return sysRoleMenuDeptMapper.selectSysRoleMenuDeptList(sysRoleMenuDept);
    }

    /**
     * 新增角色拥有的菜单与部门权限关联
     * 
     * @param sysRoleMenuDept 角色拥有的菜单与部门权限关联
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSysRoleMenuDept(SysRoleMenuDept sysRoleMenuDept,Long[] deptIds)
    {
        List<Long> deptIdList =  Arrays.asList(deptIds);
        List<Long> list = new ArrayList<>();
        // 删除角色与部门关联
        sysRoleMenuDeptMapper.deleteSysRoleMenuDeptById(sysRoleMenuDept.getRoleId(),sysRoleMenuDept.getMenuId());
        //查询一级部门下的所有部门id，放入到deptIds数组中，一起插入到角色菜单部门关系表
        for (int i=0;i<deptIdList.size();i++) {
            List<Long> sonDeptIds = sysDeptService.selectFirstDeptIdAndSon(deptIdList.get(i));
            list.addAll(sonDeptIds);
        }
        list.addAll(deptIdList);
        // 新增角色和部门信息（数据权限）
        return insertRoleMenuDept(sysRoleMenuDept,list);
    }

    /**
     * 新增角色菜单部门信息(数据权限)
     *
     * @param sysRoleMenuDept
     */
    public int insertRoleMenuDept(SysRoleMenuDept sysRoleMenuDept,List<Long> deptIds)
    {
        int rows = 1;
        // 新增角色与菜单与部门（数据权限）管理
        List<SysRoleMenuDept> list = new ArrayList<>();
        for (Long deptId : deptIds)
        {
            SysRoleMenuDept roleMenuDept = new SysRoleMenuDept();
            roleMenuDept.setRoleId(sysRoleMenuDept.getRoleId());
            roleMenuDept.setMenuId(sysRoleMenuDept.getMenuId());
            roleMenuDept.setDeptId(deptId);
            roleMenuDept.setPerms(sysRoleMenuDept.getPerms());
            list.add(roleMenuDept);
        }
        if (list.size() > 0)
        {
            rows = sysRoleMenuDeptMapper.batchRoleMenuDept(list);
        }
        return rows;
    }

    /**
     * 修改角色拥有的菜单与部门权限关联
     * 
     * @param sysRoleMenuDept 角色拥有的菜单与部门权限关联
     * @return 结果
     */
    @Override
    public int updateSysRoleMenuDept(SysRoleMenuDept sysRoleMenuDept)
    {
        return sysRoleMenuDeptMapper.updateByPrimaryKeySelective(sysRoleMenuDept);
    }

    /**
     * 删除角色拥有的菜单与部门权限关联对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysRoleMenuDeptByIds(String ids)
    {
        return sysRoleMenuDeptMapper.deleteSysRoleMenuDeptByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除角色拥有的菜单与部门权限关联信息
     * 
     * @param roleId 角色拥有的菜单与部门权限关联ID
     * @return 结果
     */
    @Override
    public int deleteSysRoleMenuDeptById(Long roleId)
    {
        return sysRoleMenuDeptMapper.deleteSysRoleMenuDeptById(roleId);
    }



    @Override
    public SysRoleMenuDept selectSingleOneByExample(Example example){
        return sysRoleMenuDeptMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<SysRoleMenuDept> selectByExample(Example example){
        return sysRoleMenuDeptMapper.selectByExample(example);
    }

}
