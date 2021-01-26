package com.ruoyi.hr.activity;

import com.ruoyi.common.utils.StringUtils;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.springframework.stereotype.Component;

public class JumpAnyWhereCmd implements Command {
    private String taskId;

    private String targetNodeId;

    public JumpAnyWhereCmd(String taskId, String targetNodeId) {
        this.taskId = taskId;
        this.targetNodeId = targetNodeId;
    }

    public Object execute(CommandContext commandContext) {
        //获取任务实例管理类
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        //获取任务审批管理类
        IdentityLinkEntityManager identityLinkEntityManager = commandContext.getIdentityLinkEntityManager();
        //获取当前任务实例
        TaskEntity currentTask = taskEntityManager.findById(taskId);

        //获取当前节点的执行实例
        ExecutionEntity execution = currentTask.getExecution();
        String executionId = execution.getId();

        //获取流程定义id
        String processDefinitionId = execution.getProcessDefinitionId();
        //获取目标节点
        Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);
        FlowElement flowElementTargert = null;
        if (StringUtils.isEmpty(targetNodeId)){
            //跳转去结束节点
            for (FlowElement flowElement : process.getFlowElements()) {
                if (flowElement instanceof EndEvent){
                    flowElementTargert = flowElement;
                    break;
                }
            }
        }else {
            flowElementTargert = process.getFlowElement(targetNodeId);
        }

        //获取历史管理
        HistoryManager historyManager = commandContext.getHistoryManager();

        //通知当前活动结束(更新act_hi_actinst)
        historyManager.recordActivityEnd(execution,"jump to " + flowElementTargert.getName());
        //通知任务节点结束(更新act_hi_taskinst)
        historyManager.recordTaskEnd(taskId,"jump to " + flowElementTargert.getName());

        //会签需要先删除审批表
        identityLinkEntityManager.deleteIdentityLinksByTaskId(taskId);

        //删除正在执行的当前任务
        taskEntityManager.delete(taskId);

        //此时设置执行实例的当前活动节点为目标节点
        execution.setCurrentFlowElement(flowElementTargert);

        //向operations中压入继续流程的操作类
        commandContext.getAgenda().planContinueProcessOperation(execution);

        return null;
    }
}
