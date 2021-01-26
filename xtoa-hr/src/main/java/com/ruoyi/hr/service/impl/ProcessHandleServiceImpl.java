package com.ruoyi.hr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ProcessDefinedValue;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.activity.JumpAnyWhereCmd;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.process.general.mapper.ProcessMapper;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProcessHandleServiceImpl implements ProcessHandleService {

    private static Logger logger = LoggerFactory.getLogger(ProcessHandleServiceImpl.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IBizTodoItemService bizTodoItemService;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private ProcessMapper processMapper;

    @Autowired
    private HrEmpService tHrEmpService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Resource
    private ISysUserService userService;


    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ProcessEngine processEngine;

    @Override
    public String selectToUserNameByAssignee(Task task) {
        SysUser sysUser = null;
        if (StringUtils.isNotBlank(task.getAssignee())){
            sysUser = userMapper.selectUserByLoginName(task.getAssignee());
            if (sysUser != null) {
                return sysUser.getUserName();
            }
        }
        List<String> todoUserIdList = processMapper.selectTodoUserListByTaskId(task.getId());
        StringBuilder sb = new StringBuilder();
        for (String todoUserId: todoUserIdList) {
            sysUser = userMapper.selectUserByLoginName(todoUserId);
            if (sysUser != null) {
                if(!sysUser.getLoginName().equals("admin")){
                    sb.append(sysUser.getUserName()).append(" ");
                }
            }
        }
        return sb.toString();
    }

    @Override
    public HrEmp selectTHrEmpByUserId() {
        return tHrEmpService.selectTHrEmpById(ShiroUtils.getUserId());
    }

    @Override
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
    @Transactional
    public int complete(String instanceId, String module, String title, String taskId,
                        Map<String, Object> variables, boolean isOk ,String comment, Boolean p_B_approved) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();

        //存储批注信息
        if (StringUtils.isNotEmpty(comment)) {
            identityService.setAuthenticatedUserId(ShiroUtils.getLoginName());
            taskService.addComment(taskId, instanceId, comment);
        }

        // 只有签收任务，act_hi_taskinst 表的 assignee 字段才不为 null
        taskService.claim(taskId, ShiroUtils.getLoginName());
        taskService.complete(taskId, variables);

        // 更新待办事项状态
        BizTodoItem query = new BizTodoItem();
        query.setTaskId(taskId);
        // 考虑到候选用户组，会有多个 todoitem 办理同个 task
        List<BizTodoItem> updateList = CollectionUtils.isEmpty(bizTodoItemService.selectBizTodoItemList(query)) ? null : bizTodoItemService.selectBizTodoItemList(query);
        Long todoId = null;
        for (BizTodoItem update: updateList) {
            // 找到当前登录用户的 todoitem，置为已办
            if (update.getTodoUserId().equals(ShiroUtils.getLoginName())) {
                update.setIsView("1");
                update.setIsHandle("1");
                if(p_B_approved){
                    update.setIsApproved("0");
                }else{
                    update.setIsApproved("1");
                }
                update.setHandleUserId(ShiroUtils.getLoginName());
                update.setHandleUserName(ShiroUtils.getSysUser().getUserName());
                update.setHandleTime(DateUtils.getNowDate());
                bizTodoItemService.updateBizTodoItem(update);
                todoId = update.getId();
            } else {
                // 删除候选用户组其他 todoitem
                bizTodoItemService.deleteBizTodoItemById(update.getId());
            }
        }

        // 下一节点处理人待办事项
        return bizTodoItemService.insertTodoItem(instanceId, title, module,isOk,todoId,processInstance.getStartUserId());
    }


    @Override
    @Transactional
    public void completeDown(String instanceId, String taskId,Map<String, Object> variables,
                             String comment, Boolean p_B_approved) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();

        //存储批注信息
        if (StringUtils.isNotEmpty(comment)) {
            identityService.setAuthenticatedUserId(ShiroUtils.getLoginName());
            taskService.addComment(taskId, instanceId, comment);
        }

        // 只有签收任务，act_hi_taskinst 表的 assignee 字段才不为 null
        taskService.claim(taskId, ShiroUtils.getLoginName());
        taskService.complete(taskId, variables);

        // 更新待办事项状态
        BizTodoItem query = new BizTodoItem();
        query.setTaskId(taskId);
        // 考虑到候选用户组，会有多个 todoitem 办理同个 task
        List<BizTodoItem> updateList = CollectionUtils.isEmpty(bizTodoItemService.selectBizTodoItemList(query)) ? null : bizTodoItemService.selectBizTodoItemList(query);
        Long todoId = null;
        for (BizTodoItem update: updateList) {
            // 找到当前登录用户的 todoitem，置为已办
            if (update.getTodoUserId().equals(ShiroUtils.getLoginName())) {
                update.setIsView("1");
                update.setIsHandle("1");
                update.setStatus("2");
                if(p_B_approved){
                    update.setIsApproved("0");
                }else{
                    update.setIsApproved("1");
                }
                update.setHandleUserId(ShiroUtils.getLoginName());
                update.setHandleUserName(ShiroUtils.getSysUser().getUserName());
                update.setHandleTime(DateUtils.getNowDate());
                bizTodoItemService.updateBizTodoItem(update);
                todoId = update.getId();
            } else {
                // 删除候选用户组其他 todoitem
                bizTodoItemService.deleteBizTodoItemById(update.getId());
            }
        }
        List<Task> list = taskService.createTaskQuery().processInstanceId(instanceId).active().list();
        if (list.size() > 0){
            list.forEach(task -> {
                //将剩余任务都跳转到结束节点
                jumpToNode(task.getId(),null);

                BizTodoItem query2 = new BizTodoItem();
                query2.setTaskId(task.getId());
                // 考虑到候选用户组，会有多个 todoitem 办理同个 task
                List<BizTodoItem> updateList2 = CollectionUtils.isEmpty(bizTodoItemService.selectBizTodoItemList(query2)) ? null : bizTodoItemService.selectBizTodoItemList(query2);
                if (updateList2 != null){
                    for (BizTodoItem bizTodoItem : updateList2) {
                        if (bizTodoItem.getHandleTime() == null){
                            bizTodoItemService.deleteBizTodoItemById(bizTodoItem.getId());
                        }
                    }
                }
            });
        }

        // 没有下一节点了
    }

    @Override
    public void jumpToNode(String taskId, String targetNodeId){
        processEngine.getManagementService().executeCommand(new JumpAnyWhereCmd(taskId,targetNodeId));
    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        boolean equals = processInstance.getStartUserId().equals(ShiroUtils.getLoginName());
        if (!equals){
            throw new RuntimeException("只有发起人才能撤销");
        }
        //此查询必须在删除流程前执行 要不然流程被删 instanceId为空
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(instanceId).active().list();

        //删除流程
        runtimeService.deleteProcessInstance(instanceId,message);

        //删除临时代办表
        for (Task task: tasks){
            // 更新待办事项状态
            BizTodoItem bizTodoItem = new BizTodoItem();
            bizTodoItem.setTaskId(task.getId());
            // 考虑到候选用户组，会有多个 todoitem 办理同个 task
            List<BizTodoItem> updateList = CollectionUtils.isEmpty(bizTodoItemService.selectBizTodoItemList(bizTodoItem)) ? null : bizTodoItemService.selectBizTodoItemList(bizTodoItem);
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

        //插入记录数据
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
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
    @Override
    public void setAssignee(String processKey, Map<String, Object> values, Long deptId, JSONObject jsonObject) {
        setAssignee(processKey,values,deptId,null, jsonObject);
    }

    @Override
    public void setAssignee(String processKey, Map<String, Object> values, Long deptId, String taskId, JSONObject jsonObject) {
        String procdefId = processMapper.findProcdefId(processKey);
        logger.info("setAssignee:" + processKey + " : " + procdefId );
        //根据流程定义id获取bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procdefId);
         /*//获取当前节点信息
        FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement("节点自定义id");*/

        FlowNode flowNode = null;
        if (StringUtils.isNotEmpty(taskId)){
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            flowNode = (FlowNode) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        }else {
            //获取开始节点
            List<Process> processes = bpmnModel.getProcesses();
            for (FlowElement flowElement : processes.get(0).getFlowElements()) {
                if (flowElement instanceof StartEvent){
                    flowNode = (FlowNode)flowElement;
                    break;
                }
            }
        }



        //获取当前节点输出连线
        List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
        //遍历输出连线
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            //获取输出节点元素
            FlowElement targetFlowElement = outgoingFlow.getTargetFlowElement();

            if (targetFlowElement instanceof UserTask) {
                //用户任务接点
                UserTask userTask = (UserTask) targetFlowElement;

                setUserTaskAssigne(values, deptId, jsonObject, userTask);

            }else if (targetFlowElement instanceof ExclusiveGateway){
                //排他网关，获取sequenceFlow的条件
                setGatewayCondition(values, jsonObject, (ExclusiveGateway) targetFlowElement,deptId);

            }
        }

    }

    private void setUserTaskAssigne(Map<String, Object> values, Long deptId, JSONObject jsonObject, UserTask userTask) {
        String assignee = userTask.getAssignee();//${表达式}
        logger.info("assignee:" + assignee);
        if (StringUtils.isNotEmpty(assignee) &&  assignee.contains("$")){
            String assigneeValue = isCondition(assignee);
            if (StringUtils.isNotEmpty(assigneeValue)){

                if (ProcessDefinedValue.UserTaskAssigneeForDeptLeader.asMap().containsKey(assigneeValue)){
                    //设置了部门负责人
                   /* SysUser sysUser = setAssigneeByDept(deptId, assigneeValue);
                    logger.info("sysUser:" + sysUser.getLoginName());
                    values.put(assigneeValue,sysUser.getLoginName());*/
                }else if (userTask.getLoopCharacteristics() != null
                        && StringUtils.isNotEmpty(userTask.getLoopCharacteristics().getInputDataItem()) ){
                    //设置会签
                    setCountersign(values, userTask,jsonObject);

                }
            }
        }
    }

    private void setGatewayCondition(Map<String, Object> values, JSONObject jsonObject, ExclusiveGateway targetFlowElement, Long deptId) {
        ExclusiveGateway exclusiveGateway = targetFlowElement;
        //获取网关的所有连接线
        List<SequenceFlow> outgoingFlows1 = exclusiveGateway.getOutgoingFlows();
        Set<String> set = new HashSet<>();
        for (SequenceFlow sequenceFlow : outgoingFlows1) {
            //解析出连接线的表达式变量
            List<String> forFlow = isConditionForFlow(sequenceFlow.getConditionExpression());
            if (!forFlow.isEmpty()){
                set.addAll(forFlow);
            }

            //获取当前节点输出连线
            FlowElement targetElement = sequenceFlow.getTargetFlowElement();
            //用户任务接点
            if (targetElement instanceof UserTask) {
                UserTask userTask = (UserTask) targetElement;
                setUserTaskAssigne(values, deptId, jsonObject, userTask);
            }else if (targetElement instanceof ExclusiveGateway){
                //排他网关，获取sequenceFlow的条件
                setGatewayCondition(values, jsonObject, (ExclusiveGateway) targetElement,deptId);

            }

        }
        logger.info("jsonObject 网关设置条件：" + jsonObject.toString());
        //连线条件变量
        int size = set.size();
        set.forEach(str -> {
            str = str.trim();
            //部门范围条件 例: 'C_deptId_in_101_201_324'
            //角色范围条件 例: 'C_roleKey_in_deptLeader'
            // C_:（大写C）代表所需值从创建人用户数据中取值;F_:从业务表单中取值
            // deptId_:代表所需条件字段，
            // in_:代表条件表达式，
            // 101_201_324:代表条件，多个用'_'分割，
            String deptId_in_ = "deptId_in_";
            String C_ = "C_";
            String F_ = "F_";
            String roleKey_in_ = "roleKey_in_";
            if (str.contains(deptId_in_)){
                //部门范围条件连线表达式
                String deptIdStr = str.replace(deptId_in_,"");
                if (deptIdStr.contains(C_)){
                    String[] deptIds = deptIdStr.replace(C_,"").split("_");
                    //流程所属用户
                    Object applyUserId = jsonObject.get("empId");
                    if (applyUserId == null){
                        applyUserId = jsonObject.get("createId");
                    }
                    if (applyUserId == null){
                        logger.info("C_: applyUserId为null");
                        values.put(str,false);
                    }else {
                        logger.info("C_: applyUserId="+applyUserId);
                        SysUser sysUser = userService.selectUserById(Long.parseLong(applyUserId + ""));
                        String[] ancestors = sysUser.getDept().getAncestors().split(",");
                        values.put(str,false);
                        for (String dId : deptIds) {
                            if (dId.equals(sysUser.getDept().getDeptId()+"")){
                                values.put(str,true);
                                break;
                            }else {
                                //遍历祖级部门
                                for (String ancestor : ancestors) {
                                    if (ancestor.equals(dId)){
                                        logger.info("C_: "+ancestor+"="+dId);
                                        values.put(str,true);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }else if ((deptIdStr.contains(F_))){
                    String[] deptIds = deptIdStr.replace(F_,"").split("_");
                    Object fDeptId = jsonObject.get("deptId");
                    if (fDeptId == null){
                        logger.info("F_: fDeptId为null");
                        values.put(str,false);
                    }else {
                        logger.info("F_: fDeptId="+fDeptId);
                        SysDept sysDept = sysDeptService.selectDeptById(Long.parseLong(fDeptId + ""));
                        String[] ancestors = sysDept.getAncestors().split(",");
                        values.put(str,false);
                        for (String dId : deptIds) {
                            if (dId.equals(fDeptId+"")){
                                values.put(str,true);
                                break;
                            }else {
                                //遍历祖级部门
                                for (String ancestor : ancestors) {
                                    if (ancestor.equals(dId)){
                                        logger.info("F_: "+ancestor+"="+dId);
                                        values.put(str,true);
                                    }
                                }
                            }
                        }
                    }
                }else {
                    throw new RuntimeException("排他网关表达式不规范");
                }

            } else if (str.contains(roleKey_in_)){
                //角色范围条件连线表达式
                String roleKeys = str.replace(roleKey_in_,"");
                if (roleKeys.contains(C_)){
                    String[] roleKeyArr = roleKeys.replace(C_,"").split("_");
                    Object applyUserId = jsonObject.get("empId");
                    if (applyUserId == null){
                        applyUserId = jsonObject.get("createId");
                    }
                    if (applyUserId == null){
                        logger.info("C_: createId为null " + jsonObject.toJSONString());
                        values.put(str,false);
                    }else {
                        logger.info("C_: applyUserId="+applyUserId);
                        SysUser sysUser = userService.selectUserById(Long.parseLong(applyUserId + ""));
                        Set<String> roles = sysUser.getRoles().stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
                        values.put(str,false);
                        for (String key : roleKeyArr) {
                            //判断角色
                            if ( roles != null && roles.contains(key) ){
                                values.put(str,true);
                                break;
                            }
                        }
                    }
                }
            } else if (str.contains("roleRank_")){
                //职级职等
                Object applyUserId = jsonObject.get("empId");
                if (applyUserId == null){
                    applyUserId = jsonObject.get("createId");
                }
                SysUser sysUser = userService.selectUserById(Long.parseLong(applyUserId + ""));
                String[] split = str.replace("roleRank_", "").split("_");
                values.put(str,false);
                for (String roleRank : split) {
                    for (SysRole sysRole : sysUser.getRoles()) {
                        if (sysRole.getRoleRank().equals(Integer.parseInt(roleRank))){
                            values.put(str,true);
                        }
                    }
                }

            } else {
                Object o = jsonObject.get(str);
                values.put(str,o);
            }
        });

        /*if (values.size() != size){
            throw new RuntimeException("网关条件设置错误");
        }*/
    }

    private void setCountersign(Map<String, Object> values, UserTask userTask, JSONObject jsonObject) {

        List<SequenceFlow> outgoingFlows1 = userTask.getOutgoingFlows();
        for (SequenceFlow sequenceFlow : outgoingFlows1) {
            FlowElement targetFlowElement = sequenceFlow.getTargetFlowElement();
            if (targetFlowElement instanceof ExclusiveGateway){
                //排他网关，获取sequenceFlow的条件(会签节点可能会出现跳过的情况，这里设置一下下一个节点如果是排他网关的变量)
                setGatewayCondition(values, jsonObject, (ExclusiveGateway) targetFlowElement,null);
            }
        }

        //流程所属用户
        SysUser applyUser = null;
        //流程创建人
        SysUser createUser = null;
        if (jsonObject.get("empId") != null){
            applyUser = userService.selectUserById(Long.parseLong(jsonObject.get("empId") + ""));
        }
        if (jsonObject.get("createId") != null){
            createUser = userService.selectUserById(Long.parseLong(jsonObject.get("createId") + ""));
        }
        if (applyUser == null){
            applyUser = createUser;
        }
        if (applyUser == null){
            throw new RuntimeException("申请人数据异常请通知后台开发人员");
        }


        //会签时 集合（多实例）表达式一定要规范：角色以role_开头 ， 用户名以下划线'_'分隔; 如: role_hr   hr角色
        String assigneeValues = isCondition(userTask.getLoopCharacteristics().getInputDataItem());
        if (assigneeValues.startsWith("role_") && StringUtils.isNotEmpty(assigneeValues.replace("role_",""))){
            /*SysRole role = new SysRole();
            role.setRoleKey(assigneeValues.replace("role_",""));*/
            SysRole sysRole = sysRoleService.getRoleByket(assigneeValues.replace("role_",""));
            Set<String> set = null;
            if (sysRole != null){
                SysUser sysUser = new SysUser();
                sysUser.setRoleId(sysRole.getRoleId());
                List<SysUser> userList = userService.selectAllocatedList(sysUser);
                set = userList.stream().map( SysUser::getLoginName).collect(Collectors.toSet());
            }
            if (set == null && set.isEmpty() ){
                throw new RuntimeException("会签人员为空");
            }
            Iterator<String> iterator = set.iterator();
            remUserById(createUser, iterator,applyUser);

            values.put(assigneeValues,set);

        }else if (assigneeValues.contains("_") && !assigneeValues.contains("deptLeader")){
            String[] split = assigneeValues.split("_");
            logger.info("会签人员:" + Arrays.toString(split));
            List<String> list = Arrays.asList(split);

            Iterator<String> iterator = list.iterator();
            remUserById(createUser, iterator, applyUser);
            values.put(assigneeValues, new LinkedHashSet<>(list));
        }else if (assigneeValues.contains("deptLeaderRole_")){
            //部门领导一次审批直到指定角色为止 C_deptLeaderRole_hr
            String role_ = assigneeValues.replace("deptLeaderRole_", "");
            // C_:（大写C）代表所需值从创建人用户数据中取值;F_:从业务表单中取值
            if (role_.contains("C_")){
                role_ = role_.replace("C_","");

                logger.info("deptLeaderRole_  C_: 流程发起人createId=" + applyUser.getCreateId());

                String[] ancestors = applyUser.getDept().getAncestors().split(",");
                ArrayList<String> users = new ArrayList<>();
                //发起人上级领导人
                SysUser leaderUser = userService.selectUserById(applyUser.getDept().getLeader());
                users.add(leaderUser.getLoginName());
                setDeptLeader(role_, ancestors, users, leaderUser,applyUser,createUser);

                values.put(assigneeValues, new LinkedHashSet<>(users));

            }else {
                //(role_.contains("F_")包含F_
                role_ = role_.replace("F_","");
                Object fDeptId = jsonObject.get("deptId");
                if (fDeptId == null){
                    throw new RuntimeException("deptLeaderRole_  F_: fDeptId为null");
                }else {
                    logger.info("deptLeaderRole_  F_: 流程发起人fDeptId=" + fDeptId);
                    SysDept fDept = sysDeptService.selectDeptById(Long.parseLong(fDeptId + ""));
                    String[] ancestors = fDept.getAncestors().split(",");
                    ArrayList<String> objects = new ArrayList<>();
                    //表单部门上级领导人
                    SysUser leaderUser = userService.selectUserById(fDept.getLeader());
                    objects.add(leaderUser.getLoginName());

                    setDeptLeader(role_, ancestors, objects, leaderUser, applyUser, createUser);

                    values.put(assigneeValues, new LinkedHashSet<>(objects));
                }
            }
        }else if (assigneeValues.contains("deptLeader_")){
            //上级部门领导审批，只到指定的第几级领导 C_deptLeader_02
            String leaderNum = assigneeValues.replace("deptLeader_", "");
            // C_:（大写C）代表所需值从创建人用户数据中取值;F_:从业务表单中取值
            if (leaderNum.contains("C_")){
                leaderNum = leaderNum.replace("C_","");
                Integer num = Integer.parseInt(leaderNum);

                    String[] ancestors = applyUser.getDept().getAncestors().split(",");
                    LinkedHashSet<String> objects = new LinkedHashSet<>();
                    //发起人上级领导人
                    SysUser leaderUser = userService.selectUserById(applyUser.getDept().getLeader());
                    if (!leaderUser.getLoginName().equals(applyUser.getLoginName())){
                        //下面是计数的所以要在遍历前过滤自己，审批人去掉自己
                        objects.add(leaderUser.getLoginName());
                    }
                    for (int i = ancestors.length - 1; i >= 0; i--) {
                        if (objects.size() < num){
                            SysDept sysDept = sysDeptService.selectDeptById(Long.parseLong(ancestors[i]));
                            if (sysDept != null){
                                SysUser user = userService.selectUserById(sysDept.getLeader());
                                if (user != null && !user.getLoginName().equals(applyUser.getLoginName())){
                                    objects.add(user.getLoginName()+"");
                                }
                            }
                        }
                    }

                    Iterator<String> iterator = objects.iterator();
                remUserById(createUser, iterator, applyUser);

                logger.info("部门领导顺序会签：" + Arrays.toString(objects.toArray()));
                    values.put(assigneeValues, objects);

            }else {
                //(role_.contains("F_")包含F_
                leaderNum = leaderNum.replace("F_","");
                Integer num = Integer.parseInt(leaderNum);
                Object fDeptId = jsonObject.get("deptId");
                if (fDeptId == null){
                    throw new RuntimeException("deptLeader_  F_: fDeptId为null");
                }else {
                    logger.info("deptLeader_  F_: 流程发起人fDeptId=" + fDeptId);
                    SysDept fDept = sysDeptService.selectDeptById(Long.parseLong(fDeptId + ""));
                    String[] ancestors = fDept.getAncestors().split(",");
                    LinkedHashSet<String> objects = new LinkedHashSet<>();
                    //表单部门上级领导人
                    SysUser leaderUser = userService.selectUserById(fDept.getLeader());
                    if (!leaderUser.getLoginName().equals(applyUser.getLoginName())){
                        //下面是计数的所以要在遍历前过滤自己，审批人去掉自己
                        objects.add(leaderUser.getLoginName());
                    }
                    for (int i = ancestors.length - 1; i >= 0; i--) {
                        if (objects.size() < num){
                            SysDept sysDept = sysDeptService.selectDeptById(Long.parseLong(ancestors[i]));
                            if (sysDept != null){
                                SysUser user = userService.selectUserById(sysDept.getLeader());
                                if (user != null && !user.getLoginName().equals(applyUser.getLoginName())){
                                    objects.add(user.getLoginName()+"");
                                }
                            }
                        }
                    }
                    Iterator<String> iterator = objects.iterator();
                    remUserById(createUser, iterator, applyUser);

                    logger.info("部门领导顺序会签：" + Arrays.toString(objects.toArray()));
                    values.put(assigneeValues, objects);
                }
            }

        }else {
            throw new RuntimeException("会签的集合（多实例）表达式不规范");
        }
    }

    private void remUserById(SysUser createUser, Iterator<String> iterator, SysUser applyUser) {
        SysUser sysUser = null;
        while (iterator.hasNext()){
            String next = iterator.next();

            if (createUser != null && next.equals(createUser.getLoginName()) ){
                //审批人去掉创建人
                iterator.remove();
            }else if (applyUser != null && next.equals(applyUser.getLoginName()) ){
                //审批人去掉流程所属用户
                iterator.remove();
            }else {
                //会签节点去掉审批人为CEO角色的
                sysUser = userService.selectUserByLoginName(next);
                if (sysUser != null && sysUser.getRoles() != null && !sysUser.getRoles().isEmpty()){
                    for (SysRole sysRole : sysUser.getRoles()) {
                        if (sysRole.getRoleKey().contains("CEO")){
                            iterator.remove();
                        }
                    }
                }
            }

        }
    }

    private void setDeptLeader(String role_, String[] ancestors, ArrayList<String> objects,
                               SysUser leaderUser, SysUser applyUser, SysUser createUser) {

        List<SysRole> roles = leaderUser.getRoles();
        Set<String> roleKeys = roles.stream().map(SysRole :: getRoleKey ).collect(Collectors.toSet());
        Set<String> roleKeys2 = null;
        if (!roleKeys.contains(role_)){
            //开始遍历上级部门领导，直到指定角色为止
            for (int i = ancestors.length - 1; i >= 0; i--) {
                SysDept sysDept = sysDeptService.selectDeptById(Long.parseLong(ancestors[i]));
                if (sysDept != null){
                    SysUser user = userService.selectUserById(sysDept.getLeader());
                    if (user != null){
                        objects.add(user.getLoginName()+"");
                        roleKeys2 = user.getRoles().stream().map(SysRole :: getRoleKey ).collect(Collectors.toSet());
                        if (roleKeys2.contains(role_)){
                            //找到指定角色，停止添加审批人
                            break;
                        }
                    }
                }
            }
        }

        Iterator<String> iterator = objects.iterator();
        remUserById(createUser, iterator, applyUser);

        logger.info("部门领导顺序会签：" + Arrays.toString(objects.toArray()));
    }

    private SysUser setAssigneeByDept(Long deptId, String assigneeValue) {
        //deptLeader01
        //01代表第一级主管，以此类推
        //目前最高到第06级
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        if (sysDept == null){
            throw new RuntimeException("Assignee：部门数据为空");
        }
        //装载上级部门集合
        String[] split = sysDept.getAncestors().split(",");
        List<String> deptList = Arrays.asList(split);
        ArrayList<String> depts = new ArrayList<>(deptList);
        depts.add( deptId + "" );
        //获取定义的审核人部门负责人是第几集的再获取部门id
        int deptLeaderInt = Integer.parseInt(assigneeValue.replace("deptLeader", ""));
        String leaderDeptId = depts.get(depts.size() - deptLeaderInt);
        SysDept leaderDept = sysDeptService.selectDeptById(Long.parseLong(leaderDeptId));
        if (leaderDept == null){
            throw new RuntimeException("Assignee：部门领导人为空");
        }

        SysUser sysUser = userService.selectUserById(leaderDept.getLeader());
        if (sysUser == null){
            throw new RuntimeException("Assignee：部门领导人为空");
        }
        return sysUser;
    }

    /**
     * userTask的el表达式判断
     *
     * @param expression
     * @return
     */
    private static String isCondition(String expression) {
        if ( expression == null || expression == "" ) {
            return null;
        }
        expression = expression.replaceAll("[{}$&]","").trim();
        return expression;
        /*//分割表达式
        String[] exprArr = expression.split("[{}$&]");
        for (String expr : exprArr) {
            //是否包含键message
            if (expr.contains("deptLeader")) {
                if (!vars.containsKey("message")) {
                    continue;
                }
                if (expr.contains("==")) {
                    String[] primes = expr.split("==");
                    String valExpr = primes[1].trim();
                    if (valExpr.startsWith("'")) {
                        valExpr = valExpr.substring(1);
                    }
                    if (valExpr.endsWith("'")) {
                        valExpr = valExpr.substring(0, valExpr.length() - 1);
                    }
                    if (primes.length == 2 && valExpr.equals(vars.get("message"))) {
                        return true;
                    }
                }
            }
        }*/
    }
    /**
     * userTask的el表达式判断
     *
     * @param expression
     * @return
     */
    private static List<String> isConditionForFlow(String expression) {
        if ( expression == null || expression == "" ) {
            return new ArrayList<>();
        }
        expression = expression.replaceAll("[{}$&]","").trim();
        List<String> arr = new ArrayList<>();
        String[] exprArr = expression.split("[{}$&]");
        for (String expr : exprArr) {
            String[] split = expr.split("[=!<>]");
            arr.add(split[0]);
        }
        return arr;
    }

}
