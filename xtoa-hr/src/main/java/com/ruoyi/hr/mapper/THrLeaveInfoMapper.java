package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.THrLeaveInfo;
import java.util.List;
/**
 * 请假数据 数据层
 *
 * @author xt
 * @date 2020-09-10
 */
public interface THrLeaveInfoMapper extends MyBaseMapper<THrLeaveInfo> {

    /**
     * 查询请假数据列表
     *
     * @param tHrLeaveInfo 请假数据
     * @return 请假数据集合
     */
    public List<THrLeaveInfo> selectTHrLeaveInfoList(THrLeaveInfo tHrLeaveInfo);

    /**
     * 删除请假数据
     *
     * @param id 请假数据ID
     * @return 结果
     */
    public int deleteTHrLeaveInfoById(Long id);

    /**
     * 批量删除请假数据
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTHrLeaveInfoByIds(String[] ids);

}