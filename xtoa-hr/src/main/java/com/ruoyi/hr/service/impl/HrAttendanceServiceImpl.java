package com.ruoyi.hr.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.Arith;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.base.domain.AttendanceRecord;
import com.ruoyi.base.domain.DTO.AttendanceDetailDTO;
import com.ruoyi.base.domain.DTO.AttendanceExtraDTO;
import com.ruoyi.base.domain.DTO.AttendanceMonthDTO;
import com.ruoyi.base.domain.DTO.HrAttendanceDTO;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.HrLeave;
import com.ruoyi.hr.mapper.AttendanceRecordMapper;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrAttendanceMapper;
import com.ruoyi.base.domain.HrAttendance;
import com.ruoyi.hr.service.IHrAttendanceService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.ruoyi.common.utils.DateUtils.YYYY_MM;

/**
 * 考勤统计Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-06-09
 */
@Service
public class HrAttendanceServiceImpl implements IHrAttendanceService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrAttendanceServiceImpl.class);

    @Resource
    private HrAttendanceMapper hrAttendanceMapper;

    @Resource
    private HrEmpMapper hrEmpMapper;

    @Resource
    private AttendanceRecordMapper attendanceRecordMapper;


    /**
     * 查询考勤统计
     * 
     * @param id 考勤统计ID
     * @return 考勤统计
     */
    @Override
    public HrAttendance selectHrAttendanceById(Long id)
    {
        return hrAttendanceMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询考勤统计列表
     * 
     * @param hrAttendance 考勤统计
     * @return 考勤统计
     */
    @Override
    public List<HrAttendance> selectHrAttendanceList(HrAttendance hrAttendance)
    {
        hrAttendance.setDelFlag("0");
        return hrAttendanceMapper.selectHrAttendanceList(hrAttendance);
    }

    /**
     * 新增考勤统计
     * 
     * @param hrAttendance 考勤统计
     * @return 结果
     */
    @Override
    public int insertHrAttendance(HrAttendance hrAttendance)
    {
        hrAttendance.setCreateId(ShiroUtils.getUserId());
        hrAttendance.setCreateBy(ShiroUtils.getLoginName());
        hrAttendance.setCreateTime(DateUtils.getNowDate());
        return hrAttendanceMapper.insertSelective(hrAttendance);
    }

    /**
     * 每日考勤数据（使用若依封装的excelUtil操作，不能获取复杂表头数据列）
     *
     * @param hrAttendanceDTO 每日考勤统计
     * @return 结果
     */
    @Override
    @Transactional
    public int insertHrAttendanceDTO(HrAttendanceDTO hrAttendanceDTO) throws ParseException {
        int i = 0;
        String empNum = hrAttendanceDTO.getEmpNum();
        if(StringUtils.isNotBlank(empNum)){
            HrEmp hrEmp = hrEmpMapper.selectHrEmpByEmpNum(empNum);
            if(hrEmp != null){
                AttendanceRecord attendanceRecord = new AttendanceRecord();
                String startTime = hrAttendanceDTO.getStartTime();
                String endTime = hrAttendanceDTO.getEndTime();
                attendanceRecord.setEmpId(hrEmp.getEmpId());
                attendanceRecord.setEmpNum(hrAttendanceDTO.getEmpNum());
                attendanceRecord.setEmpName(hrEmp.getEmpName());
                attendanceRecord.setStartTime(startTime);
                attendanceRecord.setEndTime(endTime);
                String dates = hrAttendanceDTO.getDates().substring(0, 8);
                Date date = DateUtils.parseDate(dates, "yy-MM-dd");
                attendanceRecord.setDates(date);

                //夜班补贴，餐补
                if(startTime != null && startTime != ""){
                    //startTime的格式为‘09:02’或‘次日 06:00’这种需要转换
                    String[] s1 = startTime.split(":");
                    String sub1= s1[0].substring(s1[0].length() - 2, s1[0].length());
                    int t1 = Integer.parseInt(sub1);
                    //如果上班打卡时间大于21点 则判断为夜班
                    if(t1 >= 21){
                        //设置每个夜班的补贴
//                        attendanceRecord.setNightSubsidy(0.0);
                    }
                    //否则为白班
                    else{
                        //如果白班的下班时间大于21点 则有餐补20
                        if(endTime != null && endTime != ""){
                            String[] s2 = endTime.split(":");
                            String sub2= s2[0].substring(s2[0].length() - 2, s2[0].length());
                            int t2 = Integer.parseInt(sub2);
                            if(t2 >= 21){
                                attendanceRecord.setMealSubsidy(20.0);
                            }
                        }

                    }
                }

                //获取班次信息，如果是休息日，每日统计excel中当日的班次是“休息”
                String classes = hrAttendanceDTO.getClasses();
                if(StringUtils.isNotBlank(classes)){
                    attendanceRecord.setClasses(classes);
                    if("休息".equals(classes)){
                        attendanceRecord.setType("1");
                    }else{
                        attendanceRecord.setType("0");
                    }
                }

                //迟到、早退、上下班缺卡
                Long late = hrAttendanceDTO.getLate();
                Long lateTimes = hrAttendanceDTO.getLateTimes();
                Long early = hrAttendanceDTO.getEarly();
                Long earlyTimes = hrAttendanceDTO.getEarlyTimes();
                Long noClockIn = hrAttendanceDTO.getNoClockIn();
                Long noClockOff = hrAttendanceDTO.getNoClockOff();
                //查询迟到早退次数
                Long lateNums = attendanceRecordMapper.selectGeneralLateCount(empNum);
                Long earlyNums = attendanceRecordMapper.selectGeneralEarlyCount(empNum);
                //迟到扣费规则
                if(late!=null && lateTimes!=null){
                    attendanceRecord.setLate(late);
                    attendanceRecord.setLateTimes(lateTimes);
                    if(lateTimes>10 && lateTimes<=20){
                        attendanceRecord.setLateFines(50d);
                    }
                    if(lateTimes>20 && lateTimes<=29){
                        attendanceRecord.setLateFines(80d);
                    }
                    if(lateNums < 3 && lateTimes<=10){
                        attendanceRecord.setLateNum(lateNums++);
                    }
                    if(lateNums>=3){
                        attendanceRecord.setLateNum(lateNums++);
                        attendanceRecord.setLateFines(20d);
                    }
                }
                //早退扣费规则
                if(early!=null && earlyTimes!=null){
                    attendanceRecord.setEarly(early);
                    attendanceRecord.setEarlyTimes(earlyTimes);
                    if(earlyTimes>10 && earlyTimes<=20){
                        attendanceRecord.setLateFines(50d);
                    }
                    if(earlyTimes>20 && earlyTimes<=29){
                        attendanceRecord.setLateFines(80d);
                    }
                    if(earlyNums < 3 && earlyTimes<=10){
                        attendanceRecord.setLateNum(earlyNums++);
                    }
                    if(earlyNums>=3){
                        attendanceRecord.setLateNum(earlyNums++);
                        attendanceRecord.setLateFines(20d);
                    }

                }
                if(noClockIn!=null){
                    attendanceRecord.setNoClockIn(noClockIn);
                }
                if(noClockOff!=null){
                    attendanceRecord.setNoClockOff(noClockOff);
                }
                attendanceRecord.setCreateId(ShiroUtils.getUserId());
                attendanceRecord.setCreateBy(ShiroUtils.getLoginName());
                attendanceRecord.setCreateTime(DateUtils.getNowDate());
                attendanceRecord.setDelFlag("0");

                //存在则更新，不存在则新增
                AttendanceRecord oldRecord = attendanceRecordMapper.selectRecordByCondition(empNum,date);
                if ( oldRecord == null) {
                    i = attendanceRecordMapper.insert(attendanceRecord);
                } else {
                    attendanceRecord.setId(oldRecord.getId());
                    i = attendanceRecordMapper.updateByPrimaryKeySelective(attendanceRecord);
                }
            }
        }
        return i;
    }


    /**
     * 月度汇总数据(此方法使用alibaba的easyExcel操作，可以获取复杂表头的数据列)
     *
     * @param attendanceDetailDTO 月度汇总数据
     * @param head 月度汇总数据表头
     * @return void
     */
    @Override
    @Transactional
    public void insertAttendanceDetail(AttendanceDetailDTO attendanceDetailDTO,String head) throws ParseException {
        int i = 0;

        //获取表头的日期
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String s1 = head.substring(0, head.indexOf("："));
        String date = head.substring(s1.length() + 1, s1.length() + 11);
        LocalDate date1 = LocalDate.parse(date, fmt);
        Long year = Long.valueOf(date1.getYear());
        Long month = Long.valueOf(date1.getMonthValue());
        LocalDate last = date1.with(TemporalAdjusters.lastDayOfMonth());
        String date2 = last.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Date date3 = DateUtils.parseDate(date2, "yyyy-MM-dd");

        String empNum = attendanceDetailDTO.getEmpNum();
        if(StringUtils.isNotBlank(empNum)){
            HrEmp hrEmp = hrEmpMapper.selectHrEmpByEmpNum(empNum);
            if(hrEmp != null){
                HrAttendance hrAttendance = new HrAttendance();
                Date nonManagerDate = hrEmp.getNonManagerDate();
                hrAttendance.setNonManagerDate(nonManagerDate);
                hrAttendance.setFinalDate(date3);
                Date quitDate = hrEmp.getQuitDate();
                if ( quitDate != null ) {
                    hrAttendance.setQuitDate(quitDate);
                }
                hrAttendance.setCreateId(ShiroUtils.getUserId());
                hrAttendance.setCreateBy(ShiroUtils.getLoginName());
                hrAttendance.setCreateTime(DateUtils.getNowDate());
                hrAttendance.setPostName(attendanceDetailDTO.getPostName());
                hrAttendance.setEmpId(hrEmp.getEmpId());
                hrAttendance.setEmpNum(attendanceDetailDTO.getEmpNum());
                hrAttendance.setEmpName(hrEmp.getEmpName());

                //计算工龄
                LocalDate today = LocalDate.now();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String format = sdf.format(nonManagerDate);
                hrAttendance.setWorkingAge(Arith.sub(today.getYear(),Double.parseDouble(format)));

                //考勤组
                String group = attendanceDetailDTO.getGroup();

                //部门层级
                String deptName = attendanceDetailDTO.getDeptName();
                String firstDeptName = getFirstDeptName(deptName);
                String secondDeptName = getSecondDeptName(deptName);
                if(StringUtils.isNotBlank(firstDeptName)){
                    hrAttendance.setFirstDeptName(firstDeptName);
                }
                if(StringUtils.isNotBlank(secondDeptName)){
                    hrAttendance.setSecondDeptName(secondDeptName);
                }

                //区域
                hrAttendance.setDistrict(getDistrictByGroupAndDept(group,deptName));

                //计算出勤、公休等
                Map<String, Object> map = attendanceRecordMapper.selectShouldAttendance(hrEmp.getEmpId());
//                Date finalDate = (Date) map.get("dates");
                Long days1 = (Long) map.get("workDays");
                Long days2 = (Long) map.get("restDays");
                Long workDays = attendanceDetailDTO.getWorkDays();
                Long restDays = attendanceDetailDTO.getRestDays();
                Long absentDays = attendanceDetailDTO.getAbsentDays();
                if(workDays!=null){
                    //实出勤
                    hrAttendance.setActualAttendanceDay(Double.valueOf(workDays));
                    hrAttendance.setActualAttendance(Arith.mul(7.5,workDays));
                    hrAttendance.setPayWorkHour(Arith.mul(7.5,workDays));
                }
                if(restDays!=null){
                    //实公休
                    hrAttendance.setActualPublicHolidayDay(Double.valueOf(restDays));
                    double mul = Arith.mul(7.5, restDays);
                    hrAttendance.setActualPublicHoliday(mul);
                    hrAttendance.setLegalPublicHoliday(mul);
                }
                if(absentDays!=null){
                    //旷工
                    hrAttendance.setAbsentLeave(Double.valueOf(absentDays));
                }
                hrAttendance.setShouldAttendance(Arith.mul(7.5,days1));
                hrAttendance.setShouldPublicHoliday(Arith.mul(7.5,days2));

                //获取请假数据
                Double annualLeave = attendanceDetailDTO.getAnnualLeave();
                Double personalLeave = attendanceDetailDTO.getPersonalLeave();
                Double sickLeave = attendanceDetailDTO.getSickLeave();
                Double lieuLeave = attendanceDetailDTO.getLieuLeave();
                Double maternityLeave = attendanceDetailDTO.getMaternityLeave();
                Double paternityLeave = attendanceDetailDTO.getPaternityLeave();
                Double marriageLeave = attendanceDetailDTO.getMarriageLeave();
                Double girlsLeave = attendanceDetailDTO.getGirlsLeave();
                Double funeralLeave = attendanceDetailDTO.getFuneralLeave();
                Double breastfeedingLeave = attendanceDetailDTO.getBreastfeedingLeave();
                Double otherLeave = 0d;
                if (annualLeave != null) {
                    hrAttendance.setAnnualLeave(annualLeave);
                }
                if (personalLeave != null) {
                    hrAttendance.setPersonalLeave(personalLeave);
                }
                if (sickLeave != null) {
                    hrAttendance.setSickLeave(sickLeave);
                }
                if (lieuLeave != null) {
                    hrAttendance.setLieuLeave(lieuLeave);
                }
                if (maternityLeave!=null) {
                    otherLeave += maternityLeave;
                }
                if (paternityLeave!=null) {
                    otherLeave += paternityLeave;
                }
                if (marriageLeave!=null) {
                    otherLeave += marriageLeave;
                }
                if (girlsLeave!=null) {
                    otherLeave += girlsLeave;
                }
                if (funeralLeave!=null) {
                    otherLeave += funeralLeave;
                }
                if (breastfeedingLeave!=null) {
                    otherLeave += breastfeedingLeave;
                }
                hrAttendance.setOtherLeave(otherLeave);

                //获取加班数据
                Double workLieu = attendanceDetailDTO.getWorkLieu();
                Double restLieu = attendanceDetailDTO.getRestLieu();
                Double holidayLieu = attendanceDetailDTO.getHolidayLieu();
                Double generalOvertime = 0d;
                Double overtimes = 0d;
                if (holidayLieu!=null){
                    //节假日（转调休）
                    hrAttendance.setLegalOvertime(holidayLieu);
                    overtimes += holidayLieu;
                }
                //平时加班
                if (workLieu!=null){
                    //工作日（转调休）
                    generalOvertime += workLieu;
                    overtimes += workLieu;
                }
                if (restLieu!=null){
                    //休息日（转调休）
                    generalOvertime += restLieu;
                    overtimes += restLieu;
                }
                //平时加班
                hrAttendance.setGeneralOvertime(generalOvertime);
                //本月存休
                hrAttendance.setOvertimeHoliday(overtimes);
                //第一次导入月度统计考勤数据时 本月存休 = 本月累计余假
                hrAttendance.setAllHoliday(overtimes);

                //补卡/未打卡扣费(超卡超过三次或未补卡这两个字段就应该有记录)
                Long toWorkNums = attendanceDetailDTO.getToWorkNums();
                Long offWorkNums = attendanceDetailDTO.getOffWorkNums();
                if ( toWorkNums == null ) {
                    toWorkNums = 0l;
                }
                if ( offWorkNums == null ) {
                    offWorkNums = 0l;
                }
                Long num = toWorkNums + offWorkNums;
                if ( num <=3 ){
                    hrAttendance.setNotClockDeduct(Arith.mul(num,20));
                }
                if ( num == 4 || num == 5 ) {
                    hrAttendance.setNotClockDeduct(Arith.mul(num-3,30));
                }
                if ( num >= 6 ) {
                    Double deduct1 = Arith.mul(2,30);//超过6次要加上第4、5次的扣费
                    Double deduct2 = Arith.mul(num-5,50);
                    hrAttendance.setNotClockDeduct(Arith.add(deduct1,deduct2));
                }

                //获取迟到早退扣费,补贴金额
//                Long month = attendanceRecordMapper.selectMonth(empNum);
                Map<String,Object> finesMap = attendanceRecordMapper.selectFinesAndSubsidy(empNum,year,month);
                Double earlyFines = (Double) finesMap.get("earlyFines");
                Double lateFines = (Double) finesMap.get("lateFines");
                Double nightSubsidy = (Double) finesMap.get("nightSubsidy");
                Double mealSubsidy = (Double) finesMap.get("mealSubsidy");
                hrAttendance.setEarlyDeduct(earlyFines);
                hrAttendance.setLateDeduct(lateFines);
                hrAttendance.setMealSubsidy(mealSubsidy);
                hrAttendance.setNightSubsidy(nightSubsidy);
                hrAttendance.setDelFlag("0");

                //全勤奖规则
                Double shouldAttendance = hrAttendance.getShouldAttendance();
                Double actualAttendance = hrAttendance.getActualAttendance();
                Long lateNums = attendanceDetailDTO.getLateNums();
                if (lateNums != null){
                    hrAttendance.setLateNums(lateNums);
                }

                Long leaveNums = attendanceRecordMapper.selectLeaveNums(empNum,year,month);
                Long notClockInNums = attendanceRecordMapper.selectNotClockInNums(empNum,year,month);
                Long fillClockInNums = attendanceRecordMapper.selectFillClockInNums(empNum,year,month);
                String classes = attendanceRecordMapper.selectRepositoryClasses(empNum,year,month);
                //如果是仓库考勤，全勤计算规则不一样
                if(StringUtils.isNotBlank(classes) && classes.contains("仓库考勤")){
                    //仓库大于208H才算满勤，没有请假（年假、调休除外）、迟到、漏打卡、补卡不超过三次才有全勤
                    if(actualAttendance != null && actualAttendance > 208.0){
                        if (lateNums == null && notClockInNums == 0 && fillClockInNums <= 3 && leaveNums == 0){
                            hrAttendance.setAttendanceBonus(100.0);
                        }
                    }
                }else{
                    //一般员工满勤没有请假（年假、调休除外）、迟到、漏打卡、补卡不超过三次才有全勤
                    if(shouldAttendance!=null && actualAttendance!=null && (shouldAttendance <= actualAttendance) ){
                        if (lateNums == null && notClockInNums == 0 && fillClockInNums <= 3 && leaveNums == 0){
                            hrAttendance.setAttendanceBonus(100.0);
                        }
                    }
                }

                //存在则更新，不存在则新增
                HrAttendance oldAttendance = hrAttendanceMapper.selectOldAttendanceByCondition(empNum,year,month);
                if ( oldAttendance == null) {
                    hrAttendanceMapper.insert(hrAttendance);
                } else {
                    hrAttendance.setId(oldAttendance.getId());
                    hrAttendanceMapper.updateByPrimaryKeySelective(hrAttendance);
                }
            }
        }
    }

    private boolean isDistrict(String deptName){
        if (deptName.contains("集团总部") || deptName.contains("深圳快马") || deptName.contains("东莞快马")
                || deptName.contains("中珠快马") || deptName.contains("中山快马") || deptName.contains("广佛快马")
                || deptName.contains("广州快马") || deptName.contains("武汉分公司") || deptName.contains("福建分公司")){
            return true;
        }
        return false;
    }


    private String getFirstDeptName(String deptName) {
        String[] deptNames = deptName.split("-");
        if(deptNames != null){
            if(deptNames.length==1){
                if(isDistrict(deptNames[0])){
                    return "";
                }else{
                    return deptNames[0];
                }
            }
            if(deptNames.length==2){
                if(isDistrict(deptNames[0])){
                    return deptNames[1];
                }else{
                    if(isDistrict(deptNames[1])){
                        return "";
                    }else{
                        return deptNames[0];
                    }
                }

            }
            if(deptNames.length>2){
                if(isDistrict(deptNames[0])){
                    return deptNames[1];
                }else{
                    if(isDistrict(deptNames[1])){
                        return deptNames[2];
                    }else{
                        return deptNames[0];
                    }
                }

            }
        }
        return "";
    }

    private String getSecondDeptName(String deptName) {
        String[] deptNames = deptName.split("-");
        if(deptNames != null){
            if(deptNames.length==1){
                return "";
            }
            if(deptNames.length==2){
                if(isDistrict(deptNames[0])){
                    return "";
                }else{
                    if(isDistrict(deptNames[1])){
                        return "";
                    }else{
                        return deptNames[1];
                    }
                }

            }
            if(deptNames.length>2){
                if(isDistrict(deptNames[0])){
                    return deptNames[2];
                }else{
                    if(isDistrict(deptNames[1])){
                        if(deptNames.length==3){
                            return "";
                        }else{
                            return deptNames[3];
                        }
                    }else{
                        if(deptNames.length==3){
                            return "";
                        }else{
                            return deptNames[3];
                        }
                    }
                }
            }
        }
        return "";
    }

    private String getDistrictByGroupAndDept(String group,String deptName) {
        if (deptName.contains("集团总部")){
            return "集团总部";
        }
        else if (deptName.contains("深圳快马")){
            return "深圳快马";
        }
        else if (deptName.contains("东莞快马")){
            return "东莞快马";
        }
        else if (deptName.contains("中珠快马") || deptName.contains("中山快马") ){
            return "中珠快马";
        }
        else if (deptName.contains("广佛快马") || deptName.contains("广州快马")){
            return "广佛快马";
        }
        else if (deptName.contains("武汉分公司")){
            return "武汉分公司";
        }
        else if (deptName.contains("福建分公司")){
            return "福建分公司";
        }
        else {
            if (group.contains("（深圳）办公室考勤")){
                return "集团总部";
            }
            else if (group.contains("深圳") && !group.contains("（深圳）办公室考勤")){
                return "深圳快马";
            }
            else if (group.contains("东莞")){
                return "东莞快马";
            }
            else if (group.contains("中山") || group.contains("珠海")){
                return "中珠快马";
            }
            else if (group.contains("广州") || group.contains("佛山")){
                return "广佛快马";
            }
            else if (group.contains("武汉")){
                return "武汉分公司";
            }
            else if (group.contains("福建")){
                return "福建分公司";
            }
            else {
                return "";
            }
        }
    }

    /**
     * 考勤假期、工作制、夜班补贴等数据（使用alibaba的easyExcel获取复杂表头数据列）
     *
     * @param attendanceExtraDTO 考勤假期、工作制、夜班补贴等数据
     * @return void
     */
    @Override
    @Transactional
    public void insertAttendanceExtra(AttendanceExtraDTO attendanceExtraDTO,String head) {
        //获取表头的日期
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String s1 = head.substring(0, head.indexOf("："));
        String date = head.substring(s1.length() + 1, s1.length() + 11);
        LocalDate date1 = LocalDate.parse(date, fmt);
        Long year = Long.valueOf(date1.getYear());
        Long month = Long.valueOf(date1.getMonthValue());

        String empNum = attendanceExtraDTO.getEmpNum();
        if(StringUtils.isNotBlank(empNum)){
            HrAttendance oldAttendance = hrAttendanceMapper.selectOldAttendanceByCondition(empNum,year, month);
            Double workDuration = attendanceExtraDTO.getWorkDuration();

            if (oldAttendance != null) {
                HrAttendance hrAttendance = new HrAttendance();
                //上班时长
                if (workDuration != null) {
                    hrAttendance.setWorkDuration(workDuration);
                    HrEmp hrEmp = hrEmpMapper.selectHrEmpByEmpNum(empNum);
                    if (hrEmp != null) {
                        //按8小时工作制计算出勤、公休等
                        Map<String, Object> map = attendanceRecordMapper.selectShouldAttendance(hrEmp.getEmpId());
                        Date finalDate = (Date) map.get("dates");
                        Long days1 = (Long) map.get("workDays");
                        Long days2 = (Long) map.get("restDays");
                        //实出勤
                        Double actualAttendanceDay = oldAttendance.getActualAttendanceDay();
                        if (actualAttendanceDay != null) {
                            hrAttendance.setActualAttendance(Arith.mul(8, actualAttendanceDay));
                            hrAttendance.setPayWorkHour(Arith.mul(8, actualAttendanceDay));
                        }
                        //实公休
                        Double actualPublicHolidayDay = oldAttendance.getActualPublicHolidayDay();
                        if (actualPublicHolidayDay != null) {
                            hrAttendance.setActualPublicHoliday(Arith.mul(8, actualPublicHolidayDay));
                            hrAttendance.setLegalPublicHoliday(Arith.mul(8, actualPublicHolidayDay));
                        }

                        hrAttendance.setShouldAttendance(Arith.mul(8, days1));
                        hrAttendance.setShouldPublicHoliday(Arith.mul(8, days2));

                        //全勤奖规则
                        Double shouldAttendance = hrAttendance.getShouldAttendance();
                        Double actualAttendance = hrAttendance.getActualAttendance();
                        Long lateNums = hrAttendance.getLateNums();
                        Long leaveNums = attendanceRecordMapper.selectLeaveNums(empNum,year,month);
                        Long notClockInNums = attendanceRecordMapper.selectNotClockInNums(empNum,year,month);
                        Long fillClockInNums = attendanceRecordMapper.selectFillClockInNums(empNum,year,month);
                        String classes = attendanceRecordMapper.selectRepositoryClasses(empNum,year,month);

                        //如果是仓库考勤，全勤计算规则不一样
                        if(StringUtils.isNotBlank(classes) && classes.contains("仓库考勤")){
                            //仓库大于208H才算满勤，没有迟到、漏打卡、补卡不超过三次才有全勤
                            if(actualAttendance > 208.0){
                                if (lateNums == null && fillClockInNums == 0 && notClockInNums <= 3 && leaveNums == 0){
                                    hrAttendance.setAttendanceBonus(100.0);
                                }
                            }
                        }else{
                            //一般员工满勤没有迟到、漏打卡、补卡不超过三次才有全勤
                            if(shouldAttendance!=null && actualAttendance!=null && (shouldAttendance <= actualAttendance) ){
                                if (lateNums == null && fillClockInNums == 0 && notClockInNums <= 3 && leaveNums == 0){
                                    hrAttendance.setAttendanceBonus(100.0);
                                }
                            }
                        }

                    }
                }

                //上月累计余假
                Double previousHoliday = attendanceExtraDTO.getPreviousHoliday();
                Double overAnnualLeave = attendanceExtraDTO.getOverAnnualLeave();
                //本月存休
                Double overtimeHoliday = oldAttendance.getOvertimeHoliday();
                if (overtimeHoliday != null) {
                    hrAttendance.setOvertimeHoliday(overtimeHoliday);
                }
                if (previousHoliday != null) {
                    //如果上月累计余假不为空
                    hrAttendance.setPreviousHoliday(previousHoliday);
                    if (overtimeHoliday != null) {
                        //如果本月有调休则 本月累计余假 = 上月累计余假 + 本月存休
                        hrAttendance.setAllHoliday(Arith.add(previousHoliday, overtimeHoliday));
                    } else {
                        //如果本月调休为空则 本月累计余假=上月累计余假
                        hrAttendance.setAllHoliday(previousHoliday);
                    }
                } else {
                    //如果上月累计余假为空
                    if (overtimeHoliday != null) {
                        //如果本月有调休则 本月累计余假 = 本月存休
                        hrAttendance.setAllHoliday(overtimeHoliday);
                    }
                }
                //年假剩余
                if (overAnnualLeave != null) {
                    //本月使用的年假
                    Double annualLeave = oldAttendance.getAnnualLeave();
                    if(annualLeave!=null){
                        //如果本月有使用年假，则 本月剩余年假=上月剩余年假 - 本月使用的年假
                        double annualLeaveHours = Arith.mul(annualLeave, 7.5);
                        hrAttendance.setOverAnnualLeave(Arith.sub(overAnnualLeave,annualLeaveHours));
                    }else{
                        //如果本月没有使用年假，则 本月剩余年假=上月剩余年假
                        hrAttendance.setOverAnnualLeave(overAnnualLeave);
                    }
                }

                //夜班补贴
                Double nightSubsidy = attendanceExtraDTO.getNightSubsidy();
                if (nightSubsidy != null) {
                    hrAttendance.setNightSubsidy(nightSubsidy);
                }
                //存在则更新，不存在则新增
                if (oldAttendance == null) {
                    hrAttendanceMapper.insert(hrAttendance);
                } else {
                    hrAttendance.setId(oldAttendance.getId());
                    hrAttendanceMapper.updateByPrimaryKeySelective(hrAttendance);
                }
            }
        }
    }



    /**
     * 修改考勤统计
     * 
     * @param hrAttendance 考勤统计
     * @return 结果
     */
    @Override
    public int updateHrAttendance(HrAttendance hrAttendance)
    {
        hrAttendance.setUpdateId(ShiroUtils.getUserId());
        hrAttendance.setUpdateBy(ShiroUtils.getLoginName());
        hrAttendance.setUpdateTime(DateUtils.getNowDate());
        return hrAttendanceMapper.updateByPrimaryKeySelective(hrAttendance);
    }

    /**
     * 删除考勤统计对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceByIds(String ids)
    {
        return hrAttendanceMapper.deleteHrAttendanceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除考勤统计信息
     * 
     * @param id 考勤统计ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceById(Long id)
    {
        return hrAttendanceMapper.deleteHrAttendanceById(id);
    }

    @Override
    public int selectHrAttendanceCount(HrAttendance attendance) {
        return hrAttendanceMapper.selectCount(attendance);
    }
}
