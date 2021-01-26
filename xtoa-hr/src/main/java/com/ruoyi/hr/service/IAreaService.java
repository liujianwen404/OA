package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.common.core.domain.Ztree;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.base.domain.Area;

/**
 * 地区Service接口
 * 
 * @author xt
 * @date 2020-10-26
 */
public interface IAreaService 
{
    /**
     * 查询地区
     * 
     * @param id 地区ID
     * @return 地区
     */
    public Area selectAreaById(Long id);

    /**
     * 查询地区列表
     * 
     * @param area 地区
     * @return 地区集合
     */
    public List<Area> selectAreaList(Area area);

    public List<Area> selectAreaAll();

    /**
     * 新增地区
     * 
     * @param area 地区
     * @return 结果
     */
    public int insertArea(Area area);

    /**
     * 修改地区
     * 
     * @param area 地区
     * @return 结果
     */
    public int updateArea(Area area);

    /**
     * 批量删除地区
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAreaByIds(String ids);

    /**
     * 删除地区信息
     * 
     * @param id 地区ID
     * @return 结果
     */
    public int deleteAreaById(Long id);

    Area selectSingleOneByExample(Example example);

    List<Area> selectByExample(Example example);

    List<Ztree> selectAreaListTree(Area area);

    List<Ztree> selectAreaListTreeCheckBox(Area area, Long roleId);
}
