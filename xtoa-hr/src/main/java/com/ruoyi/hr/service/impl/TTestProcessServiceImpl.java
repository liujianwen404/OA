package com.ruoyi.hr.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.base.domain.FinancePayment;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
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
import com.ruoyi.hr.mapper.TTestProcessMapper;
import com.ruoyi.base.domain.TTestProcess;
import com.ruoyi.hr.service.ITTestProcessService;
import com.ruoyi.common.core.text.Convert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 测试流程单Service业务层处理
 * 
 * @author xt
 * @date 2020-11-10
 */
@Service
public class TTestProcessServiceImpl implements ITTestProcessService 
{

    private static final Logger logger = LoggerFactory.getLogger(TTestProcessServiceImpl.class);

    @Resource
    private TTestProcessMapper tTestProcessMapper;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private TaskService taskService;

    @Resource
    private ISysUserService userService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IBizTodoItemService bizTodoItemService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 查询测试流程单
     * 
     * @param id 测试流程单ID
     * @return 测试流程单
     */
    @Override
    public TTestProcess selectTTestProcessById(Long id)
    {
        return tTestProcessMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询测试流程单列表
     * 
     * @param tTestProcess 测试流程单
     * @return 测试流程单
     */
    @Override
    public List<TTestProcess> selectTTestProcessList(TTestProcess tTestProcess)
    {
        List<TTestProcess> tTestProcessList = tTestProcessMapper.selectTTestProcessList(tTestProcess);
        for (TTestProcess test: tTestProcessList) {
            SysUser sysUser = userService.selectUserByLoginName(test.getCreateBy());
            if (sysUser != null) {
                test.setCreateUserName(sysUser.getUserName());
                test.setApplyUserName(sysUser.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(test.getInstanceId())) {
                // 例如加班会签，会同时拥有多个任务
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(test.getInstanceId()).list();
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    String assignee = processHandleService.selectToUserNameByAssignee(task);
                    test.setTaskId(task.getId());
                    test.setTaskName(task.getName());
                    test.setTodoUserName(assignee);
                } else {
                    test.setTaskName("已办结");
                }
            } else {
                test.setTaskName("未启动");
            }
        }
        return tTestProcessList;
    }

    /**
     * 新增测试流程单
     * 
     * @param tTestProcess 测试流程单
     * @return 结果
     */
    @Override
    public int insertTTestProcess(TTestProcess tTestProcess)
    {
        tTestProcess.setCreateId(ShiroUtils.getUserId());
        tTestProcess.setCreateBy(ShiroUtils.getLoginName());
        tTestProcess.setCreateTime(DateUtils.getNowDate());
        tTestProcess.setApplyUserName(ShiroUtils.getUserName());
        return tTestProcessMapper.insertSelective(tTestProcess);
    }

    /**
     * 修改测试流程单
     * 
     * @param tTestProcess 测试流程单
     * @return 结果
     */
    @Override
    public int updateTTestProcess(TTestProcess tTestProcess)
    {
        tTestProcess.setUpdateId(ShiroUtils.getUserId());
        tTestProcess.setUpdateBy(ShiroUtils.getLoginName());
        tTestProcess.setUpdateTime(DateUtils.getNowDate());
        return tTestProcessMapper.updateByPrimaryKeySelective(tTestProcess);
    }

    /**
     * 删除测试流程单对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTTestProcessByIds(String ids)
    {
        return tTestProcessMapper.deleteTTestProcessByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除测试流程单信息
     * 
     * @param id 测试流程单ID
     * @return 结果
     */
    @Override
    public int deleteTTestProcessById(Long id)
    {
        return tTestProcessMapper.deleteTTestProcessById(id);
    }



    @Override
    public TTestProcess selectSingleOneByExample(Example example){
        return tTestProcessMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<TTestProcess> selectByExample(Example example){
        return tTestProcessMapper.selectByExample(example);
    }

    @Override
    public AjaxResult submitApply(TTestProcess tTestProcess, String loginName) {
        tTestProcess.setCreateBy(loginName);
        tTestProcess.setCreateTime(DateUtils.getNowDate());
        tTestProcess.setUpdateBy(loginName);
        //修改流程状态为审核中
        tTestProcess.setAuditStatus("1");
        // 实体类 ID，作为流程的业务 key
        String businessKey = tTestProcess.getId().toString();
        HashMap<String, Object> variables = new HashMap<>();
        List<String> deptLeaders = new ArrayList<>();
        SysUser sysUser = userService.selectUserById(tTestProcess.getCreateId());
        String[] ancestors = sysUser.getDept().getAncestors().split(",");
        SysDept dept1 = sysDeptService.selectDeptById(sysUser.getDept().getDeptId());
        SysUser leader1 = userService.selectUserById(dept1.getLeader());
        deptLeaders.add(leader1.getLoginName());
        Arrays.asList(ancestors).stream().filter(a -> !"0".equals(a) && !"100".equals(a)).sorted(Comparator.reverseOrder()).forEach(deptId->{
            SysDept sysDept = sysDeptService.selectDeptById(Long.parseLong(deptId));
            SysUser leader = userService.selectUserById(sysDept.getLeader());
            String leaderLoginName = leader.getLoginName();
            deptLeaders.add(leaderLoginName);
        });
        List<String> deptList = deptLeaders.stream().filter(leader -> !loginName.equals(leader) && !"chendeming".equals(leader) && !"admin".equals(leader)).distinct().limit(3).collect(Collectors.toList());

        SysDept company = sysDeptService.selectCompanyByUserDeptId(tTestProcess.getDeptId());
//        SysDept firstDept = sysDeptService.selectFirstDeptByUserDeptId(tTestProcess.getDeptId());
        Set<String> roles = sysRoleService.selectRoleKeys(tTestProcess.getCreateId());
        boolean flag = false;
        if(!roles.isEmpty() && roles.contains("cityAdmin")){
            flag = true;
        }
        variables.put("deptLeaders",deptList);
        variables.put("createBy",loginName);
        variables.put("companyId",company.getDeptId());
//        variables.put("firstDeptId",firstDept.getDeptId());
        variables.put("isCityAdmin",flag);
//        processHandleService.setAssignee("test",variables,tTestProcess.getDeptId(),(JSONObject) JSON.toJSON(tTestProcess));

        // 建立双向关系
        tTestProcess.setInstanceId(submitApply("test",businessKey,loginName,tTestProcess.getTitle(),variables).getProcessInstanceId());
        tTestProcessMapper.updateByPrimaryKey(tTestProcess);
        return AjaxResult.success();
    }

    @Transactional
    public ProcessInstance submitApply(String module, String businessKey, String loginName, String title, Map<String, Object> variables) {

        // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中（这里的用户ID是系统里的登录用户名）
        identityService.setAuthenticatedUserId(loginName);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(module, businessKey,variables);
        String processInstanceId = processInstance.getId();

        // 下一节点处理人待办事项
        bizTodoItemService.insertTodoItem(processInstanceId, title, module,processInstance.getStartUserId());
        return processInstance;
    }

    @Override
    public AjaxResult complete(TTestProcess tTestProcess, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();
        String taskId = tTestProcess.getTaskId();
//        processHandleService.setAssignee("test",variables,tTestProcess.getDeptId(),taskId, (JSONObject)JSON.toJSON(tTestProcess));

        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean toBoolean = BooleanUtils.toBoolean(p_B_approved);

        if (toBoolean){
            //审批通过
            tTestProcess.setAuditStatus("2");

            processHandleService.complete(tTestProcess.getInstanceId(),"test",tTestProcess.getTitle(),taskId,variables,
                    tTestProcess.getAuditStatus().equals("2"),comment,BooleanUtils.toBoolean(p_B_approved));

            List<Task> list = taskService.createTaskQuery().processInstanceId(tTestProcess.getInstanceId()).active().list();
            if (list.size() < 1){
                //流程结束了更新数据状态
                tTestProcessMapper.updateByPrimaryKey(tTestProcess);
            }
        }else {
            //审批拒绝
            tTestProcess.setAuditStatus("3");
            tTestProcessMapper.updateByPrimaryKey(tTestProcess);
            processHandleService.completeDown(tTestProcess.getInstanceId(),taskId,variables,
                    comment,BooleanUtils.toBoolean(p_B_approved));
        }
        return AjaxResult.success("任务已完成");
    }

    @Override
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        //维护业务数据
        Example example = new Example(FinancePayment.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        TTestProcess tTestProcess = tTestProcessMapper.selectSingleOneByExample(example);
        tTestProcess.setAuditStatus("4");
        tTestProcessMapper.updateByPrimaryKeySelective(tTestProcess);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

}
