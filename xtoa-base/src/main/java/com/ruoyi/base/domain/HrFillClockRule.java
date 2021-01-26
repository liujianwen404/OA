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
 * 补卡规则对象 t_hr_fill_clock_rule
 * 
 * @author xt
 * @date 2020-11-21
 */
@Table(name = "t_hr_fill_clock_rule")
@Data
public class HrFillClockRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 1月 */
    @Column(name = "january")
    @Excel(name = "1月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date january;

    /** 2月 */
    @Column(name = "february")
    @Excel(name = "2月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date february;

    /** 3月 */
    @Column(name = "march")
    @Excel(name = "3月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date march;

    /** 4月 */
    @Column(name = "april")
    @Excel(name = "4月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date april;

    /** 5月 */
    @Column(name = "may")
    @Excel(name = "5月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date may;

    /** 6月 */
    @Column(name = "june")
    @Excel(name = "6月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date june;

    /** 7月 */
    @Column(name = "july")
    @Excel(name = "7月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date july;

    /** 8月 */
    @Column(name = "august")
    @Excel(name = "8月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date august;

    /** 9月 */
    @Column(name = "september")
    @Excel(name = "9月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date september;

    /** 10月 */
    @Column(name = "october")
    @Excel(name = "10月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date october;

    /** 11月 */
    @Column(name = "november")
    @Excel(name = "11月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date november;

    /** 12月 */
    @Column(name = "december")
    @Excel(name = "12月", width = 30, dateFormat = "yyyy-MM-dd")
    private Date december;


}
