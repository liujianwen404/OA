package com.ruoyi.hr.domain;

import java.util.Date;
import java.util.List;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.process.utils.SpringUtil;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 入职申请对象 t_hr_non_manager
 * 
 * @author xt
 * @date 2020-05-14
 */
@Data
@Table(name = "t_hr_non_manager")
@ColumnWidth(20)
public class HrNonManager extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 入职申请id */
    @Id
    @ExcelIgnore
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 入职人id */
    @Column(name = "emp_id")
    @ExcelIgnore
    private String empId;

    /** 入职人姓名 */
    @Column(name = "emp_name")
    @ExcelProperty(index = 0,value = "入职员工")
    private String empName;

    /** 直接上级 */
    @Column(name = "dept_leader")
    @ExcelIgnore
    private String deptLeader;

    /** 部门经理 */
    @Column(name = "dept_manager")
    @ExcelIgnore
    private String deptManager;

    /** 部门总监 */
    @Column(name = "dept_chief")
    @ExcelIgnore
    private String deptChief;

    /** 入职日期 */
    @Column(name = "non_manager_Date")
    @DateTimeFormat(value = "yyyy-MM-dd")
    @ExcelProperty(index = 1,value = "入职日期")
    private Date nonManagerDate;

    /** 入职岗位id */
    @Column(name = "non_manager_post_id")
    @ExcelIgnore
    private Long nonManagerPostId;

    @Transient
    @ExcelProperty(index = 3,value = "入职岗位")
    private String postName;
    @Transient
    @ExcelIgnore
    private Long postId;

    /** 入职部门id */
    @Column(name = "non_manager_dept_id")
    @ExcelIgnore
    private Long nonManagerDeptId;


    @Transient
    @ExcelProperty(index = 2,value = "入职部门")
    private String deptName;

    @Transient
    @ExcelIgnore
    private Long deptId;

    /** 生日 */
    @Column(name = "birthday")
    @ExcelIgnore
    private Date birthday;

    /** 用户性别（0男 1女 2未知） */
    @Column(name = "sex")
    @ExcelIgnore
    private String sex;

    /** 院校 */
    @Column(name = "recruit_schools")
    @ExcelIgnore
    private String recruitSchools;

    /** 教育背景（学历） */
    @Column(name = "recruit_education")
    @ExcelIgnore
    private String recruitEducation;

    /** 专业技能 */
    @Column(name = "recruit_skill")
    @ExcelIgnore
    private String recruitSkill;

    /** 工作年限 */
    @Column(name = "recruit_job_description")
    @ExcelIgnore
    private String recruitJobDescription;

    /** 删除标志（0代表存在 1代表删除） */
    @ExcelIgnore
    private String delFlag;

    /** 审核状态（0：待提交，1：审核中，2：通过，3：未通过） */
    @Column(name = "audit_status")
    @ExcelIgnore
    private Integer auditStatus;

    @Transient
    @ExcelProperty(index = 5,value = "当前处理人")
    private String todoUserNameExcel;

    @Transient
    @ExcelProperty(index = 6,value = "审核状态")
    private String auditStatusExcel;

    // '流程实例id',
    @Column(name = "instance_id")
    @ExcelIgnore
    private String instanceId;

    // ''人事总监审核(0:不需要，1：需要)'',
    @Column(name = "is_hr_majordomo")
    @ExcelIgnore
    private Integer isHrMajordomo;
    // ''总经理审核(0:不需要，1：需要)'',
    @Column(name = "is_general_manager")
    @ExcelIgnore
    private Integer isGeneralManager;

    @Column(name = "status")
    @ExcelIgnore
    private String status;

    @Column(name = "citys")
    @ExcelProperty(index = 4,value = "入职城市")
    private String citys;

    @Column(name = "is_manager")
    @ExcelIgnore
    private String isManager;

    @Column(name = "attachment")
    @ExcelIgnore
    private String attachment;

    @Transient
    @ExcelIgnore
    private List<String> imgList;

    @Column(name = "apply_user_id")
    @ExcelIgnore
    private Long applyUserId;

    /** 发起人姓名 */
    @Column(name = "apply_user_name")
    @ExcelIgnore
    private String applyUserName;

    /** 申请时间 */
    @Column(name = "apply_time")
    @ExcelProperty(index = 7,value = "申请时间")
    private Date applyTime;


    public void setNonManagerDate(Date nonManagerDate)
    {
        this.nonManagerDate = nonManagerDate;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getNonManagerDate()
    {
        return nonManagerDate;
    }

    @Override
    public String getTodoUserName() {
        return SpringUtil.getHandleNmae(getInstanceId(), getAuditStatus());
    }
    public Long getDeptId() {
        if (nonManagerDeptId == null){
            return deptId;
        }
        return nonManagerDeptId;
    }
    public Long getPostId() {
        return nonManagerPostId;
    }




}
