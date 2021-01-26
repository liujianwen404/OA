package com.ruoyi.hr.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.Project;
import com.ruoyi.base.domain.ProjectPlan;
import com.ruoyi.hr.service.IProjectPlanService;
import com.ruoyi.hr.service.IProjectService;
import com.ruoyi.hr.utils.enums.ProjectEnum;
import com.ruoyi.system.domain.SysUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

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
import com.ruoyi.base.domain.ProjectEmp;
import com.ruoyi.hr.service.IProjectEmpService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.annotation.Resource;

/**
 * 项目计划任务成员Controller
 * 
 * @author xt
 * @date 2020-06-30
 */
@Controller
@RequestMapping("/project/projectEmp")
public class ProjectEmpController extends BaseController
{
    private String prefix = "project/projectEmp";

    @Autowired
    private IProjectEmpService projectEmpService;

    @Autowired
    private IProjectService projectService;
    @Autowired
    private IProjectPlanService projectPlanService;

    @Resource(name = "ThreadPoolExecutorForCallBack")
    private ThreadPoolExecutor threadPoolExecutor;
//    @RequiresPermissions("project:projectEmp:view")
    @GetMapping()
    public String projectEmp()
    {
        return prefix + "/projectEmp";
    }

    /**
     * 查询项目计划任务成员列表
     */
//    @RequiresPermissions("project:projectEmp:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ProjectEmp projectEmp)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            projectEmp.setEmpId(ShiroUtils.getUserId());
        }
        startPage();
        List<ProjectEmp> list = projectEmpService.selectProjectEmpList(projectEmp);
        return getDataTable(list);
    }

    /**
     * 导出项目计划任务成员列表
     */
//    @RequiresPermissions("project:projectEmp:export")
    @Log(title = "项目计划任务成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProjectEmp projectEmp)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            projectEmp.setCreateId(ShiroUtils.getUserId());
        }
        List<ProjectEmp> list = projectEmpService.selectProjectEmpList(projectEmp);
        ExcelUtil<ProjectEmp> util = new ExcelUtil<ProjectEmp>(ProjectEmp.class);
        return util.exportExcel(list, "projectEmp");
    }

    /**
     * 新增项目计划任务成员
     */
    @GetMapping("/add")
    public String add(Long projectId,Long projectPlanId,ModelMap modelMap,Integer type)
    {
        modelMap.put("projectId",projectId);
        modelMap.put("projectPlanId",projectPlanId);
        modelMap.put("type",type);
        return prefix + "/add";
    }

    /**
     * 新增保存项目计划任务成员
     */
//    @RequiresPermissions("project:projectEmp:add")
    @Log(title = "项目计划任务成员", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ProjectEmp projectEmp,String empIds)
    {
        try {
            String[] split = empIds.split(",");
            Set<String> collect = Arrays.stream(split).filter(s -> StringUtils.isNotEmpty(s) && !s.equals("null")).collect(Collectors.toSet());

            AjaxResult ajaxResult = projectEmpService.insertProjectEmps(projectEmp, collect);
            if (Objects.equals(ajaxResult.get(AjaxResult.CODE_TAG),0)){

                Project project = projectService.selectProjectById(projectEmp.getProjectId());
                projectEmpService.synProjectChat(project.getChatId());

            }

            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    /**
     * 修改项目计划任务成员
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ProjectEmp projectEmp = projectEmpService.selectProjectEmpById(id);
        mmap.put("projectEmp", projectEmp);
        return prefix + "/edit";
    }

    /**
     * 修改保存项目计划任务成员
     */
//    @RequiresPermissions("project:projectEmp:edit")
    @Log(title = "项目计划任务成员", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ProjectEmp projectEmp)
    {
        return toAjax(projectEmpService.updateProjectEmp(projectEmp));
    }

    /**
     * 删除项目计划任务成员
     */
//    @RequiresPermissions("project:projectEmp:remove")
    @Log(title = "项目计划任务成员", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(projectEmpService.deleteProjectEmpByIds(ids));
    }

    @GetMapping("/projectEmpInfo")
    public String projectEmpInfo(Long projectId,Long projectPlanId,ModelMap modelMap,Integer type)
    {
        modelMap.put("projectId",projectId);
        modelMap.put("projectPlanId",projectPlanId);
        Project project = projectService.selectProjectById(projectId);
        ProjectPlan projectPlan = projectPlanService.selectProjectPlanById(projectPlanId);
        modelMap.put("addFlag",project.getEmpId().equals(ShiroUtils.getUserId())
                && project.getStatus().equals(ProjectEnum.ProjectStatus.creation.getValue())
                && (projectPlan == null || projectPlan.getStatus().equals(ProjectEnum.ProjectStatus.creation.getValue())));
        modelMap.put("userId",ShiroUtils.getUserId());
        modelMap.put("type",type);
        return prefix + "/projectEmpInfo";
    }

    @PostMapping("/projectEmpTableInfo")
    @ResponseBody
    public TableDataInfo projectEmpTableInfo(ProjectEmp projectEmp)
    {
        List<ProjectEmp> list = projectEmpService.selectProjectEmpList(projectEmp);
        return getDataTable(list);
    }
}
