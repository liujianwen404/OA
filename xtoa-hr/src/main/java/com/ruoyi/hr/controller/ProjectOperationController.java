package com.ruoyi.hr.controller;

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
import com.ruoyi.base.domain.ProjectOperation;
import com.ruoyi.hr.service.IProjectOperationService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目计划任务操作记录Controller
 * 
 * @author xt
 * @date 2020-06-30
 */
@Controller
@RequestMapping("/project/projectOperation")
public class ProjectOperationController extends BaseController
{
    private String prefix = "project/projectOperation";

    @Autowired
    private IProjectOperationService projectOperationService;

    @RequiresPermissions("project:projectOperation:view")
    @GetMapping()
    public String projectOperation()
    {
        return prefix + "/projectOperation";
    }

    /**
     * 查询项目计划任务操作记录列表
     */
    @RequiresPermissions("project:projectOperation:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ProjectOperation projectOperation)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            projectOperation.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<ProjectOperation> list = projectOperationService.selectProjectOperationList(projectOperation);
        return getDataTable(list);
    }

    /**
     * 导出项目计划任务操作记录列表
     */
    @RequiresPermissions("project:projectOperation:export")
    @Log(title = "项目计划任务操作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProjectOperation projectOperation)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            projectOperation.setCreateId(ShiroUtils.getUserId());
        }
        List<ProjectOperation> list = projectOperationService.selectProjectOperationList(projectOperation);
        ExcelUtil<ProjectOperation> util = new ExcelUtil<ProjectOperation>(ProjectOperation.class);
        return util.exportExcel(list, "projectOperation");
    }

    /**
     * 新增项目计划任务操作记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存项目计划任务操作记录
     */
    @RequiresPermissions("project:projectOperation:add")
    @Log(title = "项目计划任务操作记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ProjectOperation projectOperation)
    {
        return toAjax(projectOperationService.insertProjectOperation(projectOperation));
    }

    /**
     * 修改项目计划任务操作记录
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ProjectOperation projectOperation = projectOperationService.selectProjectOperationById(id);
        mmap.put("projectOperation", projectOperation);
        return prefix + "/edit";
    }

    /**
     * 修改保存项目计划任务操作记录
     */
    @RequiresPermissions("project:projectOperation:edit")
    @Log(title = "项目计划任务操作记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ProjectOperation projectOperation)
    {
        return toAjax(projectOperationService.updateProjectOperation(projectOperation));
    }

    /**
     * 删除项目计划任务操作记录
     */
    @RequiresPermissions("project:projectOperation:remove")
    @Log(title = "项目计划任务操作记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(projectOperationService.deleteProjectOperationByIds(ids));
    }
}
