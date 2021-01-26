package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 城市列信息对象 t_city
 * 
 * @author chemgmingwen
 * @date 2020-05-08
 */
public class TCity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 城市id */
    private String cityId;

    /** 城市名称 */
    @Excel(name = "城市名称")
    private String cityName;

    /** 省份id */
    @Excel(name = "省份id")
    private String provinceId;

    /** 省份名称 */
    @Excel(name = "省份名称")
    private String provinceName;

    public void setCityId(String cityId) 
    {
        this.cityId = cityId;
    }

    public String getCityId() 
    {
        return cityId;
    }
    public void setCityName(String cityName) 
    {
        this.cityName = cityName;
    }

    public String getCityName() 
    {
        return cityName;
    }
    public void setProvinceId(String provinceId) 
    {
        this.provinceId = provinceId;
    }

    public String getProvinceId() 
    {
        return provinceId;
    }
    public void setProvinceName(String provinceName) 
    {
        this.provinceName = provinceName;
    }

    public String getProvinceName() 
    {
        return provinceName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("cityId", getCityId())
            .append("cityName", getCityName())
            .append("provinceId", getProvinceId())
            .append("provinceName", getProvinceName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
