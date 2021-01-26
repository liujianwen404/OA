package com.ruoyi.base.domain.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.ToString;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 *  t_training_records
 * 
 * @author chenfei
 * @date 2021-01-04
 */
@Data
@ToString
public class TTrainingRecordsTemplate implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "员工工号")
    private String empNum;

    /** 培训开始时间 */
    @Excel(name = "培训开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trainingStartTime;

    /** 培训结束时间 */
    @Column(name = "training_end_time")
    @Excel(name = "培训结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trainingEndTime;

    /** 培训地点 */
    @Column(name = "training_site")
    @Excel(name = "培训地点")
    private String trainingSite;

    /** 培训内容 */
    @Excel(name = "培训内容")
    private String trainingContent;

    @Excel(name = "培训成绩")
    private Float trainingScore;

    @Excel(name = "培训时长")
    private Float trainingTime;

    @Excel(name = "是否考试")
    private String examFlagName;

    @Excel(name = "是否通过")
    private String passFlagName;

    @Excel(name = "培训方式")
    private String trainingTypeName;




}
