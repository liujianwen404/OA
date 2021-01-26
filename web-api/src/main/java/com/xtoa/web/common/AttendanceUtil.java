package com.xtoa.web.common;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.base.provider.appApi.AttendanceApi;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class AttendanceUtil {

    @Reference(retries = 0,check = false)
    private AttendanceApi attendanceApi;

    /**
     * 判断该员工昨天或当天的班次是否正常，正常班次返回null，非正常班次返回班次id
     * @param userId
     * @param workday（workday的选项为yesterday或today）
     * @return
     */
    public Long dayIsNormalClass(Long userId, String workday) {
        Map<String, Object> groupMap = attendanceApi.selectGroup(userId);
        if(groupMap == null || groupMap.isEmpty()){
            log.error("根据员工ID查询考勤组班次信息，groupMap : {}" + groupMap);
            throw new RuntimeException("员工不在考勤组，请联系人事相关人员！");
        }
        String type = (String) groupMap.get("type");
        Long groupId = (Long) groupMap.get("group_id") ;
        //如果是排班制
        if(groupId != null && StringUtils.isNotBlank(type) && "1".equals(type)){
            Map<String, Object> map = attendanceApi.selectScheduGroupAndClass(groupId,userId,workday);
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
        String mustDate = attendanceApi.selectDayIsMustDate(userId, workday);
        if(StringUtils.isNotBlank(mustDate)){
            //如果前一天属于必须打卡的日期，那么要按照必须打卡日期的班次来判断
            String[] mustDates = mustDate.split(",");
            for(String s : mustDates){
                if (s.contains(workday)) {
                    String classId = s.split(":")[1];//班次id
                    HrAttendanceClass hrAttendanceClass = attendanceApi.selectClass(Long.valueOf(classId));
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
        Integer needNotDate = attendanceApi.selectDayIsNeedNotDate(userId, workday);
        //如果前一天既不是必须打卡的日期，也不是不用打卡日期，按照星期数来查询班次信息
        Calendar cal = Calendar.getInstance();
        //1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
        int week  = cal.get(Calendar.DAY_OF_WEEK) - 1;//查询前一天的考勤组班次信息
        if (DateUtil.isSameDay(DateUtil.parseDate(workday),DateUtils.getNowDate())) {
            //如果是今天，则查询今天的考勤组班次信息
            week = cal.get(Calendar.DAY_OF_WEEK);
        }
        Map<String, Object> map = attendanceApi.selectGroupAndClass(userId,week);
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

    /**
     * 判断当前班次是否完成
     *      此时前一天已经确认为有班次且为非正常班次，所以才需要判断当前时间是否在前一天的班次时间内
     *      如果当前时间还在前一天的班次时间内，说明前一天的班次还未完成，否则前一天的班次已完成
     * @param classId
     * @param yesterday
     * @param today
     * @return
     */
    public Boolean classIsComplete(Long classId, String yesterday, String today) {
        HrAttendanceClass hrAttendanceClass = attendanceApi.selectClass(classId);
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

}
