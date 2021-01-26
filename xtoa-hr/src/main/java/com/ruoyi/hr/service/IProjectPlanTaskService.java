package com.ruoyi.hr.service;

import java.util.List;

import com.dingtalk.api.response.OapiWorkrecordUpdateResponse;
import com.ruoyi.base.domain.VO.ProjectPlanTaskVO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.ProjectPlanTask;

/**
 * 项目计划任务Service接口
 * 
 * @author xt
 * @date 2020-06-30
 */
public interface IProjectPlanTaskService 
{
    /**
     * 查询项目计划任务
     * 
     * @param id 项目计划任务ID
     * @return 项目计划任务
     */
    public ProjectPlanTask selectProjectPlanTaskById(Long id);

    /**
     * 查询项目计划任务列表
     * 
     * @param projectPlanTask 项目计划任务
     * @return 项目计划任务集合
     */
    public List<ProjectPlanTaskVO> selectProjectPlanTaskList(ProjectPlanTask projectPlanTask);

    /**
     * 新增项目计划任务
     * 
     * @param projectPlanTask 项目计划任务
     * @return 结果
     */
    public int insertProjectPlanTask(ProjectPlanTask projectPlanTask);

    /**
     * 修改项目计划任务
     * 
     * @param projectPlanTask 项目计划任务
     * @return 结果
     */
    public int updateProjectPlanTask(ProjectPlanTask projectPlanTask);

    /**
     * 批量删除项目计划任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectPlanTaskByIds(String ids);

    /**
     * 删除项目计划任务信息
     * 
     * @param id 项目计划任务ID
     * @return 结果
     */
    public int deleteProjectPlanTaskById(Long id);

    AjaxResult removeForClose(String ids);

    List<Long> selectProjectPlanTaskEmps(Long id);

    void addWorkRecord(ProjectPlanTask projectPlanTask);

    OapiWorkrecordUpdateResponse updateWorkRecord(String dingUserId, String recordId);

    AjaxResult finishTask(Long id);

    List<ProjectPlanTask> seletUnfinishedTask();

    void sendMsg(String img, ProjectPlanTask projectPlanTask);

    ProjectPlanTask findById(Long projectPlanTaskId);

    AjaxResult updateprojectPlanTask(Long projectPlanTaskId);
}
