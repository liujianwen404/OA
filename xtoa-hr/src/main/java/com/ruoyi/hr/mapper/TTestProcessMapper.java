package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.TTestProcess;
import java.util.List;
/**
 * 测试流程单 数据层
 *
 * @author xt
 * @date 2020-11-10
 */
public interface TTestProcessMapper extends MyBaseMapper<TTestProcess> {

    /**
     * 查询测试流程单列表
     *
     * @param tTestProcess 测试流程单
     * @return 测试流程单集合
     */
    public List<TTestProcess> selectTTestProcessList(TTestProcess tTestProcess);

    /**
     * 删除测试流程单
     *
     * @param id 测试流程单ID
     * @return 结果
     */
    public int deleteTTestProcessById(Long id);

    /**
     * 批量删除测试流程单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTestProcessByIds(String[] ids);

}