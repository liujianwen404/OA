package com.ruoyi.base.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.common.utils.Arith;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Slf4j
public class DingDingUtil {

    private static DingConfig dingConfig = SpringUtils.getBean(DingConfig.class);

    /**
     * 应出勤天数 36443430
     * 实出勤天数 36443433
     * 实休息天数 36443434
     * 旷工天数 36443445
     * 考勤结果 36443453
     * 班次 36443454
     * 休息日加班 36443451
     * 节假日加班 36443452
     * 工作日加班 36443450
     */
    private static final Integer shouldAttDay = 36443430;
    private static final Integer actualAttDay = 36443433;
    private static final Integer actualRestDay = 36443434;
    private static final Integer absentDay = 36443445;
    private static final Integer restOvertime = 36443450;
    private static final Integer holidayOvertime = 36443451;
    private static final Integer workdayOvertime = 36443452;
    private static final Integer attResult = 36443453;
    private static final Integer attClassData = 36443454;

    /**
     * 获取考勤统计报表列定义
     */
    public static List<Integer> getAttColumns(){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getattcolumns");
        OapiAttendanceGetattcolumnsRequest req = new OapiAttendanceGetattcolumnsRequest();
        OapiAttendanceGetattcolumnsResponse rsp = null;
        try {
            rsp = client.execute(req, dingConfig.getAccessToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        List<Integer> returnList = new ArrayList<>();
        if (rsp != null) {
            System.out.println(rsp.getBody());
            JSONObject jsonObject = JSON.parseObject(rsp.getBody());
            String result = String.valueOf(jsonObject.get("result"));
            JSONArray jsonArray = (JSONArray) JSON.parseObject(result).get("columns");
            if(jsonArray !=null && !jsonArray.isEmpty()){
                for (int i = 0; i < jsonArray.size(); i++) {
                    String columnObj = String.valueOf(jsonArray.get(i));
                    Integer expressionId = (Integer) JSON.parseObject(columnObj).get("expression_id");
                    if(expressionId != null){
                        returnList.add(expressionId);
                    }
                }
            }
            log.info("获取考勤统计报表列定义:{}", result);
        }
        return returnList;
    }

    /**
     * 根据列值获取考勤统计报表列值
     */
    public static JSONObject getColumnval(List<Integer> expressionIdList,String dingUserId,int year,int month){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date newStartDate = null;
        try {
            startDate = sdf.parse(year + "-" + month + "-" + "01");
//            newStartDate = DateUtil.offsetMonth(startDate,-1);//上月
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = DateUtil.parse(getLastDayOfMonth(year,month));
//        DateTime newEndDate = DateUtil.offsetMonth(endDate, -1);//上月
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getcolumnval");
        OapiAttendanceGetcolumnvalRequest req = new OapiAttendanceGetcolumnvalRequest();
        req.setUserid(dingUserId);
        req.setColumnIdList(StringUtils.strip(expressionIdList.toString(),"[]"));
        req.setFromDate(startDate);
        req.setToDate(endDate);
        OapiAttendanceGetcolumnvalResponse rsp = null;
        try {
            try {
                //单个接口的频率不可超过40次/秒，否则返回错误码90018
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rsp = client.execute(req, dingConfig.getAccessToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        if (rsp != null) {
            log.info(rsp.getBody());
            jsonObject = JSON.parseObject(rsp.getBody());
        }
        return jsonObject;
    }

    /**
     * 根据钉钉userId获取员工
     */
    public static void getUserByDingUserId(String dingUserId){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
        OapiUserGetRequest req = new OapiUserGetRequest();
        req.setUserid("256464095936234971");
        req.setHttpMethod("GET");
        OapiUserGetResponse rsp = null;
        try {
            rsp = client.execute(req, dingConfig.getAccessToken());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(rsp.getBody());

    }

    /**
     * 获取应出勤天数
     */
    public static Float getShouldAttDays(String dingUserId,int year,int month){
        Float num = 0.0F;
        JSONArray columnArray = getColumnArray(dingUserId, year, month, shouldAttDay, "获取应出勤天数统计报表:{}");
        if (columnArray !=null && !columnArray.isEmpty()) {
            for (int i = 0; i < columnArray.size(); i++) {
                String data = String.valueOf(columnArray.get(i));
                String value = (String) JSON.parseObject(data).get("value");
                if(Float.parseFloat(value) == 1.0){
                    num++;
                }
                if(Float.parseFloat(value) == 0.5){
                    num+=0.5F;
                }
            }
        }
        return num;
    }

    /**
     * 获取应出勤的日期和工时映射
     * @return
     */
    public static Map<String,Object> getShouldAttDateAndWorkHour(String dingUserId,int year,int month){
        Map<String,Object> map = DingDingUtil.getDateAndClassName(dingUserId,year,month);
        Iterator<Map.Entry<String, Object>> entryIterator = map.entrySet().iterator();
        Map<String,Object> dateWorkHourMap = new TreeMap<>();
        while(entryIterator.hasNext()){
            Map.Entry<String, Object> entry = entryIterator.next();
//            System.out.println(entry.getKey() + " : " + entry.getValue());
            String value = String.valueOf(entry.getValue());
            if(!value.contains("休息")){
                String className = (String) entry.getValue();
                Double workHour = DingDingUtil.getClassWorkHour(dingUserId, DingDingUtil.getClassIdByName(dingUserId, className));
                dateWorkHourMap.put(entry.getKey(),workHour);
            }
        }
//        dateWorkHourMap.entrySet().stream().forEach(entry -> {
//            System.out.println(entry.getKey() + " : " + entry.getValue());
//        });
        return dateWorkHourMap;
    }

    /**
     * 获取应公休的日期集合
     * @return
     */
    public static List<String> getShouldRestDays(String dingUserId,int year,int month){
        Map<String,Object> map = DingDingUtil.getDateAndClassName(dingUserId,year,month);
        Iterator<Map.Entry<String, Object>> entryIterator = map.entrySet().iterator();
        List<String> returnList = new ArrayList<>();
        while(entryIterator.hasNext()){
            Map.Entry<String, Object> entry = entryIterator.next();
            if("休息".equals(entry.getValue())){
                returnList.add(entry.getKey());
            }
        }
        return returnList;
    }

    /**
     * 获取实际出勤的日期和工时映射
     * @return
     */
    public static Map<String,Object> getActualAttDateAndWorkHour(String dingUserId,int year,int month){
        Map<String,Object> map = DingDingUtil.getShouldAttDateAndWorkHour(dingUserId,year,month);
        Map<String,Object> dateWorkHourMap = new TreeMap<>();
        List<String> actualAttDays = getActualAttDays(dingUserId,year,month);
        actualAttDays.forEach(actualDay -> {
            double workHour = map.get(actualDay) == null ? 0D : (double)map.get(actualDay);
            dateWorkHourMap.put(actualDay,workHour);
        });
        return dateWorkHourMap;
    }

    /**
     * 获取班次中的最长工时（假设该员工有多个班次）
     * @return
     */
    public static double getMaxWorkHour(String dingUserId,int year,int month){
        Map<String,Object> map = DingDingUtil.getShouldAttDateAndWorkHour(dingUserId,year,month);
        if (map != null && !map.isEmpty()) {
            return map.entrySet().stream().mapToDouble(entry -> (double) entry.getValue()).max().getAsDouble();
        }
        return 0D;
    }

    /**
     * 获取实际出勤日期集合
     */
    public static List<String> getActualAttDays(String dingUserId,int year,int month){
        List<String> returnList = new ArrayList<>();
        JSONArray columnArray = getColumnArray(dingUserId, year, month, actualAttDay, "获取实际出勤天数统计报表:{}");
        if (columnArray !=null && !columnArray.isEmpty()) {
            for (int i = 0; i < columnArray.size(); i++) {
                String data = String.valueOf(columnArray.get(i));
                String date = (String) JSON.parseObject(data).get("date");
                String value = (String) JSON.parseObject(data).get("value");
                if(!"0.0".equals(value)){
                    returnList.add(date);
                }
            }
        }
        return returnList;
    }

    /**
     * 获取实际休息日期集合
     */
    public static List<String> getActualRestDays(String dingUserId,int year,int month){
        List<String> returnList = new ArrayList<>();
        JSONArray columnArray = getColumnArray(dingUserId, year, month, actualRestDay, "获取休息天数统计报表:{}");
        if (columnArray !=null && !columnArray.isEmpty()) {
            for (int i = 0; i < columnArray.size(); i++) {
                String data = String.valueOf(columnArray.get(i));
                String date = (String) JSON.parseObject(data).get("date");
                String value = (String) JSON.parseObject(data).get("value");
                if("1".equals(value)){
                    returnList.add(date);
                }
            }
        }
        return returnList;
    }

    /**
     * 获取旷工日期集合
     */
    public static List<String> getAbsentDays(String dingUserId,int year,int month){
        List<String> returnList = new ArrayList<>();
        JSONArray columnArray = getColumnArray(dingUserId, year, month, absentDay, "获取旷工统计报表:{}");
        if (columnArray !=null && !columnArray.isEmpty()) {
            for (int i = 0; i < columnArray.size(); i++) {
                String data = String.valueOf(columnArray.get(i));
                String date = (String) JSON.parseObject(data).get("date");
                String value = (String) JSON.parseObject(data).get("value");
                if("1".equals(value)){
                    returnList.add(date);
                }
            }
        }
        return returnList;
    }

    /**
     * 获取日期与班次名的映射
     */
    public static Map<String,Object> getDateAndClassName(String dingUserId,int year,int month){
        Map<String,Object> returnMap = new TreeMap<>();
        JSONArray columnArray = getColumnArray(dingUserId, year, month, attClassData, "获取员工当月班次统计报表:{}");
        if (columnArray !=null && !columnArray.isEmpty()) {
            for (int i = 0; i < columnArray.size(); i++) {
                String DateAndClassNameData = String.valueOf(columnArray.get(i));
                String date = (String) JSON.parseObject(DateAndClassNameData).get("date");
                String value = (String) JSON.parseObject(DateAndClassNameData).get("value");
                String className = value.split(" ")[0];
                returnMap.put(date,className);
            }
        }
        return returnMap;
    }

    /**
     * 获取班次信息
     */
    public static Set<String> getClassDate(String dingUserId,int year,int month){
        Set<String> returnSet = new HashSet<>();
        JSONArray columnArray = getColumnArray(dingUserId, year, month, attClassData, "获取员工当月班次统计报表:{}");
        if (columnArray !=null && !columnArray.isEmpty()) {
            for (int i = 0; i < columnArray.size(); i++) {
                String DateAndClassNameData = String.valueOf(columnArray.get(i));
                String value = (String) JSON.parseObject(DateAndClassNameData).get("value");
                if (!value.contains("休息")) {
                    returnSet.add(value);
                }
            }
        }
        return returnSet;
    }

    /**
     * 按名称搜索班次
     */
    public static Long getClassIdByName(String dingUserId,String className){
        Long classId = null;
        if (StringUtils.isNotBlank(className)) {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/shift/search");
            OapiAttendanceShiftSearchRequest req = new OapiAttendanceShiftSearchRequest();
            req.setOpUserId(dingUserId);
            req.setShiftName(className);
            OapiAttendanceShiftSearchResponse rsp = null;
            try {
                rsp = client.execute(req, dingConfig.getAccessToken());
            } catch (ApiException e) {
                e.printStackTrace();
            }
            if (rsp != null) {
                JSONObject jsonObject = JSON.parseObject(rsp.getBody());
                if (jsonObject != null) {
                    JSONArray jsonArray = (JSONArray) jsonObject.get("result");
                    if(jsonArray != null && !jsonArray.isEmpty()){
                        for (int i=0;i<jsonArray.size();i++) {
                            JSONObject classData = (JSONObject) jsonArray.get(i);
                            Long dingClassId = Long.parseLong(String.valueOf(classData.get("id")));
                            String dingClassName = String.valueOf(classData.get("name"));
                            if (className.equals(dingClassName)) {
                                classId = dingClassId;
                            }
                        }
                        return classId;
                    }
                }
            }
        }
        return classId;
    }

    /**
     * 获取班次工时
     */
    public static Double getClassWorkHour(String dingUserId,Long classId){
        if(classId != null){
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/shift/query");
            OapiAttendanceShiftQueryRequest req = new OapiAttendanceShiftQueryRequest();
            req.setOpUserId(dingUserId);
            req.setShiftId(classId);
            OapiAttendanceShiftQueryResponse rsp = null;
            try {
                try {
                    //单个接口的频率不可超过40次/秒，否则返回错误码90018
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rsp = client.execute(req, dingConfig.getAccessToken());
            } catch (ApiException e) {
                e.printStackTrace();
            }
            if (rsp != null) {
                JSONObject jsonObject = JSON.parseObject(rsp.getBody());
                if (jsonObject != null) {
                    String result = String.valueOf(jsonObject.get("result"));
                    if (StringUtils.isNotBlank(result)) {
                        String classSetting = String.valueOf(JSON.parseObject(result).get("shift_setting"));
                        if (StringUtils.isNotBlank(classSetting)) {
                            Double workMinute = Double.parseDouble(String.valueOf(JSON.parseObject(classSetting).get("work_time_minutes")));
                            return Arith.div(workMinute, 60);
                        }
                    }
                }
            }
        }
        return 0D;
    }

    /**
     * 获取休息日加班日期与时长映射
     */
    public static Map<String,Object> getRestOvertimes(String dingUserId,int year,int month){
        Map<String,Object> returnMap = new TreeMap<>();
        JSONArray columnArray = getColumnArray(dingUserId, year, month, holidayOvertime, "获取员工当月休息日加班统计报表:{}");
        if (columnArray !=null && !columnArray.isEmpty()) {
            for (int i = 0; i < columnArray.size(); i++) {
                String data = String.valueOf(columnArray.get(i));
                String date = (String) JSON.parseObject(data).get("date");
                String value = (String) JSON.parseObject(data).get("value");
                returnMap.put(date,value);
            }
        }
        return returnMap;
    }

    /**
     * 获取节假日加班日期与时长映射
     */
    public static Map<String,Object> getHolidayOvertimes(String dingUserId,int year,int month){
        Map<String,Object> returnMap = new TreeMap<>();
        JSONArray columnArray = getColumnArray(dingUserId, year, month, workdayOvertime, "获取员工当月节假日加班统计报表:{}");
        if (columnArray !=null && !columnArray.isEmpty()) {
            for (int i = 0; i < columnArray.size(); i++) {
                String data = String.valueOf(columnArray.get(i));
                String date = (String) JSON.parseObject(data).get("date");
                String value = (String) JSON.parseObject(data).get("value");
                returnMap.put(date,value);
            }
        }
        return returnMap;
    }

    /**
     * 获取请假数据列表
     * @param dingUserId
     * @param year
     * @param month
     * @return
     */
    public static List<String> getLeaveProcessInstance(String dingUserId,int year,int month){
        List<String> returnList = new ArrayList<>();
        try {
            Set<String> processCodeList = getProcessInstanceIds(new HashSet<String>(),0L,"请假申请流程",dingUserId,year,month);
//            System.out.println("processCodeList: " + processCodeList.toString());
            if (processCodeList !=null && !processCodeList.isEmpty()) {
                Iterator<String> iterator = processCodeList.iterator();
                while (iterator.hasNext()) {
                    String processInstanceId = iterator.next();
                    //根据审批实例ID，获取审批实例详情（详情包括审批表单信息、操作记录列表、操作人、抄送人、审批任务列表）
                    DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/get");
                    OapiProcessinstanceGetRequest request = new OapiProcessinstanceGetRequest();
                    request.setProcessInstanceId(processInstanceId);
                    OapiProcessinstanceGetResponse response = client.execute(request,dingConfig.getAccessToken());
                    JSONObject jsonObject = JSON.parseObject(response.getBody());
//                    System.out.println("请假数据：" + response.getBody());
                    JSONObject processObj = (JSONObject) jsonObject.get("process_instance");
                    if (processObj != null) {
                        String status = String.valueOf(processObj.get("status"));//只获取审批完成的数据
                        if (StringUtils.isNotBlank(status) && "COMPLETED".equals(status)) {
                            JSONArray jsonArray = (JSONArray) processObj.get("form_component_values");
                            for (int i=0;i<jsonArray.size();i++) {
                                JSONObject formObj = (JSONObject) jsonArray.get(i);
                                String componentType = String.valueOf(formObj.get("component_type"));
                                if ("DDHolidayField".equals(componentType)) {
                                    returnList.add(String.valueOf(formObj.get("value")));
                                }
                            }
                        }
                    }
                }
            }
//            System.out.println("请假数据： "+JSON.parseObject(response.getBody()));
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    /**
     * 根据流程模板名称获取流程模板code
     */
    public static String getProcessCode(String processName){
        try {
            DingTalkClient leaveClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/process/get_by_name");
            OapiProcessGetByNameRequest leaveReq = new OapiProcessGetByNameRequest();
            leaveReq.setName(processName);
            OapiProcessGetByNameResponse leaveRsp = leaveClient.execute(leaveReq, dingConfig.getAccessToken());
            if (leaveRsp != null) {
                JSONObject leaveObject = JSON.parseObject(leaveRsp.getBody());
                String processCode = String.valueOf(leaveObject.get("process_code"));
                if (StringUtils.isNotBlank(processCode)){
                    return processCode;
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 递归返回审批实例ID列表
     */
    public static Set<String> getProcessInstanceIds(Set<String> proInstanceIdSet,long cursor,String processName,String userIds,int year,int month){
        String processCode = getProcessCode(processName);
        if (StringUtils.isNotBlank(processCode)) {
            OapiProcessinstanceListidsResponse response = null;
            try {
                response = getOapiProcessinstanceListidsResponse(cursor, processCode, userIds, year, month);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = JSON.parseObject(response.getBody());
            String result = String.valueOf(jsonObject.get("result"));
            Integer nextCursorObj = (Integer) JSON.parseObject(result).get("next_cursor");
            JSONArray jsonArray = (JSONArray) JSON.parseObject(result).get("list");

            if(jsonArray !=null && !jsonArray.isEmpty()){
                for (int i = 0; i < jsonArray.size(); i++) {
                    String proInstanceId = String.valueOf(jsonArray.get(i));
                    proInstanceIdSet.add(proInstanceId);
                }
            }

            if (nextCursorObj == null){//如果下一页的游标为空，则只返回当前页数据即可
                return proInstanceIdSet;
            }
            //如果下一页的游标不为空，则递归获取数据
            Long nextCursor = Long.parseLong(String.valueOf(nextCursorObj));
            return getProcessInstanceIds(proInstanceIdSet,nextCursor,processCode,userIds,year,month);
        }
        return proInstanceIdSet;
    }

    /**
     * 获取审批实例ID列表
     * @param cursor 游标
     * @param processCode 审批流的唯一码 （process_code在审批流编辑页面的URL中获取）
     * @param userIds
     * @param year
     * @param month
     * @return
     * @throws ApiException
     */
    private static OapiProcessinstanceListidsResponse getOapiProcessinstanceListidsResponse(long cursor, String processCode,String userIds, int year, int month) throws ApiException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date newStartDate = null;
        try {
            startDate = sdf.parse(year + "-" + month + "-" + "01");//本月1号
//            newStartDate = DateUtil.offsetMonth(startDate, -1);//上月
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = DateUtil.offsetMonth(startDate, 1);//下月1号
//        DateTime newEndDate = DateUtil.offsetMonth(endDate, -1);//上月

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(startDate);
        endCal.setTime(endDate);
        long startTimestamp = startCal.getTimeInMillis();
        long endTimestamp = endCal.getTimeInMillis();

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/listids");
        OapiProcessinstanceListidsRequest req = new OapiProcessinstanceListidsRequest();
        req.setProcessCode(processCode);
        req.setStartTime(startTimestamp);
        req.setEndTime(endTimestamp);//结束时间。不传该参数则默认取当前时间。
        req.setSize(10L);
        req.setCursor(cursor);
        req.setUseridList(userIds);
        return client.execute(req, dingConfig.getAccessToken());
    }

    /**
     * 获取工作日加班日期与时长映射
     */
    public static Map<String,Object> getWorkdayOvertimes(String dingUserId,int year,int month){
        Map<String,Object> returnMap = new TreeMap<>();
        JSONArray columnArray = getColumnArray(dingUserId, year, month, restOvertime, "获取员工当月工作日加班统计报表:{}");
        if (columnArray !=null && !columnArray.isEmpty()) {
            for (int i = 0; i < columnArray.size(); i++) {
                String data = String.valueOf(columnArray.get(i));
                String date = (String) JSON.parseObject(data).get("date");
                String value = (String) JSON.parseObject(data).get("value");
                returnMap.put(date,value);
            }
        }
        return returnMap;
    }

    /**
     *获取钉钉智能考勤报表的列值数据
     * @param dingUserId
     * @param year
     * @param month
     * @param columnId 列ID
     * @param message 抛出的异常信息
     * @return
     */
    private static JSONArray getColumnArray(String dingUserId, int year, int month, Integer columnId, String message) {
        List<Integer> attColumns = new ArrayList<>();
        JSONArray jsonArray = null;
        attColumns.add(columnId);
        JSONObject jsonObject = DingDingUtil.getColumnval(attColumns, dingUserId, year, month);
        if (jsonObject != null) {
            String result = String.valueOf(jsonObject.get("result"));
            if (StringUtils.isNotBlank(result)) {
                jsonArray = (JSONArray) JSON.parseObject(result).get("column_vals");
                if (jsonArray !=null && !jsonArray.isEmpty()) {
                    String columnObj = String.valueOf(jsonArray.get(0));
                    JSONArray columnArray = (JSONArray) JSON.parseObject(columnObj).get("column_vals");
                    if (columnArray !=null && !columnArray.isEmpty()) {
                        return columnArray;
                    }
                }
            }
            log.info(message,jsonObject);
        }
        return jsonArray;
    }

    /**
     * 返回传入年月的上个月最后一天的日期
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year,int month){
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

    public static String getDingDingUserIdByPhone(String phonenumber) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get_by_mobile");
        OapiUserGetByMobileRequest req = new OapiUserGetByMobileRequest();
        req.setMobile(phonenumber);
        req.setHttpMethod("GET");
        OapiUserGetByMobileResponse rsp = client.execute(req, dingConfig.getAccessToken());
        if (rsp != null) {
            JSONObject userObject = JSON.parseObject(rsp.getBody());
            Object userid = userObject.get("userid");
            if(userid == null){
                log.error("找不到手机号为[{}]用户的钉钉userid。",phonenumber);
                return null;
            }
            String dingdingUserId = String.valueOf(userid);
            if (StringUtils.isNotBlank(dingdingUserId)){
                return dingdingUserId;
            }
        }
        return null;
    }
}
