package com.ruoyi.centre.service;

import java.util.List;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.centre.domain.CentreSysConfig;

/**
 * 中台配置系统Service接口
 * 
 * @author xt
 * @date 2020-10-22
 */
public interface ICentreSysConfigService 
{
    /**
     * 查询中台配置系统
     * 
     * @param id 中台配置系统ID
     * @return 中台配置系统
     */
    public CentreSysConfig selectCentreSysConfigById(Integer id);

    CentreSysConfig selectOneByExample(Example example);

    /**
     * 查询中台配置系统列表
     * 
     * @param centreSysConfig 中台配置系统
     * @return 中台配置系统集合
     */
    public List<CentreSysConfig> selectCentreSysConfigList(CentreSysConfig centreSysConfig);

    /**
     * 新增中台配置系统
     * 
     * @param centreSysConfig 中台配置系统
     * @return 结果
     */
    public int insertCentreSysConfig(CentreSysConfig centreSysConfig);

    /**
     * 修改中台配置系统
     * 
     * @param centreSysConfig 中台配置系统
     * @return 结果
     */
    public int updateCentreSysConfig(CentreSysConfig centreSysConfig);

    /**
     * 批量删除中台配置系统
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCentreSysConfigByIds(String ids);

    /**
     * 删除中台配置系统信息
     * 
     * @param sysId 中台配置系统ID
     * @return 结果
     */
    public int deleteCentreSysConfigById(Integer sysId);

    CentreSysConfig selectSingleOneByExample(Example example);

    List<CentreSysConfig> selectByExample(Example example);

}
