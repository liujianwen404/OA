package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrBusinessTrip;
import java.util.List;
/**
 * 出差申请 数据层
 *
 * @author liujianwen
 * @date 2020-06-30
 */
public interface HrBusinessTripMapper extends MyBaseMapper<HrBusinessTrip> {

    /**
     * 查询出差申请列表
     *
     * @param hrBusinessTrip 出差申请
     * @return 出差申请集合
     */
    public List<HrBusinessTrip> selectHrBusinessTripList(HrBusinessTrip hrBusinessTrip);

//    @DataScope(deptAlias = "d", menuAlias = "hr:businessTrip:businessTripList")
    public List<HrBusinessTrip> selectBusinessTripManageList(HrBusinessTrip hrBusinessTrip);

    /**
     * 删除出差申请
     *
     * @param id 出差申请ID
     * @return 结果
     */
    public int deleteHrBusinessTripById(Long id);

    /**
     * 批量删除出差申请
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrBusinessTripByIds(String[] ids);

}