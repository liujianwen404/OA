package com.xtoa.web.controller.app;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ruoyi.base.domain.HrAttendanceInfo;
import com.ruoyi.base.provider.appApi.ClockInApi;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.vdurmont.emoji.EmojiParser;
import com.xtoa.web.common.AttendanceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webApi/clockIn")
public class ClockInController {

    @Reference(retries = 0,check = false)
    private ClockInApi clockInApi;

    @Autowired
    private AttendanceUtil attendanceUtil;

//    @HystrixCommand(fallbackMethod = "saveClockInError")
    @RequestMapping("/saveClockIn")
    public AjaxResult saveClockIn(HttpServletRequest request, HttpServletResponse response){
        //验证h5用户是否安全登录
        if(!ShiroUtils.isAuthenticated(request.getParameter("sessionId"), request, response)){
            return AjaxResult.error("非法访问！");
        }
        Long userId = Long.valueOf(request.getParameter("userId"));
        Long groupId = Long.valueOf(request.getParameter("groupId"));
        Long classId = Long.valueOf(request.getParameter("classId"));
        String userName = request.getParameter("userName");
        String mode = request.getParameter("mode");
        String address = request.getParameter("address");
        String time = request.getParameter("time");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");
        String remarks = request.getParameter("remarks");
        Boolean isAm = Boolean.valueOf(request.getParameter("isAm"));
        Boolean isPm = Boolean.valueOf(request.getParameter("isPm"));
        Boolean isOutWork = Boolean.valueOf(request.getParameter("is"));//是否外勤
        String[] imgURLS = request.getParameterValues("imgURL");

        HrAttendanceInfo hrAttendanceInfo = new HrAttendanceInfo();
        hrAttendanceInfo.setEmpId(userId);
        hrAttendanceInfo.setEmpName(userName);
        hrAttendanceInfo.setGroupId(groupId);
        hrAttendanceInfo.setClassId(classId);
        hrAttendanceInfo.setWorkDate(DateUtils.dateTimeNow("yyyy-MM-dd"));
        hrAttendanceInfo.setUserCheckTime(DateUtils.parseDate(time));
        hrAttendanceInfo.setUserAddress(address);
        hrAttendanceInfo.setUserLongitude(longitude);
        hrAttendanceInfo.setUserLatitude(latitude);
        hrAttendanceInfo.setSourceType("PHONE");
        if(StringUtils.isNotBlank(remarks)){
            hrAttendanceInfo.setRemark(EmojiParser.removeAllEmojis(remarks));
        }
        if(isOutWork){
            hrAttendanceInfo.setLocationResult("Normal");
        }else{
            hrAttendanceInfo.setLocationResult("Outside");
        }
        if (imgURLS != null || imgURLS.length>0) {
            String imgURL = StringUtils.strip(Arrays.toString(imgURLS), "[]");
            hrAttendanceInfo.setImgUrl(imgURL);
        }
        log.info("打卡信息{}"+hrAttendanceInfo.toString());
        return clockInApi.saveClockIn(hrAttendanceInfo, isAm, isPm);
    }

    /**
     * 熔断方法
     * @param request
     * @return
     */
    public AjaxResult saveClockInError(HttpServletRequest request, HttpServletResponse response){
        return AjaxResult.error("打卡异常，请联系管理人员！");
    }

    /**
     * 删除当天的下班卡
     * @param request
     * @return
     */
//    @HystrixCommand(fallbackMethod = "delClockInInError")
    @RequestMapping("/delClockIn")
    public AjaxResult delClockIn(HttpServletRequest request, HttpServletResponse response){
        //验证h5用户是否安全登录
        if(!ShiroUtils.isAuthenticated(request.getParameter("sessionId"), request, response)){
            return AjaxResult.error("非法访问！");
        }
        Long userId = Long.valueOf(request.getParameter("userId"));
        String today = DateUtils.dateTimeNow("yyyy-MM-dd");//今天
        String yesterday = DateUtil.format(DateUtil.yesterday(),"yyyy-MM-dd");
        Long classId = attendanceUtil.dayIsNormalClass(userId,yesterday);
        if (classId != null) {
            //如果前一天是非正常班次，则要判断前一天的班次有没有完成
            Boolean isComplete = attendanceUtil.classIsComplete(classId,yesterday,today);
            if(!isComplete){
                //前一天的班次未完成，则删除的是前一天的打卡信息
                int i = clockInApi.delClockIn(userId,yesterday);
                if(i > 0){
                    return AjaxResult.success();
                }
                return AjaxResult.error("删除打卡数据失败");
            }
        }
        int i = clockInApi.delClockIn(userId,today);
        if(i > 0){
            return AjaxResult.success();
        }
        return AjaxResult.error("删除打卡数据失败");
    }

    /**
     * 检查是否早退
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/checkClockIn")
    public AjaxResult checkClockIn(HttpServletRequest request, HttpServletResponse response){
        //验证h5用户是否安全登录
        if(!ShiroUtils.isAuthenticated(request.getParameter("sessionId"), request, response)){
            return AjaxResult.error("非法访问！");
        }
        Long userId = Long.valueOf(request.getParameter("userId"));
        String workTime = request.getParameter("workTime");
        String closingTime = request.getParameter("closingTime");
        String today = DateUtils.dateTimeNow("yyyy-MM-dd");//今天
        String yesterday = DateUtil.format(DateUtil.yesterday(),"yyyy-MM-dd");
        Long yestedayClassId = attendanceUtil.dayIsNormalClass(userId,yesterday);
        boolean flag = false;
        if (yestedayClassId != null) {
            //如果前一天是非正常班次，则要判断前一天的班次有没有完成
            Boolean isComplete = attendanceUtil.classIsComplete(yestedayClassId,yesterday,today);
            if(!isComplete){
                //前一天的班次未完成，则判断前一天班次是否早退
                DateTime closingDateTime = DateUtil.parse(today + " " + closingTime);//前一天班次下班时间
                flag = closingDateTime.isBefore(DateUtils.getNowDate());
                if(flag){
                    return AjaxResult.success();
                }
                return AjaxResult.error();
            }

        }
        //如果前一天是正常班次或前一天的非正常班次已完成，则判断当天班次是否正常
        Long todayClassId = attendanceUtil.dayIsNormalClass(userId,today);
        if (todayClassId != null) {
            //如果当天是非正常班次，又因为当天非正常班次的下班时间都是到了第二天，所以当天的下班卡都是早退
            return AjaxResult.error();
        }
        //如果当天是正常班次，判读当天正常班次是否早退
        DateTime closingDateTime = DateUtil.parse(today + " " + closingTime);//当天班次下班时间
        flag = closingDateTime.isBefore(DateUtils.getNowDate());
        if(flag){
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }


    /**
     * 熔断方法
     * @param request
     * @return
     */
    public AjaxResult delClockInInError(HttpServletRequest request, HttpServletResponse response){
        return AjaxResult.error("重置打卡异常，请联系管理人员！");
    }

//    @HystrixCommand(fallbackMethod = "getClockInError")
    @RequestMapping("/getClockIn")
    public AjaxResult getClockIn(HttpServletRequest request, HttpServletResponse response){
        //验证h5用户是否安全登录
        if(!ShiroUtils.isAuthenticated(request.getParameter("sessionId"), request, response)){
            return AjaxResult.error("非法访问！");
        }
        String today = DateUtils.dateTimeNow("yyyy-MM-dd");//今天
        Long userId = Long.valueOf(request.getParameter("userId"));
        List<Map<String,Object>> list = clockInApi.getClockIn(userId,today);
        return AjaxResult.success(list);
    }

    /**
     * 熔断方法
     * @param request
     * @return
     */
    public AjaxResult getClockInError(HttpServletRequest request, HttpServletResponse response){
        return AjaxResult.error("获取打卡信息异常，请联系管理人员！");
    }
}
