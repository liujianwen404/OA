package com.ruoyi.hr.mapper;

import com.ruoyi.base.domain.VO.ProjectPlanTaskVO;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.ProjectPlanTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * 项目计划任务 数据层
 *
 * @author xt
 * @date 2020-06-30
 */
public interface ProjectPlanTaskMapper extends MyBaseMapper<ProjectPlanTask> {

    /**
     * 查询项目计划任务列表
     *
     * @param projectPlanTask 项目计划任务
     * @return 项目计划任务集合
     */
    public List<ProjectPlanTaskVO> selectProjectPlanTaskList(ProjectPlanTask projectPlanTask);

    /**
     * 删除项目计划任务
     *
     * @param id 项目计划任务ID
     * @return 结果
     */
    public int deleteProjectPlanTaskById(Long id);

    /**
     * 批量删除项目计划任务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectPlanTaskByIds(String[] ids);

    List<Long> selectProjectPlanTaskEmps(@Param("projectId") Long projectId);

    List<ProjectPlanTask> seletUnfinishedTask();

    ProjectPlanTask queryProjectPlanTaskById(@Param("id") Long id);

}