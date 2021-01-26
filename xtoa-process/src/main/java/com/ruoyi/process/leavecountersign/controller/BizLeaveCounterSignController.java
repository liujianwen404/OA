package com.ruoyi.process.leavecountersign.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.process.leave.domain.BizLeaveVo;
import com.ruoyi.process.leavecountersign.service.IBizLeaveCounterSignService;
import com.ruoyi.system.domain.SysUser;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 请假业务Controller
 *
 * @author Xianlu Tech
 * @date 2019-10-11
 */
@Controller
@RequestMapping("/process/leaveCounterSign")
public class BizLeaveCounterSignController extends BaseController {
    private String prefix = "process/leaveCounterSign";

    @Autowired
    private IBizLeaveCounterSignService bizLeaveService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @RequiresPermissions("process:leaveCounterSign:view")
    @GetMapping()
    public String leave(ModelMap mmap) {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/leave";
    }

    /**
     * 查询请假业务列表
     */
    @RequiresPermissions("process:leaveCounterSign:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizLeaveVo bizLeave) {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            bizLeave.setCreateBy(ShiroUtils.getLoginName());
        }
        startPage();
        List<BizLeaveVo> list = bizLeaveService.selectBizLeaveList(bizLeave);
        return getDataTable(list);
    }

    /**
     * 导出请假业务列表
     */
    @RequiresPermissions("process:leaveCounterSign:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizLeaveVo bizLeave) {
        List<BizLeaveVo> list = bizLeaveService.selectBizLeaveList(bizLeave);
        ExcelUtil<BizLeaveVo> util = new ExcelUtil<BizLeaveVo>(BizLeaveVo.class);
        return util.exportExcel(list, "leave");
    }

    /**
     * 新增请假业务
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存请假业务
     */
    @RequiresPermissions("process:leaveCounterSign:add")
    @Log(title = "请假业务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizLeaveVo bizLeave) {
        Long userId = ShiroUtils.getUserId();
        if (SysUser.isAdmin(userId)) {
            return error("提交申请失败：不允许管理员提交申请！");
        }
        return toAjax(bizLeaveService.insertBizLeave(bizLeave));
    }

    @GetMapping("/selectVerifyUser/{id}")
    public String selectVerifyUser(@PathVariable("id") Long id, ModelMap mmap) {
        List<Group> groups = identityService.createGroupQuery().list();
        Map<String, List<User>> usersByGroup = new HashMap<String, List<User>>(groups.size());
        for (Group group : groups) {
            List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
            usersByGroup.put(group.getName(), users);
        }
        mmap.put("usersByGroup", usersByGroup);
        mmap.put("id", id);
        return prefix + "/selectVerifyUser";
    }

    /**
     * 修改请假业务
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        BizLeaveVo bizLeave = bizLeaveService.selectBizLeaveById(id);
        mmap.put("bizLeave", bizLeave);
        return prefix + "/edit";
    }

    /**
     * 修改保存请假业务
     */
    @RequiresPermissions("process:leaveCounterSign:edit")
    @Log(title = "请假业务", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizLeaveVo bizLeave) {
        return toAjax(bizLeaveService.updateBizLeave(bizLeave));
    }

    /**
     * 删除请假业务
     */
    @RequiresPermissions("process:leaveCounterSign:remove")
    @Log(title = "请假业务", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizLeaveService.deleteBizLeaveByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "请假业务", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id, HttpServletRequest request, @RequestParam("users[]") String[] users) {
        BizLeaveVo leave = bizLeaveService.selectBizLeaveById(id);
        String applyUserId = ShiroUtils.getLoginName();

        Map<String, Object> variables = new HashMap<String, Object>();
        Enumeration<String> parameterNames = request.getParameterNames();
        try {
            if (users != null && users.length > 0) {
                Object value =  Arrays.asList(users);
                variables.put("users", value);
            }
            bizLeaveService.submitApplyByCounterSign(leave, applyUserId, variables);
            return success();
        } catch (Exception e) {
            logger.error("error on submitApply leaveId {}, variables={}", new Object[]{leave.getId(), variables, e});
            return error("提交申请失败");
        }

    }

    @RequiresPermissions("process:leaveCounterSign:todoView")
    @GetMapping("/leaveTodo")
    public String todoView() {
        return prefix + "/leaveTodo";
    }

    /**
     * 我的待办列表
     * @param bizLeave
     * @return
     */
    @RequiresPermissions("process:leaveCounterSign:taskList")
    @PostMapping("/taskList")
    @ResponseBody
    public TableDataInfo taskLlist(BizLeaveVo bizLeave) {
        startPage();
        List<BizLeaveVo> list = bizLeaveService.findTodoTasks(bizLeave, ShiroUtils.getLoginName());
        return getDataTable(list);
    }

    /**
     * 加载审批弹窗
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        BizLeaveVo bizLeave = bizLeaveService.selectBizLeaveById(new Long(processInstance.getBusinessKey()));
        mmap.put("bizLeave", bizLeave);
        mmap.put("taskId", taskId);
        String verifyName = task.getTaskDefinitionKey().substring(0, 1).toUpperCase() + task.getTaskDefinitionKey().substring(1);
        return prefix + "/task" + verifyName;
    }

    /**
     * 完成任务
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @RequestParam(value = "saveEntity", required = false) String saveEntity,
                           @ModelAttribute("preloadLeave") BizLeaveVo leave, HttpServletRequest request) {
        boolean saveEntityBoolean = BooleanUtils.toBoolean(saveEntity);
        Map<String, Object> variables = new HashMap<String, Object>();
        Enumeration<String> parameterNames = request.getParameterNames();
        String comment = null;          // 批注
        try {
            while (parameterNames.hasMoreElements()) {
                String parameterName = (String) parameterNames.nextElement();
                if (parameterName.startsWith("p_")) {
                    // 参数结构：p_B_name，p为参数的前缀，B为类型，name为属性名称
                    String[] parameter = parameterName.split("_");
                    if (parameter.length == 3) {
                        String paramValue = request.getParameter(parameterName);
                        Object value = paramValue;
                        if (parameter[1].equals("B")) {
                            value = BooleanUtils.toBoolean(paramValue);
                        } else if (parameter[1].equals("DT")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            value = sdf.parse(paramValue);
                        } else if (parameter[1].equals("COM")) {
                            comment = paramValue;
                        }
                        variables.put(parameter[2], value);
                    } else {
                        throw new RuntimeException("invalid parameter for activiti variable: " + parameterName);
                    }
                }
            }
            if (StringUtils.isNotEmpty(comment)) {
                identityService.setAuthenticatedUserId(ShiroUtils.getLoginName());
                taskService.addComment(taskId, leave.getInstanceId(), comment);
            }
            bizLeaveService.complete(leave, saveEntityBoolean, taskId, variables);
            return success("任务已完成");
        } catch (Exception e) {
            logger.error("error on complete task {}, variables={}", new Object[]{taskId, variables, e});
            return error("完成任务失败");
        }
    }

    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadLeave")
    public BizLeaveVo getLeave(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
        if (id != null) {
            return bizLeaveService.selectBizLeaveById(id);
        }
        return new BizLeaveVo();
    }

    @RequiresPermissions("process:leaveCounterSign:doneView")
    @GetMapping("/leaveDone")
    public String doneView() {
        return prefix + "/leaveDone";
    }

    /**
     * 我的已办列表
     * @param bizLeave
     * @return
     */
    @RequiresPermissions("process:leaveCounterSign:taskDoneList")
    @PostMapping("/taskDoneList")
    @ResponseBody
    public TableDataInfo taskDoneList(BizLeaveVo bizLeave) {
        startPage();
        List<BizLeaveVo> list = bizLeaveService.findDoneTasks(bizLeave, ShiroUtils.getLoginName());
        return getDataTable(list);
    }

}
