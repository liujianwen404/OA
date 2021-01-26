package com.ruoyi.base.domain.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.Ztree;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 员工对象 t_hr_emp
 * 
 * @author vivi07
 * @date 2020-05-07
 */
public class HrEmpVo extends Ztree
{
    private static final long serialVersionUID = 1L;

    /** 员工id */
    private Long empId;

    /** 用户id */
    private Long userId;

    /** 员工姓名 */
    private String empName;

    /** 部门id */
    private Long deptId;
    private String deptName;
    private String deptPName;
    private Long parentId;

    /** 岗位id */
    private Long postId;
    private String postName;

    /** 岗位id */
    private String empNum;
    /** 岗位id */
    private Date nonManagerDate;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getEmpId() {
        return empId;
    }

    public String getDeptPName() {
        return deptPName;
    }

    public void setDeptPName(String deptPName) {
        this.deptPName = deptPName;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }


    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getEmpNum() {
        return empNum;
    }

    public void setEmpNum(String empNum) {
        this.empNum = empNum;
    }

    public Date getNonManagerDate() {
        return nonManagerDate;
    }

    public void setNonManagerDate(Date nonManagerDate) {
        this.nonManagerDate = nonManagerDate;
    }
}
