package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrAttendanceGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 考勤组 数据层
 *
 * @author liujianwen
 * @date 2020-07-29
 */
public interface HrAttendanceGroupMapper extends MyBaseMapper<HrAttendanceGroup> {

    /**
     * 查询考勤组列表
     *
     * @param hrAttendanceGroup 考勤组
     * @return 考勤组集合
     */
    public List<HrAttendanceGroup> selectHrAttendanceGroupList(HrAttendanceGroup hrAttendanceGroup);

    /**
     * 删除考勤组
     *
     * @param id 考勤组ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupById(Long id);

    /**
     * 批量删除考勤组
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupByIds(String[] ids);

    Map<String, Object> selectGroupAndClass(@Param("userId") Long userId, @Param("week") Integer week);

    List<String> selectAllEmpId(@Param("groupId")Long groupId);

    String selectEmpNameByEmpId(Long id);

    Double selectDayOfWorkHours(@Param("userId") Long userId, @Param("week") int week);

    Integer selectDayIsNeedNotDate(@Param("empId") Long empId, @Param("day") String day);

    String selectDayIsMustDate(@Param("empId") Long empId, @Param("day") String day);

    Integer selectDayIsClass(@Param("empId") Long empId, @Param("week") Integer week);

    Map<String, Object> selectGruopByEmpIdFromApi(Long userId);

    HrAttendanceGroup selectGroupByEmpId(@Param("empId") Long empId);

    int selectIsExistGroup(Long userId);

    Map<String, Object> selectScheduGroupAndClass(@Param("groupId")Long groupId, @Param("userId")Long userId
            , @Param("scheduDate")String scheduDate, @Param("dayNum")int dayNum);

    List<HrAttendanceGroup> selectGroupByClassId(@Param("classId") String classId);
}