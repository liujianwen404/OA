package com.ruoyi.hr.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.base.domain.*;
import com.ruoyi.base.listenerEvent.BizItemInsetEvent;
import com.ruoyi.base.provider.hrService.*;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.enums.SysConfigEnum;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.hr.domain.HrJobTransfer;
import com.ruoyi.hr.domain.HrNonManager;
import com.ruoyi.hr.domain.HrRecruitNeed;
import com.ruoyi.hr.service.*;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BizItemInsetListener implements ApplicationListener<BizItemInsetEvent> {

    private static Logger logger = LoggerFactory.getLogger(BizItemInsetListener.class);

    @Autowired
    private IHrOffService hrOffService;
    @Autowired
    private IHrLeaveService leaveService;
    @Autowired
    private IHrRegularService hrRegularService;
    @Autowired
    private IHrQuitService hrQuitService;
    @Autowired
    private IHrOvertimeService hrOvertimeService;
    @Autowired
    private IHrBusinessTripService businessTripService;
    @Autowired
    private IHrGooutService hrGooutService;
    @Autowired
    private IHrJobTransferService hrJobTransferService;
    @Autowired
    private IHrNonManagerService nonManagerService;
    @Autowired
    private IDingdingMsgService dingdingMsgService;
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IHrFillClockService hrFillClockService;
    @Autowired
    private ISalaryStructureService salaryStructureService;
    @Autowired
    private IHrRecruitNeedService hrRecruitNeedService;
    @Value("${dingMsgUrl}")
    private String dingMsgUrl;
    @Value("${ruoyi.demoEnabled}")
    private String demoEnabled;
    @Autowired
    private RuntimeService runtimeService;

    @Async
    @Override
    public void onApplicationEvent(BizItemInsetEvent bizItemInsetEvent) {

        JSONObject jsonObject = bizItemInsetEvent.getJsonObject();
        logger.info("onApplicationEvent : " + jsonObject.toJSONString());

        String module = jsonObject.getString("module");
        String instanceId = jsonObject.getString("instanceId");
        String loginName = jsonObject.getString("todoUserId");
        SysUser sysUser = sysUserService.selectUserByLoginName(loginName);
        
        if (Objects.equals("0", jsonObject.get("isHandle") + "")
                && StrUtil.isNotBlank(module) && StrUtil.isNotBlank(instanceId) 
                && StrUtil.isNotBlank(loginName) && sysUser != null ) {

            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
            
            if (module.contains(ProcessKey.userDefined01Offer)) {
                //offer流程推送
                offerPushMsg(processInstance,sysUser);
            } else if (module.contains(ProcessKey.userDefined01Leave)) {
                //leave流程推送
                leverPushMsg(processInstance,sysUser);
            } else if (module.contains(ProcessKey.userDefined01Regular)) {
                //转正流程推送
                regularPushMsg(processInstance,sysUser);
            } else if (module.contains(ProcessKey.userDefined01Quit)) {
                //转正流程推送
                quitPushMsg(processInstance,sysUser);
            } else if (module.contains(ProcessKey.userDefined01OverTime)) {
                //加班流程推送
                overTimePushMsg(processInstance,sysUser);
            } else if (module.contains(ProcessKey.userDefined01BusinessTrip)) {
                //出差流程推送
                businessTripPushMsg(processInstance,sysUser);
            } else if (module.contains(ProcessKey.userDefined01Goout)) {
                //外出流程推送
                gooutPushMsg(processInstance,sysUser);
            } else if (module.contains(ProcessKey.userDefined01NonManager)) {
                //入职流程推送
                nonManagerPushMsg(processInstance,sysUser);
            } else if (module.contains(ProcessKey.userDefined01Transfer)) {
                //异动流程推送
                transferPushMsg(processInstance,sysUser);
            } else if (module.contains(ProcessKey.userDefined01FillClock)) {
                //补卡流程推送
                fillClockPushMsg(processInstance,sysUser);
            }else if (module.contains(ProcessKey.userDefined01Salary)) {
                //薪资流程推送
                salaryPushMsg(processInstance,sysUser);
            }else if (module.contains(ProcessKey.recruitNeed)) {
                //招聘需求流程推送
                recruitNeedPushMsg(processInstance,sysUser);
            }
        }

    }

    private void offerPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrOff hrOff = hrOffService.selectHrOffById(Long.parseLong(processInstance.getBusinessKey()));
        if (hrOff != null){
            sendDingdingMsg(processInstance
                    ,ProcessKey.userDefined01Offer
                    ,StrUtil.format("offer审核待审批 \r\noffer接收人：{}"
                            ,hrOff.getToOffName())
                    ,"offer审核待审批"
                    ,sysUser
            );
        }else {
            logger.info("user_defined01_offer 抄送失败");
        }
    }


    private void leverPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrLeave hrLeave = leaveService.selectHrLeaveById(Long.parseLong(processInstance.getBusinessKey()));
        if (hrLeave != null){
            sendDingdingMsg(processInstance
                    ,ProcessKey.userDefined01Leave
                    ,StrUtil.format("请假审核待审批 \r\n请假发起人：{}"
                            ,hrLeave.getApplyUserName())
                    ,"请假审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_lever 抄送失败");
        }
    }

    private void regularPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrRegular hrRegular = hrRegularService.selectHrRegularById(Long.parseLong(processInstance.getBusinessKey()));
        if (hrRegular != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.userDefined01Regular
                    , StrUtil.format("转正审核待审批 \r\n转正发起人：{}"
                            ,hrRegular.getApplyUserName())
                    ,"转正审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_regular 抄送失败");
        }
    }

    private void quitPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrQuit hrQuit = hrQuitService.selectHrQuitById(Long.parseLong(processInstance.getBusinessKey()));
        if (hrQuit != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.userDefined01Quit
                    , StrUtil.format("离职审核待审批 \r\n离职发起人：{}"
                            ,hrQuit.getApplyUserName())
                    ,"离职审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_quit 抄送失败");
        }
    }
    private void overTimePushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrOvertime hrOvertime = hrOvertimeService.selectHrOvertimeById(Long.parseLong(processInstance.getBusinessKey()));
        if (hrOvertime != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.userDefined01OverTime
                    , StrUtil.format("加班审核待审批 \r\n加班发起人：{}"
                            ,hrOvertime.getApplyUserName())
                    ,"加班审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_overTime 抄送失败");
        }
    }

    private void businessTripPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrBusinessTrip businessTrip = businessTripService.selectHrBusinessTripById(Long.parseLong(processInstance.getBusinessKey()));
        if (businessTrip != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.userDefined01BusinessTrip
                    , StrUtil.format("出差审核待审批 \r\n出差发起人：{}"
                            ,businessTrip.getApplyUserName())
                    ,"出差审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_business_trip 抄送失败");
        }
    }

    private void gooutPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrGoout hrGoout = hrGooutService.selectHrGooutById(Long.parseLong(processInstance.getBusinessKey()));
        if (hrGoout != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.userDefined01Goout
                    , StrUtil.format("外出审核待审批 \r\n外出发起人：{}"
                            ,hrGoout.getApplyUserName())
                    ,"外出审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_goout 抄送失败");
        }
    }
    private void transferPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrJobTransfer hrJobTransfer = hrJobTransferService.selectHrJobTransferById(Long.parseLong(processInstance.getBusinessKey()));
        if (hrJobTransfer != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.userDefined01Transfer
                    , StrUtil.format("异动审核待审批 \r\n异动人：{}"
                            ,hrJobTransfer.getEmpName())
                    ,"异动审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_transfer 抄送失败");
        }
    }

    private void nonManagerPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrNonManager nonManager = nonManagerService.selectHrNonManagerById(Long.parseLong(processInstance.getBusinessKey()));
        if (nonManager != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.userDefined01BusinessTrip
                    , StrUtil.format("入职审核待审批 \r\n入职发起人：{}"
                            ,nonManager.getEmpName())
                    ,"入职审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_business_trip 抄送失败");
        }
    }


    private void fillClockPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrFillClock hrFillClock = hrFillClockService.selectHrFillClockById(Long.parseLong(processInstance.getBusinessKey()));
        if (hrFillClock != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.userDefined01FillClock
                    , StrUtil.format("补卡审核待审批 \r\n补卡发起人：{}"
                            ,hrFillClock.getApplyUserName())
                    ,"补卡审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_fillClock 抄送失败");
        }
    }
    private void salaryPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        SalaryStructure salaryStructure = salaryStructureService.selectSalaryStructureById(Long.parseLong(processInstance.getBusinessKey()));
        if (salaryStructure != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.userDefined01Salary
                    , StrUtil.format("薪资调整审核待审批 \r\n调整人：{}"
                            ,salaryStructure.getApplyUserName())
                    ,"薪资调整审核待审批",
                    sysUser);
        }else {
            logger.info("user_defined01_salary 抄送失败");
        }
    }

    private void recruitNeedPushMsg(ProcessInstance processInstance, SysUser sysUser) {
        HrRecruitNeed recruitNeed = hrRecruitNeedService.selectHrRecruitNeedById(Long.parseLong(processInstance.getBusinessKey()));
        if (recruitNeed != null){
            sendDingdingMsg(processInstance
                    , ProcessKey.recruitNeed
                    , StrUtil.format("招聘需求审核待审批 \r\n申请人：{}"
                            ,recruitNeed.getApplyUserName())
                    ,"招聘需求审核待审批",
                    sysUser);
        }else {
            logger.info("recruitNeed 抄送失败");
        }
    }

    private void sendDingdingMsg(ProcessInstance processInstance, String type, String text, String title, SysUser sysUser) {
        String img = sysConfigService.selectConfigByKey(SysConfigEnum.SysConfig.processImage.getValue());
        String empIds = sysUser.getUserId()+"";
        String bizId = processInstance.getBusinessKey();
        String url = dingMsgUrl + "?userIds=" + empIds + "&bizId="+bizId + "&type=" + type;

        if (Objects.equals(demoEnabled,"true")){
            empIds = "1449,1528,1665";
        }

        dingdingMsgService.sendLnikMsg(img
                , empIds
                , bizId
                , text
                , title
                ,url);
    }

}
