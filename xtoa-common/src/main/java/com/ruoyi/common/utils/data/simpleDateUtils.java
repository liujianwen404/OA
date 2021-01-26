package com.ruoyi.common.utils.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class simpleDateUtils {
    public double calLeaveDays(Date startTime, Date endTime){
        double leaveDays = 0;
        //从startTime开始循环，若该日期不是节假日或者不是周六日则请假天数+1
        Date flag = startTime;//设置循环开始日期
        Calendar cal = Calendar.getInstance();

        //从数据库得到节假日的起始日期和终止日期
        List<Map> maps = null;
        try{
//            maps = getDao().getHolidaysPeriod;//maps用于保存符合条件的所有节假日的起始日期和终止日期，如startDate:2017-07-13,endDate:2017-07-14
        }catch (Exception e){
            e.printStackTrace();
        }
//        //用于格式化日期
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        int week;
//        outer:while(flag.compareTo(endTime)!=1){
//            cal.setTime(flag);
//            //判断是否为周六日
//            week = cal.get(Calendar.DAY_OF_WEEK) - 1;
//            if(week == 0 || week == 6){//0为周日，6为周六
//                //跳出循环进入下一个日期
//                cal.add(Calendar.DAY_OF_MONTH, +1);
//                flag = cal.getTime();
//                continue;
//            }else{
//                //判断是否为节假日
////                if(maps != null || !maps.isEmpty()){
////                    inner:for (Map map : maps){
////                        if(flag.compareTo((Date)map.get("startDay"))>-1&&flag.compareTo((Date)map.get("endDay"))<1){
////　　　　　　　　　　　　　　//跳出循环进入下一个日期
////　　　　　　　　　　　　　　cal.add(Calendar.DAY_OF_MONTH, +1);
////                            flag = cal.getTime();
////                            continue outer;
////                        }
////                    }
////                }
//            }
//
//            leaveDays = leaveDays + 1;
//            //日期往后加一天
//            cal.add(Calendar.DAY_OF_MONTH, +1);
//            flag = cal.getTime();
//        }
        return leaveDays;
    }
}
