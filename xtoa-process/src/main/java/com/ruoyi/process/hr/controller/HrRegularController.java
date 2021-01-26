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
//import com.ruoyi.process.hr.domain.HrRegular;
//import com.ruoyi.process.hr.service.IHrRegularService;
//import com.ruoyi.system.domain.SysUser;
//import org.activiti.engine.IdentityService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
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
// * 转正申请Controller
// *
// * @author liujianwen
// * @date 2020-05-15
// */
//@Controller
//@RequestMapping("/process/hr/regular")
//public class HrRegularController extends BaseController
//{
//    private String prefix = "process/hr/regular";
//
//    @Autowired
//    private IHrRegularService hrRegularService;
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
//    @RequiresPermissions("process:hr:regular:view")
//    @GetMapping()
//    public String regular(ModelMap mmap)
//    {
//        mmap.put("currentUser", ShiroUtils.getSysUser());
//        return prefix + "/regular";
//    }
//
//    /**
//     * 查询申请列表
//     */
//    @RequiresPermissions("process:hr:regular:list")
//    @PostMapping("/list")
//    @ResponseBody
//    public TableDataInfo list(HrRegular hrRegular)
//    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrRegular.setCreateBy(ShiroUtils.getLoginName());
//        }
//        startPage();
//        List<HrRegular> list = hrRegularService.selectHrRegularList(hrRegular);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出申请列表
//     */
//    @RequiresPermissions("process:hr:regular:export")
//    @Log(title = "离职申请", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export(HrRegular hrRegular)
//    {
//        List<HrRegular> list = hrRegularService.selectHrRegularList(hrRegular);
//        ExcelUtil<HrRegular> util = new ExcelUtil<HrRegular>(HrRegular.class);
//        return util.exportExcel(list, "regular");
//    }
//
//    /**
//     * 新增申请
//     */
//    @GetMapping("/add")
//    public String add()
//    {
//        return prefix + "/add";
//    }
//
//    /**
//     * 新增保存申请
//     */
//    @RequiresPermissions("process:hr:regular:add")
//    @Log(title = "离职申请", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(HrRegular hrRegular)
//    {
//        return toAjax(hrRegularService.insertHrRegular(hrRegular));
//    }
//
//    /**
//     * 修改申请
//     */
//    @GetMapping("/edit/{id}")
//    public String edit(@PathVariable("id") Long id, ModelMap mmap)
//    {
//        HrRegular hrRegular = hrRegularService.selectHrRegularById(id);
//        mmap.put("hrRegular", hrRegular);
//        return prefix + "/edit";
//    }
//
//    /**
//     * 修改保存申请
//     */
//    @RequiresPermissions("process:hr:regular:edit")
//    @Log(title = "转正申请", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(HrRegular hrRegular)
//    {
//        return toAjax(hrRegularService.updateHrRegular(hrRegular));
//    }
//
//    /**
//     * 删除离职申请
//     */
//    @RequiresPermissions("process:hr:regular:remove")
//    @Log(title = "转正申请", businessType = BusinessType.DELETE)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids)
//    {
//        return toAjax(hrRegularService.deleteHrRegularByIds(ids));
//    }
//
//    /**
//     * 提交申请
//     */
//    @Log(title = "请假业务", businessType = BusinessType.UPDATE)
//    @PostMapping( "/submitApply")
//    @ResponseBody
//    public AjaxResult submitApply(Long id) {
//        HrRegular hrRegular = hrRegularService.selectHrRegularById(id);
//        String applyUserId = ShiroUtils.getLoginName();
//        hrRegularService.submitApply(hrRegular, applyUserId);
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
//                               @ModelAttribute("preloadRegular") HrRegular hrRegular, HttpServletRequest request) {
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
//                            hrRegular.setAuditStatus("3");
//                            hrRegularService.updateHrRegular(hrRegular);
//                        }
//                    } else {
//                        throw new RuntimeException("invalid parameter for activiti variable: " + parameterName);
//                    }
//                }
//            }
//            if (StringUtils.isNotEmpty(comment)) {
//                identityService.setAuthenticatedUserId(ShiroUtils.getLoginName());
//                taskService.addComment(taskId, hrRegular.getInstanceId(), comment);
//            }
//            //如果是最后一个审批节点同意
//            if(task.getTaskDefinitionKey().equals("hr") && (Boolean) variables.get("approved")){
//                hrRegular.setAuditStatus("2");
//                hrRegularService.updateHrRegular(hrRegular);
//            }
//
//            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
//
//            hrRegularService.complete(hrRegular, saveEntityBoolean, taskId, variables,processInstance.getStartUserId());
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
//    @ModelAttribute("preloadRegular")
//    public HrRegular getLeave(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
//        if (id != null) {
//            return hrRegularService.selectHrRegularById(id);
//        }
//        return new HrRegular();
//    }
//
//    /**
//     *撤销
//     * @return
//     */
//    @RequiresPermissions("process:hr:regular:remove")
//    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public AjaxResult repeal( String instanceId,
//                              HttpServletRequest request,String message) {
//        try {
//            return hrRegularService.repeal(instanceId, request, message);
//        } catch (Exception e) {
//            logger.error("error on complete task {}", new Object[]{instanceId, e});
//            return AjaxResult.error("repeal失败");
//        }
//    }
//}
