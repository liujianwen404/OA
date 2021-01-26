package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrBusinessTripSon;
import java.util.List;
/**
 * 出差申请子 数据层
 *
 * @author liujianwen
 * @date 2020-07-01
 */
public interface HrBusinessTripSonMapper extends MyBaseMapper<HrBusinessTripSon> {

    /**
     * 查询出差申请子列表
     *
     * @param hrBusinessTripSon 出差申请子
     * @return 出差申请子集合
     */
    public List<HrBusinessTripSon> selectHrBusinessTripSonList(HrBusinessTripSon hrBusinessTripSon);

    /**
     * 删除出差申请子
     *
     * @param id 出差申请子ID
     * @return 结果
     */
    public int deleteHrBusinessTripSonById(Long id);

    /**
     * 批量删除出差申请子
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrBusinessTripSonByIds(String[] ids);

}