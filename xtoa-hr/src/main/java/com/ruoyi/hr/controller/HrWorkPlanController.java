package com.ruoyi.hr.controller;

import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;

import java.util.Arrays;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
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
import com.ruoyi.base.domain.HrWorkPlan;
import com.ruoyi.hr.service.IHrWorkPlanService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 工作计划Controller
 * 
 * @author liujianwen
 * @date 2020-08-20
 */
@Controller
@RequestMapping("/hr/workPlan")
public class HrWorkPlanController extends BaseController
{
    private String prefix = "hr/workPlan";

    @Autowired
    private IHrWorkPlanService hrWorkPlanService;

    @Autowired
    private ProcessHandleService processHandleService;

    @RequiresPermissions("hr:workPlan:view")
    @GetMapping()
    public String plan()
    {
        return prefix + "/plan";
    }

    /**
     * 查询工作计划列表
     */
    @RequiresPermissions("hr:workPlan:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrWorkPlan hrWorkPlan)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrWorkPlan.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrWorkPlan> list = hrWorkPlanService.selectHrWorkPlanList(hrWorkPlan);
        return getDataTable(list);
    }

    /**
     * 导出工作计划列表
     */
    @RequiresPermissions("hr:workPlan:export")
    @Log(title = "工作计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrWorkPlan hrWorkPlan)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrWorkPlan.setCreateId(ShiroUtils.getUserId());
        }
        List<HrWorkPlan> list = hrWorkPlanService.selectHrWorkPlanList(hrWorkPlan);
        ExcelUtil<HrWorkPlan> util = new ExcelUtil<HrWorkPlan>(HrWorkPlan.class);
        return util.exportExcel(list, "plan");
    }

    /**
     * 新增工作计划
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存工作计划
     */
    @RequiresPermissions("hr:workPlan:add")
    @Log(title = "工作计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrWorkPlan hrWorkPlan) throws TaskException, SchedulerException {

        return toAjax(hrWorkPlanService.insertHrWorkPlan(hrWorkPlan));
    }

    /**
     * 修改工作计划
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrWorkPlan hrWorkPlan = hrWorkPlanService.selectHrWorkPlanById(id);
        if (StringUtils.isNotBlank(hrWorkPlan.getAttachment())){
            //从分隔符第一次出现的位置向后截取
            String fileUrls = StringUtils.substringAfter(hrWorkPlan.getAttachment(), ",");
            hrWorkPlan.setFileList(Arrays.asList(fileUrls.split(",")));
        }
        mmap.put("hrWorkPlan", hrWorkPlan);
        return prefix + "/edit";
    }

    /**
     * 修改保存工作计划
     */
    @RequiresPermissions("hr:workPlan:edit")
    @Log(title = "工作计划", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrWorkPlan hrWorkPlan) throws SchedulerException, TaskException
    {
        return toAjax(hrWorkPlanService.updateHrWorkPlan(hrWorkPlan));
    }

    /**
     * 删除工作计划
     */
    @RequiresPermissions("hr:workPlan:remove")
    @Log(title = "工作计划", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrWorkPlanService.deleteHrWorkPlanByIds(ids));
    }
}
