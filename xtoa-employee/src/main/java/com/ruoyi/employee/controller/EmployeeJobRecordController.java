package com.ruoyi.employee.controller;

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
import com.ruoyi.employee.domain.EmployeeJobRecord;
import com.ruoyi.employee.service.IEmployeeJobRecordService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工工作记录Controller
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Controller
@RequestMapping("/employee/job/record")
public class EmployeeJobRecordController extends BaseController
{
    private String prefix = "employee/record";

    @Autowired
    private IEmployeeJobRecordService employeeJobRecordService;

    @RequiresPermissions("employee:record:view")
    @GetMapping()
    public String record()
    {
        return prefix + "/record";
    }

    /**
     * 查询员工工作记录列表
     */
    @RequiresPermissions("employee:record:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EmployeeJobRecord employeeJobRecord)
    {
        startPage();
        List<EmployeeJobRecord> list = employeeJobRecordService.selectEmployeeJobRecordList(employeeJobRecord);
        return getDataTable(list);
    }

    /**
     * 导出员工工作记录列表
     */
    @RequiresPermissions("employee:record:export")
    @Log(title = "员工工作记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EmployeeJobRecord employeeJobRecord)
    {
        List<EmployeeJobRecord> list = employeeJobRecordService.selectEmployeeJobRecordList(employeeJobRecord);
        ExcelUtil<EmployeeJobRecord> util = new ExcelUtil<EmployeeJobRecord>(EmployeeJobRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 新增员工工作记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存员工工作记录
     */
    @RequiresPermissions("employee:record:add")
    @Log(title = "员工工作记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(EmployeeJobRecord employeeJobRecord)
    {
        return toAjax(employeeJobRecordService.insertEmployeeJobRecord(employeeJobRecord));
    }

    /**
     * 修改员工工作记录
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        EmployeeJobRecord employeeJobRecord = employeeJobRecordService.selectEmployeeJobRecordById(id);
        mmap.put("employeeJobRecord", employeeJobRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存员工工作记录
     */
    @RequiresPermissions("employee:record:edit")
    @Log(title = "员工工作记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EmployeeJobRecord employeeJobRecord)
    {
        return toAjax(employeeJobRecordService.updateEmployeeJobRecord(employeeJobRecord));
    }

    /**
     * 删除员工工作记录
     */
    @RequiresPermissions("employee:record:remove")
    @Log(title = "员工工作记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(employeeJobRecordService.deleteEmployeeJobRecordByIds(ids));
    }
}
