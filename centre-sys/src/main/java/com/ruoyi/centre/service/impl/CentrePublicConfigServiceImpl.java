package com.ruoyi.centre.service.impl;

import com.ruoyi.centre.domain.CentrePublicConfig;
import com.ruoyi.centre.mapper.CentrePublicConfigMapper;
import com.ruoyi.centre.service.ICentrePublicConfigService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.framework.util.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 中台配置公共服务Service业务层处理
 * 
 * @author xt
 * @date 2020-10-23
 */
@Service
public class CentrePublicConfigServiceImpl implements ICentrePublicConfigService 
{

    private static final Logger logger = LoggerFactory.getLogger(CentrePublicConfigServiceImpl.class);

    @Autowired
    private CentrePublicConfigMapper centrePublicConfigMapper;

    /**
     * 查询中台配置公共服务
     * 
     * @param serverId 中台配置公共服务ID
     * @return 中台配置公共服务
     */
    @Override
    public CentrePublicConfig selectCentrePublicConfigById(Integer serverId)
    {
        return centrePublicConfigMapper.selectByPrimaryKey(serverId);
    }

    /**
     * 查询中台配置公共服务列表
     * 
     * @param centrePublicConfig 中台配置公共服务
     * @return 中台配置公共服务
     */
    @Override
    public List<CentrePublicConfig> selectCentrePublicConfigList(CentrePublicConfig centrePublicConfig)
    {
        return centrePublicConfigMapper.selectCentrePublicConfigList(centrePublicConfig);
    }

    /**
     * 新增中台配置公共服务
     * 
     * @param centrePublicConfig 中台配置公共服务
     * @return 结果
     */
    @Override
    public int insertCentrePublicConfig(CentrePublicConfig centrePublicConfig)
    {
        centrePublicConfig.setCreateName(ShiroUtils.getUserName());
        return centrePublicConfigMapper.insertSelective(centrePublicConfig);
    }

    /**
     * 修改中台配置公共服务
     * 
     * @param centrePublicConfig 中台配置公共服务
     * @return 结果
     */
    @Override
    public int updateCentrePublicConfig(CentrePublicConfig centrePublicConfig)
    {
        return centrePublicConfigMapper.updateByPrimaryKeySelective(centrePublicConfig);
    }

    /**
     * 删除中台配置公共服务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCentrePublicConfigByIds(String ids)
    {
        return centrePublicConfigMapper.deleteCentrePublicConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除中台配置公共服务信息
     * 
     * @param serverId 中台配置公共服务ID
     * @return 结果
     */
    @Override
    public int deleteCentrePublicConfigById(Integer serverId)
    {
        return centrePublicConfigMapper.deleteCentrePublicConfigById(serverId);
    }



    @Override
    public CentrePublicConfig selectSingleOneByExample(Example example){
        return centrePublicConfigMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<CentrePublicConfig> selectByExample(Example example){
        return centrePublicConfigMapper.selectByExample(example);
    }

}
