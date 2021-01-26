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
import com.ruoyi.employee.domain.EmployeeRecord;
import com.ruoyi.employee.service.IEmployeeRecordService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工记录Controller
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Controller
@RequestMapping("/employee/record")
public class EmployeeRecordController extends BaseController
{
    private String prefix = "employee/record";

    @Autowired
    private IEmployeeRecordService employeeRecordService;

    @RequiresPermissions("employee:record:view")
    @GetMapping()
    public String record()
    {
        return prefix + "/record";
    }

    /**
     * 查询员工记录列表
     */
    @RequiresPermissions("employee:record:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EmployeeRecord employeeRecord)
    {
        startPage();
        List<EmployeeRecord> list = employeeRecordService.selectEmployeeRecordList(employeeRecord);
        return getDataTable(list);
    }

    /**
     * 导出员工记录列表
     */
    @RequiresPermissions("employee:record:export")
    @Log(title = "员工记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EmployeeRecord employeeRecord)
    {
        List<EmployeeRecord> list = employeeRecordService.selectEmployeeRecordList(employeeRecord);
        ExcelUtil<EmployeeRecord> util = new ExcelUtil<EmployeeRecord>(EmployeeRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 新增员工记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存员工记录
     */
    @RequiresPermissions("employee:record:add")
    @Log(title = "员工记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(EmployeeRecord employeeRecord)
    {
        return toAjax(employeeRecordService.insertEmployeeRecord(employeeRecord));
    }

    /**
     * 修改员工记录
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        EmployeeRecord employeeRecord = employeeRecordService.selectEmployeeRecordById(id);
        mmap.put("employeeRecord", employeeRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存员工记录
     */
    @RequiresPermissions("employee:record:edit")
    @Log(title = "员工记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EmployeeRecord employeeRecord)
    {
        return toAjax(employeeRecordService.updateEmployeeRecord(employeeRecord));
    }

    /**
     * 删除员工记录
     */
    @RequiresPermissions("employee:record:remove")
    @Log(title = "员工记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(employeeRecordService.deleteEmployeeRecordByIds(ids));
    }
}
