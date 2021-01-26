package com.ruoyi.hr.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hr.domain.HrNonManager;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.process.general.domain.HistoricActivity;
import com.ruoyi.process.general.service.IProcessService;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrJobTransferMapper;
import com.ruoyi.hr.domain.HrJobTransfer;
import com.ruoyi.hr.service.IHrJobTransferService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 调动申请Service业务层处理
 * 
 * @author xt
 * @date 2020-05-22
 */
@Service
public class HrJobTransferServiceImpl implements IHrJobTransferService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrJobTransferServiceImpl.class);

    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private HrJobTransferMapper hrJobTransferMapper;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IBizTodoItemService bizTodoItemService;

    @Autowired
    private IProcessService processService;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private ProcessHandleService processHandleService;
    /**
     * 查询调动申请
     * 
     * @param jobTransferId 调动申请ID
     * @return 调动申请
     */
    @Override
    public HrJobTransfer selectHrJobTransferById(Long jobTransferId)
    {
        return hrJobTransferMapper.selectByPrimaryKey(jobTransferId);
    }

    /**
     * 查询调动申请列表
     * 
     * @param hrJobTransfer 调动申请
     * @return 调动申请
     */
    @Override
    public List<HrJobTransfer> selectHrJobTransferList(HrJobTransfer hrJobTransfer)
    {
        hrJobTransfer.setDelFlag("0");
        List<HrJobTransfer> hrJobTransfers = hrJobTransferMapper.selectHrJobTransferList(hrJobTransfer);
        SysDept nowLeader = null;
        SysDept newLeader = null;
        for (HrJobTransfer hrJob : hrJobTransfers){
            setInfo(hrJob);
        }

        return hrJobTransfers;
    }
    @Override
    public List<HrJobTransfer> selectHrJobTransferListManage(HrJobTransfer hrJobTransfer)
    {
        hrJobTransfer.setDelFlag("0");
        List<HrJobTransfer> hrJobTransfers = hrJobTransferMapper.selectHrJobTransferListManage(hrJobTransfer);
        SysDept nowLeader = null;
        SysDept newLeader = null;
        for (HrJobTransfer hrJob : hrJobTransfers){
            setInfo(hrJob);
        }

        return hrJobTransfers;
    }

    private void setInfo(HrJobTransfer hrJob) {
        SysDept nowLeader;
        SysDept newLeader;
        nowLeader = iSysDeptService.selectDeptById(hrJob.getCurrentDeptId());
        if (nowLeader != null) {
            hrJob.setNowLeaderName(nowLeader.getLeaderName());
        }
        newLeader = iSysDeptService.selectDeptById(hrJob.getJobTransferDeptId());
        if (newLeader != null) {
            hrJob.setNewleaderName(newLeader.getLeaderName());
        }

        if (StringUtils.isNotBlank(hrJob.getInstanceId())) {
            // 例如请假会签，会同时拥有多个任务
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(hrJob.getInstanceId()).list();
            if (!CollectionUtils.isEmpty(taskList)) {
                Task task = taskList.get(0);
                hrJob.setTaskId(task.getId());
                hrJob.setTaskName(task.getName());
                SysUser sysUser3 = userMapper.selectUserByLoginName(task.getAssignee());
                if (sysUser3 != null) {
                    hrJob.setTodoUserName(sysUser3.getUserName());
                }
            } else {
                hrJob.setTaskName("已办结");
            }
        } else {
            hrJob.setTaskName("未启动");
        }
    }

    /**
     * 查询调动申请列表
     *
     * @param hrJobTransfer 调动申请
     * @return 调动申请
     */
    @Override
    public List<HrJobTransfer> selectHrJobTransferList2(HrJobTransfer hrJobTransfer)
    {
        hrJobTransfer.setDelFlag("0");
        List<HrJobTransfer> hrJobTransfers = hrJobTransferMapper.selectHrJobTransferList2(hrJobTransfer);
        SysDept nowLeader = null;
        SysDept newLeader = null;
        for (HrJobTransfer hrJob : hrJobTransfers){
            setInfo(hrJob);
        }

        return hrJobTransfers;
    }

    /**
     * 新增调动申请
     * 
     * @param hrJobTransfer 调动申请
     * @return 结果
     */
    @Override
    public int insertHrJobTransfer(HrJobTransfer hrJobTransfer)
    {
        hrJobTransfer.setCreateId(ShiroUtils.getUserId());
        hrJobTransfer.setCreateBy(ShiroUtils.getLoginName());
        hrJobTransfer.setCreateTime(DateUtils.getNowDate());

        hrJobTransfer.setApplyUser(ShiroUtils.getLoginName());
        hrJobTransfer.setApplyUserName(ShiroUtils.getUserName());

        SysDept sysDept = iSysDeptService.selectDeptById(hrJobTransfer.getCurrentDeptId());
        if (sysDept != null && sysDept.getStatus().equals("0") && sysDept.getLeader() != null){
            hrJobTransfer.setCurrentLeaderId(sysDept.getLeader()+"");
        }

        sysDept = iSysDeptService.selectDeptById(hrJobTransfer.getJobTransferDeptId());
        if (sysDept != null && sysDept.getStatus().equals("0") && sysDept.getLeader() != null){
            hrJobTransfer.setTransferLeaderId(sysDept.getLeader()+"");
        }


        if (StringUtil.isNotEmpty(hrJobTransfer.getAttachment())){
            String[] split = hrJobTransfer.getAttachment().split(",");

            split = ArrayUtil.filter(split, (Filter<String>) s -> {
                if (StringUtil.isEmpty(s)){
                    return false;
                }
                return true;
            });

            String join = ArrayUtil.join(split, ",");
            hrJobTransfer.setAttachment(join);
        }else {
            hrJobTransfer.setAttachment(null);
        }

        int i = hrJobTransferMapper.insertSelective(hrJobTransfer);
        if (i > 0){
            if (StringUtils.isEmpty(hrJobTransfer.getInstanceId()) && hrJobTransfer.getStatus().equals("1") ){
                starProcess(hrJobTransfer);
            }
        }

        return i;
    }

    /**
     * 修改调动申请
     * 
     * @param hrJobTransfer 调动申请
     * @return 结果
     */
    @Override
    public int updateHrJobTransfer(HrJobTransfer hrJobTransfer)
    {
        hrJobTransfer.setUpdateId(ShiroUtils.getUserId());
        hrJobTransfer.setUpdateBy(ShiroUtils.getLoginName());
        hrJobTransfer.setUpdateTime(DateUtils.getNowDate());
        return hrJobTransferMapper.updateByPrimaryKeySelective(hrJobTransfer);
    }

    /**
     * 删除调动申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrJobTransferByIds(String ids)
    {
        return hrJobTransferMapper.deleteHrJobTransferByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除调动申请信息
     * 
     * @param jobTransferId 调动申请ID
     * @return 结果
     */
    @Override
    public int deleteHrJobTransferById(Long jobTransferId)
    {
        return hrJobTransferMapper.deleteHrJobTransferById(jobTransferId);
    }

    @Override
    public AjaxResult commit(Long jobTransferId) {
        HrJobTransfer hrJobTransfer = hrJobTransferMapper.selectByPrimaryKey(jobTransferId);
        hrJobTransfer.setStatus("1");
        hrJobTransfer.setApplyTime(new Date());
        hrJobTransferMapper.updateByPrimaryKeySelective(hrJobTransfer);
        if (StringUtils.isEmpty(hrJobTransfer.getInstanceId()) && hrJobTransfer.getStatus().equals("1") ){
            starProcess(hrJobTransfer);
        }
        
        return AjaxResult.success();
    }

    private void starProcess(HrJobTransfer hrJobTransfer){
        identityService.setAuthenticatedUserId(ShiroUtils.getLoginName());
        HashMap<String, Object> variables = new HashMap<>();
        processHandleService.setAssignee(ProcessKey.userDefined01Transfer,variables,hrJobTransfer.getCurrentDeptId(),(JSONObject) JSON.toJSON(hrJobTransfer));
        //更新业务数据
        // 建立双向关系
        hrJobTransfer.setInstanceId(processHandleService.submitApply(
                ProcessKey.userDefined01Transfer,
                hrJobTransfer.getJobTransferId()+"",
                ShiroUtils.getLoginName(),
                hrJobTransfer.getEmpName() + "异动申请",variables).getProcessInstanceId());
        hrJobTransfer.setAuditStatus(1);
        hrJobTransfer.setApplyTime(new Date());
        hrJobTransfer.setApplyUser(ShiroUtils.getLoginName());
        hrJobTransfer.setApplyUserName(ShiroUtils.getUserName());
        hrJobTransferMapper.updateByPrimaryKeySelective(hrJobTransfer);
    }

    @Override
    public void showVerifyDialog(String taskId, String module, String formPageName, ModelMap mmap, String instanceId, HrJobTransfer jobTransfer) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String businessKey ;
        String startUserId ;
        if (task == null){
            if (StringUtil.isEmpty(instanceId)){
                HrJobTransfer job = new HrJobTransfer();
                job.setJobTransferId(jobTransfer.getJobTransferId());
                instanceId = hrJobTransferMapper.selectOne(job).getInstanceId();
            }
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            businessKey = historicProcessInstance.getBusinessKey();
            startUserId = historicProcessInstance.getStartUserId();
        }else {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            businessKey = processInstance.getBusinessKey();
            startUserId = processInstance.getStartUserId();
        }
        HrJobTransfer hrJobTransfer = hrJobTransferMapper.selectByPrimaryKey(new Long(businessKey));

        hrJobTransfer.setApplyUserName(startUserId);
        hrJobTransfer.setTaskId(taskId);
        mmap.put("entity", hrJobTransfer);
        mmap.put("taskId", taskId);

        List<HistoricActivity> list = processService.selectHistoryList(instanceId, new HistoricActivity());
        mmap.put("historicActivity", list);
    }

    @Override
    @Transactional
    public AjaxResult complete(String taskId, HttpServletRequest request, HrJobTransfer hrJobTransfer, String comment, String p_B_hrApproved) {
        // 审批意见(true或者是false)
        hrJobTransfer = hrJobTransferMapper.selectByPrimaryKey(hrJobTransfer.getJobTransferId());

        boolean approved = BooleanUtils.toBoolean(p_B_hrApproved);
        Map<String,Object> variables = new HashMap<>();
        processHandleService.setAssignee(ProcessKey.userDefined01Transfer,variables,hrJobTransfer.getCurrentDeptId(),taskId,(JSONObject) JSON.toJSON(hrJobTransfer));
        if(approved){
            //审批通过
            hrJobTransfer.setAuditStatus(2);
            processHandleService.complete(hrJobTransfer.getInstanceId(),ProcessKey.userDefined01Transfer,
                    hrJobTransfer.getEmpName() + "异动申请",taskId,variables,
                    hrJobTransfer.getAuditStatus().equals(2),comment, approved);
            List<Task> list = taskService.createTaskQuery().processInstanceId(hrJobTransfer.getInstanceId()).active().list();
            if(list.size() < 1) {
                //流程的活动节点数为0，流程走完，修改流程状态为通过
                hrJobTransfer.setAuditStatus(2);
                hrJobTransferMapper.updateByPrimaryKey(hrJobTransfer);
            }
        } else {
            //页面拒绝流程后，修改流程状态为拒绝
            hrJobTransfer.setAuditStatus(3);
            hrJobTransferMapper.updateByPrimaryKey(hrJobTransfer);
            processHandleService.completeDown(hrJobTransfer.getInstanceId(),taskId,variables,
                    comment, approved);
        }


        return AjaxResult.success("任务已完成");

    }



    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        boolean equals = processInstance.getStartUserId().equals(ShiroUtils.getLoginName());
        if (!equals){
            return AjaxResult.error("只有发起人才能撤销");
        }
        //删除临时代办表
        List<Task> chengxy = taskService.createTaskQuery().processInstanceId(instanceId).active().list();
        //删除流程
        runtimeService.deleteProcessInstance(instanceId,message);//删除流程
        //维护业务数据
        Example example = new Example(HrNonManager.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        HrJobTransfer hrJobTransfer = hrJobTransferMapper.selectSingleOneByExample(example);
        hrJobTransfer.setAuditStatus(4);
        hrJobTransferMapper.updateByPrimaryKeySelective(hrJobTransfer);

        for (Task task: chengxy){
            // 更新待办事项状态
            BizTodoItem query = new BizTodoItem();
            query.setTaskId(task.getId());
            // 考虑到候选用户组，会有多个 todoitem 办理同个 task
            List<BizTodoItem> updateList = CollectionUtils.isEmpty(bizTodoItemService.selectBizTodoItemList(query)) ? null : bizTodoItemService.selectBizTodoItemList(query);
            if (updateList != null) {
                for (BizTodoItem update : updateList) {
                    //  todoitem，将未办理的删除
                    if (update.getIsHandle().equals("0")) {
                        // 删除候选用户组其他 todoitem
                        bizTodoItemService.deleteBizTodoItemById(update.getId());
                    }
                }
            }
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();

        //插入记录数据
        BizTodoItem bizTodoItem = new BizTodoItem();
        bizTodoItem.setItemName(historicProcessInstance.getProcessDefinitionName());
        bizTodoItem.setItemContent("删除："+historicProcessInstance.getDeleteReason());
        bizTodoItem.setModule(historicProcessInstance.getProcessDefinitionKey());

        bizTodoItem.setNodeName("撤销");

        bizTodoItem.setIsView("1");
        bizTodoItem.setIsHandle("1");
        bizTodoItem.setTodoTime(new Date());
        bizTodoItem.setHandleTime(new Date());
        bizTodoItem.setStatus("3");
        bizTodoItem.setStarUserName(historicProcessInstance.getStartUserId());
        bizTodoItem.setInstanceId(instanceId);

        bizTodoItemService.insertBizTodoItem(bizTodoItem);

        return AjaxResult.success();
    }

}
