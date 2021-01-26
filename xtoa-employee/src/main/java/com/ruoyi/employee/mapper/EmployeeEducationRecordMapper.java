package com.ruoyi.employee.mapper;

import java.util.List;
import com.ruoyi.employee.domain.EmployeeEducationRecord;

/**
 * 员工教育记录Mapper接口
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public interface EmployeeEducationRecordMapper 
{
    /**
     * 查询员工教育记录
     * 
     * @param id 员工教育记录ID
     * @return 员工教育记录
     */
    public EmployeeEducationRecord selectEmployeeEducationRecordById(Long id);

    /**
     * 查询员工教育记录列表
     * 
     * @param employeeEducationRecord 员工教育记录
     * @return 员工教育记录集合
     */
    public List<EmployeeEducationRecord> selectEmployeeEducationRecordList(EmployeeEducationRecord employeeEducationRecord);

    /**
     * 新增员工教育记录
     * 
     * @param employeeEducationRecord 员工教育记录
     * @return 结果
     */
    public int insertEmployeeEducationRecord(EmployeeEducationRecord employeeEducationRecord);

    /**
     * 修改员工教育记录
     * 
     * @param employeeEducationRecord 员工教育记录
     * @return 结果
     */
    public int updateEmployeeEducationRecord(EmployeeEducationRecord employeeEducationRecord);

    /**
     * 删除员工教育记录
     * 
     * @param id 员工教育记录ID
     * @return 结果
     */
    public int deleteEmployeeEducationRecordById(Long id);

    /**
     * 批量删除员工教育记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteEmployeeEducationRecordByIds(String[] ids);
}
