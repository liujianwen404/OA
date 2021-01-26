package com.ruoyi.hr.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTO;
import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTOError;
import com.ruoyi.base.domain.DTO.SalaryDTOError;
import com.ruoyi.base.domain.DTO.SalaryStructureDTO;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.utils.DingDingUtil;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.Arith;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.data.service.ITDateService;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrAttendanceStatisticsMapper;
import com.ruoyi.base.domain.HrAttendanceStatistics;
import com.ruoyi.hr.service.IHrAttendanceStatisticsService;
import com.ruoyi.common.core.text.Convert;

import javax.annotation.Resource;

/**
 * 考勤统计Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-08-11
 */
@Slf4j
@Service
public class HrAttendanceStatisticsServiceImpl implements IHrAttendanceStatisticsService 
{


    @Resource
    private HrAttendanceStatisticsMapper hrAttendanceStatisticsMapper;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ITDateService dateService;

    @Resource(name = "ThreadPoolExecutorForCallBack")
    private ThreadPoolExecutor threadPoolExecutor;


    /**
     * 查询考勤统计
     * 
     * @param id 考勤统计ID
     * @return 考勤统计
     */
    @Override
    public HrAttendanceStatistics selectHrAttendanceStatisticsById(Long id)
    {
        return hrAttendanceStatisticsMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询考勤统计列表
     * 
     * @param hrAttendanceStatistics 考勤统计
     * @return 考勤统计
     */
    @Override
    @DataScope(deptAlias = "d", menuAlias = "hr:attendanceStatistics:view")
    public List<HrAttendanceStatistics> selectHrAttendanceStatisticsList(HrAttendanceStatistics hrAttendanceStatistics)
    {
        hrAttendanceStatistics.setDelFlag("0");
        return hrAttendanceStatisticsMapper.selectHrAttendanceStatisticsList(hrAttendanceStatistics);
    }

    /**
     * 新增考勤统计
     * 
     * @param hrAttendanceStatistics 考勤统计
     * @return 结果
     */
    @Override
    public int insertHrAttendanceStatistics(HrAttendanceStatistics hrAttendanceStatistics)
    {
        hrAttendanceStatistics.setCreateId(ShiroUtils.getUserId());
        hrAttendanceStatistics.setCreateBy(ShiroUtils.getLoginName());
        hrAttendanceStatistics.setCreateTime(DateUtils.getNowDate());
        return hrAttendanceStatisticsMapper.insertSelective(hrAttendanceStatistics);
    }

    /**
     * 修改考勤统计
     * 
     * @param hrAttendanceStatistics 考勤统计
     * @return 结果
     */
    @Override
    public int updateHrAttendanceStatistics(HrAttendanceStatistics hrAttendanceStatistics)
    {
        hrAttendanceStatistics.setUpdateId(ShiroUtils.getUserId());
        hrAttendanceStatistics.setUpdateBy(ShiroUtils.getLoginName());
        hrAttendanceStatistics.setUpdateTime(DateUtils.getNowDate());
        return hrAttendanceStatisticsMapper.updateByPrimaryKeySelective(hrAttendanceStatistics);
    }

    /**
     * 删除考勤统计对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceStatisticsByIds(String ids)
    {
        return hrAttendanceStatisticsMapper.deleteHrAttendanceStatisticsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除考勤统计信息
     * 
     * @param id 考勤统计ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceStatisticsById(Long id)
    {
        return hrAttendanceStatisticsMapper.deleteHrAttendanceStatisticsById(id);
    }

    /**
     * 考勤假期、扣款、补贴等数据
     *
     * @param attendanceStatisticsDTO 考勤假期、工作制、夜班补贴等数据
     * @param successCount
     * @param errorList
     * @return void
     */
    @Override
    public void insertAttendanceStatistics(AttendanceStatisticsDTO attendanceStatisticsDTO, AtomicInteger successCount, List<AttendanceStatisticsDTOError> errorList) {
        Long empId = attendanceStatisticsDTO.getEmpId();
        String statisticsDate = attendanceStatisticsDTO.getStatisticsDate();
        if(empId == null || statisticsDate == null){
            addErrorDTO(attendanceStatisticsDTO, errorList, "员工ID与统计日期不能为空");
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(statisticsDate.substring(0,10), fmt);
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        HrAttendanceStatistics hrAttendanceStatistics = convert(attendanceStatisticsDTO);
        HrEmp hrEmp = hrEmpService.selectTHrEmpById(empId);
        if (hrEmp == null) {
            addErrorDTO(attendanceStatisticsDTO, errorList, "不存在员工ID"+empId+"对应的员工信息");
            return;
        }
        hrAttendanceStatistics.setEmpName(hrEmp.getEmpName());
        hrAttendanceStatistics.setDeptId(hrEmp.getDeptId());
        hrAttendanceStatistics.setPostId(hrEmp.getPostId());
        hrAttendanceStatistics.setDelFlag("0");
        //存在则更新，不存在则新增
        HrAttendanceStatistics oldAttendanceStatistics = hrAttendanceStatisticsMapper.selectSingleOneByCondition(empId,year,month);
        if (oldAttendanceStatistics == null) {
            hrAttendanceStatisticsMapper.insert(hrAttendanceStatistics);
        } else {
            hrAttendanceStatistics.setId(oldAttendanceStatistics.getId());
            //只更新实体对象中不为空的字段
            hrAttendanceStatisticsMapper.updateByPrimaryKeySelective(hrAttendanceStatistics);
        }
        //记录一条成功数据
        successCount.incrementAndGet();
    }


    private void addErrorDTO(AttendanceStatisticsDTO attendanceStatisticsDTO, List<AttendanceStatisticsDTOError> errorList, String remark) {
        AttendanceStatisticsDTOError attendanceStatisticsDTOError = new AttendanceStatisticsDTOError();
        BeanUtil.copyProperties(attendanceStatisticsDTO, attendanceStatisticsDTOError);
        attendanceStatisticsDTOError.setErrorInfo(remark);
        errorList.add(attendanceStatisticsDTOError);
    }

    @Override
    public HrAttendanceStatistics selectSingleOneByCondition(Long empId, Integer year, Integer month){
        return hrAttendanceStatisticsMapper.selectSingleOneByCondition(empId,year,month);
    }

    @Override
    public void restart(HrAttendanceStatistics hrAttendanceStatistics) {
        hrAttendanceStatistics.setStatus("3");//同步中
        hrAttendanceStatisticsMapper.updateByPrimaryKeySelective(hrAttendanceStatistics);

        String statisticsDate = DateUtil.now();
        Date lastDate = DateUtil.offsetMonth(DateUtil.parse(statisticsDate), -1);//上个月日期
        LocalDate lastLocalDate = lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int lastYear = lastLocalDate.getYear();
        int lastMonth = lastLocalDate.getMonthValue();
        SysUser sysUser = sysUserService.selectUserById(hrAttendanceStatistics.getEmpId());
        String dingUserId = hrEmpService.getDingUserId(sysUser, false);
        try {
            threadPoolExecutor.execute(() -> {
                //上班班次
                Set<String> classDateSet = DingDingUtil.getClassDate(dingUserId, lastYear, lastMonth);
                String className = StringUtils.strip(StringUtils.strip(classDateSet.toString(), "[]"), ",");

                //每日上班时长
                double workDuration = DingDingUtil.getMaxWorkHour(dingUserId, lastYear, lastMonth);

                //应出勤
                Map<String, Double> shouldAttendanceMap = getShouldAttendanceMap(dingUserId, lastYear, lastMonth);
                double shouldAttendanceDays = shouldAttendanceMap.get("days");
                double shouldAttendanceHours = shouldAttendanceMap.get("hours");
                //应公休
                double restHours = getShouldRestHour(dingUserId, lastYear, lastMonth, workDuration);
                //实出勤
                Map<String, Double> actualAttMap = getActualAttMap(dingUserId, lastYear, lastMonth);
                double actualAttendancehour = actualAttMap.get("hours");
                double actualAttendanceDay = actualAttMap.get("days");
                //实公休
                Map<String, Double> actualRestMap = getActualRestMap(dingUserId, lastYear, lastMonth, workDuration);
                double actualRestHour = actualRestMap.get("hours");
                double actualRestDay = actualRestMap.get("days");

                Double legalPublicHoliday = dateService.selectAllLegalPublicHoliday(lastYear, lastMonth);//法定假天数
                Double legalPublicHolidayHours = Arith.mul(legalPublicHoliday, workDuration);//法定假工时

                //休息日加班
                Map<String, Object> restOvertimeMap = DingDingUtil.getRestOvertimes(dingUserId, lastYear, lastMonth);
                double restOvertimeNum = restOvertimeMap.entrySet().parallelStream()
                        .map(s -> String.valueOf(s.getValue())).mapToDouble(Double::parseDouble).sum();
                //节假日加班
                Map<String, Object> holidayOvertimeMap = DingDingUtil.getHolidayOvertimes(dingUserId, lastYear, lastMonth);
                double holidayOvertimeNum = holidayOvertimeMap.entrySet().parallelStream()
                        .map(s -> String.valueOf(s.getValue())).mapToDouble(Double::parseDouble).sum();
                //工作日加班
                Map<String, Object> workdayOvertimeMap = DingDingUtil.getWorkdayOvertimes(dingUserId, lastYear, lastMonth);
                double workdayOvertimeNum = workdayOvertimeMap.entrySet().parallelStream()
                        .map(s -> String.valueOf(s.getValue())).mapToDouble(Double::parseDouble).sum();
                //平时加班 = 休息日加班 + 工作日加班
                double generalOvertime = restOvertimeNum + workdayOvertimeNum;
                //本月加班存休 = 法定假加班时长 + 平时加班时长
                double overtimeHoliday = holidayOvertimeNum + generalOvertime;

                //旷工时长
                double absentLeave = 0D;
                List<String> absentDays = DingDingUtil.getAbsentDays(dingUserId, lastYear, lastMonth);
                if (!absentDays.isEmpty()) {
                    Map<String, Object> shouldAttDateAndWorkHour = DingDingUtil.getShouldAttDateAndWorkHour(dingUserId, lastYear, lastMonth);
                    absentLeave = absentDays.stream()
                            .map(s -> String.valueOf(shouldAttDateAndWorkHour.get(s) == null ? "0" : shouldAttDateAndWorkHour.get(s)))
                            .mapToDouble(Double::parseDouble).sum();
                }

                double otherLeave = 0D;
                double lieuLeave = 0D;//调休
                double sickLeave = 0D;//病假
                double personalLeave = 0D;//事假
                double annualLeave = 0D;//年假
                double maternityLeave = 0D;//产假
                double paternityLeave = 0D;//陪产假
                double marriageLeave = 0D;//婚假
                double funeralLeave = 0D;//丧假
//                    double breastfeedingLeave = 0D;//哺乳假
//                    double mensesLeave = 0D;//例假

                //请假数据
                List<String> leaveData = DingDingUtil.getLeaveProcessInstance(dingUserId, lastYear, lastMonth);
                if (!leaveData.isEmpty()) {
                    for (String s : leaveData) {
                        double hour = Double.parseDouble(StringUtils.strip(s.split(",")[2],"\"\""));
                        String type = StringUtils.strip(s.split(",")[4], "\"\"");
                        switch (type) {
                            case "调休":
                                lieuLeave += hour;
                                break;
                            case "病假":
                                sickLeave += hour;
                                break;
                            case "事假":
                                personalLeave += hour;
                                break;
                            case "年假":
                                annualLeave += hour;
                                break;
                            case "产假":
                                maternityLeave += hour;
                                break;
                            case "陪产假":
                                paternityLeave += hour;
                                break;
                            case "婚假":
                                marriageLeave += hour;
                                break;
                            case "丧假":
                                funeralLeave += hour;
                                break;
                        }
                    }
                }
                otherLeave = maternityLeave + paternityLeave + marriageLeave + funeralLeave;
                //计薪工时 = 应出勤工时 + 法定假工时 - （病假 + 事假）
                double payWorkHour = shouldAttendanceHours + legalPublicHolidayHours - sickLeave - personalLeave;
                hrAttendanceStatistics.setClassName(className);
                hrAttendanceStatistics.setWorkDuration(workDuration);
                hrAttendanceStatistics.setShouldAttendance(shouldAttendanceHours);
                hrAttendanceStatistics.setShouldPublicHoliday(restHours);
                hrAttendanceStatistics.setActualAttendance(actualAttendancehour);
                hrAttendanceStatistics.setActualAttendanceDay(actualAttendanceDay);
                hrAttendanceStatistics.setLegalOvertime(holidayOvertimeNum);
                hrAttendanceStatistics.setGeneralOvertime(generalOvertime);
                hrAttendanceStatistics.setLieuLeave(lieuLeave);
                hrAttendanceStatistics.setPersonalLeave(personalLeave);
                hrAttendanceStatistics.setSickLeave(sickLeave);
                hrAttendanceStatistics.setAnnualLeave(annualLeave);
                hrAttendanceStatistics.setOtherLeave(otherLeave);
                hrAttendanceStatistics.setAbsentLeave(absentLeave);
                hrAttendanceStatistics.setActualPublicHolidayDay(actualRestDay);
                hrAttendanceStatistics.setActualPublicHoliday(actualRestHour);
                hrAttendanceStatistics.setLegalPublicHoliday(legalPublicHolidayHours);
                hrAttendanceStatistics.setOvertimeHoliday(overtimeHoliday);
                hrAttendanceStatistics.setPayWorkHour(payWorkHour);
                hrAttendanceStatistics.setStatisticsDate(statisticsDate);
                hrAttendanceStatistics.setStatus("1");//已同步
                hrAttendanceStatistics.setDelFlag("0");
                hrAttendanceStatisticsMapper.updateByPrimaryKeySelective(hrAttendanceStatistics);
            });
        } catch (Exception e) {
            hrAttendanceStatistics.setStatus("2");//同步失败
            hrAttendanceStatisticsMapper.updateByPrimaryKeySelective(hrAttendanceStatistics);
            e.printStackTrace();
            log.error("员工考勤汇总重试异常:[{}],dingUserId:[{}]",e.getMessage(),dingUserId);
        }
    }

    @Override
    public AjaxResult init() {
        String statisticsDate = DateUtil.now();
        Date lastDate = DateUtil.offsetMonth(DateUtil.parse(statisticsDate), -1);//上个月日期
        LocalDate lastLocalDate = lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int lastYear = lastLocalDate.getYear();
        int lastMonth = lastLocalDate.getMonthValue();
        List<HrEmp> empList = hrEmpService.selectCountAttEmpList(lastYear,lastMonth);
        try{
            empList.forEach(emp -> {
                HrAttendanceStatistics hrAttendanceStatistics = new HrAttendanceStatistics();
                Long empId = emp.getEmpId();
                String empName = emp.getEmpName();
                Date finalDate  = DateUtil.parse(DingDingUtil.getLastDayOfMonth(lastYear, lastMonth));
                Date quitDate = emp.getQuitDate();
                if (quitDate != null) {
                    finalDate = quitDate;
                }
                //部门、岗位
                Long deptId = emp.getDeptId();
                Long postId = emp.getPostId();
                SysDept sysDept = deptService.selectDeptById(deptId);
                SysPost sysPost = postService.selectPostById(postId);
                String deptName = sysDept.getDeptName();
                String postName = sysPost.getPostName();
                String ancestors = sysDept.getAncestors();
                String[] ancestorsArray = ancestors.split(",");
                Long firstDeptId = null;
                Long secondDeptId = null;
                String firstDeptName = null;
                String secondDeptName = null;
                if (ancestorsArray.length == 2) {
                    //那么此人只有一级部门，且当前部门本身就是一级部门
                    firstDeptId = deptId;
                    firstDeptName = deptName;
                } else if (ancestorsArray.length == 3) {
                    //那么此人既有一级部门，也有二级部门，且当前部门本身就是二级部门
                    firstDeptId = Long.valueOf(ancestorsArray[2]);
                    firstDeptName = deptService.selectDeptById(firstDeptId).getDeptName();
                    secondDeptId = deptId;
                    secondDeptName = deptName;
                } else if (ancestorsArray.length > 3) {
                    //那么此人既有一级部门，也有二级部门
                    firstDeptId = Long.valueOf(ancestorsArray[2]);
                    firstDeptName = deptService.selectDeptById(firstDeptId).getDeptName();
                    secondDeptId = Long.valueOf(ancestorsArray[3]);
                    secondDeptName = deptService.selectDeptById(secondDeptId).getDeptName();
                }
                //一二级部门
                if (firstDeptId != null) {
                    hrAttendanceStatistics.setFirstDeptId(firstDeptId);
                    hrAttendanceStatistics.setFirstDeptName(firstDeptName);
                }
                if (secondDeptId != null) {
                    hrAttendanceStatistics.setSecondDeptId(secondDeptId);
                    hrAttendanceStatistics.setSecondDeptName(secondDeptName);
                }

                hrAttendanceStatistics.setEmpId(empId);
                hrAttendanceStatistics.setEmpNum(emp.getEmpNum());
                hrAttendanceStatistics.setEmpName(empName);
                hrAttendanceStatistics.setDeptId(deptId);
                hrAttendanceStatistics.setDeptName(deptName);
                hrAttendanceStatistics.setPostId(postId);
                hrAttendanceStatistics.setPostName(postName);
                hrAttendanceStatistics.setNonManagerDate(emp.getNonManagerDate());
                hrAttendanceStatistics.setFinalDate(finalDate);
                hrAttendanceStatistics.setDelFlag("0");
                hrAttendanceStatistics.setStatisticsDate(statisticsDate);

                //存在则更新，不存在则新增
                HrAttendanceStatistics oldAttendanceStatistics = hrAttendanceStatisticsMapper.selectSingleOneByCondition(empId, lastYear, lastMonth);
                if (oldAttendanceStatistics != null) {
                    hrAttendanceStatistics.setId(oldAttendanceStatistics.getId());
                    hrAttendanceStatisticsMapper.updateByPrimaryKeySelective(hrAttendanceStatistics);
                } else {
                    hrAttendanceStatistics.setStatus("0");//未同步
                    hrAttendanceStatisticsMapper.insert(hrAttendanceStatistics);
                }
                log.info("初始化员工考勤数据，姓名：{}",empName);
            });
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化考勤数据异常，{}",e.getMessage());
            return AjaxResult.error("初始化考勤数据异常，请联系管理员。");
        }
    }

    /**
     * 获取应出勤小时数，天数
     * @param year
     * @param month
     * @return
     */
    public Map<String,Double> getShouldAttendanceMap(String dingUserId,int year,int month){
        Map<String, Object> shouldAttDateAndWorkHour = DingDingUtil.getShouldAttDateAndWorkHour(dingUserId, year, month);
        double hours = 0D;
        double days = 0D;
        if (shouldAttDateAndWorkHour != null && !shouldAttDateAndWorkHour.isEmpty()) {
            hours = shouldAttDateAndWorkHour.entrySet().parallelStream()
                    .mapToDouble(entry -> (double) entry.getValue()).sum();
            days = shouldAttDateAndWorkHour.entrySet().size();
        }
        Map<String,Double> returnMap = new HashMap<>();
        returnMap.put("hours",hours);
        returnMap.put("days",days);
        return returnMap;
    }

    /**
     * 获取应公休小时数
     * @param year
     * @param month
     * @return
     */
    public double getShouldRestHour(String dingUserId,int year,int month,double workHour){
        List<String> restDays = DingDingUtil.getShouldRestDays(dingUserId, year, month);
        return Arith.mul(restDays.size(),workHour);
    }

    /**
     * 获取实际出勤小时数，天数
     * @param year
     * @param month
     * @return
     */
    public Map<String,Double> getActualAttMap(String dingUserId,int year,int month){
        Map<String, Object> actualAttDateAndWorkHour = DingDingUtil.getActualAttDateAndWorkHour(dingUserId, year, month);
        Double hours = 0D;
        double days = 0D;
        if (actualAttDateAndWorkHour != null && !actualAttDateAndWorkHour.isEmpty()) {
            hours = actualAttDateAndWorkHour.entrySet().parallelStream()
                    .mapToDouble(entry -> (double) entry.getValue()).sum();
            days = actualAttDateAndWorkHour.entrySet().size();
        }
        Map<String,Double> returnMap = new HashMap<>();
        returnMap.put("hours",hours);
        returnMap.put("days",days);
        return returnMap;
    }

    /**
     * 获取实际公休小时数，天数
     * @param year
     * @param month
     * @return
     */
    public Map<String,Double> getActualRestMap(String dingUserId,int year,int month,double workHour){
        List<String> actualRestDays = DingDingUtil.getActualRestDays(dingUserId, year, month);
        double days = actualRestDays.size();
        double hours = Arith.mul(days,workHour);
        Map<String,Double> returnMap = new HashMap<>();
        returnMap.put("hours",hours);
        returnMap.put("days",days);
        return returnMap;
    }

    /**
     * 实体对象属性拷贝
     * @param attendanceStatisticsDTO
     * @return
     */
    public HrAttendanceStatistics convert(AttendanceStatisticsDTO attendanceStatisticsDTO) {
        HrAttendanceStatistics hrAttendanceStatistics = new HrAttendanceStatistics();
        try {
            BeanUtils.copyProperties(attendanceStatisticsDTO,hrAttendanceStatistics);
        } catch (Exception e) {
            log.error("对象属性拷贝异常");
            e.printStackTrace();
        }
        return hrAttendanceStatistics;
    }

}
