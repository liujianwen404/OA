package com.ruoyi.hr.controller;

import com.alibaba.excel.EasyExcel;
import com.ruoyi.base.domain.HrLeave;
import com.ruoyi.base.domain.HrOff;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.framework.web.service.DictService;
import com.ruoyi.hr.domain.HrNonManager;
import com.ruoyi.hr.service.IHrNonManagerService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 入职申请Controller
 * 
 * @author xt
 * @date 2020-05-14
 */
@Controller
@RequestMapping("/hr/manager")
public class HrNonManagerController extends BaseController
{
    private String prefix = "hr/manager";

    @Autowired
    private IHrNonManagerService hrNonManagerService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private DictService dictService;


    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysPostService sysPostService;

    @RequiresPermissions("hr:manager:view")
    @GetMapping()
    public String manager(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/manager";
    }

    /**
     * 查询入职申请列表
     */
    @RequiresPermissions("hr:manager:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrNonManager hrNonManager)
    {

            hrNonManager.setCreateId(ShiroUtils.getUserId());

        startPage();
        List<HrNonManager> list = hrNonManagerService.selectHrNonManagerList(hrNonManager);
        return getDataTable(list);
    }


    @RequiresPermissions("hr:manager:viewManage")
    @GetMapping("/viewManage")
    public String viewManage(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/managerViewManage";
    }

    /**
     * 查询入职申请列表
     */
//    @RequiresPermissions("hr:manager:listManage")
    @PostMapping("/listManage")
    @ResponseBody
    public TableDataInfo listManage(HrNonManager hrNonManager)
    {
        /*if (!SysUser.isAdmin(ShiroUtils.getUserId()) && !ShiroUtils.getSysUser().hrFlag().equals(1)) {
            hrNonManager.setCreateId(ShiroUtils.getUserId());
        }*/
        startPage();
        List<HrNonManager> list = hrNonManagerService.selectHrNonManagerListManage(hrNonManager);
        return getDataTable(list);
    }



    /**
     * 导出入职申请列表
     */
    @RequiresPermissions("hr:manager:export")
    @Log(title = "入职申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrNonManager hrNonManager)
    {
        List<HrNonManager> list = hrNonManagerService.selectHrNonManagerListExport(hrNonManager);
        for (HrNonManager nonManager : list) {
            nonManager.setTodoUserNameExcel(nonManager.getTodoUserName());

            nonManager.setCitys(dictService.getLabel("citys",nonManager.getCitys()));
            nonManager.setAuditStatusExcel(dictService.getLabel("auditStatus", nonManager.getAuditStatus()+""));

            nonManager.setDeptName(sysDeptService.selectDeptById(nonManager.getDeptId()).getShowName());
            nonManager.setPostName(sysPostService.selectPostById(nonManager.getPostId()).getPostName());
        }

        String fileName = "入职_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, HrNonManager.class).sheet("入职").doWrite(list);
        return AjaxResult.success(fileName);
    }

    /**
     * 新增入职申请
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存入职申请
     */
    @RequiresPermissions("hr:manager:add")
    @Log(title = "入职申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrNonManager hrNonManager)
    {
        return toAjax(hrNonManagerService.insertHrNonManager(hrNonManager));
    }

    /**
     * 修改入职申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrNonManager hrNonManager = hrNonManagerService.selectHrNonManagerById(id);
        if (StringUtils.isNotBlank(hrNonManager.getAttachment())){
            hrNonManager.setImgList(Arrays.asList(hrNonManager.getAttachment().split(",")));
        }
        mmap.put("nonManager", hrNonManager);
        return prefix + "/edit";
    }
    /**
     * 修改入职申请
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrNonManager hrNonManager = hrNonManagerService.selectHrNonManagerById(id);
        if (StringUtils.isNotBlank(hrNonManager.getAttachment())){
            hrNonManager.setImgList(Arrays.asList(hrNonManager.getAttachment().split(",")));
        }
        mmap.put("nonManager", hrNonManager);
        return prefix + "/detail";
    }

    /**
     * 修改保存入职申请
     */
    @RequiresPermissions("hr:manager:edit")
    @Log(title = "入职申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrNonManager hrNonManager)
    {
        return toAjax(hrNonManagerService.updateHrNonManager(hrNonManager));
    }

    /**
     * 删除入职申请
     */
    @RequiresPermissions("hr:manager:remove")
    @Log(title = "入职申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrNonManagerService.deleteHrNonManagerByIds(ids));
    }



    /**
     * 加载办理弹窗
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap,
                                   String module, String activitiId,String instanceId) {

        hrNonManagerService.showVerifyDialog(taskId,module,activitiId,mmap,instanceId);
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
        HrNonManager hrNonManager = hrNonManagerService.selectHrNonManagerById(new Long(businessKey));
        if (StringUtils.isNotBlank(hrNonManager.getAttachment())){
            hrNonManager.setImgList(Arrays.asList(hrNonManager.getAttachment().split(",")));
        }
        return prefix + "/nonManagerTask" ;
    }


    /**
     * 完成任务
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId,
                               HttpServletRequest request,
                               HrNonManager hrNonManager,
                               String comment,
                               String p_B_hrApproved) {
        try {
            return hrNonManagerService.complete(taskId, request, hrNonManager, comment, p_B_hrApproved);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{taskId, e});
            return AjaxResult.error("完成任务失败");
        }
    }


    /**
     *撤销
     * @return
     */
    @RequiresPermissions("hr:manager:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal( String instanceId,
                               HttpServletRequest request,String message) {
        try {
            return hrNonManagerService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     *提交
     * @return
     */
    @RequiresPermissions("hr:manager:edit")
    @RequestMapping(value = "/commit", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult commit( Long id) {
        try {
            return hrNonManagerService.commit(id);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{id, e});
            return AjaxResult.error("repeal失败");
        }
    }

}
