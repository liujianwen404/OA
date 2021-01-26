package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.enums.SysConfigEnum;
import com.ruoyi.base.domain.ProjectPlanTask;
import com.ruoyi.hr.service.IProjectPlanTaskService;
import com.ruoyi.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 未完成的任务发送钉钉工作通知
 */
@Component("projectPlanTaskMsg")
public class ProjectPlanTaskMsg {

    private Logger logger = LoggerFactory.getLogger(ProjectPlanTaskMsg.class);

    @Autowired
    private IProjectPlanTaskService projectPlanTaskService;

    @Autowired
    private ISysConfigService sysConfigService;


    @Value("${dingWorkRecordUrl}")
    private String dingWorkRecordUrl;

    public void sendRemindMsg(){
        //获取未完成的任务（项目，计划都处于非关闭或完成的状态）

        logger.info("未完成的任务发送钉钉工作通知");

        List<ProjectPlanTask> projectPlanTaskList = projectPlanTaskService.seletUnfinishedTask();
        String img = sysConfigService.selectConfigByKey(SysConfigEnum.SysConfig.projectTaskImage.getValue());
        projectPlanTaskList.forEach(projectPlanTask -> {
            try {
                //接口配额
                Thread.sleep(100);
                projectPlanTaskService.sendMsg(img, projectPlanTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
