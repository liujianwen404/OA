package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.AttendanceRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 考勤记录 数据层
 *
 * @author liujianwen
 * @date 2020-06-17
 */
public interface AttendanceRecordMapper extends MyBaseMapper<AttendanceRecord> {

    /**
     * 查询考勤记录列表
     *
     * @param attendanceRecord 考勤记录
     * @return 考勤记录集合
     */
    public List<AttendanceRecord> selectAttendanceRecordList(AttendanceRecord attendanceRecord);

    /**
     * 删除考勤记录
     *
     * @param id 考勤记录ID
     * @return 结果
     */
    public int deleteAttendanceRecordById(Long id);

    /**
     * 批量删除考勤记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAttendanceRecordByIds(String[] ids);

    Map<String,Object> selectShouldAttendance(Long empId);

    List<AttendanceRecord> selectLateList(AttendanceRecord attendanceRecord);

    int selectLateCount(AttendanceRecord attendanceRecord);

    Long selectGeneralLateCount(String empNum);

    Long selectGeneralEarlyCount(String empNum);

    Map<String,Object> selectFinesAndSubsidy(@Param("empNum") String empNum,@Param("year") Long year,@Param("month") Long month);

    Long selectMonth(String empNum);

    AttendanceRecord selectRecordByCondition(@Param("empNum")String empNum, @Param("date")Date date);

    /**
     * 获取未打卡次数
     * */
    Long selectNotClockInNums(@Param("empNum")String empNum, @Param("year") Long year, @Param("month")Long month);

    /**
     * 获取补卡次数
     * */
    Long selectFillClockInNums(@Param("empNum")String empNum, @Param("year") Long year, @Param("month")Long month);

    /**
     * 获取仓库考勤班次
     * */
    String selectRepositoryClasses(@Param("empNum")String empNum, @Param("year") Long year, @Param("month")Long month);

    /**
     * 获取不包含调休、年假的请假次数
     * */
    Long selectLeaveNums(@Param("empNum")String empNum, @Param("year") Long year, @Param("month")Long month);
}