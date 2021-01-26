package com.ruoyi.hr.service;

import java.util.List;
import com.ruoyi.base.domain.HrBusinessTripSon;

/**
 * 出差申请子Service接口
 * 
 * @author liujianwen
 * @date 2020-07-01
 */
public interface IHrBusinessTripSonService 
{
    /**
     * 查询出差申请子
     * 
     * @param id 出差申请子ID
     * @return 出差申请子
     */
    public HrBusinessTripSon selectHrBusinessTripSonById(Long id);

    /**
     * 查询出差申请子列表
     * 
     * @param hrBusinessTripSon 出差申请子
     * @return 出差申请子集合
     */
    public List<HrBusinessTripSon> selectHrBusinessTripSonList(HrBusinessTripSon hrBusinessTripSon);

    /**
     * 新增出差申请子
     * 
     * @param hrBusinessTripSon 出差申请子
     * @return 结果
     */
    public int insertHrBusinessTripSon(HrBusinessTripSon hrBusinessTripSon);

    /**
     * 修改出差申请子
     * 
     * @param hrBusinessTripSon 出差申请子
     * @return 结果
     */
    public int updateHrBusinessTripSon(HrBusinessTripSon hrBusinessTripSon);

    /**
     * 批量删除出差申请子
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrBusinessTripSonByIds(String ids);

    /**
     * 删除出差申请子信息
     * 
     * @param id 出差申请子ID
     * @return 结果
     */
    public int deleteHrBusinessTripSonById(Long id);
}
