package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.TCity;
import com.ruoyi.system.mapper.TCityMapper;
import com.ruoyi.system.service.ITCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 城市列信息Service业务层处理
 * 
 * @author chemgmingwen
 * @date 2020-05-08
 */
@Service
public class TCityServiceImpl implements ITCityService 
{
    @Autowired
    private TCityMapper tCityMapper;

    @Override
    public List<TCity> selectAll() {
        return tCityMapper.selectAll();
    }

    /**
     * 查询城市列信息
     * 
     * @param cityId 城市列信息ID
     * @return 城市列信息
     */
    @Override
    public TCity selectTCityById(String cityId)
    {
        return tCityMapper.selectTCityById(cityId);
    }

    /**
     * 查询城市列信息列表
     * 
     * @param tCity 城市列信息
     * @return 城市列信息
     */
    @Override
    public List<TCity> selectTCityList(TCity tCity)
    {
        return tCityMapper.selectTCityList(tCity);
    }

    /**
     * 新增城市列信息
     * 
     * @param tCity 城市列信息
     * @return 结果
     */
    @Override
    public int insertTCity(TCity tCity)
    {
        tCity.setCreateTime(DateUtils.getNowDate());
        return tCityMapper.insertTCity(tCity);
    }

    /**
     * 修改城市列信息
     * 
     * @param tCity 城市列信息
     * @return 结果
     */
    @Override
    public int updateTCity(TCity tCity)
    {
        tCity.setUpdateTime(DateUtils.getNowDate());
        return tCityMapper.updateTCity(tCity);
    }

    /**
     * 删除城市列信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTCityByIds(String ids)
    {
        return tCityMapper.deleteTCityByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除城市列信息信息
     * 
     * @param cityId 城市列信息ID
     * @return 结果
     */
    @Override
    public int deleteTCityById(String cityId)
    {
        return tCityMapper.deleteTCityById(cityId);
    }
}
