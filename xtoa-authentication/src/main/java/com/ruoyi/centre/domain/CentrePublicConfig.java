package com.ruoyi.centre.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 中台配置公共服务对象 centre_public_config
 * 
 * @author xt
 * @date 2020-10-23
 */
@Data
@Table(name = "centre_public_config")
public class CentrePublicConfig
{
    private static final long serialVersionUID = 1L;

    /** 服务id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer serverId;

    /** 系统名称 */
    @Column(name = "name")
    @Excel(name = "系统名称")
    private String name;

    /** 网络地址 */
    @Column(name = "web_url")
    @Excel(name = "网络地址")
    private String webUrl;

    /** logo图片地址 */
    @Column(name = "logo")
    @Excel(name = "logo图片地址")
    private String logo;

    /** 是否可用 */
    @Column(name = "enable")
    @Excel(name = "是否可用")
    private Boolean enable;

    /** 排序值 */
    @Column(name = "sort_value")
    @Excel(name = "排序值")
    private Integer sortValue;

    /** 创建人姓名 */
    @Column(name = "create_name")
    @Excel(name = "创建人姓名")
    private String createName;


    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", insertable = false, updatable = false)
    @ExcelIgnore
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time" ,insertable = false, updatable = false)
    @ExcelIgnore
    private Date updateTime;

}
