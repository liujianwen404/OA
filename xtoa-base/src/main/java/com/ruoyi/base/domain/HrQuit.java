package com.ruoyi.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 离职申请对象 t_hr_quit
 * 
 * @author liujianwen
 * @date 2020-05-15
 */
@Data
@Table(name = "t_hr_quit")
public class HrQuit extends BaseEntity
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

    /** 申请人姓名 */
    @Column(name = "apply_user_name")
    @Excel(name = "申请人")
    private String applyUserName;


    /** 离职原因 */
    @Column(name = "reason")
    private String reason;

    /** 离职时间 */
    @Column(name = "quit_time")
    @Excel(name = "离职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date quitTime;

    /** 部门id */
    @Column(name = "dept_id")
    private Long deptId;

    /** 部门名称 */
    @Column(name = "dept_name")
    private String deptName;

    /** 岗位id */
    @Column(name = "post_id")
    private Long postId;

    /** 流程所属员工id */
    @Column(name = "emp_id")
    private Long empId;

    /** 岗位名称 */
    @Column(name = "post_name")
    private String postName;

    /** 流程实例ID */
    @Column(name = "instance_id")
    private String instanceId;

    /** 实际开始时间 */
    @Column(name = "reality_start_time")
    private Date realityStartTime;

    /** 实际结束时间 */
    @Column(name = "reality_end_time")
    private Date realityEndTime;

    /** 申请人 */
    @Column(name = "apply_user")
    private String applyUser;

    /** 申请时间 */
    @Column(name = "apply_time")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销) */
    @Column(name = "audit_status")
    @Excel(name = "审核状态",readConverterExp = "0=待提交,1=审核中,2=通过,3=拒绝,4=撤销")
    private String auditStatus;

    /** 附件 */
    @Column(name = "attachment")
    private String attachment;

    /** 职级 */
    @Column(name = "rank")
    private String rank;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getQuitTime() {
        return quitTime;
    }

    public void setQuitTime(Date quitTime) {
        this.quitTime = quitTime;
    }
}
