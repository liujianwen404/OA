package com.ruoyi.hr.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.HrAttendanceInfo;

/**
 * 个人考勤记录Service接口
 * 
 * @author liujianwen
 * @date 2020-07-13
 */
public interface IHrAttendanceInfoService 
{
    /**
     * 查询个人考勤记录
     * 
     * @param id 个人考勤记录ID
     * @return 个人考勤记录
     */
    public HrAttendanceInfo selectHrAttendanceInfoById(Long id);

    /**
     * 查询个人考勤记录列表
     * 
     * @param hrAttendanceInfo 个人考勤记录
     * @return 个人考勤记录集合
     */
    public List<HrAttendanceInfo> selectHrAttendanceInfoList(HrAttendanceInfo hrAttendanceInfo);

    public List<HrAttendanceInfo> selectHrAttendanceInfoAllList(HrAttendanceInfo hrAttendanceInfo);

    /**
     * 从钉钉获取考勤数据并插入到本地表
     * @param dingUserId 钉钉userid
     * */
    public void saveAttendanceByDingAPI(String dingUserId);

    /**
     * 新增个人考勤记录
     * 
     * @param hrAttendanceInfo 个人考勤记录
     * @return 结果
     */
    public int insertHrAttendanceInfo(HrAttendanceInfo hrAttendanceInfo);

    public int insertByApp(HrAttendanceInfo hrAttendanceInfo,Boolean isAm,Boolean isPm);

    HrAttendanceInfo selectOldHrAttendanceInfo(HrAttendanceInfo hrAttendanceInfo,String checkType,String day);

    /**
     * 修改个人考勤记录
     * 
     * @param hrAttendanceInfo 个人考勤记录
     * @return 结果
     */
    public int updateHrAttendanceInfo(HrAttendanceInfo hrAttendanceInfo);

    /**
     * 批量删除个人考勤记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceInfoByIds(String ids);

    /**
     * 删除个人考勤记录信息
     * 
     * @param id 个人考勤记录ID
     * @return 结果
     */
    public int deleteHrAttendanceInfoById(Long id);

    List<Map<String,Object>> getClockIn(Long userId, String date);

    /**
     * 查询实际打卡日期和当天对应的工作时长
     * @param empId
     * @param month
     * @return
     */
    List<Map<String,Object>> selectActualAttendanceDay(Long empId, int year, int month);

    /**
     * 查询旷工半天的次数
     * @param empId
     * @param year
     * @param month
     * @return
     */
    Integer selectLateOrEarly(Long empId, int year, int month);

    /**
     * 查询该日期旷工一整天的标记
     * @param empId
     * @param day
     * @return
     */
    List<String> selectAbsentListByDay(Long empId, String day);

    int delClockIn(Long userId, String today);
}
