package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.base.domain.FinancePayment;
import com.ruoyi.common.core.domain.AjaxResult;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.base.domain.TTestProcess;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试流程单Service接口
 * 
 * @author xt
 * @date 2020-11-10
 */
public interface ITTestProcessService 
{
    /**
     * 查询测试流程单
     * 
     * @param id 测试流程单ID
     * @return 测试流程单
     */
    public TTestProcess selectTTestProcessById(Long id);

    /**
     * 查询测试流程单列表
     * 
     * @param tTestProcess 测试流程单
     * @return 测试流程单集合
     */
    public List<TTestProcess> selectTTestProcessList(TTestProcess tTestProcess);

    /**
     * 新增测试流程单
     * 
     * @param tTestProcess 测试流程单
     * @return 结果
     */
    public int insertTTestProcess(TTestProcess tTestProcess);

    /**
     * 修改测试流程单
     * 
     * @param tTestProcess 测试流程单
     * @return 结果
     */
    public int updateTTestProcess(TTestProcess tTestProcess);

    /**
     * 批量删除测试流程单
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTestProcessByIds(String ids);

    /**
     * 删除测试流程单信息
     * 
     * @param id 测试流程单ID
     * @return 结果
     */
    public int deleteTTestProcessById(Long id);

    TTestProcess selectSingleOneByExample(Example example);

    List<TTestProcess> selectByExample(Example example);

    AjaxResult submitApply(TTestProcess tTestProcess, String loginName);

    AjaxResult complete(TTestProcess tTestProcess, HttpServletRequest request);

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);
}
