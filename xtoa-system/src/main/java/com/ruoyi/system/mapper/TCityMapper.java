package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TCity;
import org.apache.ibatis.annotations.Param;

/**
 * 城市列信息Mapper接口
 * 
 * @author chemgmingwen
 * @date 2020-05-08
 */
public interface TCityMapper 
{
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
     * 删除城市列信息
     * 
     * @param cityId 城市列信息ID
     * @return 结果
     */
    public int deleteTCityById(String cityId);

    /**
     * 批量删除城市列信息
     * 
     * @param cityIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCityByIds(String[] cityIds);

    List<TCity> selectAll();

    List<String> getCityNmae(@Param("s") String s);
}
