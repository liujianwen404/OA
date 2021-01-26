package com.ruoyi.hr.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.ruoyi.base.domain.DTO.HolidayDTO;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrLeave;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.base.provider.hrService.IHrLeaveService;
import com.ruoyi.hr.service.IHolidayService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.hr.service.impl.HolidayServiceImpl;
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
import java.util.*;

/**
 * 请假业务Controller
 *
 * @author liujianwen
 * @date 2020-05-15
 */
@Controller
@RequestMapping("/hr/leave")
public class HrLeaveController extends BaseController {
    private String prefix = "hr/leave";

    @Autowired
    private IHrLeaveService hrLeaveService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private HrEmpService tHrEmpService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IHolidayService holidayService;


    @RequiresPermissions("hr:leave:view")
    @GetMapping()
    public String leave(ModelMap mmap) {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/leave";
    }


    @RequiresPermissions("hr:leave:leaveList")
    @GetMapping("/leaveList")
    public String leaveList(ModelMap mmap) {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/leaveList";
    }

    /**
     * 查询请假业务管理列表
     */
    @RequiresPermissions("hr:leave:leaveList")
    @PostMapping("/viewList")
    @ResponseBody
    public TableDataInfo viewList(HrLeave hrLeave) {
        /*if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrLeave.setCreateBy(ShiroUtils.getLoginName());
        }*/
        startPage();
        List<HrLeave> list = hrLeaveService.selectHrLeaveViewList(hrLeave);
        return getDataTable(list);
    }



    /**
     * 查询请假业务列表
     */
    @RequiresPermissions("hr:leave:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrLeave hrLeave) {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrLeave.setCreateBy(ShiroUtils.getLoginName());
        }
        startPage();
        List<HrLeave> list = hrLeaveService.selectHrLeaveList(hrLeave);
        return getDataTable(list);
    }

    /**
     * 导出请假业务列表
     */
    @RequiresPermissions("hr:leave:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrLeave hrLeave) {
        List<HrLeave> list = hrLeaveService.selectHrLeaveViewListExcel(hrLeave);
        String fileName = "请假业务_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, HrLeave.class).sheet("请假业务").doWrite(list);
        return AjaxResult.success(fileName);
    }

    /**
     * 新增请假业务
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        Map<String,Double> holidayHours = holidayService.getHolidayHours(ShiroUtils.getUserId());
        Double holidayType1 = holidayHours.get(HolidayServiceImpl.holidayType1);
        Double holidayType4 = holidayHours.get(HolidayServiceImpl.holidayType4);
        mmap.put("holidayHours", StrUtil.format("剩余调休：{}H；剩余年假：{}H",holidayType1,holidayType4));
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存请假业务
     */
    @RequiresPermissions("hr:leave:add")
    @Log(title = "请假业务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrLeave hrLeave) {
        Long userId = ShiroUtils.getUserId();
        if (SysUser.isAdmin(userId)) {
            return error("提交申请失败：不允许管理员提交申请！");
        }
        return toAjax(hrLeaveService.insertHrLeave(hrLeave));
    }

    /**
     * 修改请假业务
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        HrLeave hrLeave = hrLeaveService.selectHrLeaveById(id);
        Map<String,Double> holidayHours = holidayService.getHolidayHours(ShiroUtils.getUserId());
        Double holidayType1 = holidayHours.get(HolidayServiceImpl.holidayType1);
        Double holidayType4 = holidayHours.get(HolidayServiceImpl.holidayType4);
        mmap.put("holidayHours", StrUtil.format("剩余调休：{}H；剩余年假：{}H",holidayType1,holidayType4));

        mmap.put("hrLeave", hrLeave);
        return prefix + "/edit";
    }

    /**
     * 修改保存请假业务
     */
    @RequiresPermissions("hr:leave:edit")
    @Log(title = "请假业务", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrLeave hrLeave) {
        return toAjax(hrLeaveService.updateHrLeave(hrLeave));
    }

    /**
     * 删除图片
     */
    @Log(title = "删除证明图片", businessType = BusinessType.DELETE)
    @RequestMapping("/editDelImg")
    @ResponseBody
    public AjaxResult editDelImg(String img,Long id) {
        try {
            return hrLeaveService.editDelImg(img,id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.error();
    }

    /**
     * 删除请假业务
     */
    @RequiresPermissions("hr:leave:remove")
    @Log(title = "请假业务", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(hrLeaveService.deleteHrLeaveByIds(ids));
    }

    /**
     * 获取请假时长
     */
    @PostMapping( "/leaveTime")
    @ResponseBody
    public AjaxResult leaveTime(HttpServletRequest request) {

//        AjaxResult leaveTime = hrLeaveService.getLeaveTime(request);



        String s1 = request.getParameter("startTime");
        String s2 = request.getParameter("endTime");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("hours",0D);
        if (StringUtils.isEmpty(s1) || StringUtils.isEmpty(s2)){
            return AjaxResult.success("success",resultMap);
        }


        Double aDouble = hrLeaveService.attendanceGroupHandle(s1, s2, ShiroUtils.getUserId());
        resultMap.put("hours",aDouble);
        Map<String,Double> holidayHours = holidayService.getHolidayHours(ShiroUtils.getUserId());

        String type = request.getParameter("type");
        if(type != null){
            if (type.equals("1")){
                //调休
                Double holidayType1 = holidayHours.get(HolidayServiceImpl.holidayType1);
                if (holidayType1 < aDouble ){
                    return AjaxResult.error("调休余额不足!",resultMap);
                }
            }else if (type.equals("4")){
                //年假
                Double holidayType4 = holidayHours.get(HolidayServiceImpl.holidayType4);
                if (holidayType4 < aDouble ){
                    return AjaxResult.error("年假余额不足!",resultMap);
                }
            }
        }
        return AjaxResult.success("success",resultMap);
    }

    /**
     * 提交申请
     */
    @Log(title = "请假业务", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(Long id) {
        HrLeave hrLeave = hrLeaveService.selectHrLeaveById(id);
        String applyUserId = ShiroUtils.getLoginName();
        return hrLeaveService.submitApply(hrLeave, applyUserId);
    }

    @RequiresPermissions("hr:leave:todoView")
    @GetMapping("/leaveTodo")
    public String todoView() {
        return prefix + "/leaveTodo";
    }

    /**
     * 我的待办列表
     * @param hrLeave
     * @return
     */
    @RequiresPermissions("hr:leave:taskList")
    @PostMapping("/taskList")
    @ResponseBody
    public TableDataInfo taskList(HrLeave hrLeave) {
        startPage();
        List<HrLeave> list = hrLeaveService.findTodoTasks(hrLeave, ShiroUtils.getLoginName());
        return getDataTable(list);
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
        HrLeave hrLeave = hrLeaveService.selectHrLeaveById(new Long(businessKey));
        if (StringUtils.isNotEmpty(hrLeave.getImgUrls())){
            hrLeave.setImgList(Arrays.asList(hrLeave.getImgUrls().split(",")));
        }

        mmap.put("hrLeave", hrLeave);
        return "hr/leave/leaveTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("preloadLeave") HrLeave hrLeave,
                               HttpServletRequest request) {
           return hrLeaveService.complete(hrLeave, taskId, request);
    }
    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadLeave")
    public HrLeave getLeave(@RequestParam(value = "id", required = false) Long id, HttpSession session) {
        if (id != null) {
            return hrLeaveService.selectHrLeaveById(id);
        }
        return new HrLeave();
    }

    @RequiresPermissions("hr:leave:doneView")
    @GetMapping("/leaveDone")
    public String doneView() {
        return prefix + "/leaveDone";
    }

    /**
     * 我的已办列表
     * @param hrLeave
     * @return
     */
    @RequiresPermissions("hr:leave:taskDoneList")
    @PostMapping("/taskDoneList")
    @ResponseBody
    public TableDataInfo taskDoneList(HrLeave hrLeave) {
        startPage();
        List<HrLeave> list = hrLeaveService.findDoneTasks(hrLeave, ShiroUtils.getLoginName());
        return getDataTable(list);
    }

    /**
     *撤销
     * @return
     */
    @RequiresPermissions("hr:leave:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal( String instanceId,
                              HttpServletRequest request,String message) {
        try {
            return hrLeaveService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }

}
