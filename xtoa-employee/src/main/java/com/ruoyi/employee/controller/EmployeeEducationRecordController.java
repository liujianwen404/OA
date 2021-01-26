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
import com.ruoyi.employee.domain.EmployeeEducationRecord;
import com.ruoyi.employee.service.IEmployeeEducationRecordService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工教育记录Controller
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Controller
@RequestMapping("/employee/education/record")
public class EmployeeEducationRecordController extends BaseController
{
    private String prefix = "employee/record";

    @Autowired
    private IEmployeeEducationRecordService employeeEducationRecordService;

    @RequiresPermissions("employee:record:view")
    @GetMapping()
    public String record()
    {
        return prefix + "/record";
    }

    /**
     * 查询员工教育记录列表
     */
    @RequiresPermissions("employee:record:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(EmployeeEducationRecord employeeEducationRecord)
    {
        startPage();
        List<EmployeeEducationRecord> list = employeeEducationRecordService.selectEmployeeEducationRecordList(employeeEducationRecord);
        return getDataTable(list);
    }

    /**
     * 导出员工教育记录列表
     */
    @RequiresPermissions("employee:record:export")
    @Log(title = "员工教育记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EmployeeEducationRecord employeeEducationRecord)
    {
        List<EmployeeEducationRecord> list = employeeEducationRecordService.selectEmployeeEducationRecordList(employeeEducationRecord);
        ExcelUtil<EmployeeEducationRecord> util = new ExcelUtil<EmployeeEducationRecord>(EmployeeEducationRecord.class);
        return util.exportExcel(list, "record");
    }

    /**
     * 新增员工教育记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存员工教育记录
     */
    @RequiresPermissions("employee:record:add")
    @Log(title = "员工教育记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(EmployeeEducationRecord employeeEducationRecord)
    {
        return toAjax(employeeEducationRecordService.insertEmployeeEducationRecord(employeeEducationRecord));
    }

    /**
     * 修改员工教育记录
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        EmployeeEducationRecord employeeEducationRecord = employeeEducationRecordService.selectEmployeeEducationRecordById(id);
        mmap.put("employeeEducationRecord", employeeEducationRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存员工教育记录
     */
    @RequiresPermissions("employee:record:edit")
    @Log(title = "员工教育记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EmployeeEducationRecord employeeEducationRecord)
    {
        return toAjax(employeeEducationRecordService.updateEmployeeEducationRecord(employeeEducationRecord));
    }

    /**
     * 删除员工教育记录
     */
    @RequiresPermissions("employee:record:remove")
    @Log(title = "员工教育记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(employeeEducationRecordService.deleteEmployeeEducationRecordByIds(ids));
    }
}
