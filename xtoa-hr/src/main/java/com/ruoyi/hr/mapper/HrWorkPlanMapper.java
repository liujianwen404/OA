package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrWorkPlan;
import java.util.List;
/**
 * 工作计划 数据层
 *
 * @author liujianwen
 * @date 2020-08-20
 */
public interface HrWorkPlanMapper extends MyBaseMapper<HrWorkPlan> {

    /**
     * 查询工作计划列表
     *
     * @param hrWorkPlan 工作计划
     * @return 工作计划集合
     */
    public List<HrWorkPlan> selectHrWorkPlanList(HrWorkPlan hrWorkPlan);

    /**
     * 删除工作计划
     *
     * @param id 工作计划ID
     * @return 结果
     */
    public int deleteHrWorkPlanById(Long id);

    /**
     * 批量删除工作计划
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrWorkPlanByIds(String[] ids);

}