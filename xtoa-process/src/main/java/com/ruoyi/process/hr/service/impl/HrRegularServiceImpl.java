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
//import com.ruoyi.process.hr.domain.HrRegular;
//import com.ruoyi.process.hr.mapper.HrRegularMapper;
//import com.ruoyi.process.hr.service.IHrRegularService;
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
//import javax.annotation.Resource;
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
//public class HrRegularServiceImpl implements IHrRegularService
//{
//
//    private static final Logger logger = LoggerFactory.getLogger(HrRegularServiceImpl.class);
//
//    @Resource
//    private HrRegularMapper hrRegularMapper;
//    @Resource
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
//     * 查询申请
//     *
//     * @param id 申请ID
//     */
//    @Override
//    public HrRegular selectHrRegularById(Long id)
//    {
//        HrRegular hrRegular = hrRegularMapper.selectByPrimaryKey(id);
//        SysUser sysUser = userMapper.selectUserByLoginName(hrRegular.getApplyUser());
//        if (sysUser != null) {
//            hrRegular.setApplyUserName(sysUser.getUserName());
//        }
//        return hrRegular;
//    }
//
//    /**
//     * 查询申请列表
//     */
//    @Override
//    public List<HrRegular> selectHrRegularList(HrRegular Regular)
//    {
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Integer pageNum = pageDomain.getPageNum();
//        Integer pageSize = pageDomain.getPageSize();
//
//        // PageHelper 仅对第一个 List 分页
//        Page<HrRegular> list = (Page<HrRegular>) hrRegularMapper.selectHrRegularList(Regular);
//        Page<HrRegular> returnList = new Page<>();
//        for (HrRegular hrRegular: list) {
//            SysUser sysUser = userMapper.selectUserByLoginName(hrRegular.getCreateBy());
//            if (sysUser != null) {
//                hrRegular.setCreateUserName(sysUser.getUserName());
//            }
//            SysUser sysUser2 = userMapper.selectUserByLoginName(hrRegular.getApplyUser());
//            if (sysUser2 != null) {
//                hrRegular.setApplyUserName(sysUser2.getUserName());
//            }
//            // 当前环节
//            if (StringUtils.isNotBlank(hrRegular.getInstanceId())) {
//                List<Task> taskList = taskService.createTaskQuery()
//                        .processInstanceId(hrRegular.getInstanceId())
////                        .singleResult();
//                        .list();    // 例如请假会签，会同时拥有多个任务
//                if (!CollectionUtils.isEmpty(taskList)) {
//                    Task task = taskList.get(0);
//                    hrRegular.setTaskId(task.getId());
//                    hrRegular.setTaskName(task.getName());
//                } else {
//                    hrRegular.setTaskName("已办结");
//                }
//            } else {
//                hrRegular.setTaskName("未启动");
//            }
//            returnList.add(hrRegular);
//        }
//        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
//        returnList.setPageNum(pageNum);
//        returnList.setPageSize(pageSize);
//        return returnList;
//    }
//
//    /**
//     * 新增申请
//     */
//    @Override
//    public int insertHrRegular(HrRegular hrRegular)
//    {
//        hrRegular.setCreateId(ShiroUtils.getUserId());
//        hrRegular.setCreateBy(ShiroUtils.getLoginName());
//        hrRegular.setCreateTime(DateUtils.getNowDate());
//        return hrRegularMapper.insertSelective(hrRegular);
//    }
//
//    /**
//     * 修改申请
//     */
//    @Override
//    public int updateHrRegular(HrRegular hrRegular)
//    {
//        hrRegular.setUpdateId(ShiroUtils.getUserId());
//        hrRegular.setUpdateBy(ShiroUtils.getLoginName());
//        hrRegular.setUpdateTime(DateUtils.getNowDate());
//        return hrRegularMapper.updateByPrimaryKeySelective(hrRegular);
//    }
//
//    /**
//     * 删除申请对象
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    @Override
//    public int deleteHrRegularByIds(String ids)
//    {
//        return hrRegularMapper.deleteHrRegularByIds(Convert.toStrArray(ids));
//    }
//
//    /**
//     * 删除离职申请信息
//     *
//     * @param id 离职申请ID
//     * @return 结果
//     */
//    @Override
//    public int deleteHrRegularById(Long id)
//    {
//        return hrRegularMapper.deleteHrRegularById(id);
//    }
//
//    /**
//     * 启动流程
//     * @param hrRegular
//     * @param applyUserId
//     * @return
//     */
//    @Override
//    public ProcessInstance submitApply(HrRegular hrRegular, String applyUserId) {
//        hrRegular.setApplyUser(applyUserId);
//        hrRegular.setApplyTime(DateUtils.getNowDate());
//        hrRegular.setUpdateBy(applyUserId);
//        //修改流程状态为审核中
//        hrRegular.setAuditStatus("1");
//        hrRegularMapper.updateByPrimaryKey(hrRegular);
//        // 实体类 ID，作为流程的业务 key
//        String businessKey = hrRegular.getId().toString();
//
//        // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
//        identityService.setAuthenticatedUserId(applyUserId);
//        HashMap<String, Object> variables = new HashMap<>();
//        variables.put("leader",userMapper.selectDeptLeaderByLoginName(applyUserId));
//        System.out.println("登录用户名："+applyUserId);
//        System.out.println("流程变量："+variables);
//        // 启动流程时设置业务 key
//        ProcessInstance processInstance = runtimeService
//                .startProcessInstanceByKey("regularProcess", businessKey,variables);
//        String processInstanceId = processInstance.getId();
//        // 建立双向关系
//        hrRegular.setInstanceId(processInstanceId);
//        hrRegularMapper.updateByPrimaryKey(hrRegular);
//        // 下一节点处理人待办事项
//        bizTodoItemService.insertTodoItem(processInstanceId, hrRegular.getTitle(), "regularProcess",processInstance.getStartUserId());
//
//        return processInstance;
//    }
//
//    /**
//     * 完成任务
//     * @param hrRegular
//     * @param saveEntity
//     * @param taskId
//     * @param variables
//     */
//    @Override
//    public void complete(HrRegular hrRegular, Boolean saveEntity, String taskId, Map<String, Object> variables,String starUserName) {
//        if (saveEntity) {
//            hrRegularMapper.updateByPrimaryKey(hrRegular);
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
//        // 下一节点处理人待办事项,注意ProcessKey这里要枚举不同的key
//        bizTodoItemService.insertTodoItem(hrRegular.getInstanceId(), hrRegular.getTitle(), ProcessKey.PROCESS_REGULAR,
//                hrRegular.getAuditStatus().equals("2"),todoId,starUserName);
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
//        HrRegular hrRegular = hrRegularMapper.selectSingleOneByExample(example);
//        hrRegular.setAuditStatus("4");
//        hrRegularMapper.updateByPrimaryKeySelective(hrRegular);
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
