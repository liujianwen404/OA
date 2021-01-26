package com.ruoyi.employee.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.employee.mapper.EmployeeRecordMapper;
import com.ruoyi.employee.domain.EmployeeRecord;
import com.ruoyi.employee.service.IEmployeeRecordService;
import com.ruoyi.common.core.text.Convert;

/**
 * 员工记录Service业务层处理
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Service
public class EmployeeRecordServiceImpl implements IEmployeeRecordService 
{
    @Autowired
    private EmployeeRecordMapper employeeRecordMapper;

    /**
     * 查询员工记录
     * 
     * @param id 员工记录ID
     * @return 员工记录
     */
    @Override
    public EmployeeRecord selectEmployeeRecordById(Long id)
    {
        return employeeRecordMapper.selectEmployeeRecordById(id);
    }

    /**
     * 查询员工记录列表
     * 
     * @param employeeRecord 员工记录
     * @return 员工记录
     */
    @Override
    public List<EmployeeRecord> selectEmployeeRecordList(EmployeeRecord employeeRecord)
    {
        return employeeRecordMapper.selectEmployeeRecordList(employeeRecord);
    }

    /**
     * 新增员工记录
     * 
     * @param employeeRecord 员工记录
     * @return 结果
     */
    @Override
    public int insertEmployeeRecord(EmployeeRecord employeeRecord)
    {
        employeeRecord.setCreateTime(DateUtils.getNowDate());
        return employeeRecordMapper.insertEmployeeRecord(employeeRecord);
    }

    /**
     * 修改员工记录
     * 
     * @param employeeRecord 员工记录
     * @return 结果
     */
    @Override
    public int updateEmployeeRecord(EmployeeRecord employeeRecord)
    {
        employeeRecord.setUpdateTime(DateUtils.getNowDate());
        return employeeRecordMapper.updateEmployeeRecord(employeeRecord);
    }

    /**
     * 删除员工记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteEmployeeRecordByIds(String ids)
    {
        return employeeRecordMapper.deleteEmployeeRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除员工记录信息
     * 
     * @param id 员工记录ID
     * @return 结果
     */
    @Override
    public int deleteEmployeeRecordById(Long id)
    {
        return employeeRecordMapper.deleteEmployeeRecordById(id);
    }
}
