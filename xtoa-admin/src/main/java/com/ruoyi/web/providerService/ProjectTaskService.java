package com.ruoyi.web.providerService;

import com.ruoyi.base.domain.ProjectPlanTask;
import com.ruoyi.base.provider.webApi.ProjectTaskApi;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.service.IProjectPlanTaskService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProjectTaskService implements ProjectTaskApi {


    @Autowired
    private IProjectPlanTaskService projectPlanTaskService;

    public ProjectPlanTask findById(Long projectPlanTaskId)
    {
        try {
            return projectPlanTaskService.findById(projectPlanTaskId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AjaxResult updateprojectPlanTask(Long projectPlanTaskId)
    {
        try {
            return projectPlanTaskService.updateprojectPlanTask(projectPlanTaskId);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

}
