package com.ruoyi.process.todoitem.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 待办事项对象 biz_todo_item
 *
 * @author Xianlu Tech
 * @date 2019-11-08
 */
public class BizTodoItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 ID */
    private Long id;

    /** 事项标题 */
    @Excel(name = "事项标题")
    private String itemName;

    /** 事项内容 */
    @Excel(name = "事项内容")
    private String itemContent;

    /** 模块名称 (必须以 uri 一致) */
    @Excel(name = "模块名称")
    private String module;

    /** 任务 ID */
    @Excel(name = "任务 ID")
    private String taskId;

    /** 任务名称 (必须以表单页面名称一致) */
    @Excel(name = "任务名称")
    private String taskName;

    /** 节点名称 */
    @Excel(name = "节点名称")
    private String nodeName;

    /** 是否查看 default 0 (0 否 1 是) */
    @Excel(name = "是否查看")
    private String isView;

    /** 是否处理 default 0 (0 否 1 是) */
    @Excel(name = "是否处理")
    private String isHandle;

    /** 待办人 ID */
    @Excel(name = "待办人 ID")
    private String todoUserId;

    /** 待办人名称 */
    @Excel(name = "待办人名称")
    private String todoUserName;

    /** 处理人 ID */
    @Excel(name = "处理人 ID")
    private String handleUserId;

    /** 处理人名称 */
    @Excel(name = "处理人名称")
    private String handleUserName;

    /** 通知时间 */
    @Excel(name = "通知时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date todoTime;

    /** 处理时间 */
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handleTime;

    /** 发起人名称 */
    @Excel(name = "发起人名称")
    private String starUserName;

    private String isApproved;

    private String instanceId;

    private String status;

    //员工
    private Integer count1;
    private Integer count2;
    private Integer count3;
    private Integer count4;
    private Integer count5;

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getCount5() {
        return count5;
    }

    public void setCount5(Integer count5) {
        this.count5 = count5;
    }

    public Integer getCount1() {
        return count1;
    }

    public void setCount1(Integer count1) {
        this.count1 = count1;
    }

    public Integer getCount2() {
        return count2;
    }

    public void setCount2(Integer count2) {
        this.count2 = count2;
    }

    public Integer getCount3() {
        return count3;
    }

    public void setCount3(Integer count3) {
        this.count3 = count3;
    }

    public Integer getCount4() {
        return count4;
    }

    public void setCount4(Integer count4) {
        this.count4 = count4;
    }

    public String getStarUserName() {
        return starUserName;
    }

    public void setStarUserName(String starUserName) {
        this.starUserName = starUserName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public String getItemContent() {
        return itemContent;
    }
    public void setModule(String module) {
        this.module = module;
    }

    public String getModule() {
        return module;
    }
    @Override
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }
    @Override
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }
    public void setIsView(String isView) {
        this.isView = isView;
    }

    public String getIsView() {
        return isView;
    }
    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    public String getIsHandle() {
        return isHandle;
    }
    public void setTodoUserId(String todoUserId) {
        this.todoUserId = todoUserId;
    }

    public String getTodoUserId() {
        return todoUserId;
    }
    @Override
    public void setTodoUserName(String todoUserName) {
        this.todoUserName = todoUserName;
    }

    @Override
    public String getTodoUserName() {
        return todoUserName;
    }
    public void setHandleUserId(String handleUserId) {
        this.handleUserId = handleUserId;
    }

    public String getHandleUserId() {
        return handleUserId;
    }
    public void setHandleUserName(String handleUserName) {
        this.handleUserName = handleUserName;
    }

    public String getHandleUserName() {
        return handleUserName;
    }
    public void setTodoTime(Date todoTime) {
        this.todoTime = todoTime;
    }

    public Date getTodoTime() {
        return todoTime;
    }
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("itemName", getItemName())
            .append("itemContent", getItemContent())
            .append("module", getModule())
            .append("taskId", getTaskId())
            .append("taskName", getTaskName())
            .append("isView", getIsView())
            .append("isHandle", getIsHandle())
            .append("todoUserId", getTodoUserId())
            .append("todoUserName", getTodoUserName())
            .append("handleUserId", getHandleUserId())
            .append("handleUserName", getHandleUserName())
            .append("todoTime", getTodoTime())
            .append("handleTime", getHandleTime())
            .toString();
    }
}
