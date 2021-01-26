package com.ruoyi.quartz.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.fasterxml.jackson.core.sym.NameN;
import com.ruoyi.base.dingTalk.DingAttendanceApi;
import com.ruoyi.base.domain.*;
import com.ruoyi.base.provider.hrService.IHrLeaveService;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.base.utils.DingDingUtil;
import com.ruoyi.common.utils.Arith;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.data.service.ITDateService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrAttendanceStatisticsMapper;
import com.ruoyi.hr.service.*;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 定时任务调度获取钉钉打卡详情,并存储到本地表
 * 
 * @author liujianwen
 */
@Slf4j
@Component("attendanceStatisticsOfDayTask")
public class AttendanceStatisticsOfDayTask
{

    @Autowired
    private IHrAttendanceInfoService hrAttendanceInfoService;

    @Autowired
    private DingAttendanceApi dingAttendanceApi;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private IHrAttendanceGroupService attendanceGroupService;

    @Autowired
    private IHrAttendanceClassService attendanceClassService;

    @Resource
    private HrAttendanceStatisticsMapper attendanceStatisticsMapper;

    @Autowired
    private IHrOvertimeService hrOvertimeService;

    @Autowired
    private IHrLeaveService hrLeaveService;

    @Autowired
    private ITDateService dateService;

    @Autowired
    private IHolidayService holidayService;
    
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private IHrFillClockService hrFillClockService;

    @Resource(name = "ThreadPoolExecutorForCallBack")
    private ThreadPoolExecutor threadPoolExecutor;


    /**
     * 统计所有员工当前月份当天之前的考勤数据
     *
     * **/
    public void countAttendance()
    {
        log.info("考勤汇总报表定时任务开始");
        long startTime = System.currentTimeMillis();
        String statisticsDate = DateUtil.now();
        Date lastDate = DateUtil.offsetMonth(DateUtil.parse(statisticsDate), -1);//上个月日期
        LocalDate lastLocalDate = lastDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int lastYear = lastLocalDate.getYear();
        int lastMonth = lastLocalDate.getMonthValue();
        List<Map<String,Object>> empList = hrEmpService.selectOnTheJobEmpList(lastYear,lastMonth);

        empList.forEach(map -> {
            try {
                threadPoolExecutor.execute(() -> {
                    Long empId = (Long) map.get("emp_id");
                    String dingUserId = String.valueOf(map.get("ding_user_id"));
                    Date finalDate  = DateUtil.parse(getLastDayOfMonth(lastYear, lastMonth));
                    Date quitDate = (Date) map.get("quit_date");
                    if (quitDate != null) {
                        finalDate = quitDate;
                    }
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

                    String empNum = String.valueOf(map.get("emp_num"));//工号
                    String empName = String.valueOf(map.get("emp_name"));//姓名
                    String city = String.valueOf(map.get("city"));//城市
                    Date nonManagerDate = (Date) map.get("non_manager_date");//入职日期
                    //工龄
                    double workingAge = Double.valueOf(DateUtil.ageOfNow(nonManagerDate));
                    //剩余年假
                    double overAnnualLeave = holidayService.selectRemainingAnuualLeave(empId, lastYear);
                    HrAttendanceStatistics hrAttendanceStatistics =
                            new HrAttendanceStatistics().builder()
                                    .workingAge(workingAge)
                                    .overAnnualLeave(overAnnualLeave)
                                    .empNum(empNum)
                                    .empId(empId)
                                    .empName(empName)
                                    .nonManagerDate(nonManagerDate)
                                    .quitDate(quitDate)
                                    .district(city)
                                    .finalDate(finalDate)
                                    .className(className)
                                    .workDuration(workDuration)
                                    .shouldAttendance(shouldAttendanceHours)
                                    .shouldPublicHoliday(restHours)
                                    .actualAttendance(actualAttendancehour)
                                    .actualAttendanceDay(actualAttendanceDay)
                                    .legalOvertime(holidayOvertimeNum)
                                    .generalOvertime(generalOvertime)
                                    .lieuLeave(lieuLeave)
                                    .personalLeave(personalLeave)
                                    .sickLeave(sickLeave)
                                    .annualLeave(annualLeave)
                                    .otherLeave(otherLeave)
                                    .absentLeave(absentLeave)
                                    .actualPublicHolidayDay(actualRestDay)
                                    .actualPublicHoliday(actualRestHour)
                                    .legalPublicHoliday(legalPublicHolidayHours)
                                    .overtimeHoliday(overtimeHoliday)
                                    .payWorkHour(payWorkHour)
                                    .statisticsDate(statisticsDate)
                                    .status("1")//已同步
                                    .delFlag("0")
                                    .build();
                    //部门、岗位
                    hrAttendanceStatistics = setDept(map, hrAttendanceStatistics);

                    //缺卡扣款计算 TODO
//                int noClockNum = hrFillClockService.selectHrFillClockNum(empId, DateUtils.getNowDate());//所有缺卡次数
//                int fillClockNum = hrFillClockService.selectHrFillClockCompleteCount(empId, DateUtils.getNowDate());//所有补卡通过的次数
//
//                if ( noClockNum <=3 ){//如果缺卡1-3次，补卡不扣款，未补卡每次扣款20元
//                    int deductNum = noClockNum - fillClockNum;//需要扣款的次数
//                    hrAttendanceStatistics.setNotClockDeduct(BigDecimal.valueOf(deductNum).multiply(new BigDecimal("20")));
//                }
//                if ( noClockNum == 4 || noClockNum == 5 ) {//如果缺卡4-5次，前3次补卡不扣款，未补卡每次扣款20元；后两次每次扣款30元
//                    if(fillClockNum > 3){//补卡超过3次
//                        int deductNum = fillClockNum - 3;//需要扣款30的次数
//                        hrAttendanceStatistics.setNotClockDeduct(BigDecimal.valueOf(deductNum).multiply(new BigDecimal("30")));
//                    }else{//补卡没有超过3次
//                        int deductNum20 = 3 - fillClockNum;//需要扣款20的次数
//                        int deductNum30 = noClockNum - 3;//需要扣款30的次数
//                        BigDecimal big20 = BigDecimal.valueOf(deductNum20).multiply(new BigDecimal("20"));
//                        BigDecimal big30 = BigDecimal.valueOf(deductNum30).multiply(new BigDecimal("30"));
//                        BigDecimal deduct = big20.add(big30);
//                        hrAttendanceStatistics.setNotClockDeduct(deduct);
//                    }
//
//                }
//                if ( noClockNum >= 6 ) {//如果缺卡6次及以上，前3次补卡不扣款，未补卡每次扣款20元；后两次每次扣款30元，第6次及以上每次扣款50元
//                    if(fillClockNum > 3){//补卡超过3次
//                        if(fillClockNum < 6){//补卡不超过6次
//                            int deductNum = fillClockNum - 3;//需要扣款30的次数
//                            BigDecimal big30 = BigDecimal.valueOf(deductNum).multiply(new BigDecimal("30"));
//                            hrAttendanceStatistics.setNotClockDeduct(big30);
//                        }else{//补卡超过6次
//                            BigDecimal big30 = new BigDecimal("60");//前3次不扣款，第4、5次，总共扣款60元
//                            int deductNum50 = fillClockNum - 5;//需要扣款50的次数
//                            BigDecimal big50 = BigDecimal.valueOf(deductNum50).multiply(new BigDecimal("50"));
//                            BigDecimal deduct = big50.add(big30);
//                            hrAttendanceStatistics.setNotClockDeduct(deduct);
//                        }
//                    }else{//补卡没有超过3次
//                        int deductNum20 = 3 - fillClockNum;//需要扣款20的次数
//                        int deductNum30 = 2;//需要扣款30的次数
//                        int deductNum50 = noClockNum - 5;//需要扣款50的次数
//                        BigDecimal big20 = BigDecimal.valueOf(deductNum20).multiply(new BigDecimal("20"));
//                        BigDecimal big30 = BigDecimal.valueOf(deductNum30).multiply(new BigDecimal("30"));
//                        BigDecimal big50 = BigDecimal.valueOf(deductNum50).multiply(new BigDecimal("50"));
//                        BigDecimal deduct = big50.add(big20.add(big30));
//                        hrAttendanceStatistics.setNotClockDeduct(deduct);
//                    }
//                }

                    //存在则更新，不存在则新增
                    HrAttendanceStatistics oldAttendanceStatistics = attendanceStatisticsMapper.selectSingleOneByCondition(empId, lastYear, lastMonth);
                    if (oldAttendanceStatistics != null) {
                        hrAttendanceStatistics.setId(oldAttendanceStatistics.getId());
                        attendanceStatisticsMapper.updateByPrimaryKeySelective(hrAttendanceStatistics);
                    } else {
                        attendanceStatisticsMapper.insert(hrAttendanceStatistics);
                    }
                    log.info("考勤汇总报表统计完成，empId:{},empName:{},dingUserId:{}",empId,empName,dingUserId);
                });
            } catch (Exception e){
                e.printStackTrace();
                HrAttendanceStatistics hrAttendanceStatistics = new HrAttendanceStatistics();
                String empName = String.valueOf(map.get("emp_name"));//姓名
                Long empId = (Long) map.get("emp_id");
                String dingUserId = String.valueOf(map.get("ding_user_id"));
                Date finalDate  = DateUtil.parse(getLastDayOfMonth(lastYear, lastMonth));
                Date quitDate = (Date) map.get("quit_date");
                if (quitDate != null) {
                    finalDate = quitDate;
                }
                hrAttendanceStatistics = setDept(map, hrAttendanceStatistics);
                hrAttendanceStatistics.setEmpId(empId);
                hrAttendanceStatistics.setEmpName(empName);
                hrAttendanceStatistics.setQuitDate(finalDate);
                hrAttendanceStatistics.setStatisticsDate(statisticsDate);
                hrAttendanceStatistics.setStatus("2");//失败
                hrAttendanceStatistics.setDelFlag("0");
                HrAttendanceStatistics oldAttendanceStatistics = attendanceStatisticsMapper.selectSingleOneByCondition(empId, lastYear, lastMonth);
                if (oldAttendanceStatistics != null) {
                    hrAttendanceStatistics.setId(oldAttendanceStatistics.getId());
                    attendanceStatisticsMapper.updateByPrimaryKeySelective(hrAttendanceStatistics);
                } else {
                    attendanceStatisticsMapper.insert(hrAttendanceStatistics);
                }
                log.error("考勤汇总报表统计异常：empId:{},empName:{},dingUserId:{},[{}]，",empId,empName,dingUserId,e);
            }
        });

//        threadPoolExecutor.shutdown();
//        while (true) {
//            if (threadPoolExecutor.isTerminated()) {
//                long endTime = System.currentTimeMillis();
//                log.info("考勤统计完成，耗时：" + (endTime - startTime));
//                break;
//            }
//        }

    }

    private HrAttendanceStatistics setDept(Map<String, Object> map, HrAttendanceStatistics hrAttendanceStatistics) {
        //部门、岗位
        Long deptId = (Long) map.get("dept_id");
        Long postId = (Long) map.get("post_id");
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
        hrAttendanceStatistics.setDeptId(deptId);
        hrAttendanceStatistics.setDeptName(deptName);
        hrAttendanceStatistics.setPostId(postId);
        hrAttendanceStatistics.setPostName(postName);
        return hrAttendanceStatistics;
    }

    public String getDingUserIdByEmpId(Long empId){
        SysUser sysUser = sysUserService.selectUserById(empId);
        return sysUser.getDingUserId();
    }
    /**
     * //1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
     * @return
     */
    private int getWeekNum(String s) {
        if(s.contains("Mon")){
            return 2;
        }if(s.contains("Tue")){
            return 3;
        }if(s.contains("Wed")){
            return 4;
        }if(s.contains("Thu")){
            return 5;
        }if(s.contains("Fri")){
            return 6;
        }if(s.contains("Sat")){
            return 7;
        }if(s.contains("Sun")){
            return 1;
        }else{
            return 1;
        }
    }

    /**
     * 获取旷工一天的标记次数
     * @return
     */
    public Integer getAbsentNum(List<String> absentList){
        Integer nums = 0;//当日旷工一天的标记次数
        if(absentList.size() < 2){
            nums = absentList.size();
        }else if(absentList.size() == 2){
            //如果当日旷工一天的标记类型一样，则证明是两个班次都有旷工
            if(absentList.get(0) == absentList.get(1)){
                nums = 2;
            }else{
                //否则证明是同一个班次的上班卡和下班卡都旷工一天了
                nums = 1;
            }
        }else if(absentList.size() > 2){
            nums = 2;
        }
        return nums;
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
     * 当前日期对应班次的每日上班时长
     * @param userId
     * @param s: 日期与星期字符串（格式为 2020-08-18:Tue）
     * @return
     */
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
     * 返回传入年月的上个月最后一天的日期
     * @param year
     * @param month
     * @return
     */
    public String getLastDayOfMonth(int year,int month){
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month);
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

    /**
     * 根据日期获取当月天数
     * @param date
     * @return
     */
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
            log.error("对象属性拷贝异常");
            e.printStackTrace();
        }
        return attendanceInfo;
    }

    public void test(){
        log.info("========================================定时任务====================================================");
        System.out.println("========================================定时任务====================================================");
    }

}
