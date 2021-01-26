package com.ruoyi.hr.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.ruoyi.base.domain.DTO.HolidayDTO;
import com.ruoyi.base.domain.DTO.HolidayDTOError;
import com.ruoyi.base.domain.Holiday;

/**
 * 员工假期Service接口
 * 
 * @author liujianwen
 * @date 2020-06-05
 */
public interface IHolidayService 
{
    /**
     * 查询员工假期
     * 
     * @param id 员工假期ID
     * @return 员工假期
     */
    public Holiday selectHolidayById(Long id);

    /**
     * 查询员工假期列表
     * 
     * @param holiday 员工假期
     * @return 员工假期集合
     */
    public List<Holiday> selectHolidayList(Holiday holiday);

    /**
     * 新增员工假期
     * 
     * @param holiday 员工假期
     * @return 结果
     */
    public int insertHoliday(Holiday holiday);

    /**
     * 修改员工假期
     * 
     * @param holiday 员工假期
     * @return 结果
     */
    public int updateHoliday(Holiday holiday);

    /**
     * 批量删除员工假期
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHolidayByIds(String ids);

    /**
     * 删除员工假期信息
     * 
     * @param id 员工假期ID
     * @return 结果
     */
    public int deleteHolidayById(Long id);


    public int deleteHolidayByIdClassInfoId(Long classInfoId);

    int initHoliday(int days);

    Map<String,Double> getHolidayHours(Long userId);

    List<Holiday> selectHolidayType1ByUserId(Long userId, String type);

    /**
     * 查询员工指定年份剩余年假
     * @param empId
     * @param year
     * @return
     */
    Double selectRemainingAnuualLeave(Long empId, int year);

    /**
     * 查询该员工指定月份的公休日加班时长
     * @param empId
     * @param year
     * @param month
     * @return
     */
    Double selectPublicOvertime(Long empId, int year, int month);

    /**
     * 查询该员工指定月份的平时加班时长
     * @param empId
     * @param year
     * @param month
     * @return
     */
    Double selectGeneralOvertime(Long empId, int year, int month);

    /**
     * 查询该员工指定月份的法定假加班时长
     * @param empId
     * @param year
     * @param month
     * @return
     */
    Double selectLegalOvertime(Long empId, int year, int month);

    /**
     * 导入数据
     * @param holidayDTO excel里的数据
     * @param successCount 插入成功的数量
     * @param errorList 插入失败的数据集合
     */
    void insertSalaryStructureDTO(HolidayDTO holidayDTO, AtomicInteger successCount, List<HolidayDTOError> errorList);
}
