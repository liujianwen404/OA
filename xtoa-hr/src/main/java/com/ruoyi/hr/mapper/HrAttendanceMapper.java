package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrAttendance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 考勤统计 数据层
 *
 * @author liujianwen
 * @date 2020-06-09
 */
public interface HrAttendanceMapper extends MyBaseMapper<HrAttendance> {

    /**
     * 查询考勤统计列表
     *
     * @param hrAttendance 考勤统计
     * @return 考勤统计集合
     */
    public List<HrAttendance> selectHrAttendanceList(HrAttendance hrAttendance);

    /**
     * 删除考勤统计
     *
     * @param id 考勤统计ID
     * @return 结果
     */
    public int deleteHrAttendanceById(Long id);

    /**
     * 批量删除考勤统计
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceByIds(String[] ids);

    List<Map<String,Object>> selectLeaveDetail(@Param("empId") Long empId, @Param("year") Long year, @Param("month") Long month);

    List<Map<String,Object>> selectOverTimeDetail(@Param("empId") Long empId, @Param("year") Long year, @Param("month") Long month);

    HrAttendance selectOldAttendanceByCondition(@Param("empNum")String empNum, @Param("year") Long year, @Param("month") Long month);

    Map<String, Object> selectExtraLeave(@Param("empNum")String empNum, @Param("year") Long year, @Param("month") Long month);
}