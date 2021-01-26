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
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.hr.service.IHrAttendanceClassService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 考勤班次Controller
 * 
 * @author liujianwen
 * @date 2020-07-27
 */
@Controller
@RequestMapping("/hr/attendanceClass")
public class HrAttendanceClassController extends BaseController
{
    private String prefix = "hr/attendanceClass";

    @Autowired
    private IHrAttendanceClassService hrAttendanceClassService;

    @RequiresPermissions("hr:attendanceClass:view")
    @GetMapping()
    public String attendanceClass()
    {
        return prefix + "/class";
    }

    /**
     * 查询考勤班次列表
     */
    @RequiresPermissions("hr:attendanceClass:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrAttendanceClass hrAttendanceClass)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrAttendanceClass.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrAttendanceClass> list = hrAttendanceClassService.selectHrAttendanceClassList(hrAttendanceClass);
        return getDataTable(list);
    }

    /**
     * 加载班次列表
     */
    @GetMapping("/selectClass")
    public String selectClass(String classId,String className,ModelMap mmap)
    {
        mmap.put("classId",classId);
        mmap.put("className",className);
        return prefix + "/selectClass";
    }

    /**
     * 加载班次列表，返回选择排班班次页面
     */
    @GetMapping("/selectScheduClass")
    public String selectScheduClass()
    {
        return prefix + "/selectScheduClass";
    }

    /**
     * 导出考勤班次列表
     */
    @RequiresPermissions("hr:attendanceClass:export")
    @Log(title = "考勤班次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrAttendanceClass hrAttendanceClass)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrAttendanceClass.setCreateId(ShiroUtils.getUserId());
        }
        List<HrAttendanceClass> list = hrAttendanceClassService.selectHrAttendanceClassList(hrAttendanceClass);
        ExcelUtil<HrAttendanceClass> util = new ExcelUtil<HrAttendanceClass>(HrAttendanceClass.class);
        return util.exportExcel(list, "class");
    }

    /**
     * 新增考勤班次
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存考勤班次
     */
    @RequiresPermissions("hr:attendanceClass:add")
    @Log(title = "考勤班次", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrAttendanceClass hrAttendanceClass)
    {
        return toAjax(hrAttendanceClassService.insertHrAttendanceClass(hrAttendanceClass));
    }

    /**
     * 修改考勤班次
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrAttendanceClass hrAttendanceClass = hrAttendanceClassService.selectHrAttendanceClassById(id);
        mmap.put("hrAttendanceClass", hrAttendanceClass);
        return prefix + "/edit";
    }

    /**
     * 修改保存考勤班次
     */
    @RequiresPermissions("hr:attendanceClass:edit")
    @Log(title = "考勤班次", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrAttendanceClass hrAttendanceClass)
    {
        return toAjax(hrAttendanceClassService.updateHrAttendanceClass(hrAttendanceClass));
    }

    /**
     * 删除考勤班次
     */
    @RequiresPermissions("hr:attendanceClass:remove")
    @Log(title = "考勤班次", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrAttendanceClassService.deleteHrAttendanceClassByIds(ids));
    }
}
