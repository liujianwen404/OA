package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrAttendanceClass;
import java.util.List;
/**
 * 考勤班次 数据层
 *
 * @author liujianwen
 * @date 2020-07-27
 */
public interface HrAttendanceClassMapper extends MyBaseMapper<HrAttendanceClass> {

    /**
     * 查询考勤班次列表
     *
     * @param hrAttendanceClass 考勤班次
     * @return 考勤班次集合
     */
    public List<HrAttendanceClass> selectHrAttendanceClassList(HrAttendanceClass hrAttendanceClass);

    /**
     * 删除考勤班次
     *
     * @param id 考勤班次ID
     * @return 结果
     */
    public int deleteHrAttendanceClassById(Long id);

    /**
     * 批量删除考勤班次
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceClassByIds(String[] ids);

    List<HrAttendanceClass> selectHrAttendanceClassAll();
}