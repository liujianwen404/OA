package com.ruoyi.hr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.ruoyi.base.domain.HrBusinessTrip;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.domain.HrNonManager;
import com.ruoyi.hr.mapper.HrNonManagerMapper;
import com.ruoyi.hr.service.HrRecruitService;
import com.ruoyi.hr.service.IHrNonManagerService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.process.general.domain.HistoricActivity;
import com.ruoyi.process.general.service.IProcessService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysDeptService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 入职申请Service业务层处理
 * 
 * @author xt
 * @date 2020-05-14
 */
@Service
public class HrNonManagerServiceImpl implements IHrNonManagerService 
{

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private HrNonManagerMapper hrNonManagerMapper;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IProcessService processService;

    @Autowired
    private HrRecruitService hrRecruitService;

    @Autowired
    private ProcessHandleService processHandleService;

    /**
     * 查询入职申请
     * 
     * @param id 入职申请ID
     * @return 入职申请
     */
    @Override
    public HrNonManager selectHrNonManagerById(Long id)
    {
        return hrNonManagerMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询入职申请列表
     * 
     * @param hrNonManager 入职申请
     * @return 入职申请
     */
    @Override
    public List<HrNonManager> selectHrNonManagerList(HrNonManager hrNonManager)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        // PageHelper 仅对第一个 List 分页
        Page<HrNonManager> list = (Page<HrNonManager>) hrNonManagerMapper.selectHrNonManagerList(hrNonManager);
        Page<HrNonManager> returnList = new Page<>();
        for (HrNonManager manager: list) {
            setInfo(hrNonManager, manager);
            returnList.add(manager);
        }
        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
        returnList.setPageNum(pageNum);
        returnList.setPageSize(pageSize);
        return returnList;
    }
    @Override
    public List<HrNonManager> selectHrNonManagerListManage(HrNonManager hrNonManager)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        // PageHelper 仅对第一个 List 分页
        Page<HrNonManager> list = (Page<HrNonManager>) hrNonManagerMapper.selectHrNonManagerListManage(hrNonManager);
        Page<HrNonManager> returnList = new Page<>();
        for (HrNonManager manager: list) {
            setInfo(hrNonManager, manager);
            returnList.add(manager);
        }
        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
        returnList.setPageNum(pageNum);
        returnList.setPageSize(pageSize);
        return returnList;
    }
    @Override
    public List<HrNonManager> selectHrNonManagerListExport(HrNonManager hrNonManager)
    {
        // PageHelper 仅对第一个 List 分页
        List<HrNonManager> list = hrNonManagerMapper.selectHrNonManagerListManage(hrNonManager);
        Page<HrNonManager> returnList = new Page<>();
        for (HrNonManager manager: list) {
            setInfo(hrNonManager, manager);
            returnList.add(manager);
        }
        return returnList;
    }

    private void setInfo(HrNonManager hrNonManager, HrNonManager manager) {
        SysUser sysUser = userMapper.selectUserByLoginName(manager.getCreateBy());
        if (sysUser != null) {
            hrNonManager.setCreateUserName(sysUser.getUserName());
        }
        // 当前环节
        if (StringUtils.isNotBlank(manager.getInstanceId())) {
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(manager.getInstanceId())
//                        .singleResult();
                    .list();    // 例如请假会签，会同时拥有多个任务
            if (!CollectionUtils.isEmpty(taskList)) {
                Task task = taskList.get(0);
                manager.setTaskId(task.getId());
                manager.setTaskName(task.getName());
                SysUser sysUser3 = userMapper.selectUserByLoginName(task.getAssignee());
                if (sysUser3 != null) {
                    manager.setTodoUserName(sysUser3.getUserName());
                }
            } else {
                manager.setTaskName("已办结");
            }
        } else {
            manager.setTaskName("未启动");
        }
    }

    /**
     * 新增入职申请
     * 
     * @param hrNonManager 入职申请
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertHrNonManager(HrNonManager hrNonManager)
    {
        hrNonManager.setCreateId(ShiroUtils.getUserId());
        hrNonManager.setCreateBy(ShiroUtils.getLoginName());
        hrNonManager.setCreateTime(DateUtils.getNowDate());
        String attachment = hrNonManager.getAttachment();
        if(StringUtils.isNotBlank(attachment)){
            String substring = attachment.substring(1);
            hrNonManager.setAttachment(substring);
        }
        //设置审核人
        Long nonManagerDeptId = hrNonManager.getNonManagerDeptId();
        SysDept sysDept = iSysDeptService.selectDeptById(nonManagerDeptId);
        if (sysDept == null || sysDept.getStatus().equals("1")){
            throw new BusinessException("所选择的部门不可用");
        }
//        hrNonManager.setAuditor(sysDept.getLeader());
        int i = hrNonManagerMapper.insertSelective(hrNonManager);
        if (i > 0 && hrNonManager.getStatus().equals("1")){
            starProcess(hrNonManager);
        }
        return i;
    }

    public void starProcess(HrNonManager hrNonManager) {
        //修改流程状态为审核中
        hrNonManager.setAuditStatus(1);

        hrNonManager.setApplyUserId(ShiroUtils.getUserId());
        hrNonManager.setApplyUserName(ShiroUtils.getUserName());
        hrNonManager.setApplyTime(new Date());

        identityService.setAuthenticatedUserId(ShiroUtils.getLoginName());
        HashMap<String, Object> variables = new HashMap<>();
        String businessKey = hrNonManager.getId().toString();
        String applyUserId = ShiroUtils.getLoginName();
        String title = hrNonManager.getEmpName()+"入职申请";
        processHandleService.setAssignee(ProcessKey.userDefined01NonManager,variables,hrNonManager.getDeptId(),(JSONObject) JSON.toJSON(hrNonManager));
        hrNonManager.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01NonManager,businessKey,applyUserId,title,variables).getProcessInstanceId());
        hrNonManagerMapper.updateByPrimaryKeySelective(hrNonManager);
    }

    /**
     * 修改入职申请
     * 
     * @param hrNonManager 入职申请
     * @return 结果
     */
    @Override
    @Transactional
    public int updateHrNonManager(HrNonManager hrNonManager)
    {
        hrNonManager.setUpdateId(ShiroUtils.getUserId());
        hrNonManager.setUpdateBy(ShiroUtils.getLoginName());
        hrNonManager.setUpdateTime(DateUtils.getNowDate());
        return hrNonManagerMapper.updateByPrimaryKeySelective(hrNonManager);
    }

    /**
     * 删除入职申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteHrNonManagerByIds(String ids)
    {
        return hrNonManagerMapper.deleteHrNonManagerByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除入职申请信息
     * 
     * @param id 入职申请ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteHrNonManagerById(Long id)
    {
        return hrNonManagerMapper.deleteHrNonManagerById(id);
    }

    @Override
    public void showVerifyDialog(String taskId, String module, String formPageName, ModelMap mmap, String instanceId) {
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
        HrNonManager hrNonManager = hrNonManagerMapper.selectByPrimaryKey(new Long(businessKey));

        hrNonManager.setApplyUserName(startUserId);
        hrNonManager.setTaskId(taskId);
        mmap.put("nonManager", hrNonManager);
        mmap.put("taskId", taskId);

        List<HistoricActivity> list = processService.selectHistoryList(instanceId, new HistoricActivity());
        mmap.put("historicActivity", list);
    }

    @Override
    @Transactional
    public AjaxResult complete(String taskId, HttpServletRequest request, HrNonManager hrNonManager, String comment, String p_B_hrApproved) {
        hrNonManager = hrNonManagerMapper.selectByPrimaryKey(hrNonManager.getId());
        boolean approved = BooleanUtils.toBoolean(p_B_hrApproved);
        String title = hrNonManager.getEmpName() + "入职申请";
        Map<String, Object> variables = new HashMap<String, Object>();
        processHandleService.setAssignee(ProcessKey.userDefined01NonManager,variables,hrNonManager.getDeptId(),taskId, (JSONObject)JSON.toJSON(hrNonManager));
        if(approved){
            //审批通过
            hrNonManager.setAuditStatus(2);
            processHandleService.complete(hrNonManager.getInstanceId(),ProcessKey.userDefined01NonManager,title,taskId,variables,
                    hrNonManager.getAuditStatus().equals(2),comment,approved);
            List<Task> list = taskService.createTaskQuery().processInstanceId(hrNonManager.getInstanceId()).active().list();
            if(list.size() < 1) {
                //流程的活动节点数为0，流程走完，修改流程状态为通过
                hrNonManager.setAuditStatus(2);
                hrNonManagerMapper.updateByPrimaryKey(hrNonManager);
                //维护招聘需求数据
                hrRecruitService.updateRecruitCountByHrNonManager(hrNonManager);
            }
        } else {
            //页面拒绝流程后，修改流程状态为拒绝
            hrNonManager.setAuditStatus(3);
            hrNonManagerMapper.updateByPrimaryKey(hrNonManager);
            processHandleService.completeDown(hrNonManager.getInstanceId(),taskId,variables,
                    comment,approved);
        }

        return AjaxResult.success("任务已完成");

    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        //维护业务数据
        Example example = new Example(HrBusinessTrip.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        HrNonManager hrNonManager = hrNonManagerMapper.selectSingleOneByExample(example);
        hrNonManager.setAuditStatus(4);
        hrNonManagerMapper.updateByPrimaryKeySelective(hrNonManager);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult commit(Long id) {
        HrNonManager hrNonManager = hrNonManagerMapper.selectByPrimaryKey(id);
        hrNonManager.setStatus("1");
        hrNonManagerMapper.updateByPrimaryKeySelective(hrNonManager);
        if (StringUtils.isEmpty(hrNonManager.getInstanceId()) && hrNonManager.getStatus().equals("1")){
            starProcess(hrNonManager);
        }
        return AjaxResult.success();
    }

}
