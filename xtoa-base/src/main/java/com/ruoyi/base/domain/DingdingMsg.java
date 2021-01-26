package com.ruoyi.base.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 钉钉工作通知发送记录对象 t_dingding_msg
 * 
 * @author xt
 * @date 2020-07-16
 */
@Table(name = "t_dingding_msg")
public class DingdingMsg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 接收者的企业内部用户的userid列表，最大用户列表长度：100 */
    @Column(name = "userid_list")
    @Excel(name = "接收者的企业内部用户的userid列表，最大用户列表长度：100")
    private String useridList;

    /** 接收者的部门id列表，最大列表长度：20,  接收者是部门id下(包括子部门下)的所有用户 */
    @Column(name = "dept_id_list")
    @Excel(name = "接收者的部门id列表，最大列表长度：20,  接收者是部门id下(包括子部门下)的所有用户")
    private String deptIdList;

    /** 是否发送给企业全部用户 */
    @Column(name = "to_all_user")
    @Excel(name = "是否发送给企业全部用户")
    private Integer toAllUser;

    /** 消息类型 */
    @Column(name = "msg_type")
    @Excel(name = "消息类型")
    private String msgType;

    /** json字符串，消息内容，消息类型和样例参考“消息类型与数据格式”。最长不超过2048个字节 */
    @Column(name = "msg")
    @Excel(name = "json字符串，消息内容，消息类型和样例参考“消息类型与数据格式”。最长不超过2048个字节")
    private String msg;

    /** 创建的发送任务id */
    @Column(name = "task_id")
    @Excel(name = "创建的发送任务id")
    private String taskId;

    /** 关联业务类型（1：项目任务通知） */
    @Column(name = "biz_type")
    @Excel(name = "关联业务类型", readConverterExp = "1=：项目任务通知")
    private Integer bizType;

    /** 关联业务id */
    @Column(name = "biz_id")
    @Excel(name = "关联业务id")
    private String bizId;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUseridList(String useridList) 
    {
        this.useridList = useridList;
    }

    public String getUseridList() 
    {
        return useridList;
    }
    public void setDeptIdList(String deptIdList) 
    {
        this.deptIdList = deptIdList;
    }

    public String getDeptIdList() 
    {
        return deptIdList;
    }
    public void setToAllUser(Integer toAllUser) 
    {
        this.toAllUser = toAllUser;
    }

    public Integer getToAllUser() 
    {
        return toAllUser;
    }
    public void setMsg(String msg) 
    {
        this.msg = msg;
    }

    public String getMsg() 
    {
        return msg;
    }
    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskId()
    {
        return taskId;
    }
    public void setBizType(Integer bizType) 
    {
        this.bizType = bizType;
    }

    public Integer getBizType() 
    {
        return bizType;
    }
    public void setBizId(String bizId) 
    {
        this.bizId = bizId;
    }

    public String getBizId() 
    {
        return bizId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("useridList", getUseridList())
            .append("deptIdList", getDeptIdList())
            .append("toAllUser", getToAllUser())
            .append("msg", getMsg())
            .append("taskId", getTaskId())
            .append("bizType", getBizType())
            .append("bizId", getBizId())
            .append("createId", getCreateId())
            .append("updateId", getUpdateId())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
