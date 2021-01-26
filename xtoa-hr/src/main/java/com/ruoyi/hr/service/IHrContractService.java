package com.ruoyi.hr.service;

import java.io.IOException;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.HrContract;

import javax.servlet.http.HttpServletRequest;

/**
 * 劳动合同Service接口
 * 
 * @author xt
 * @date 2020-06-17
 */
public interface IHrContractService 
{
    /**
     * 查询劳动合同
     * 
     * @param id 劳动合同ID
     * @return 劳动合同
     */
    public HrContract selectHrContractById(Long id);

    List<HrContract> selectHrContractByEmpIdForCount(Long empid);

    /**
     * 查询劳动合同列表
     * 
     * @param hrContract 劳动合同
     * @return 劳动合同集合
     */
    public List<HrContract> selectHrContractList(HrContract hrContract);

    /**
     * 新增劳动合同
     * 
     * @param hrContract 劳动合同
     * @return 结果
     */
    public int insertHrContract(HrContract hrContract);

    /**
     * 修改劳动合同
     * 
     * @param hrContract 劳动合同
     * @return 结果
     */
    public int updateHrContract(HrContract hrContract);

    /**
     * 强行更新不跳过null
     * @param hrContract
     * @return
     */
    public int updateContract(HrContract hrContract);

    /**
     * 批量删除劳动合同
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrContractByIds(String ids);

    /**
     * 删除劳动合同信息
     * 
     * @param id 劳动合同ID
     * @return 结果
     */
    public int deleteHrContractById(Long id);

    AjaxResult submitApply(HrContract hrContract, String applyUserId);

    AjaxResult complete(HrContract hrContract, String taskId, HttpServletRequest request) throws IOException;

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);

    List<HrContract> selectHrContractListIsFrom(HrContract hrContract);
}
