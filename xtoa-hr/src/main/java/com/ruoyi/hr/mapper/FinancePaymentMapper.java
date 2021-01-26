package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.FinancePayment;
import java.util.List;
/**
 * 财务付款流程 数据层
 *
 * @author liujianwen
 * @date 2020-10-26
 */
public interface FinancePaymentMapper extends MyBaseMapper<FinancePayment> {

    /**
     * 查询财务付款流程列表
     *
     * @param financePayment 财务付款流程
     * @return 财务付款流程集合
     */
    public List<FinancePayment> selectFinancePaymentList(FinancePayment financePayment);

    /**
     * 删除财务付款流程
     *
     * @param id 财务付款流程ID
     * @return 结果
     */
    public int deleteFinancePaymentById(Long id);

    /**
     * 批量删除财务付款流程
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFinancePaymentByIds(String[] ids);

}