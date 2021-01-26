package com.ruoyi.hr.controller;

import java.util.Date;
import java.util.List;

import com.ruoyi.framework.util.ShiroUtils;
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
import com.ruoyi.common.utils.data.domain.TDate;
import com.ruoyi.common.utils.data.service.ITDateService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 日期Controller
 * 
 * @author liujianwen
 * @date 2020-05-19
 */
@Controller
@RequestMapping("/hr/date")
public class TDateController extends BaseController
{
    private String prefix = "hr/date";

    @Autowired
    private ITDateService tDateService;

    @RequiresPermissions("hr:date:view")
    @GetMapping()
    public String date()
    {
        return "hr/attendanceClass" + "/date";
    }

    /**
     * 查询日期列表
     */
//    @RequiresPermissions("hr:date:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TDate tDate)
    {
        startPage();
        List<TDate> list = tDateService.selectTDateList(tDate);
        return getDataTable(list);
    }

    /**
     * 导出日期列表
     */
    @RequiresPermissions("hr:date:export")
    @Log(title = "日期", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TDate tDate)
    {
        List<TDate> list = tDateService.selectTDateList(tDate);
        ExcelUtil<TDate> util = new ExcelUtil<TDate>(TDate.class);
        return util.exportExcel(list, "date");
    }

    /**
     * 新增日期
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存日期
     */
    @RequiresPermissions("hr:date:add")
    @Log(title = "日期", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TDate tDate)
    {
        return toAjax(tDateService.insertTDate(tDate));
    }

    /**
     * 修改日期
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TDate tDate = tDateService.selectTDateById(id);
        mmap.put("tDate", tDate);
        return prefix + "/edit";
    }

    /**
     * 修改保存日期
     */
    @RequiresPermissions("hr:date:edit")
    @Log(title = "日期", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TDate tDate)
    {
        return toAjax(tDateService.updateTDate(tDate));
    }
 /**
     * 修改保存日期
     */
    @RequiresPermissions("hr:date:edit")
    @Log(title = "日期改变状态", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(TDate tDate)
    {

//        if (tDate.getDay().before(new Date())){
//            return AjaxResult.error("不能修改过期的日期");
//        }

        tDate.setUpdateId(ShiroUtils.getUserId());
        tDate.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(tDateService.changeStatus(tDate));
    }

    /**
     * 删除日期
     */
    @RequiresPermissions("hr:date:remove")
    @Log(title = "日期", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tDateService.deleteTDateByIds(ids));
    }
}
