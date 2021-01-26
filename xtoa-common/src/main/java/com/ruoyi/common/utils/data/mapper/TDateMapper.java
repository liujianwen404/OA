package com.ruoyi.common.utils.data.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.common.utils.data.domain.TDate;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
/**
 * 日期 数据层
 *
 * @author liujianwen
 * @date 2020-05-19
 */
public interface TDateMapper extends MyBaseMapper<TDate> {

    /**
     * 查询日期列表
     *
     * @param tDate 日期
     * @return 日期集合
     */
    public List<TDate> selectTDateList(TDate tDate);

    /**
     * 删除日期
     *
     * @param id 日期ID
     * @return 结果
     */
    public int deleteTDateById(Long id);

    /**
     * 批量删除日期
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTDateByIds(String[] ids);

    /**
     * 获取请假时间段内的工作日数
     *
     * @param startTime
     * @param endTime
     * @return 结果
     */
    int selectDateCounts(@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    int changeStatus(TDate tDate);

    Double selectAllLegalPublicHoliday(@Param("year")int year, @Param("month")int month);

    int selectIsLegalDay(String day);
}