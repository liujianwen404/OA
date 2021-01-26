package com.ruoyi.hr.service;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.HrEmp;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 业务流程处理Service接口
 *
 * @author 刘剑文
 * @date 2020-05-29
 */
public interface ProcessHandleService {

    /**
     * 查询待办人
     * */
    String selectToUserNameByAssignee(Task task);

    /**
     * 查询当前登录员工
     * */
    HrEmp selectTHrEmpByUserId();

    /**
     * 提交申请
     * */
    ProcessInstance submitApply(String module, String businessKey, String loginName, String title, Map<String, Object> variables);

    /**
     * 审批流程
     * */
    int complete(String instanceId, String module, String title, String taskId, Map<String, Object> variables, boolean isOk ,String comment,Boolean p_B_approved);

    /**
     * 拒绝审批流程
     * */
    void completeDown(String instanceId, String taskId, Map<String, Object> variables, String comment,Boolean p_B_approved);


    /**
     * 流程撤回
     */
    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);

    /**
     * 跳转到指定节点
     * @param taskId 任务id
     * @param targetNodeId 目标节点，为空时会跳转到结束节点
     */
    void jumpToNode(String taskId, String targetNodeId);


    /**
     * 开启流程时设置审批人
     * @param processKey 流程的key
     * @param values 开启流程需要的变量容器
     * @param deptId 流程相关部门Id，可以为空
     * @param jsonObject 业务对象的json数据。设置条件时需要用到
     */
    void setAssignee(String processKey, Map<String, Object> values, Long deptId, JSONObject jsonObject);

    /**
     * 设置审批节点变量和审批人
     * @param processKey 流程的key
     * @param values 开启流程需要的变量容器
     * @param deptId 流程相关部门Id，可以为空
     * @param taskId 任务Id，可以为空（为空时则说明当前节点为开始节点）
     * @param jsonObject  业务对象的json数据。设置条件时需要用到
     */
    void setAssignee(String processKey, Map<String, Object> values, Long deptId, String taskId, JSONObject jsonObject);

}
