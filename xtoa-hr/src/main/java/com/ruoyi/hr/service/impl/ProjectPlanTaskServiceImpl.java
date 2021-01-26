package com.ruoyi.hr.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.request.OapiWorkrecordAddRequest.FormItemVo;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.dingtalk.api.response.OapiWorkrecordUpdateResponse;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.base.dingTalk.DingMessageApi;
import com.ruoyi.base.dingTalk.DingWorkRecordApi;
import com.ruoyi.base.domain.DingdingMsg;
import com.ruoyi.base.domain.Project;
import com.ruoyi.base.domain.ProjectPlan;
import com.ruoyi.base.domain.ProjectPlanTask;
import com.ruoyi.base.domain.VO.ProjectPlanTaskVO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.ProjectPlanTaskMapper;
import com.ruoyi.hr.service.*;
import com.ruoyi.base.utils.DingdingMsgEnum;
import com.ruoyi.hr.utils.enums.ProjectEnum;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目迭代任务Service业务层处理
 * 
 * @author xt
 * @date 2020-06-30
 */
@Service
public class ProjectPlanTaskServiceImpl implements IProjectPlanTaskService 
{

    private static final Logger logger = LoggerFactory.getLogger(ProjectPlanTaskServiceImpl.class);

    @Resource
    private ProjectPlanTaskMapper projectPlanTaskMapper;

    @Autowired
    private IProjectPlanService projectPlanService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IProjectOperationService projectOperationService;

    @Autowired
    private DingWorkRecordApi dingWorkRecordApi;
    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private ISysUserService sysUserService;

    @Value("${dingWorkRecordUrl}")
    private String dingWorkRecordUrl;


    @Autowired
    private DingConfig dingConfig;

    @Autowired
    private DingMessageApi dingMessageApi;

    @Autowired
    private IDingdingMsgService dingdingMsgService;

    /**
     * 查询项目迭代任务
     * 
     * @param id 项目迭代任务ID
     * @return 项目迭代任务
     */
    @Override
    public ProjectPlanTask selectProjectPlanTaskById(Long id)
    {
        return projectPlanTaskMapper.queryProjectPlanTaskById(id);
    }

    /**
     * 查询项目迭代任务列表
     * 
     * @param projectPlanTask 项目迭代任务
     * @return 项目迭代任务
     */
    @Override
    public List<ProjectPlanTaskVO> selectProjectPlanTaskList(ProjectPlanTask projectPlanTask)
    {

        List<ProjectPlanTaskVO> projectPlanTasksList = projectPlanTaskMapper.selectProjectPlanTaskList(projectPlanTask);

        if(!CollectionUtils.isEmpty(projectPlanTasksList)){
             projectPlanTasksList.stream().forEach(nico->nico.setIntroducerName(hrEmpService.queryEmpNameByEmpId(nico.getIntroducerId())));
        }

        if(StringUtils.isNotBlank(projectPlanTask.getIntroducerName())){
            projectPlanTasksList= projectPlanTasksList.stream().filter(nico->
                        nico.getIntroducerName().contains(projectPlanTask.getIntroducerName())
            ).collect(Collectors.toList());
        }
        return projectPlanTasksList;
    }

    /**
     * 新增项目迭代任务
     * 
     * @param projectPlanTask 项目迭代任务
     * @return 结果
     */
    @Override
    @Transactional
    public int insertProjectPlanTask(ProjectPlanTask projectPlanTask)
    {
        projectPlanTask.setCreateId(ShiroUtils.getUserId());
        projectPlanTask.setCreateBy(ShiroUtils.getLoginName());
        projectPlanTask.setCreateTime(DateUtils.getNowDate());
        int i = projectPlanTaskMapper.insertSelective(projectPlanTask);
        if (i > 0){

            String format =
                    StrUtil.format("{} 为 项目迭代 添加了任务 ：{}",
                            ShiroUtils.getSysUser().getUserName(),
                            projectPlanTask.getName());

            projectOperationService.addProjectOperationByProjectPlanTask
                    (projectPlanTask,ProjectEnum.ProjectOperation.creation.getValue(),format);

            // 发布钉钉代办
            addWorkRecord(projectPlanTask);

        }
        return i;
    }

    @Override
    public void addWorkRecord(ProjectPlanTask projectPlanTask) {
        // 发布钉钉代办
        OapiWorkrecordAddRequest req = new OapiWorkrecordAddRequest();
        SysUser sysUser = sysUserService.selectUserById(projectPlanTask.getEmpId());
        SysUser sysUserCreater = ShiroUtils.getSysUser();
        req.setUserid(hrEmpService.getDingUserId(sysUser,false));
        req.setCreateTime(System.currentTimeMillis());
        Project project = projectService.selectProjectById(projectPlanTask.getProjectId());
//        ProjectPlan projectPlan = projectPlanService.selectProjectPlanById(projectPlanTask.getProjectPlanId());
        req.setTitle(project.getName());
        List<FormItemVo> list2 = new ArrayList<>();
        FormItemVo obj3 = new FormItemVo();
        obj3.setTitle(project.getName());
        obj3.setContent(projectPlanTask.getName());
        list2.add(obj3);
        req.setFormItemList(list2);
        req.setOriginatorUserId(hrEmpService.getDingUserId(sysUserCreater,false));
//        req.setBizId(projectPlanTask.getId()+"");当传了biz_id参数时，每个用户的biz_id不能重复，不利于修改负责人这里不做上传了
        req.setUrl(dingWorkRecordUrl + "?userId=" + projectPlanTask.getEmpId() +
                "&projectPlanTaskId="+projectPlanTask.getId() + "&type="+DingdingMsgEnum.MsgBizType.projectTaskMsg.getValue());
        OapiWorkrecordAddResponse oapiWorkrecordAddResponse = dingWorkRecordApi.add(req);
        if (!DingConfig.isSuccess(oapiWorkrecordAddResponse)){
            if (Objects.equals(oapiWorkrecordAddResponse.getErrorCode(),"854001")){
                throw new BusinessException("每人每天最多收到一条任务名称相同的待办，" +
                        "请修改任务名称解决此问题。" );
            }
            throw new BusinessException("钉钉接口调用异常，请重试操作:" + oapiWorkrecordAddResponse.getErrmsg());
        }
        projectPlanTask.setRecordId(oapiWorkrecordAddResponse.getRecordId());
        projectPlanTaskMapper.updateByPrimaryKeySelective(projectPlanTask);
    }

    @Override
    public OapiWorkrecordUpdateResponse updateWorkRecord(String dingUserId, String recordId ) {
        // 更新(移除)钉钉代办
        OapiWorkrecordUpdateResponse oapiWorkrecordUpdateResponse = dingWorkRecordApi.update(dingUserId, recordId);
        if (!DingConfig.isSuccess(oapiWorkrecordUpdateResponse)){
            throw new BusinessException("钉钉接口调用异常，请重试操作:" + oapiWorkrecordUpdateResponse.getErrmsg());
        }
        return oapiWorkrecordUpdateResponse;
    }

    @Override
    @Transactional
    public AjaxResult finishTask(Long id) {

        ProjectPlanTask projectPlanTask = projectPlanTaskMapper.selectByPrimaryKey(id);
        projectPlanTask.setStatus(ProjectEnum.ProjectStatus.finish.getValue());
        int i = updateProjectPlanTask(projectPlanTask);
        if (i > 0){
            SysUser sysUser = sysUserService.selectUserById(projectPlanTask.getEmpId());
            updateWorkRecord(hrEmpService.getDingUserId(sysUser,false), projectPlanTask.getRecordId());
        }
        return AjaxResult.success();
    }

    @Override
    public List<ProjectPlanTask> seletUnfinishedTask() {
        return projectPlanTaskMapper.seletUnfinishedTask();
    }

    /**
     * 修改项目迭代任务
     * 
     * @param projectPlanTask 项目迭代任务
     * @return 结果
     */
    @Override
    @Transactional
    public int updateProjectPlanTask(ProjectPlanTask projectPlanTask)
    {
        if (projectPlanTask.getUpdateId() == null){
            projectPlanTask.setUpdateId(ShiroUtils.getUserId());
        }
        if (StringUtils.isEmpty(projectPlanTask.getUpdateBy())){
            projectPlanTask.setUpdateBy(ShiroUtils.getLoginName());
        }
        projectPlanTask.setUpdateTime(DateUtils.getNowDate());
        int i = projectPlanTaskMapper.updateByPrimaryKeySelective(projectPlanTask);
        if ( i > 0 ){
            if (Objects.equals(projectPlanTask.getStatus(),ProjectEnum.ProjectStatus.finish.getValue())){
                SysUser sysUser = sysUserService.selectUserById(projectPlanTask.getEmpId());
                String format =
                        StrUtil.format("{} 完成了任务 ：{}",
                                sysUser.getUserName(),
                                projectPlanTask.getName());
                projectOperationService.addProjectOperationByProjectPlanTask(projectPlanTask,
                        ProjectEnum.ProjectOperation.finish.getValue(),format);

            }else {
                SysUser sysUser = sysUserService.selectUserById(projectPlanTask.getEmpId());
                String format =
                        StrUtil.format("{} 编辑了任务 ：{}",
                                sysUser.getUserName(),
                                projectPlanTask.getName());
                projectOperationService.addProjectOperationByProjectPlanTask(projectPlanTask,
                        ProjectEnum.ProjectOperation.edit.getValue(),format);
            }
        }
        return i;
    }

    /**
     * 删除项目迭代任务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteProjectPlanTaskByIds(String ids)
    {
        return projectPlanTaskMapper.deleteProjectPlanTaskByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除项目迭代任务信息
     * 
     * @param id 项目迭代任务ID
     * @return 结果
     */
    @Override
    public int deleteProjectPlanTaskById(Long id)
    {
        return projectPlanTaskMapper.deleteProjectPlanTaskById(id);
    }

    @Override
    @Transactional
    public AjaxResult removeForClose(String ids) {
        String[] strings = Convert.toStrArray(ids);
        for (String string : strings) {
            ProjectPlanTask projectPlanTask = projectPlanTaskMapper.selectByPrimaryKey(Long.parseLong(string));
            if (projectPlanTask != null && Objects.equals(projectPlanTask.getStatus(),ProjectEnum.ProjectStatus.creation.getValue())){
                projectPlanTask.setStatus(ProjectEnum.ProjectStatus.close.getValue());
                int i = updateProjectPlanTask(projectPlanTask);
                if (i > 0){
                    String format =
                            StrUtil.format("{} 关闭了项目迭代任务 ：{}",
                                    ShiroUtils.getSysUser().getUserName(),
                                    projectPlanTask.getName());
                    projectOperationService.addProjectOperationByProjectPlanTask(projectPlanTask,
                            ProjectEnum.ProjectOperation.close.getValue(),format);

                    SysUser sysUser = sysUserService.selectUserById(projectPlanTask.getEmpId());
                    updateWorkRecord( hrEmpService.getDingUserId(sysUser,false),  projectPlanTask.getRecordId() ) ;
                }
            }
        }
        return AjaxResult.success();
    }

    @Override
    public List<Long> selectProjectPlanTaskEmps(Long id) {
        return projectPlanTaskMapper.selectProjectPlanTaskEmps(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendMsg(String img, ProjectPlanTask projectPlanTask) {
        //记录发送钉钉工作通知数据
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        req.setAgentId(dingConfig.getAgentId());
        SysUser sysUser = sysUserService.selectUserById(projectPlanTask.getEmpId());
        req.setUseridList(hrEmpService.getDingUserId(sysUser,false));
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        String msgType = "link";
        msg.setMsgtype(msgType);
        OapiMessageCorpconversationAsyncsendV2Request.Link link = new OapiMessageCorpconversationAsyncsendV2Request.Link();
        link.setPicUrl(img);
        link.setMessageUrl(dingWorkRecordUrl + "?userId=" + projectPlanTask.getEmpId() +
                "&projectPlanTaskId="+projectPlanTask.getId()+ "&type="+DingdingMsgEnum.MsgBizType.projectTaskMsg.getValue());
        link.setText(StrUtil.format("任务待完成提醒,提醒时间：{} \r\n 项目：{},迭代：{}，任务：{}。"
                ,DateUtil.date(),projectPlanTask.getProjectName()
                ,projectPlanTask.getProjectPlanName()
                ,projectPlanTask.getName()));
        link.setTitle("任务提醒");
        msg.setLink(link);
        req.setMsg(msg);
        DingdingMsg dingdingMsg = new DingdingMsg();
        dingdingMsg.setBizType(DingdingMsgEnum.MsgBizType.projectTaskMsg.getValue());
        dingdingMsg.setBizId(projectPlanTask.getId().toString());
        dingdingMsg.setMsg(req.getMsg());
        dingdingMsg.setMsgType(msgType);
        dingdingMsg.setUseridList(req.getUseridList());
        int i = dingdingMsgService.insertDingdingMsg(dingdingMsg);
        if (i > 0){
            //发送钉钉工作通知
            OapiMessageCorpconversationAsyncsendV2Response oapiMessageCorpconversationAsyncsendV2Response = dingMessageApi.asyncsend_v2(req);
            if (!DingConfig.isSuccess(oapiMessageCorpconversationAsyncsendV2Response)){
                throw new BusinessException("通知提醒发送失败：" + projectPlanTask.getId());
            }else {
                dingdingMsg.setTaskId(oapiMessageCorpconversationAsyncsendV2Response.getTaskId().toString());
                dingdingMsgService.updateDingdingMsg(dingdingMsg);
            }
        }
    }

    @Value("${server.port}")
    private String port;

    @Override
    public ProjectPlanTask findById(Long projectPlanTaskId) {
        logger.info("~~~~~~~~~~~~~~~~~~~~~~port:"+port);
        ProjectPlanTask projectPlanTask = selectProjectPlanTaskById(projectPlanTaskId);
        if (projectPlanTask == null){
            return null;
        }
        Project project = projectService.selectProjectById(projectPlanTask.getProjectId());
        ProjectPlan projectPlan = projectPlanService.selectProjectPlanById(projectPlanTask.getProjectPlanId());

        projectPlanTask.setProjectName(project.getName());
        projectPlanTask.setProjectPlanName(projectPlan.getName());
        return projectPlanTask;
    }

    @Override
    @Transactional
    public AjaxResult updateprojectPlanTask(Long projectPlanTaskId) {

        ProjectPlanTask projectPlanTask = selectProjectPlanTaskById(projectPlanTaskId);
        SysUser sysUser = sysUserService.selectUserById(projectPlanTask.getEmpId());
        String dingUserId = hrEmpService.getDingUserId(sysUser, false);
        if (Objects.equals(projectPlanTask.getStatus(),ProjectEnum.ProjectStatus.creation.getValue())){
            projectPlanTask.setStatus(ProjectEnum.ProjectStatus.finish.getValue());
            projectPlanTask.setUpdateId(sysUser.getUserId());
            projectPlanTask.setUpdateBy(sysUser.getLoginName());
            int i = updateProjectPlanTask(projectPlanTask);
            if (i > 0){
                //projectPlanTaskService.updateWorkRecord(dingUserId, projectPlanTask.getRecordId());
            }else {
                logger.info("projectPlanTaskId:"+projectPlanTaskId+":操作失败1");
                return AjaxResult.error("操作失败");
            }
        }
        dingWorkRecordApi.update(dingUserId, projectPlanTask.getRecordId());

        return AjaxResult.success();
    }


}
