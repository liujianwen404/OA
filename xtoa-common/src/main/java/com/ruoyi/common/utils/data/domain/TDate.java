package com.ruoyi.common.utils.data.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 日期对象 t_date
 * 
 * @author liujianwen
 * @date 2020-05-19
 */
@Table(name = "t_date")
public class TDate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 日期 */
    @Column(name = "day")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date day;

    /** 是否为节假日（0代表否 1代表是） */
    @Column(name = "flag")
    @Excel(name = "是否为节假日", readConverterExp = "0=代表否,1=代表是")
    private String flag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDay(Date day) 
    {
        this.day = day;
    }

    public Date getDay() 
    {
        return day;
    }
    public void setFlag(String flag) 
    {
        this.flag = flag;
    }

    public String getFlag() 
    {
        return flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("day", getDay())
            .append("flag", getFlag())
            .toString();
    }
}
