package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.base.domain.HrEmp;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.base.domain.HrEmpTransfers;

/**
 * 员工状态异动信息Service接口
 * 
 * @author liujianwen
 * @date 2021-01-11
 */
public interface IHrEmpTransfersService 
{
    /**
     * 查询员工状态异动信息
     * 
     * @param id 员工状态异动信息ID
     * @return 员工状态异动信息
     */
    public HrEmpTransfers selectHrEmpTransfersById(Long id);

    /**
     * 查询员工状态异动信息列表
     * 
     * @param hrEmpTransfers 员工状态异动信息
     * @return 员工状态异动信息集合
     */
    public List<HrEmpTransfers> selectHrEmpTransfersList(HrEmpTransfers hrEmpTransfers);

    /**
     * 新增员工状态异动信息
     * 
     * @param hrEmpTransfers 员工状态异动信息
     * @return 结果
     */
    public int insertHrEmpTransfers(HrEmpTransfers hrEmpTransfers);

    /**
     * 修改员工状态异动信息
     * 
     * @param hrEmpTransfers 员工状态异动信息
     * @return 结果
     */
    public int updateHrEmpTransfers(HrEmpTransfers hrEmpTransfers);

    /**
     * 批量删除员工状态异动信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrEmpTransfersByIds(String ids);

    /**
     * 删除员工状态异动信息信息
     * 
     * @param id 员工状态异动信息ID
     * @return 结果
     */
    public int deleteHrEmpTransfersById(Long id);

    HrEmpTransfers selectSingleOneByExample(Example example);

    List<HrEmpTransfers> selectByExample(Example example);

    /**
     * 根据员工前后状态对比，如果有变化就插入一条状态变更的数据
     * @param oldEmp
     * @param nowEmp
     */
    void insertEmpTransfers(HrEmp oldEmp, HrEmp nowEmp);
}
