package com.ruoyi.process.todoitem.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.base.listenerEvent.BizItemInsetEvent;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.process.general.mapper.ProcessMapper;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.mapper.BizTodoItemMapper;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 待办事项Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2019-11-08
 */
@Service
@Transactional
@Slf4j
public class BizTodoItemServiceImpl implements IBizTodoItemService {
    @Autowired
    private BizTodoItemMapper bizTodoItemMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 查询待办事项
     *
     * @param id 待办事项ID
     * @return 待办事项
     */
    @Override
    public BizTodoItem selectBizTodoItemById(Long id) {
        return bizTodoItemMapper.selectBizTodoItemById(id);
    }

    /**
     * 查询待办事项列表
     *
     * @param bizTodoItem 待办事项
     * @return 待办事项
     */
    @Override
    public List<BizTodoItem> selectBizTodoItemList(BizTodoItem bizTodoItem) {
        List<BizTodoItem> BizTodoItemList = bizTodoItemMapper.selectBizTodoItemList(bizTodoItem);
        for (BizTodoItem item: BizTodoItemList) {
            SysUser sysUser = userMapper.selectUserByLoginName(item.getStarUserName());
            if (sysUser != null) {
                item.setApplyUserName(sysUser.getUserName());
            }


            if (sysUser != null) {
                StringBuilder stringBuilder = new StringBuilder();
                if (StringUtils.isNotEmpty(item.getInstanceId())){
                    List<Task> taskList = taskService.createTaskQuery()
                            .processInstanceId(item.getInstanceId())
                            .list();
                    for (Task taskitem : taskList) {
                        List<BizTodoItem> bizTodoItems = selectByTaskId(taskitem.getId());
                        for (BizTodoItem bizTodo : bizTodoItems) {
                            stringBuilder.append(bizTodo.getTodoUserName() + " - ");
                        }
                    }
                }
                item.setTodoUserName(stringBuilder.toString());
            }
        }
        return BizTodoItemList;
    }

    /**
     * 新增待办事项
     *
     * @param bizTodoItem 待办事项
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBizTodoItem(BizTodoItem bizTodoItem) {

        int i = bizTodoItemMapper.insertBizTodoItem(bizTodoItem);
        if (i > 0){
            //发送钉钉推送
            applicationContext.publishEvent(new BizItemInsetEvent(this,(JSONObject) JSON.toJSON(bizTodoItem)));
        }
        return i;
    }

    /**
     * 修改待办事项
     *
     * @param bizTodoItem 待办事项
     * @return 结果
     */
    @Override
    public int updateBizTodoItem(BizTodoItem bizTodoItem) {
        return bizTodoItemMapper.updateBizTodoItem(bizTodoItem);
    }

    /**
     * 删除待办事项对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizTodoItemByIds(String ids) {
        return bizTodoItemMapper.deleteBizTodoItemByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除待办事项信息
     *
     * @param id 待办事项ID
     * @return 结果
     */
    @Override
    public int deleteBizTodoItemById(Long id) {
        return bizTodoItemMapper.deleteBizTodoItemById(id);
    }

    @Override
    public int insertTodoItem(String instanceId, String title, String module,String starUserName) {
        return insertTodoItem(instanceId,title,module,null,null,starUserName);
    }

    @Override
    @Transactional
    public int insertTodoItem(String instanceId, String title, String module,Boolean isOK,Long todoId,String starUserName) {
        if (StringUtils.isEmpty(starUserName)){
            throw new BusinessException("流程发起人不能为空");
        }
        BizTodoItem todoItem = new BizTodoItem();
        todoItem.setItemName(title);
        todoItem.setIsView("0");
        todoItem.setIsHandle("0");
        todoItem.setModule(module);
        todoItem.setTodoTime(DateUtils.getNowDate());
        todoItem.setStarUserName(starUserName);

        List<Task> taskList = taskService.createTaskQuery().processInstanceId(instanceId).active().list();
        int counter = 0;
        for (Task task: taskList) {

            // todoitem 去重
            BizTodoItem bizTodoItem = bizTodoItemMapper.selectTodoItemByTaskId(task.getId());
            if (bizTodoItem != null) continue;

            BizTodoItem newItem = new BizTodoItem();
            BeanUtils.copyProperties(todoItem, newItem);
            newItem.setTaskId(task.getId());
            newItem.setTaskName("task" + task.getTaskDefinitionKey().substring(0, 1).toUpperCase() + task.getTaskDefinitionKey().substring(1));
            newItem.setNodeName(task.getName());
            newItem.setInstanceId(instanceId);
            String assignee = task.getAssignee();
            if (StringUtils.isNotBlank(assignee)) {
                newItem.setTodoUserId(assignee);
                SysUser user = userMapper.selectUserByLoginName(assignee);
                newItem.setTodoUserName(user.getUserName());
                insertBizTodoItem(newItem);
                counter++;
            } else {
                List<String> todoUserIdList = processMapper.selectTodoUserListByTaskId(task.getId());
                if(todoUserIdList.isEmpty()){
                    throw new BusinessException("下一节点的待办人为空，检查审批节点的人员配置！");
                }
                for (String todoUserId: todoUserIdList) {
                    SysUser todoUser = userMapper.selectUserByLoginName(todoUserId);
                    newItem.setTodoUserId(todoUser.getLoginName());
                    newItem.setTodoUserName(todoUser.getUserName());
                    insertBizTodoItem(newItem);
                    counter++;
                }
            }
        }
        if(counter <= 0){
            //流程結束維護待办事项表
            if (isOK == null || todoId == null){
                throw new BusinessException("流程完成一定要维护流程状态");
            }
            BizTodoItem bizTodoItem = bizTodoItemMapper.selectBizTodoItemById(todoId);
            if (bizTodoItem != null){
                if (isOK){
                    bizTodoItem.setStatus("1");
                }else {
                    bizTodoItem.setStatus("2");
                }
                bizTodoItemMapper.updateBizTodoItem(bizTodoItem);
            }

        }

        return counter;
    }

    @Override
    public Map<String, Object> selectBizTodoItemListCount(BizTodoItem bizTodoItem) {
        Map<String, Object> map = new HashMap<>();
        //入职代办数
        bizTodoItem.setModule(ProcessKey.userDefined01NonManager);
        Integer nonManager = bizTodoItemMapper.selectBizTodoItemListCount(bizTodoItem);
        map.put(ProcessKey.userDefined01NonManager,nonManager);


        //转正代办数
        bizTodoItem.setModule(ProcessKey.PROCESS_REGULAR);
        Integer regular = bizTodoItemMapper.selectBizTodoItemListCount(bizTodoItem);
        map.put(ProcessKey.PROCESS_REGULAR,regular);

        //请假代办数
        bizTodoItem.setModule(ProcessKey.PROCESS_LEAVE);
        Integer leave = bizTodoItemMapper.selectBizTodoItemListCount(bizTodoItem);
        map.put(ProcessKey.PROCESS_LEAVE,leave);

        //离职代办数
        bizTodoItem.setModule(ProcessKey.PROCESS_QUIT);
        Integer quit = bizTodoItemMapper.selectBizTodoItemListCount(bizTodoItem);
        map.put(ProcessKey.PROCESS_QUIT,quit);

        //调动代办数
        bizTodoItem.setModule(ProcessKey.transfer);
        Integer transfer = bizTodoItemMapper.selectBizTodoItemListCount(bizTodoItem);
        map.put(ProcessKey.transfer,transfer);

        return map;
    }

    @Override
    public Map<String, Object> listCountForEmp(BizTodoItem bizTodoItem) {
        Map<String, Object> map = new HashMap<>();

        BizTodoItem nonManager = bizTodoItemMapper.listCountForEmp(bizTodoItem);
        if (nonManager == null){
            map.put("count1",0);
            map.put("count2",0);
            map.put("count3",0);
            map.put("count4",0);
        }else {
            map.put("count1",nonManager.getCount1());
            map.put("count2",nonManager.getCount2());
            map.put("count3",nonManager.getCount3());
            map.put("count4",nonManager.getCount4());
        }

        //我的待辦理
        BizTodoItem bizTodoItem1 = bizTodoItemMapper.listCountForEmpHandle(bizTodoItem);
        if (bizTodoItem1 == null){
            map.put("count5",0);
        }else {
            map.put("count5",bizTodoItem1.getCount5());
        }

        //我的抄送
        List<String> moduleList = selectModules(ShiroUtils.getUserId());
        map.put("count6",0);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(moduleList)) {
            String[] mudoles = new String[moduleList.size()];
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
            Integer count = bizTodoItemMapper.selectCopyTodoItemCount(bizTodoItem, moduleList.toArray(mudoles));
            map.put("count6",count);
        }


        return map;
    }


    @Override
    public Long updateTodoItem(String taskId,String isApproved) {
        // 更新待办事项状态
        BizTodoItem query = new BizTodoItem();
        query.setTaskId(taskId);
        // 考虑到候选用户组，会有多个 todoitem 办理同个 task
        List<BizTodoItem> updateList = CollectionUtils.isEmpty(selectBizTodoItemList(query)) ? null : selectBizTodoItemList(query);
        Long todoId = null;
        for (BizTodoItem update : updateList) {
            // 找到当前登录用户的 todoitem，置为已办
            if (update.getTodoUserId().equals(ShiroUtils.getLoginName())) {
                update.setIsView("1");
                update.setIsHandle("1");
                update.setHandleUserId(ShiroUtils.getLoginName());
                update.setHandleUserName(ShiroUtils.getSysUser().getUserName());
                update.setHandleTime(DateUtils.getNowDate());
                update.setIsApproved(isApproved);
                updateBizTodoItem(update);
                todoId = update.getId();
            } else {
                deleteBizTodoItemById(update.getId()); // 删除候选用户组其他 todoitem
            }
        }
        return todoId;
    }

    @Override
    public List<BizTodoItem> selectByTaskId(String taskId) {
        return bizTodoItemMapper.selectByTaskId(taskId);
    }
  @Override
    public List<BizTodoItem> selectByTaskIdAll(String taskId) {
        return bizTodoItemMapper.selectByTaskIdAll(taskId);
    }

    @Override
    public String getEndHandleName(String instanceId) {
        return bizTodoItemMapper.getEndHandleName(instanceId);
    }

    @Override
    public List<String> selectModules(Long userId) {
        return bizTodoItemMapper.selectModules(userId);
    }

    @Override
    public List<BizTodoItem> selectCopyTodoItemList(BizTodoItem bizTodoItem,String[] modules) {
        List<BizTodoItem> BizTodoItemList = bizTodoItemMapper.selectCopyTodoItemList(bizTodoItem,modules);
        for (BizTodoItem item: BizTodoItemList) {
            SysUser sysUser = userMapper.selectUserByLoginName(item.getStarUserName());
            if (sysUser != null) {
                item.setApplyUserName(sysUser.getUserName());
                StringBuilder stringBuilder = new StringBuilder();
                if (StringUtils.isNotEmpty(item.getInstanceId())){
                    List<Task> taskList = taskService.createTaskQuery()
                            .processInstanceId(item.getInstanceId())
                            .list();
                    for (Task taskitem : taskList) {
                        List<BizTodoItem> bizTodoItems = selectByTaskId(taskitem.getId());
                        for (BizTodoItem bizTodo : bizTodoItems) {
                            stringBuilder.append(bizTodo.getTodoUserName() + " - ");
                        }
                    }
                }
                item.setTodoUserName(stringBuilder.toString());
            }
        }
        return BizTodoItemList;
    }

    @Override
    public List<BizTodoItem> selectMainCopyTodoItemList(BizTodoItem bizTodoItem,String[] modules) {
        List<BizTodoItem> BizTodoItemList = bizTodoItemMapper.selectMainCopyTodoItemList(bizTodoItem,modules);
        for (BizTodoItem item: BizTodoItemList) {
            SysUser sysUser = userMapper.selectUserByLoginName(item.getStarUserName());
            if (sysUser != null) {
                item.setApplyUserName(sysUser.getUserName());
                StringBuilder stringBuilder = new StringBuilder();
                if (StringUtils.isNotEmpty(item.getInstanceId())){
                    List<Task> taskList = taskService.createTaskQuery()
                            .processInstanceId(item.getInstanceId())
                            .list();
                    for (Task taskitem : taskList) {
                        List<BizTodoItem> bizTodoItems = selectByTaskId(taskitem.getId());
                        for (BizTodoItem bizTodo : bizTodoItems) {
                            stringBuilder.append(bizTodo.getTodoUserName() + " - ");
                        }
                    }
                }
                item.setTodoUserName(stringBuilder.toString());
            }
        }
        return BizTodoItemList;
    }

    @Override
    @Transactional
    public int taskEntrust(String taskId,String todoUserId,String userId,String loginName,String empName){
        int i = 0;
        try{
            BizTodoItem selectItem = new BizTodoItem();
            selectItem.setIsHandle("0");
            selectItem.setTaskId(taskId);
            selectItem.setTodoUserId(todoUserId);
            List<BizTodoItem> returnItems = selectBizTodoItemList(selectItem);

            BizTodoItem updateItem = new BizTodoItem();
            updateItem.setTaskId(taskId);
            updateItem.setTodoUserId(loginName);
            updateItem.setTodoUserName(empName);
            updateItem.setId(returnItems.get(0).getId());
            taskService.setAssignee(taskId,loginName);
            i = updateBizTodoItem(updateItem);
            log.info("任务转发成功！");
        }catch(Exception e){
            e.printStackTrace();
            log.info("任务转发失败！");
        }
        return i;
    }
}
