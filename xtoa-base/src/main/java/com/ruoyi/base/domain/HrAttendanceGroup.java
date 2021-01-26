package com.ruoyi.base.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考勤组对象 t_hr_attendance_group
 * 
 * @author liujianwen
 * @date 2020-07-29
 */
@Data
@ToString
@Table(name = "t_hr_attendance_group")
public class HrAttendanceGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 考勤组类型 */
    @Column(name = "type")
    @Excel(name = "考勤组类型",readConverterExp = "0=固定班制,1=排班制")
    private String type;

    /** 考勤组名称 */
    @Column(name = "name")
    @Excel(name = "考勤组名称")
    private String name;

    /** 参与考勤人员 */
    @Column(name = "emp_id")
    @Excel(name = "参与考勤人员ID")
    private String empId;

    /** 参与考勤人员 */
    @Column(name = "emp_name")
    @Excel(name = "参与考勤人员")
    private String empName;

    /** 周一 */
    @Column(name = "default_class")
    @Excel(name = "默认班次")
    private String defaultClass;

    /** 周一 */
    @Column(name = "monday")
    @Excel(name = "周一")
    private String monday;

    /** 周一班次ID */
    @Column(name = "mon_class_id")
    @Excel(name = "周一班次ID")
    private String monClassId;

    /** 周一班次名称 */
    @Column(name = "mon_class_name")
    @Excel(name = "周一班次名称")
    private String monClassName;

    /** 周二 */
    @Column(name = "tuesday")
    @Excel(name = "周二")
    private String tuesday;

    /** 周二班次ID */
    @Column(name = "tue_class_id")
    @Excel(name = "周二班次ID")
    private String tueClassId;

    /** 周二班次名称 */
    @Column(name = "tue_class_name")
    @Excel(name = "周二班次名称")
    private String tueClassName;

    /** 周三 */
    @Column(name = "wednesday")
    @Excel(name = "周三")
    private String wednesday;

    /** 周三班次ID */
    @Column(name = "wed_class_id")
    @Excel(name = "周三班次ID")
    private String wedClassId;

    /** 周三班次名称 */
    @Column(name = "wed_class_name")
    @Excel(name = "周三班次名称")
    private String wedClassName;

    /** 周四 */
    @Column(name = "thursday")
    @Excel(name = "周四")
    private String thursday;

    /** 周四班次ID */
    @Column(name = "thu_class_id")
    @Excel(name = "周四班次ID")
    private String thuClassId;

    /** 周四班次名称 */
    @Column(name = "thu_class_name")
    @Excel(name = "周四班次名称")
    private String thuClassName;

    /** 周五 */
    @Column(name = "friday")
    @Excel(name = "周五")
    private String friday;

    /** 周五班次ID */
    @Column(name = "fri_class_id")
    @Excel(name = "周五班次ID")
    private String friClassId;

    /** 周五班次名称 */
    @Column(name = "fri_class_name")
    @Excel(name = "周五班次名称")
    private String friClassName;

    /** 周六 */
    @Column(name = "saturday")
    @Excel(name = "周六")
    private String saturday;

    /** 周六班次ID */
    @Column(name = "sat_class_id")
    @Excel(name = "周六班次ID")
    private String satClassId;

    /** 周六班次名称 */
    @Column(name = "sat_class_name")
    @Excel(name = "周六班次名称")
    private String satClassName;

    /** 周日 */
    @Column(name = "sunday")
    @Excel(name = "周日")
    private String sunday;

    /** 周日班次ID */
    @Column(name = "sun_class_id")
    @Excel(name = "周日班次ID")
    private String sunClassId;

    /** 周日班次名称 */
    @Column(name = "sun_class_name")
    @Excel(name = "周日班次名称")
    private String sunClassName;

    /** 必须打卡的日期 */
    @Column(name = "must_date")
    @Excel(name = "必须打卡的日期")
    private String mustDate;

    /** 必须打卡的日期班次 */
    @Column(name = "must_date_class")
    @Excel(name = "必须打卡的日期班次")
    private String mustDateClass;

    /** 不用打卡的日期 */
    @Column(name = "need_not_date")
    @Excel(name = "不用打卡的日期")
    private String needNotDate;

    /** 排班班次ID */
    @Column(name = "schedu_class_id")
    private String scheduClassId;

    /** 排班班次名称 */
    @Column(name = "schedu_class_name")
    @Excel(name = "排班班次名称")
    private String scheduClassName;

    /** 打卡地址 */
    @Column(name = "address")
    @Excel(name = "打卡地址")
    private String address;

    /** 打卡经度 */
    @Column(name = "longitude")
    @Excel(name = "打卡经度")
    private String longitude;

    /** 打卡纬度 */
    @Column(name = "latitude")
    @Excel(name = "打卡纬度")
    private String latitude;

    /** 打卡有效范围 */
    @Column(name = "scope")
    @Excel(name = "打卡有效范围")
    private String scope;

}
