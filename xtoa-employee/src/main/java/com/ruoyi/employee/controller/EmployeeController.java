package com.ruoyi.employee.controller;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.employee.domain.Employee;
import com.ruoyi.employee.service.IEmployeeService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysRoleService;


/**
 * 员工Controller
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Controller
@RequestMapping("/employee/employee")
public class EmployeeController extends BaseController
{
	private final static Logger log = LoggerFactory.getLogger(EmployeeController.class);
	
    private String prefix = "employee/employee";

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @RequiresPermissions("employee:employee:view")
    @GetMapping()
    public String employee(ModelMap mmap)
    {
        mmap.put("posts", postService.selectPostAll());
        return prefix + "/employee";
    }

    /**
     * 查询员工列表
     */
    @RequiresPermissions("employee:employee:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Employee employee)
    {
        startPage();
        if(employee.getDelFlag()==null) {
        	employee.setDelFlag("0");
        }
        List<Employee> list = employeeService.selectEmployeeList(employee);
        return getDataTable(list);
    }

    /**
     * 导出员工列表
     */
    @RequiresPermissions("employee:employee:export")
    @Log(title = "员工", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Employee employee)
    {
        if(StringUtils.isBlank(employee.getDelFlag())) {
        	employee.setDelFlag("0");
        }
        List<Employee> list = employeeService.selectEmployeeList(employee);
        ExcelUtil<Employee> util = new ExcelUtil<Employee>(Employee.class);
        return util.exportExcel(list, "employee");
    }

    /**
     * 新增员工
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("posts", postService.selectPostAll());
        return prefix + "/add";
    }

    /**
     * 新增保存员工
     */
    @RequiresPermissions("employee:employee:add")
    @Log(title = "员工", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Employee employee)
    {
    	try {
    		employee.setCreateBy(ShiroUtils.getLoginName());
    		employee.setCreateTime(new Date());
    		employee.setUpdateBy(ShiroUtils.getLoginName());
    		employee.setUpdateTime(new Date());
            return toAjax(employeeService.insertEmployee(employee));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if(e instanceof DuplicateKeyException) {
				if(e.getMessage().indexOf("'phone'")>0) { 
					return AjaxResult.error("手机号码已存在");
				}else if(e.getMessage().indexOf("'id_card'")>0) { 
					return AjaxResult.error("身份证号码已存在");
				}
			}
			return AjaxResult.error();
		}
    }

    /**
     * 修改员工
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("posts", postService.selectPostAll());
        Employee employee = employeeService.selectEmployeeById(id);
        mmap.put("employee", employee);
        return prefix + "/edit";
    }

    /**
     * 修改保存员工
     */
    @RequiresPermissions("employee:employee:edit")
    @Log(title = "员工", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Employee employee)
    {
		employee.setUpdateBy(ShiroUtils.getLoginName());
		employee.setUpdateTime(new Date());
        return toAjax(employeeService.updateEmployee(employee));
    }

    /**
     * 删除员工
     */
    @RequiresPermissions("employee:employee:remove")
    @Log(title = "员工", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
    	int count = 0;
    	for(String id : ids.split(",")) {
    		Employee employee = employeeService.selectEmployeeById(Long.valueOf(id));
    		employee.setUpdateBy(ShiroUtils.getLoginName());
    		employee.setUpdateTime(new Date());
    		count += employeeService.invalidEmployee(employee);
    	}
        return toAjax(count);
    }
}
