package com.ruoyi.hr.controller;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import com.ruoyi.base.domain.HrRegular;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;


import com.ruoyi.base.domain.HrQuit;
import com.ruoyi.base.provider.hrService.IHrQuitService;
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
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 离职申请Controller
 * 
 * @author liujianwen
 * @date 2020-05-15
 */
@Controller
@RequestMapping("/hr/quit")
public class HrQuitController extends BaseController
{
    private String prefix = "hr/quit";

    @Autowired
    private IHrQuitService hrQuitService;

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


    @RequiresPermissions("hr:quit:view")
    @GetMapping()
    public String quit(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/quit";
    }

    /**
     * 查询离职申请列表
     */
    @RequiresPermissions("hr:quit:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrQuit hrQuit)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId()) && !ShiroUtils.getSysUser().hrFlag().equals(1)) {
            hrQuit.setCreateBy(ShiroUtils.getLoginName());
        }
        startPage();
        List<HrQuit> list = hrQuitService.selectHrQuitList(hrQuit);
        return getDataTable(list);
    }

    /**
     * 导出离职申请列表
     */
    @RequiresPermissions("hr:quit:export")
    @Log(title = "离职申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrQuit hrQuit)
    {
        List<HrQuit> list = hrQuitService.selectQuitManageList(hrQuit);
        ExcelUtil<HrQuit> util = new ExcelUtil<HrQuit>(HrQuit.class);
        return util.exportExcel(list, "quit");
    }

    /**
     * 新增离职申请
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存离职申请
     */
    @RequiresPermissions("hr:quit:add")
    @Log(title = "离职申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrQuit hrQuit)
    {
        return toAjax(hrQuitService.insertHrQuit(hrQuit));
    }

    /**
     * 修改离职申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrQuit hrQuit = hrQuitService.selectHrQuitById(id);

        mmap.put("hrQuit", hrQuit);
        return prefix + "/edit";
    }

    /**
     * 修改保存离职申请
     */
    @RequiresPermissions("hr:quit:edit")
    @Log(title = "离职申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrQuit hrQuit)
    {
        if (StringUtil.isNotEmpty(hrQuit.getAttachment())){
            String[] split = hrQuit.getAttachment().split(",");

            split = ArrayUtil.filter(split, (Filter<String>) s -> {
                if (StringUtil.isEmpty(s)){
                    return false;
                }
                return true;
            });

            String join = ArrayUtil.join(split, ",");
            hrQuit.setAttachment(join);
        }else {
            hrQuit.setAttachment(null);
        }

        return toAjax(hrQuitService.updateHrQuit(hrQuit));
    }

    /**
     * 删除离职申请
     */
    @RequiresPermissions("hr:quit:remove")
    @Log(title = "离职申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrQuitService.deleteHrQuitByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "离职申请", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        HrQuit hrQuit = hrQuitService.selectHrQuitById(id);
        return hrQuitService.submitApply(hrQuit);
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
        HrQuit hrQuit = hrQuitService.selectHrQuitById(new Long(businessKey));
        mmap.put("hrQuit", hrQuit);
        return "hr/quit/quitTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("preloadQuit") HrQuit hrQuit,
                               HttpServletRequest request) {
           return hrQuitService.complete(hrQuit, taskId, request);
    }

    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadQuit")
    public HrQuit getQuit(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            return hrQuitService.selectHrQuitById(id);
        }
        return new HrQuit();
    }

    /**
     *撤销
     * @return
     */
    @RequiresPermissions("hr:quit:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal( String instanceId,
                              HttpServletRequest request,String message) {
        try {
            return hrQuitService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequiresPermissions("hr:quit:quitList")
    @GetMapping("/quitList")
    public String quitList(ModelMap mmap) {
        return prefix + "/quitList";
    }

    /**
     * 查询业务管理列表
     */
    @PostMapping("/quitManageList")
    @ResponseBody
    public TableDataInfo quitManageList(HrQuit hrQuit) {
        startPage();
        List<HrQuit> list = hrQuitService.selectQuitManageList(hrQuit);
        return getDataTable(list);
    }
}
