package com.ruoyi.hr.controller;

import java.util.List;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrRecruit;
import com.ruoyi.base.domain.VO.HrRecruitVO;
import com.ruoyi.system.domain.SysUser;
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
import com.ruoyi.hr.service.HrRecruitService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 招聘申请Controller
 * 
 * @author cmw
 * @date 2020-05-11
 */
@Controller
@RequestMapping("/hr/recruit")
public class HrRecruitController extends BaseController
{
    private String prefix = "hr/recruit";

    @Autowired
    private HrRecruitService tHrRecruitService;

    @RequiresPermissions("hr:recruit:view")
    @GetMapping()
    public String recruit()
    {
        return prefix + "/recruit";
    }

    /**
     * 查询招聘申请列表
     */
    @RequiresPermissions("hr:recruit:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrRecruit hrRecruit)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrRecruit.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrRecruit> list = tHrRecruitService.selectTHrRecruitList(hrRecruit);
        return getDataTable(list);
    }

    /**
     * 导出招聘申请列表
     */
    @RequiresPermissions("hr:recruit:export")
    @Log(title = "招聘申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrRecruit hrRecruit)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrRecruit.setCreateId(ShiroUtils.getUserId());
        }
        List<HrRecruit> list = tHrRecruitService.selectTHrRecruitList(hrRecruit);
        ExcelUtil<HrRecruit> util = new ExcelUtil<HrRecruit>(HrRecruit.class);
        return util.exportExcel(list, "recruit");
    }

    /**
     * 新增招聘申请
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存招聘申请
     */
    @RequiresPermissions("hr:recruit:add")
    @Log(title = "招聘申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrRecruit hrRecruit)
    {
        return toAjax(tHrRecruitService.insertTHrRecruit(hrRecruit));
    }

    /**
     * 修改招聘申请
     */
    @GetMapping("/edit/{recruitId}")
    public String edit(@PathVariable("recruitId") Long recruitId, ModelMap mmap)
    {
        HrRecruit hrRecruit = tHrRecruitService.selectTHrRecruitById(recruitId);
        mmap.put("hrRecruit", hrRecruit);
        return prefix + "/edit";
    }

    /**
     * 详情招聘申请
     */
    @GetMapping("/detail/{recruitId}")
    public String detail(@PathVariable("recruitId") Long recruitId, ModelMap mmap)
    {
        HrRecruit hrRecruit = tHrRecruitService.selectTHrRecruitById(recruitId);
        mmap.put("hrRecruit", hrRecruit);
        return prefix + "/detail";
    }


    /**
     * 修改保存招聘申请
     */
    @RequiresPermissions("hr:recruit:edit")
    @Log(title = "招聘申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrRecruit hrRecruit)
    {
        return toAjax(tHrRecruitService.updateTHrRecruit(hrRecruit));
    }


    /**
     * 修改保存招聘申请
     */
    @RequiresPermissions("hr:recruit:edit")
    @Log(title = "提交招聘申请", businessType = BusinessType.UPDATE)
    @PostMapping("/submitRecruit")
    @ResponseBody
    public AjaxResult submitRecruit(Long recruitId)
    {
        return tHrRecruitService.submitRecruit(recruitId);
    }

    /**
     * 删除招聘申请
     */
    @RequiresPermissions("hr:recruit:remove")
    @Log(title = "招聘申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tHrRecruitService.deleteTHrRecruitByIds(ids));
    }

    /**
     * 查询招聘看板列表
     */
    @RequiresPermissions("hr:recruit:findAllInfo")
    @GetMapping("/findAllInfo")
    public String findAllInfo( ModelMap mmap)
    {
        List<HrRecruitVO> list = tHrRecruitService.findAllInfo();
        Integer count = tHrRecruitService.findAllInfoCount();
        mmap.put("hrRecruitVO", list);
        mmap.put("hrRecruitVOCount", count == null ? 0 :count);
        return prefix + "/recruitVO";
    }


}
