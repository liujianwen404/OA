//package com.ruoyi.process.hr.service.impl;
//
//import com.github.pagehelper.Page;
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.common.core.page.PageDomain;
//import com.ruoyi.common.core.page.TableSupport;
//import com.ruoyi.common.core.text.Convert;
//import com.ruoyi.common.enums.ProcessKey;
//import com.ruoyi.common.utils.DateUtils;
//import com.ruoyi.common.utils.StringUtils;
//import com.ruoyi.framework.util.ShiroUtils;
//import com.ruoyi.process.hr.domain.HrQuit;
//import com.ruoyi.process.hr.mapper.HrQuitMapper;
//import com.ruoyi.process.hr.service.IHrQuitService;
//import com.ruoyi.process.leave.domain.BizLeaveVo;
//import com.ruoyi.process.todoitem.domain.BizTodoItem;
//import com.ruoyi.process.todoitem.service.IBizTodoItemService;
//import com.ruoyi.system.domain.SysUser;
//import com.ruoyi.system.mapper.SysUserMapper;
//import org.activiti.engine.IdentityService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//import tk.mybatis.mapper.entity.Example;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 离职申请Service业务层处理
// *
// * @author liujianwen
// * @date 2020-05-15
// */
//@Service
//public class HrQuitServiceImpl implements IHrQuitService
//{
//
//    private static final Logger logger = LoggerFactory.getLogger(HrQuitServiceImpl.class);
//
//    @Autowired
//    private HrQuitMapper hrQuitMapper;
//    @Autowired
//    private SysUserMapper userMapper;
//
//    @Autowired
//    private TaskService taskService;
//
//    @Autowired
//    private IdentityService identityService;
//
//    @Autowired
//    private RuntimeService runtimeService;
//
//    @Autowired
//    private IBizTodoItemService bizTodoItemService;
//
//    /**
//     * 查询离职申请
//     *
//     * @param id 离职申请ID
//     * @return 离职申请
//     */
//    @Override
//    public HrQuit selectHrQuitById(Long id)
//    {
//        HrQuit hrQuit = hrQuitMapper.selectByPrimaryKey(id);
//        SysUser sysUser = userMapper.selectUserByLoginName(hrQuit.getApplyUser());
//        if (sysUser != null) {
//            hrQuit.setApplyUserName(sysUser.getUserName());
//        }
//        return hrQuit;
//    }
//
//    /**
//     * 查询离职申请列表
//     *
//     * @param quit 离职申请
//     * @return 离职申请
//     */
//    @Override
//    public List<HrQuit> selectHrQuitList(HrQuit quit)
//    {
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Integer pageNum = pageDomain.getPageNum();
//        Integer pageSize = pageDomain.getPageSize();
//
//        // PageHelper 仅对第一个 List 分页
//        Page<HrQuit> list = (Page<HrQuit>) hrQuitMapper.selectHrQuitList(quit);
//        Page<HrQuit> returnList = new Page<>();
//        for (HrQuit hrQuit: list) {
//            SysUser sysUser = userMapper.selectUserByLoginName(hrQuit.getCreateBy());
//            if (sysUser != null) {
//                hrQuit.setCreateUserName(sysUser.getUserName());
//            }
//            SysUser sysUser2 = userMapper.selectUserByLoginName(hrQuit.getApplyUser());
//            if (sysUser2 != null) {
//                hrQuit.setApplyUserName(sysUser2.getUserName());
//            }
//            // 当前环节
//            if (StringUtils.isNotBlank(hrQuit.getInstanceId())) {
//                List<Task> taskList = taskService.createTaskQuery()
//                        .processInstanceId(hrQuit.getInstanceId())
////                        .singleResult();
//                        .list();    // 例如请假会签，会同时拥有多个任务
//                if (!CollectionUtils.isEmpty(taskList)) {
//                    Task task = taskList.get(0);
//                    hrQuit.setTaskId(task.getId());
//                    hrQuit.setTaskName(task.getName());
//                } else {
//                    hrQuit.setTaskName("已办结");
//                }
//            } else {
//                hrQuit.setTaskName("未启动");
//            }
//            returnList.add(hrQuit);
//        }
//        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
//        returnList.setPageNum(pageNum);
//        returnList.setPageSize(pageSize);
//        return returnList;
//    }
//
//    /**
//     * 新增离职申请
//     *
//     * @param hrQuit 离职申请
//     * @return 结果
//     */
//    @Override
//    public int insertHrQuit(HrQuit hrQuit)
//    {
//        hrQuit.setCreateId(ShiroUtils.getUserId());
//        hrQuit.setCreateBy(ShiroUtils.getLoginName());
//        hrQuit.setCreateTime(DateUtils.getNowDate());
//        return hrQuitMapper.insertSelective(hrQuit);
//    }
//
//    /**
//     * 修改离职申请
//     *
//     * @param hrQuit 离职申请
//     * @return 结果
//     */
//    @Override
//    public int updateHrQuit(HrQuit hrQuit)
//    {
//        hrQuit.setUpdateId(ShiroUtils.getUserId());
//        hrQuit.setUpdateBy(ShiroUtils.getLoginName());
//        hrQuit.setUpdateTime(DateUtils.getNowDate());
//        return hrQuitMapper.updateByPrimaryKeySelective(hrQuit);
//    }
//
//    /**
//     * 删除离职申请对象
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    @Override
//    public int deleteHrQuitByIds(String ids)
//    {
//        return hrQuitMapper.deleteHrQuitByIds(Convert.toStrArray(ids));
//    }
//
//    /**
//     * 删除离职申请信息
//     *
//     * @param id 离职申请ID
//     * @return 结果
//     */
//    @Override
//    public int deleteHrQuitById(Long id)
//    {
//        return hrQuitMapper.deleteHrQuitById(id);
//    }
//
//    /**
//     * 启动流程
//     * @param hrQuit
//     * @param applyUserId
//     * @return
//     */
//    @Override
//    public ProcessInstance submitApply(HrQuit hrQuit, String applyUserId) {
//        hrQuit.setApplyUser(applyUserId);
//        hrQuit.setApplyTime(DateUtils.getNowDate());
//        hrQuit.setUpdateBy(applyUserId);
//        //修改流程状态为审核中
//        hrQuit.setAuditStatus("1");
//        hrQuitMapper.updateByPrimaryKey(hrQuit);
//        // 实体类 ID，作为流程的业务 key
//        String businessKey = hrQuit.getId().toString();
//
//        // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
//        identityService.setAuthenticatedUserId(applyUserId);
//        HashMap<String, Object> variables = new HashMap<>();
//        variables.put("leader",userMapper.selectDeptLeaderByLoginName(applyUserId));
//        System.out.println("登录用户名："+applyUserId);
//        System.out.println("流程变量："+variables);
//        // 启动流程时设置业务 key
//        ProcessInstance processInstance = runtimeService
//                .startProcessInstanceByKey("quitProcess", businessKey,variables);
//        String processInstanceId = processInstance.getId();
//        // 建立双向关系
//        hrQuit.setInstanceId(processInstanceId);
//        hrQuitMapper.updateByPrimaryKey(hrQuit);
//        // 下一节点处理人待办事项
//        bizTodoItemService.insertTodoItem(processInstanceId, hrQuit.getTitle(), "quitProcess",processInstance.getStartUserId());
//
//        return processInstance;
//    }
//
//    /**
//     * 完成任务
//     * @param hrQuit
//     * @param saveEntity
//     * @param taskId
//     * @param variables
//     */
//    @Override
//    public void complete(HrQuit hrQuit, Boolean saveEntity, String taskId, Map<String, Object> variables,String starUserName) {
//        if (saveEntity) {
//            hrQuitMapper.updateByPrimaryKey(hrQuit);
//        }
//        // 只有签收任务，act_hi_taskinst 表的 assignee 字段才不为 null
//        taskService.claim(taskId, ShiroUtils.getLoginName());
//        taskService.complete(taskId, variables);
//
//        // 更新待办事项状态
//        BizTodoItem query = new BizTodoItem();
//        query.setTaskId(taskId);
//        // 考虑到候选用户组，会有多个 todoitem 办理同个 task
//        List<BizTodoItem> updateList = CollectionUtils.isEmpty(bizTodoItemService.selectBizTodoItemList(query)) ? null : bizTodoItemService.selectBizTodoItemList(query);
//        Long todoId = null;
//        for (BizTodoItem update: updateList) {
//            // 找到当前登录用户的 todoitem，置为已办
//            if (update.getTodoUserId().equals(ShiroUtils.getLoginName())) {
//                update.setIsView("1");
//                update.setIsHandle("1");
//                update.setHandleUserId(ShiroUtils.getLoginName());
//                update.setHandleUserName(ShiroUtils.getSysUser().getUserName());
//                update.setHandleTime(DateUtils.getNowDate());
//                bizTodoItemService.updateBizTodoItem(update);
//                todoId = update.getId();
//            } else {
//                // 删除候选用户组其他 todoitem
//                bizTodoItemService.deleteBizTodoItemById(update.getId());
//            }
//        }
//
//        // 下一节点处理人待办事项
//        bizTodoItemService.insertTodoItem(hrQuit.getInstanceId(), hrQuit.getTitle(), ProcessKey.PROCESS_QUIT,
//                hrQuit.getAuditStatus().equals("2"),todoId,starUserName);
//    }
//
//    @Override
//    @Transactional
//    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
//
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
//        boolean equals = processInstance.getStartUserId().equals(ShiroUtils.getLoginName());
//        if (!equals){
//            return AjaxResult.error("只有发起人才能撤销");
//        }
//
//        //删除流程
//        runtimeService.deleteProcessInstance(instanceId,message);
//        //维护业务数据
//        Example example = new Example(HrQuit.class);
//        example.createCriteria().andEqualTo("instanceId",instanceId);
//        HrQuit hrQuit = hrQuitMapper.selectSingleOneByExample(example);
//        hrQuit.setAuditStatus("4");
//        hrQuitMapper.updateByPrimaryKeySelective(hrQuit);
//
//        //删除临时代办表
//        List<Task> tasks = taskService.createTaskQuery().processInstanceId(instanceId).active().list();
//        for (Task task: tasks){
//            // 更新待办事项状态
//            BizTodoItem bizTodoItem = new BizTodoItem();
//            bizTodoItem.setTaskId(task.getId());
//            // 考虑到候选用户组，会有多个 todoitem 办理同个 task
//            List<BizTodoItem> updateList = CollectionUtils.isEmpty(bizTodoItemService.selectBizTodoItemList(bizTodoItem)) ? null : bizTodoItemService.selectBizTodoItemList(bizTodoItem);
//            for (BizTodoItem update : updateList) {
//                //  todoitem，将未办理的删除
//                if (update.getIsHandle().equals("0")) {
//                    // 删除候选用户组其他 todoitem
//                    bizTodoItemService.deleteBizTodoItemById(update.getId());
//                }
//            }
//        }
//        return AjaxResult.success();
//    }
//}
