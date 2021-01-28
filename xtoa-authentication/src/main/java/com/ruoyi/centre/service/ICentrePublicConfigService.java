package com.ruoyi.centre.service;

import java.util.List;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.centre.domain.CentrePublicConfig;

/**
 * 中台配置公共服务Service接口
 * 
 * @author xt
 * @date 2020-10-23
 */
public interface ICentrePublicConfigService 
{
    /**
     * 查询中台配置公共服务
     * 
     * @param serverId 中台配置公共服务ID
     * @return 中台配置公共服务
     */
    public CentrePublicConfig selectCentrePublicConfigById(Integer serverId);

    /**
     * 查询中台配置公共服务列表
     * 
     * @param centrePublicConfig 中台配置公共服务
     * @return 中台配置公共服务集合
     */
    public List<CentrePublicConfig> selectCentrePublicConfigList(CentrePublicConfig centrePublicConfig);

    /**
     * 新增中台配置公共服务
     * 
     * @param centrePublicConfig 中台配置公共服务
     * @return 结果
     */
    public int insertCentrePublicConfig(CentrePublicConfig centrePublicConfig);

    /**
     * 修改中台配置公共服务
     * 
     * @param centrePublicConfig 中台配置公共服务
     * @return 结果
     */
    public int updateCentrePublicConfig(CentrePublicConfig centrePublicConfig);

    /**
     * 批量删除中台配置公共服务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCentrePublicConfigByIds(String ids);

    /**
     * 删除中台配置公共服务信息
     * 
     * @param serverId 中台配置公共服务ID
     * @return 结果
     */
    public int deleteCentrePublicConfigById(Integer serverId);

    CentrePublicConfig selectSingleOneByExample(Example example);

    List<CentrePublicConfig> selectByExample(Example example);

}
