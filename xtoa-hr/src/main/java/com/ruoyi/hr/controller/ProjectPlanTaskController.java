package com.ruoyi.hr.controller;



import cn.hutool.core.util.StrUtil;
import com.ruoyi.base.dingTalk.DingMessageApi;
import com.ruoyi.base.dingTalk.dingCallBackDTO.RobotSendMsgDTO;
import com.ruoyi.base.domain.VO.ProjectPlanSelectVO;
import com.ruoyi.base.domain.VO.ProjectPlanTaskVO;
import com.ruoyi.common.enums.ProjectPlanTaskStatus;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.Project;
import com.ruoyi.base.domain.ProjectPlan;
import com.ruoyi.hr.service.*;
import com.ruoyi.hr.utils.enums.ProjectEnum;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.domain.SysUser;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import com.ruoyi.system.domain.vo.UserTreeVo;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.system.service.ISysUserService;
import com.taobao.api.ApiException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.base.domain.ProjectPlanTask;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.annotation.Resource;

/**
 * 项目计划任务Controller
 * 
 * @author xt
 * @date 2020-06-30
 */
@Controller
@RequestMapping("/project/projectPlanTask")
public class ProjectPlanTaskController extends BaseController
{
    private String prefix = "project/projectPlanTask";

    @Resource
    private IProjectPlanTaskService projectPlanTaskService;

    @Resource
    private IProjectOperationService projectOperationService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IProjectPlanService projectPlanService;
    @Resource
    private IProjectService projectService;

    @Resource
    private HrEmpService hrEmpService;

    @Resource
    private DingMessageApi dingMessageApi;

    @Resource
    private ISysDictTypeService dictTypeService;

    @RequiresPermissions("project:projectPlanTask:view")
    @GetMapping("/projectPlanTask")
    public String projectPlanTask(ModelMap modelMap)
    {
        modelMap.put("projectSelectList", projectService.getProjectSelectList());
        modelMap.put("projectPlanSelectList", projectPlanService.getProjectPlanSelectList(null));
        return prefix + "/projectPlanTask";
    }

    @PostMapping("/getProjectPlanSelectList")
    @ResponseBody
    public List<ProjectPlanSelectVO> projectPlanTask(Long projectId)
    {
        List<ProjectPlanSelectVO> list=projectPlanService.getProjectPlanSelectList(projectId);

        return list;
    }

    /**
     * 查询项目计划任务列表
     */
//    @RequiresPermissions("project:projectPlanTask:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ProjectPlanTask projectPlanTask)
    {
        /*if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            projectPlanTask.setCreateId(ShiroUtils.getUserId());
        }*/
        startPage();
        List<ProjectPlanTaskVO> list = projectPlanTaskService.selectProjectPlanTaskList(projectPlanTask);
        return getDataTable(list);
    }

    /**
     * 导出项目计划任务列表
     */
//    @RequiresPermissions("project:projectPlanTask:export")
    @Log(title = "项目计划任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProjectPlanTask projectPlanTask)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            projectPlanTask.setCreateId(ShiroUtils.getUserId());
        }
        List<ProjectPlanTaskVO> list = projectPlanTaskService.selectProjectPlanTaskList(projectPlanTask);
        ExcelUtil<ProjectPlanTaskVO> util = new ExcelUtil<>(ProjectPlanTaskVO.class);
        return util.exportExcel(list, "projectPlanTask");
    }

    /**
     * 新增项目计划任务
     */
    @GetMapping("/add")
    public String add(Long projectId,Long projectPlanId,ModelMap modelMap,Integer type)
    {
        modelMap.put("projectId",projectId);
        if(!Objects.isNull(projectPlanId)) {
            modelMap.put("projectPlanId", projectPlanId);
        }
        modelMap.put("projectSelectList", projectService.getProjectSelectList());
        modelMap.put("projectPlanSelectList", projectPlanService.getProjectPlanSelectList(null));
       /* Project project = projectService.selectProjectById(projectId);
        ProjectPlan projectPlan = projectPlanService.selectProjectPlanById(projectPlanId);
        modelMap.put("addFlag",projectPlan.getEmpId().equals(ShiroUtils.getUserId())
                && project.getStatus().equals(ProjectEnum.ProjectStatus.creation.getValue())
                && projectPlan.getStatus().equals(ProjectEnum.ProjectStatus.creation.getValue()));*/
//        modelMap.put("userId",ShiroUtils.getUserId());
        modelMap.put("type",type);
        return prefix + "/add";
    }

    /**
     * 新增保存项目计划任务
     */
//    @RequiresPermissions("project:projectPlanTask:add")
    @Log(title = "项目计划任务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ProjectPlanTask projectPlanTask)
    {
        return toAjax(projectPlanTaskService.insertProjectPlanTask(projectPlanTask));
    }

    /**
     * 修改项目计划任务
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ProjectPlanTask projectPlanTask = projectPlanTaskService.selectProjectPlanTaskById(id);
        mmap.put("projectPlanTask", projectPlanTask);
        mmap.put("projectSelectList", projectService.getProjectSelectList());
        mmap.put("projectPlanSelectList", projectPlanService.getProjectPlanSelectList(null));
        SysUser sysUser = new SysUser();
        sysUser.setUserId(projectPlanTask.getEmpId());

        mmap.put("empUser", new UserTreeVo());
        List<UserTreeVo> userTreeVos = sysUserService.getUserTreeVos(sysUser);
        for (UserTreeVo userTreeVo : userTreeVos) {
            if (userTreeVo.getId().equals(projectPlanTask.getEmpId())){
                mmap.put("empUser", userTreeVo);
            }
        }


        return prefix + "/edit";
    }

    /**
     * 修改保存项目计划任务
     */
//    @RequiresPermissions("project:projectPlanTask:edit")
    @Log(title = "修改项目计划任务", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    public AjaxResult editSave(ProjectPlanTask projectPlanTask) throws ApiException {

        ProjectPlanTask projectPlanTaskOld = projectPlanTaskService.selectProjectPlanTaskById(projectPlanTask.getId());
        Boolean isChangeEmp = false;
        Long empIdOld = null;
        //是否更换了负责人
        if (!Objects.equals(projectPlanTaskOld.getEmpId(),projectPlanTask.getEmpId())){
            empIdOld = projectPlanTaskOld.getEmpId();
            isChangeEmp = true;
        }

        //判斷是否改動了狀態
        if(!Objects.equals(projectPlanTaskOld.getStatus(),projectPlanTask.getStatus())) {
            if (Objects.equals(ProjectPlanTaskStatus.PLANNED.getCode(), projectPlanTask.getStatus()) || Objects.equals(ProjectPlanTaskStatus.RELEASED.getCode(), projectPlanTask.getStatus())) {
                //获取项目群组会话id
                String chatId=projectService.queryProjectChatIdById(projectPlanTask.getProjectId());
                if(StringUtils.isNotBlank(chatId)) {
                    //推送消息到钉钉群组
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                    SysUser user = sysUserService.selectUserById(projectPlanTask.getEmpId());
                    String dingUserId = hrEmpService.getDingUserId(user, false);
                   Project project= projectService.selectProjectById(projectPlanTask.getProjectId());
                    List<SysDictData> sysDictData = dictTypeService.selectDictDataByType("sys_project_task_status");
                    AtomicReference<String> statusName= new AtomicReference<>("");
                    sysDictData.stream().forEach(
                        (e) ->{
                            if(e.getDictValue() != null && e.getDictValue().equals(String.valueOf(projectPlanTask.getStatus()))){
                                statusName.set(e.getDictLabel());
                            }
                    });

                    RobotSendMsgDTO robotSendMsgDTO = RobotSendMsgDTO.builder().title(projectPlanTask.getName()).empName(projectPlanTask.getEmpName()).projectName(project.getName())
                            .userId(dingUserId).startTime(sdf.format(projectPlanTask.getStartTime())).endTime(sdf.format(projectPlanTask.getEndTime())).chatId(chatId).status(statusName.toString()).build();

                    dingMessageApi.sendTextToGroupMsg(robotSendMsgDTO);
                }
            }
         }

        int i = projectPlanTaskService.updateProjectPlanTask(projectPlanTask);
        if (i > 0){
            String format =
                    StrUtil.format("{} 编辑了项目计划任务 ：{}",
                            ShiroUtils.getSysUser().getUserName(),
                            projectPlanTask.getName());
            projectOperationService.addProjectOperationByProjectPlanTask(projectPlanTask,
                    ProjectEnum.ProjectOperation.edit.getValue(),format);

            if (isChangeEmp){
                //是更换了负责人,调整钉钉待办
                SysUser sysUser = sysUserService.selectUserById(empIdOld);
                projectPlanTaskService.updateWorkRecord(hrEmpService.getDingUserId(sysUser,false),projectPlanTaskOld.getRecordId());
                projectPlanTaskService.addWorkRecord(projectPlanTask);

            }

        }
        return toAjax(i);
    }

    /**
     * 删除项目计划任务
     */
//    @RequiresPermissions("project:projectPlanTask:remove")
    @Log(title = "项目计划任务", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult removeForClose(String ids)
    {
        try {
            return projectPlanTaskService.removeForClose(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }


    @GetMapping("/projectPlanTaskInfo")
    public String projectPlanTaskInfo(Long projectId,Long projectPlanId,ModelMap modelMap,Integer type)
    {
        modelMap.put("projectId",projectId);
        modelMap.put("projectPlanId",projectPlanId);
        Project project = projectService.selectProjectById(projectId);
        ProjectPlan projectPlan = projectPlanService.selectProjectPlanById(projectPlanId);
        modelMap.put("addFlag",projectPlan.getEmpId().equals(ShiroUtils.getUserId())
                && project.getStatus().equals(ProjectEnum.ProjectStatus.creation.getValue())
                && projectPlan.getStatus().equals(ProjectEnum.ProjectStatus.creation.getValue()));
        modelMap.put("userId",ShiroUtils.getUserId());
        modelMap.put("projectPlan",projectPlan);
        modelMap.put("type",type);
        return prefix + "/projectPlanTaskInfo";
    }

    @RequestMapping("/finishTask")
    @ResponseBody
    public AjaxResult finishTask(Long id)
    {
        try {
            return projectPlanTaskService.finishTask(id);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("操作失败");
        }
    }

}
