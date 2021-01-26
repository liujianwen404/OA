package com.ruoyi.hr.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.*;
import com.ruoyi.hr.mapper.ProjectOperationMapper;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.IProjectEmpService;
import com.ruoyi.hr.service.IProjectOperationService;
import com.ruoyi.hr.utils.enums.ProjectEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目计划任务操作记录Service业务层处理
 * 
 * @author xt
 * @date 2020-06-30
 */
@Service
public class ProjectOperationServiceImpl implements IProjectOperationService 
{

    private static final Logger logger = LoggerFactory.getLogger(ProjectOperationServiceImpl.class);


    @Autowired
    private ProjectOperationMapper projectOperationMapper;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private IProjectEmpService projectEmpService;

    /**
     * 查询项目计划任务操作记录
     * 
     * @param id 项目计划任务操作记录ID
     * @return 项目计划任务操作记录
     */
    @Override
    public ProjectOperation selectProjectOperationById(Long id)
    {
        return projectOperationMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询项目计划任务操作记录列表
     * 
     * @param projectOperation 项目计划任务操作记录
     * @return 项目计划任务操作记录
     */
    @Override
    public List<ProjectOperation> selectProjectOperationList(ProjectOperation projectOperation)
    {
        projectOperation.setDelFlag("0");
        return projectOperationMapper.selectProjectOperationList(projectOperation);
    }

    /**
     * 新增项目计划任务操作记录
     * 
     * @param projectOperation 项目计划任务操作记录
     * @return 结果
     */
    @Override
    public int insertProjectOperation(ProjectOperation projectOperation)
    {
        if(ShiroUtils.getSysUser() != null && ShiroUtils.getUserId() != null){
            projectOperation.setCreateId(ShiroUtils.getUserId());
            projectOperation.setCreateBy(ShiroUtils.getLoginName());
            projectOperation.setCreateTime(DateUtils.getNowDate());
        }
        return projectOperationMapper.insertSelective(projectOperation);
    }

    /**
     * 修改项目计划任务操作记录
     * 
     * @param projectOperation 项目计划任务操作记录
     * @return 结果
     */
    @Override
    public int updateProjectOperation(ProjectOperation projectOperation)
    {
        projectOperation.setUpdateId(ShiroUtils.getUserId());
        projectOperation.setUpdateBy(ShiroUtils.getLoginName());
        projectOperation.setUpdateTime(DateUtils.getNowDate());
        return projectOperationMapper.updateByPrimaryKeySelective(projectOperation);
    }

    /**
     * 删除项目计划任务操作记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteProjectOperationByIds(String ids)
    {
        return projectOperationMapper.deleteProjectOperationByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除项目计划任务操作记录信息
     * 
     * @param id 项目计划任务操作记录ID
     * @return 结果
     */
    @Override
    public int deleteProjectOperationById(Long id)
    {
        return projectOperationMapper.deleteProjectOperationById(id);
    }

    @Override
    public void addProjectOperationByProjectEmp(ProjectEmp projectEmp,Integer operation,String contentDescribe) {
        ProjectOperation projectOperation = new ProjectOperation();
        projectOperation.setOperation(operation);
        if (projectEmp.getUpdateId() == null){
            projectOperation.setEmpId(projectEmp.getCreateId());
        }else {
            projectOperation.setEmpId(projectEmp.getUpdateId());
        }
        projectOperation.setProjectId(projectEmp.getProjectId());
        projectOperation.setProjectPlanId(projectEmp.getProjectPlanId());
        projectOperation.setProjectPlanTaskId(projectEmp.getProjectPlanTaskId());
        projectOperation.setType(projectEmp.getType());
        projectOperation.setContentDescribe(contentDescribe);
        insertProjectOperation(projectOperation);
    }

    @Override
    public void addProjectOperationByProjectPlanTask(ProjectPlanTask projectPlanTask, Integer operation, String contentDescribe) {
        ProjectOperation projectOperation = new ProjectOperation();
        projectOperation.setOperation(operation);
        Long userId = ShiroUtils.getUserId() == null ? projectPlanTask.getEmpId() : ShiroUtils.getUserId();
        projectOperation.setEmpId(userId);
        projectOperation.setProjectId(projectPlanTask.getProjectId());
        projectOperation.setProjectPlanId(projectPlanTask.getProjectPlanId());
        projectOperation.setProjectPlanTaskId(projectPlanTask.getId());
        projectOperation.setType(ProjectEnum.ProjectType.task.getValue());
        projectOperation.setContentDescribe(contentDescribe);
        int i = insertProjectOperation(projectOperation);

        if (i > 0 && operation.equals(ProjectEnum.ProjectOperation.creation.getValue())){
            ProjectEmp projectEmp = new ProjectEmp();
            projectEmp.setEmpId(projectPlanTask.getEmpId());
            projectEmp.setProjectId(projectPlanTask.getProjectId());
            projectEmp.setProjectPlanId(projectPlanTask.getProjectPlanId());
            projectEmp.setProjectPlanTaskId(projectPlanTask.getId());
            projectEmp.setType(ProjectEnum.ProjectType.task.getValue());
            projectEmpService.insertProjectEmp(projectEmp);
        }

    }

    @Override
    public void addProjectOperationByProjectPlan(ProjectPlan projectPlan, Integer operation, String contentDescribe) {
        ProjectOperation projectOperation = new ProjectOperation();
        projectOperation.setOperation(operation);
        projectOperation.setEmpId(ShiroUtils.getUserId());
        projectOperation.setProjectId(projectPlan.getProjectId());
        projectOperation.setProjectPlanId(projectPlan.getId());
        projectOperation.setType(ProjectEnum.ProjectType.plan.getValue());
        projectOperation.setContentDescribe(contentDescribe);
        insertProjectOperation(projectOperation);

        if (operation.equals(ProjectEnum.ProjectOperation.creation.getValue())){
            ProjectEmp projectEmp = new ProjectEmp();
            projectEmp.setEmpId(projectPlan.getEmpId());
            projectEmp.setProjectId(projectPlan.getProjectId());
            projectEmp.setProjectPlanId(projectPlan.getId());
            projectEmp.setType(ProjectEnum.ProjectType.plan.getValue());
            projectEmpService.insertProjectEmp(projectEmp);
        }

    }

    @Override
    public void addProjectOperationByProject(Project project, Integer operation, String format) {
        ProjectOperation projectOperation = new ProjectOperation();
        projectOperation.setOperation(operation);
        projectOperation.setEmpId(ShiroUtils.getUserId());
        projectOperation.setProjectId(project.getId());
        projectOperation.setType(ProjectEnum.ProjectType.project.getValue());
        projectOperation.setContentDescribe(format);
        insertProjectOperation(projectOperation);
    }

}
