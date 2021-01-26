import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.google.common.collect.Lists;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.base.dingTalk.DingUserApi;
import com.ruoyi.base.service.MQService;
import com.ruoyi.base.utils.DingDingUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.taobao.api.ApiException;
import com.xtoa.web.WebApiApplication;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApiApplication.class)//这里是springBoot启动类
//@ContextConfiguration(classes = {I18Config.class, MybatisConfig.class, MybatisMapperScannerConfig.class, PropertiesConfig.class, QuartzConfiguration.class, RedisConfig.class})
public class ZegoUtilTest {


    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }
    
    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

    @Autowired
    private DingConfig dingConfig;

    @Autowired
    private MQService mqService;

    @Autowired
    private DingUserApi dingUserApi;

    @Autowired
    private ISysUserService sysUserService;


    @SneakyThrows
    @Test
    public void test01() {
        List<String> leaveProcessInstance = DingDingUtil.getLeaveProcessInstance("163039176233046785", 2020, 11);
        double hours = 0D;
        if (!leaveProcessInstance.isEmpty()) {
            for (String s : leaveProcessInstance) {
                hours += Double.parseDouble(s.split(",")[2]);
            }
        }
        System.out.println("请假总时长：" + hours);
        System.out.println(leaveProcessInstance.toString());
    }

    @SneakyThrows
    @Test
    public void test02(){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getattcolumns");
        OapiAttendanceGetattcolumnsRequest req = new OapiAttendanceGetattcolumnsRequest();
        OapiAttendanceGetattcolumnsResponse rsp = client.execute(req, dingConfig.getAccessToken());
        System.out.println(rsp.getBody());
    }

    @SneakyThrows
    @Test
    public void test03(){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getcolumnval");
        OapiAttendanceGetcolumnvalRequest req = new OapiAttendanceGetcolumnvalRequest();
        req.setUserid("145333204120875630");
        req.setColumnIdList("36443456,36443455,36443458,36443457");
        req.setFromDate(DateUtil.parse("2020-12-01"));
        req.setToDate(DateUtil.parse("2020-12-18"));
        OapiAttendanceGetcolumnvalResponse rsp = client.execute(req, dingConfig.getAccessToken());
        System.out.println(rsp.getBody());
    }


    @Test
    public void test04(){
        List<SysUser> userList = sysUserService.selectUserList(new SysUser());
        List<String> phoneList = userList.stream().map(SysUser::getPhonenumber).collect(Collectors.toList());

        phoneList.forEach(phone -> {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get_by_mobile");
            OapiUserGetByMobileRequest req = new OapiUserGetByMobileRequest();
            req.setMobile(phone);
            req.setHttpMethod("GET");
            OapiUserGetByMobileResponse rsp = null;
            try {
                rsp = client.execute(req, dingConfig.getAccessToken());
            } catch (ApiException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = JSON.parseObject(rsp.getBody());
            String userid = String.valueOf(jsonObject.get("userid"));
            SysUser sysUser = sysUserService.selectUserByPhoneNumber(phone);
            sysUser.setDingUserId(userid);
            sysUserService.updateUserInfo(sysUser);
        });

    }

    /**
     * 应出勤天数 36443430
     * 实出勤天数 36443433
     * 实休息天数 36443434
     * 考勤结果 36443453
     * 出勤班次 36443432
     * 班次 36443454
     * 休息日加班 36443451
     * 节假日加班 36443452
     * 工作日加班 36443450
     */
    @SneakyThrows
    @Test
    public void test05(){
//        List<Integer> attColumns = DingDingUtil.getAttColumns();
//        List<List<Integer>> partition = Lists.partition(attColumns, 20);
//        partition.forEach(list -> {
//            DingDingUtil.getColumnval(list,"093022262537664610");
//        });
//        attColumns.forEach(System.out::println);

        List<Integer> attColumns = new ArrayList<>();
//        attColumns.add(36443445);
//        attColumns.add(36443453);
//        attColumns.add(36443433);
//        attColumns.add(36443434);
//        attColumns.add(36443454);
//        attColumns.add(36443433);
//        attColumns.add(36443451);
//        attColumns.add(96981711);
//        DingDingUtil.getColumnval(attColumns,"145333204120875630",2021,1);
//        Float num1 = DingDingUtil.getShouldAttDays();
//        Float num2 = DingDingUtil.getActualAttDays("093022262537664610");
//        Float num3 = DingDingUtil.getRestDays("093022262537664610");
//        System.out.println("应出勤天数： " + num1);
//        System.out.println("实际出勤天数：" + num2);
//        System.out.println("休息天数：" + num3);
//        long startTime = System.currentTimeMillis();
//        Map<String, Object> shouldAttDateAndWorkHour = DingDingUtil.getShouldAttDateAndWorkHour("145333204120875630",2020,12);
//        List<String> restDays = DingDingUtil.getShouldRestDays("145333204120875630", 2020, 12);
//        double sum = shouldAttDateAndWorkHour.entrySet().parallelStream().mapToDouble(entry -> (double) entry.getValue()).sum();
//        double workHour = DingDingUtil.getMaxWorkHour("093022262537664610");
//        Map<String, Object> actualAttDateAndWorkHour = DingDingUtil.getActualAttDateAndWorkHour("145333204120875630",2020,12);
//        double sum = actualAttDateAndWorkHour.entrySet().parallelStream().mapToDouble(entry -> (double) entry.getValue()).sum();
//        long endTime = System.currentTimeMillis();
//        long count = endTime - startTime;
//        System.out.println("实出勤："+ sum + " 计算耗时：" + count);
//        System.out.println("=================================");
//        List<String> shouldRestDays = DingDingUtil.getRestDays("093022262537664610");
//        shouldRestDays.forEach(System.out::println);
//        double restOvertimes = DingDingUtil.getRestOvertimes("2951393921925060");
//        System.out.println("restOvertimes: " + restOvertimes);
//        double holidayOvertimes = DingDingUtil.getHolidayOvertimes("2951393921925060");
//        System.out.println("holidayOvertimes: " + holidayOvertimes);
//        double workdayOvertimes = DingDingUtil.getWorkdayOvertimes("2951393921925060");
//        System.out.println("workdayOvertimes: " + workdayOvertimes);

//        Map<String, Object> restOvertimeMap = DingDingUtil.getRestOvertimes("2951393921925060");
//        restOvertimeMap.entrySet().stream().map(entry -> "休息日加班：" + entry.getKey() + ": " + entry.getValue()+"小时").forEach(System.out::println);
//        Map<String, Object> holidayOvertimeMap = DingDingUtil.getHolidayOvertimes("2951393921925060");
//        holidayOvertimeMap.entrySet().stream().map(entry -> "节假日加班：" + entry.getKey() + ": " + entry.getValue()+"小时").forEach(System.out::println);
//        Map<String, Object> workdayOvertimeMap = DingDingUtil.getWorkdayOvertimes("2951393921925060");
//        workdayOvertimeMap.entrySet().stream().map(entry -> "工作日加班：" + entry.getKey() + ": " + entry.getValue()+"小时").forEach(System.out::println);
//        Set<String> classDate = DingDingUtil.getClassDate("145333204120875630", 2021, 1);
//        System.out.println("classDate: " + classDate.toString() + ",classDate是否为空：" + classDate.isEmpty());
//        List<String> absentDays = DingDingUtil.getAbsentDays("145333204120875630", 2021, 1);
//        System.out.println("absentDays: " + absentDays.toString() + ",absentDays是否为空：" + absentDays.isEmpty());
//        Map<String, Object> dateAndClassName = DingDingUtil.getDateAndClassName("01005029086733260359", 2020, 11);
//        Long classId = DingDingUtil.getClassIdByName("01005029086733260359", "仓库考勤");
//        Double classWorkHour = DingDingUtil.getClassWorkHour("01005029086733260359", classId);
//        Map<String, Object> shouldAttDateAndWorkHour = DingDingUtil.getShouldAttDateAndWorkHour("01005029086733260359", 2020, 11);
//        double sum = shouldAttDateAndWorkHour.entrySet().parallelStream().mapToDouble(entry -> (double) entry.getValue()).sum();
//        log.info("dateAndClassName:{}",dateAndClassName);
//        log.info("shouldAttDateAndWorkHour:{},sum:{}",shouldAttDateAndWorkHour,sum);
//        Map<String, Object> actualAttDateAndWorkHour = DingDingUtil.getActualAttDateAndWorkHour("145333204120875630", 2020, 12);
//        double sum = actualAttDateAndWorkHour.entrySet().parallelStream().mapToDouble(entry -> (double) entry.getValue()).sum();
//        log.info("actualAttDateAndWorkHour:{},sum:{}",actualAttDateAndWorkHour,sum);
        List<String> leaveData = DingDingUtil.getLeaveProcessInstance("145333204120875630", 2020, 12);
        log.info("leaveData:{}",leaveData);
    }

    @Test
    public void test06(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date newStartDate = null;
        try {
            startDate = sdf.parse(2021 + "-" + 1 + "-" + "01");
            newStartDate = DateUtil.offsetMonth(startDate,-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = DateUtil.parse(DingDingUtil.getLastDayOfMonth(2021,1));
        DateTime newEndDate = DateUtil.offsetMonth(endDate, -1);
        System.out.println("startDate: " + newStartDate + ",endDate: " + newEndDate);
    }

}