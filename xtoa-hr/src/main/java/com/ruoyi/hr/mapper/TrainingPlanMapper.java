package com.ruoyi.hr.mapper;

import java.util.List;
import com.ruoyi.base.domain.TrainingPlan;

/**
 * 培训计划Mapper接口
 * 
 * @author liujianwen
 * @date 2020-05-08
 */
public interface TrainingPlanMapper 
{
    /**
     * 查询培训计划
     * 
     * @param id 培训计划ID
     * @return 培训计划
     */
    public TrainingPlan selectTrainingPlanById(Long id);

    /**
     * 查询培训计划列表
     * 
     * @param trainingPlan 培训计划
     * @return 培训计划集合
     */
    public List<TrainingPlan> selectTrainingPlanList(TrainingPlan trainingPlan);

    /**
     * 新增培训计划
     * 
     * @param trainingPlan 培训计划
     * @return 结果
     */
    public int insertTrainingPlan(TrainingPlan trainingPlan);

    /**
     * 修改培训计划
     * 
     * @param trainingPlan 培训计划
     * @return 结果
     */
    public int updateTrainingPlan(TrainingPlan trainingPlan);

    /**
     * 删除培训计划
     * 
     * @param id 培训计划ID
     * @return 结果
     */
    public int deleteTrainingPlanById(Long id);

    /**
     * 批量删除培训计划
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTrainingPlanByIds(String[] ids);
}
