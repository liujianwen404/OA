package com.ruoyi.hr.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.request.OapiChatUpdateRequest;
import com.dingtalk.api.response.OapiChatGetResponse;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.base.dingTalk.DingChatApi;
import com.ruoyi.base.dingTalk.dingCallBackDTO.ChatCallBackDTO;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.*;
import com.ruoyi.hr.mapper.ProjectEmpMapper;
import com.ruoyi.hr.service.*;
import com.ruoyi.hr.utils.enums.ProjectEnum;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.vo.UserTreeVo;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import com.ruoyi.common.exception.BusinessException;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 项目迭代任务成员Service业务层处理
 * 
 * @author xt
 * @date 2020-06-30
 */
@Service
public class ProjectEmpServiceImpl implements IProjectEmpService 
{

    private static final Logger logger = LoggerFactory.getLogger(ProjectEmpServiceImpl.class);

    @Resource
    private ProjectEmpMapper projectEmpMapper;

    @Autowired
    private IProjectOperationService projectOperationService;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private IProjectPlanTaskService projectPlanTaskService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private IProjectPlanService projectPlanService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private DingChatApi dingChatApi;

    @Resource(name = "ThreadPoolExecutorForCallBack")
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 查询项目迭代任务成员
     * 
     * @param id 项目迭代任务成员ID
     * @return 项目迭代任务成员
     */
    @Override
    public ProjectEmp selectProjectEmpById(Long id)
    {
        return projectEmpMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询项目迭代任务成员列表
     * 
     * @param projectEmp 项目迭代任务成员
     * @return 项目迭代任务成员
     */
    @Override
    public List<ProjectEmp> selectProjectEmpList(ProjectEmp projectEmp)
    {
        List<ProjectEmp> projectEmps = projectEmpMapper.selectProjectEmpList(projectEmp);

        projectEmps.stream().forEach(p -> {
            p.setProjectName(projectService.selectProjectById(p.getProjectId()).getName());
            if (p.getProjectPlanId() != null){
                p.setProjectPlanName(projectPlanService.selectProjectPlanById(p.getProjectPlanId()).getName());
            }
            if (p.getProjectPlanTaskId() != null){
                p.setProjectPlanTaskName(projectPlanTaskService.selectProjectPlanTaskById(p.getProjectPlanTaskId()).getName());
            }
            p.setEmpName(hrEmpService.selectTHrEmpById(p.getEmpId()).getEmpName());
        });

        return projectEmps;
    }

    @Override
    public int queryCountByEmpId(Long projectId, Long empId) {
        return projectEmpMapper.queryCountByEmpId(projectId,empId);
    }

    /**
     * 新增项目迭代任务成员
     * 
     * @param projectEmp 项目迭代任务成员
     * @return 结果
     */
    @Override
    @Transactional
    public int insertProjectEmp(ProjectEmp projectEmp)
    {
        if (projectEmp.getCreateId() == null){
            projectEmp.setCreateId(ShiroUtils.getUserId());
        }
        if (StringUtils.isEmpty(projectEmp.getCreateBy())){
            projectEmp.setCreateBy(ShiroUtils.getLoginName());
        }
        if (StringUtils.isEmpty(projectEmp.getCreateUserName())){
            projectEmp.setCreateUserName(ShiroUtils.getSysUser().getUserName());
        }
        projectEmp.setCreateTime(DateUtils.getNowDate());


        //查询员工是否已经是该项目的成员了
        Example example = new Example(ProjectEmp.class);
        Example.Criteria criteria = example.createCriteria()
                .andEqualTo("type",projectEmp.getType())
                .andEqualTo("projectId", projectEmp.getProjectId())
                .andEqualTo("delFlag", "0")
                .andEqualTo("empId", projectEmp.getEmpId());

        if (projectEmp.getProjectPlanId() != null){
            //寻找项目迭代成员
            criteria.andEqualTo("projectPlanId",projectEmp.getProjectPlanId());
        }

        if (projectEmp.getProjectPlanTaskId() != null){
            //寻找项目迭代任务成员
            criteria.andEqualTo("projectPlanTaskId",projectEmp.getProjectPlanTaskId());
        }

        List<ProjectEmp> select = projectEmpMapper.selectByExample(example);
        int i = 1;
        if (select.isEmpty()){
            i = projectEmpMapper.insertSelective(projectEmp);
        }/*else {
            //该用户已经是项目成员
            if (projectEmp.getProjectPlanId() != null){

                Project project = projectService.selectProjectById(projectEmp.getProjectId());

                Boolean isOperation = false;
                if (!Objects.equals(project.getEmpId(),projectEmp.getEmpId())){
                    //该员工不是这个项目的负责人才去更新关系数据
                    for (ProjectEmp emp : select) {
                        if ( emp.getProjectPlanId() != null ){
                            continue;
                        }
                        //有一条数据没有项目计划，现在分配项目计划
                        emp.setProjectPlanId(projectEmp.getProjectPlanId());
                        emp.setType(projectEmp.getType());
                        i = projectEmpMapper.updateByPrimaryKeySelective(emp);
                        isOperation = true;
                    }
                }

                if (!isOperation){
                    Boolean isEq = false;
                    for (ProjectEmp emp : select) {
                        if ( Objects.equals(emp.getProjectPlanId(),projectEmp.getProjectPlanId()) ){
                            isEq = true;
                        }
                    }
                    if (!isEq){
                        //没有一条数据是这个项目计划,现在添加新的项目计划
                        i = projectEmpMapper.insertSelective(projectEmp);
                    }
                }

            }
        }*/
        if (i > 0){
            HrEmp hrEmp = hrEmpService.selectTHrEmpById(projectEmp.getEmpId());
            String format =
                    StrUtil.format("{} 为 {} 添加了成员 ：{}",
                            projectEmp.getCreateUserName(),
                            ProjectEnum.ProjectType.asMap().get(projectEmp.getType()),
                            hrEmp.getEmpName());
            projectOperationService.addProjectOperationByProjectEmp(projectEmp,
                    ProjectEnum.ProjectOperation.turnover.getValue(),format);

        }
        return i;
    }

    /**
     * 修改项目迭代任务成员
     * 
     * @param projectEmp 项目迭代任务成员
     * @return 结果
     */
    @Override
    public int updateProjectEmp(ProjectEmp projectEmp)
    {
        projectEmp.setUpdateId(ShiroUtils.getUserId());
        projectEmp.setUpdateBy(ShiroUtils.getLoginName());
        projectEmp.setUpdateTime(DateUtils.getNowDate());
        return projectEmpMapper.updateByPrimaryKeySelective(projectEmp);
    }

    /**
     * 删除项目迭代任务成员对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteProjectEmpByIds(String ids)
    {
        String[] strings = Convert.toStrArray(ids);
        //目前只支持项目管理内单个删除 ^_^
        Long projectEmpId = Long.parseLong(strings[0]);

        Long pId = null;
        ProjectEmp projEmp = projectEmpMapper.selectByPrimaryKey(projectEmpId);
        if (projEmp != null && Objects.equals(projEmp.getDelFlag(),"0")){

            if (projEmp.getProjectPlanTaskId() != null){
                ProjectPlanTask projectPlanTask = projectPlanTaskService.selectProjectPlanTaskById(projEmp.getProjectPlanTaskId());
                if (projectPlanTask != null && Objects.equals(projectPlanTask.getEmpId(),projEmp.getEmpId())){
                    throw new BusinessException("项目迭代任务负责人不能直接移除，更换项目迭代任务负责人即可");
                }
            }else {
                if (projEmp.getProjectPlanId() != null){
                    ProjectPlan projectPlan = projectPlanService.selectProjectPlanById(projEmp.getProjectPlanId());
                    if (projectPlan != null && Objects.equals(projectPlan.getEmpId(),projEmp.getEmpId())){
                        throw new BusinessException("项目迭代负责人不能直接移除，更换项目迭代负责人即可");
                    }
                }else {
                    Project project = projectService.selectProjectById(projEmp.getProjectId());
                    if (project != null ){
                        if ( Objects.equals(project.getEmpId(),projEmp.getEmpId()) ){
                            throw new BusinessException("项目负责人不能直接移除，更换项目负责人即可");
                        }else {
                            List<Long> planEmps = projectPlanService.selectProjectPlanEmps(project.getId());
                            if (planEmps.contains(projEmp.getEmpId())){
                                throw new BusinessException("项目迭代负责人不能直接移除，更换项目迭代负责人即可");
                            }

                            List<Long> taskEmps = projectPlanTaskService.selectProjectPlanTaskEmps(project.getId());
                            if (taskEmps.contains(projEmp.getEmpId())){
                                throw new BusinessException("项目迭代任务负责人不能直接移除，更换项目迭代任务负责人即可");
                            }
                        }
                        pId = project.getId();
                    }
                }
            }
        }

        int i = projectEmpMapper.deleteProjectEmpByIds(strings);
        if (i > 0){

            Set<Long> pids  = new HashSet<>();
            Set<Long> userIds  = new HashSet<>();
            for (String string : strings) {
                ProjectEmp projectEmp = selectProjectEmpById(Long.parseLong(string));
                if (projectEmp != null && projectEmp.getDelFlag().equals("1")){
                    //删除的成员数据都是同一个项目的，这里随便拿一个
                    pids.add(projectEmp.getProjectId());
                    //删除的成员数据，这里拿用户id集合
                    userIds.add(projectEmp.getEmpId());

                    HrEmp hrEmp = hrEmpService.selectTHrEmpById(projectEmp.getEmpId());
                    String format = StrUtil.format("{} 为 {} 删除了成员 ：{}",
                            ShiroUtils.getSysUser().getUserName(),
                            ProjectEnum.ProjectType.asMap().get(projectEmp.getType()), hrEmp.getEmpName());
                    projectOperationService.addProjectOperationByProjectEmp(projectEmp,ProjectEnum.ProjectOperation.turnover.getValue(),format);
                }
            }

            if (!userIds.isEmpty()){
                if (pId != null){
                    //移除项目成员并移除该项目中这个成员所在项目计划的成员数据
                    projectEmpMapper.deleteProjectEmpByIdsByProjectId(pId,userIds);
                }
            }

            if (!pids.isEmpty()){
                //移除钉钉群
                Iterator<Long> iterator = pids.iterator();
                if (iterator.hasNext()){
                    Project project = projectService.selectProjectById(iterator.next());
                    if (project != null){
                        List<String> dingUserIds = new ArrayList<>();
                        userIds.forEach(userId -> {
                            SysUser sysUser = sysUserService.selectUserById(userId);
                            String dingUserId = hrEmpService.getDingUserId(sysUser, false);
                            dingUserIds.add(dingUserId);
                        });

                        OapiChatGetResponse oapiChatGetResponse = dingChatApi.get(project.getChatId());
                        if (oapiChatGetResponse.isSuccess()){
                            OapiChatUpdateRequest oapiChatUpdateRequest = new OapiChatUpdateRequest();
                            oapiChatUpdateRequest.setChatid(project.getChatId());
                            oapiChatUpdateRequest.setOwnerType("emp");
                            oapiChatUpdateRequest.setDelUseridlist(dingUserIds);
                            oapiChatUpdateRequest.setManagementType(oapiChatGetResponse.getChatInfo().getManagementType());
                            oapiChatUpdateRequest.setValidationType(oapiChatGetResponse.getChatInfo().getValidationType());
                            dingChatApi.update(oapiChatUpdateRequest);
                        }

                    }
                }
            }
        }
        return i;
    }

    /**
     * 删除项目计划任务成员信息
     * 
     * @param id 项目计划任务成员ID
     * @return 结果
     */
    @Override
    public int deleteProjectEmpById(Long id)
    {
        return projectEmpMapper.deleteProjectEmpById(id);
    }

    @Override
    @Transactional
    public AjaxResult insertProjectEmps(ProjectEmp projectEmp, Set<String> collect) {

        collect.forEach(empId -> {
            logger.info("insertProjectEmps : " + empId);
            ProjectEmp pEmp = new ProjectEmp();
            BeanUtil.copyProperties(projectEmp,pEmp);
            pEmp.setEmpId(Long.parseLong(empId));

           /* //查询员工是否已经是该醒目的成员了
            Example example = new Example(ProjectEmp.class);
            example.createCriteria()
                    .andEqualTo("projectId",pEmp.getProjectId())
                    .andEqualTo("delFlag","0")
                    .andEqualTo("empId",pEmp.getEmpId());
            List<ProjectEmp> select = projectEmpMapper.selectByExample(example);
            if (select.isEmpty()){
                insertProjectEmp(pEmp);
            }*/
            insertProjectEmp(pEmp);
        });
        return AjaxResult.success();
    }

    @Override
    public List<UserTreeVo> selectProjectEmpToUserTreeVo(ProjectEmp projectEmp) {
        List<ProjectEmp> projectEmps = selectProjectEmpList(projectEmp);
        Set<Long> collect = projectEmps.stream().map(p -> p.getEmpId()).collect(Collectors.toSet());
        return sysUserService.selectUserByIds(collect);
    }

    @Override
    @Transactional
    public void chatAddMember(ChatCallBackDTO chatCallBackDTO) {
        Project project = projectService.selectProjectByChatId(chatCallBackDTO.getChatId());
        if (project != null){
            //项目添加成员
            ProjectEmp projectEmp = new ProjectEmp();
            projectEmp.setProjectId(project.getId());
            projectEmp.setType(ProjectEnum.ProjectType.project.getValue());
            //根据dingUserId获取操作人
            SysUser operatorSysUser = hrEmpService.getSysUserByDingUserId(chatCallBackDTO.getOperator(),false);
            if (operatorSysUser != null){
                projectEmp.setCreateBy(operatorSysUser.getLoginName());
                projectEmp.setCreateUserName(operatorSysUser.getUserName());
                projectEmp.setCreateId(operatorSysUser.getUserId());

                Set<String> ids = chatCallBackDTO.getUserId().stream()
                        //遍历list获取userId
                        .map(dingUserId -> {
                    SysUser sysUser = hrEmpService.getSysUserByDingUserId(dingUserId,true);
                    return sysUser == null ? "" : sysUser.getUserId()+"";
                }).collect(Collectors.toList())
                        //去除空的数据并且去重
                        .stream().filter(userId -> StringUtils.isNotEmpty(userId)).collect(Collectors.toSet());
                logger.info("ids : " + Arrays.toString(ids.toArray()));
                //为项目添加成员
                insertProjectEmps(projectEmp,ids);


                threadPoolExecutor.execute(() -> {
                    synProjectChat(project.getChatId());
                });


            }else {
                logger.info("操作人为空 : " + chatCallBackDTO.getOperator());
            }
        }else {
            logger.info("该会话没有找到相关项目 : " + chatCallBackDTO.getChatId());
        }
    }

    @Override
    @Transactional
    public void chatRemoveMember(ChatCallBackDTO chatCallBackDTO) {
        Project project = projectService.selectProjectByChatId(chatCallBackDTO.getChatId());
        //这里做项目状态判断，OA系统关闭项目时不做删除成员操作
        if (project != null && !project.getStatus().equals(ProjectEnum.ProjectStatus.close.getValue())){
            //根据dingUserId获取操作人
            SysUser operatorSysUser = hrEmpService.getSysUserByDingUserId(chatCallBackDTO.getOperator(),false);
            List<Long> emps = projectPlanService.selectProjectPlanEmps(project.getId());
            Set<Long> ids = chatCallBackDTO.getUserId().stream()
                    //遍历list获取userId
                    .map(dingUserId -> {
                        SysUser sysUser = hrEmpService.getSysUserByDingUserId(dingUserId,true);
                        if (sysUser != null){
                           //排除项目计划负责人
                            if (emps.contains(sysUser.getUserId())){
                                return null;
                            }
                        }
                        return sysUser == null ? null : sysUser.getUserId();
                    }).collect(Collectors.toList())
                    //去除空的数据并且去重,不能去掉项目负责人
                    .stream().filter(userId -> userId != null && !userId.equals(project.getEmpId())).collect(Collectors.toSet());
            logger.info("ids : " + Arrays.toString(ids.toArray()));

            if (!ids.isEmpty()){
                ProjectEmp projectEmp = new ProjectEmp();
                projectEmp.setProjectId(project.getId());
                projectEmp.setEmpIds(ids.stream().collect(Collectors.toList()));
                if (operatorSysUser != null){
                    projectEmp.setUpdateId(operatorSysUser.getUserId());
                    projectEmp.setUpdateBy(operatorSysUser.getLoginName());
                }
                projectEmpMapper.deleteProjectEmpByCallBack(projectEmp);
            }



            threadPoolExecutor.execute(() -> {
                synProjectChat(project.getChatId());
            });


        }else {
            logger.info("该会话没有找到相关项目 : " + project);
        }

    }

    @Override
    @Transactional
    public void chatQuit(ChatCallBackDTO chatCallBackDTO) {
        Project project = projectService.selectProjectByChatId(chatCallBackDTO.getChatId());
        if (project != null){
            //根据dingUserId获取操作人
            SysUser operatorSysUser = hrEmpService.getSysUserByDingUserId(chatCallBackDTO.getOperator(),false);
            List<Long> emps = projectPlanService.selectProjectPlanEmps(project.getId());
            if (operatorSysUser != null
                    && !operatorSysUser.getUserId().equals(project.getEmpId())
                    && !emps.contains(operatorSysUser.getUserId())){
                ProjectEmp projectEmp = new ProjectEmp();
                projectEmp.setProjectId(project.getId());
                List<Long> list = new ArrayList<>();
                list.add(operatorSysUser.getUserId());
                projectEmp.setEmpIds(list);
                projectEmp.setUpdateId(operatorSysUser.getUserId());
                projectEmp.setUpdateBy(operatorSysUser.getLoginName());
                projectEmpMapper.deleteProjectEmpByCallBack(projectEmp);

                threadPoolExecutor.execute(() -> {
                    synProjectChat(project.getChatId());
                });

            }else {
                logger.info("不能去掉负责人 : " + operatorSysUser);
            }

            threadPoolExecutor.execute(() -> {
                synProjectChat(project.getChatId());
            });

        }else {
            logger.info("该会话没有找到相关项目 : " + chatCallBackDTO.getChatId());
        }
    }

    @Override
    @Transactional
    public void chatDisband(ChatCallBackDTO chatCallBackDTO) {

        Project project = projectService.selectProjectByChatId(chatCallBackDTO.getChatId());
        if (project != null){
            //根据dingUserId获取操作人
            SysUser operatorSysUser = hrEmpService.getSysUserByDingUserId(chatCallBackDTO.getOperator(),false);

            if (operatorSysUser != null){
                project.setUpdateId(operatorSysUser.getUserId());
                project.setUpdateBy(operatorSysUser.getLoginName());
            }

            project.setStatus(ProjectEnum.ProjectStatus.close.getValue());
            projectService.updateProject(project);
        }else {
            logger.info("该会话没有找到相关项目 : " + chatCallBackDTO.getChatId());
        }

    }

    /**
     * 同步OA项目成员和钉钉群成员
     */
    @Override
    @Transactional
    public void synProjectChat(String chatId){

        try {
            //这里稍微延时调用保证执行顺序
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Project project = projectService.selectProjectByChatId(chatId);
        if (project != null && !project.getStatus().equals(ProjectEnum.ProjectStatus.close.getValue())){

            OapiChatGetResponse oapiChatGetResponse = dingChatApi.get(chatId);
            if (oapiChatGetResponse.isSuccess()){
                //获取项目的所有成员
                Example example = new Example(ProjectEmp.class);
                example.createCriteria().andEqualTo("projectId",project.getId())
                        .andEqualTo("delFlag","0");
                List<ProjectEmp> projectEmps = projectEmpMapper.selectByExample(example);
                Set<String> collect = projectEmps.stream().map(projectEmp -> {
                    SysUser sysUser = sysUserService.selectUserById(projectEmp.getEmpId());
                    return hrEmpService.getDingUserId(sysUser,true);
                }).filter(dingUserId -> StringUtils.isNotEmpty(dingUserId)).collect(Collectors.toSet());

                List<String> useridlist = oapiChatGetResponse.getChatInfo().getUseridlist();
                //获取钉钉群不包含的项目成员，需要添加到钉钉群
                Set<String> pEmps = collect.stream()
                        .filter(dingUserId -> !useridlist.contains(dingUserId)).collect(Collectors.toSet());
                logger.info("添加到钉钉群:" + Arrays.toString(pEmps.toArray()));
                OapiChatUpdateRequest oapiChatUpdateRequest = new OapiChatUpdateRequest();
                oapiChatUpdateRequest.setChatid(chatId);
                oapiChatUpdateRequest.setOwnerType("emp");
                oapiChatUpdateRequest.setAddUseridlist(pEmps.stream().collect(Collectors.toList()));
                oapiChatUpdateRequest.setManagementType(oapiChatGetResponse.getChatInfo().getManagementType());
                oapiChatUpdateRequest.setValidationType(oapiChatGetResponse.getChatInfo().getValidationType());
                dingChatApi.update(oapiChatUpdateRequest);



                //获取项目成员不包含的钉钉群成员，需要添加到OA表
                Set<String> dEmps = useridlist.stream()
                        .filter(dingUserId -> !collect.contains(dingUserId)).collect(Collectors.toSet());
                //获取userId
                Set<String> empIds = dEmps.stream().map(deingUserId -> {
                    SysUser sysUserByDingUserId = hrEmpService.getSysUserByDingUserId(deingUserId,true);
                    return sysUserByDingUserId == null ? null : sysUserByDingUserId.getUserId()+"";
                }).filter(dingUserId -> StringUtils.isNotEmpty(dingUserId)).collect(Collectors.toSet());
                logger.info("添加到OA表:" + Arrays.toString(empIds.toArray()));
                ProjectEmp projectEmp = new ProjectEmp();
                projectEmp.setProjectId(project.getId());

                //这里先标记为admin
                SysUser admin = sysUserService.selectUserById(1L);
                projectEmp.setCreateId(admin.getUserId());
                projectEmp.setCreateBy(admin.getLoginName());
                projectEmp.setCreateUserName(admin.getUserName());
                projectEmp.setRemark("回调时数据做强制同步");
                insertProjectEmps(projectEmp,empIds);

            }

        }
    }

    @Override
    public List<ProjectEmp> selectByExample(Example example) {
        return projectEmpMapper.selectByExample(example);
    }

    @Override
    public void resetEmpToProject(Set<Long> empIds) {
        projectEmpMapper.resetEmpToProject(empIds);
    }


}
