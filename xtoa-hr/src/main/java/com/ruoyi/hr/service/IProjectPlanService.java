package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.base.domain.DTO.ProjectPlanDTO;
import com.ruoyi.base.domain.VO.ProjectPlanSelectVO;
import com.ruoyi.base.domain.VO.ProjectPlanVO;
import com.ruoyi.base.domain.VO.ProjectSelectVO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.ProjectPlan;

/**
 * 项目计划Service接口
 * 
 * @author xt
 * @date 2020-06-30
 */
public interface IProjectPlanService 
{
    /**
     * 查询项目计划
     * 
     * @param id 项目计划ID
     * @return 项目计划
     */
    public ProjectPlan selectProjectPlanById(Long id);

    /**
     * 查询项目计划列表
     * 
     * @param projectPlan 项目计划
     * @return 项目计划集合
     */
    public List<ProjectPlan> selectProjectPlanList(ProjectPlan projectPlan);

    /**
     * 新增项目计划
     * 
     * @param projectPlan 项目计划
     * @return 结果
     */
    public int insertProjectPlan(ProjectPlan projectPlan);

    /**
     * 修改项目计划
     * 
     * @param projectPlan 项目计划
     * @return 结果
     */
    public int updateProjectPlan(ProjectPlan projectPlan);

    /**
     * 批量删除项目计划
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectPlanByIds(String ids);

    /**
     * 删除项目计划信息
     * 
     * @param id 项目计划ID
     * @return 结果
     */
    public int deleteProjectPlanById(Long id);

    AjaxResult closeProjectPlanByIds(String ids);

    /**
     * 根据项目ID获取所有项目负责人
     * @param projectId 项目ID
     * @return 结果
     */
    List<Long> selectProjectPlanEmps(Long projectId);

    AjaxResult finishPlan(Long id);


    List<ProjectPlanVO> getProjectPlanList(ProjectPlanDTO dto);

    /**
     * 项目下拉列表
     * @return
     */
    List<ProjectPlanSelectVO> getProjectPlanSelectList(Long projectId);

}
