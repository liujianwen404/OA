package com.ruoyi.employee.mapper;

import java.util.List;
import com.ruoyi.employee.domain.EmployeeRecord;

/**
 * 员工记录Mapper接口
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public interface EmployeeRecordMapper 
{
    /**
     * 查询员工记录
     * 
     * @param id 员工记录ID
     * @return 员工记录
     */
    public EmployeeRecord selectEmployeeRecordById(Long id);

    /**
     * 查询员工记录列表
     * 
     * @param employeeRecord 员工记录
     * @return 员工记录集合
     */
    public List<EmployeeRecord> selectEmployeeRecordList(EmployeeRecord employeeRecord);

    /**
     * 新增员工记录
     * 
     * @param employeeRecord 员工记录
     * @return 结果
     */
    public int insertEmployeeRecord(EmployeeRecord employeeRecord);

    /**
     * 修改员工记录
     * 
     * @param employeeRecord 员工记录
     * @return 结果
     */
    public int updateEmployeeRecord(EmployeeRecord employeeRecord);

    /**
     * 删除员工记录
     * 
     * @param id 员工记录ID
     * @return 结果
     */
    public int deleteEmployeeRecordById(Long id);

    /**
     * 批量删除员工记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteEmployeeRecordByIds(String[] ids);
}
