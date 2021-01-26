package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.VO.ProjectVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import java.util.ArrayList;
import java.util.List;
import com.ruoyi.system.domain.vo.UserTreeVo;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.base.domain.Project;
import com.ruoyi.hr.service.IProjectService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目Controller
 * 
 * @author xt
 * @date 2020-06-30
 */
@Controller
@RequestMapping("/project/project")
public class ProjectController extends BaseController
{
    private String prefix = "project/project";

    @Autowired
    private IProjectService projectService;

    @Autowired
    private ISysUserService sysUserService;

    @RequiresPermissions("project:project:view")
    @GetMapping("/projectIndex")
    public String projectIndex()
    {
        return prefix + "/projectIndex";
    }


//    @RequiresPermissions("project:project:view")
    @GetMapping()
    public String project()
    {
        return prefix + "/project";
    }

    /**
     * 查询项目列表
     */
//    @RequiresPermissions("project:project:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Project project)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            project.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<Project> list = projectService.selectProjectList(project);
        return getDataTable(list);
    }

    /**
     * 导出项目列表
     */
//    @RequiresPermissions("project:project:export")
    @Log(title = "项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Project project)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            project.setCreateId(ShiroUtils.getUserId());
        }
        List<Project> list = projectService.selectProjectList(project);
        ExcelUtil<Project> util = new ExcelUtil<Project>(Project.class);
        return util.exportExcel(list, "project");
    }

    /**
     * 新增项目
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存项目
     */
//    @RequiresPermissions("project:project:add")
    @Log(title = "项目", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Project project)
    {
        if(StringUtils.isBlank(project.getName())){
            return AjaxResult.error("项目名称不能为空");
        }

        if(project.getName().length()> 20){
            return AjaxResult.error("项目名称字数限制在20字以内");
        }
        if(StringUtils.isNotBlank(project.getContentDescribe())) {
            if (project.getContentDescribe().length() > 100) {
                return AjaxResult.error("描述字数限制在100字以内");
            }
        }
        return toAjax(projectService.insertProject(project));
    }

    /**
     * 修改项目
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Project project = projectService.selectProjectById(id);
        mmap.put("project", project);
        SysUser sysUser = new SysUser();
        sysUser.setUserId(project.getEmpId());

        mmap.put("empUser", new UserTreeVo());
        List<UserTreeVo> userTreeVos = sysUserService.getUserTreeVos(sysUser);
        for (UserTreeVo userTreeVo : userTreeVos) {
            if (userTreeVo.getId().equals(project.getEmpId())){
                mmap.put("empUser", userTreeVo);
            }
        }

        return prefix + "/edit";
    }

    /**
     * 修改保存项目
     */
//    @RequiresPermissions("project:project:edit")
    @Log(title = "项目", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Project project)
    {
        return toAjax(projectService.updateProject(project));
    }

    /**
     * 删除项目
     */
//    @RequiresPermissions("project:project:remove")
    @Log(title = "项目", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(projectService.deleteProjectByIds(ids));
    }


    @PostMapping( "/getMyCount")
    @ResponseBody
    public AjaxResult getMyCount()
    {
        try {
            return projectService.getMyCount();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    @PostMapping("/projectTableInfo")
    @ResponseBody
    public TableDataInfo projectTableInfo(@RequestParam("statusDatas") Integer statusDatas,@RequestParam("empName") String empName)
    {
        try {
            startPage();
            List<ProjectVO> list=projectService.projectTableInfo(statusDatas,empName);
            return getDataTable(list);
        } catch (Exception e) {
            e.printStackTrace();
            return getDataTable(new ArrayList<>());
        }
    }

    @RequestMapping("/finishProject")
    @ResponseBody
    public AjaxResult finishProject(Long id)
    {
        try {
            return projectService.finishProject(id);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
    @GetMapping("/projectInfo")
    public String projectInfo()
    {
        return prefix + "/projectInfo";
    }

}
