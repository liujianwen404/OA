package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.FinancePayment;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.base.domain.TTestProcess;
import com.ruoyi.hr.service.ITTestProcessService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 测试流程单Controller
 * 
 * @author xt
 * @date 2020-11-10
 */
@Controller
@RequestMapping("/hr/test")
public class TTestProcessController extends BaseController
{
    private String prefix = "hr/test";

    @Autowired
    private ITTestProcessService tTestProcessService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessHandleService processHandleService;

    @GetMapping()
    public String process(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/process";
    }

    /**
     * 查询测试流程单列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TTestProcess tTestProcess)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            tTestProcess.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<TTestProcess> list = tTestProcessService.selectTTestProcessList(tTestProcess);
        return getDataTable(list);
    }

    /**
     * 导出测试流程单列表
     */
    @Log(title = "测试流程单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TTestProcess tTestProcess)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            tTestProcess.setCreateId(ShiroUtils.getUserId());
        }
        List<TTestProcess> list = tTestProcessService.selectTTestProcessList(tTestProcess);
        ExcelUtil<TTestProcess> util = new ExcelUtil<TTestProcess>(TTestProcess.class);
        return util.exportExcel(list, "process");
    }

    /**
     * 新增测试流程单
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存测试流程单
     */
    @Log(title = "测试流程单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TTestProcess tTestProcess)
    {
        return toAjax(tTestProcessService.insertTTestProcess(tTestProcess));
    }

    /**
     * 修改测试流程单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TTestProcess tTestProcess = tTestProcessService.selectTTestProcessById(id);
        mmap.put("tTestProcess", tTestProcess);
        return prefix + "/edit";
    }

    /**
     * 修改保存测试流程单
     */
    @Log(title = "测试流程单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TTestProcess tTestProcess)
    {
        return toAjax(tTestProcessService.updateTTestProcess(tTestProcess));
    }

    /**
     * 删除测试流程单
     */
    @Log(title = "测试流程单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tTestProcessService.deleteTTestProcessByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "测试流程", businessType = BusinessType.SUBMIT)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        TTestProcess tTestProcess = tTestProcessService.selectTTestProcessById(id);
        return tTestProcessService.submitApply(tTestProcess, ShiroUtils.getLoginName());
    }

    /**
     * 加载办理弹窗
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap,
                                   String module, String formPageName,String instanceId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String businessKey;
        if (task == null){
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            businessKey = historicProcessInstance.getBusinessKey();
        }else {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            businessKey = processInstance.getBusinessKey();
        }
        mmap.put("taskId", taskId);
        TTestProcess tTestProcess = tTestProcessService.selectTTestProcessById(new Long(businessKey));
        mmap.put("tTestProcess", tTestProcess);
        return prefix + "/testTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @Log(title = "测试流程", businessType = BusinessType.AUDIT)
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("preloadTTestProcess")TTestProcess tTestProcess, HttpServletRequest request) {
        tTestProcess.setTaskId(taskId);
        return tTestProcessService.complete(tTestProcess, request);
    }

    /**
     * 自动绑定页面字段，这里必须通过id去数据库里查询，因为有很多字段页面没有带过来，导致流程节点会出问题
     */
    @ModelAttribute("preloadTTestProcess")
    public TTestProcess getTTestProcess(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
        if (id != null) {
            return tTestProcessService.selectTTestProcessById(id);
        }
        return new TTestProcess();
    }

    /**
     *撤销
     * @return
     */
    @Log(title = "测试流程", businessType = BusinessType.REPEAL)
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal( String instanceId,
                              HttpServletRequest request,String message) {
        try {
            return tTestProcessService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }
}
