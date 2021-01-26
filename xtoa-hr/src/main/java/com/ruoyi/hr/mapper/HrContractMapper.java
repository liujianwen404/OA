package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrContract;
import java.util.List;
/**
 * 劳动合同 数据层
 *
 * @author xt
 * @date 2020-06-17
 */
public interface HrContractMapper extends MyBaseMapper<HrContract> {

    /**
     * 查询劳动合同列表
     *
     * @param hrContract 劳动合同
     * @return 劳动合同集合
     */
    @DataScope(deptAlias = "d", menuAlias = "hr:contract:view")
    public List<HrContract> selectHrContractList(HrContract hrContract);

    /**
     * 删除劳动合同
     *
     * @param id 劳动合同ID
     * @return 结果
     */
    public int deleteHrContractById(Long id);

    /**
     * 批量删除劳动合同
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrContractByIds(String[] ids);

    @DataScope(deptAlias = "d", menuAlias = "hr:contract:view")
    List<HrContract> selectHrContractListIsFrom(HrContract hrContract);
}