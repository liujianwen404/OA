package com.ruoyi.base.domain;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考勤班次对象 t_hr_attendance_class
 * 
 * @author liujianwen
 * @date 2020-07-27
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_hr_attendance_class")
public class HrAttendanceClass extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 班次名称 */
    @Column(name = "name")
    @Excel(name = "班次名称")
    private String name;

    /** 上班时间 */
    @Column(name = "work_time")
    @Excel(name = "上班时间")
    private String workTime;

    /** 下班时间 */
    @Column(name = "closing_time")
    @Excel(name = "下班时间")
    private String closingTime;

    /** 休息开始时间 */
    @Column(name = "rest_start_time")
    @Excel(name = "休息开始时间")
    private String restStartTime;

    /** 休息结束时间 */
    @Column(name = "rest_end_time")
    @Excel(name = "休息结束时间")
    private String restEndTime;

    /** 工作时长 */
    @Column(name = "hours")
    @Excel(name = "工作时长")
    private String hours;

    /** 弹性标志（0代表不允许弹性 1代表允许弹性） */
    @Column(name = "elasticity_flag")
    @Excel(name = "是否弹性",readConverterExp = "0=否,1=是")
    private String elasticityFlag;

}
