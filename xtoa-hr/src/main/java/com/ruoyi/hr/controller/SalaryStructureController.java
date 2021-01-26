package com.ruoyi.hr.controller;

import com.alibaba.excel.EasyExcel;
import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTOError;
import com.ruoyi.base.domain.DTO.SalaryDTOError;
import com.ruoyi.base.domain.DTO.SalaryStructureDTO;
import com.ruoyi.base.domain.SalaryStructure;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.ISalaryStructureService;
import com.ruoyi.hr.utils.AttendanceStatisticsEasyExcelListener;
import com.ruoyi.hr.utils.ImportInfoEntity;
import com.ruoyi.hr.utils.SalaryStructureExcelListener;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 薪资结构Controller
 * 
 * @author xt
 * @date 2020-06-18
 */
@Controller
@RequestMapping("/hr/salaryStructure")
public class SalaryStructureController extends BaseController
{
    private String prefix = "hr/salaryStructure";

    @Autowired
    private ISalaryStructureService salaryStructureService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    

    @RequiresPermissions("hr:salaryStructure:view")
    @GetMapping()
    public String salaryStructure()
    {
        return prefix + "/salaryStructure";
    }

    /**
     * 最新的薪资结构列表页
     * @param empId
     * @param mmap
     * @return
     */
    @GetMapping("/infoView/{empId}")
    public String infoView(@PathVariable("empId")Long empId,ModelMap mmap)
    {
        mmap.put("empId",empId);
        return prefix + "/salaryStructureInfoList";
    }

    /**
     * 最新的薪资结构列表数据
     * @param empId
     * @return
     */
    @PostMapping("/infoList/{empId}")
    @ResponseBody
    public TableDataInfo infoList(@PathVariable("empId")Long empId,SalaryStructure salaryStructure)
    {
        startPage();
        salaryStructure.setEmpId(empId);
        List<SalaryStructure> list = salaryStructureService.selectSalaryStructureList(salaryStructure);
        return getDataTable(list);
    }

    /**
     * 查询薪资结构列表
     */
    @RequiresPermissions("hr:salaryStructure:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SalaryStructure salaryStructure,String startNonManagerDate,String endNonManagerDate
            ,String startAdjustDate,String endAdjustDate)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId()) && !ShiroUtils.getSysUser().hrFlag().equals(1)) {
//            salaryStructure.setCreateId(ShiroUtils.getUserId());
//        }
        startPage();
        List<SalaryStructure> list = salaryStructureService.selectEmpSalaryStructureList(salaryStructure,startNonManagerDate,endNonManagerDate
                                                                                    ,startAdjustDate,endAdjustDate);
        return getDataTable(list);
    }


    @RequiresPermissions("hr:salaryStructure:viewInfo")
    @GetMapping("/Info")
    public String salaryStructureInfo()
    {
        return prefix + "/salaryStructureInfo";
    }

    /**
     * 查询薪资结构列表
     */
    @RequiresPermissions("hr:salaryStructure:viewInfo")
    @PostMapping("/listInfo")
    @ResponseBody
    public TableDataInfo listInfo(SalaryStructure salaryStructure)
    {
        /*if (!SysUser.isAdmin(ShiroUtils.getUserId()) && !ShiroUtils.getSysUser().hrFlag().equals(1)) {
            salaryStructure.setCreateId(ShiroUtils.getUserId());
        }*/
        startPage();
        salaryStructure.setAuditStatus(2);
        List<SalaryStructure> list = salaryStructureService.selectSalaryStructureListInfo(salaryStructure);
        return getDataTable(list);
    }

    @RequiresPermissions("hr:salaryStructure:view")
    @GetMapping("/salaryStructureProcess")
    public String salaryStructureProcess()
    {
        return prefix + "/salaryStructureProcess";
    }

    /**
     * 查询薪资结构列表
     */
    @RequiresPermissions("hr:salaryStructure:list")
    @PostMapping("/listProcess")
    @ResponseBody
    public TableDataInfo listProcess(SalaryStructure salaryStructure)
    {
        /*if (!SysUser.isAdmin(ShiroUtils.getUserId()) && !ShiroUtils.getSysUser().hrFlag().equals(1)) {
            salaryStructure.setCreateId(ShiroUtils.getUserId());
        }*/
        startPage();
        List<SalaryStructure> list = salaryStructureService.selectSalaryStructureList(salaryStructure);
        return getDataTable(list);
    }

    /**
     * 新增薪资结构
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存薪资结构
     */
    @RequiresPermissions("hr:salaryStructure:add")
    @Log(title = "薪资结构", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SalaryStructure salaryStructure)
    {
        SalaryStructure selectSalary = new SalaryStructure();
        selectSalary.setEmpId(salaryStructure.getEmpId());
        selectSalary.setDelFlag("0");
        selectSalary.setAuditStatus(2);
        selectSalary.setIsHistory(0);
        SalaryStructure salary = salaryStructureService.selectOneBySalaryStructure(selectSalary);
        if (salary != null){
            return AjaxResult.error("这个员工已经有薪资结构了，如需调整请提交薪资调整流程");
        }
        salaryStructure.setAuditStatus(2);
        salaryStructure.setSalaryContent("录入初始数据");
        salaryStructure.setMonthDate(salaryStructure.getAdjustDate());

        salaryStructure.setEmpName(hrEmpService.selectTHrEmpById(salaryStructure.getEmpId()).getEmpName());

        salaryStructure.setCreateId(ShiroUtils.getUserId());
        salaryStructure.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(salaryStructureService.insertSalaryStructure(salaryStructure));
    }

    /**
     * 修改薪资结构
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SalaryStructure salaryStructure = salaryStructureService.selectSalaryStructureById(id);
        mmap.put("salaryStructure", salaryStructure);
        return prefix + "/edit";
    }

  

     /**
     * 修改保存薪资结构
     */
//    @RequiresPermissions("hr:salaryStructure:edit")
     @Log(title = "调整薪资结构", businessType = BusinessType.INSERT)
     @PostMapping("/addProcess")
     @ResponseBody
     public AjaxResult addProcess(SalaryStructure salaryStructure)
     {
         return toAjax(salaryStructureService.insertSalaryStructureProcess(salaryStructure));
     }

    /**
     * 删除薪资结构
     */
    @RequiresPermissions("hr:salaryStructure:remove")
    @Log(title = "薪资结构", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(salaryStructureService.deleteSalaryStructureByIds(ids));
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
        SalaryStructure salaryStructureNew = salaryStructureService.selectSalaryStructureById(new Long(businessKey));

        mmap.put("salaryStructureNew", salaryStructureNew);
        SalaryStructure salaryStructure = salaryStructureService.selectSalaryStructureById(salaryStructureNew.getOldId());
        mmap.put("salaryStructure", salaryStructure);
        return prefix + "/task";
    }
    /**
     * 加载办理弹窗
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public String info(@PathVariable("id") Long id, ModelMap mmap) {

        SalaryStructure salaryStructureNew = salaryStructureService.selectSalaryStructureById(id);
        mmap.put("salaryStructureNew", salaryStructureNew);
        SalaryStructure salaryStructure = salaryStructureService.selectSalaryStructureById(salaryStructureNew.getOldId());
        mmap.put("salaryStructure", salaryStructure);
        return prefix + "/info";
    }

    /**
     * 审批流程
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId, @ModelAttribute("salaryStructure") SalaryStructure salaryStructure,
                               HttpServletRequest request) {
        return salaryStructureService.complete(salaryStructure, taskId, request);
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
            return salaryStructureService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }

    /**   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~以下是导入导出相关接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   */


    /**
     * 导出员工薪资列表
     */
    @RequiresPermissions("hr:holiday:export")
    @Log(title = "员工薪资导出错误数据", businessType = BusinessType.EXPORT)
    @PostMapping("/exportErrorList")
    @ResponseBody
    public AjaxResult exportErrorList()
    {
        Set<Object> objects = redisTemplate.opsForZSet().reverseRange(SalaryStructureExcelListener.redisKeyData, 0, -1);
        List<ImportInfoEntity<SalaryDTOError>> list = new ArrayList<>();
        for (Object object : objects) {
            ImportInfoEntity<SalaryDTOError> importInfoEntity = (ImportInfoEntity<SalaryDTOError>)object;
            list.add(importInfoEntity);
        }
        return AjaxResult.success(list);
    }

    /**
     * 导出错误数据
     */
    @Log(title = "员工薪资导出错误数据", businessType = BusinessType.EXPORT)
    @PostMapping("/exportError")
    @ResponseBody
    public AjaxResult exportError(Date date) throws IOException
    {
        //数量大一五条删除多余的数据
        List<SalaryDTOError> list = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(SalaryStructureExcelListener.redisKeyData,0,-1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
            ImportInfoEntity<SalaryDTOError> importInfoEntity = (ImportInfoEntity<SalaryDTOError>)typedTuple.getValue();
            long value = importInfoEntity.getImportDate().getTime() - date.getTime();
            //时间匹配可能会有毫秒级的差距
            if ( -1000 <= value && value <= 1000 ){
                list = importInfoEntity.getImportInfoList();
            }
        }
        String fileName = "导出错误数据_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, SalaryStructureDTO.class).sheet("错误数据").doWrite(list);
        return AjaxResult.success(fileName);
    }

    /**
     * 清除错误数据
     */
    @Log(title = "清除错误数据", businessType = BusinessType.DELETE)
    @GetMapping("/exportError")
    @ResponseBody
    public AjaxResult clearError()
    {
        Set<Object> objects = redisTemplate.opsForZSet().reverseRange(SalaryStructureExcelListener.redisKeyData, 0, -1);
        for (Object object : objects) {
            ImportInfoEntity<SalaryDTOError> importInfoEntity = (ImportInfoEntity<SalaryDTOError>)object;
            redisTemplate.opsForZSet().remove(SalaryStructureExcelListener.redisKeyData,importInfoEntity);
        }
        return AjaxResult.success();
    }

    /**
     * 导出薪资结构列表
     */
    @RequiresPermissions("hr:salaryStructure:export")
    @Log(title = "薪资结构", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SalaryStructure salaryStructure,HttpServletResponse response) throws IOException
    {
       
        startPage();
        salaryStructure.setAuditStatus(2);
        List<SalaryStructure> list = salaryStructureService.selectSalaryStructureListInfo(salaryStructure);

        String fileName = "salaryStructure_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, SalaryStructure.class).sheet("薪资报表").doWrite(list);
        return AjaxResult.success(fileName);
    }


    /**
     * 导出模板
     */
    @Log(title = "员工薪资导出模板", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadTemplate")
    @ResponseBody
    public AjaxResult export() throws IOException
    {
        List<SalaryStructureDTO> list = new ArrayList<>();

        SalaryStructureDTO salaryStructureDTO = new SalaryStructureDTO();
        salaryStructureDTO.setEmpNum("000");
        salaryStructureDTO.setMonthDate("yyyy年MM");
        salaryStructureDTO.setPerformanceBonusActual(BigDecimal.ZERO);
        salaryStructureDTO.setAttendanceBonus(BigDecimal.ZERO);
        salaryStructureDTO.setNightAllowance(BigDecimal.ZERO);
        salaryStructureDTO.setOvertimeActual(BigDecimal.ZERO);
        salaryStructureDTO.setOtherSubsidiesActual(BigDecimal.ZERO);
        salaryStructureDTO.setAmortization(BigDecimal.ZERO);
        salaryStructureDTO.setDeductionForUtilities(BigDecimal.ZERO);
        salaryStructureDTO.setDeductionForOther(BigDecimal.ZERO);
        salaryStructureDTO.setSocialSecurity(BigDecimal.ZERO);
        salaryStructureDTO.setAccumulationFund(BigDecimal.ZERO);
        salaryStructureDTO.setTallage(BigDecimal.ZERO);
        list.add(salaryStructureDTO);

        String fileName = "薪资导入模板_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, SalaryStructureDTO.class).sheet("薪资导入").doWrite(list);
        return AjaxResult.success(fileName);
    }


    /**
     * 检查是否可以导入
     */
    @RequiresPermissions("hr:holiday:export")
    @GetMapping("/checkUrl")
    @ResponseBody
    public AjaxResult checkUrl()
    {
        try {
            if (redisTemplate.hasKey(SalaryStructureExcelListener.redisKey)){
                return AjaxResult.error();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.success();
    }
    /**
     * 导入数据
     */
    @RequiresPermissions("hr:salaryStructure:export")
    @PostMapping("/importData")
    @ResponseBody
    @Transactional
    public AjaxResult importData(@RequestParam("importUtilFile") MultipartFile[] files)
    {
        if (files != null) {
            if (files.length > 1){
                return AjaxResult.error("不支持多文件！");
            }
            try {
                StringBuilder message = new StringBuilder();

                String fileName = files[0].getOriginalFilename();

                EasyExcel.read(files[0].getInputStream(), SalaryStructureDTO.class,
                        new SalaryStructureExcelListener(salaryStructureService,redisTemplate))
                        .doReadAll();
                message.append("导入成功");

                return AjaxResult.success(message.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error(e.getMessage());
            }
        }
        return AjaxResult.error("没有检测到文件！");
    }
    


}
