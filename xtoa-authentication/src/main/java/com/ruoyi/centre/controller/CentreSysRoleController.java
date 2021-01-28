package com.ruoyi.centre.controller;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import java.util.List;

import com.ruoyi.system.service.ISysRoleService;
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
import com.ruoyi.system.domain.CentreSysRole;
import com.ruoyi.system.service.ICentreSysRoleService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * OA角色映射对接系统Controller
 * 
 * @author xt
 * @date 2020-10-28
 */
@Controller
@RequestMapping("/centre/centreRole")
public class CentreSysRoleController extends BaseController
{
    private String prefix = "centre/centreRole";

    @Autowired
    private ICentreSysRoleService centreSysRoleService;
    @Autowired
    private ISysRoleService sysRoleService;

    @RequiresPermissions("centre:centreRole:view")
    @GetMapping()
    public String centreRole(String centreSysId, ModelMap mmap)
    {
        mmap.put("centreSysId",centreSysId);
        return prefix + "/centreRole";
    }

    /**
     * 查询OA角色映射对接系统列表
     */
    @RequiresPermissions("centre:centreRole:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CentreSysRole centreSysRole)
    {
        startPage();
        List<CentreSysRole> list = centreSysRoleService.selectCentreSysRoleList(centreSysRole);
        return getDataTable(list);
    }

    /**
     * 导出OA角色映射对接系统列表
     */
    @RequiresPermissions("centre:centreRole:export")
    @Log(title = "OA角色映射对接系统", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CentreSysRole centreSysRole)
    {
        List<CentreSysRole> list = centreSysRoleService.selectCentreSysRoleList(centreSysRole);
        ExcelUtil<CentreSysRole> util = new ExcelUtil<CentreSysRole>(CentreSysRole.class);
        return util.exportExcel(list, "centreRole");
    }

    /**
     * 新增OA角色映射对接系统
     */
    @GetMapping("/add")
    public String add(String centreSysId, ModelMap mmap)
    {
        mmap.put("centreSysId",centreSysId);
        return prefix + "/add";
    }

    /**
     * 新增保存OA角色映射对接系统
     */
    @RequiresPermissions("centre:centreRole:add")
    @Log(title = "OA角色映射对接系统", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CentreSysRole centreSysRole)
    {
        centreSysRole.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(centreSysRoleService.insertCentreSysRole(centreSysRole));
    }

    /**
     * 修改OA角色映射对接系统
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        CentreSysRole centreSysRole = centreSysRoleService.selectCentreSysRoleById(id);

        SysRole sysRole = sysRoleService.selectRoleById(centreSysRole.getOaRoleId());

        centreSysRole.setOaRoleName(sysRole.getRoleName());

        mmap.put("centreSysRole", centreSysRole);
        return prefix + "/edit";
    }

    /**
     * 修改保存OA角色映射对接系统
     */
    @RequiresPermissions("centre:centreRole:edit")
    @Log(title = "OA角色映射对接系统", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CentreSysRole centreSysRole)
    {
        centreSysRole.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(centreSysRoleService.updateCentreSysRole(centreSysRole));
    }

    /**
     * 删除OA角色映射对接系统
     */
    @RequiresPermissions("centre:centreRole:remove")
    @Log(title = "OA角色映射对接系统", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(centreSysRoleService.deleteCentreSysRoleByIds(ids));
    }
}
