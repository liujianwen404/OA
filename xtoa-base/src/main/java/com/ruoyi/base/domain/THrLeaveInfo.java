package com.ruoyi.base.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 请假数据对象 t_hr_leave_info
 * 
 * @author xt
 * @date 2020-09-10
 */
@Table(name = "t_hr_leave_info")
public class THrLeaveInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 请假申请id */
    @Column(name = "leave_id")
    @Excel(name = "请假申请id")
    private Long leaveId;

    /** 员工id */
    @Column(name = "emp_id")
    @Excel(name = "员工id")
    private Long empId;

    /** 员工姓名 */
    @Column(name = "emp_name")
    @Excel(name = "员工姓名")
    private String empName;

    /** 请假类型 */
    @Column(name = "type")
    @Excel(name = "请假类型")
    private String type;

    /** 开始时间 */
    @Column(name = "start_time")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @Column(name = "end_time")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 请假时长，单位小时 */
    @Column(name = "total_hours")
    @Excel(name = "请假时长，单位小时")
    private Double totalHours;

    /** null */
    @Column(name = "dept_id")
    @Excel(name = "null")
    private Long deptId;

    /** null */
    @Column(name = "post_id")
    @Excel(name = "null")
    private Long postId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLeaveId(Long leaveId) 
    {
        this.leaveId = leaveId;
    }

    public Long getLeaveId() 
    {
        return leaveId;
    }
    public void setEmpId(Long empId) 
    {
        this.empId = empId;
    }

    public Long getEmpId() 
    {
        return empId;
    }
    public void setEmpName(String empName) 
    {
        this.empName = empName;
    }

    public String getEmpName() 
    {
        return empName;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setTotalHours(Double totalHours) 
    {
        this.totalHours = totalHours;
    }

    public Double getTotalHours() 
    {
        return totalHours;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }
    public void setPostId(Long postId) 
    {
        this.postId = postId;
    }

    public Long getPostId() 
    {
        return postId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("leaveId", getLeaveId())
            .append("empId", getEmpId())
            .append("empName", getEmpName())
            .append("type", getType())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("totalHours", getTotalHours())
            .append("deptId", getDeptId())
            .append("postId", getPostId())
            .append("createId", getCreateId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateId", getUpdateId())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .toString();
    }
}
