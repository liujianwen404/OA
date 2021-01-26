package com.ruoyi.system.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.system.domain.SysUserHistory;
import java.util.List;
/**
 * 用户历史信息 数据层
 *
 * @author xt
 * @date 2020-06-23
 */
public interface SysUserHistoryMapper extends MyBaseMapper<SysUserHistory> {

    /**
     * 查询用户历史信息列表
     *
     * @param sysUserHistory 用户历史信息
     * @return 用户历史信息集合
     */
    public List<SysUserHistory> selectSysUserHList(SysUserHistory sysUserHistory);

    /**
     * 删除用户历史信息
     *
     * @param id 用户历史信息ID
     * @return 结果
     */
    public int deleteSysUserHById(Long id);

    /**
     * 批量删除用户历史信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUserHByIds(String[] ids);

}