package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrAttendanceGroupSon;
import java.util.List;
/**
 * 考勤组排班子 数据层
 *
 * @author liujianwen
 * @date 2020-10-29
 */
public interface HrAttendanceGroupSonMapper extends MyBaseMapper<HrAttendanceGroupSon> {

    /**
     * 查询考勤组排班子列表
     *
     * @param hrAttendanceGroupSon 考勤组排班子
     * @return 考勤组排班子集合
     */
    public List<HrAttendanceGroupSon> selectHrAttendanceGroupSonList(HrAttendanceGroupSon hrAttendanceGroupSon);

    /**
     * 删除考勤组排班子
     *
     * @param id 考勤组排班子ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupSonById(Long id);

    /**
     * 批量删除考勤组排班子
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupSonByIds(String[] ids);

}