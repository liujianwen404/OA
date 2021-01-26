package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.base.domain.TTrainingRecords;
import tk.mybatis.mapper.entity.Example;

/**
 * 培训档案Service接口
 * 
 * @author xt
 * @date 2021-01-04
 */
public interface ITTrainingRecordsService 
{
    /**
     * 查询培训档案
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    TTrainingRecords selectTTrainingRecordsById(Long id);

    /**
     * 查询培训档案列表
     * 
     * @param tTrainingRecords 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<TTrainingRecords> selectTTrainingRecordsList(TTrainingRecords tTrainingRecords);

    /**
     * 新增
     * 
     * @param tTrainingRecords
     * @return 结果
     */
    public int insertTTrainingRecords(TTrainingRecords tTrainingRecords);

    /**
     * 修改
     * 
     * @param tTrainingRecords
     * @return 结果
     */
    public int updateTTrainingRecords(TTrainingRecords tTrainingRecords);

    /**
     * 批量删除
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTrainingRecordsByIds(String ids);

    /**
     * 删除
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTTrainingRecordsById(Long id);

    TTrainingRecords selectSingleOneByExample(Example example);

    List<TTrainingRecords> selectByExample(Example example);

}
