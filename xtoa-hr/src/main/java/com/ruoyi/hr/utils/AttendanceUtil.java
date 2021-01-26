package com.ruoyi.hr.utils;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.hr.service.IHrAttendanceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Component
public class AttendanceUtil {

    @Autowired
    private IHrAttendanceGroupService hrAttendanceGroupService;
    /*
     * 判断某个日期是否是休息日
     * */
    @Transactional
    public Boolean dayIsWorkDay(Long empId,String day){
        //先判断该日期在不在考勤组的不用打卡日期字段里，如果在则证明是休息日
        Integer num = hrAttendanceGroupService.selectDayIsNeedNotDate(empId,day);
        if(num > 0){
            return true;
        }
        //如果不在，再判断该日期是星期几，有没有排班信息
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String week = sdf.format(DateUtils.parseDate(day));
        if("Mon".equals(week)){
            Integer i = hrAttendanceGroupService.selectDayIsClass(empId,2);
            if (i > 0){
                return true;
            }
        }
        if("Tue".equals(week)){
            Integer i = hrAttendanceGroupService.selectDayIsClass(empId,3);
            if (i > 0){
                return true;
            }
        }
        if("Wed".equals(week)){
            Integer i = hrAttendanceGroupService.selectDayIsClass(empId,4);
            if (i > 0){
                return true;
            }
        }
        if("Thu".equals(week)){
            Integer i = hrAttendanceGroupService.selectDayIsClass(empId,5);
            if (i > 0){
                return true;
            }
        }
        if("Fri".equals(week)){
            Integer i = hrAttendanceGroupService.selectDayIsClass(empId,6);
            if (i > 0){
                return true;
            }
        }
        if("Sat".equals(week)){
            Integer i = hrAttendanceGroupService.selectDayIsClass(empId,7);
            if (i > 0){
                return true;
            }
        }
        if("Sun".equals(week)){
            Integer i = hrAttendanceGroupService.selectDayIsClass(empId,1);
            if (i > 0){
                return true;
            }
        }
        return false;
    }
}
