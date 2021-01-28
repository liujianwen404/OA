package com.ruoyi.centre.domain;

import java.util.Date;

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

/**
 * 中台配置系统对象 centre_sys_config
 * 
 * @author xt
 * @date 2020-10-22
 */
@Data
@Table(name = "centre_sys_config")
public class CentreSysConfig
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    /** 系统id */
    @Column(name = "sys_id",updatable = false)
    @Excel(name = "系统id")
    private String sysId;

    /** 系统名称 */
    @Column(name = "name")
    @Excel(name = "系统名称")
    private String name;

    /** 英文名 */
    @Column(name = "english_name")
    @Excel(name = "英文名")
    private String englishName;

    /** 产品线 */
    @Column(name = "product_line")
    @Excel(name = "产品线")
    private String productLine;

    /** 地区 */
    @Column(name = "area_id")
    @Excel(name = "地区")
    private Integer areaId;

    /** 1:开发环境；2测试环境；3：验证环境；4：生产环境 */
    @Column(name = "sys_type")
    @Excel(name = "1:开发环境；2测试环境；3：验证环境；4：生产环境")
    private Integer sysType;

    /** 秘钥 */
    @Column(name = "secret_key",updatable = false)
    @Excel(name = "秘钥")
    private String secretKey;

    /** 系统组 */
    /*@Column(name = "sys_group")
    @Excel(name = "系统组")
    private String sysGroup;*/

    /** 登录地址 */
    /*@Column(name = "login_url")
    @Excel(name = "登录地址")
    private String loginUrl;*/

    /** 回调url */
    @Column(name = "call_back_url")
    @Excel(name = "回调url")
    private String callBackUrl;

    /** 登录页图片地址 */
    /*@Column(name = "login_logo")
    @Excel(name = "登录页图片地址")
    private String loginLogo;*/

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
    @Excel(name = "创建人姓名", width = 30, dateFormat = "yyyy-MM-dd")
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
