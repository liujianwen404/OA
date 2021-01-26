package com.ruoyi.hr.service.impl;

import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.request.OapiChatUpdateRequest;
import com.dingtalk.api.response.OapiChatCreateResponse;
import com.dingtalk.api.response.OapiChatGetResponse;
import com.ruoyi.base.dingTalk.DingChatApi;
import com.ruoyi.base.domain.Project;
import com.ruoyi.base.domain.ProjectEmp;
import com.ruoyi.base.domain.ProjectOperation;
import com.ruoyi.base.domain.VO.ProjectSelectVO;
import com.ruoyi.base.domain.VO.ProjectVO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.ProjectMapper;
import com.ruoyi.hr.service.*;
import com.ruoyi.hr.utils.enums.ProjectEnum;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目Service业务层处理
 * 
 * @author xt
 * @date 2020-06-30
 */
@Service
public class ProjectServiceImpl implements IProjectService 
{

    @Resource
    private ProjectMapper projectMapper;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private IProjectOperationService projectOperationService;

    @Resource
    private IProjectEmpService projectEmpService;

    @Autowired
    private DingChatApi dingChatApi;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IProjectPlanService projectPlanService;

    /**
     * 查询项目
     * 
     * @param id 项目ID
     * @return 项目
     */
    @Override
    public Project selectProjectById(Long id)
    {
        return projectMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询项目列表
     * 
     * @param project 项目
     * @return 项目
     */
    @Override
    public List<Project> selectProjectList(Project project)
    {
        project.setDelFlag("0");
        return projectMapper.selectProjectList(project);
    }

    /**
     * 新增项目
     * 
     * @param project 项目
     * @return 结果
     */
    @Override
    @Transactional
    public int insertProject(Project project)
    {

        SysUser user = sysUserService.selectUserById(project.getEmpId());
        String dingUserId = hrEmpService.getDingUserId(user, false);

        if(StringUtils.isBlank(dingUserId)) {
            throw new BusinessException("此负责人已退出钉钉群组,请重新选择负责人");
        }

            OapiChatCreateResponse chatCreateResponse = dingChatApi.create(dingUserId, project.getName());

            project.setChatId(chatCreateResponse.getChatid());
            project.setCreateId(ShiroUtils.getUserId());
            project.setCreateBy(ShiroUtils.getLoginName());
            project.setCreateTime(DateUtils.getNowDate());
            int i = projectMapper.insertSelective(project);
            if (i > 0) {
                ProjectOperation projectOperation = new ProjectOperation();
                projectOperation.setOperation(ProjectEnum.ProjectOperation.creation.getValue());
                projectOperation.setEmpId(ShiroUtils.getUserId());
                projectOperation.setProjectId(project.getId());
                projectOperation.setType(ProjectEnum.ProjectType.project.getValue());
                projectOperation.setContentDescribe(
                        StrUtil.format("{} 创建了项目", ShiroUtils.getSysUser().getUserName()));
                projectOperationService.insertProjectOperation(projectOperation);

                ProjectEmp projectEmp = new ProjectEmp();
                projectEmp.setEmpId(project.getEmpId());
                projectEmp.setProjectId(project.getId());
                projectEmp.setType(ProjectEnum.ProjectType.project.getValue());

                projectEmpService.insertProjectEmp(projectEmp);

            }
            return i;

    }

    /**
     * 修改项目
     * 
     * @param project 项目
     * @return 结果
     */
    @Override
    @Transactional
    public int updateProject(Project project)
    {

        Project oldProject = projectMapper.selectByPrimaryKey(project.getId());

        if (project.getUpdateId() == null){
            project.setUpdateId(ShiroUtils.getUserId());
        }

        if (StringUtils.isEmpty(project.getUpdateBy())){
            project.setUpdateBy(ShiroUtils.getLoginName());
        }

        project.setUpdateTime(DateUtils.getNowDate());
        int i = projectMapper.updateByPrimaryKeySelective(project);
        if ( i > 0){
            String format ;
            if (!Objects.equals(oldProject.getEmpId(),project.getEmpId())){
                if(projectEmpService.queryCountByEmpId(project.getId(),project.getEmpId())==0){
                    ProjectEmp projectEmp = new ProjectEmp();
                    projectEmp.setEmpId(project.getEmpId());
                    projectEmp.setProjectId(project.getId());
                    projectEmp.setType(ProjectEnum.ProjectType.project.getValue());
                    projectEmpService.insertProjectEmp(projectEmp);
                }

                SysUser sysUser = sysUserService.selectUserById(project.getEmpId());

                String dingUserId = hrEmpService.getDingUserId(sysUser, false);
                ArrayList<String> addUserList = new ArrayList<>();
                addUserList.add(dingUserId);

                OapiChatGetResponse oapiChatGetResponse = dingChatApi.get(oldProject.getChatId());
                OapiChatUpdateRequest oapiChatUpdateRequest = new OapiChatUpdateRequest();
                oapiChatUpdateRequest.setChatid(oldProject.getChatId());
                oapiChatUpdateRequest.setOwner(dingUserId);
                oapiChatUpdateRequest.setOwnerType("emp");
                oapiChatUpdateRequest.setAddUseridlist(addUserList);
                oapiChatUpdateRequest.setManagementType(oapiChatGetResponse.getChatInfo().getManagementType());
                oapiChatUpdateRequest.setValidationType(oapiChatGetResponse.getChatInfo().getValidationType());
                dingChatApi.update(oapiChatUpdateRequest);

                if (!Objects.equals(oldProject.getContentDescribe(),project.getContentDescribe())){
                    format =
                            StrUtil.format("{} 更换了项目负责人：{} ，并编辑了项目内容: {}",
                                    ShiroUtils.getSysUser().getUserName(),sysUser.getUserName(),project.getName());
                }else {
                    format =
                            StrUtil.format("{} 更换了项目负责人：{}",
                                    ShiroUtils.getSysUser().getUserName(),sysUser.getUserName());
                }
            }else {

                if (project.getStatus().equals(ProjectEnum.ProjectStatus.close.getValue())){
                    format =
                            StrUtil.format("{} 关闭了项目: {}",
                                    project.getUpdateBy(),project.getName());
                }else {
                    format =
                            StrUtil.format("{} 编辑了项目内容: {}",
                                    ShiroUtils.getSysUser().getUserName(),project.getName());
                }
            }


            projectOperationService.addProjectOperationByProject(project,ProjectEnum.ProjectOperation.edit.getValue(),format);
        }
        return i;
    }

    /**
     * 删除项目对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteProjectByIds(String ids)
    {
        return projectMapper.deleteProjectByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除项目信息
     * 
     * @param id 项目ID
     * @return 结果
     */
    @Override
    public int deleteProjectById(Long id)
    {
        return projectMapper.deleteProjectById(id);
    }

    @Override
    public AjaxResult getMyCount() {

//        HashMap<String,Integer> map = projectMapper.getMyCount(ShiroUtils.getUserId());
        HashMap<String,Integer> map = new HashMap<>();
        Integer myProject = projectMapper.getMyProjectCount();
        Integer myProjectPlan = projectMapper.getMyProjectPlanCount();
        Integer myProjectPlanTask = projectMapper.getMyProjectPlanTaskCount();

        map.put("myProject",myProject);
        map.put("myProjectPlan",myProjectPlan);
        map.put("myProjectPlanTask",myProjectPlanTask);

        return AjaxResult.success(map);
    }

    @Override
    public List<ProjectVO> projectTableInfo(Integer status, String empName) {
        List<ProjectVO> list = projectMapper.projectTableInfo(status,empName);
        return list;
    }

    @Override
    public Project selectProjectByChatId(String chatId) {
        Example example = new Example(Project.class);
        example.createCriteria().andEqualTo("chatId" ,chatId);
        return projectMapper.selectOneByExample(example);
    }

    @Override
    @Transactional
    public AjaxResult finishProject(Long id) {

        List<Long> planEmps = projectPlanService.selectProjectPlanEmps(id);
        if (!planEmps.isEmpty()){
            return AjaxResult.error("还有未完成的计划,请先确认当前项目的计划都为完成或关闭的状态！");
        }


        Project project = projectMapper.selectByPrimaryKey(id);
        project.setStatus(ProjectEnum.ProjectStatus.finish.getValue());
        project.setUpdateId(ShiroUtils.getUserId());
        project.setUpdateBy(ShiroUtils.getLoginName());
        int i = projectMapper.updateByPrimaryKeySelective(project);
        if (i > 0) {
            String format =
                    StrUtil.format("{} 完成了项目。{}",
                            ShiroUtils.getSysUser().getUserName(),project.getName());
            projectOperationService.addProjectOperationByProject(project,
                    ProjectEnum.ProjectOperation.finish.getValue(),format);


            OapiChatGetResponse oapiChatGetResponse = dingChatApi.get(project.getChatId());
            List<String> useridlist = oapiChatGetResponse.getChatInfo().getUseridlist();
            OapiChatUpdateRequest oapiChatUpdateRequest = new OapiChatUpdateRequest();
            oapiChatUpdateRequest.setChatid(project.getChatId());
            oapiChatUpdateRequest.setOwnerType("emp");
            oapiChatUpdateRequest.setDelUseridlist(useridlist);
            oapiChatUpdateRequest.setManagementType(oapiChatGetResponse.getChatInfo().getManagementType());
            oapiChatUpdateRequest.setValidationType(oapiChatGetResponse.getChatInfo().getValidationType());
            dingChatApi.update(oapiChatUpdateRequest);


        }
        return AjaxResult.success();
    }

    @Override
    public List<ProjectSelectVO> getProjectSelectList() {
        return projectMapper.getProjectSelectList();
    }

    @Override
    public String queryProjectChatIdById(Long id) {
        return projectMapper.queryProjectChatIdById(id);
    }
}
