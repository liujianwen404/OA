package com.ruoyi.hr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.HrOvertime;
import com.ruoyi.base.domain.HrQuit;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.FinancePaymentMapper;
import com.ruoyi.base.domain.FinancePayment;
import com.ruoyi.hr.service.IFinancePaymentService;
import com.ruoyi.common.core.text.Convert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 财务付款流程Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-10-26
 */
@Service
public class FinancePaymentServiceImpl implements IFinancePaymentService 
{

    private static final Logger logger = LoggerFactory.getLogger(FinancePaymentServiceImpl.class);

    @Resource
    private FinancePaymentMapper financePaymentMapper;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private TaskService taskService;

    @Resource
    private ISysUserService userService;


    /**
     * 查询财务付款流程
     * 
     * @param id 财务付款流程ID
     * @return 财务付款流程
     */
    @Override
    public FinancePayment selectFinancePaymentById(Long id)
    {
        return financePaymentMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询财务付款流程列表
     * 
     * @param financePayment 财务付款流程
     * @return 财务付款流程
     */
    @Override
    public List<FinancePayment> selectFinancePaymentList(FinancePayment financePayment)
    {
        List<FinancePayment> financePayments = financePaymentMapper.selectFinancePaymentList(financePayment);
        for (FinancePayment payment: financePayments) {
            SysUser sysUser = userService.selectUserByLoginName(payment.getCreateBy());
            if (sysUser != null) {
                payment.setCreateUserName(sysUser.getUserName());
                payment.setApplyUserName(sysUser.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(payment.getInstanceId())) {
                // 例如加班会签，会同时拥有多个任务
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(payment.getInstanceId()).list();
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    String assignee = processHandleService.selectToUserNameByAssignee(task);
                    payment.setTaskId(task.getId());
                    payment.setTaskName(task.getName());
                    payment.setTodoUserName(assignee);
                } else {
                    payment.setTaskName("已办结");
                }
            } else {
                payment.setTaskName("未启动");
            }
        }
        return financePayments;
    }

    /**
     * 新增财务付款流程
     * 
     * @param financePayment 财务付款流程
     * @return 结果
     */
    @Override
    public int insertFinancePayment(FinancePayment financePayment)
    {
        financePayment.setCreateId(ShiroUtils.getUserId());
        financePayment.setCreateBy(ShiroUtils.getLoginName());
        financePayment.setApplyUserName(ShiroUtils.getUserName());
        financePayment.setCreateTime(DateUtils.getNowDate());
        String attachment = financePayment.getAttachment();
        String path = financePayment.getPath();
        String newAttachment = removeNullStringArray(attachment);
        String newPath = removeNullStringArray(path);
        if(StringUtils.isNotBlank(newAttachment)){
            financePayment.setAttachment(StringUtils.strip(newAttachment,","));
        }if(StringUtils.isNotBlank(newPath)){
            financePayment.setPath(StringUtils.strip(newPath,","));
        }
        return financePaymentMapper.insertSelective(financePayment);
    }

    public String removeNullStringArray(String name){
        if(StringUtils.isNotBlank(name)){
            String[] nameArray = name.split(",");
            List<String> tmp = new ArrayList<String>();
            for(String str:nameArray){
                if(str != null && str != ""){
                    tmp.add(str);
                }
            }
            return StringUtils.strip(tmp.toString(),"[]");
        }
        return name;
    }

    /**
     * 修改财务付款流程
     * 
     * @param financePayment 财务付款流程
     * @return 结果
     */
    @Override
    public int updateFinancePayment(FinancePayment financePayment)
    {
        financePayment.setUpdateId(ShiroUtils.getUserId());
        financePayment.setUpdateBy(ShiroUtils.getLoginName());
        financePayment.setUpdateTime(DateUtils.getNowDate());
        String attachment = financePayment.getAttachment();
        String path = financePayment.getPath();
        String newAttachment = removeNullStringArray(attachment);
        String newPath = removeNullStringArray(path);
        if(StringUtils.isNotBlank(newAttachment)){
            financePayment.setAttachment(newAttachment);
        }if(StringUtils.isNotBlank(newPath)){
            financePayment.setPath(newPath);
        }
        return financePaymentMapper.updateByPrimaryKeySelective(financePayment);
    }

    /**
     * 删除财务付款流程对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFinancePaymentByIds(String ids)
    {
        return financePaymentMapper.deleteFinancePaymentByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除财务付款流程信息
     * 
     * @param id 财务付款流程ID
     * @return 结果
     */
    @Override
    public int deleteFinancePaymentById(Long id)
    {
        return financePaymentMapper.deleteFinancePaymentById(id);
    }



    @Override
    public FinancePayment selectSingleOneByExample(Example example){
        return financePaymentMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<FinancePayment> selectByExample(Example example){
        return financePaymentMapper.selectByExample(example);
    }

    @Override
    @Transactional
    public AjaxResult submitApply(FinancePayment financePayment, String loginName) {
        financePayment.setCreateBy(loginName);
        financePayment.setCreateTime(DateUtils.getNowDate());
        financePayment.setUpdateBy(loginName);
        //修改流程状态为审核中
        financePayment.setAuditStatus("1");
        // 实体类 ID，作为流程的业务 key
        String businessKey = financePayment.getId().toString();
        HashMap<String, Object> variables = new HashMap<>();
//        variables.put("deptLeader",userService.selectDeptLeaderByLoginName(loginName));
        variables.put("createBy",loginName);
        processHandleService.setAssignee(ProcessKey.financePayment,variables,financePayment.getDeptId(),(JSONObject) JSON.toJSON(financePayment));

        // 建立双向关系
        financePayment.setInstanceId(processHandleService.
                submitApply(ProcessKey.financePayment,businessKey,loginName,financePayment.getTitle(),variables).getProcessInstanceId());
        financePaymentMapper.updateByPrimaryKey(financePayment);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult complete(FinancePayment financePayment, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();
        String taskId = financePayment.getTaskId();
        processHandleService.setAssignee(ProcessKey.financePayment,variables,financePayment.getDeptId(),taskId, (JSONObject)JSON.toJSON(financePayment));

        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean toBoolean = BooleanUtils.toBoolean(p_B_approved);

        if (toBoolean){
            //审批通过
            financePayment.setAuditStatus("2");

            processHandleService.complete(financePayment.getInstanceId(),ProcessKey.financePayment,financePayment.getTitle(),taskId,variables,
                    financePayment.getAuditStatus().equals("2"),comment,BooleanUtils.toBoolean(p_B_approved));

            List<Task> list = taskService.createTaskQuery().processInstanceId(financePayment.getInstanceId()).active().list();
            if (list.size() < 1){
                //流程结束了更新数据状态
                financePaymentMapper.updateByPrimaryKey(financePayment);
            }
        }else {
            //审批拒绝
            financePayment.setAuditStatus("3");
            financePaymentMapper.updateByPrimaryKey(financePayment);
            processHandleService.completeDown(financePayment.getInstanceId(),taskId,variables,
                    comment,BooleanUtils.toBoolean(p_B_approved));
        }
        return AjaxResult.success("任务已完成");
    }

    @Override
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        //维护业务数据
        Example example = new Example(FinancePayment.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        FinancePayment financePayment = financePaymentMapper.selectSingleOneByExample(example);
        financePayment.setAuditStatus("4");
        financePaymentMapper.updateByPrimaryKeySelective(financePayment);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

}
