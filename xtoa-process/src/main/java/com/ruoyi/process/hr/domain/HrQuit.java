//package com.ruoyi.process.hr.domain;
//
//import com.ruoyi.common.annotation.Excel;
//import com.ruoyi.common.core.domain.BaseEntity;
//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;
//import tk.mybatis.mapper.annotation.KeySql;
//
//import javax.persistence.Column;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.util.Date;
//
///**
// * 离职申请对象 t_hr_quit
// *
// * @author liujianwen
// * @date 2020-05-15
// */
//@Table(name = "t_hr_quit")
//public class HrQuit extends BaseEntity
//{
//    private static final long serialVersionUID = 1L;
//
//    /** 主键ID */
//    @Id
//    @KeySql(useGeneratedKeys = true)
//    private Long id;
//
//    /** 标题 */
//    @Column(name = "title")
//    @Excel(name = "标题")
//    private String title;
//
//    /** 离职原因 */
//    @Column(name = "reason")
//    @Excel(name = "离职原因")
//    private String reason;
//
//    /** 离职时间 */
//    @Column(name = "quit_time")
//    @Excel(name = "离职时间", width = 30, dateFormat = "yyyy-MM-dd")
//    private Date quitTime;
//
//    /** 部门id */
//    @Column(name = "dept_id")
//    @Excel(name = "部门id")
//    private Long deptId;
//
//    /** 部门名称 */
//    @Column(name = "dept_name")
//    @Excel(name = "部门名称")
//    private String deptName;
//
//    /** 岗位id */
//    @Column(name = "post_id")
//    @Excel(name = "岗位id")
//    private Long postId;
//
//    /** 岗位名称 */
//    @Column(name = "post_name")
//    @Excel(name = "岗位名称")
//    private String postName;
//
//    /** 流程实例ID */
//    @Column(name = "instance_id")
//    @Excel(name = "流程实例ID")
//    private String instanceId;
//
//    /** 实际开始时间 */
//    @Column(name = "reality_start_time")
//    @Excel(name = "实际开始时间", width = 30, dateFormat = "yyyy-MM-dd")
//    private Date realityStartTime;
//
//    /** 实际结束时间 */
//    @Column(name = "reality_end_time")
//    @Excel(name = "实际结束时间", width = 30, dateFormat = "yyyy-MM-dd")
//    private Date realityEndTime;
//
//    /** 申请人 */
//    @Column(name = "apply_user")
//    @Excel(name = "申请人")
//    private String applyUser;
//
//    /** 申请时间 */
//    @Column(name = "apply_time")
//    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
//    private Date applyTime;
//
//    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销) */
//    @Column(name = "audit_status")
//    @Excel(name = "审核状态")
//    private String auditStatus;
//
//    public void setId(Long id)
//    {
//        this.id = id;
//    }
//
//    public Long getId()
//    {
//        return id;
//    }
//    public void setTitle(String title)
//    {
//        this.title = title;
//    }
//
//    public String getTitle()
//    {
//        return title;
//    }
//    public void setReason(String reason)
//    {
//        this.reason = reason;
//    }
//
//    public String getReason()
//    {
//        return reason;
//    }
//    public void setQuitTime(Date quitTime)
//    {
//        this.quitTime = quitTime;
//    }
//
//    public Date getQuitTime()
//    {
//        return quitTime;
//    }
//    public void setDeptId(Long deptId)
//    {
//        this.deptId = deptId;
//    }
//
//    public Long getDeptId()
//    {
//        return deptId;
//    }
//    public void setDeptName(String deptName)
//    {
//        this.deptName = deptName;
//    }
//
//    public String getDeptName()
//    {
//        return deptName;
//    }
//    public void setPostId(Long postId)
//    {
//        this.postId = postId;
//    }
//
//    public Long getPostId()
//    {
//        return postId;
//    }
//    public void setPostName(String postName)
//    {
//        this.postName = postName;
//    }
//
//    public String getPostName()
//    {
//        return postName;
//    }
//    public void setInstanceId(String instanceId)
//    {
//        this.instanceId = instanceId;
//    }
//
//    public String getInstanceId()
//    {
//        return instanceId;
//    }
//    public void setRealityStartTime(Date realityStartTime)
//    {
//        this.realityStartTime = realityStartTime;
//    }
//
//    public Date getRealityStartTime()
//    {
//        return realityStartTime;
//    }
//    public void setRealityEndTime(Date realityEndTime)
//    {
//        this.realityEndTime = realityEndTime;
//    }
//
//    public Date getRealityEndTime()
//    {
//        return realityEndTime;
//    }
//    public void setApplyUser(String applyUser)
//    {
//        this.applyUser = applyUser;
//    }
//
//    public String getApplyUser()
//    {
//        return applyUser;
//    }
//    public void setApplyTime(Date applyTime)
//    {
//        this.applyTime = applyTime;
//    }
//
//    public Date getApplyTime()
//    {
//        return applyTime;
//    }
//
//    public String getAuditStatus() {
//        return auditStatus;
//    }
//
//    public void setAuditStatus(String auditStatus) {
//        this.auditStatus = auditStatus;
//    }
//
//    @Override
//    public String toString() {
//        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
//            .append("id", getId())
//            .append("title", getTitle())
//            .append("reason", getReason())
//            .append("quitTime", getQuitTime())
//            .append("deptId", getDeptId())
//            .append("deptName", getDeptName())
//            .append("postId", getPostId())
//            .append("postName", getPostName())
//            .append("instanceId", getInstanceId())
//            .append("realityStartTime", getRealityStartTime())
//            .append("realityEndTime", getRealityEndTime())
//            .append("createBy", getCreateBy())
//            .append("createTime", getCreateTime())
//            .append("updateBy", getUpdateBy())
//            .append("updateTime", getUpdateTime())
//            .append("applyUser", getApplyUser())
//            .append("applyTime", getApplyTime())
//            .toString();
//    }
//}
