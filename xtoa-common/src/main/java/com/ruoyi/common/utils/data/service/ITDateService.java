package com.ruoyi.common.utils.data.service;

import com.ruoyi.common.utils.data.domain.TDate;

import java.util.Date;
import java.util.List;


/**
 * 日期Service接口
 * 
 * @author liujianwen
 * @date 2020-05-19
 */
public interface ITDateService 
{
    /**
     * 查询日期
     * 
     * @param id 日期ID
     * @return 日期
     */
    public TDate selectTDateById(Long id);

    /**
     * 查询日期列表
     * 
     * @param tDate 日期
     * @return 日期集合
     */
    public List<TDate> selectTDateList(TDate tDate);

    /**
     * 新增日期
     * 
     * @param tDate 日期
     * @return 结果
     */
    public int insertTDate(TDate tDate);

    /**
     * 修改日期
     * 
     * @param tDate 日期
     * @return 结果
     */
    public int updateTDate(TDate tDate);

    /**
     * 批量删除日期
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTDateByIds(String ids);

    /**
     * 删除日期信息
     * 
     * @param id 日期ID
     * @return 结果
     */
    public int deleteTDateById(Long id);

    /**
     * 获取请假时间段内的工作日数
     *
     * @param startTime
     * @param endTime
     * @return 结果
     */
    int selectDateCounts(Date startTime, Date endTime);

    int changeStatus(TDate tDate);

    /**
     * 查询指定年月的法定假天数
     * @param year
     * @param month
     * @return
     */
    Double selectAllLegalPublicHoliday(int year, int month);

    /**
     * 查询该日期是否是法定假
     * @param day
     * @return
     */
    Boolean selectIsLegalDay(String day);
}
