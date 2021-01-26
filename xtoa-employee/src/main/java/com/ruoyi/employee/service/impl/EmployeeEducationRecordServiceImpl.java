package com.ruoyi.employee.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.employee.mapper.EmployeeEducationRecordMapper;
import com.ruoyi.employee.domain.EmployeeEducationRecord;
import com.ruoyi.employee.service.IEmployeeEducationRecordService;
import com.ruoyi.common.core.text.Convert;

/**
 * 员工教育记录Service业务层处理
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Service
public class EmployeeEducationRecordServiceImpl implements IEmployeeEducationRecordService 
{
    @Autowired
    private EmployeeEducationRecordMapper employeeEducationRecordMapper;

    /**
     * 查询员工教育记录
     * 
     * @param id 员工教育记录ID
     * @return 员工教育记录
     */
    @Override
    public EmployeeEducationRecord selectEmployeeEducationRecordById(Long id)
    {
        return employeeEducationRecordMapper.selectEmployeeEducationRecordById(id);
    }

    /**
     * 查询员工教育记录列表
     * 
     * @param employeeEducationRecord 员工教育记录
     * @return 员工教育记录
     */
    @Override
    public List<EmployeeEducationRecord> selectEmployeeEducationRecordList(EmployeeEducationRecord employeeEducationRecord)
    {
        return employeeEducationRecordMapper.selectEmployeeEducationRecordList(employeeEducationRecord);
    }

    /**
     * 新增员工教育记录
     * 
     * @param employeeEducationRecord 员工教育记录
     * @return 结果
     */
    @Override
    public int insertEmployeeEducationRecord(EmployeeEducationRecord employeeEducationRecord)
    {
        employeeEducationRecord.setCreateTime(DateUtils.getNowDate());
        return employeeEducationRecordMapper.insertEmployeeEducationRecord(employeeEducationRecord);
    }

    /**
     * 修改员工教育记录
     * 
     * @param employeeEducationRecord 员工教育记录
     * @return 结果
     */
    @Override
    public int updateEmployeeEducationRecord(EmployeeEducationRecord employeeEducationRecord)
    {
        employeeEducationRecord.setUpdateTime(DateUtils.getNowDate());
        return employeeEducationRecordMapper.updateEmployeeEducationRecord(employeeEducationRecord);
    }

    /**
     * 删除员工教育记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteEmployeeEducationRecordByIds(String ids)
    {
        return employeeEducationRecordMapper.deleteEmployeeEducationRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除员工教育记录信息
     * 
     * @param id 员工教育记录ID
     * @return 结果
     */
    @Override
    public int deleteEmployeeEducationRecordById(Long id)
    {
        return employeeEducationRecordMapper.deleteEmployeeEducationRecordById(id);
    }
}
