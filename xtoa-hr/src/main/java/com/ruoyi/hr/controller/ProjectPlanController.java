package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.DTO.ProjectPlanDTO;
import com.ruoyi.base.domain.VO.ProjectPlanVO;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.Project;
import com.ruoyi.hr.service.IProjectService;
import com.ruoyi.hr.utils.enums.ProjectEnum;
import com.ruoyi.system.domain.SysUser;
import java.util.List;

import com.ruoyi.system.domain.vo.UserTreeVo;
import com.ruoyi.system.service.ISysUserService;
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
import com.ruoyi.base.domain.ProjectPlan;
import com.ruoyi.hr.service.IProjectPlanService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目计划Controller
 * 
 * @author xt
 * @date 2020-06-30
 */
@Controller
@RequestMapping("/project/projectPlan")
public class ProjectPlanController extends BaseController
{
    private String prefix = "project/projectPlan";

    @Autowired
    private IProjectPlanService projectPlanService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IProjectService projectService;

    @RequiresPermissions("project:projectPlan:view")
    @GetMapping("/projectPlan")
    public String projectPlan(ModelMap modelMap)
    {
        modelMap.put("projectSelectList", projectService.getProjectSelectList());
        return prefix + "/projectPlan";
    }


    /**
     * 查询项目计划列表
     */
//    @RequiresPermissions("project:projectPlan:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ProjectPlan projectPlan)
    {
        /*if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            Project project = projectService.selectProjectById(projectPlan.getProjectId());
            if (project == null || !project.getEmpId().equals(ShiroUtils.getUserId())){
                projectPlan.setEmpId(ShiroUtils.getUserId());
            }
        }*/
        startPage();
        List<ProjectPlan> list = projectPlanService.selectProjectPlanList(projectPlan);
        return getDataTable(list);
    }



    @PostMapping("/getProjectPlanList")
    @ResponseBody
    public TableDataInfo getProjectPlanList(ProjectPlanDTO projectPlanDTO)
    {
        startPage();
        List<ProjectPlanVO> list = projectPlanService.getProjectPlanList(projectPlanDTO);
        return getDataTable(list);
    }

    /**
     * 导出项目计划列表
     */
//    @RequiresPermissions("project:projectPlan:export")
    @Log(title = "项目计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProjectPlan projectPlan)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            projectPlan.setCreateId(ShiroUtils.getUserId());
        }
        List<ProjectPlan> list = projectPlanService.selectProjectPlanList(projectPlan);
        ExcelUtil<ProjectPlan> util = new ExcelUtil<ProjectPlan>(ProjectPlan.class);
        return util.exportExcel(list, "projectPlan");
    }

    /**
     * 新增项目计划
     */
    @GetMapping("/add")
    public String add(Long projectId,ModelMap modelMap,Integer type)
    {
        modelMap.put("projectId",projectId);
        modelMap.put("projectSelectList", projectService.getProjectSelectList());
        return prefix + "/add";
    }

    /**
     * 新增保存项目计划
     */
//    @RequiresPermissions("project:projectPlan:add")
    @Log(title = "项目计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ProjectPlan projectPlan)
    {
        return toAjax(projectPlanService.insertProjectPlan(projectPlan));
    }

    /**
     * 修改项目计划
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ProjectPlan projectPlan = projectPlanService.selectProjectPlanById(id);
        mmap.put("projectPlan", projectPlan);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(projectPlan.getEmpId());

        mmap.put("empUser", new UserTreeVo());
        List<UserTreeVo> userTreeVos = sysUserService.getUserTreeVos(sysUser);
        for (UserTreeVo userTreeVo : userTreeVos) {
            if (userTreeVo.getId().equals(projectPlan.getEmpId())){
                mmap.put("empUser", userTreeVo);
            }
        }
        mmap.put("projectSelectList", projectService.getProjectSelectList());
//        mmap.put("projectId", projectPlan.getProjectId());

        return prefix + "/edit";
    }

    /**
     * 修改保存项目计划
     */
//    @RequiresPermissions("project:projectPlan:edit")
    @Log(title = "项目计划", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ProjectPlan projectPlan)
    {
        return toAjax(projectPlanService.updateProjectPlan(projectPlan));
    }

    /**
     * 关闭项目计划
     */
//    @RequiresPermissions("project:projectPlan:remove")
    @Log(title = "项目计划", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try {
            return projectPlanService.closeProjectPlanByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    @GetMapping("/projectPlanInfo")
    public String projectPlanInfo(Long projectId,ModelMap modelMap,Integer type)
    {
        modelMap.put("projectId",projectId);
        Project project = projectService.selectProjectById(projectId);
        modelMap.put("addFlag",project.getEmpId().equals(ShiroUtils.getUserId())
                && project.getStatus().equals(ProjectEnum.ProjectStatus.creation.getValue()));
        modelMap.put("userId",ShiroUtils.getUserId());
        modelMap.put("type",type);
        return prefix + "/projectPlanInfo";
    }

    @RequestMapping("/finishPlan")
    @ResponseBody
    public AjaxResult finishPlan(Long id)
    {
        try {
            return projectPlanService.finishPlan(id);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

}
