package com.ruoyi.hr.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.base.domain.HrFillClockRule;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrBusinessTrip;
import com.ruoyi.base.domain.HrBusinessTripSon;
import com.ruoyi.hr.service.IHrFillClockRuleService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.hr.service.impl.HrAttendanceInfoServiceImpl;
import com.ruoyi.system.domain.SysUser;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;
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
import com.ruoyi.base.domain.HrFillClock;
import com.ruoyi.hr.service.IHrFillClockService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 补卡申请Controller
 * 
 * @author liujianwen
 * @date 2020-06-24
 */
@Controller
@RequestMapping("/hr/clock")
public class HrFillClockController extends BaseController
{
    private String prefix = "hr/clock";

    @Autowired
    private IHrFillClockService hrFillClockService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IHrFillClockRuleService hrFillClockRuleService;

    @RequiresPermissions("hr:clock:view")
    @GetMapping()
    public String clock(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/clock";
    }



    @RequiresPermissions("hr:clockManage:view")
    @GetMapping("/clockManage")
    public String clockManage(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/clockManage";
    }

    /**
     * 查询补卡申请列表
     */
    @RequiresPermissions("hr:clockManage:list")
    @PostMapping("/listManage")
    @ResponseBody
    public TableDataInfo listManage(HrFillClock hrFillClock)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
           /* if (ShiroUtils){

            }*/
        }

        Object endApplyTime = hrFillClock.getParams().get("endApplyTime");
        if (endApplyTime != null && !Objects.equals("",endApplyTime)){
            hrFillClock.getParams().put("endApplyTime",endApplyTime + " 23:59:59");
        }
        startPage();
        List<HrFillClock> list = hrFillClockService.selectHrFillClockListManage(hrFillClock);
        return getDataTable(list);
    }

    /**
     * 查询补卡申请列表
     */
    @RequiresPermissions("hr:clock:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrFillClock hrFillClock)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrFillClock.setCreateId(ShiroUtils.getUserId());
        }
        Object endApplyTime = hrFillClock.getParams().get("endApplyTime");
        if (endApplyTime != null && !Objects.equals("",endApplyTime)){
            hrFillClock.getParams().put("endApplyTime",endApplyTime + " 23:59:59");
        }
        startPage();
        List<HrFillClock> list = hrFillClockService.selectHrFillClockList(hrFillClock);
        return getDataTable(list);
    }



    /**
     * 导出补卡申请列表
     */
    @RequiresPermissions("hr:clock:export")
    @Log(title = "补卡申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrFillClock hrFillClock)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrFillClock.setCreateId(ShiroUtils.getUserId());
//        }
        List<HrFillClock> list = hrFillClockService.selectHrFillClockListManage(hrFillClock);
        ExcelUtil<HrFillClock> util = new ExcelUtil<HrFillClock>(HrFillClock.class);
        return util.exportExcel(list, "clock");
    }

    /**
     * 新增补卡申请
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("hrEmp",processHandleService.selectTHrEmpByUserId());
        return prefix + "/add";
    }

    /**
     * 新增保存补卡申请
     */
    @RequiresPermissions("hr:clock:add")
    @Log(title = "补卡申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrFillClock hrFillClock)
    {
        hrFillClock.setApplyUser(ShiroUtils.getLoginName());
        hrFillClock.setApplyUserName(ShiroUtils.getUserName());
        hrFillClock.setApplyTime(DateUtils.getNowDate());
        hrFillClock.setCreateId(ShiroUtils.getUserId());
        hrFillClock.setCreateBy(ShiroUtils.getLoginName());
        hrFillClock.setCreateTime(DateUtils.getNowDate());
        return toAjax(hrFillClockService.insertHrFillClock(hrFillClock));
    }

    /**
     * 修改补卡申请
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrFillClock hrFillClock = hrFillClockService.selectHrFillClockById(id);
        mmap.put("hrFillClock", hrFillClock);
        return prefix + "/edit";
    }

    /**
     * 修改保存补卡申请
     */
    @RequiresPermissions("hr:clock:edit")
    @Log(title = "补卡申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrFillClock hrFillClock)
    {
        return toAjax(hrFillClockService.updateHrFillClock(hrFillClock));
    }

    /**
     * 删除补卡申请
     */
    @RequiresPermissions("hr:clock:remove")
    @Log(title = "补卡申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrFillClockService.deleteHrFillClockByIds(ids));
    }


    /**
     * 修改补卡申请
     */
    @GetMapping("/commit/{id}")
    public String commit(@PathVariable("id") Long id,Date classDate, ModelMap mmap)
    {

        HrFillClock hrFillClock = hrFillClockService.selectHrFillClockById(id);

        mmap.put("hrFillClock", hrFillClock);
        Integer count = hrFillClockService.selectHrFillClockCount(ShiroUtils.getUserId(),classDate);
        mmap.put("hrFillClockCount", "本月已申请" + count + "次补卡");
        mmap.put("classDate", DateUtil.format(classDate,"yyyy-MM-dd"));
        return prefix + "/commit";
    }


    /**
     * 提交申请
     */
    @Log(title = "补卡申请", businessType = BusinessType.UPDATE)
    @PostMapping( "/submitApply")
    @ResponseBody
    public AjaxResult submitApply(HrFillClock hrFillClockWeb,String classDate) {
        Date fillClassDate = DateUtil.parseDate(classDate);
        if(!DateUtil.isSameDay(fillClassDate,hrFillClockWeb.getDates())){
            return AjaxResult.error("您提交的补卡日期，不是本条申请应补卡日期！");
        }
        int month = DateUtil.month(hrFillClockWeb.getDates()) + 1;
        HrFillClockRule hrFillClockRule = new HrFillClockRule();
        List<HrFillClockRule> hrFillClockRules = hrFillClockRuleService.selectHrFillClockRuleList(hrFillClockRule);
        if(!hrFillClockRules.isEmpty()){
            HrFillClockRule rule = hrFillClockRules.get(0);
            Date verifyDate = verifyRule(rule,month,DateUtil.date());
            if(verifyDate != null){
                return AjaxResult.error(month + "月的补卡截止日期为："+ DateUtil.format(verifyDate,"MM-dd"));
            }
        }

        HrFillClock hrFillClock = hrFillClockService.selectHrFillClockById(hrFillClockWeb.getId());

        hrFillClock.setDates(hrFillClockWeb.getDates());
        hrFillClock.setReason(hrFillClockWeb.getReason());
        if (StringUtil.isNotEmpty(hrFillClockWeb.getImgUrls())){
            String[] split = hrFillClockWeb.getImgUrls().split(",");
            String join = StrUtil.join(",", split);
            if (join.startsWith(",")){
                join = join.substring(1,join.length());
            }
            hrFillClock.setImgUrls(join);
        }

        String applyUserId = ShiroUtils.getLoginName();
        return hrFillClockService.submitApply(hrFillClock, applyUserId);
    }

    /**
     * 根据
     * @param rule
     * @param fillClockDate
     * @return
     */
    private Date verifyRule(HrFillClockRule rule,int month,Date fillClockDate) {
//        int month = DateUtil.month(fillClockDate) + 1;
        if(month == 1){
            Date january = rule.getJanuary();
            if(january != null && fillClockDate.after(january)){
                return january;
            }
        }if(month == 2){
            Date february = rule.getFebruary();
            if(february != null && fillClockDate.after(february)){
                return february;
            }
        }if(month == 3){
            Date march = rule.getMarch();
            if(march != null && fillClockDate.after(march)){
                return march;
            }
        }if(month == 4){
            Date april = rule.getApril();
            if(april != null && fillClockDate.after(april)){
                return april;
            }
        }if(month == 5){
            Date may = rule.getMay();
            if(may != null && fillClockDate.after(may)){
                return may;
            }
        }if(month == 6){
            Date june = rule.getJune();
            if(june != null && fillClockDate.after(june)){
                return june;
            }
        }if(month == 7){
            Date july = rule.getJuly();
            if(july != null && fillClockDate.after(july)){
                return july;
            }
        }if(month == 8){
            Date august = rule.getAugust();
            if(august != null && fillClockDate.after(august)){
                return august;
            }
        }if(month == 9){
            Date september = rule.getSeptember();
            if(september != null && fillClockDate.after(september)){
                return september;
            }
        }if(month == 10){
            Date october = rule.getOctober();
            if(october != null && fillClockDate.after(october)){
                return october;
            }
        }if(month == 11){
            Date november = rule.getNovember();
            if(november != null && fillClockDate.after(november) && !DateUtil.isSameDay(fillClockDate,november)){
                return november;
            }
        }if(month == 12){
            Date december = rule.getDecember();
            if(december != null && fillClockDate.after(december) && !DateUtil.isSameDay(fillClockDate,december)){
                return december;
            }
        }
        return null;
    }

    /**
     *撤销
     * @return
     */
    @RequiresPermissions("hr:businessTrip:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal(String instanceId,HttpServletRequest request, String message) {
        try {
            return hrFillClockService.repeal(instanceId, request, message);
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
        HrFillClock hrFillClock = hrFillClockService.selectHrFillClockById(new Long(businessKey));

        hrFillClock.setClassDateStr(DateUtil.format(hrFillClock.getClassDate(),"yyyy-MM-dd") +  " "
                + (hrFillClock.getCheckType().equals(HrAttendanceInfoServiceImpl.check_type_OnDuty) ? "上班卡" : "下班卡" ));

        mmap.put("hrFillClock", hrFillClock);
        return "hr/clock/clockTask";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("preloadClock") HrFillClock hrFillClock,
                               HttpServletRequest request) {
        return hrFillClockService.complete(hrFillClock, taskId, request);
    }

    /**
     * 自动绑定页面字段
     */
    @ModelAttribute("preloadClock")
    public HrFillClock getClock(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            return hrFillClockService.selectHrFillClockById(id);
        }
        return new HrFillClock();
    }
}
