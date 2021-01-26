package com.ruoyi.hr.service;

import java.text.ParseException;
import java.util.List;

import com.ruoyi.base.domain.DTO.AttendanceDetailDTO;
import com.ruoyi.base.domain.DTO.AttendanceExtraDTO;
import com.ruoyi.base.domain.DTO.AttendanceMonthDTO;
import com.ruoyi.base.domain.DTO.HrAttendanceDTO;
import com.ruoyi.base.domain.HrAttendance;

/**
 * 考勤统计Service接口
 * 
 * @author liujianwen
 * @date 2020-06-09
 */
public interface IHrAttendanceService 
{
    /**
     * 查询考勤统计
     * 
     * @param id 考勤统计ID
     * @return 考勤统计
     */
    public HrAttendance selectHrAttendanceById(Long id);

    /**
     * 查询考勤统计列表
     * 
     * @param hrAttendance 考勤统计
     * @return 考勤统计集合
     */
    public List<HrAttendance> selectHrAttendanceList(HrAttendance hrAttendance);

    /**
     * 新增考勤统计
     * 
     * @param hrAttendance 考勤统计
     * @return 结果
     */
    public int insertHrAttendance(HrAttendance hrAttendance);

    /**
     * 每日考勤数据
     *
     * @param hrAttendanceDTO 每日考勤统计
     * @return 结果
     */
    public int insertHrAttendanceDTO(HrAttendanceDTO hrAttendanceDTO) throws ParseException;

    /**
     * 月度汇总数据
     * @param attendanceDetailDTO 月度汇总数据统计
     * @param head 月度汇总数据表头
     * @return void
     */
    void insertAttendanceDetail(AttendanceDetailDTO attendanceDetailDTO,String head) throws ParseException;

    /**
     * 考勤假期、工作制、夜班补贴等数据
     * @return void
     */
    void insertAttendanceExtra(AttendanceExtraDTO attendanceExtraDTO,String head) throws ParseException;

    /**
     * 修改考勤统计
     * 
     * @param hrAttendance 考勤统计
     * @return 结果
     */
    public int updateHrAttendance(HrAttendance hrAttendance);

    /**
     * 批量删除考勤统计
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceByIds(String ids);

    /**
     * 删除考勤统计信息
     * 
     * @param id 考勤统计ID
     * @return 结果
     */
    public int deleteHrAttendanceById(Long id);

    int selectHrAttendanceCount(HrAttendance attendance);

}
