package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.base.domain.Area;
import com.ruoyi.centre.domain.CentreSysConfig;
import com.ruoyi.centre.service.ICentreSysConfigService;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.hr.service.IAreaService;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.ICentreSysRoleService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.impl.SysRoleServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 角色信息
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController
{
    private String prefix = "system/role";

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ICentreSysRoleService centreSysRoleService;
    @Autowired
    private ICentreSysConfigService centreSysConfigService;

    @Autowired
    private ISysDeptService deptService;

    @RequiresPermissions("system:role:view")
    @GetMapping()
    public String role(ModelMap mmap)
    {
        List<SysDept> deptList = deptService.selectFirstDeptData();
        mmap.put("deptList",deptList);
        return prefix + "/role";
    }

    @RequiresPermissions("system:role:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysRole role)
    {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:role:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysRole role)
    {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 新增角色
     */
    @GetMapping("/add")
    public String add(ModelMap map)
    {
        List<SysDept> deptList = deptService.selectFirstDeptData();
        map.put("deptList",deptList);
        return prefix + "/add";
    }

    /**
     * 新增保存角色
     */
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysRole role)
    {
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
        {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(ShiroUtils.getLoginName());
        ShiroUtils.clearCachedAuthorizationInfo();
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{roleId}")
    public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {

        SysRole sysRole = roleService.selectRoleById(roleId);
        if (StringUtil.isNotEmpty(sysRole.getAreaIds())){
            List<String> stringBuilder = new ArrayList<>();
            String[] split = sysRole.getAreaIds().split(",");
            for (String areaId : split) {
                stringBuilder.add(areaService.selectAreaById(Long.parseLong(areaId)).getFullname());
            }
            sysRole.setAreaNames(ArrayUtil.join(stringBuilder.toArray(),","));
        }

        List<SysDept> deptList = deptService.selectFirstDeptData();
        mmap.put("deptList",deptList);
        mmap.put("role", sysRole);
        return prefix + "/edit";
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysRole role)
    {
        if (!ShiroUtils.getSysUser().isAdmin() && role != null && SysRoleServiceImpl.roleKeys.contains(role.getRoleKey())){
            throw new BusinessException("不允许操作流程相关角色，请使用admin账号");
        }

        roleService.checkRoleAllowed(role);
        if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
        {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        }
        else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
        {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(ShiroUtils.getLoginName());
        ShiroUtils.clearCachedAuthorizationInfo();
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 角色分配数据权限
     */
    @GetMapping("/authDataScope/{roleId}")
    public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/dataScope";
    }

    /**
     * 保存角色分配数据权限
     */
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/authDataScope")
    @ResponseBody
    public AjaxResult authDataScopeSave(SysRole role)
    {
        if (!ShiroUtils.getSysUser().isAdmin() && role != null && SysRoleServiceImpl.roleKeys.contains(role.getRoleKey())){
            throw new BusinessException("不允许操作流程相关角色，请使用admin账号");
        }
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(ShiroUtils.getLoginName());
        if (roleService.authDataScope(role) > 0)
        {
            ShiroUtils.setSysUser(userService.selectUserById(ShiroUtils.getSysUser().getUserId()));
            return success();
        }
        return error();
    }

    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            Long[] roleIds = Convert.toLongArray(ids);
            for (Long roleId : roleIds) {
                /*SysRole sysRole = roleService.selectRoleById(roleId);
                if (!ShiroUtils.getSysUser().isAdmin() && sysRole != null && SysRoleServiceImpl.roleKeys.contains(sysRole.getRoleKey())){
                    throw new BusinessException("不允许操作流程相关角色，请使用admin账号");
                }*/
                SysRole role = roleService.selectRoleById(roleId);
                Example example = new Example(CentreSysRole.class);
                example.createCriteria().andEqualTo("oaRoleId",roleId);
                List<CentreSysRole> centreSysRoles = centreSysRoleService.selectByExample(example);
                if (centreSysRoles != null && !centreSysRoles.isEmpty()){

                    //外部系统名称
                    Set<String> sysIds = centreSysRoles.stream().map(CentreSysRole::getCentreSysId).collect(Collectors.toSet());
                    Set<String> sysName = sysIds.stream().map(sysid -> {
                        Example exampleConfig = new Example(CentreSysConfig.class);
                        exampleConfig.createCriteria().andEqualTo("sysId", sysid);
                        CentreSysConfig centreSysConfig = centreSysConfigService.selectOneByExample(exampleConfig);
                        if (centreSysConfig == null) {
                            return "";
                        }
                        return centreSysConfig.getName();
                    }).collect(Collectors.toSet());

                    throw new BusinessException(StrUtil.format("{}已分配外部系统 {},不能删除",
                            role.getRoleName(),
                            ArrayUtil.join(sysName.toArray(),",")));
                }
            }
            return toAjax(roleService.deleteRoleByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public String checkRoleNameUnique(SysRole role)
    {
        return roleService.checkRoleNameUnique(role);
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    public String checkRoleKeyUnique(SysRole role)
    {
        return roleService.checkRoleKeyUnique(role);
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree()
    {
        return prefix + "/tree";
    }

    /**
     * 角色状态修改
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:role:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysRole role)
    {
        if (!ShiroUtils.getSysUser().isAdmin() && role != null && SysRoleServiceImpl.roleKeys.contains(role.getRoleKey())){
            throw new BusinessException("不允许操作流程相关角色，请使用admin账号");
        }
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.changeStatus(role));
    }

    /**
     * 分配用户
     */
    @RequiresPermissions("system:role:edit")
    @GetMapping("/authUser/{roleId}")
    public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/authUser";
    }

    /**
     * 查询已分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/allocatedList")
    @ResponseBody
    public TableDataInfo allocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancel")
    @ResponseBody
    public AjaxResult cancelAuthUser(SysUserRole userRole)
    {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancelAll")
    @ResponseBody
    public AjaxResult cancelAuthUserAll(Long roleId, String userIds)
    {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 选择用户
     */
    @GetMapping("/authUser/selectUser/{roleId}")
    public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap)
    {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/selectUser";
    }

    /**
     * 查询未分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/unallocatedList")
    @ResponseBody
    public TableDataInfo unallocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 批量选择用户授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/selectAll")
    @ResponseBody
    public AjaxResult selectAuthUserAll(Long roleId, String userIds)
    {
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 加载角色列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(Long roleId)
    {
        List<Ztree> ztrees = roleService.selectRoleListTree(roleId);
        return ztrees;
    }
    /**
     * 加载角色列表树
     */
    @GetMapping("/selectTree")
    public String selectTree()
    {
        return prefix + "/tree";
    }




}