package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.base.domain.FinancePayment;

import javax.servlet.http.HttpServletRequest;

/**
 * 财务付款流程Service接口
 * 
 * @author liujianwen
 * @date 2020-10-26
 */
public interface IFinancePaymentService 
{
    /**
     * 查询财务付款流程
     * 
     * @param id 财务付款流程ID
     * @return 财务付款流程
     */
    public FinancePayment selectFinancePaymentById(Long id);

    /**
     * 查询财务付款流程列表
     * 
     * @param financePayment 财务付款流程
     * @return 财务付款流程集合
     */
    public List<FinancePayment> selectFinancePaymentList(FinancePayment financePayment);

    /**
     * 新增财务付款流程
     * 
     * @param financePayment 财务付款流程
     * @return 结果
     */
    public int insertFinancePayment(FinancePayment financePayment);

    /**
     * 修改财务付款流程
     * 
     * @param financePayment 财务付款流程
     * @return 结果
     */
    public int updateFinancePayment(FinancePayment financePayment);

    /**
     * 批量删除财务付款流程
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFinancePaymentByIds(String ids);

    /**
     * 删除财务付款流程信息
     * 
     * @param id 财务付款流程ID
     * @return 结果
     */
    public int deleteFinancePaymentById(Long id);

    FinancePayment selectSingleOneByExample(Example example);

    List<FinancePayment> selectByExample(Example example);

    AjaxResult submitApply(FinancePayment financePayment, String loginName);

    AjaxResult complete(FinancePayment financePayment, HttpServletRequest request);

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);
}
