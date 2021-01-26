package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.base.domain.*;

/**
 * 项目计划任务操作记录Service接口
 * 
 * @author xt
 * @date 2020-06-30
 */
public interface IProjectOperationService 
{
    /**
     * 查询项目计划任务操作记录
     * 
     * @param id 项目计划任务操作记录ID
     * @return 项目计划任务操作记录
     */
    public ProjectOperation selectProjectOperationById(Long id);

    /**
     * 查询项目计划任务操作记录列表
     * 
     * @param projectOperation 项目计划任务操作记录
     * @return 项目计划任务操作记录集合
     */
    public List<ProjectOperation> selectProjectOperationList(ProjectOperation projectOperation);

    /**
     * 新增项目计划任务操作记录
     * 
     * @param projectOperation 项目计划任务操作记录
     * @return 结果
     */
    public int insertProjectOperation(ProjectOperation projectOperation);

    /**
     * 修改项目计划任务操作记录
     * 
     * @param projectOperation 项目计划任务操作记录
     * @return 结果
     */
    public int updateProjectOperation(ProjectOperation projectOperation);

    /**
     * 批量删除项目计划任务操作记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectOperationByIds(String ids);

    /**
     * 删除项目计划任务操作记录信息
     * 
     * @param id 项目计划任务操作记录ID
     * @return 结果
     */
    public int deleteProjectOperationById(Long id);

    void addProjectOperationByProjectEmp(ProjectEmp projectEmp,Integer operation,String contentDescribe);

    void addProjectOperationByProjectPlanTask(ProjectPlanTask projectPlanTask, Integer operation, String contentDescribe);

    void addProjectOperationByProjectPlan(ProjectPlan projectPlan, Integer operation, String contentDescribe);

    void addProjectOperationByProject(Project project, Integer operation, String format);
}
