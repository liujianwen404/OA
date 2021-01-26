package com.xtoa.web.controller;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.base.domain.*;
import com.ruoyi.base.provider.webApi.ProjectTaskApi;
import com.ruoyi.base.provider.hrService.*;
import com.ruoyi.base.utils.DingdingMsgEnum;
import com.ruoyi.base.utils.HoildayAndLeaveType;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequestMapping("/webApi")
public class WorkRecordController {
    private static final Logger logger = LoggerFactory.getLogger(WorkRecordController.class);

    @Reference(retries = 0,check=false)
    private ProjectTaskApi projectTaskApi;

    @Reference(retries = 0,check=false)
    private IHrOffService hrOffService;

    @Reference(retries = 0,check=false)
    private IHrQuitService hrQuitService;

    @Reference(retries = 0,check=false)
    private IHrLeaveService hrLeaveService;

    @Reference(retries = 0,check=false)
    private IHrRegularService hrRegularService;

    @Reference(retries = 0,check=false)
    private IHrOvertimeService hrOvertimeService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysPostService sysPostService;


    @RequestMapping("/workRecord")
    public String wordRecord(HttpServletRequest request,ModelMap modelMap,String userId,Long projectPlanTaskId,
                             String bizId,String type,String userIds){
        if (Objects.equals(type,DingdingMsgEnum.MsgBizType.projectTaskMsg.getValue()+"")){
            if (projectTaskMsg(modelMap, projectPlanTaskId)) {
                return "error";
            }
            return "webApi/workRecordPC";
        }else if (Objects.equals(type,ProcessKey.userDefined01Offer)){
            offerProcessMsg(modelMap, bizId);
            return "webApi/hrOffProcess";
        }else if (Objects.equals(type,ProcessKey.userDefined01Leave)){
            leverProcessMsg(modelMap, bizId);
            return "webApi/hrLeverProcess";
        }else if (Objects.equals(type,ProcessKey.userDefined01Regular)){
            regularProcessMsg(modelMap, bizId);
            return "webApi/regularProcess";
        }else if (Objects.equals(type,ProcessKey.userDefined01Quit)){
            quitProcessMsg(modelMap, bizId);
            return "webApi/quitProcess";
        }else if (Objects.equals(type,ProcessKey.userDefined01OverTime)){
            overTimeProcessMsg(modelMap, bizId);
            return "webApi/overTimeProcess";
        }
        return "error";
    }
    private void overTimeProcessMsg(ModelMap modelMap, String bizId) {
        HrOvertime hrOvertime = hrOvertimeService.selectHrOvertimeById(Long.parseLong(bizId));
        setBasics(modelMap, hrOvertime.getDeptId(),hrOvertime.getPostId(),hrOvertime.getId(),hrOvertime.getApplyUserName());
        modelMap.put("type",hrOvertime.getType());

        String type = hrOvertime.getType();
        String typeStr = "";
        switch (type){
            case "1":
                typeStr = "法定假加班";
                break;
            case "2":
                typeStr = "平时加班";
                break;
        }


        modelMap.put("typeStr",typeStr);
        modelMap.put("reason",hrOvertime.getReason());
        modelMap.put("startTime",DateUtil.format(hrOvertime.getStartTime(),"yyyy-MM-dd"));
        modelMap.put("endTime",DateUtil.format(hrOvertime.getEndTime(),"yyyy-MM-dd"));
        modelMap.put("totalTimes",hrOvertime.getTotalTimes());
        modelMap.put("applyUserName",hrOvertime.getApplyUserName());

    }
    private void regularProcessMsg(ModelMap modelMap, String bizId) {
        HrRegular hrRegular = hrRegularService.selectHrRegularById(Long.parseLong(bizId));
        setBasics(modelMap, hrRegular.getDeptId(),hrRegular.getPostId(),hrRegular.getId(),hrRegular.getApplyUserName());
        modelMap.put("trial",hrRegular.getTrial());
        modelMap.put("sumUp",hrRegular.getSumUp());
        modelMap.put("suggest",hrRegular.getSuggest());
        modelMap.put("reason",hrRegular.getReason());
        modelMap.put("regularTime",DateUtil.format(hrRegular.getRegularTime(),"yyyy-MM-dd"));
        modelMap.put("nonManagerDate",DateUtil.format(hrRegular.getNonManagerDate(),"yyyy-MM-dd"));
        modelMap.put("applyUserName",hrRegular.getApplyUserName());

    }

    private void setBasics(ModelMap modelMap, Long deptId,Long postId, Long id,String applyUserName) {
        modelMap.put("id",id);
        modelMap.put("deptId",deptId);
        modelMap.put("postId",postId);
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        setShowDempName(sysDept);
        modelMap.put("deptName", sysDept.getShowName());
        modelMap.put("postName",sysPostService.selectPostById(postId).getPostName());

        modelMap.put("applyUserName",applyUserName);
    }

    private void setShowDempName(SysDept sysDept) {
        String depdName = sysDept.getDeptName();
        String[] split = sysDept.getAncestors().split(",");
        for (int i = split.length - 1; i > 1; i--) {
            SysDept sysDeptparen = sysDeptService.selectDeptById(Long.parseLong(split[i]));
            if (sysDeptparen != null){
                depdName = sysDeptparen.getDeptName() + ";  " + depdName;
            }
        }
        sysDept.setShowName(depdName);
    }

    private void leverProcessMsg(ModelMap modelMap, String bizId) {
        HrLeave hrLeave = hrLeaveService.selectHrLeaveById(Long.parseLong(bizId));
        modelMap.put("id",hrLeave.getId());
        modelMap.put("deptId",hrLeave.getDeptId());
        modelMap.put("postId",hrLeave.getPostId());
        SysDept sysDept = sysDeptService.selectDeptById(hrLeave.getDeptId());
        setShowDempName(sysDept);
        modelMap.put("deptName", sysDept.getShowName());
        modelMap.put("postName",sysPostService.selectPostById(hrLeave.getPostId()).getPostName());

        String typeLever = hrLeave.getType();
        String typeStr = HoildayAndLeaveType.getHoildayAndLeaveStr(typeLever);

        modelMap.put("typeStr",typeStr);
        modelMap.put("reason",hrLeave.getReason());
        modelMap.put("startTime",DateUtil.format(hrLeave.getStartTime(),"yyyy-MM-dd hh:mm"));
        modelMap.put("endTime",DateUtil.format(hrLeave.getEndTime(),"yyyy-MM-dd hh:mm"));
        modelMap.put("applyUserName",hrLeave.getApplyUserName());
    }

    private void offerProcessMsg(ModelMap modelMap, String bizId) {
        HrOff hrOff = hrOffService.selectHrOffById(Long.parseLong(bizId));
        modelMap.put("id",hrOff.getId());
        modelMap.put("deptId",hrOff.getDeptId());
        modelMap.put("postId",hrOff.getPostId());
        SysDept sysDept = sysDeptService.selectDeptById(hrOff.getDeptId());
        setShowDempName(sysDept);
        modelMap.put("deptName", sysDept.getShowName());
        modelMap.put("postName",sysPostService.selectPostById(hrOff.getPostId()).getPostName());
        modelMap.put("offName",hrOff.getToOffName());
        modelMap.put("offEmail",hrOff.getToOffEmail());
        modelMap.put("adjunct",hrOff.getAdjunct());

    }

    private void quitProcessMsg(ModelMap modelMap, String bizId) {
        HrQuit hrQuit = hrQuitService.selectHrQuitById(Long.parseLong(bizId));
        setBasics(modelMap, hrQuit.getDeptId(),hrQuit.getPostId(),hrQuit.getId(),hrQuit.getApplyUserName());
        modelMap.put("quitTime",DateUtil.format(hrQuit.getQuitTime(),"yyyy-MM-dd "));
        modelMap.put("reason",hrQuit.getReason());
    }

    private boolean projectTaskMsg(ModelMap modelMap, Long projectPlanTaskId) {
        ProjectPlanTask projectPlanTask = projectTaskApi.findById(projectPlanTaskId);
        if (projectPlanTask == null){
            return true;
        }
        modelMap.put("projectPlanTask",projectPlanTask);
        return false;
    }

    @RequestMapping("/updateprojectPlanTask")
    @ResponseBody
    @Transactional
    public AjaxResult updateprojectPlanTask(Long projectPlanTaskId){
        try {

            logger.info("projectPlanTaskId:"+projectPlanTaskId+":操作成功");
            return projectTaskApi.updateprojectPlanTask(projectPlanTaskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("projectPlanTaskId:"+projectPlanTaskId+":操作失败2");
        return AjaxResult.error("操作失败");
    }


}
