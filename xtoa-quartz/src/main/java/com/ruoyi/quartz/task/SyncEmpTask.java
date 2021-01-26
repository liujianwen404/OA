//package com.ruoyi.quartz.task;
//
//import com.ruoyi.base.dingTalk.DingUserApi;
//import com.taobao.api.ApiException;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import javax.annotation.Resource;
//
//@Configuration
//@EnableScheduling
//public class SyncEmpTask {
//    @Resource
//    private DingUserApi dingUserApi;
//
//
//    @Scheduled(cron = "0/5 * * * * ?")
//    private void configureTasks() throws ApiException {
//        dingUserApi.getDingDeptList();
//    }
//}
