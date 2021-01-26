package com.ruoyi.quartz.task;

import cn.hutool.core.date.DateUtil;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.ruoyi.base.dingTalk.DingAttendanceApi;
import com.ruoyi.base.domain.HrAttendanceStatistics;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.base.provider.hrService.IHrLeaveService;
import com.ruoyi.common.utils.Arith;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrAttendanceInfo;
import com.ruoyi.hr.mapper.HrAttendanceStatisticsMapper;
import com.ruoyi.hr.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 定时任务调度获取钉钉打卡详情,并存储到本地表
 * 
 * @author liujianwen
 */
@Component("attendanceStatisticsTask")
public class AttendanceStatisticsTask
{
    private static final Logger logger = LoggerFactory.getLogger(AttendanceStatisticsTask.class);

    @Resource
    private IHrAttendanceInfoService hrAttendanceInfoService;

    @Autowired
    private DingAttendanceApi dingAttendanceApi;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private IHrAttendanceGroupService attendanceGroupService;

    @Resource
    private HrAttendanceStatisticsMapper attendanceStatisticsMapper;

    @Autowired
    private IHrOvertimeService hrOvertimeService;

    @Autowired
    private IHrLeaveService hrLeaveService;

    /**
     * 获取所有员工上个月的考勤数据
     *
     * **/
    public void getAttdanceInfo()
    {
        List<HrEmp> empList = hrEmpService.selectEmpList();
        List<HrAttendanceStatistics> statisticsList = empList.parallelStream().map(emp -> {
            int workingAge = DateUtil.ageOfNow(DateUtil.format(emp.getNonManagerDate(), "yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int month = now.getMonthValue() - 1;//上个月
            List<String> dayOfWeek = getShouldAttendanceHours(year, month);
            String lastDayOfMonth = getLastDayOfMonth(year, month);
            Double ShouldAttendanceHours = 0d;//应出勤小时数
            Double ShouldAttendanceDays = 0d;//应出勤天数
            Double restDays = 0d;//应休息天数
            Double workDuration = 0d;//每日上班时长
            for (String s:dayOfWeek) {
                Double dayOfWorkHours = getDayOfWorkHours(emp.getEmpId(), s);//当前日期对应班次的每日上班时长
                ShouldAttendanceHours += dayOfWorkHours;
                if(dayOfWorkHours == 0d){
                    restDays++;
                }
                if(dayOfWorkHours != 0d){
                    workDuration = dayOfWorkHours;
                }
            }
            Double restHours = Arith.mul(restDays,workDuration);//应休息小时数
            ShouldAttendanceDays = getDaysOfMonth(DateUtil.parse(lastDayOfMonth)) - restDays;
//            Double actualAttendanceDay = hrAttendanceInfoService.selectActualAttendanceDay(emp.getEmpId(),month);//实出勤天数
//            Double actualAttendancehour = Arith.mul(actualAttendanceDay,workDuration);//实出勤小时数
//            Double actualRestDay = ShouldAttendanceDays - actualAttendanceDay;//实际休息天数 = 应出勤天数 - 实出勤天数
//            Double actualRestHour = Arith.mul(actualRestDay,workDuration);//实际休息小时数

//            Double legalOvertime = hrOvertimeService.selectOvertimeByType(emp.getEmpId(),month,"1");//法定假加班
//            Double generalOvertime = hrOvertimeService.selectOvertimeByType(emp.getEmpId(),month,"2");//平时加班
//
//            Double otherLeave = 0d;
//            Double lieuLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"1");//调休
//            Double personalLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"2");//事假
//            Double sickLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"3");//病假
//            Double annualLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"4");//年假
//            Double funeralLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"5");//丧假
//            Double marriageLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"6");//婚假
//            Double maternityLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"7");//产假
//            Double paternityLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"8");//陪产假
//            Double breastfeedingLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"9");//哺乳假
//            Double mensesLeave = hrLeaveService.selectLeaveByType(emp.getEmpId(),month,"10");//例假
//            otherLeave = funeralLeave + marriageLeave + maternityLeave + paternityLeave + breastfeedingLeave + mensesLeave;
            HrAttendanceStatistics hrAttendanceStatistics =
                    new HrAttendanceStatistics().builder()
                    .workingAge(Double.valueOf(workingAge))
                    .empNum(emp.getEmpNum())
                    .empId(emp.getEmpId())
                    .empName(emp.getEmpName())
                    .deptId(emp.getDeptId())
                    .postId(emp.getPostId())
                    .nonManagerDate(emp.getNonManagerDate())
                    .quitDate(emp.getQuitDate())
                    .district(emp.getCity())
                    .finalDate(DateUtil.parse(lastDayOfMonth))
                    .workDuration(workDuration)
                    .shouldAttendance(ShouldAttendanceHours)
                    .shouldPublicHoliday(restHours)
//                    .actualAttendance(actualAttendancehour)
//                    .actualAttendanceDay(actualAttendanceDay)
//                    .legalOvertime(legalOvertime)
//                    .generalOvertime(generalOvertime)
//                    .lieuLeave(lieuLeave)
//                    .personalLeave(personalLeave)
//                    .sickLeave(sickLeave).annualLeave(annualLeave)
//                    .otherLeave(otherLeave)
//                    .actualPublicHolidayDay(actualRestDay)
//                    .actualPublicHoliday(actualRestHour)
//                    .payWorkHour(actualAttendancehour)
                    .build();
            hrAttendanceStatistics.setDelFlag("0");
            return hrAttendanceStatistics;
        }).collect(Collectors.toList());
        attendanceStatisticsMapper.insertList(statisticsList);
    }

    /**
     * 返回一个月中所有的日期与对应的星期的集合，格式为 日期:星期
     * @param year
     * @param month
     * @return
     */
    public List<String> getShouldAttendanceHours(int year,int month){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf1.setLenient(false);
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE", Locale.ENGLISH);
        List<String> list = new ArrayList<>();
        for(int i = 1; i < 32; i++){
            try {
                Date date = sdf1.parse(year + "-" + month + "-" + i);
                list.add(sdf1.format(date) + ":" + sdf2.format(date));
            } catch (ParseException e) {
                //do nothing
            }
        }
        return list;
    }

    public Double getDayOfWorkHours(Long userId,String s){
        if(s.contains("Mon")){
            return attendanceGroupService.selectDayOfWorkHours(userId,2);
        }if(s.contains("Tue")){
            return attendanceGroupService.selectDayOfWorkHours(userId,3);
        }if(s.contains("Wed")){
            return attendanceGroupService.selectDayOfWorkHours(userId,4);
        }if(s.contains("Thu")){
            return attendanceGroupService.selectDayOfWorkHours(userId,5);
        }if(s.contains("Fri")){
            return attendanceGroupService.selectDayOfWorkHours(userId,6);
        }if(s.contains("Sat")){
            return attendanceGroupService.selectDayOfWorkHours(userId,7);
        }if(s.contains("Sun")){
            return attendanceGroupService.selectDayOfWorkHours(userId,1);
        }else{
            return 0d;
        }
    }

    /**
     * 返回传入月份的上个月最后一天的日期
     * @param year
     * @param month
     * @return
     */
    public String getLastDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        // cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.MONTH, month); //设置当前月的上一个月
        // 获取某月最大天数
        //int lastDay = cal.getActualMaximum(Calendar.DATE);
        int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
        // 设置日历中月份的最大天数
        //cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public List<String> getDingUserId(){
        List<String> list = new ArrayList<>(50);
        String dingUserId = hrEmpService.getDingUserId(ShiroUtils.getSysUser(), false);
        return list;
    }

    public Double getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return Double.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }


    /**
     * 将数据接受对象的属性拷贝到对应的实体类
     * @param recordresult
     * @return
     */
    public HrAttendanceInfo convert(OapiAttendanceListRecordResponse.Recordresult recordresult) {
        HrAttendanceInfo attendanceInfo = new HrAttendanceInfo();
        try {
            BeanUtils.copyProperties(recordresult,attendanceInfo);
        } catch (Exception e) {
            logger.error("对象属性拷贝异常");
            e.printStackTrace();
        }
        return attendanceInfo;
    }

}
