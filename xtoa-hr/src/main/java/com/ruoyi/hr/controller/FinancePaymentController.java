package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.DTO.FileDTO;
import com.ruoyi.base.domain.HrOvertime;
import com.ruoyi.base.domain.HrQuit;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ruoyi.base.domain.FinancePayment;
import com.ruoyi.hr.service.IFinancePaymentService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 财务付款流程Controller
 * 
 * @author liujianwen
 * @date 2020-10-26
 */
@Controller
@RequestMapping("/hr/payment")
public class FinancePaymentController extends BaseController
{
    private String prefix = "hr/payment";

    @Autowired
    private IFinancePaymentService financePaymentService;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessHandleService processHandleService;




    @RequiresPermissions("hr:payment:view")
    @GetMapping()
    public String payment(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/payment";
    }

    /**
     * 查询财务付款流程列表
     */
    @RequiresPermissions("hr:payment:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FinancePayment financePayment)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            financePayment.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<FinancePayment> list = financePaymentService.selectFinancePaymentList(financePayment);
        return getDataTable(list);
    }

    /**
     * 导出财务付款流程列表
     */
    @RequiresPermissions("hr:payment:export")
    @Log(title = "财务付款流程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FinancePayment financePayment)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            financePayment.setCreateId(ShiroUtils.getUserId());
        }
        List<FinancePayment> list = financePaymentService.selectFinancePaymentList(financePayment);
        ExcelUtil<FinancePayment> util = new ExcelUtil<FinancePayment>(FinancePayment.class);
        return util.exportExcel(list, "payment");
    }

    /**
     * 导出财务付款流程列表
     */
    @Log(title = "财务付款流程", businessType = BusinessType.UPLOAD)
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(MultipartFile[] files)
    {
        List<FileDTO> list = new ArrayList<>();
        for (int i = 0; i < files.length; i++){
            FileDTO fileDTO = new FileDTO();
            // 上传文件路径
            String filePath = Global.getUploadPath();
            // 上传并返回新文件名称
            String fileName = null;
            try {
                fileName = FileUploadUtils.upload(filePath, files[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = serverConfig.getUrl() + fileName;
            fileDTO.setName(files[i].getOriginalFilename());
            fileDTO.setPath(url);
            list.add(fileDTO);
        }
        return AjaxResult.success(list);
    }

    /**
     * 新增财务付款流程
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存财务付款流程
     */
    @RequiresPermissions("hr:payment:add")
    @Log(title = "财务付款流程", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FinancePayment financePayment)
    {
        return toAjax(financePaymentService.insertFinancePayment(financePayment));
    }

    /**
     * 修改财务付款流程
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        FinancePayment financePayment = financePaymentService.selectFinancePaymentById(id);
        mmap.put("financePayment", financePayment);
        return prefix + "/edit";
    }

    /**
     * 修改保存财务付款流程
     */
    @RequiresPermissions("hr:payment:edit")
    @Log(title = "财务付款流程", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FinancePayment financePayment)
    {
        return toAjax(financePaymentService.updateFinancePayment(financePayment));
    }

    /**
     * 删除财务付款流程
     */
    @RequiresPermissions("hr:payment:remove")
    @Log(title = "财务付款流程", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(financePaymentService.deleteFinancePaymentByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "财务付款流程", businessType = BusinessType.SUBMIT)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        FinancePayment financePayment = financePaymentService.selectFinancePaymentById(id);
        return financePaymentService.submitApply(financePayment, ShiroUtils.getLoginName());
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
        FinancePayment financePayment = financePaymentService.selectFinancePaymentById(new Long(businessKey));
        mmap.put("financePayment", financePayment);
        return "hr/payment/paymentTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @Log(title = "财务付款流程", businessType = BusinessType.AUDIT)
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId,@ModelAttribute("preloadHrOvertime")FinancePayment financePayment,HttpServletRequest request) {
        financePayment.setTaskId(taskId);
        return financePaymentService.complete(financePayment, request);
    }

    /**
     * 自动绑定页面字段，这里必须通过id去数据库里查询，因为有很多字段页面没有带过来，导致流程节点会出问题
     */
    @ModelAttribute("preloadHrOvertime")
    public FinancePayment getFinancePayment(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
        if (id != null) {
            return financePaymentService.selectFinancePaymentById(id);
        }
        return new FinancePayment();
    }

    /**
     *撤销
     * @return
     */
    @Log(title = "财务付款流程", businessType = BusinessType.REPEAL)
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal( String instanceId,
                              HttpServletRequest request,String message) {
        try {
            return financePaymentService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }


}
