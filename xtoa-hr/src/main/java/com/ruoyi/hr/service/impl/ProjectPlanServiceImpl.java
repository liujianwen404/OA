package com.ruoyi.hr.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.base.domain.DTO.ProjectPlanDTO;
import com.ruoyi.base.domain.VO.ProjectPlanSelectVO;
import com.ruoyi.base.domain.VO.ProjectPlanVO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.ProjectPlan;
import com.ruoyi.hr.mapper.ProjectPlanMapper;
import com.ruoyi.hr.service.*;
import com.ruoyi.hr.utils.enums.ProjectEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

/**
 * 项目计划Service业务层处理
 * 
 * @author xt
 * @date 2020-06-30
 */
@Service
public class ProjectPlanServiceImpl implements IProjectPlanService 
{

    @Resource
    private ProjectPlanMapper projectPlanMapper;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IProjectEmpService projectEmpService;

    @Autowired
    private IProjectOperationService projectOperationService;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private IProjectPlanTaskService projectPlanTaskService;


    /**
     * 查询项目计划
     * 
     * @param id 项目计划ID
     * @return 项目计划
     */
    @Override
    public ProjectPlan selectProjectPlanById(Long id)
    {
        return projectPlanMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询项目计划列表
     * 
     * @param projectPlan 项目计划
     * @return 项目计划
     */
    @Override
    public List<ProjectPlan> selectProjectPlanList(ProjectPlan projectPlan)
    {
        List<ProjectPlan> projectPlans = projectPlanMapper.selectProjectPlanList(projectPlan);

        projectPlans.forEach(
                p ->
                {
                    p.setProjectName( Optional.ofNullable(projectService.selectProjectById(projectPlan.getProjectId()))
                            .map(pj -> pj.getName()).orElse(null));

                    p.setEmpName( Optional.ofNullable(hrEmpService.selectTHrEmpById(p.getEmpId()))
                            .map(emp -> emp.getEmpName()).orElse(null));
                }
        );

        return projectPlans;
    }

    /**
     * 新增项目计划
     * 
     * @param projectPlan 项目计划
     * @return 结果
     */
    @Override
    @Transactional
    public int insertProjectPlan(ProjectPlan projectPlan)
    {
        projectPlan.setCreateId(ShiroUtils.getUserId());
        projectPlan.setCreateBy(ShiroUtils.getLoginName());
        projectPlan.setCreateTime(DateUtils.getNowDate());

        int i = projectPlanMapper.insertSelective(projectPlan);
        if ( i > 0){
            String format =
                    StrUtil.format("{} 为 项目 添加了计划 ：{}",
                            ShiroUtils.getSysUser().getUserName(),
                            projectPlan.getName());

            projectOperationService.addProjectOperationByProjectPlan(projectPlan,ProjectEnum.ProjectOperation.creation.getValue(),format);
        }

        return i;
    }

    /**
     * 修改项目计划
     * 
     * @param projectPlan 项目计划
     * @return 结果
     */
    @Override
    public int updateProjectPlan(ProjectPlan projectPlan)
    {
        projectPlan.setUpdateId(ShiroUtils.getUserId());
        projectPlan.setUpdateBy(ShiroUtils.getLoginName());
        projectPlan.setUpdateTime(DateUtils.getNowDate());
        int i = projectPlanMapper.updateByPrimaryKeySelective(projectPlan);
        if (i > 0){
            String format =
                    StrUtil.format("{} 编辑了项目计划。",
                            ShiroUtils.getSysUser().getUserName());
            projectOperationService.addProjectOperationByProjectPlan(projectPlan,ProjectEnum.ProjectOperation.edit.getValue(),format);
        }
        return i;
    }

    /**
     * 删除项目计划对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteProjectPlanByIds(String ids)
    {
        return projectPlanMapper.deleteProjectPlanByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除项目计划信息
     * 
     * @param id 项目计划ID
     * @return 结果
     */
    @Override
    public int deleteProjectPlanById(Long id)
    {
        return projectPlanMapper.deleteProjectPlanById(id);
    }

    @Override
    @Transactional
    public AjaxResult closeProjectPlanByIds(String ids) {

        String[] strings = Convert.toStrArray(ids);
        for (String string : strings) {
            ProjectPlan projectPlan = selectProjectPlanById(Long.parseLong(string));
            if (projectPlan != null && projectPlan.getStatus().equals(ProjectEnum.ProjectStatus.creation.getValue())){
                projectPlan.setStatus(ProjectEnum.ProjectStatus.close.getValue());

                projectPlan.setUpdateId(ShiroUtils.getUserId());
                projectPlan.setUpdateBy(ShiroUtils.getLoginName());
                projectPlan.setUpdateTime(DateUtils.getNowDate());
                int i = projectPlanMapper.updateByPrimaryKeySelective(projectPlan);
                if (i > 0){
                    String format =
                            StrUtil.format("{} 关闭了计划 ：{}",
                                    ShiroUtils.getSysUser().getUserName(),
                                    projectPlan.getName());

                    projectOperationService.addProjectOperationByProjectPlan(projectPlan,ProjectEnum.ProjectOperation.close.getValue(),format);

                   /* //标记项目计划的成员为项目成员而不是项目计划成员
                    Example example = new Example(ProjectEmp.class);
                    example.createCriteria().andEqualTo("projectId",projectPlan.getProjectId())
                                            .andEqualTo("projectPlanId",projectPlan.getId())
                                            .andEqualTo("delFlag","0");
                    List<ProjectEmp> emps = projectEmpService.selectByExample(example);
                    Set<Long> collect = emps.stream().map(e -> {
                        return e.getEmpId();
                    }).collect(Collectors.toSet());

                    projectEmpService.resetEmpToProject(collect);*/

                }
            }
        }
        return AjaxResult.success();
    }

    @Override
    public List<Long> selectProjectPlanEmps(Long projectId) {
        return projectPlanMapper.selectProjectPlanEmps(projectId);
    }

    @Override
    @Transactional
    public AjaxResult finishPlan(Long id) {

        List<Long> planEmps = projectPlanTaskService.selectProjectPlanTaskEmps(id);
        if (!planEmps.isEmpty()){
            return AjaxResult.error("还有未完成的任务,请先确认当前计划的任务都为完成或关闭的状态！");
        }


        ProjectPlan projectPlan = projectPlanMapper.selectByPrimaryKey(id);

        projectPlan.setStatus(ProjectEnum.ProjectStatus.finish.getValue());
        projectPlan.setUpdateId(ShiroUtils.getUserId());
        projectPlan.setUpdateBy(ShiroUtils.getLoginName());
        projectPlan.setUpdateTime(DateUtils.getNowDate());
        int i = projectPlanMapper.updateByPrimaryKeySelective(projectPlan);
        if (i > 0){
            String format =
                    StrUtil.format("{} 完成了计划 ：{}",
                            ShiroUtils.getSysUser().getUserName(),
                            projectPlan.getName());
            projectOperationService.addProjectOperationByProjectPlan(projectPlan,
                    ProjectEnum.ProjectOperation.finish.getValue(),format);
        }
        return AjaxResult.success();
    }

    @Override
    public List<ProjectPlanVO> getProjectPlanList(ProjectPlanDTO dto) {
        List<ProjectPlanVO> list= projectPlanMapper.getProjectPlanList(dto);
        if(!CollectionUtils.isEmpty(list)){
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
           list.stream().forEach(nico->nico.setIterationsTime(sdf.format(nico.getStartTime())+"~"+sdf.format(nico.getEndTime())));
        }
        return list;
    }

    @Override
    public List<ProjectPlanSelectVO> getProjectPlanSelectList(Long projectId) {
        return projectPlanMapper.queryProjectPlanList(projectId);
    }


}
