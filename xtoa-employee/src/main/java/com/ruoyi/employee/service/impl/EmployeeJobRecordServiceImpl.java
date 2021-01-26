package com.ruoyi.employee.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.employee.mapper.EmployeeJobRecordMapper;
import com.ruoyi.employee.domain.EmployeeJobRecord;
import com.ruoyi.employee.service.IEmployeeJobRecordService;
import com.ruoyi.common.core.text.Convert;

/**
 * 员工工作记录Service业务层处理
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Service
public class EmployeeJobRecordServiceImpl implements IEmployeeJobRecordService 
{
    @Autowired
    private EmployeeJobRecordMapper employeeJobRecordMapper;

    /**
     * 查询员工工作记录
     * 
     * @param id 员工工作记录ID
     * @return 员工工作记录
     */
    @Override
    public EmployeeJobRecord selectEmployeeJobRecordById(Long id)
    {
        return employeeJobRecordMapper.selectEmployeeJobRecordById(id);
    }

    /**
     * 查询员工工作记录列表
     * 
     * @param employeeJobRecord 员工工作记录
     * @return 员工工作记录
     */
    @Override
    public List<EmployeeJobRecord> selectEmployeeJobRecordList(EmployeeJobRecord employeeJobRecord)
    {
        return employeeJobRecordMapper.selectEmployeeJobRecordList(employeeJobRecord);
    }

    /**
     * 新增员工工作记录
     * 
     * @param employeeJobRecord 员工工作记录
     * @return 结果
     */
    @Override
    public int insertEmployeeJobRecord(EmployeeJobRecord employeeJobRecord)
    {
        employeeJobRecord.setCreateTime(DateUtils.getNowDate());
        return employeeJobRecordMapper.insertEmployeeJobRecord(employeeJobRecord);
    }

    /**
     * 修改员工工作记录
     * 
     * @param employeeJobRecord 员工工作记录
     * @return 结果
     */
    @Override
    public int updateEmployeeJobRecord(EmployeeJobRecord employeeJobRecord)
    {
        employeeJobRecord.setUpdateTime(DateUtils.getNowDate());
        return employeeJobRecordMapper.updateEmployeeJobRecord(employeeJobRecord);
    }

    /**
     * 删除员工工作记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteEmployeeJobRecordByIds(String ids)
    {
        return employeeJobRecordMapper.deleteEmployeeJobRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除员工工作记录信息
     * 
     * @param id 员工工作记录ID
     * @return 结果
     */
    @Override
    public int deleteEmployeeJobRecordById(Long id)
    {
        return employeeJobRecordMapper.deleteEmployeeJobRecordById(id);
    }
}
