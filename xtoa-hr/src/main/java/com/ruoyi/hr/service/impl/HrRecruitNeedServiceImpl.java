package com.ruoyi.hr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hr.manager.ProcessManager;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.process.general.domain.HistoricActivity;
import com.ruoyi.process.general.service.IProcessService;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrRecruitNeedMapper;
import com.ruoyi.hr.domain.HrRecruitNeed;
import com.ruoyi.hr.service.IHrRecruitNeedService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 招聘需求Service业务层处理
 * 
 * @author xt
 * @date 2020-05-28
 */
@Service
public class HrRecruitNeedServiceImpl implements IHrRecruitNeedService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrRecruitNeedServiceImpl.class);

    @Autowired
    private HrRecruitNeedMapper hrRecruitNeedMapper;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IProcessService processService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private IBizTodoItemService bizTodoItemService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Resource
    private ISysUserService userService;

    @Autowired
    private ProcessManager processManager;

    /**
     * 查询招聘需求
     * 
     * @param recruitNeedId 招聘需求ID
     * @return 招聘需求
     */
    @Override
    public HrRecruitNeed selectHrRecruitNeedById(Long recruitNeedId)
    {
        return hrRecruitNeedMapper.selectByPrimaryKey(recruitNeedId);
    }

    /**
     * 查询招聘需求列表
     * 
     * @param hrRecruitNeed 招聘需求
     * @return 招聘需求
     */
    @Override
    public List<HrRecruitNeed> selectHrRecruitNeedList(HrRecruitNeed hrRecruitNeed)
    {
        hrRecruitNeed.setDelFlag("0");
        return hrRecruitNeedMapper.selectHrRecruitNeedList(hrRecruitNeed);
    }

    /**
     * 新增招聘需求
     * 
     * @param hrRecruitNeed 招聘需求
     * @return 结果
     */
    @Override
    @Transactional
    public int insertHrRecruitNeed(HrRecruitNeed hrRecruitNeed)
    {
        hrRecruitNeed.setCreateId(ShiroUtils.getUserId());
        hrRecruitNeed.setCreateBy(ShiroUtils.getLoginName());
        hrRecruitNeed.setCreateTime(DateUtils.getNowDate());
        int i = hrRecruitNeedMapper.insertSelective(hrRecruitNeed);
        if (i > 0){
            starProcess(hrRecruitNeed);
        }
        return i;
    }

    /**
     * 修改招聘需求
     * 
     * @param hrRecruitNeed 招聘需求
     * @return 结果
     */
    @Override
    public int updateHrRecruitNeed(HrRecruitNeed hrRecruitNeed)
    {
        hrRecruitNeed.setUpdateId(ShiroUtils.getUserId());
        hrRecruitNeed.setUpdateBy(ShiroUtils.getLoginName());
        hrRecruitNeed.setUpdateTime(DateUtils.getNowDate());
        return hrRecruitNeedMapper.updateByPrimaryKeySelective(hrRecruitNeed);
    }

    /**
     * 删除招聘需求对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrRecruitNeedByIds(String ids)
    {
        return hrRecruitNeedMapper.deleteHrRecruitNeedByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除招聘需求信息
     * 
     * @param recruitNeedId 招聘需求ID
     * @return 结果
     */
    @Override
    public int deleteHrRecruitNeedById(Long recruitNeedId)
    {
        return hrRecruitNeedMapper.deleteHrRecruitNeedById(recruitNeedId);
    }

    public void starProcess(HrRecruitNeed hrRecruitNeed) {
        Long userId = hrRecruitNeed.getCreateId();
        SysUser sysUser = userService.selectUserById(userId);
        String loginName = sysUser.getLoginName();

        hrRecruitNeed.setCreateBy(loginName);
        hrRecruitNeed.setCreateTime(DateUtils.getNowDate());
        hrRecruitNeed.setUpdateBy(loginName);
        //修改流程状态为审核中
        hrRecruitNeed.setAuditStatus(1);
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrRecruitNeed.getRecruitNeedId().toString();
        Map<String,Object> variables = new HashMap<>();

        List deptLeaderLoginNames = processManager.getDeptLeaderLoginNames(userId,3);
        List directLeaderLoginNames = processManager.getDeptLeaderLoginNames(userId,1);
        int companyNum = processManager.getCompany(userId);
        int firstDeptNum = processManager.getFirstDept(userId);
        boolean isEast = processManager.isEast(userId);

        variables.put("isEast",isEast);
        variables.put("directLeader",directLeaderLoginNames);
        variables.put("deptLeaders",deptLeaderLoginNames);
        variables.put("companyNum",companyNum);
        variables.put("firstDeptNum",firstDeptNum);

        //更新业务住数据
        hrRecruitNeed.setInstanceId(processHandleService.submitApply(ProcessKey.recruitNeed,businessKey,loginName,"招聘需求",variables).getProcessInstanceId());
        hrRecruitNeedMapper.updateByPrimaryKeySelective(hrRecruitNeed);
    }

    @Override
    @Transactional
    public AjaxResult complete(String taskId, HttpServletRequest request, HrRecruitNeed hrRecruitNeed, String comment, String p_b_Approved, String endHr) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String assignee = task.getAssignee();

        hrRecruitNeed = hrRecruitNeedMapper.selectByPrimaryKey(hrRecruitNeed.getRecruitNeedId());
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        Map<String, Object> map = new HashMap<>();
        if (task.getTaskDefinitionKey().equals("hrAdmin")) {
            if ("true".equals(p_b_Approved)) {
                if (StringUtils.isEmpty(endHr)){
                    throw new BusinessException("请指定人事确认");
                }
                //同意
                map.put("type", 1);
                map.put("endHr", endHr);
            } else {
                //驳回
                map.put("type", 0);
                hrRecruitNeed.setAuditStatus(3);
                hrRecruitNeedMapper.updateByPrimaryKeySelective(hrRecruitNeed);
            }
        } else if (task.getTaskDefinitionKey().equals("endHr")) {
            //人事调整申请
            if ("true".equals(p_b_Approved)) {
                //同意
                map.put("type", 1);
                hrRecruitNeed.setAuditStatus(2);
                hrRecruitNeedMapper.updateByPrimaryKeySelective(hrRecruitNeed);
            } else {
                //驳回
                map.put("type", 0);
            }
        }
        taskService.claim(taskId, ShiroUtils.getLoginName());
        if (StringUtils.isNotEmpty(comment)) {
            taskService.addComment(taskId, hrRecruitNeed.getInstanceId(), comment);
        }
        taskService.complete(taskId, map);
        Long todoId = bizTodoItemService.updateTodoItem(taskId,"true".equals(p_b_Approved) ? "0" : "1");


        int item = bizTodoItemService.insertTodoItem(hrRecruitNeed.getInstanceId(), processInstance.getProcessDefinitionName(), ProcessKey.recruitNeed,
                hrRecruitNeed.getAuditStatus() != 3,todoId,processInstance.getStartUserId());

        return AjaxResult.success("任务已完成");
    }

    @Override
    public void showVerifyDialog(String taskId, String module, String activitiId, ModelMap mmap, String instanceId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String businessKey ;
        String startUserId ;
        if (task == null){
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            businessKey = historicProcessInstance.getBusinessKey();
            startUserId = historicProcessInstance.getStartUserId();
        }else {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            businessKey = processInstance.getBusinessKey();
            startUserId = processInstance.getStartUserId();
        }

        HrRecruitNeed hrRecruitNeed = hrRecruitNeedMapper.selectByPrimaryKey(new Long(businessKey));
        hrRecruitNeed.setApplyUserName(startUserId);
        hrRecruitNeed.setTaskId(taskId);
        mmap.put("hrRecruitNeed", hrRecruitNeed);
        mmap.put("taskId", taskId);

        if (task != null && task.getTaskDefinitionKey().equals("endHr")){
            mmap.put("taskName", 1);
        }else {
            mmap.put("taskName", 0);
        }

        List<HistoricActivity> list = processService.selectHistoryList(instanceId, new HistoricActivity());
        mmap.put("historicActivity", list);
    }
}
