package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrEmpTransfers;
import java.util.List;
/**
 * 员工状态异动信息 数据层
 *
 * @author liujianwen
 * @date 2021-01-11
 */
public interface HrEmpTransfersMapper extends MyBaseMapper<HrEmpTransfers> {

    /**
     * 查询员工状态异动信息列表
     *
     * @param hrEmpTransfers 员工状态异动信息
     * @return 员工状态异动信息集合
     */
    public List<HrEmpTransfers> selectHrEmpTransfersList(HrEmpTransfers hrEmpTransfers);

    /**
     * 删除员工状态异动信息
     *
     * @param id 员工状态异动信息ID
     * @return 结果
     */
    public int deleteHrEmpTransfersById(Long id);

    /**
     * 批量删除员工状态异动信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrEmpTransfersByIds(String[] ids);

}