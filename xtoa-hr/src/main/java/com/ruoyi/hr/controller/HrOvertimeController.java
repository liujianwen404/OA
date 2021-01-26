package com.ruoyi.web.controller.hr;

import com.ruoyi.base.domain.HrLeave;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;

import java.util.Date;
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
import com.ruoyi.base.domain.HrOvertime;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 加班申请Controller
 * 
 * @author liujianwen
 * @date 2020-06-11
 */
@Controller
@RequestMapping("/hr/overtime")
public class HrOvertimeController extends BaseController
{
    private String prefix = "hr/overtime";

    @Autowired
    private IHrOvertimeService hrOvertimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private HistoryService historyService;

    @RequiresPermissions("hr:overtime:view")
    @GetMapping()
    public String overtime(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/overtime";
    }

    /**
     * 查询加班申请列表
     */
    @RequiresPermissions("hr:overtime:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrOvertime hrOvertime)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrOvertime.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrOvertime> list = hrOvertimeService.selectHrOvertimeList(hrOvertime);
        return getDataTable(list);
    }

    /**
     * 导出加班申请列表
     */
    @RequiresPermissions("hr:overtime:export")
    @Log(title = "加班申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrOvertime hrOvertime)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrOvertime.setCreateId(ShiroUtils.getUserId());
//        }
        List<HrOvertime> list = hrOvertimeService.selectOvertimeManageList(hrOvertime);
        ExcelUtil<HrOvertime> util = new ExcelUtil<HrOvertime>(HrOvertime.class);
        return util.exportExcel(list, "overtime");
    }

    /**
     * 新增加班申请
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存加班申请
     */
    @RequiresPermissions("hr:overtime:add")
    @Log(title = "加班申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrOvertime hrOvertime)
    {
        return toAjax(hrOvertimeService.insertHrOvertime(hrOvertime));
    }

    /**
     * 修改加班申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrOvertime hrOvertime = hrOvertimeService.selectHrOvertimeById(id);
        mmap.put("hrOvertime", hrOvertime);
        return prefix + "/edit";
    }

    /**
     * 修改保存加班申请
     */
    @RequiresPermissions("hr:overtime:edit")
    @Log(title = "加班申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrOvertime hrOvertime)
    {
        hrOvertime.setApplyTime(new Date());
        return toAjax(hrOvertimeService.updateHrOvertime(hrOvertime));
    }

    /**
     * 删除加班申请
     */
    @RequiresPermissions("hr:overtime:remove")
    @Log(title = "加班申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrOvertimeService.deleteHrOvertimeByIds(ids));
    }

    /**
     * 获取加班时长
     */
    @Log(title = "加班业务", businessType = BusinessType.DELETE)
    @PostMapping( "/getOverTime")
    @ResponseBody
    public AjaxResult leaveTime(HttpServletRequest request) {
        return hrOvertimeService.getOverTimes(request);
    }

    /**
     * 提交申请
     */
    @Log(title = "加班业务", businessType = BusinessType.INSERT)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        HrOvertime hrOvertime = hrOvertimeService.selectHrOvertimeById(id);
        String applyUserId = ShiroUtils.getLoginName();
        return hrOvertimeService.submitApply(hrOvertime, applyUserId);
    }

    /**
     * 加载办理弹窗
     * @param taskId
     * @param mmap
     * @return
     */
    @Log(title = "加载办理弹窗", businessType = BusinessType.OTHER)
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap,
                                   String module, String formPageName,String instanceId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String businessKey ;
        if (task == null){
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            businessKey = historicProcessInstance.getBusinessKey();
        }else {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            businessKey = processInstance.getBusinessKey();
        }
        mmap.put("taskId", taskId);
        HrOvertime hrOvertime = hrOvertimeService.selectHrOvertimeById(new Long(businessKey));
        mmap.put("hrOvertime", hrOvertime);
        return "hr/overtime/overtimeTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @Log(title = "审批流程", businessType = BusinessType.OTHER)
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("preloadHrOvertime") HrOvertime hrOvertime,
                               HttpServletRequest request) {
        return hrOvertimeService.complete(hrOvertime, taskId, request);
    }
    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadHrOvertime")
    public HrOvertime getOvertime(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
        if (id != null) {
            return hrOvertimeService.selectHrOvertimeById(id);
        }
        return new HrOvertime();
    }

    /**
     *撤销
     * @return
     */
    @Log(title = "撤销", businessType = BusinessType.OTHER)
    @RequiresPermissions("hr:overtime:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal( String instanceId,
                              HttpServletRequest request,String message) {
        try {
            return hrOvertimeService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequiresPermissions("hr:overtime:overtimeList")
    @GetMapping("/overtimeList")
    public String overtimeList(ModelMap mmap) {
        return prefix + "/overtimeList";
    }

    /**
     * 查询业务管理列表
     */
    @PostMapping("/overtimeManageList")
    @ResponseBody
    public TableDataInfo overtimeManageList(HrOvertime hrOvertime) {
        startPage();
        List<HrOvertime> list = hrOvertimeService.selectOvertimeManageList(hrOvertime);
        return getDataTable(list);
    }
}
