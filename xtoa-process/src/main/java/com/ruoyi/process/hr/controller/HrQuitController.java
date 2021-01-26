//package com.ruoyi.process.hr.controller;
//
//import com.ruoyi.common.annotation.Log;
//import com.ruoyi.common.core.controller.BaseController;
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.common.core.page.TableDataInfo;
//import com.ruoyi.common.enums.BusinessType;
//import com.ruoyi.common.utils.StringUtils;
//import com.ruoyi.common.utils.poi.ExcelUtil;
//import com.ruoyi.framework.util.ShiroUtils;
//import com.ruoyi.process.hr.domain.HrQuit;
//import com.ruoyi.process.hr.service.IHrQuitService;
//
//import com.ruoyi.system.domain.SysUser;
//import org.activiti.engine.IdentityService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.activiti.engine.task.TaskQuery;
//import org.apache.commons.lang3.BooleanUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.text.SimpleDateFormat;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 离职申请Controller
// *
// * @author liujianwen
// * @date 2020-05-15
// */
//@Controller
//@RequestMapping("/process/hr/quit")
//public class HrQuitController extends BaseController
//{
//    private String prefix = "process/hr/quit";
//
//    @Autowired
//    private IHrQuitService hrQuitService;
//
//    @Autowired
//    private TaskService taskService;
//
//    @Autowired
//    private RuntimeService runtimeService;
//
//    @Autowired
//    private IdentityService identityService;
//
//    @RequiresPermissions("process:hr:quit:view")
//    @GetMapping()
//    public String quit(ModelMap mmap)
//    {
//        mmap.put("currentUser", ShiroUtils.getSysUser());
//        return prefix + "/quit";
//    }
//
//    /**
//     * 查询离职申请列表
//     */
//    @RequiresPermissions("process:hr:quit:list")
//    @PostMapping("/list")
//    @ResponseBody
//    public TableDataInfo list(HrQuit hrQuit)
//    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrQuit.setCreateBy(ShiroUtils.getLoginName());
//        }
//        startPage();
//        List<HrQuit> list = hrQuitService.selectHrQuitList(hrQuit);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出离职申请列表
//     */
//    @RequiresPermissions("process:hr:quit:export")
//    @Log(title = "离职申请", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export(HrQuit hrQuit)
//    {
//        List<HrQuit> list = hrQuitService.selectHrQuitList(hrQuit);
//        ExcelUtil<HrQuit> util = new ExcelUtil<HrQuit>(HrQuit.class);
//        return util.exportExcel(list, "quit");
//    }
//
//    /**
//     * 新增离职申请
//     */
//    @GetMapping("/add")
//    public String add()
//    {
//        return prefix + "/add";
//    }
//
//    /**
//     * 新增保存离职申请
//     */
//    @RequiresPermissions("process:hr:quit:add")
//    @Log(title = "离职申请", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(HrQuit hrQuit)
//    {
//        return toAjax(hrQuitService.insertHrQuit(hrQuit));
//    }
//
//    /**
//     * 修改离职申请
//     */
//    @GetMapping("/edit/{id}")
//    public String edit(@PathVariable("id") Long id, ModelMap mmap)
//    {
//        HrQuit hrQuit = hrQuitService.selectHrQuitById(id);
//        mmap.put("hrQuit", hrQuit);
//        return prefix + "/edit";
//    }
//
//    /**
//     * 修改保存离职申请
//     */
//    @RequiresPermissions("process:hr:quit:edit")
//    @Log(title = "离职申请", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(HrQuit hrQuit)
//    {
//        return toAjax(hrQuitService.updateHrQuit(hrQuit));
//    }
//
//    /**
//     * 删除离职申请
//     */
//    @RequiresPermissions("process:hr:quit:remove")
//    @Log(title = "离职申请", businessType = BusinessType.DELETE)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids)
//    {
//        return toAjax(hrQuitService.deleteHrQuitByIds(ids));
//    }
//
//    /**
//     * 提交申请
//     */
//    @Log(title = "请假业务", businessType = BusinessType.UPDATE)
//    @PostMapping( "/submitApply")
//    @ResponseBody
//    public AjaxResult submitApply(Long id) {
//        HrQuit hrQuit = hrQuitService.selectHrQuitById(id);
//        String applyUserId = ShiroUtils.getLoginName();
//        hrQuitService.submitApply(hrQuit, applyUserId);
//        return success();
//    }
//
//    /**
//     * 完成任务
//     *
//     * @return
//     */
//    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public AjaxResult complete(@PathVariable("taskId") String taskId, @RequestParam(value = "saveEntity", required = false) String saveEntity,
//                               @ModelAttribute("preloadQuit") HrQuit hrQuit, HttpServletRequest request) {
//        boolean saveEntityBoolean = BooleanUtils.toBoolean(saveEntity);
//        Map<String, Object> variables = new HashMap<String, Object>();
//        Enumeration<String> parameterNames = request.getParameterNames();
//        String comment = null;          // 批注
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//        try {
//            while (parameterNames.hasMoreElements()) {
//                String parameterName = (String) parameterNames.nextElement();
//                if (parameterName.startsWith("p_")) {
//                    // 参数结构：p_B_name，p为参数的前缀，B为类型，name为属性名称
//                    String[] parameter = parameterName.split("_");
//                    if (parameter.length == 3) {
//                        String paramValue = request.getParameter(parameterName);
//                        Object value = paramValue;
//                        if (parameter[1].equals("B")) {
//                            value = BooleanUtils.toBoolean(paramValue);
//                        } else if (parameter[1].equals("DT")) {
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                            value = sdf.parse(paramValue);
//                        } else if (parameter[1].equals("COM")) {
//                            comment = paramValue;
//                        }
//                        variables.put(parameter[2], value);
//                        if(parameter[2].equals("approved") && !(Boolean) variables.get("approved")){
//                            //页面拒绝流程后，修改流程状态为拒绝
//                            hrQuit.setAuditStatus("3");
//                            hrQuitService.updateHrQuit(hrQuit);
//                        }
//                    } else {
//                        throw new RuntimeException("invalid parameter for activiti variable: " + parameterName);
//                    }
//                }
//            }
//            if (StringUtils.isNotEmpty(comment)) {
//                identityService.setAuthenticatedUserId(ShiroUtils.getLoginName());
//                taskService.addComment(taskId, hrQuit.getInstanceId(), comment);
//            }
//            if(task.getTaskDefinitionKey().equals("hr2") && (Boolean) variables.get("approved")){//如果是最后一个审批节点同意
//                hrQuit.setAuditStatus("2");
//                hrQuitService.updateHrQuit(hrQuit);
//            }
//
//            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
//
//            hrQuitService.complete(hrQuit, saveEntityBoolean, taskId, variables,processInstance.getStartUserId());
//            return success("任务已完成");
//        } catch (Exception e) {
//            logger.error("error on complete task {}, variables={}", new Object[]{taskId, variables, e});
//            return error("完成任务失败");
//        }
//    }
//
//    /**
//     * 自动绑定页面字段
//     */
//    @ModelAttribute("preloadQuit")
//    public HrQuit getLeave(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
//        if (id != null) {
//            return hrQuitService.selectHrQuitById(id);
//        }
//        return new HrQuit();
//    }
//
//    /**
//     *撤销
//     * @return
//     */
//    @RequiresPermissions("process:hr:quit:remove")
//    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public AjaxResult repeal( String instanceId,
//                              HttpServletRequest request,String message) {
//        try {
//            return hrQuitService.repeal(instanceId, request, message);
//        } catch (Exception e) {
//            logger.error("error on complete task {}", new Object[]{instanceId, e});
//            return AjaxResult.error("repeal失败");
//        }
//    }
//}
