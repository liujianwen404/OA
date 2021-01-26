package com.ruoyi.base.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.ruoyi.common.annotation.Easyexcel;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 请假业务对象 t_hr_leave
 *
 * @author Xianlu Tech
 * @date 2019-10-11
 */
@Data
@Table(name = "t_hr_leave")
@ColumnWidth(20)
public class HrLeave extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @ExcelIgnore
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 请假类型 */
//    @Easyexcel(name = "请假类型",readConverterExp = "1=调休,2=事假,3=病假,4=年假,5=丧假,6=婚假")
    @ExcelIgnore
    private String type;

    @Transient
    @ExcelProperty(index = 0,value = "请假类型")
    private String typeExcel;

    /** 标题 */
    @ExcelIgnore
    private String title;

    /** 原因 */
    @ExcelIgnore
    private String reason;

    /** 开始时间 */
    @DateTimeFormat("yyyy/MM/dd HH:mm:ss")
    @ExcelProperty(index = 1,value = "开始时间")
    private Date startTime;

    /** 结束时间 */
    @DateTimeFormat("yyyy/MM/dd HH:mm:ss")
    @ExcelProperty(index = 2,value = "结束时间")
    private Date endTime;

    /** 请假时长，单位秒 */
    @ExcelIgnore
    private Long totalTime;

    /** 请假时长，单位小时 */
    @ExcelProperty(index = 3,value = "请假时长")
    private Double totalHours;

    /** 流程实例ID */
    @ExcelIgnore
    private String instanceId;

    /** 申请人 */
    @ExcelIgnore
    private String applyUser;

    /** 申请时间 */
    @DateTimeFormat("yyyy/MM/dd HH:mm:ss")
    @ExcelProperty(index = 5,value = "申请时间")
    private Date applyTime;

    /** 发起人工号 */
    @ExcelIgnore
    private String applyUserNum;

    /** 发起人姓名 */
    @Excel(name = "发起人姓名")
    @ExcelProperty(index = 4,value = "发起人姓名")
    private String applyUserName;

    /** 实际开始时间 */
    @ExcelIgnore
    private Date realityStartTime;

    /** 实际结束时间 */
    @ExcelIgnore
    private Date realityEndTime;

    @Transient
    @ExcelProperty(index = 6,value = "当前任务名称")
    private String taskNameExcel;


    @ExcelProperty(index = 7,value = "当前处理人")
    private String todoUserNameExcel;

    /** 审核状态（0：待审核，1：审核中，2：通过，3：拒绝，4：撤销) */
    @Column(name = "audit_status")
//    @Excel(name = "审核状态",readConverterExp = "0=待审核,1=审核中,2=通过,3=拒绝,4=撤销")
    @ExcelIgnore
    private String auditStatus;

    @ExcelProperty(index = 8,value = "审核状态")
    private String auditStatusExcel;

    /** 部门id */
    @Column(name = "dept_id")
    @ExcelIgnore
    private Long deptId;

    /** 岗位id */
    @Column(name = "post_id")
    @ExcelIgnore
    private Long postId;

    @Column(name = "img_urls")
    @ExcelIgnore
    private String imgUrls;


    /** 流程所属员工id */
    @Column(name = "emp_id")
    @ExcelIgnore
    private Long empId;


    /** 流程所属员工id */
    @Column(name = "holiday_ids")
    @ExcelIgnore
    private String holidayIds;

    @Transient
    @ExcelIgnore
    private List<String> imgList;
    
}
