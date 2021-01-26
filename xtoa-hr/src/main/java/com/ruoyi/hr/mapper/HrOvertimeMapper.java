package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrOvertime;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
/**
 * 加班申请 数据层
 *
 * @author liujianwen
 * @date 2020-06-11
 */
public interface HrOvertimeMapper extends MyBaseMapper<HrOvertime> {

    /**
     * 查询加班申请列表
     *
     * @param hrOvertime 加班申请
     * @return 加班申请集合
     */
    public List<HrOvertime> selectHrOvertimeList(HrOvertime hrOvertime);

    @DataScope(deptAlias = "d", menuAlias = "hr:overtime:overtimeList")
    public List<HrOvertime> selectOvertimeManageList(HrOvertime hrOvertime);

    /**
     * 删除加班申请
     *
     * @param id 加班申请ID
     * @return 结果
     */
    public int deleteHrOvertimeById(Long id);

    /**
     * 批量删除加班申请
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrOvertimeByIds(String[] ids);

    HrOvertime selectHrOvertimeByCondition(@Param("applyUser")String applyUser, @Param("applyTime")Date applyTime,
                                           @Param("startTime")Date startTime, @Param("endTime")Date endTime);

    Double selectOvertimeByType(@Param("empId")Long empId,@Param("year")int year,@Param("month")int month,@Param("type")String type);

}