package com.ruoyi.quartz.task;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.utils.enums.SysConfigEnum;
import com.ruoyi.hr.service.IDingdingMsgService;
import com.ruoyi.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("workPlanTask")
public class WorkPlanTask
{
    @Autowired
    private IDingdingMsgService dingdingMsgService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Value("${dingMsgUrl}")
    private String dingMsgUrl;

    private static final Logger log = LoggerFactory.getLogger(WorkPlanTask.class);

    public void excute(Integer empId, String empName, String dateToStr)
    {
        log.info("执行工作计划定时任务方法，开始=============================");
        String img = sysConfigService.selectConfigByKey(SysConfigEnum.SysConfig.processImage.getValue());
//        String empIds = empId.toString();
        String empIds = "1000211,1000370";//刘剑文，胡莹莹
        String now = DateUtil.now();
        String text = empName + "，当前时间:"+now+",您有一条工作计划将于 "+dateToStr+" 开始，请提前做好准备！";
        String title = "工作计划提醒";
        String bizId = "";
        String url = dingMsgUrl + "?userIds=" + empIds;
        dingdingMsgService.sendLnikMsg(img
                , empIds
                , bizId
                , text
                , title
                ,url);
        log.info("执行工作计划定时任务方法，结束=============================");
    }

}
