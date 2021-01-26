package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrFillClock;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
/**
 * 补卡申请 数据层
 *
 * @author liujianwen
 * @date 2020-06-24
 */
public interface HrFillClockMapper extends MyBaseMapper<HrFillClock> {

    /**
     * 查询补卡申请列表
     *
     * @param hrFillClock 补卡申请
     * @return 补卡申请集合
     */
    public List<HrFillClock> selectHrFillClockList(HrFillClock hrFillClock);

    /**
     * 删除补卡申请
     *
     * @param id 补卡申请ID
     * @return 结果
     */
    public int deleteHrFillClockById(Long id);

    /**
     * 批量删除补卡申请
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrFillClockByIds(String[] ids);

    HrFillClock selectFillClockByCondition(@Param("applyUser")String applyUser,@Param("dates")Date dates , @Param("classes")String classes);

    Integer selectHrFillClockCount(@Param("userId") Long userId, @Param("classDate") Date classDate);

    @DataScope(deptAlias = "d", menuAlias = "hr:clockManage:view")
    List<HrFillClock> selectHrFillClockListManage(HrFillClock hrFillClock);

    int selectHrFillClockNum(@Param("empId")Long empId, @Param("classDate")Date classDate);

    Integer selectHrFillClockCompleteCount(@Param("empId")Long empId, @Param("classDate")Date classDate);
}