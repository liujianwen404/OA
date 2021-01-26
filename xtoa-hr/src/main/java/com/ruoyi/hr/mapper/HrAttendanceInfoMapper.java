package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrAttendanceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 个人考勤记录 数据层
 *
 * @author liujianwen
 * @date 2020-07-13
 */
public interface HrAttendanceInfoMapper extends MyBaseMapper<HrAttendanceInfo> {

    /**
     * 查询个人考勤记录列表
     *
     * @param hrAttendanceInfo 个人考勤记录
     * @return 个人考勤记录集合
     */
    public List<HrAttendanceInfo> selectHrAttendanceInfoList(HrAttendanceInfo hrAttendanceInfo);

    public List<HrAttendanceInfo> selectHrAttendanceInfoAllList(HrAttendanceInfo hrAttendanceInfo);

    /**
     * 删除个人考勤记录
     *
     * @param id 个人考勤记录ID
     * @return 结果
     */
    public int deleteHrAttendanceInfoById(Long id);

    /**
     * 批量删除个人考勤记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceInfoByIds(String[] ids);

    List<Map<String,Object>> getClockIn(@Param("userId")Long userId, @Param("date")String date);

    List<Map<String,Object>> selectActualAttendanceDay(@Param("empId") Long empId, @Param("year")int year,@Param("month") int month);

    Integer selectLateOrEarly(@Param("empId")Long empId, @Param("year")int year, @Param("month")int month);

    List<String> selectAbsentListByDay(@Param("empId")Long empId, @Param("day")String day);

    int delClockIn(@Param("userId")Long userId, @Param("today")String today);
    List<HrAttendanceInfo> selectDelClockIn(@Param("userId")Long userId, @Param("today")String today);
}