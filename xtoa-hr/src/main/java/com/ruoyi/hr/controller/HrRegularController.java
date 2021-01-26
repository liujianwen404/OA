package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.HrOvertime;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrRegular;
import com.ruoyi.base.provider.hrService.IHrRegularService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 转正申请Controller
 * 
 * @author liujianwen
 * @date 2020-05-15
 */
@Controller
@RequestMapping("/hr/regular")
public class HrRegularController extends BaseController
{
    private String prefix = "hr/regular";

    @Autowired
    private IHrRegularService hrRegularService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private HistoryService historyService;

    @RequiresPermissions("hr:regular:view")
    @GetMapping()
    public String regular(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/regular";
    }

    /**
     * 查询申请列表
     */
    @RequiresPermissions("hr:regular:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrRegular hrRegular)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId()) && !ShiroUtils.getSysUser().hrFlag().equals(1)) {
            hrRegular.setCreateBy(ShiroUtils.getLoginName());
        }
        startPage();
        List<HrRegular> list = hrRegularService.selectHrRegularList(hrRegular);
        return getDataTable(list);
    }

    /**
     * 导出申请列表
     */
    @RequiresPermissions("hr:regular:export")
    @Log(title = "转正申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrRegular hrRegular)
    {
        List<HrRegular> list = hrRegularService.selectRegularManageList(hrRegular);
        ExcelUtil<HrRegular> util = new ExcelUtil<HrRegular>(HrRegular.class);
        return util.exportExcel(list, "regular");
    }

    /**
     * 新增申请
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存申请
     */
    @RequiresPermissions("hr:regular:add")
    @Log(title = "离职申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrRegular hrRegular)
    {
        return toAjax(hrRegularService.insertHrRegular(hrRegular));
    }

    /**
     * 修改申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrRegular hrRegular = hrRegularService.selectHrRegularById(id);
        mmap.put("hrRegular", hrRegular);
        return prefix + "/edit";
    }

    /**
     * 修改保存申请
     */
    @RequiresPermissions("hr:regular:edit")
    @Log(title = "转正申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrRegular hrRegular)
    {
        return toAjax(hrRegularService.updateHrRegular(hrRegular));
    }

    /**
     * 删除离职申请
     */
    @RequiresPermissions("hr:regular:remove")
    @Log(title = "转正申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrRegularService.deleteHrRegularByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "提交转正申请流程", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        HrRegular hrRegular = hrRegularService.selectHrRegularById(id);
        return hrRegularService.submitApply(hrRegular);
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
        HrRegular hrRegular = hrRegularService.selectHrRegularById(new Long(businessKey));
        mmap.put("hrRegular", hrRegular);
        return "hr/regular/regularTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("preloadRegular") HrRegular hrRegular,
                               HttpServletRequest request) {
        return hrRegularService.complete(hrRegular, taskId, request);
    }

    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadRegular")
    public HrRegular getRegular(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
        if (id != null) {
            return hrRegularService.selectHrRegularById(id);
        }
        return new HrRegular();
    }

    /**
     *撤销
     * @return
     */
    @RequiresPermissions("hr:regular:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal( String instanceId,
                              HttpServletRequest request,String message) {
        try {
            return hrRegularService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequiresPermissions("hr:regular:regularList")
    @GetMapping("/regularList")
    public String regularList(ModelMap mmap) {
        return prefix + "/regularList";
    }

    /**
     * 查询业务管理列表
     */
    @PostMapping("/regularManageList")
    @ResponseBody
    public TableDataInfo regularManageList(HrRegular hrRegular) {
        startPage();
        List<HrRegular> list = hrRegularService.selectRegularManageList(hrRegular);
        return getDataTable(list);
    }
}
