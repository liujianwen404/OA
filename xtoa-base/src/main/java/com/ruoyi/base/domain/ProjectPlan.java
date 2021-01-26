package com.ruoyi.base.domain;

import java.util.Date;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目计划对象 t_project_plan
 * 
 * @author xt
 * @date 2020-06-30
 */
@Data
@ToString
@Table(name = "t_project_plan")
public class ProjectPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 计划id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 迭代id */
    @Column(name = "project_id")
    @Excel(name = "项目id")
    private Long projectId;
    @Transient
    private String projectName;
    /** 名称 */
    @Column(name = "name")
    @Excel(name = "名称")
    private String name;

    /** 描述 */
    @Column(name = "content_describe")
    @Excel(name = "描述")
    private String contentDescribe;

    /** 负责人 */
    @Column(name = "emp_id")
    @Excel(name = "负责人")
    private Long empId;
    @Transient
    private String empName;
    /** 开始时间 */
    @Column(name = "start_time")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @Column(name = "end_time")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 状态(0:创建，1:完成，2:关闭) */
    @Column(name = "status")
    @Excel(name = "状态(0:创建，1:完成，2:关闭)")
    private Integer status;

}
