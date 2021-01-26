package com.ruoyi.base.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 地区对象 area
 * 
 * @author xt
 * @date 2020-10-26
 */
@Table(name = "area")
public class Area
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 创建日期 */
    @Column(name = "createdDate")
    @Excel(name = "创建日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createddate;

    /** 最后修改日期 */
    @Column(name = "lastModifiedDate")
    @Excel(name = "最后修改日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastmodifieddate;

    /** 版本 */
    @Column(name = "version")
    @Excel(name = "版本")
    private Long version;

    /** 排序 */
    @Column(name = "orders")
    @Excel(name = "排序")
    private Long orders;

    /** 全称 */
    @Column(name = "fullName")
    @Excel(name = "全称")
    private String fullname;

    /** 层级 */
    @Column(name = "grade")
    @Excel(name = "层级")
    private Long grade;

    /** 名称 */
    @Column(name = "name")
    @Excel(name = "名称")
    private String name;

    /** 树路径 */
    @Column(name = "treePath")
    @Excel(name = "树路径")
    private String treepath;

    /** 上级地区 */
    @Column(name = "parent_id")
    @Excel(name = "上级地区")
    private Long parentId;

    /** 别名 */
    @Column(name = "alias")
    @Excel(name = "别名")
    private String alias;

    /** 是否开放 */
    @Column(name = "is_open")
    @Excel(name = "是否开放")
    private Long isOpen;

    /** 服务域名 */
    @Column(name = "server_url")
    @Excel(name = "服务域名")
    private String serverUrl;

    /** 是否开放服务 */
    @Column(name = "is_open_server")
    @Excel(name = "是否开放服务")
    private Long isOpenServer;

    /** 网站地址 */
    @Column(name = "h5_url")
    @Excel(name = "网站地址")
    private String h5Url;

    /** 推送仓库ID */
    @Column(name = "push_store_id")
    @Excel(name = "推送仓库ID")
    private String pushStoreId;

    /** 推送仓库名称 */
    @Column(name = "push_store_name")
    @Excel(name = "推送仓库名称")
    private String pushStoreName;

    /** 进销存地址 */
    @Column(name = "jxc_url")
    @Excel(name = "进销存地址")
    private String jxcUrl;

    /** 管理端地址 */
    @Column(name = "admin_url")
    @Excel(name = "管理端地址")
    private String adminUrl;

    /** 仓库纬度 */
    @Column(name = "storage_lat")
    @Excel(name = "仓库纬度")
    private Double storageLat;

    /** 仓库经度 */
    @Column(name = "storage_lng")
    @Excel(name = "仓库经度")
    private Double storageLng;

    /** 是否开放下单 */
    @Column(name = "is_open_order")
    @Excel(name = "是否开放下单")
    private Long isOpenOrder;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCreateddate(Date createddate) 
    {
        this.createddate = createddate;
    }

    public Date getCreateddate() 
    {
        return createddate;
    }
    public void setLastmodifieddate(Date lastmodifieddate) 
    {
        this.lastmodifieddate = lastmodifieddate;
    }

    public Date getLastmodifieddate() 
    {
        return lastmodifieddate;
    }
    public void setVersion(Long version) 
    {
        this.version = version;
    }

    public Long getVersion() 
    {
        return version;
    }
    public void setOrders(Long orders) 
    {
        this.orders = orders;
    }

    public Long getOrders() 
    {
        return orders;
    }
    public void setFullname(String fullname) 
    {
        this.fullname = fullname;
    }

    public String getFullname() 
    {
        return fullname;
    }
    public void setGrade(Long grade) 
    {
        this.grade = grade;
    }

    public Long getGrade() 
    {
        return grade;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setTreepath(String treepath) 
    {
        this.treepath = treepath;
    }

    public String getTreepath() 
    {
        return treepath;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setAlias(String alias) 
    {
        this.alias = alias;
    }

    public String getAlias() 
    {
        return alias;
    }
    public void setIsOpen(Long isOpen) 
    {
        this.isOpen = isOpen;
    }

    public Long getIsOpen() 
    {
        return isOpen;
    }
    public void setServerUrl(String serverUrl) 
    {
        this.serverUrl = serverUrl;
    }

    public String getServerUrl() 
    {
        return serverUrl;
    }
    public void setIsOpenServer(Long isOpenServer) 
    {
        this.isOpenServer = isOpenServer;
    }

    public Long getIsOpenServer() 
    {
        return isOpenServer;
    }
    public void setH5Url(String h5Url) 
    {
        this.h5Url = h5Url;
    }

    public String getH5Url() 
    {
        return h5Url;
    }
    public void setPushStoreId(String pushStoreId) 
    {
        this.pushStoreId = pushStoreId;
    }

    public String getPushStoreId() 
    {
        return pushStoreId;
    }
    public void setPushStoreName(String pushStoreName) 
    {
        this.pushStoreName = pushStoreName;
    }

    public String getPushStoreName() 
    {
        return pushStoreName;
    }
    public void setJxcUrl(String jxcUrl) 
    {
        this.jxcUrl = jxcUrl;
    }

    public String getJxcUrl() 
    {
        return jxcUrl;
    }
    public void setAdminUrl(String adminUrl) 
    {
        this.adminUrl = adminUrl;
    }

    public String getAdminUrl() 
    {
        return adminUrl;
    }
    public void setStorageLat(Double storageLat) 
    {
        this.storageLat = storageLat;
    }

    public Double getStorageLat() 
    {
        return storageLat;
    }
    public void setStorageLng(Double storageLng) 
    {
        this.storageLng = storageLng;
    }

    public Double getStorageLng() 
    {
        return storageLng;
    }
    public void setIsOpenOrder(Long isOpenOrder) 
    {
        this.isOpenOrder = isOpenOrder;
    }

    public Long getIsOpenOrder() 
    {
        return isOpenOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("createddate", getCreateddate())
            .append("lastmodifieddate", getLastmodifieddate())
            .append("version", getVersion())
            .append("orders", getOrders())
            .append("fullname", getFullname())
            .append("grade", getGrade())
            .append("name", getName())
            .append("treepath", getTreepath())
            .append("parentId", getParentId())
            .append("alias", getAlias())
            .append("isOpen", getIsOpen())
            .append("serverUrl", getServerUrl())
            .append("isOpenServer", getIsOpenServer())
            .append("h5Url", getH5Url())
            .append("pushStoreId", getPushStoreId())
            .append("pushStoreName", getPushStoreName())
            .append("jxcUrl", getJxcUrl())
            .append("adminUrl", getAdminUrl())
            .append("storageLat", getStorageLat())
            .append("storageLng", getStorageLng())
            .append("isOpenOrder", getIsOpenOrder())
            .toString();
    }
}
