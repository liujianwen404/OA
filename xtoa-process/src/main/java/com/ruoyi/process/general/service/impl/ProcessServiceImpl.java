package com.ruoyi.process.general.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.process.general.domain.HistoricActivity;
import com.ruoyi.process.general.mapper.ProcessMapper;
import com.ruoyi.process.general.service.IProcessService;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.CommentEntityImpl;
import org.activiti.engine.task.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProcessServiceImpl implements IProcessService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private IBizTodoItemService bizTodoItemService;

    @Override
    public List<HistoricActivity> selectHistoryList(String processInstanceId, HistoricActivity historicActivity) {
        List<HistoricActivity> activityList = new ArrayList<>();
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();
        if (StringUtils.isNotBlank(historicActivity.getAssignee())) {
            query.taskAssignee(historicActivity.getAssignee());
        }
        if (StringUtils.isNotBlank(historicActivity.getActivityName())) {
            query.activityName(historicActivity.getActivityName());
        }
        List<HistoricActivityInstance> list = query.processInstanceId(processInstanceId)
                .activityType("userTask")
                .finished()
                .orderByHistoricActivityInstanceStartTime()
                .desc()
                .list();
        list.forEach(instance -> {
            List<BizTodoItem> bizTodoItems = bizTodoItemService.selectByTaskIdAll(instance.getTaskId());
            if (!bizTodoItems.isEmpty()){
                HistoricActivity activity = new HistoricActivity();
                BeanUtils.copyProperties(instance, activity);
                String taskId = instance.getTaskId();
                List<Comment> comment = taskService.getTaskComments(taskId, "comment");
                if (!CollectionUtils.isEmpty(comment)) {
                    if(comment instanceof CommentEntityImpl){
                        CommentEntityImpl commentEntity = (CommentEntityImpl) comment;
                        activity.setComment(commentEntity.getMessage());
                    }
                    activity.setComment(comment.get(0).getFullMessage());
                }
                SysUser sysUser = userMapper.selectUserByLoginName(instance.getAssignee());
                if (sysUser != null) {
                    activity.setAssigneeName(sysUser.getUserName());
                }

                bizTodoItems.forEach(bizTodoItem -> {
                    activity.setIsApproved(StringUtils.isNotBlank(bizTodoItem.getIsApproved())&&bizTodoItem.getIsApproved().equals("0")?"同意":"拒绝");
                });
                activityList.add(activity);
            }

        });
        return activityList;
    }

    @Override
    public HistoricProcessInstance getDeleteReason(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance != null
                && historicProcessInstance.getEndTime() != null
                && historicProcessInstance.getEndActivityId() == null
                && StringUtils.isNotEmpty(historicProcessInstance.getDeleteReason())
                ){
            return historicProcessInstance;
        }
        return null;
    }

}
