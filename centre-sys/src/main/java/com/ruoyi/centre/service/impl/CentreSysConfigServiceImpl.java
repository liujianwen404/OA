package com.ruoyi.centre.service.impl;

import com.ruoyi.centre.domain.CentreSysConfig;
import com.ruoyi.centre.mapper.CentreSysConfigMapper;
import com.ruoyi.centre.service.ICentreSysConfigService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.framework.util.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

/**
 * 中台配置系统Service业务层处理
 * 
 * @author xt
 * @date 2020-10-22
 */
@Service
public class CentreSysConfigServiceImpl implements ICentreSysConfigService 
{

    private static final Logger logger = LoggerFactory.getLogger(CentreSysConfigServiceImpl.class);

    @Autowired
    private CentreSysConfigMapper centreSysConfigMapper;

    /**
     * 查询中台配置系统
     * 
     * @param id 中台配置系统ID
     * @return 中台配置系统
     */
    @Override
    public CentreSysConfig selectCentreSysConfigById(Integer id)
    {
        return centreSysConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public CentreSysConfig selectOneByExample(Example example)
    {
        return centreSysConfigMapper.selectOneByExample(example);
    }

    /**
     * 查询中台配置系统列表
     * 
     * @param centreSysConfig 中台配置系统
     * @return 中台配置系统
     */
    @Override
    public List<CentreSysConfig> selectCentreSysConfigList(CentreSysConfig centreSysConfig)
    {
        return centreSysConfigMapper.selectCentreSysConfigList(centreSysConfig);
    }

    /**
     * 新增中台配置系统
     * 
     * @param centreSysConfig 中台配置系统
     * @return 结果
     */
    @Override
    public int insertCentreSysConfig(CentreSysConfig centreSysConfig)
    {
        centreSysConfig.setCreateName(ShiroUtils.getUserName());
        centreSysConfig.setSysId(UUID.randomUUID().toString());
        centreSysConfig.setSecretKey(UUID.randomUUID().toString());
        return centreSysConfigMapper.insertSelective(centreSysConfig);
    }

    /**
     * 修改中台配置系统
     * 
     * @param centreSysConfig 中台配置系统
     * @return 结果
     */
    @Override
    public int updateCentreSysConfig(CentreSysConfig centreSysConfig)
    {
        return centreSysConfigMapper.updateByPrimaryKeySelective(centreSysConfig);
    }

    /**
     * 删除中台配置系统对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCentreSysConfigByIds(String ids)
    {
        return centreSysConfigMapper.deleteCentreSysConfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除中台配置系统信息
     * 
     * @param sysId 中台配置系统ID
     * @return 结果
     */
    @Override
    public int deleteCentreSysConfigById(Integer sysId)
    {
        return centreSysConfigMapper.deleteCentreSysConfigById(sysId);
    }



    @Override
    public CentreSysConfig selectSingleOneByExample(Example example){
        return centreSysConfigMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<CentreSysConfig> selectByExample(Example example){
        return centreSysConfigMapper.selectByExample(example);
    }

}
