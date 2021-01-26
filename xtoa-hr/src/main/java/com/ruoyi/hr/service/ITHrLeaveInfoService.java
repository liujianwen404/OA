package com.ruoyi.hr.service;

import java.util.List;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.base.domain.THrLeaveInfo;

/**
 * 请假数据Service接口
 * 
 * @author xt
 * @date 2020-09-10
 */
public interface ITHrLeaveInfoService 
{
    /**
     * 查询请假数据
     * 
     * @param id 请假数据ID
     * @return 请假数据
     */
    public THrLeaveInfo selectTHrLeaveInfoById(Long id);

    /**
     * 查询请假数据列表
     * 
     * @param tHrLeaveInfo 请假数据
     * @return 请假数据集合
     */
    public List<THrLeaveInfo> selectTHrLeaveInfoList(THrLeaveInfo tHrLeaveInfo);

    /**
     * 新增请假数据
     * 
     * @param tHrLeaveInfo 请假数据
     * @return 结果
     */
    public int insertTHrLeaveInfo(THrLeaveInfo tHrLeaveInfo);

    /**
     * 修改请假数据
     * 
     * @param tHrLeaveInfo 请假数据
     * @return 结果
     */
    public int updateTHrLeaveInfo(THrLeaveInfo tHrLeaveInfo);

    /**
     * 批量删除请假数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTHrLeaveInfoByIds(String ids);

    /**
     * 删除请假数据信息
     * 
     * @param id 请假数据ID
     * @return 结果
     */
    public int deleteTHrLeaveInfoById(Long id);

    THrLeaveInfo selectSingleOneByExample(Example example);

    List<THrLeaveInfo> selectByExample(Example example);

}
