package com.ruoyi.centre.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.centre.domain.CentrePublicConfig;
import java.util.List;
/**
 * 中台配置公共服务 数据层
 *
 * @author xt
 * @date 2020-10-23
 */
public interface CentrePublicConfigMapper extends MyBaseMapper<CentrePublicConfig> {

    /**
     * 查询中台配置公共服务列表
     *
     * @param centrePublicConfig 中台配置公共服务
     * @return 中台配置公共服务集合
     */
    public List<CentrePublicConfig> selectCentrePublicConfigList(CentrePublicConfig centrePublicConfig);

    /**
     * 删除中台配置公共服务
     *
     * @param serverId 中台配置公共服务ID
     * @return 结果
     */
    public int deleteCentrePublicConfigById(Integer serverId);

    /**
     * 批量删除中台配置公共服务
     *
     * @param serverIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCentrePublicConfigByIds(String[] serverIds);

}