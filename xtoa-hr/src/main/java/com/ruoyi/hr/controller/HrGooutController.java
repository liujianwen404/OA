package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.HrOvertime;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrFillClock;
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
import com.ruoyi.base.domain.HrGoout;
import com.ruoyi.hr.service.IHrGooutService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 外出申请Controller
 * 
 * @author liujianwen
 * @date 2020-07-06
 */
@Controller
@RequestMapping("/hr/goout")
public class HrGooutController extends BaseController
{
    private String prefix = "hr/goout";

    @Autowired
    private IHrGooutService hrGooutService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessHandleService processHandleService;

    @RequiresPermissions("hr:goout:view")
    @GetMapping()
    public String goout(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/goout";
    }

    /**
     * 查询外出申请列表
     */
    @RequiresPermissions("hr:goout:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrGoout hrGoout)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrGoout.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrGoout> list = hrGooutService.selectHrGooutList(hrGoout);
        return getDataTable(list);
    }

    /**
     * 导出外出申请列表
     */
    @RequiresPermissions("hr:goout:export")
    @Log(title = "外出申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrGoout hrGoout)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrGoout.setCreateId(ShiroUtils.getUserId());
//        }
        List<HrGoout> list = hrGooutService.selectGooutManageList(hrGoout);
        ExcelUtil<HrGoout> util = new ExcelUtil<HrGoout>(HrGoout.class);
        return util.exportExcel(list, "goout");
    }

    /**
     * 新增外出申请
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存外出申请
     */
    @RequiresPermissions("hr:goout:add")
    @Log(title = "外出申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrGoout hrGoout)
    {
        return toAjax(hrGooutService.insertHrGoout(hrGoout));
    }

    /**
     * 修改外出申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrGoout hrGoout = hrGooutService.selectHrGooutById(id);
        mmap.put("hrGoout", hrGoout);
        return prefix + "/edit";
    }

    /**
     * 修改保存外出申请
     */
    @RequiresPermissions("hr:goout:edit")
    @Log(title = "外出申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrGoout hrGoout)
    {
        return toAjax(hrGooutService.updateHrGoout(hrGoout));
    }

    /**
     * 删除外出申请
     */
    @RequiresPermissions("hr:goout:remove")
    @Log(title = "外出申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrGooutService.deleteHrGooutByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "补卡申请", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        HrGoout hrGoout = hrGooutService.selectHrGooutById(id);
        String applyUserId = ShiroUtils.getLoginName();
        return hrGooutService.submitApply(hrGoout, applyUserId);
    }

    /**
     *撤销
     * @return
     */
    @RequiresPermissions("hr:goout:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        try {
            return hrGooutService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 加载办理弹窗
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap,String instanceId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String businessKey;
        if (task == null){
            HistoricProcessInstance historicProcessInstance =
                    historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            businessKey = historicProcessInstance.getBusinessKey();
        }else {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            businessKey = processInstance.getBusinessKey();
        }
        mmap.put("taskId", taskId);
        HrGoout hrGoout = hrGooutService.selectHrGooutById(new Long(businessKey));
        mmap.put("hrGoout", hrGoout);
        return "hr/goout/gooutTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("preloadGoout") HrGoout hrGoout,
                               HttpServletRequest request) {
        return hrGooutService.complete(hrGoout, taskId, request);
    }

    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadGoout")
    public HrGoout getClock(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            return hrGooutService.selectHrGooutById(id);
        }
        return new HrGoout();
    }

    @RequiresPermissions("hr:goout:gooutList")
    @GetMapping("/gooutList")
    public String gooutList(ModelMap mmap) {
        return prefix + "/gooutList";
    }

    /**
     * 查询业务管理列表
     */
    @PostMapping("/gooutManageList")
    @ResponseBody
    public TableDataInfo gooutManageList(HrGoout hrGoout) {
        startPage();
        List<HrGoout> list = hrGooutService.selectGooutManageList(hrGoout);
        return getDataTable(list);
    }
}
