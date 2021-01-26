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
import com.ruoyi.base.domain.HrEmpExperiences;
import com.ruoyi.hr.service.IHrEmpExperiencesService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工工作经历Controller
 * 
 * @author liujianwen
 * @date 2021-01-11
 */
@Controller
@RequestMapping("/hr/empExperiences")
public class HrEmpExperiencesController extends BaseController
{
    private String prefix = "hr/empExperiences";

    @Autowired
    private IHrEmpExperiencesService hrEmpExperiencesService;

    @RequiresPermissions("hr:empExperiences:view")
    @GetMapping()
    public String empExperiences()
    {
        return prefix + "/empExperiences";
    }

    /**
     * 查询员工工作经历列表
     */
    @RequiresPermissions("hr:empExperiences:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrEmpExperiences hrEmpExperiences)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrEmpExperiences.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrEmpExperiences> list = hrEmpExperiencesService.selectHrEmpExperiencesList(hrEmpExperiences);
        return getDataTable(list);
    }

    /**
     * 导出员工工作经历列表
     */
    @RequiresPermissions("hr:empExperiences:export")
    @Log(title = "员工工作经历", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrEmpExperiences hrEmpExperiences)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrEmpExperiences.setCreateId(ShiroUtils.getUserId());
        }
        List<HrEmpExperiences> list = hrEmpExperiencesService.selectHrEmpExperiencesList(hrEmpExperiences);
        ExcelUtil<HrEmpExperiences> util = new ExcelUtil<HrEmpExperiences>(HrEmpExperiences.class);
        return util.exportExcel(list, "empExperiences");
    }

    /**
     * 新增员工工作经历
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存员工工作经历
     */
    @RequiresPermissions("hr:empExperiences:add")
    @Log(title = "员工工作经历", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrEmpExperiences hrEmpExperiences)
    {
        return toAjax(hrEmpExperiencesService.insertHrEmpExperiences(hrEmpExperiences));
    }

    /**
     * 修改员工工作经历
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrEmpExperiences hrEmpExperiences = hrEmpExperiencesService.selectHrEmpExperiencesById(id);
        mmap.put("hrEmpExperiences", hrEmpExperiences);
        return prefix + "/edit";
    }

    /**
     * 修改保存员工工作经历
     */
    @RequiresPermissions("hr:empExperiences:edit")
    @Log(title = "员工工作经历", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrEmpExperiences hrEmpExperiences)
    {
        return toAjax(hrEmpExperiencesService.updateHrEmpExperiences(hrEmpExperiences));
    }

    /**
     * 删除员工工作经历
     */
    @RequiresPermissions("hr:empExperiences:remove")
    @Log(title = "员工工作经历", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrEmpExperiencesService.deleteHrEmpExperiencesByIds(ids));
    }

    /**
     * 员工修改页面，删除员工工作经历信息
     */
    @Log(title = "员工工作经历", businessType = BusinessType.DELETE)
    @PostMapping( "/removeOld")
    @ResponseBody
    public AjaxResult removeOld(Long id)
    {
        return toAjax(hrEmpExperiencesService.deleteHrEmpExperiencesById(id));
    }
}
