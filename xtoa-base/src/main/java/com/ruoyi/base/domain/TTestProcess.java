package com.ruoyi.base.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 测试流程单对象 t_test_process
 * 
 * @author xt
 * @date 2020-11-10
 */
@Table(name = "t_test_process")
public class TTestProcess extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 标题 */
    @Column(name = "title")
    @Excel(name = "标题")
    private String title;

    /** 附件名称 */
    @Column(name = "attachment")
    @Excel(name = "附件名称")
    private String attachment;

    /** 附件路径 */
    @Column(name = "path")
    @Excel(name = "附件路径")
    private String path;

    /** 员工id */
    @Column(name = "emp_id")
    @Excel(name = "员工id")
    private Long empId;

    /** 部门id */
    @Column(name = "dept_id")
    @Excel(name = "部门id")
    private Long deptId;

    /** 部门名称 */
    @Column(name = "dept_name")
    @Excel(name = "部门名称")
    private String deptName;

    /** 岗位id */
    @Column(name = "post_id")
    @Excel(name = "岗位id")
    private Long postId;

    /** 岗位名称 */
    @Column(name = "post_name")
    @Excel(name = "岗位名称")
    private String postName;

    /** 流程实例ID */
    @Column(name = "instance_id")
    @Excel(name = "流程实例ID")
    private String instanceId;

    /** 申请人姓名 */
    @Column(name = "apply_user_name")
    @Excel(name = "申请人姓名")
    private String applyUserName;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销) */
    @Column(name = "audit_status")
    @Excel(name = "审核状态", readConverterExp = "审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销)")
    private String auditStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setAttachment(String attachment) 
    {
        this.attachment = attachment;
    }

    public String getAttachment() 
    {
        return attachment;
    }
    public void setPath(String path) 
    {
        this.path = path;
    }

    public String getPath() 
    {
        return path;
    }
    public void setEmpId(Long empId) 
    {
        this.empId = empId;
    }

    public Long getEmpId() 
    {
        return empId;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }
    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }
    public void setPostId(Long postId) 
    {
        this.postId = postId;
    }

    public Long getPostId() 
    {
        return postId;
    }
    public void setPostName(String postName) 
    {
        this.postName = postName;
    }

    public String getPostName() 
    {
        return postName;
    }
    public void setInstanceId(String instanceId) 
    {
        this.instanceId = instanceId;
    }

    public String getInstanceId() 
    {
        return instanceId;
    }
    public void setApplyUserName(String applyUserName) 
    {
        this.applyUserName = applyUserName;
    }

    public String getApplyUserName() 
    {
        return applyUserName;
    }
    public void setAuditStatus(String auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus() 
    {
        return auditStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("attachment", getAttachment())
            .append("path", getPath())
            .append("empId", getEmpId())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("postId", getPostId())
            .append("postName", getPostName())
            .append("instanceId", getInstanceId())
            .append("createId", getCreateId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateId", getUpdateId())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("applyUserName", getApplyUserName())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("auditStatus", getAuditStatus())
            .toString();
    }
}
