package com.ruoyi.base.domain;

import java.util.Date;

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
 * 出差申请子对象 t_hr_business_trip_son
 * 
 * @author liujianwen
 * @date 2020-07-01
 */
@Data
@Table(name = "t_hr_business_trip_son")
public class HrBusinessTripSon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 父ID */
    @Column(name = "parent_id")
    @Excel(name = "父ID")
    private Long parentId;

    /** name */
    @Column(name = "name")
    @Excel(name = "name")
    private String name;

    /** type */
    @Column(name = "type")
    @Excel(name = "type")
    private String type;

    /** 开始时间 */
    @Column(name = "start_time")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @Column(name = "end_time")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 时长，单位小时 */
    @Column(name = "total_times")
    @Excel(name = "时长，单位小时")
    private Double totalTimes;

    /** 交通工具（1：飞机，2：火车，3：汽车，4：其他) */
    @Column(name = "vehicle")
    @Excel(name = "交通工具", readConverterExp = "交通工具（1：飞机，2：火车，3：汽车，4：其他)")
    private String vehicle;

    /** 单程往返（1：单程，2：往返) */
    @Column(name = "journey")
    @Excel(name = "单程往返", readConverterExp = "单程往返（1：单程，2：往返)")
    private String journey;

    /** 出发城市 */
    @Column(name = "depart_city")
    @Excel(name = "出发城市")
    private String departCity;

    /** 目的城市 */
    @Column(name = "destination_city")
    @Excel(name = "目的城市")
    private String destinationCity;

}
