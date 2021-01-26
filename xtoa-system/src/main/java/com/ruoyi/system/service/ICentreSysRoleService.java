package com.ruoyi.system.service;

import java.util.List;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.system.domain.CentreSysRole;

/**
 * OA角色映射对接系统Service接口
 * 
 * @author xt
 * @date 2020-10-28
 */
public interface ICentreSysRoleService 
{
    /**
     * 查询OA角色映射对接系统
     * 
     * @param id OA角色映射对接系统ID
     * @return OA角色映射对接系统
     */
    public CentreSysRole selectCentreSysRoleById(Long id);

    /**
     * 查询OA角色映射对接系统列表
     * 
     * @param centreSysRole OA角色映射对接系统
     * @return OA角色映射对接系统集合
     */
    public List<CentreSysRole> selectCentreSysRoleList(CentreSysRole centreSysRole);

    /**
     * 新增OA角色映射对接系统
     * 
     * @param centreSysRole OA角色映射对接系统
     * @return 结果
     */
    public int insertCentreSysRole(CentreSysRole centreSysRole);

    /**
     * 修改OA角色映射对接系统
     * 
     * @param centreSysRole OA角色映射对接系统
     * @return 结果
     */
    public int updateCentreSysRole(CentreSysRole centreSysRole);

    /**
     * 批量删除OA角色映射对接系统
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCentreSysRoleByIds(String ids);

    /**
     * 删除OA角色映射对接系统信息
     * 
     * @param id OA角色映射对接系统ID
     * @return 结果
     */
    public int deleteCentreSysRoleById(Long id);

    CentreSysRole selectSingleOneByExample(Example example);

    List<CentreSysRole> selectByExample(Example example);

}
