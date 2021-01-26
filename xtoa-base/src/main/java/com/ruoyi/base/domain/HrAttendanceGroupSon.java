package com.ruoyi.base.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 考勤组排班子对象 t_hr_attendance_group_son
 * 
 * @author liujianwen
 * @date 2020-10-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_hr_attendance_group_son")
public class HrAttendanceGroupSon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    @ExcelIgnore
    private Long id;

    @Column(name = "parent_id")
    @ExcelProperty("考勤组ID")
    private Long parentId;

    /** 员工ID */
    @Column(name = "emp_id")
    @ExcelProperty("员工ID")
    private Long empId;

    /** 员工姓名 */
    @Column(name = "emp_name")
    @ExcelProperty("员工姓名")
    private String empName;

    /** 排班日期 */
    @Column(name = "schedu_date")
    @ExcelProperty("排班日期")
    private String scheduDate;

    /** 1 */
    @Column(name = "class_id_1")
    @ExcelProperty("1")
    private String classId1;

    /** 2 */
    @Column(name = "class_id_2")
    @ExcelProperty("2")
    private String classId2;

    /** 3 */
    @Column(name = "class_id_3")
    @ExcelProperty("3")
    private String classId3;

    /** 4 */
    @Column(name = "class_id_4")
    @ExcelProperty("4")
    private String classId4;

    /** 5 */
    @Column(name = "class_id_5")
    @ExcelProperty("5")
    private String classId5;

    /** 6 */
    @Column(name = "class_id_6")
    @ExcelProperty("6")
    private String classId6;

    /** 7 */
    @Column(name = "class_id_7")
    @ExcelProperty("7")
    private String classId7;

    /** 8 */
    @Column(name = "class_id_8")
    @ExcelProperty("8")
    private String classId8;

    /** 9 */
    @Column(name = "class_id_9")
    @ExcelProperty("9")
    private String classId9;

    /** 10 */
    @Column(name = "class_id_10")
    @ExcelProperty("10")
    private String classId10;

    /** 11 */
    @Column(name = "class_id_11")
    @ExcelProperty("11")
    private String classId11;

    /** 12 */
    @Column(name = "class_id_12")
    @ExcelProperty("12")
    private String classId12;

    /** 13 */
    @Column(name = "class_id_13")
    @ExcelProperty("13")
    private String classId13;

    /** 14 */
    @Column(name = "class_id_14")
    @ExcelProperty("14")
    private String classId14;

    /** 15 */
    @Column(name = "class_id_15")
    @ExcelProperty("15")
    private String classId15;

    /** 16 */
    @Column(name = "class_id_16")
    @ExcelProperty("16")
    private String classId16;

    /** 17 */
    @Column(name = "class_id_17")
    @ExcelProperty("17")
    private String classId17;

    /** 18 */
    @Column(name = "class_id_18")
    @ExcelProperty("18")
    private String classId18;

    /** 19 */
    @Column(name = "class_id_19")
    @ExcelProperty("19")
    private String classId19;

    /** 20 */
    @Column(name = "class_id_20")
    @ExcelProperty("20")
    private String classId20;

    /** 21 */
    @Column(name = "class_id_21")
    @ExcelProperty("21")
    private String classId21;

    /** 22 */
    @Column(name = "class_id_22")
    @ExcelProperty("22")
    private String classId22;

    /** 23 */
    @Column(name = "class_id_23")
    @ExcelProperty("23")
    private String classId23;

    /** 24 */
    @Column(name = "class_id_24")
    @ExcelProperty("24")
    private String classId24;

    /** 25 */
    @Column(name = "class_id_25")
    @ExcelProperty("25")
    private String classId25;

    /** 26 */
    @Column(name = "class_id_26")
    @ExcelProperty("26")
    private String classId26;

    /** 27 */
    @Column(name = "class_id_27")
    @ExcelProperty("27")
    private String classId27;

    /** 28 */
    @Column(name = "class_id_28")
    @ExcelProperty("28")
    private String classId28;

    /** 29 */
    @Column(name = "class_id_29")
    @ExcelProperty("29")
    private String classId29;

    /** 30 */
    @Column(name = "class_id_30")
    @ExcelProperty("30")
    private String classId30;

    /** 31 */
    @Column(name = "class_id_31")
    @ExcelProperty("31")
    private String classId31;


}
