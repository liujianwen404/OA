package com.ruoyi.hr.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.base.domain.DTO.DateOperation;
import com.ruoyi.base.domain.HrAttendanceClass;

/**
 * 考勤班次Service接口
 * 
 * @author liujianwen
 * @date 2020-07-27
 */
public interface IHrAttendanceClassService 
{
    /**
     * 查询考勤班次
     * 
     * @param id 考勤班次ID
     * @return 考勤班次
     */
    public HrAttendanceClass selectHrAttendanceClassById(Long id);

    /**
     * 查询考勤班次列表
     * 
     * @param hrAttendanceClass 考勤班次
     * @return 考勤班次集合
     */
    public List<HrAttendanceClass> selectHrAttendanceClassList(HrAttendanceClass hrAttendanceClass);

    /**
     * 新增考勤班次
     * 
     * @param hrAttendanceClass 考勤班次
     * @return 结果
     */
    public int insertHrAttendanceClass(HrAttendanceClass hrAttendanceClass);

    /**
     * 修改考勤班次
     * 
     * @param hrAttendanceClass 考勤班次
     * @return 结果
     */
    public int updateHrAttendanceClass(HrAttendanceClass hrAttendanceClass);

    /**
     * 批量删除考勤班次
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceClassByIds(String ids);

    /**
     * 删除考勤班次信息
     * 
     * @param id 考勤班次ID
     * @return 结果
     */
    public int deleteHrAttendanceClassById(Long id);

    List<HrAttendanceClass> selectHrAttendanceClassAll();

    /**
     * 获取员工某天的班次
     * @param empId 员工Id
     * @param startTime 日期
     * @return
     */
    HrAttendanceClass getAttendanceClass(Long empId, Date startTime);

    /**
     * 通过日期获取班次的各个时间
     * @param offsetDay
     * @param attendanceClass
     * @param empId
     * @param shiftCriticalPoint
     */
    DateOperation getDateOperation(Date offsetDay, HrAttendanceClass attendanceClass, Long empId, Integer shiftCriticalPoint);
}
