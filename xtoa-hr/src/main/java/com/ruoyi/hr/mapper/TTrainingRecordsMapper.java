package com.ruoyi.hr.mapper;

import com.ruoyi.base.domain.TTrainingRecords;
import com.ruoyi.common.mapper.MyBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * 培训档案
 *
 * @author xt
 * @date 2021-01-04
 */
public interface TTrainingRecordsMapper extends MyBaseMapper<TTrainingRecords> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tTrainingRecords 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TTrainingRecords> selectTTrainingRecordsList(TTrainingRecords tTrainingRecords);

    /**
     * 删除
     *
     * @param id
     * @return 结果
     */
    public int deleteTTrainingRecordsById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTrainingRecordsByIds(String[] ids);

    /**
     * 总培训次数
     * @param empNum
     * @return
     */
    @Select("SELECT COUNT(1) FROM t_training_records WHERE emp_num=#{empNum}")
    int queryTrainingCount(@Param("empNum") String empNum);

    /**
     * 总培训次数
     * @param empNum
     * @return
     */
    @Select("SELECT SUM(training_time) FROM t_training_records where emp_num=#{empNum}")
    Float queryTrainingTimeSum(@Param("empNum") String empNum);
}