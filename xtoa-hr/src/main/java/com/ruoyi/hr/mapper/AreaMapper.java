package com.ruoyi.hr.mapper;

import com.ruoyi.base.domain.Area;
import com.ruoyi.common.mapper.MyBaseMapper;
import java.util.List;
/**
 * 地区 数据层
 *
 * @author xt
 * @date 2020-10-26
 */
public interface AreaMapper extends MyBaseMapper<Area> {

    /**
     * 查询地区列表
     *
     * @param area 地区
     * @return 地区集合
     */
    public List<Area> selectAreaList(Area area);

    /**
     * 删除地区
     *
     * @param id 地区ID
     * @return 结果
     */
    public int deleteAreaById(Long id);

    /**
     * 批量删除地区
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAreaByIds(String[] ids);

}