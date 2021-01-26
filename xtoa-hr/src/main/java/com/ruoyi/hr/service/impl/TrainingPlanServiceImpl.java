package com.ruoyi.hr.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hr.mapper.TrainingPlanMapper;
import com.ruoyi.base.domain.TrainingPlan;
import com.ruoyi.hr.service.ITrainingPlanService;
import com.ruoyi.common.core.text.Convert;

/**
 * 培训计划Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-05-08
 */
@Service
public class TrainingPlanServiceImpl implements ITrainingPlanService 
{
    @Autowired
    private TrainingPlanMapper trainingPlanMapper;

    /**
     * 查询培训计划
     * 
     * @param id 培训计划ID
     * @return 培训计划
     */
    @Override
    public TrainingPlan selectTrainingPlanById(Long id)
    {
        return trainingPlanMapper.selectTrainingPlanById(id);
    }

    /**
     * 查询培训计划列表
     * 
     * @param trainingPlan 培训计划
     * @return 培训计划
     */
    @Override
    public List<TrainingPlan> selectTrainingPlanList(TrainingPlan trainingPlan)
    {
        return trainingPlanMapper.selectTrainingPlanList(trainingPlan);
    }

    /**
     * 新增培训计划
     * 
     * @param trainingPlan 培训计划
     * @return 结果
     */
    @Override
    public int insertTrainingPlan(TrainingPlan trainingPlan)
    {
        trainingPlan.setCreateTime(DateUtils.getNowDate());
        return trainingPlanMapper.insertTrainingPlan(trainingPlan);
    }

    /**
     * 修改培训计划
     * 
     * @param trainingPlan 培训计划
     * @return 结果
     */
    @Override
    public int updateTrainingPlan(TrainingPlan trainingPlan)
    {
        trainingPlan.setUpdateTime(DateUtils.getNowDate());
        return trainingPlanMapper.updateTrainingPlan(trainingPlan);
    }

    /**
     * 删除培训计划对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTrainingPlanByIds(String ids)
    {
        return trainingPlanMapper.deleteTrainingPlanByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除培训计划信息
     * 
     * @param id 培训计划ID
     * @return 结果
     */
    @Override
    public int deleteTrainingPlanById(Long id)
    {
        return trainingPlanMapper.deleteTrainingPlanById(id);
    }
}
