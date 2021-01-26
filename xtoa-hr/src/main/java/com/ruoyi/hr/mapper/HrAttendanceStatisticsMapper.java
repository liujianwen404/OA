package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrAttendanceStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 考勤统计 数据层
 *
 * @author liujianwen
 * @date 2020-08-11
 */
public interface HrAttendanceStatisticsMapper extends MyBaseMapper<HrAttendanceStatistics> {

    /**
     * 查询考勤统计列表
     *
     * @param hrAttendanceStatistics 考勤统计
     * @return 考勤统计集合
     */
    public List<HrAttendanceStatistics> selectHrAttendanceStatisticsList(HrAttendanceStatistics hrAttendanceStatistics);

    /**
     * 删除考勤统计
     *
     * @param id 考勤统计ID
     * @return 结果
     */
    public int deleteHrAttendanceStatisticsById(Long id);

    /**
     * 批量删除考勤统计
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceStatisticsByIds(String[] ids);

    HrAttendanceStatistics selectSingleOneByCondition(@Param("empId") Long empId, @Param("year") Integer year, @Param("month") Integer month);

    Boolean insertOrUpdateList(List<HrAttendanceStatistics> statisticsList);
}