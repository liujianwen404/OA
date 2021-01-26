package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TCity;

/**
 * 城市列信息Service接口
 * 
 * @author chemgmingwen
 * @date 2020-05-08
 */
public interface ITCityService 
{
    /**
     * 查询城市列信息
     *
     * @return 城市列信息
     */
    public List<TCity> selectAll();

    /**
     * 查询城市列信息
     *
     * @param cityId 城市列信息ID
     * @return 城市列信息
     */
    public TCity selectTCityById(String cityId);

    /**
     * 查询城市列信息列表
     * 
     * @param tCity 城市列信息
     * @return 城市列信息集合
     */
    public List<TCity> selectTCityList(TCity tCity);

    /**
     * 新增城市列信息
     * 
     * @param tCity 城市列信息
     * @return 结果
     */
    public int insertTCity(TCity tCity);

    /**
     * 修改城市列信息
     * 
     * @param tCity 城市列信息
     * @return 结果
     */
    public int updateTCity(TCity tCity);

    /**
     * 批量删除城市列信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCityByIds(String ids);

    /**
     * 删除城市列信息信息
     * 
     * @param cityId 城市列信息ID
     * @return 结果
     */
    public int deleteTCityById(String cityId);
}
