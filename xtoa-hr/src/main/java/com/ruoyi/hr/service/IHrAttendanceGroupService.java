package com.ruoyi.hr.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateTime;
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.base.domain.HrAttendanceGroup;
import org.springframework.web.multipart.MultipartFile;

/**
 * 考勤组Service接口
 * 
 * @author liujianwen
 * @date 2020-07-29
 */
public interface IHrAttendanceGroupService 
{
    /**
     * 查询考勤组
     * 
     * @param id 考勤组ID
     * @return 考勤组
     */
    public HrAttendanceGroup selectHrAttendanceGroupById(Long id);

    /**
     * 查询考勤组列表
     * 
     * @param hrAttendanceGroup 考勤组
     * @return 考勤组集合
     */
    public List<HrAttendanceGroup> selectHrAttendanceGroupList(HrAttendanceGroup hrAttendanceGroup);

    /**
     * 新增考勤组
     * 
     * @param hrAttendanceGroup 考勤组
     * @return 结果
     */
    public int insertHrAttendanceGroup(HrAttendanceGroup hrAttendanceGroup);

    /**
     * 修改考勤组
     * 
     * @param hrAttendanceGroup 考勤组
     * @return 结果
     */
    public int updateHrAttendanceGroup(HrAttendanceGroup hrAttendanceGroup);

    /**
     * 批量删除考勤组
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupByIds(String ids);

    /**
     * 删除考勤组信息
     * 
     * @param id 考勤组ID
     * @return 结果
     */
    public int deleteHrAttendanceGroupById(Long id);

    /**
     * //1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
     * @param userId
     * @param week
     * @return
     */
    Map<String,Object> selectGroupAndClass(Long userId,int week);

    List<String> compareEmp(String empId,Long groupId);

    String uploadImg(MultipartFile file);

    /**
     * 查询该员工当前日期对应的上班时长
     *
     * @param userId 员工id
     * @param week 星期数
     * @return 当天上班班次的小时数
     */
    Double selectDayOfWorkHours(Long userId,int week);

    /**
     * 查询当前日期是不是该员工的不用打卡日期，并返回查询出的数据条数
     * @param empId
     * @param day
     * @return
     */
    Integer selectDayIsNeedNotDate(Long empId, String day);

    /**
     * 查询当前日期是不是该员工的必须打卡日期，并返回必须打卡日期字段
     * @param empId
     * @param day
     * @return
     */
    String selectDayIsMustDate(Long empId, String day);

    Integer selectDayIsClass(Long empId, int i);

    Map<String, Object> selectGruopByEmpIdFromApi(Long userId);
    /**
     * 时间段内的工作时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param userId 员工id
     * @return
     */
    Double todayIsWorkHour(Date startTime, Date endTime, Long userId);

    /**
     * 通过员工ID获取考勤组数据
     * @param userId
     * @return
     */
    HrAttendanceGroup selectGroupByEmpId(Long userId);

    Map<String,Object> elasticityOperation(Long userId, HrAttendanceClass hrAttendanceClass, String dateFormat,
                                           DateTime workDate,
                                           DateTime closingDate,
                                           DateTime restStartDate,
                                           DateTime restEndDate);

    /**
     * 两点之间小时数（不足一小时算一小时）
     * @param startTime
     * @param restEndDate
     * @return
     */
    public Double getBetween(Date startTime, Date restEndDate);

    /**
     * 通过时间范围和考勤时间段计算工作时间小时数
     * @param startTime
     * @param endTime
     * @param resultDouble
     * @param dateList
     * @return
     */
    Double getResultDouble(DateTime startTime, DateTime endTime, Double resultDouble, List<Map<String, DateTime>> dateList);

    Boolean selectIsExistGroup(Long userId);

    /**
     * 通过考勤组ID，员工ID，日期字符串，获取员工排班制的考勤组班次信息
     * @param groupId
     * @param userId
     * @param day
     * @return
     */
    Map<String, Object> selectScheduGroupAndClass(Long groupId, Long userId, String day);

    List<HrAttendanceGroup> selectGroupByClassId(String classId);
}
