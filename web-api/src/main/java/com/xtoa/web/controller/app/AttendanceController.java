package com.xtoa.web.controller.app;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.base.provider.appApi.AttendanceApi;
import com.ruoyi.base.provider.appApi.ClockInApi;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.data.service.ITDateService;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.xtoa.web.common.AttendanceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/webApi/attendance")
public class AttendanceController {

    @Reference(retries = 0,check = false)
    private AttendanceApi attendanceApi;

    @Reference(retries = 0,check = false)
    private ClockInApi clockInApi;

    @Autowired
    private ITDateService dateService;

    @Autowired
    private AttendanceUtil attendanceUtil;

//    @HystrixCommand(fallbackMethod = "getDataError")
    @GetMapping("/getData")
    public AjaxResult getData(String sessionId,HttpServletRequest request,HttpServletResponse response){
        //验证h5用户是否安全登录
        if(!ShiroUtils.isAuthenticated(sessionId, request, response)){
            return AjaxResult.error("非法访问！");
        }
//        SysUser sysUser = ShiroUtils.getSysUserBySession(sessionId, request, response);
//        SysUser sysUser = ShiroUtils.getSysUserBySession(sessionId, request, response);
        //因为h5端已经设置携带了cookie信息，所以此时不用通过请求中的sessionId来获取用户信息
        SysUser sysUser = ShiroUtils.getSysUser();
        Long userId = sysUser.getUserId();
        if(userId == null){
            return AjaxResult.error("userId为空,需要重新登录");
        }
        String today = DateUtils.dateTimeNow("yyyy-MM-dd");//今天
        String yesterday = DateUtil.format(DateUtil.yesterday(),"yyyy-MM-dd");

        Long classId = attendanceUtil.dayIsNormalClass(userId,yesterday);

        Map<String, Object> map = new HashMap<>();
        if(classId != null){
            //如果前一天是非正常班次，则要判断前一天的班次有没有完成
            Boolean isComplete = attendanceUtil.classIsComplete(classId,yesterday,today);
            if(!isComplete){
                //前一天的班次未完成，则返回前一天的考勤组、班次，及该员工前一天的打卡信息
                map = getGroupClassInfo(userId,classId,yesterday);
                return AjaxResult.success(map);
            }
        }

        //如果员工前一天的班次都是正常的，此时进入当天逻辑的判断
        Map<String, Object> groupMap = attendanceApi.selectGroup(userId);
        String type = (String) groupMap.get("type");
        Long groupId = (Long) groupMap.get("group_id") ;

        //如果是排班制
        if(groupId != null && StringUtils.isNotBlank(type) && "1".equals(type)){
            Map<String,Object> scheduGroupAndClassMap = attendanceApi.selectScheduGroupAndClass(groupId,userId,today);
            if(scheduGroupAndClassMap == null || scheduGroupAndClassMap.isEmpty()){
                //当天班次信息为空，则是公休日
                map.put("isRest",true);
                return AjaxResult.success(map);
            }
            Double hours = Double.valueOf((String) scheduGroupAndClassMap.get("hours"));
            //当日班次上班时长为0或空，则判定为公休日
            if(hours == null || hours == 0d){
                map.put("isRest",true);
                return AjaxResult.success(map);
            }
            map.putAll(scheduGroupAndClassMap);
            List<Map<String,Object>> list = clockInApi.getClockIn(userId,today);
            if(!CollectionUtils.isEmpty(list)){
                map.put("clockIn", JSONUtil.parse(list));
            }else{
                map.put("clockIn", new ArrayList<Map<String,Object>>());
            }
            return AjaxResult.success(map);
        }

        //如果是固定班制
        String mustDate = attendanceApi.selectDayIsMustDate(userId,today);
        Integer needNotDate = attendanceApi.selectDayIsNeedNotDate(userId, today);
        if(StringUtils.isNotBlank(mustDate)){
            //如果当天是必须打卡日期，返回必须打卡日期对应的考勤组、班次与打卡信息
            String[] mustDates = mustDate.split(",");
            for(String s : mustDates){
                if (s.contains(today)) {
                    Long class_id = Long.valueOf(s.split(":")[1]);//当天的班次id
                    map = getGroupClassInfo(userId,class_id,today);
                    return AjaxResult.success(map);
                }
            }
        }
        //如果当天不是必须打卡日期，则判断当天是不是公休日
        Calendar cal = Calendar.getInstance();
        //1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
        int week = cal.get(Calendar.DAY_OF_WEEK);
        Map<String,Object> groupAndClassMap = attendanceApi.selectGroupAndClass(userId,week);
        //当天班次信息为空，或当天属于不用打卡的日期，则今天是公休日
        if(groupAndClassMap == null || groupAndClassMap.isEmpty() || needNotDate > 0){
            map.put("isRest",true);
            return AjaxResult.success(map);
        }
        map.putAll(groupAndClassMap);
        List<Map<String,Object>> list = clockInApi.getClockIn(userId,today);
        if(!CollectionUtils.isEmpty(list)){
            map.put("clockIn", JSONUtil.parse(list));
        }else{
            map.put("clockIn", new ArrayList<Map<String,Object>>());
        }
        return AjaxResult.success(map);
    }

    /**
     * 熔断方法
     * @param sessionId
     * @return
     */
    public AjaxResult getDataError(String sessionId, HttpServletRequest request, HttpServletResponse response){
        return AjaxResult.error("获取考勤数据异常，请联系管理人员！");
    }

    /**
     * 获取该员工考勤组班次和打卡信息
     * @param userId
     * @param classId
     * @return
     */
    private Map<String, Object> getGroupClassInfo(Long userId,Long classId,String day) {
        HrAttendanceClass attendanceClass = attendanceApi.selectClass(classId);
        Map<String, Object> map = attendanceApi.selectGroup(userId);
        List<Map<String,Object>> list = clockInApi.getClockIn(userId,day);
        map.put("class_id", attendanceClass.getId());
        map.put("class_name", attendanceClass.getName());
        map.put("work_time", attendanceClass.getWorkTime());
        map.put("closing_time", attendanceClass.getClosingTime());
        map.put("rest_start_time", attendanceClass.getRestStartTime());
        map.put("rest_end_time", attendanceClass.getRestEndTime());
        map.put("elasticity_flag", attendanceClass.getElasticityFlag());
        map.put("clockIn", JSONUtil.parse(list));
        return map;
    }

}
