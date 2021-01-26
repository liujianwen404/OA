package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.HrOvertime;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrBusinessTripSon;
import com.ruoyi.base.domain.HrQuit;
import com.ruoyi.hr.service.IHrBusinessTripSonService;
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
import com.ruoyi.base.domain.HrBusinessTrip;
import com.ruoyi.hr.service.IHrBusinessTripService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 出差申请Controller
 * 
 * @author liujianwen
 * @date 2020-06-30
 */
@Controller
@RequestMapping("/hr/businessTrip")
public class HrBusinessTripController extends BaseController
{
    private String prefix = "hr/businessTrip";

    @Autowired
    private IHrBusinessTripService hrBusinessTripService;

    @Autowired
    private IHrBusinessTripSonService hrBusinessTripSonService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @RequiresPermissions("hr:businessTrip:view")
    @GetMapping()
    public String trip(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/trip";
    }

    /**
     * 查询出差申请列表
     */
    @RequiresPermissions("hr:businessTrip:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrBusinessTrip hrBusinessTrip)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrBusinessTrip.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrBusinessTrip> list = hrBusinessTripService.selectHrBusinessTripList(hrBusinessTrip);
        return getDataTable(list);
    }

    /**
     * 导出出差申请列表
     */
    @RequiresPermissions("hr:businessTrip:export")
    @Log(title = "出差申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrBusinessTrip hrBusinessTrip)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrBusinessTrip.setCreateId(ShiroUtils.getUserId());
//        }
        List<HrBusinessTrip> list = hrBusinessTripService.selectBusinessTripManageList(hrBusinessTrip);
        ExcelUtil<HrBusinessTrip> util = new ExcelUtil<HrBusinessTrip>(HrBusinessTrip.class);
        return util.exportExcel(list, "trip");
    }

    /**
     * 新增出差申请
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存出差申请
     */
    @RequiresPermissions("hr:businessTrip:add")
    @Log(title = "出差申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrBusinessTrip hrBusinessTrip, HttpServletRequest request)
    {
        return toAjax(hrBusinessTripService.insertHrBusinessTrip(hrBusinessTrip,request));
    }

    /**
     * 修改出差申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrBusinessTrip hrBusinessTrip = hrBusinessTripService.selectHrBusinessTripById(id);
        HrBusinessTripSon hrBusinessTripSon = new HrBusinessTripSon();
        hrBusinessTripSon.setParentId(id);
        List<HrBusinessTripSon> hrBusinessTripSons = hrBusinessTripSonService.selectHrBusinessTripSonList(hrBusinessTripSon);
        mmap.put("hrBusinessTrip", hrBusinessTrip);
        mmap.put("hrBusinessTripSons", hrBusinessTripSons);
        return prefix + "/edit";
    }
    /**
     * 提交流程后的表单数据
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrBusinessTrip hrBusinessTrip = hrBusinessTripService.selectHrBusinessTripById(id);
        HrBusinessTripSon hrBusinessTripSon = new HrBusinessTripSon();
        hrBusinessTripSon.setParentId(id);
        List<HrBusinessTripSon> hrBusinessTripSons = hrBusinessTripSonService.selectHrBusinessTripSonList(hrBusinessTripSon);
        mmap.put("hrBusinessTrip", hrBusinessTrip);
        mmap.put("hrBusinessTripSons", hrBusinessTripSons);
        return prefix + "/detail";
    }

    /**
     * 修改保存出差申请
     */
    @RequiresPermissions("hr:businessTrip:edit")
    @Log(title = "出差申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrBusinessTrip hrBusinessTrip,HttpServletRequest request)
    {
        return toAjax(hrBusinessTripService.updateHrBusinessTrip(hrBusinessTrip,request));
    }

    /**
     * 删除出差申请
     */
    @RequiresPermissions("hr:businessTrip:remove")
    @Log(title = "出差申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrBusinessTripService.deleteHrBusinessTripByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "出差申请", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        HrBusinessTrip hrBusinessTrip = hrBusinessTripService.selectHrBusinessTripById(id);
        String applyUserId = ShiroUtils.getLoginName();
        return hrBusinessTripService.submitApply(hrBusinessTrip, applyUserId);
    }

    /**
     *撤销
     * @return
     */
    @RequiresPermissions("hr:businessTrip:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal( String instanceId,
                              HttpServletRequest request,String message) {
        try {
            return hrBusinessTripService.repeal(instanceId, request, message);
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
        HrBusinessTrip hrBusinessTrip = hrBusinessTripService.selectHrBusinessTripById(new Long(businessKey));
        HrBusinessTripSon hrBusinessTripSon = new HrBusinessTripSon();
        hrBusinessTripSon.setParentId(hrBusinessTrip.getId());
        List<HrBusinessTripSon> hrBusinessTripSons = hrBusinessTripSonService.selectHrBusinessTripSonList(hrBusinessTripSon);
        mmap.put("hrBusinessTrip", hrBusinessTrip);
        mmap.put("hrBusinessTripSons", hrBusinessTripSons);
        return "hr/businessTrip/tripTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("preloadTrip") HrBusinessTrip hrBusinessTrip,
                               HttpServletRequest request) {
        return hrBusinessTripService.complete(hrBusinessTrip, taskId, request);
    }

    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadTrip")
    public HrBusinessTrip getTrip(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            return hrBusinessTripService.selectHrBusinessTripById(id);
        }
        return new HrBusinessTrip();
    }

    @RequiresPermissions("hr:businessTrip:businessTripList")
    @GetMapping("/businessTripList")
    public String businessTripList(ModelMap mmap) {
        return prefix + "/businessTripList";
    }

    /**
     * 查询业务管理列表
     */
    @PostMapping("/businessTripManageList")
    @ResponseBody
    public TableDataInfo businessTripManageList(HrBusinessTrip hrBusinessTrip) {
        startPage();
        List<HrBusinessTrip> list = hrBusinessTripService.selectBusinessTripManageList(hrBusinessTrip);
        return getDataTable(list);
    }
}
