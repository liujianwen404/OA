package com.ruoyi.employee.mapper;

import java.util.List;
import com.ruoyi.employee.domain.EmployeeJobRecord;

/**
 * 员工工作记录Mapper接口
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public interface EmployeeJobRecordMapper 
{
    /**
     * 查询员工工作记录
     * 
     * @param id 员工工作记录ID
     * @return 员工工作记录
     */
    public EmployeeJobRecord selectEmployeeJobRecordById(Long id);

    /**
     * 查询员工工作记录列表
     * 
     * @param employeeJobRecord 员工工作记录
     * @return 员工工作记录集合
     */
    public List<EmployeeJobRecord> selectEmployeeJobRecordList(EmployeeJobRecord employeeJobRecord);

    /**
     * 新增员工工作记录
     * 
     * @param employeeJobRecord 员工工作记录
     * @return 结果
     */
    public int insertEmployeeJobRecord(EmployeeJobRecord employeeJobRecord);

    /**
     * 修改员工工作记录
     * 
     * @param employeeJobRecord 员工工作记录
     * @return 结果
     */
    public int updateEmployeeJobRecord(EmployeeJobRecord employeeJobRecord);

    /**
     * 删除员工工作记录
     * 
     * @param id 员工工作记录ID
     * @return 结果
     */
    public int deleteEmployeeJobRecordById(Long id);

    /**
     * 批量删除员工工作记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteEmployeeJobRecordByIds(String[] ids);
}
