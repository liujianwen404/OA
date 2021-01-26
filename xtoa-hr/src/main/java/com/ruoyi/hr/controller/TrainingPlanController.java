package com.ruoyi.hr.controller;

import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.base.domain.TrainingPlan;
import com.ruoyi.hr.service.ITrainingPlanService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 培训计划Controller
 * 
 * @author liujianwen
 * @date 2020-05-08
 */
@Controller
@RequestMapping("/hr/training/plan")
public class TrainingPlanController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(TrainingPlanController.class);

    private String prefix = "hr/training/plan";

    @Autowired
    private ITrainingPlanService trainingPlanService;

    @RequiresPermissions("hr:training:plan:view")
    @GetMapping()
    public String plan()
    {
        return prefix + "/plan";
    }

    /**
     * 查询培训计划列表
     */
    @RequiresPermissions("hr:training:plan:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TrainingPlan trainingPlan)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            trainingPlan.setCreateBy(ShiroUtils.getLoginName());
        }
        startPage();
        List<TrainingPlan> list = trainingPlanService.selectTrainingPlanList(trainingPlan);
        return getDataTable(list);
    }

    /**
     * 导出培训计划列表
     */
    @RequiresPermissions("hr:training:plan:export")
    @Log(title = "培训计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TrainingPlan trainingPlan)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            trainingPlan.setCreateBy(ShiroUtils.getLoginName());
        }
        List<TrainingPlan> list = trainingPlanService.selectTrainingPlanList(trainingPlan);
        ExcelUtil<TrainingPlan> util = new ExcelUtil<TrainingPlan>(TrainingPlan.class);
        return util.exportExcel(list, "plan");
    }

    /**
     * 新增培训计划
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存培训计划
     */
    @RequiresPermissions("hr:training:plan:add")
    @Log(title = "培训计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HttpServletRequest request, TrainingPlan trainingPlan, @RequestParam(value = "image" , required = false) MultipartFile image)
    {
        try
        {
            trainingPlan.setCreateBy(ShiroUtils.getLoginName());
            trainingPlan.setCreateId(ShiroUtils.getUserId());
            if ( image != null && !image.isEmpty())
            {
                String img = FileUploadUtils.upload(Global.getUploadPath(), image);
                trainingPlan.setImg(img);
                return toAjax(trainingPlanService.insertTrainingPlan(trainingPlan));
            }
            return toAjax(trainingPlanService.insertTrainingPlan(trainingPlan));
        }
        catch (Exception e)
        {
            log.error("上传培训计划图片失败！", e);
            return error(e.getMessage());
        }
    }

    /**
     * 修改培训计划
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TrainingPlan trainingPlan = trainingPlanService.selectTrainingPlanById(id);
        mmap.put("trainingPlan", trainingPlan);
        return prefix + "/edit";
    }

    /**
     * 修改保存培训计划
     */
    @RequiresPermissions("hr:training:plan:edit")
    @Log(title = "培训计划", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TrainingPlan trainingPlan, @RequestParam(value = "image" , required = false)MultipartFile image)
    {
        try
        {
            trainingPlan.setUpdateBy(ShiroUtils.getLoginName());
            trainingPlan.setUpdateId(ShiroUtils.getUserId());
            if (!image.isEmpty())
            {
                String img = FileUploadUtils.upload(Global.getUploadPath(), image);
                trainingPlan.setImg(img);
                return toAjax(trainingPlanService.updateTrainingPlan(trainingPlan));
            }
            return toAjax(trainingPlanService.updateTrainingPlan(trainingPlan));
        }
        catch (Exception e)
        {
            log.error("上传培训计划图片失败！", e);
            return error(e.getMessage());
        }

    }

    /**
     * 新增保存培训计划图片
     */
    @RequiresPermissions("hr:training:plan:edit")
    @Log(title = "培训计划", businessType = BusinessType.UPDATE)
    @PostMapping("/uploadImg")
    @ResponseBody
    public AjaxResult uploadImg(@RequestParam("image") MultipartFile file)
    {
        try
        {
            if (!file.isEmpty())
            {
                String img = FileUploadUtils.upload(Global.getUploadPath(), file);
                return success(img);
            }
            return error();
        }
        catch (Exception e)
        {
            log.error("上传培训计划图片失败！", e);
            return error(e.getMessage());
        }
    }

    /**
     * 修改保存培训计划图片
     */
    @RequiresPermissions("hr:training:plan:edit")
    @Log(title = "培训计划", businessType = BusinessType.UPDATE)
    @PostMapping("/uploadImg/{id}")
    @ResponseBody
    public synchronized AjaxResult uploadImg(@PathVariable("id") Long id,@RequestParam("image") MultipartFile file)
    {
        try
        {
            if(!file.isEmpty()){
                TrainingPlan trainingPlan = trainingPlanService.selectTrainingPlanById(id);
                String oldImg = trainingPlan.getImg();
                StringBuilder sb = new StringBuilder(oldImg);
                String img = FileUploadUtils.upload(Global.getUploadPath(), file);
                String newImg = sb.append(";").append(img).toString();
                trainingPlan.setImg(newImg);
                trainingPlanService.updateTrainingPlan(trainingPlan);
                return success(newImg);
            }
            return error();
        }
        catch (Exception e)
        {
            log.error("上传培训计划图片失败！", e);
            return error(e.getMessage());
        }
    }

    /**
     * 删除培训计划
     */
    @RequiresPermissions("hr:training:plan:remove")
    @Log(title = "培训计划", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(trainingPlanService.deleteTrainingPlanByIds(ids));
    }
}
