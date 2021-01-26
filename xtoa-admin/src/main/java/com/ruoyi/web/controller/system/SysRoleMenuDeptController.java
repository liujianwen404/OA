package com.ruoyi.web.controller.system;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysRoleMenuDept;
import com.ruoyi.system.service.ISysRoleMenuDeptService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 角色拥有的菜单与部门权限关联Controller
 * 
 * @author liujianwen
 * @date 2020-09-22
 */
@Controller
@RequestMapping("/system/roleMenuDept")
public class SysRoleMenuDeptController extends BaseController
{
    private String prefix = "system/roleMenuDept";

    @Autowired
    private ISysRoleMenuDeptService sysRoleMenuDeptService;

    @RequiresPermissions("system:roleMenuDept:view")
    @GetMapping()
    public String roleMenuDept()
    {
        return prefix + "/roleMenuDept";
    }

    /**
     * 查询角色拥有的菜单与部门权限关联列表
     */
    @RequiresPermissions("system:roleMenuDept:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysRoleMenuDept sysRoleMenuDept)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            sysRoleMenuDept.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<SysRoleMenuDept> list = sysRoleMenuDeptService.selectSysRoleMenuDeptList(sysRoleMenuDept);
        return getDataTable(list);
    }

    /**
     * 导出角色拥有的菜单与部门权限关联列表
     */
    @RequiresPermissions("system:roleMenuDept:export")
    @Log(title = "角色拥有的菜单与部门权限关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysRoleMenuDept sysRoleMenuDept)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            sysRoleMenuDept.setCreateId(ShiroUtils.getUserId());
        }
        List<SysRoleMenuDept> list = sysRoleMenuDeptService.selectSysRoleMenuDeptList(sysRoleMenuDept);
        ExcelUtil<SysRoleMenuDept> util = new ExcelUtil<SysRoleMenuDept>(SysRoleMenuDept.class);
        return util.exportExcel(list, "roleMenuDept");
    }

    /**
     * 新增角色拥有的菜单与部门权限关联
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存角色拥有的菜单与部门权限关联
     */
    @Log(title = "角色拥有的菜单与部门权限关联", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysRoleMenuDept sysRoleMenuDept,Long[] deptIds)
    {
        if(sysRoleMenuDeptService.insertSysRoleMenuDept(sysRoleMenuDept,deptIds) > 0){
            return success();
        }
        return error();
    }

    /**
     * 修改角色拥有的菜单与部门权限关联
     */
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        SysRoleMenuDept sysRoleMenuDept = sysRoleMenuDeptService.selectSysRoleMenuDeptById(roleId);
        mmap.put("sysRoleMenuDept", sysRoleMenuDept);
        return prefix + "/edit";
    }

    /**
     * 修改保存角色拥有的菜单与部门权限关联
     */
    @RequiresPermissions("system:roleMenuDept:edit")
    @Log(title = "角色拥有的菜单与部门权限关联", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysRoleMenuDept sysRoleMenuDept)
    {
        return toAjax(sysRoleMenuDeptService.updateSysRoleMenuDept(sysRoleMenuDept));
    }

    /**
     * 删除角色拥有的菜单与部门权限关联
     */
    @RequiresPermissions("system:roleMenuDept:remove")
    @Log(title = "角色拥有的菜单与部门权限关联", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysRoleMenuDeptService.deleteSysRoleMenuDeptByIds(ids));
    }
}
