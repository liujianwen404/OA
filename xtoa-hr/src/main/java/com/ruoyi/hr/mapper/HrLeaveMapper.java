package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrLeave;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 请假业务Mapper接口
 *
 * @author Xianlu Tech
 * @date 2019-10-11
 */
public interface HrLeaveMapper extends MyBaseMapper<HrLeave> {
    /**
     * 查询请假业务
     *
     * @param id 请假业务ID
     * @return 请假业务
     */
    public HrLeave selectHrLeaveById(Long id);

    /**
     * 查询请假业务列表
     *
     * @param hrLeave 请假业务
     * @return 请假业务集合
     */
    public List<HrLeave> selectHrLeaveList(HrLeave hrLeave);

    @DataScope(deptAlias = "d", menuAlias = "hr:leave:leaveList")
    public List<HrLeave> selectHrLeaveViewList(HrLeave hrLeave);

    /**
     * 新增请假业务
     *
     * @param hrLeave 请假业务
     * @return 结果
     */
    public int insertHrLeave(HrLeave hrLeave);

    /**
     * 修改请假业务
     *
     * @param hrLeave 请假业务
     * @return 结果
     */
    public int updateHrLeave(HrLeave hrLeave);

    /**
     * 删除请假业务
     *
     * @param id 请假业务ID
     * @return 结果
     */
    public int deleteHrLeaveById(Long id);

    /**
     * 批量删除请假业务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrLeaveByIds(String[] ids);

    HrLeave selectHrLeaveByCondition(@Param("applyUser")String applyUser, @Param("applyTime")Date applyTime,
                                     @Param("startTime")Date startTime, @Param("endTime")Date endTime, @Param("type")String type);

    Double selectLeaveByType(@Param("empId")Long empId,@Param("year")int year,@Param("month")int month,@Param("type")String type);
}
