package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.hr.domain.HrJobTransfer;
import java.util.List;
/**
 * 调动申请 数据层
 *
 * @author xt
 * @date 2020-05-22
 */
public interface HrJobTransferMapper extends MyBaseMapper<HrJobTransfer> {

    /**
     * 查询调动申请列表
     *
     * @param hrJobTransfer 调动申请
     * @return 调动申请集合
     */
    @DataScope(deptAlias = "d", menuAlias = "hr:report:jobTransfer:view")
    public List<HrJobTransfer> selectHrJobTransferList(HrJobTransfer hrJobTransfer);

    @DataScope(deptAlias = "d", menuAlias = "hr:jobTransfer:viewManage")
    public List<HrJobTransfer> selectHrJobTransferListManage(HrJobTransfer hrJobTransfer);

    public List<HrJobTransfer> selectHrJobTransferList2(HrJobTransfer hrJobTransfer);

    /**
     * 删除调动申请
     *
     * @param jobTransferId 调动申请ID
     * @return 结果
     */
    public int deleteHrJobTransferById(Long jobTransferId);

    /**
     * 批量删除调动申请
     *
     * @param jobTransferIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrJobTransferByIds(String[] jobTransferIds);

}