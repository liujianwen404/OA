package com.ruoyi.system.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.system.domain.CentreSysRole;
import java.util.List;
/**
 * OA角色映射对接系统 数据层
 *
 * @author xt
 * @date 2020-10-28
 */
public interface CentreSysRoleMapper extends MyBaseMapper<CentreSysRole> {

    /**
     * 查询OA角色映射对接系统列表
     *
     * @param centreSysRole OA角色映射对接系统
     * @return OA角色映射对接系统集合
     */
    public List<CentreSysRole> selectCentreSysRoleList(CentreSysRole centreSysRole);

    /**
     * 删除OA角色映射对接系统
     *
     * @param id OA角色映射对接系统ID
     * @return 结果
     */
    public int deleteCentreSysRoleById(Long id);

    /**
     * 批量删除OA角色映射对接系统
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCentreSysRoleByIds(String[] ids);

}