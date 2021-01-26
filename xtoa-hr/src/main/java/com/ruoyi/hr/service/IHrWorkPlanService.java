package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.common.exception.job.TaskException;
import org.quartz.SchedulerException;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.base.domain.HrWorkPlan;

/**
 * 工作计划Service接口
 * 
 * @author liujianwen
 * @date 2020-08-20
 */
public interface IHrWorkPlanService 
{
    /**
     * 查询工作计划
     * 
     * @param id 工作计划ID
     * @return 工作计划
     */
    public HrWorkPlan selectHrWorkPlanById(Long id);

    /**
     * 查询工作计划列表
     * 
     * @param hrWorkPlan 工作计划
     * @return 工作计划集合
     */
    public List<HrWorkPlan> selectHrWorkPlanList(HrWorkPlan hrWorkPlan);

    /**
     * 新增工作计划
     * 
     * @param hrWorkPlan 工作计划
     * @return 结果
     */
    public int insertHrWorkPlan(HrWorkPlan hrWorkPlan) throws SchedulerException, TaskException;

    /**
     * 修改工作计划
     * 
     * @param hrWorkPlan 工作计划
     * @return 结果
     */
    public int updateHrWorkPlan(HrWorkPlan hrWorkPlan) throws SchedulerException, TaskException;

    /**
     * 批量删除工作计划
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrWorkPlanByIds(String ids);

    /**
     * 删除工作计划信息
     * 
     * @param id 工作计划ID
     * @return 结果
     */
    public int deleteHrWorkPlanById(Long id);

    HrWorkPlan selectSingleOneByExample(Example example);

    List<HrWorkPlan> selectByExample(Example example);

}
