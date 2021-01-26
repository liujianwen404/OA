package com.ruoyi.base.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 个人考勤记录对象 t_hr_attendance_info
 * 
 * @author liujianwen
 * @date 2020-07-13
 */
@Data
@ToString
@Table(name = "t_hr_attendance_info")
public class HrAttendanceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 钉钉用户ID */
    @Column(name = "ding_user_id")
    private String dingUserId;

    /** 考勤组ID */
    @Column(name = "group_id")
    private Long groupId;

    /** 排班ID */
    @Column(name = "plan_id")
    private Long planId;

    /** 考勤班次id(没有的话表示该次打卡不在排班内) */
    @Column(name = "class_id")
    private Long classId;

    /** 员工ID */
    @Column(name = "emp_id")
    private Long empId;

    /** 员工姓名 */
    @Column(name = "emp_name")
    @Excel(name = "员工姓名")
    private String empName;

    /** 工作日 */
    @Column(name = "work_date")
    @Excel(name = "工作日")
    private String workDate;

    /** 基准时间(计算迟到和早退) */
    @Column(name = "base_check_time")
    private Date baseCheckTime;

    /** 排班打卡时间 */
    @Column(name = "plan_check_time")
    @Excel(name = "排班打卡时间", width = 30)
    private String planCheckTime;

    /** 实际打卡时间 */
    @Column(name = "user_check_time")
    @Excel(name = "实际打卡时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date userCheckTime;

    /** 考勤类型(OnDuty：上班,OffDuty：下班) */
    @Column(name = "check_type")
    @Excel(name = "考勤类型",readConverterExp = "OnDuty=上班,OffDuty=下班")
    private String checkType;

    /** 数据来源(ATM：考勤机;BEACON：IBeacon;DING_ATM：钉钉考勤机;USER：用户打卡;BOSS：老板改签;APPROVE：审批系统;SYSTEM：考勤系统;AUTO_CHECK：自动打卡) */
    @Column(name = "source_type")
    @Excel(name = "数据来源",readConverterExp = "ATM=考勤机,BEACON=IBeacon,DING_ATM=钉钉考勤机,USER=用户打卡,BOSS=老板改签,APPROVE=审批系统,SYSTEM=考勤系统,PHONE=自动打卡,SYSTEM=手机打卡")
    private String sourceType;

    /** 时间结果(Normal：正常;Early：早退;Late：迟到;SeriousLate：严重迟到；Absenteeism：旷工迟到；NotSigned：未打卡) */
    @Column(name = "time_result")
    @Excel(name = "时间结果",readConverterExp = "Normal=正常,Early=早退,Late=迟到,SeriousLate=严重迟到,SeriousEarly=严重早退,AbsenteeismLate=迟到旷工,AbsenteeismEarly=早退旷工,NotSigned=未打卡")
    private String timeResult;

    @Column(name = "result_minutes")
    @Excel(name = "迟到早退分钟数", width = 30)
    private String resultMinutes;

    /** 位置结果(Normal：范围内;Outside：范围外，外勤打卡时为这个值) */
    @Column(name = "location_result")
    @Excel(name = "位置结果",readConverterExp = "Normal=范围内,Outside=范围外")
    private String locationResult;

    /** 定位方法 */
    @Column(name = "location_method")
    private String locationMethod;

    /** 关联的审批表单id(当该字段非空时，表示打卡记录与请假、加班等审批有关) */
    @Column(name = "approve_id")
    private String approveId;

    /** 关联的审批实例id(当该字段非空时，表示打卡记录与请假、加班等审批有关) */
    @Column(name = "proc_inst_id")
    private String procInstId;

    /** 是否合法(当timeResult和locationResult都为Normal时，该值为Y；否则为N) */
    @Column(name = "is_legal")
    private String isLegal;

    /** 设备id */
    @Column(name = "device_id")
    private String deviceId;

    /** 用户打卡地址 */
    @Column(name = "user_address")
    @Excel(name = "用户打卡地址")
    private String userAddress;

    /** 用户打卡经度 */
    @Column(name = "user_longitude")
    private String userLongitude;

    /** 用户打卡纬度 */
    @Column(name = "user_latitude")
    private String userLatitude;

    /** 用户打卡定位精度 */
    @Column(name = "user_accuracy")
    private String userAccuracy;

    /** 基准地址 */
    @Column(name = "base_address")
    private String baseAddress;

    /** 基准经度 */
    @Column(name = "base_longitude")
    private String baseLongitude;

    /** 基准纬度 */
    @Column(name = "base_latitude")
    private String baseLatitude;

    /** 基准定位精度 */
    @Column(name = "base_accuracy")
    private String baseAccuracy;

    /** 打卡图片地址 */
    @Column(name = "img_url")
    private String imgUrl;

    /** 部门ID */
    @Column(name = "dept_id")
    private Long deptId;

    /** 部门名称 */
    @Column(name = "dept_name")
    private String deptName;

    /** 岗位ID */
    @Column(name = "post_id")
    private Long postId;

    /** 岗位名称 */
    @Column(name = "post_name")
    private String postName;

}
