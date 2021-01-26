package com.ruoyi.hr.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.base.domain.DTO.HolidayDTO;
import com.ruoyi.base.domain.DTO.HolidayDTOError;
import com.ruoyi.base.domain.DTO.SalaryDTOError;
import com.ruoyi.base.domain.DTO.SalaryStructureDTO;
import com.ruoyi.base.domain.*;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.SalaryStructureMapper;
import com.ruoyi.hr.service.*;
import com.ruoyi.process.general.service.IProcessService;
import com.ruoyi.process.utils.SpringUtil;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 薪资结构Service业务层处理
 * 
 * @author xt
 * @date 2020-06-18
 */
@Service
public class SalaryStructureServiceImpl implements ISalaryStructureService 
{

    private static final Logger logger = LoggerFactory.getLogger(SalaryStructureServiceImpl.class);

    @Autowired
    private SalaryStructureMapper salaryStructureMapper;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IProcessService processService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IHrAttendanceGroupService attendanceGroupService;

    @Autowired
    private ISysPostService sysPostService;

    @Autowired
    private IHrAttendanceStatisticsService hrAttendanceStatisticsService;


    /**
     * 查询薪资结构
     * 
     * @param id 薪资结构ID
     * @return 薪资结构
     */
    @Override
    public SalaryStructure selectSalaryStructureById(Long id)
    {
        return salaryStructureMapper.selectByPrimaryKey(id);
    }


    @Override
    public List<SalaryStructure> selectSalaryStructureList(SalaryStructure salaryStructure)
    {
        if (salaryStructure.getMonthDate() != null){
            salaryStructure.setBeginOfMonth(DateUtil.beginOfMonth(salaryStructure.getMonthDate()));
            salaryStructure.setEndOfMonth(DateUtil.endOfMonth(salaryStructure.getMonthDate()));
        }

        List<SalaryStructure> salaryStructures = salaryStructureMapper.selectSalaryStructureList(salaryStructure);
        return salaryStructures;
    }

    @Override
    public List<SalaryStructure> selectEmpSalaryStructureList(SalaryStructure salaryStructure,String startNonManagerDate,String endNonManagerDate
            ,String startAdjustDate,String endAdjustDate)
    {
//        if (salaryStructure.getMonthDate() != null){
//            salaryStructure.setBeginOfMonth(DateUtil.beginOfMonth(salaryStructure.getMonthDate()));
//            salaryStructure.setEndOfMonth(DateUtil.endOfMonth(salaryStructure.getMonthDate()));
//        }

        List<SalaryStructure> salaryStructures = salaryStructureMapper.selectEmpSalaryStructureList(salaryStructure,startNonManagerDate,endNonManagerDate
                                                                                    ,startAdjustDate,endAdjustDate);
        return salaryStructures;
    }

    /**
     * 查询薪资结构列表
     * 
     * @param salaryStructure 薪资结构
     * @return 薪资结构
     */
    @Override
    public List<SalaryStructure> selectSalaryStructureListInfo(SalaryStructure salaryStructure)
    {
        if (salaryStructure.getMonthDate() != null){
            salaryStructure.setBeginOfMonth(DateUtil.beginOfMonth(salaryStructure.getMonthDate()));
            salaryStructure.setEndOfMonth(DateUtil.endOfMonth(salaryStructure.getMonthDate()));
        }

        List<SalaryStructure> salaryStructures = salaryStructureMapper.selectSalaryStructureListInfo(salaryStructure);
        HrEmp hrEmp = null;
        SysDept sysDept = null;
        String[] split = null;
        SysDept sysDeptTemp = null;
        HrAttendanceGroup hrAttendanceGroup = null;
        SysPost sysPost = null;
        DateTime monthDate = null;
        HrAttendanceStatistics hrAttendanceStatistics = null;
        for (SalaryStructure structure : salaryStructures) {
            hrEmp = hrEmpService.selectTHrEmpById(structure.getEmpId());
            structure.setEmpNum(hrEmp.getEmpNum());
            structure.setNonManagerDate(hrEmp.getNonManagerDate());

            sysDept = sysDeptService.selectDeptById(hrEmp.getDeptId());
            split = sysDept.getAncestors().split(",");
            if ( split.length >= 3 ){
                sysDeptTemp = sysDeptService.selectDeptById(Long.parseLong(split[2]));
                if (sysDeptTemp != null){
                    structure.setDeptName1(sysDeptTemp.getDeptName());
                }
            }
            if ( split.length >= 4 ){
                sysDeptTemp = sysDeptService.selectDeptById(Long.parseLong(split[3]));
                if (sysDeptTemp != null){
                    structure.setDeptName2(sysDeptTemp.getDeptName());
                }
            }

            hrAttendanceGroup = attendanceGroupService.selectGroupByEmpId(hrEmp.getEmpId());
            if (hrAttendanceGroup != null){
                structure.setAttGroup(hrAttendanceGroup.getName());
            }

            sysPost = sysPostService.selectPostById(hrEmp.getPostId());
            structure.setPostName(sysPost.getPostName());

            monthDate = DateUtil.date(structure.getMonthDate());
            hrAttendanceStatistics = hrAttendanceStatisticsService.selectSingleOneByCondition(hrEmp.getEmpId(), monthDate.year(), monthDate.month() + 1);
            if ( hrAttendanceStatistics != null ){
                structure.setShouldAttendance(hrAttendanceStatistics.getShouldAttendance());
                structure.setActualAttendance(hrAttendanceStatistics.getActualAttendance());
            }
        }

        return salaryStructures;
    }

    /**
     * 查询薪资结构列表
     *
     * @param salaryStructure 薪资结构
     * @return 薪资结构
     */
    @Override
    public List<SalaryStructure> selectSalaryStructureListEx(SalaryStructure salaryStructure)
    {
        if (salaryStructure.getMonthDate() != null){
            salaryStructure.setBeginOfMonth(DateUtil.beginOfMonth(salaryStructure.getMonthDate()));
            salaryStructure.setEndOfMonth(DateUtil.endOfMonth(salaryStructure.getMonthDate()));
        }

        List<SalaryStructure> salaryStructures = salaryStructureMapper.selectSalaryStructureListEx(salaryStructure);

        return salaryStructures;
    }

    /**
     * 新增薪资结构
     * 
     * @param salaryStructure 薪资结构
     * @return 结果
     */
    @Override
    public int insertSalaryStructure(SalaryStructure salaryStructure)
    {
        salaryStructure.setCreateTime(DateUtils.getNowDate());
        return salaryStructureMapper.insertSelective(salaryStructure);
    }

    /**
     * 修改薪资结构
     * 
     * @param salaryStructure 薪资结构
     * @return 结果
     */
    @Override
    public int updateSalaryStructure(SalaryStructure salaryStructure)
    {
        salaryStructure.setUpdateTime(DateUtils.getNowDate());
        return salaryStructureMapper.updateByPrimaryKeySelective(salaryStructure);
    }

    @Override
    public int updateSalary(SalaryStructure salaryStructure)
    {
        salaryStructure.setUpdateId(ShiroUtils.getUserId());
        salaryStructure.setUpdateBy(ShiroUtils.getLoginName());
        salaryStructure.setUpdateTime(DateUtils.getNowDate());
        return salaryStructureMapper.updateByPrimaryKeySelective(salaryStructure);
    }

    /**
     * 删除薪资结构对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSalaryStructureByIds(String ids)
    {
        return salaryStructureMapper.deleteSalaryStructureByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除薪资结构信息
     * 
     * @param id 薪资结构ID
     * @return 结果
     */
    @Override
    public int deleteSalaryStructureById(Long id)
    {
        return salaryStructureMapper.deleteSalaryStructureById(id);
    }

    @Override
    @Transactional
    public int insertSalaryStructureProcess(SalaryStructure salaryStructure) {
//        if (salaryStructure.getOldId() == null){
//            return 0;
//        }
//
//        Example example = new Example(SalaryStructure.class);
//        example.createCriteria().andEqualTo("empId",salaryStructure.getEmpId())
//                                .andIsNotNull("instanceId").andIn("auditStatus",Arrays.asList( new Integer[]{1,0}));
//
//        List<SalaryStructure> salaryStructures = selectSalaryStructuresByExample(example);
//        if (!salaryStructures.isEmpty()){
//            throw new BusinessException("有正在审批的薪资调整申请，不能重复提交！");
//        }

        salaryStructure.setEmpName(hrEmpService.selectTHrEmpById(salaryStructure.getEmpId()).getEmpName());
        salaryStructure.setApplyUser(ShiroUtils.getUserName());
        salaryStructure.setApplyUserName(ShiroUtils.getLoginName());
        salaryStructure.setApplyTime(new Date());
        salaryStructure.setMonthDate(salaryStructure.getAdjustDate());
        salaryStructure.setCreateId(ShiroUtils.getUserId());
        salaryStructure.setCreateBy(ShiroUtils.getLoginName());
        int i = insertSalaryStructure(salaryStructure);
//        if (i > 0){
//            salaryStructure.setUpdateBy(ShiroUtils.getLoginName());
//            salaryStructure.setUpdateId(ShiroUtils.getUserId());
//
//            SysUser sysUser1 = sysUserService.selectUserById(salaryStructure.getEmpId());
//            salaryStructure.setAncestors(sysUser1.getDept().getAncestors());
//
//            //修改流程状态为审核中
//            salaryStructure.setAuditStatus(1);
//            // 实体类 ID，作为流程的业务 key
//
//            String businessKey = salaryStructure.getId().toString();
//
//            Map<String,Object> values = new HashMap<>();
//            processHandleService.setAssignee(ProcessKey.userDefined01Salary,values,salaryStructure.getDeptId(),(JSONObject) JSON.toJSON(salaryStructure));
//
//
//            // 建立双向关系
//            salaryStructure.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01Salary,businessKey,ShiroUtils.getLoginName(),"薪资调整",values).getProcessInstanceId());
//            salaryStructureMapper.updateByPrimaryKeySelective(salaryStructure);
//        }
        return i;
    }

    @Override
    public List<SalaryStructure> selectSalaryStructuresByExample(Example example) {
        return salaryStructureMapper.selectByExample(example);
    }

    @Override
    public List<SalaryStructure> selectSalaryStructureListNew(SalaryStructure selectSalary) {
        if (selectSalary.getMonthDate() != null){
            selectSalary.setBeginOfMonth(DateUtil.beginOfMonth(selectSalary.getMonthDate()));
            selectSalary.setEndOfMonth(DateUtil.endOfMonth(selectSalary.getMonthDate()));
        }
        return salaryStructureMapper.selectSalaryStructureListNew(selectSalary);
    }

    @Override
    @Transactional
    public AjaxResult complete(SalaryStructure salaryStructure, String taskId, HttpServletRequest request) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        salaryStructure = salaryStructureMapper.selectByPrimaryKey(salaryStructure.getId());
        Map<String, Object> variables = new HashMap<String, Object>();
        processHandleService.setAssignee(ProcessKey.userDefined01Salary,variables,null,taskId, (JSONObject)JSON.toJSON(salaryStructure));

        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean appType = BooleanUtils.toBoolean(p_B_approved);
        if (appType){
            salaryStructure.setAuditStatus(2);
            //审批通过
            processHandleService.complete(salaryStructure.getInstanceId(),ProcessKey.userDefined01Salary,"薪资调整",taskId,variables,
                    salaryStructure.getAuditStatus().equals(2),comment,BooleanUtils.toBoolean(p_B_approved));

            List<Task> list = taskService.createTaskQuery().processInstanceId(salaryStructure.getInstanceId()).active().list();
            if (list.size() < 1){
                //流程结束了跟新下数据状态
                SalaryStructure oldSalay = salaryStructureMapper.selectByPrimaryKey(salaryStructure.getOldId());

                salaryStructure.setPerformanceBonusActual(oldSalay.getPerformanceBonusActual());
                salaryStructure.setAttendanceBonus(oldSalay.getAttendanceBonus());
                salaryStructure.setNightAllowance(oldSalay.getNightAllowance());
                salaryStructure.setOvertimeActual(oldSalay.getOvertimeActual());
                salaryStructure.setOtherSubsidiesActual(oldSalay.getOtherSubsidiesActual());
                salaryStructure.setAmortization(oldSalay.getAmortization());

                salaryStructure.setDeductionForUtilities(oldSalay.getDeductionForUtilities());
                salaryStructure.setDeductionForOther(oldSalay.getDeductionForOther());
                salaryStructure.setSocialSecurity(oldSalay.getSocialSecurity());
                salaryStructure.setAccumulationFund(oldSalay.getAccumulationFund());
                salaryStructure.setTallage(oldSalay.getTallage());

                salaryStructure.setBasicActual(oldSalay.getBasicActual());
                salaryStructure.setOvertimePayActual(oldSalay.getOvertimePayActual());
                salaryStructure.setAllowanceActual(oldSalay.getAllowanceActual());
                //应发合计=（基本工资+加班费+岗位补贴+绩效工资+提成+其他补贴+全勤奖+夜班补贴+平时加班费）
                salaryStructure.setLaballot(oldSalay.getLaballot());

                salaryStructure.setPretaxIncome(oldSalay.getPretaxIncome());

                salaryStructure.setNetPayroll(oldSalay.getNetPayroll());

                salaryStructureMapper.updateByPrimaryKeySelective(salaryStructure);
                //流程通过

                if (oldSalay != null){
                    oldSalay.setIsHistory(1);
                    salaryStructureMapper.updateByPrimaryKeySelective(oldSalay);
                }

            }
        }else {
            salaryStructure.setAuditStatus(3);
            salaryStructureMapper.updateByPrimaryKeySelective(salaryStructure);
            //审批拒绝
            processHandleService.completeDown(salaryStructure.getInstanceId(),taskId,variables,
                    comment,BooleanUtils.toBoolean(p_B_approved));
        }
        return AjaxResult.success("任务已完成");
    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        //维护业务数据
        Example example = new Example(HrQuit.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        SalaryStructure salaryStructure = salaryStructureMapper.selectSingleOneByExample(example);
        salaryStructure.setAuditStatus(4);
        salaryStructureMapper.updateByPrimaryKeySelective(salaryStructure);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    @Override
    public SalaryStructure selectOneBySalaryStructure(SalaryStructure salaryStructure) {
        if (salaryStructure.getMonthDate() != null){
            salaryStructure.setBeginOfMonth(DateUtil.beginOfMonth(salaryStructure.getMonthDate()));
            salaryStructure.setEndOfMonth(DateUtil.endOfMonth(salaryStructure.getMonthDate()));
        }

        return salaryStructureMapper.selectOneBySalaryStructure(salaryStructure);
    }

    @Override
    public void insertSalaryStructureDTO(SalaryStructureDTO salaryStructureDTO, AtomicInteger successCount, List<SalaryDTOError> errorList) {

        if (StringUtil.isEmpty(salaryStructureDTO.getEmpNum()) || StringUtil.isEmpty(salaryStructureDTO.getMonthDate())){
            addErrorDTO(salaryStructureDTO, errorList, "工号或日期为空");
            return;
        }


        if (StringUtil.isNotEmpty(salaryStructureDTO.getEmpNum())){
            HrEmp hrEmp = hrEmpService.selectTHrEmpByNumber(salaryStructureDTO.getEmpNum());
            if (hrEmp != null){
                SalaryStructure selectSalary = new SalaryStructure();
                selectSalary.setEmpId(hrEmp.getEmpId());
                selectSalary.setDelFlag("0");
                selectSalary.setAuditStatus(2);
                selectSalary.setIsHistory(0);
                DateTime monthDate;
                try {
                    monthDate = DateUtil.parse(salaryStructureDTO.getMonthDate(), "yyyy年MM");
                }catch (Exception e){
                    e.printStackTrace();
                    salaryStructureDTO.setMonthDate("");
                    addErrorDTO(salaryStructureDTO, errorList, "日期格式不正确,正确格式：yyyy年MM");
                    return;
                }
                selectSalary.setMonthDate(monthDate);//这个月有没有数据
                SalaryStructure salaryExample = selectOneBySalaryStructure(selectSalary);
                if (salaryExample != null){
                    salaryExample.setPerformanceBonusActual(salaryStructureDTO.getPerformanceBonusActual());
                    salaryExample.setAttendanceBonus(salaryStructureDTO.getAttendanceBonus());
                    salaryExample.setNightAllowance(salaryStructureDTO.getNightAllowance());
                    salaryExample.setOvertimeActual(salaryStructureDTO.getOvertimeActual());
                    salaryExample.setOtherSubsidiesActual(salaryStructureDTO.getOtherSubsidiesActual());
                    salaryExample.setAmortization(salaryStructureDTO.getAmortization());

                    salaryExample.setDeductionForUtilities(salaryStructureDTO.getDeductionForUtilities());
                    salaryExample.setDeductionForOther(salaryStructureDTO.getDeductionForOther());
                    salaryExample.setSocialSecurity(salaryStructureDTO.getSocialSecurity());
                    salaryExample.setAccumulationFund(salaryStructureDTO.getAccumulationFund());
                    salaryExample.setTallage(salaryStructureDTO.getTallage());

                    salaryExample.setRemark("导入薪资数据");
                    updateSalary(salaryExample);
                    //记录一条成功数据
                    successCount.incrementAndGet();
                    return;
                }else {
                    //薪资数据的新增在导入员工数据时操作
                    addErrorDTO(salaryStructureDTO, errorList, "无薪资数据不能直接更新，薪资数据的新增在导入员工数据时操作" +
                            "，否则需在管理菜单中操作");
                }
            }
        }

        addErrorDTO(salaryStructureDTO, errorList, "根据工号找不到员工数据");

    }


    private void addErrorDTO(SalaryStructureDTO salaryStructureDTO, List<SalaryDTOError> errorList, String remark) {
        SalaryDTOError salaryDTOError = new SalaryDTOError();
        BeanUtil.copyProperties(salaryStructureDTO, salaryDTOError);
        salaryDTOError.setErrorInfo(remark);
        errorList.add(salaryDTOError);
    }

}
