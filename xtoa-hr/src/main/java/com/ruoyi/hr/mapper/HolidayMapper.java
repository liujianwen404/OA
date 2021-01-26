package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.Holiday;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 员工假期 数据层
 *
 * @author liujianwen
 * @date 2020-06-05
 */
public interface HolidayMapper extends MyBaseMapper<Holiday> {

    /**
     * 查询员工假期列表
     *
     * @param holiday 员工假期
     * @return 员工假期集合
     */
    public List<Holiday> selectHolidayList(Holiday holiday);

    /**
     * 删除员工假期
     *
     * @param id 员工假期ID
     * @return 结果
     */
    public int deleteHolidayById(Long id);

    /**
     * 批量删除员工假期
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHolidayByIds(String[] ids);

    Map<String,Double> getHolidayHours(@Param("userId") Long userId);

    List<Holiday> selectHolidayType1ByUserId(@Param("userId") Long userId, @Param("type") String type);

    Double selectRemainingAnuualLeave(@Param("empId")Long empId, @Param("year")int year);

    Double selectPublicOvertime(@Param("empId")Long empId, @Param("year")int year, @Param("month")int month);

    Double selectGeneralOvertime(@Param("empId")Long empId, @Param("year")int year, @Param("month")int month);

    Double selectLegalOvertime(@Param("empId")Long empId, @Param("year")int year, @Param("month")int month);

    int deleteHolidayByIdClassInfoId(@Param("classInfoId") Long classInfoId);

}