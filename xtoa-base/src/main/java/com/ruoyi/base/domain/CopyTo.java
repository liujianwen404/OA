package com.ruoyi.base.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程抄送关系对象 t_copy_to
 * 
 * @author liujianwen
 * @date 2020-06-06
 */
@Data
@Table(name = "t_copy_to")
public class CopyTo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 抄送员工id */
    @Column(name = "emp_id")
    @Excel(name = "员工号")
    private String empId;

    /** 抄送员工姓名 */
    @Column(name = "emp_name")
    @Excel(name = "抄送员工姓名")
    private String empName;

    /** 抄送用户id */
    @Column(name = "user_id")
    private String userId;

    /** 部门id */
    @Column(name = "dept_id")
    private Long deptId;

    /** 岗位id */
    @Column(name = "post_id")
    private Long postId;

    /** 流程KEY */
    @Column(name = "process_key")
    @Excel(name = "流程KEY")
    private String processKey;

}
