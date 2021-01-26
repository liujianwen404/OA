package com.ruoyi.hr.service;

import java.util.List;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.base.domain.HrAttendanceGroupSon;

/**
 * 考勤组排班子Service接口
 * 
 * @author liujianwen
 * @date 2020-10-29
 */
public interface IHrAttendanceGroupSonService 
{
    /**
     * 查询考勤组排班子
     * 
     * @param id 考勤组排班子ID
     * @return 考勤组排班子
     */
    public HrAttendanceGroupSon selectHrAttendanceGroupSonById(Long id);

    /**
     * 查询考勤组排班子列表
     * 
     * @param hrAttendanceGroupSon 考勤组排班子
     * @return 考勤组排班子集合
     */
    public List<HrAttendanceGroupSon> selectHrAttendanceGroupSonList(HrAttendanceGroupSon hrAttendanceGroupSon);

    /**
     * 新增考勤组排班子
     * 
     * @param hrAttendanceGroupSon 考勤组排班子
     * @return 结果
     */
    public int insertHrAttendanceGroupSon(HrAttendanceGroupSon hrAttendanceGroupSon);

    /**
     * 修改考勤组排班子
     * 
     * @param hrAttendanceGroupSon 考勤组排班子
     * @return 结果
     */
    public int updateHrAttendanceGroupSon(HrAttendanceGroupSon hrAttendanceGroupSon);

    public int updateGroupSonOne(HrAttendanceGroupSon hrAttendanceGroupSon);

    /**
     * 批量删除考勤组排班子
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupSonByIds(String ids);

    /**
     * 删除考勤组排班子信息
     * 
     * @param id 考勤组排班子ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupSonById(Long id);

    HrAttendanceGroupSon selectSingleOneByExample(Example example);

    List<HrAttendanceGroupSon> selectByExample(Example example);

    int insertList(List<HrAttendanceGroupSon> returnList);

    int importHrAttendanceGroupSon(HrAttendanceGroupSon son);
}
