package com.ruoyi.hr.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.ruoyi.base.domain.DTO.DateOperation;
import com.ruoyi.base.domain.Holiday;
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.dingTalk.DingAttendanceApi;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.enums.SysConfigEnum;
import com.ruoyi.common.utils.data.service.ITDateService;
import com.ruoyi.hr.service.*;
import com.ruoyi.hr.utils.AttendanceUtil;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrAttendanceInfoMapper;
import com.ruoyi.base.domain.HrAttendanceInfo;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;

/**
 * 个人考勤记录Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-07-13
 */
@Service
public class HrAttendanceInfoServiceImpl implements IHrAttendanceInfoService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrAttendanceInfoServiceImpl.class);

    @Resource
    private HrAttendanceInfoMapper hrAttendanceInfoMapper;

    @Autowired
    private IHrAttendanceGroupService hrAttendanceGroupService;

    @Autowired
    private IHrAttendanceClassService hrAttendanceClassService;

    @Autowired
    private DingAttendanceApi dingAttendanceApi;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private AttendanceUtil attendanceUtil;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IHrOvertimeService overtimeService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IHolidayService holidayService;

    @Autowired
    private ITDateService dateService;


    /**
     * 查询个人考勤记录
     * 
     * @param id 个人考勤记录ID
     * @return 个人考勤记录
     */
    @Override
    public HrAttendanceInfo selectHrAttendanceInfoById(Long id)
    {
        return hrAttendanceInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询我的考勤记录列表
     * 
     * @param hrAttendanceInfo 我的考勤记录
     * @return 我的考勤记录
     */
    @Override
    public List<HrAttendanceInfo> selectHrAttendanceInfoList(HrAttendanceInfo hrAttendanceInfo)
    {
        hrAttendanceInfo.setDelFlag("0");
        List<HrAttendanceInfo> list = hrAttendanceInfoMapper.selectHrAttendanceInfoAllList(hrAttendanceInfo);
//        list.stream().map(info -> {
//            if(info.getBaseCheckTime() == null){
//                HrAttendanceClass attendanceClass = hrAttendanceClassService.selectHrAttendanceClassById(info.getClassId());
//                if("OnDuty".equals(info.getCheckType())){
//                    info.setPlanCheckTime(attendanceClass.getWorkTime());
//                }else{
//                    info.setPlanCheckTime(attendanceClass.getClosingTime());
//                }
//                return info;
//            }
//            info.setPlanCheckTime(DateUtil.format(info.getBaseCheckTime(),"hh:mm"));
//            return info;
//        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 查询员工考勤记录列表
     *
     * @param hrAttendanceInfo 员工考勤记录
     * @return 员工考勤记录
     */
    @Override
    @DataScope(deptAlias = "d", menuAlias = "hr:attendanceInfoAll:view")
    public List<HrAttendanceInfo> selectHrAttendanceInfoAllList(HrAttendanceInfo hrAttendanceInfo)
    {
        hrAttendanceInfo.setDelFlag("0");
        List<HrAttendanceInfo> list = hrAttendanceInfoMapper.selectHrAttendanceInfoAllList(hrAttendanceInfo);
//        list.stream().map(info -> {
//            if(info.getBaseCheckTime() == null){
//                HrAttendanceClass attendanceClass = hrAttendanceClassService.selectHrAttendanceClassById(info.getClassId());
//                if("OnDuty".equals(info.getCheckType())){
//                    info.setPlanCheckTime(attendanceClass.getWorkTime());
//                }else{
//                    info.setPlanCheckTime(attendanceClass.getClosingTime());
//                }
//                return info;
//            }
//            info.setPlanCheckTime(DateUtil.format(info.getBaseCheckTime(),"hh:mm"));
//            return info;
//        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 从钉钉获取考勤数据并插入到本地表
     * 以前的逻辑会根据dingUserId来查找有没有重复数据，可能导致该员工有一个钉钉考勤数据，一条OA打卡数据，现改为只按员工id和打卡日期，打卡类型判断
     * @param dingUserId 钉钉userid
     * */
    @Transactional
    public void saveAttendanceByDingAPI(String dingUserId){
        SysUser sysUser = hrEmpService.getSysUserByDingUserId(dingUserId, false);
        Assert.notNull(sysUser,"找不到dingUserId对应的用户数据。dingUserId: " + dingUserId);
        HrEmp emp = hrEmpService.selectTHrEmpById(sysUser.getUserId());
        Assert.notNull(emp,"无法根据系统用户id找到员工id。userId: " + sysUser.getUserId());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime beforeDay = today.plusMinutes(-30);
        String dateTo = dtf.format(today);
        String dateForm = dtf.format(beforeDay);
        String workDate = dateForm.substring(0,10);
        OapiAttendanceListRecordResponse response = dingAttendanceApi.getAttendance(dingUserId, dateForm, dateTo);
        List<OapiAttendanceListRecordResponse.Recordresult> list = response.getRecordresult();

        try {
            if(CollectionUtils.isNotEmpty(list)){
                list.forEach(recordresult -> {
                    HrAttendanceInfo attendanceInfo = convert(recordresult);
                    attendanceInfo.setDelFlag("0");
                    attendanceInfo.setRemark("钉钉考勤数据");
                    attendanceInfo.setDingUserId(dingUserId);
                    attendanceInfo.setEmpId(emp.getEmpId());
                    attendanceInfo.setEmpName(emp.getEmpName());
                    attendanceInfo.setDeptId(emp.getDeptId());
                    attendanceInfo.setPostId(emp.getPostId());
                    attendanceInfo.setWorkDate(workDate);
                    //这里不设置ID，则会存入钉钉传过来的ID
                    attendanceInfo.setId(null);
                    Example example = new Example(HrAttendanceInfo.class);
                    example.createCriteria().andEqualTo("delFlag","0")
                            .andEqualTo("empId",emp.getEmpId())
                            .andEqualTo("workDate",workDate)
                            .andEqualTo("checkType",attendanceInfo.getCheckType());
                    HrAttendanceInfo info = hrAttendanceInfoMapper.selectSingleOneByExample(example);
                    if(info == null){
                        hrAttendanceInfoMapper.insert(attendanceInfo);
                        logger.info("员工打卡数据回调存储成功，员工姓名:{}",emp.getEmpName());
                    }else {
                        attendanceInfo.setId(info.getId());
                        hrAttendanceInfoMapper.updateByPrimaryKey(attendanceInfo);
                        logger.info("员工打卡数据回调存储成功，员工姓名:{}",emp.getEmpName());
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("员工打卡数据回调存储失败，员工姓名:{},钉钉userId:{}",emp.getEmpName(),dingUserId);
        }
    }

    /**
     * 将数据接受对象的属性拷贝到对应的实体类
     * */
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
    
    /**
     * 新增个人考勤记录
     * 
     * @param hrAttendanceInfo 个人考勤记录
     * @return 结果
     */
    @Override
    public int insertHrAttendanceInfo(HrAttendanceInfo hrAttendanceInfo)
    {
        if (hrAttendanceInfo.getCreateId() == null){
            hrAttendanceInfo.setCreateId(ShiroUtils.getUserId());
        }
        if (StringUtil.isEmpty(hrAttendanceInfo.getCreateBy())){
            hrAttendanceInfo.setCreateBy(ShiroUtils.getLoginName());
        }

        return hrAttendanceInfoMapper.insertSelective(hrAttendanceInfo);
    }

    /**
     * 判断该员工前一天的班次是否完成
     *      此时前一天已经确认为有班次且为非正常班次，所以才需要判断当前时间是否在前一天的班次时间内
     *      如果当前时间还在前一天的班次时间内，说明前一天的班次还未完成，否则前一天的班次已完成
     * @param classId
     * @param yesterday
     * @param today
     * @return
     */
    private Boolean classIsComplete(Long classId, String yesterday, String today) {
        HrAttendanceClass hrAttendanceClass = hrAttendanceClassService.selectHrAttendanceClassById(classId);
        String workTime = hrAttendanceClass.getWorkTime();
        String closingTime = hrAttendanceClass.getClosingTime();
        String elasticityFlag = hrAttendanceClass.getElasticityFlag();//是否弹性制
        String workTimeStr = yesterday + " " + workTime + ":00";//前一天班次上班时间
        String closingTimeStr = today + " " + closingTime + ":00";//前一天班次下班时间
        Date beginDate = DateUtils.parseDate(workTimeStr);
        Date endDate = DateUtils.parseDate(closingTimeStr);
        //当前日期时间与前一天下班打卡时间6小时后的时间比较
        int num = DateUtil.compare(DateUtils.getNowDate(), DateUtil.offsetHour(endDate,6));
        if(num <= 0){
            //当前时间比前一天的下班时间小，说明在前一天的下班时间之前，则前一天的班次还未完成
            return false;
        }
        return true;
    }

    /**
     * 判断该员工昨天或当天的班次是否正常，正常班次返回null，非正常班次返回班次id
     * @param userId
     * @param workday（workday的选项为yesterday或today）
     * @return
     */
    private Long dayIsNormalClass(Long userId, String workday) {
        Map<String, Object> groupMap = hrAttendanceGroupService.selectGruopByEmpIdFromApi(userId);
        if(groupMap == null || groupMap.isEmpty()){
            logger.error("根据员工ID查询考勤组班次信息，groupMap : {}" + groupMap);
            throw new RuntimeException("员工不在考勤组，请联系人事相关人员！");
        }
        String type = (String) groupMap.get("type");
        Long groupId = (Long) groupMap.get("group_id") ;
        //如果是排班制
        if(groupId != null && StringUtils.isNotBlank(type) && "1".equals(type)){
            Map<String, Object> map = hrAttendanceGroupService.selectScheduGroupAndClass(groupId,userId,workday);
            if(map != null && !map.isEmpty()){
                Double hours = Double.valueOf((String) map.get("hours"));
                //当日班次上班时长为0或空，则判定为公休日
                if(hours == null || hours == 0d){
                    return null;
                }
                String elasticityFlag = (String) map.get("elasticity_flag");//是否弹性制
                String workTime = (String) map.get("work_time");//班次上班打卡时间
                Integer standardWorkMinute = Integer.parseInt(workTime.split(":")[0])*60 + Integer.parseInt(workTime.split(":")[1]);
                String closingTime = (String) map.get("closing_time");//班次下班打卡时间
                Integer standardClosingMinute = Integer.parseInt(closingTime.split(":")[0])*60 + Integer.parseInt(closingTime.split(":")[1]);
                //上班打卡时间转化的分钟数 > 下班打卡时间转化的分钟数，则为非正常的跨天班次
                if(standardWorkMinute > standardClosingMinute){
                    Long classId = (Long) map.get("class_id");
                    return classId;
                }
            }
            return null;
        }

        //如果是固定班制
        String mustDate = hrAttendanceGroupService.selectDayIsMustDate(userId, workday);
        if(StringUtils.isNotBlank(mustDate)){
            //如果前一天属于必须打卡的日期，那么要按照必须打卡日期的班次来判断
            String[] mustDates = mustDate.split(",");
            for(String s : mustDates){
                if (s.contains(workday)) {
                    String classId = s.split(":")[1];//班次id
                    HrAttendanceClass hrAttendanceClass = hrAttendanceClassService.selectHrAttendanceClassById(Long.valueOf(classId));
                    String workTime = hrAttendanceClass.getWorkTime();
                    String closingTime = hrAttendanceClass.getClosingTime();
                    String elasticityFlag = hrAttendanceClass.getElasticityFlag();//是否弹性制
                    Integer standardWorkMinute = Integer.parseInt(workTime.split(":")[0])*60 + Integer.parseInt(workTime.split(":")[1]);
                    Integer standardClosingMinute = Integer.parseInt(closingTime.split(":")[0])*60 + Integer.parseInt(closingTime.split(":")[1]);
                    //上班打卡时间转化的分钟数 > 下班打卡时间转化的分钟数，则为非正常的跨天班次
                    if(standardWorkMinute > standardClosingMinute){
                        return Long.valueOf(classId);
                    }
                    return null;
                }
            }
        }
        Integer needNotDate = hrAttendanceGroupService.selectDayIsNeedNotDate(userId, workday);
        //如果前一天既不是必须打卡的日期，也不是不用打卡日期，按照星期数来查询班次信息
        Calendar cal = Calendar.getInstance();
        //1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;//查询前一天的考勤组班次信息
        if (DateUtil.isSameDay(DateUtil.parseDate(workday),DateUtils.getNowDate())) {
            //如果是今天，则查询今天的考勤组班次信息
            week = cal.get(Calendar.DAY_OF_WEEK);
        }
        Map<String, Object> map = hrAttendanceGroupService.selectGroupAndClass(userId,week);
//        Boolean isLegalDay = dateService.selectIsLegalDay(yesterday);
        if(map != null && !map.isEmpty()){
            Double hours = Double.valueOf((String) map.get("hours"));
            //当日班次上班时长为0或空，或当天属于不用打卡的日期，则判定为公休日
            if(hours == null || hours == 0d || needNotDate > 0){
                return null;
            }
            String elasticityFlag = (String) map.get("elasticity_flag");//是否弹性制
            String workTime = (String) map.get("work_time");//班次上班打卡时间
            Integer standardWorkMinute = Integer.parseInt(workTime.split(":")[0])*60 + Integer.parseInt(workTime.split(":")[1]);
            String closingTime = (String) map.get("closing_time");//班次下班打卡时间
            Integer standardClosingMinute = Integer.parseInt(closingTime.split(":")[0])*60 + Integer.parseInt(closingTime.split(":")[1]);
            //上班打卡时间转化的分钟数 > 下班打卡时间转化的分钟数，则为非正常的跨天班次
            if(standardWorkMinute > standardClosingMinute){
                Long classId = (Long) map.get("class_id");
                return classId;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public int insertByApp(HrAttendanceInfo hrAttendanceInfo,Boolean isAm,Boolean isPm)
    {
        logger.info("打卡信息{}"+hrAttendanceInfo.toString());
        int i = 0;
        String today = DateUtils.dateTimeNow("yyyy-MM-dd");//今天
        String yesterday = DateUtil.format(DateUtil.yesterday(),"yyyy-MM-dd");//昨天
        Long empId = hrAttendanceInfo.getEmpId();
        if(empId == null){
            return i;
        }

        HrEmp hrEmp = hrEmpService.selectTHrEmpById(empId);
        Long deptId = hrEmp.getDeptId();
        Long postId = hrEmp.getPostId();
        hrAttendanceInfo.setDeptId(deptId);
        hrAttendanceInfo.setPostId(postId);
        try {
            Map<String, Object> groupMap = hrAttendanceGroupService.selectGruopByEmpIdFromApi(empId);
            String type = (String) groupMap.get("type");
            Long groupId = (Long) groupMap.get("group_id") ;
            Long yesterdayClassId = dayIsNormalClass(empId,yesterday);
            if (yesterdayClassId != null) {
                Boolean isComplete = classIsComplete(yesterdayClassId,yesterday,today);
                if (!isComplete) {
                    //如果前一天是非正常班次，且前一天的班次没有完成
                    hrAttendanceInfo.setWorkDate(yesterday);
                    //如果是排班制，按照排班的班次信息打卡
                    if(groupId != null && StringUtils.isNotBlank(type) && "1".equals(type)){
                        Map<String,Object> yesterdayScheduGroupAndClassMap = hrAttendanceGroupService.selectScheduGroupAndClass(groupId,empId,yesterday);
                        //当天班次信息为空，则是公休日
                        if(yesterdayScheduGroupAndClassMap == null || yesterdayScheduGroupAndClassMap.isEmpty()){
                            return 1;
                        }
                        String elasticityFlag = (String) yesterdayScheduGroupAndClassMap.get("elasticity_flag");//是否弹性制
                        String workTime = (String) yesterdayScheduGroupAndClassMap.get("work_time");//班次上班打卡时间
                        Integer standardWorkMinute = Integer.parseInt(workTime.split(":")[0])*60 + Integer.parseInt(workTime.split(":")[1]);
                        String closingTime = (String) yesterdayScheduGroupAndClassMap.get("closing_time");//班次下班打卡时间
                        Integer standardClosingMinute = Integer.parseInt(closingTime.split(":")[0])*60 + Integer.parseInt(closingTime.split(":")[1]);
                        LocalDateTime checkTime = LocalDateTime.parse(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",hrAttendanceInfo.getUserCheckTime())
                                ,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        int checkMinute = checkTime.getHour()*60 + checkTime.getMinute();
                        return clockIn(elasticityFlag,hrAttendanceInfo,isAm,isPm,yesterday,standardWorkMinute,standardClosingMinute,checkMinute);//打卡
                    }
                    //如果是固定班制
                    String mustDate = hrAttendanceGroupService.selectDayIsMustDate(empId,yesterday);
                    Integer needNotDate = hrAttendanceGroupService.selectDayIsNeedNotDate(empId, yesterday);
                    //如果当天属于必须打卡的日期，那么要按照必须打卡日期的班次来打卡
                    if(StringUtils.isNotBlank(mustDate)){
                        String[] mustDates = mustDate.split(",");
                        for(String s : mustDates){
                            if (s.contains(yesterday)) {
                                String classId = s.split(":")[1];//当天的班次id
                                HrAttendanceClass hrAttendanceClass = hrAttendanceClassService.selectHrAttendanceClassById(Long.valueOf(classId));
                                String workTime = hrAttendanceClass.getWorkTime();
                                String closingTime = hrAttendanceClass.getClosingTime();
                                String elasticityFlag = hrAttendanceClass.getElasticityFlag();//是否弹性制
                                Integer standardWorkMinute = Integer.parseInt(workTime.split(":")[0])*60 + Integer.parseInt(workTime.split(":")[1]);
                                Integer standardClosingMinute = Integer.parseInt(closingTime.split(":")[0])*60 + Integer.parseInt(closingTime.split(":")[1]);
                                LocalDateTime checkTime = LocalDateTime.parse(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",hrAttendanceInfo.getUserCheckTime())
                                        ,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                int checkMinute = checkTime.getHour()*60 + checkTime.getMinute();
                                return clockIn(elasticityFlag,hrAttendanceInfo,isAm,isPm,yesterday,standardWorkMinute,standardClosingMinute,checkMinute);//打卡
                            }
                        }
                    }
                    Calendar cal = Calendar.getInstance();
                    //1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
                    int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
                    //查询当天对应的班次来打卡
                    Map<String, Object> groupAndClassMap = hrAttendanceGroupService.selectGroupAndClass(empId,week);
                    //Boolean isLegalDay = dateService.selectIsLegalDay(today);
                    //当天班次信息为空，或当天属于不用打卡的日期，则今天是公休日
                    if(groupAndClassMap == null || groupAndClassMap.isEmpty() || needNotDate > 0){
                        return 1;
                    }

                    String elasticityFlag = (String) groupAndClassMap.get("elasticity_flag");//是否弹性制
                    String workTime = (String) groupAndClassMap.get("work_time");//班次上班打卡时间
                    Integer standardWorkMinute = Integer.parseInt(workTime.split(":")[0])*60 + Integer.parseInt(workTime.split(":")[1]);
                    String closingTime = (String) groupAndClassMap.get("closing_time");//班次下班打卡时间
                    Integer standardClosingMinute = Integer.parseInt(closingTime.split(":")[0])*60 + Integer.parseInt(closingTime.split(":")[1]);
                    LocalDateTime checkTime = LocalDateTime.parse(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",hrAttendanceInfo.getUserCheckTime())
                            ,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    int checkMinute = checkTime.getHour()*60 + checkTime.getMinute();
                    return clockIn(elasticityFlag,hrAttendanceInfo,isAm,isPm,yesterday,standardWorkMinute,standardClosingMinute,checkMinute);//打卡
                }
            }

            //如果前一天是正常班次或前一天的非正常班次已完成，则进入当天打卡逻辑
            Long todayClassId = dayIsNormalClass(empId,today);
            //如果是排班制，按照排班的班次信息打卡
            if(groupId != null && StringUtils.isNotBlank(type) && "1".equals(type)){
                Map<String,Object> scheduGroupAndClassMap = hrAttendanceGroupService.selectScheduGroupAndClass(groupId,empId,today);
                //当天班次信息为空，则是公休日
                if(scheduGroupAndClassMap == null || scheduGroupAndClassMap.isEmpty()){
                    return 1;
                }
                String elasticityFlag = (String) scheduGroupAndClassMap.get("elasticity_flag");//是否弹性制
                String workTime = (String) scheduGroupAndClassMap.get("work_time");//班次上班打卡时间
                Integer standardWorkMinute = Integer.parseInt(workTime.split(":")[0])*60 + Integer.parseInt(workTime.split(":")[1]);
                String closingTime = (String) scheduGroupAndClassMap.get("closing_time");//班次下班打卡时间
                Integer standardClosingMinute = Integer.parseInt(closingTime.split(":")[0])*60 + Integer.parseInt(closingTime.split(":")[1]);
                LocalDateTime checkTime = LocalDateTime.parse(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",hrAttendanceInfo.getUserCheckTime())
                        ,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                int checkMinute = checkTime.getHour()*60 + checkTime.getMinute();
                if(todayClassId != null){
                    //如果当天是非正常班次（跨天班次），下班时间为第二天，所以要加24*60分钟
                    standardClosingMinute = standardClosingMinute + 1440;
                }
                return clockIn(elasticityFlag,hrAttendanceInfo,isAm,isPm,today,standardWorkMinute,standardClosingMinute,checkMinute);//打卡
            }

            //如果是固定班制
            String mustDate = hrAttendanceGroupService.selectDayIsMustDate(empId,today);
            Integer needNotDate = hrAttendanceGroupService.selectDayIsNeedNotDate(empId, today);
            //如果当天属于必须打卡的日期，那么要按照必须打卡日期的班次来打卡
            if(StringUtils.isNotBlank(mustDate)){
                String[] mustDates = mustDate.split(",");
                for(String s : mustDates){
                    if (s.contains(today)) {
                        String classId = s.split(":")[1];//当天的班次id
                        HrAttendanceClass hrAttendanceClass = hrAttendanceClassService.selectHrAttendanceClassById(Long.valueOf(classId));
                        String workTime = hrAttendanceClass.getWorkTime();
                        String closingTime = hrAttendanceClass.getClosingTime();
                        String elasticityFlag = hrAttendanceClass.getElasticityFlag();//是否弹性制
                        Integer standardWorkMinute = Integer.parseInt(workTime.split(":")[0])*60 + Integer.parseInt(workTime.split(":")[1]);
                        Integer standardClosingMinute = Integer.parseInt(closingTime.split(":")[0])*60 + Integer.parseInt(closingTime.split(":")[1]);
                        LocalDateTime checkTime = LocalDateTime.parse(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",hrAttendanceInfo.getUserCheckTime())
                                ,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        int checkMinute = checkTime.getHour()*60 + checkTime.getMinute();
                        if(todayClassId != null){
                            //如果当天是非正常班次（跨天班次），下班时间为第二天，所以要加24*60分钟
                            standardClosingMinute = standardClosingMinute + 1440;
                        }
                        return clockIn(elasticityFlag,hrAttendanceInfo,isAm,isPm,today,standardWorkMinute,standardClosingMinute,checkMinute);//打卡
                    }
                }
            }
            Calendar cal = Calendar.getInstance();
            //1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
            int week = cal.get(Calendar.DAY_OF_WEEK);
            //查询当天对应的班次来打卡
            Map<String, Object> groupAndClassMap = hrAttendanceGroupService.selectGroupAndClass(empId,week);
            //Boolean isLegalDay = dateService.selectIsLegalDay(today);
            //当天班次信息为空，或当天属于不用打卡的日期，则今天是公休日
            if(groupAndClassMap == null || groupAndClassMap.isEmpty() || needNotDate > 0){
                return 1;
            }

            String elasticityFlag = (String) groupAndClassMap.get("elasticity_flag");//是否弹性制
            String workTime = (String) groupAndClassMap.get("work_time");//班次上班打卡时间
            Integer standardWorkMinute = Integer.parseInt(workTime.split(":")[0])*60 + Integer.parseInt(workTime.split(":")[1]);
            String closingTime = (String) groupAndClassMap.get("closing_time");//班次下班打卡时间
            Integer standardClosingMinute = Integer.parseInt(closingTime.split(":")[0])*60 + Integer.parseInt(closingTime.split(":")[1]);
            LocalDateTime checkTime = LocalDateTime.parse(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",hrAttendanceInfo.getUserCheckTime())
                                                                                    ,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            int checkMinute = checkTime.getHour()*60 + checkTime.getMinute();
            if(todayClassId != null){
                //如果当天是非正常班次（跨天班次），下班时间为第二天，所以要加24*60分钟
                standardClosingMinute = standardClosingMinute + 1440;
            }
            i = clockIn(elasticityFlag,hrAttendanceInfo,isAm,isPm,today,standardWorkMinute,standardClosingMinute,checkMinute);//打卡
        } catch (Exception e) {
            i = 0;
            e.printStackTrace();
        }
        return i;
    }


    //时间结果(Normal：正常;Early：早退;Late：迟到;SeriousLate：严重迟到；Absenteeism：旷工迟到；NotSigned：未打卡
    public static String time_result_Normal = "Normal";//正常
    public static String time_result_Early = "Early";//正常
    public static String time_result_Late = "Late";//迟到
    public static String time_result_SeriousLate = "SeriousLate";//严重迟到
    public static String time_result_AbsenteeismLate = "AbsenteeismLate";//旷工迟到
    public static String time_result_NotSigned = "NotSigned";//未打卡
    public static String time_result_SeriousEarly = "SeriousEarly";//严重早退
    public static String time_result_AbsenteeismEarly = "AbsenteeismEarly";//早退旷工
    public static String time_result_AbsenteeismLateOneDay = "AbsenteeismLateOneDay";//迟到一天
    public static String time_result_AbsenteeismEarlyOneDay = "AbsenteeismEarlyOneDay";//早退一天

    //考勤类型(OnDuty：上班,OffDuty：下班)
    public static String check_type_OnDuty = "OnDuty";//上班
    public static String check_type_OffDuty = "OffDuty";//下班

    //数据来源(ATM：考勤机;BEACON：IBeacon;DING_ATM：钉钉考勤机;USER：用户打卡;BOSS：老板改签;APPROVE：审批系统;SYSTEM：考勤系统;AUTO_CHECK：自动打卡)
    public static String source_type_ATM = "ATM";//考勤机
    public static String source_type_DING_ATM = "DING_ATM";//钉钉考勤机
    public static String source_type_USER = "USER";//用户打卡
    public static String source_type_BOSS = "BOSS";//老板改签
    public static String source_type_APPROVE = "APPROVE";//审批系统
    public static String source_type_SYSTEM = "SYSTEM";//考勤系统
    public static String source_type_AUTO_CHECK = "AUTO_CHECK";//自动打卡

    //位置结果(Normal：范围内;Outside：范围外，外勤打卡时为这个值)
    public static String location_result_Normal = "Normal";//范围内
    public static String location_result_Outside = "Outside";//范围外


    /**
     * 打卡数据记录
     * @param elasticityFlag 是否弹性制
     * @param hrAttendanceInfo
     * @param isAm 是否上班
     * @param isPm 是否下班
     * @param day 打卡日期
     * @param standardWorkMinute 上班时间分钟数
     * @param standardClosingMinute 下班时间分钟数
     * @param checkMinute 打卡时间分钟数
     * @return
     */
    @Transactional
    public int clockIn(String elasticityFlag,HrAttendanceInfo hrAttendanceInfo,Boolean isAm,Boolean isPm,String day,
                        Integer standardWorkMinute,Integer standardClosingMinute,int checkMinute){
        int i = 0;
        if(isAm){//上班卡
            hrAttendanceInfo.setTimeResult(time_result_Normal);//正常
            if("0".equals(elasticityFlag)){//非弹性打卡要判断迟到的各种状态
                int onTimeResult = checkMinute - standardWorkMinute;
                if(onTimeResult > 0){
                    hrAttendanceInfo.setResultMinutes(String.valueOf(onTimeResult));
                    if (onTimeResult <= 10) {
                        hrAttendanceInfo.setTimeResult(time_result_Late);//迟到
                    }if (onTimeResult > 10 && onTimeResult < 30) {
                        hrAttendanceInfo.setTimeResult(time_result_SeriousLate);//严重迟到
                    }if (onTimeResult >= 30 && onTimeResult < 60) {
                        hrAttendanceInfo.setTimeResult(time_result_AbsenteeismLate);//迟到30分钟（含）到59分钟，直接按旷工半天处理
                    }if (onTimeResult >= 60) {
                        hrAttendanceInfo.setTimeResult(time_result_AbsenteeismLateOneDay);//迟到60分钟（含）以上，直接按旷工一天处理
                    }
                }
            }

            hrAttendanceInfo.setCheckType(check_type_OnDuty);
            HrAttendanceInfo oldHrAttendanceInfo = selectOldHrAttendanceInfo(hrAttendanceInfo,check_type_OnDuty,day);
            //存在则更新，不存在则新增
            if ( oldHrAttendanceInfo == null) {
                i = hrAttendanceInfoMapper.insertSelective(hrAttendanceInfo);
            }else{
                hrAttendanceInfo.setId(oldHrAttendanceInfo.getId());
                i = hrAttendanceInfoMapper.updateByPrimaryKeySelective(hrAttendanceInfo);
            }
        }
        if(isPm){//如果是下班卡
            hrAttendanceInfo.setTimeResult(time_result_Normal);//正常
            if("0".equals(elasticityFlag)){//非弹性打卡要判断早退的各种状态
                int offTimeResult =  standardClosingMinute - checkMinute;
                if(offTimeResult > 0){
                    hrAttendanceInfo.setResultMinutes(String.valueOf(offTimeResult));
                    if (offTimeResult <= 10) {
                        hrAttendanceInfo.setTimeResult(time_result_Early);//早退
                    }if (offTimeResult > 10 && offTimeResult < 30) {
                        hrAttendanceInfo.setTimeResult(time_result_SeriousEarly);//严重早退
                    }if (offTimeResult >= 30 && offTimeResult < 60) {
                        hrAttendanceInfo.setTimeResult(time_result_AbsenteeismEarly);//早退30分钟（含）到59分钟，直接按旷工半天处理
                    }if (offTimeResult >= 60) {
                        hrAttendanceInfo.setTimeResult(time_result_AbsenteeismEarlyOneDay);//迟到60分钟（含）以上，直接按旷工一天处理
                    }
                }
            }

            hrAttendanceInfo.setCheckType(check_type_OffDuty);
            HrAttendanceInfo oldHrAttendanceInfo = selectOldHrAttendanceInfo(hrAttendanceInfo,check_type_OffDuty,day);
            //存在则更新，不存在则新增
            if ( oldHrAttendanceInfo == null) {
                i = hrAttendanceInfoMapper.insertSelective(hrAttendanceInfo);
            }else{
                //如果有存在跨天的夜班，并且当前打卡时间大于当天夜班的上班时间，
                // 那么打的下班卡则插入表中，而不是更新下班卡记录（因为这条当天的早退下班卡会更新掉夜班班次前一天的下班卡记录）
                if(standardWorkMinute > standardClosingMinute && checkMinute > standardWorkMinute){
                    i = hrAttendanceInfoMapper.insertSelective(hrAttendanceInfo);
                }
                //否则更新当天的下班卡记录
                hrAttendanceInfo.setId(oldHrAttendanceInfo.getId());
                i = hrAttendanceInfoMapper.updateByPrimaryKeySelective(hrAttendanceInfo);
            }

            // 仓储人员加班数据
            //打卡正常
            if (hrAttendanceInfo.getTimeResult().equals(time_result_Normal)){
                Long empId = hrAttendanceInfo.getEmpId();
                SysUser sysUser = sysUserService.selectUserById(empId);
                for (SysRole sysRole : sysUser.getRoles()) {
                    if (sysRole.getRoleKey().equals("storage_auto_overTime")){
                        //仓储人员加班数据
                        DateTime offsetDay = DateUtil.offsetDay(hrAttendanceInfo.getUserCheckTime(), -1);
                        Integer shiftCriticalPoint = Integer.parseInt(sysConfigService.selectConfigByKey(SysConfigEnum.SysConfig.shift_critical_point.getValue()));
                        DateOperation dateOperation = getDateOperation(hrAttendanceInfo, offsetDay,shiftCriticalPoint);

                        Double hours = overtimeService.getHours(hrAttendanceInfo.getUserCheckTime(), dateOperation.getOriginalEnd());
                        if (hours >= 0.5D ){

                            //大于半小时，开始维护加班数据
                            Holiday holiday = new Holiday();
                            holiday.setType("1");//加班工时
                            holiday.setEmpName(sysUser.getUserName());
                            holiday.setUserId(sysUser.getUserId());
                            holiday.setEmpId(sysUser.getUserId());
                            holiday.setClassInfoId(hrAttendanceInfo.getId());
                            holiday.setStartDate(dateOperation.getOriginalEnd());
                            holiday.setEndDate(DateUtil.offsetMonth(hrAttendanceInfo.getUserCheckTime(),3));
                            holiday.setHours(hours);

                            holiday.setIsPublic(dateOperation.getIsPublic() ? 1 : 0);
                            holiday.setAttendanceDate(dateOperation.getAttendanceDay());
                            holiday.setRemark("仓储人员下班后插入加班详情数据");

                            holiday.setCreateId(sysUser.getUserId());
                            holiday.setCreateBy(sysUser.getLoginName());
                            holiday.setUpdateId(sysUser.getUserId());
                            holiday.setUpdateBy(sysUser.getLoginName());

                            holidayService.insertHoliday(holiday);
                        }

                        break;
                    }
                }
            }
        }
        return i;
    }

    private DateOperation getDateOperation(HrAttendanceInfo hrAttendanceInfo, DateTime offsetDay,
                                           Integer shiftCriticalPoint) {
        HrAttendanceClass hrAttendanceClass = hrAttendanceClassService.getAttendanceClass(hrAttendanceInfo.getEmpId()
                , offsetDay);
        if (hrAttendanceClass != null){
            DateOperation dateOperation = hrAttendanceClassService.getDateOperation(
                    offsetDay, hrAttendanceClass, hrAttendanceInfo.getEmpId(),shiftCriticalPoint);
            if (DateUtil.date(dateOperation.getShiftCriticalPointEndDate())
                    .isBeforeOrEquals(hrAttendanceInfo.getUserCheckTime())){
                //班次下班卡在本次打卡之前，本班次不是这条打卡数据的班次，寻找下一个班次
                return getDateOperation(hrAttendanceInfo,DateUtil.offsetDay(offsetDay,1), shiftCriticalPoint);
            }else {
                return dateOperation;
            }
        }else {
            //寻找下一个班次
            return getDateOperation(hrAttendanceInfo,DateUtil.offsetDay(offsetDay,1), shiftCriticalPoint);
        }
    }

    public HrAttendanceInfo selectOldHrAttendanceInfo(HrAttendanceInfo hrAttendanceInfo,String checkType,String day){
        Example example = new Example(HrAttendanceInfo.class);
        example.createCriteria().andEqualTo("empId",hrAttendanceInfo.getEmpId())
                .andEqualTo("workDate",day)
                .andEqualTo("checkType",checkType)
                .andEqualTo("delFlag","0");
        HrAttendanceInfo oldHrAttendanceInfo = hrAttendanceInfoMapper.selectSingleOneByExample(example);
        return oldHrAttendanceInfo;
    }

    /**
     * 修改个人考勤记录
     * 
     * @param hrAttendanceInfo 个人考勤记录
     * @return 结果
     */
    @Override
    public int updateHrAttendanceInfo(HrAttendanceInfo hrAttendanceInfo)
    {
        hrAttendanceInfo.setUpdateId(ShiroUtils.getUserId());
        hrAttendanceInfo.setUpdateBy(ShiroUtils.getLoginName());
        hrAttendanceInfo.setUpdateTime(DateUtils.getNowDate());
        return hrAttendanceInfoMapper.updateByPrimaryKeySelective(hrAttendanceInfo);
    }

    /**
     * 删除个人考勤记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceInfoByIds(String ids)
    {
        return hrAttendanceInfoMapper.deleteHrAttendanceInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除个人考勤记录信息
     * 
     * @param id 个人考勤记录ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceInfoById(Long id)
    {
        return hrAttendanceInfoMapper.deleteHrAttendanceInfoById(id);
    }

    @Override
    public List<Map<String,Object>> getClockIn(@Param("userId") Long userId, @Param("date") String date) {
        return hrAttendanceInfoMapper.getClockIn(userId,date);
    }

    @Override
    public List<Map<String,Object>> selectActualAttendanceDay(Long empId, int year, int month) {
        return hrAttendanceInfoMapper.selectActualAttendanceDay(empId,year,month);
    }

    @Override
    public Integer selectLateOrEarly(Long empId, int year, int month) {
        return hrAttendanceInfoMapper.selectLateOrEarly(empId,year,month);
    }

    @Override
    public List<String> selectAbsentListByDay(Long empId,String day) {
        return hrAttendanceInfoMapper.selectAbsentListByDay(empId,day);
    }

    @Override
    @Transactional
    public int delClockIn(Long userId, String today) {

        List<HrAttendanceInfo> hrAttendanceInfos = hrAttendanceInfoMapper.selectDelClockIn(userId, today);
        for (HrAttendanceInfo hrAttendanceInfo : hrAttendanceInfos) {
            holidayService.deleteHolidayByIdClassInfoId(hrAttendanceInfo.getId());
        }

        return hrAttendanceInfoMapper.delClockIn(userId,today);
    }

}
