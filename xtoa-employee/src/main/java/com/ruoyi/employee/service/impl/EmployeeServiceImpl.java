package com.ruoyi.employee.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.employee.domain.Employee;
import com.ruoyi.employee.domain.EmployeeRecord;
import com.ruoyi.employee.mapper.EmployeeMapper;
import com.ruoyi.employee.mapper.EmployeeRecordMapper;
import com.ruoyi.employee.service.IEmployeeService;

/**
 * 员工Service业务层处理
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService 
{
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EmployeeRecordMapper employeeRecordMapper;

    /**
     * 查询员工
     * 
     * @param id 员工ID
     * @return 员工
     */
    @Override
    public Employee selectEmployeeById(Long id)
    {
        return employeeMapper.selectEmployeeById(id);
    }

    /**
     * 查询员工列表
     * 
     * @param employee 员工
     * @return 员工
     */
    @Override
    public List<Employee> selectEmployeeList(Employee employee)
    {
        return employeeMapper.selectEmployeeList(employee);
    }

    /**
     * 新增员工
     * 
     * @param employee 员工
     * @return 结果
     */
    @Override
    public int insertEmployee(Employee employee)
    {
        employee.setCreateTime(DateUtils.getNowDate());
        int r =  employeeMapper.insertEmployee(employee);
        
        //添加操作记录
        EmployeeRecord employeeRecord = new EmployeeRecord();
        employeeRecord.setEmployeeId(employee.getId());
        employeeRecord.setEmployeeName(employee.getName());
        employeeRecord.setRecoreTime(new Date());
        employeeRecord.setStatus(employee.getStatus());
        employeeRecord.setCreateBy(employee.getCreateBy());
        employeeRecord.setCreateTime(new Date());
        employeeRecord.setUpdateBy(employee.getUpdateBy());
        employeeRecord.setUpdateTime(new Date());
        employeeRecordMapper.insertEmployeeRecord(employeeRecord);
        
        return r;
    }

    /**
     * 修改员工
     * 
     * @param employee 员工
     * @return 结果
     */
    @Override
    public int updateEmployee(Employee employee)
    {
        employee.setUpdateTime(DateUtils.getNowDate());
        int r =  employeeMapper.updateEmployee(employee);
        
        //添加操作记录
        EmployeeRecord employeeRecord = new EmployeeRecord();
        employeeRecord.setEmployeeId(employee.getId());
        employeeRecord.setEmployeeName(employee.getName());
        employeeRecord.setRecoreTime(new Date());
        employeeRecord.setStatus(employee.getStatus());
        employeeRecord.setUpdateBy(employee.getUpdateBy());
        employeeRecord.setUpdateTime(new Date());
        employeeRecordMapper.insertEmployeeRecord(employeeRecord);
        
        return r;
    }

    /**
     * 删除员工对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteEmployeeByIds(String ids)
    {
        return employeeMapper.deleteEmployeeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除员工信息
     * 
     * @param id 员工ID
     * @return 结果
     */
    @Override
    public int deleteEmployeeById(Long id)
    {
        return employeeMapper.deleteEmployeeById(id);
    }

	@Override
	public int invalidEmployee(Employee employee) {
		return employeeMapper.invalidEmployee(employee);
	}
}
