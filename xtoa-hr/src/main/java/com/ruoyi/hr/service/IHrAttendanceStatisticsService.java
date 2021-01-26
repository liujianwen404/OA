package com.ruoyi.hr.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTO;
import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTOError;
import com.ruoyi.base.domain.HrAttendanceStatistics;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 考勤统计Service接口
 * 
 * @author liujianwen
 * @date 2020-08-11
 */
public interface IHrAttendanceStatisticsService 
{
    /**
     * 查询考勤统计
     * 
     * @param id 考勤统计ID
     * @return 考勤统计
     */
    public HrAttendanceStatistics selectHrAttendanceStatisticsById(Long id);

    /**
     * 查询考勤统计列表
     * 
     * @param hrAttendanceStatistics 考勤统计
     * @return 考勤统计集合
     */
    public List<HrAttendanceStatistics> selectHrAttendanceStatisticsList(HrAttendanceStatistics hrAttendanceStatistics);

    /**
     * 新增考勤统计
     * 
     * @param hrAttendanceStatistics 考勤统计
     * @return 结果
     */
    public int insertHrAttendanceStatistics(HrAttendanceStatistics hrAttendanceStatistics);

    /**
     * 修改考勤统计
     * 
     * @param hrAttendanceStatistics 考勤统计
     * @return 结果
     */
    public int updateHrAttendanceStatistics(HrAttendanceStatistics hrAttendanceStatistics);

    /**
     * 批量删除考勤统计
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceStatisticsByIds(String ids);

    /**
     * 删除考勤统计信息
     * 
     * @param id 考勤统计ID
     * @return 结果
     */
    public int deleteHrAttendanceStatisticsById(Long id);

    /**
     * 考勤假期、扣款、补贴等数据
     * @return void
     */
    void insertAttendanceStatistics(AttendanceStatisticsDTO attendanceStatisticsDTO, AtomicInteger successCount, List<AttendanceStatisticsDTOError> errorList);

    HrAttendanceStatistics selectSingleOneByCondition(Long empId, Integer year, Integer month);

    void restart(HrAttendanceStatistics hrAttendanceStatistics);

    AjaxResult init();
}
