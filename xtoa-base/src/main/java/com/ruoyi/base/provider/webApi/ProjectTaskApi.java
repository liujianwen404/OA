package com.ruoyi.base.provider.webApi;

import com.ruoyi.base.domain.ProjectPlanTask;
import com.ruoyi.common.core.domain.AjaxResult;

public interface ProjectTaskApi {

    public ProjectPlanTask findById(Long projectPlanTaskId);

    public AjaxResult updateprojectPlanTask(Long projectPlanTaskId);

}
