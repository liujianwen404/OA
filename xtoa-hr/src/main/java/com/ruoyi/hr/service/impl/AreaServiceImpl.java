package com.ruoyi.hr.service.impl;

import com.ruoyi.base.domain.Area;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.hr.mapper.AreaMapper;
import com.ruoyi.hr.service.IAreaService;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.service.ISysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 地区Service业务层处理
 * 
 * @author xt
 * @date 2020-10-26
 */
@Service
public class AreaServiceImpl implements IAreaService 
{

    private static final Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private ISysRoleService roleService;

    /**
     * 查询地区
     * 
     * @param id 地区ID
     * @return 地区
     */
    @Override
    public Area selectAreaById(Long id)
    {
        return areaMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询地区列表
     * 
     * @param area 地区
     * @return 地区
     */
    @Override
    public List<Area> selectAreaList(Area area)
    {
        return areaMapper.selectAreaList(area);
    }

    @Override
    public List<Area> selectAreaAll()
    {
        return areaMapper.selectAreaList(new Area());
    }

    /**
     * 新增地区
     * 
     * @param area 地区
     * @return 结果
     */
    @Override
    public int insertArea(Area area)
    {
        return areaMapper.insertSelective(area);
    }

    /**
     * 修改地区
     * 
     * @param area 地区
     * @return 结果
     */
    @Override
    public int updateArea(Area area)
    {
        return areaMapper.updateByPrimaryKeySelective(area);
    }

    /**
     * 删除地区对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAreaByIds(String ids)
    {
        return areaMapper.deleteAreaByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除地区信息
     * 
     * @param id 地区ID
     * @return 结果
     */
    @Override
    public int deleteAreaById(Long id)
    {
        return areaMapper.deleteAreaById(id);
    }



    @Override
    public Area selectSingleOneByExample(Example example){
        return areaMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<Area> selectByExample(Example example){
        return areaMapper.selectByExample(example);
    }

    @Override
    public List<Ztree> selectAreaListTree(Area area) {
        List<Area> areas = areaMapper.selectAreaList(new Area());
        List<Ztree> ztrees = new ArrayList<>();
        Ztree ztree = null;
        for (Area areaObj : areas) {
            ztree = new Ztree();
            ztree.setId(areaObj.getId());
            ztree.setTitle(areaObj.getFullname());
            ztree.setName(areaObj.getName());
            ztree.setpId(areaObj.getParentId());

            if (area.getId() != null && areaObj.getId().equals(area.getId())){
                ztree.setChecked(true);
            }

            ztrees.add(ztree);
        }
        return ztrees;
    }

    @Override
    public List<Ztree> selectAreaListTreeCheckBox(Area area, Long roleId) {
        SysRole sysRole = roleService.selectRoleById(roleId);
        List<String> areaList = new ArrayList<>();
        if (sysRole != null && StringUtil.isNotEmpty(sysRole.getAreaIds())){
            String[] split = sysRole.getAreaIds().split(",");
            areaList = Arrays.asList(split);
        }
        List<Area> areas = areaMapper.selectAreaList(area);
        List<Ztree> ztrees = new ArrayList<>();
        Ztree ztree = null;
        for (Area areaObj : areas) {
            ztree = new Ztree();
            ztree.setId(areaObj.getId());
            ztree.setTitle(areaObj.getFullname());
            ztree.setName(areaObj.getName());
            ztree.setpId(areaObj.getParentId());
            if (!areaList.isEmpty() && areaList.contains(areaObj.getId()+"")){
                ztree.setChecked(true);
            }
            ztrees.add(ztree);
        }
        return ztrees;

    }

}
