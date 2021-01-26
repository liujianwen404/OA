package com.ruoyi.base.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 *  t_training_records
 * 
 * @author chenfei
 * @date 2021-01-04
 */
@Data
@ToString
@Table(name = "t_training_records")
public class TTrainingRecords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 员工工号 */
    @Column(name = "emp_num")
    @Excel(name = "员工工号")
    private String empNum;

    /** 员工姓名 */
    @Column(name = "emp_name")
    @Excel(name = "员工姓名")
    private String empName;

    /** 部门id */
    @Column(name = "dept_id")
    private Long deptId;

    /** 岗位id */
    @Column(name = "post_id")
    private Long postId;

    @Transient
    @Excel(name = "所属部门")
    private String deptName;

    @Transient
    @Excel(name = "职位")
    private String postName;

    /** 总培训时长 */
    @Excel(name = "总培训时长")
    @Transient
    private Float trainingTimeTotal;


    /** 培训次数 */
    @Excel(name = "培训次数")
    @Transient
    private Integer participateTotal;

    /** 培训开始时间 */
    @Column(name = "training_start_time")
    @Excel(name = "培训开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "培训开始时间不能为空")
    private Date trainingStartTime;

    /** 培训结束时间 */
    @Column(name = "training_end_time")
    @Excel(name = "培训结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "培训结束时间不能为空")
    private Date trainingEndTime;

    /** 培训地点 */
    @Column(name = "training_site")
    @Excel(name = "培训地点")
    @NotBlank(message = "培训地点不能为空")
    private String trainingSite;

    /** 培训方式 */
    @Column(name = "training_type")
    @NotNull(message = "培训方式不能为空")
    private Integer trainingType;

    /** 培训内容 */
    @Column(name = "training_content")
    @Excel(name = "培训内容")
    @NotBlank(message = "培训内容不能为空")
    private String trainingContent;

    @Column(name = "training_score")
    @Excel(name = "培训成绩")
    private Float trainingScore;

    @Column(name = "training_time")
    @Excel(name = "培训时长")
    private Float trainingTime;


    @Transient
    private Float startLongTime;

    @Transient
    private Float endLongTime;

    @Transient
    private Float startScore;

    @Transient
    private Float endScore;

    /** 是否考试(0是 1否) */
    @Column(name = "exam_flag")
    private Integer examFlag;

    @Transient
    @Excel(name = "是否考试")
    private String examFlagName;

    @Transient
    @Excel(name = "是否通过")
    private String passFlagName;

    @Transient
    @Excel(name = "培训方式")
    private String trainingTypeName;

    /** 是否通过(0是 1否) */
    @Column(name = "pass_flag")
    private Integer passFlag;



}
