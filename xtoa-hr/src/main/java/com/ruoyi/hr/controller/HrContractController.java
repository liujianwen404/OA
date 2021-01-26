package com.ruoyi.hr.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.FileUtils;
import com.ruoyi.base.domain.Holiday;
import com.ruoyi.base.domain.HrContract;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.IHrContractService;
import com.ruoyi.system.domain.SysUser;
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
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 劳动合同Controller
 * 
 * @author xt
 * @date 2020-06-17
 */
@Controller
@RequestMapping("/hr/contract")
public class HrContractController extends BaseController
{
    private String prefix = "hr/contract";

    @Autowired
    private IHrContractService hrContractService;


    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @RequiresPermissions("hr:contract:view")
    @GetMapping()
    public String contract(ModelMap modelMap)
    {
        modelMap.put("currentUser",ShiroUtils.getSysUser());
        return prefix + "/contract";
    }

    @RequiresPermissions("hr:contract:view")
    @GetMapping("/contractList")
    public String contractList(ModelMap modelMap)
    {
        modelMap.put("currentUser",ShiroUtils.getSysUser());
        return prefix + "/contractList";
    }

    /**
     * 查询劳动合同列表
     */
    @RequiresPermissions("hr:contract:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrContract hrContract,String isFrom)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId()) && !ShiroUtils.getSysUser().hrFlag().equals(1)) {
//            hrContract.setCreateId(ShiroUtils.getUserId());
//        }
        startPage();
        if (StringUtils.isNotEmpty(isFrom) && isFrom.equals("contractList")){
            //列表页面
            List<HrContract> list = hrContractService.selectHrContractListIsFrom(hrContract);

            return getDataTable(list);
        }
        List<HrContract> list = hrContractService.selectHrContractList(hrContract);

        return getDataTable(list);
    }

    /**
     * 导出劳动合同列表
     */
    @RequiresPermissions("hr:contract:export")
    @Log(title = "劳动合同", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrContract hrContract) throws Exception
    {

        List<HrContract> list = hrContractService.selectHrContractListIsFrom(hrContract);
        InputStream inputStream = null;
        for (HrContract contract : list) {
                try {
                    if (StringUtil.isNotEmpty(contract.getContractUrl())){
                        inputStream = FileUtils.openInputStream(new File(Global.getProfile()
                                + StringUtils.substringAfter(contract.getContractUrl(), Constants.RESOURCE_PREFIX)));
                        contract.setInputStream(inputStream);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
        String fileName = "劳动合同_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, HrContract.class).sheet("劳动合同").doWrite(list);
        try {
            return AjaxResult.success(fileName);
        }finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 新增劳动合同
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存劳动合同
     */
    @RequiresPermissions("hr:contract:add")
    @Log(title = "劳动合同", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrContract hrContract)
    {
        return toAjax(hrContractService.insertHrContract(hrContract));
    }

    /**
     * 修改劳动合同
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrContract hrContract = hrContractService.selectHrContractById(id);
        mmap.put("hrContract", hrContract);
        return prefix + "/edit";
    }

    /**
     * 修改保存劳动合同
     */
    @RequiresPermissions("hr:contract:edit")
    @Log(title = "劳动合同", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrContract hrContract)
    {
        return toAjax(hrContractService.updateHrContract(hrContract));
    }

    /**
     * 删除劳动合同
     */
    @RequiresPermissions("hr:contract:remove")
    @Log(title = "劳动合同", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrContractService.deleteHrContractByIds(ids));
    }


    /**
     * 提交申请
     */
    @Log(title = "劳动合同", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        try {
            HrContract hrContract = hrContractService.selectHrContractById(id);
            String applyUserId = ShiroUtils.getLoginName();
            return hrContractService.submitApply(hrContract, applyUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return error("提交申请失败");
        }
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
        HrContract hrContract = hrContractService.selectHrContractById(new Long(businessKey));
        mmap.put("hrContract", hrContract);
        return prefix + "/task";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("hrContract") HrContract hrContract,
                               HttpServletRequest request) {
        try {
            return hrContractService.complete(hrContract, taskId, request);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
    

    /**
     *撤销
     * @return
     */
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal( String instanceId,
                              HttpServletRequest request,String message) {
        try {
            return hrContractService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }
}
