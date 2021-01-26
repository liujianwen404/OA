package com.ruoyi.base.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 员工假期对象 t_holiday
 * 
 * @author liujianwen
 * @date 2020-06-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "t_holiday")
public class Holiday extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    @KeySql(useGeneratedKeys = true)
    @ExcelIgnore
    private Long id;

    /** 员工id */
    @Column(name = "emp_id")
    @ExcelIgnore
    private Long empId;

    /** 员工姓名 */
    @Column(name = "emp_name")
    @ColumnWidth(15)
    @ExcelProperty(index = 0,value = "员工姓名")
    private String empName;

    /** 用户id */
    @Column(name = "user_id")
    @ExcelIgnore
    private Long userId;

    @Transient
    @ExcelIgnore
    private Long deptId;
    @Transient
    @ExcelIgnore
    private Long postId;

    /** 部门 */
    @Transient
    @ColumnWidth(25)
    @ExcelProperty(index = 1,value = "所属部门")
    private String deptName;

    /** 岗位 */
    @Transient
    @ColumnWidth(15)
    @ExcelProperty(index = 2,value = "岗位")
    private String postName;

    /** 假期类型（0年假 1调休 2未知） */
    @Column(name = "type")
    @ExcelIgnore
    private String type;

    @Transient
    @ColumnWidth(15)
    @ExcelProperty(index = 3,value = "假期类型")
    private String typeStr;

    /** 假期时长，单位小时 */
    @Column(name = "hours")
    @ColumnWidth(15)
    @ExcelProperty(index = 4,value = "假期时长/小时")
    private Double hours;

    /** 已用假期时长，单位小时 */
    @Column(name = "use_hours")
    @ColumnWidth(15)
    @ExcelProperty(index = 5,value = "已用假期时长/小时")
    private Double useHours;

    /** 开始日期 */
    @Column(name = "start_date")@DateTimeFormat("yyyy/MM/dd")
    @ColumnWidth(15)
    @ExcelProperty(index = 6,value = "开始日期")
    private Date startDate;

    /** 结束日期 */
    @Column(name = "end_date")@DateTimeFormat("yyyy/MM/dd")
    @ColumnWidth(15)
    @ExcelProperty(index = 7,value = "结束日期")
    private Date endDate;

    @Transient
    @ColumnWidth(15)
    @ExcelProperty(index = 8,value = "可用时长")
    private Double availableTime;

    /** 来源 */
    @Column(name = "source")
    @ExcelIgnore
    private String source;

    @Column(name = "is_public")
    @ExcelIgnore
    private Integer isPublic;

    @Column(name = "overtime_id")
    @ExcelIgnore
    private Long overtimeId;

    @Column(name = "class_info_id")
    @ExcelIgnore
    private Long classInfoId;

    @Column(name = "attendance_date")
    @ExcelIgnore
    private Date attendanceDate;

}
