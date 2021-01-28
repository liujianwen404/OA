package com.ruoyi.centre.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.centre.domain.CentreSysConfig;
import java.util.List;
/**
 * 中台配置系统 数据层
 *
 * @author xt
 * @date 2020-10-22
 */
public interface CentreSysConfigMapper extends MyBaseMapper<CentreSysConfig> {

    /**
     * 查询中台配置系统列表
     *
     * @param centreSysConfig 中台配置系统
     * @return 中台配置系统集合
     */
    public List<CentreSysConfig> selectCentreSysConfigList(CentreSysConfig centreSysConfig);

    /**
     * 删除中台配置系统
     *
     * @param id 中台配置系统ID
     * @return 结果
     */
    public int deleteCentreSysConfigById(Integer id);

    /**
     * 批量删除中台配置系统
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCentreSysConfigByIds(String[] ids);

}