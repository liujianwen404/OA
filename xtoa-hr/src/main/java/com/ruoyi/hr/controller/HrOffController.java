package com.ruoyi.hr.controller;

import com.alibaba.excel.EasyExcel;
import com.ruoyi.base.domain.HrLeave;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.framework.web.service.DictService;
import com.ruoyi.system.domain.SysUser;

import java.io.IOException;
import java.util.List;

import com.ruoyi.system.service.ISysUserService;
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
import com.ruoyi.base.domain.HrOff;
import com.ruoyi.base.provider.hrService.IHrOffService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * off流程Controller
 * 
 * @author xt
 * @date 2020-07-28
 */
@Controller
@RequestMapping("/hr/hrOff")
public class HrOffController extends BaseController
{
    private String prefix = "hr/hrOff";

    @Autowired
    private IHrOffService hrOffService;
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private DictService dictService;

    @RequiresPermissions("hr:hrOff:view")
    @GetMapping()
    public String hrOff(ModelMap mmap)
    {
        mmap.put("currentUser",ShiroUtils.getSysUser());
        return prefix + "/hrOff";
    }

    /**
     * 查询off流程列表
     */
    @RequiresPermissions("hr:hrOff:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrOff hrOff)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrOff.setCreateId(ShiroUtils.getUserId());
//        }
        startPage();
        List<HrOff> list = hrOffService.selectHrOffList(hrOff);
        return getDataTable(list);
    }

    @RequiresPermissions("hr:hrOff:viewManage")
    @GetMapping("/hrOffViewManage")
    public String hrOffViewManage(ModelMap mmap)
    {
        mmap.put("currentUser",ShiroUtils.getSysUser());
        return prefix + "/hrOffViewManage";
    }

    /**
     * 查询off流程列表
     */
//    @RequiresPermissions("hr:hrOff:listManage")
    @PostMapping("/listManage")
    @ResponseBody
    public TableDataInfo listManage(HrOff hrOff)
    {
        /*if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrOff.setCreateId(ShiroUtils.getUserId());
        }*/
        startPage();
        List<HrOff> list = hrOffService.selectHrOffListManage(hrOff);
        return getDataTable(list);
    }


    /**
     * 导出off流程列表
     */
    @RequiresPermissions("hr:hrOff:export")
    @Log(title = "offer流程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrOff hrOff)
    {
       /* if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrOff.setCreateId(ShiroUtils.getUserId());
        }*/
        List<HrOff> list = hrOffService.selectHrOffListManage(hrOff);

        for (HrOff off : list) {
            off.setCity(dictService.getLabel("citys",off.getCity()));
        }

        String fileName = "offer_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, HrOff.class).sheet("offer").doWrite(list);
        return AjaxResult.success(fileName);
    }

    /**
     * 新增off流程
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存off流程
     */
    @RequiresPermissions("hr:hrOff:add")
    @Log(title = "offer流程", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrOff hrOff,
                              @RequestParam(value = "adjunctFile", required = false) MultipartFile multipart,
                              @RequestParam(value = "salaryAdjunctFile", required = false) MultipartFile salaryAdjunctFile,
                              @RequestParam(value = "resumeAdjunctFile", required = false) MultipartFile resumeAdjunctFile) throws IOException {

        if (multipart != null && !multipart.isEmpty()){
            String fileName = FileUploadUtils.upload(Global.getProfile() + "/hrOffer", multipart);
            hrOff.setAdjunct(fileName);
        }
        if (salaryAdjunctFile != null && !salaryAdjunctFile.isEmpty()){
            String fileName = FileUploadUtils.upload(Global.getProfile() + "/hrOffer", salaryAdjunctFile);
            hrOff.setSalaryAdjunct(fileName);
        }
        if (resumeAdjunctFile != null && !resumeAdjunctFile.isEmpty()){
            String fileName = FileUploadUtils.upload(Global.getProfile() + "/hrOffer", resumeAdjunctFile);
            hrOff.setResumeAdjunct(fileName);
        }
        return toAjax(hrOffService.insertHrOff(hrOff));
    }

    /**
     * 修改off流程
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap,Integer isDetail)
    {
        HrOff hrOff = hrOffService.selectHrOffById(id);
        mmap.put("hrOff", hrOff);
        mmap.put("isDetail",isDetail);
        return prefix + "/edit";
    }

    /**
     * 修改保存off流程
     */
    @RequiresPermissions("hr:hrOff:edit")
    @Log(title = "offer流程", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrOff hrOff
            ,@RequestParam(value = "adjunctFile", required = false) MultipartFile multipart
            ,@RequestParam(value = "salaryAdjunctFile", required = false) MultipartFile salaryAdjunctFile
            ,@RequestParam(value = "resumeAdjunctFile", required = false) MultipartFile resumeAdjunctFile) throws IOException {
        if (multipart != null && !multipart.isEmpty()){
            String fileName = FileUploadUtils.upload(Global.getProfile() + "/hrOffer", multipart);
            hrOff.setAdjunct(fileName);
        }
        if (salaryAdjunctFile != null && !salaryAdjunctFile.isEmpty()){
            String fileName = FileUploadUtils.upload(Global.getProfile() + "/hrOffer", salaryAdjunctFile);
            hrOff.setSalaryAdjunct(fileName);
        }
        if (resumeAdjunctFile != null && !resumeAdjunctFile.isEmpty()){
            String fileName = FileUploadUtils.upload(Global.getProfile() + "/hrOffer", resumeAdjunctFile);
            hrOff.setResumeAdjunct(fileName);
        }
        return toAjax(hrOffService.updateHrOff(hrOff));
    }

    /**
     * 删除off流程
     */
    @RequiresPermissions("hr:hrOff:remove")
    @Log(title = "offer流程", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrOffService.deleteHrOffByIds(ids));
    }

    /**
     * 提交申请
     */
    @Log(title = "offer流程", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        HrOff hrOff = hrOffService.selectHrOffById(id);
        String applyUserId = ShiroUtils.getLoginName();
        return hrOffService.submitApply(hrOff, applyUserId);
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
        HrOff hrOff = hrOffService.selectHrOffById(new Long(businessKey));
        mmap.put("hrOff", hrOff);
        return "hr/hrOff/hrOffTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("preloadOff") HrOff hrOff,
                               HttpServletRequest request) {
        return hrOffService.complete(hrOff, taskId, request);
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
            return hrOffService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }
}
