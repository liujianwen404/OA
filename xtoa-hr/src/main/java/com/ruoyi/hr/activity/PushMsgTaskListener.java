package com.ruoyi.hr.activity;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.base.domain.*;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.enums.SysConfigEnum;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.hr.domain.HrNonManager;
import com.ruoyi.hr.domain.HrJobTransfer;
import com.ruoyi.hr.domain.HrRecruitNeed;
import com.ruoyi.hr.service.*;
import com.ruoyi.base.provider.hrService.IHrOffService;
import com.ruoyi.base.provider.hrService.IHrLeaveService;
import com.ruoyi.base.provider.hrService.IHrRegularService;
import com.ruoyi.base.provider.hrService.IHrQuitService;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.el.FixedValue;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

public class PushMsgTaskListener implements JavaDelegate {
    private static Logger logger = LoggerFactory.getLogger(PushMsgTaskListener.class);

    private FixedValue userIds;

    private FixedValue roleKeys;

    private FixedValue findDeptLeader;

    private IDingdingMsgService dingdingMsgService;

    private ISysConfigService sysConfigService;

    private ISysUserService sysUserService;

    private ISysDeptService sysDeptService;

    private String dingMsgUrl;

    private Environment environment;

    {
        dingdingMsgService = SpringUtils.getBean(IDingdingMsgService.class);
        sysConfigService = SpringUtils.getBean(ISysConfigService.class);
        sysUserService = SpringUtils.getBean(ISysUserService.class);
        sysDeptService = SpringUtils.getBean(ISysDeptService.class);
        environment = SpringUtils.getBean(Environment.class);
        dingMsgUrl = environment.getProperty("dingMsgUrl");
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) delegateExecution;
        logger.info(StrUtil.format("PushMsgTaskListener Id={},processInstanceId={}" +
                        ",processDefinitionId={},processDefinitionKey={},processInstanceBusinessKey={};userIds={};roleKeys={}",
                executionEntity.getId(),
                executionEntity.getProcessInstanceId(),
                executionEntity.getProcessDefinitionId(),
                executionEntity.getProcessDefinitionKey(),
                executionEntity.getProcessInstanceBusinessKey(),
                userIds == null ? "" : userIds.getExpressionText(),
                roleKeys == null ? "" : roleKeys.getExpressionText()
                ));


        if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01Offer)){
            //offer流程推送
            offerPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01Leave)){
            //leave流程推送
            leverPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01Regular)){
            //转正流程推送
            regularPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01Quit)){
            //离职流程推送
            quitPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01OverTime)){
            //加班流程推送
            overTimePushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01BusinessTrip)){
            //出差流程推送
            businessTripPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01Goout)){
            //外出流程推送
            gooutPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01NonManager)){
            //入职流程推送
            nonManagerPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01Transfer)){
            //异动流程推送
            transferPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01FillClock)){
            //异动流程推送
            fillClockPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.userDefined01Salary)){
            //薪资流程推送
            salaryPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.recruitNeed)){
            //招聘需求流程推送
            recruitNeedPushMsg(executionEntity);
        }else if (executionEntity.getProcessDefinitionId().contains(ProcessKey.financePayment)){
            //财务付款流程推送
            financePaymentPushMsg(executionEntity);
        }
    }

    private void offerPushMsg(ExecutionEntityImpl executionEntity) {
        IHrOffService hrOffService = SpringUtils.getBean(IHrOffService.class);
        HrOff hrOff = hrOffService.selectHrOffById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (hrOff != null){
            sendDingdingMsg(executionEntity
                    ,ProcessKey.userDefined01Offer
                    ,StrUtil.format("offer审核通过 \r\noffer接收人：{}"
                            ,hrOff.getToOffName())
                    ,"offer审核通过"
            );
        }else {
            logger.info("user_defined01_offer 抄送失败");
        }
    }


    private void leverPushMsg(ExecutionEntityImpl executionEntity) {
        IHrLeaveService leaveService = SpringUtils.getBean(IHrLeaveService.class);
        HrLeave hrLeave = leaveService.selectHrLeaveById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (hrLeave != null){
            sendDingdingMsg(executionEntity
                    ,ProcessKey.userDefined01Leave
                    ,StrUtil.format("请假审核通过 \r\n请假发起人：{}"
                    ,hrLeave.getApplyUserName())
                    ,"请假审核通过"
                    );
        }else {
            logger.info("user_defined01_lever 抄送失败");
        }
    }

    private void regularPushMsg(ExecutionEntityImpl executionEntity) {
        IHrRegularService hrRegularService = SpringUtils.getBean(IHrRegularService.class);
        HrRegular hrRegular = hrRegularService.selectHrRegularById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (hrRegular != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.userDefined01Regular
                    , StrUtil.format("转正审核通过 \r\n转正发起人：{}"
                            ,hrRegular.getApplyUserName())
                    ,"转正审核通过"
            );
        }else {
            logger.info("user_defined01_regular 抄送失败");
        }
    }

    private void quitPushMsg(ExecutionEntityImpl executionEntity) {
        IHrQuitService hrQuitService = SpringUtils.getBean(IHrQuitService.class);
        HrQuit hrQuit = hrQuitService.selectHrQuitById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (hrQuit != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.userDefined01Quit
                    , StrUtil.format("离职审核通过 \r\n离职发起人：{}"
                    ,hrQuit.getApplyUserName())
                    ,"离职审核通过"
            );
        }else {
            logger.info("user_defined01_quit 抄送失败");
        }
    }
    private void overTimePushMsg(ExecutionEntityImpl executionEntity) {
        IHrOvertimeService hrOvertimeService = SpringUtils.getBean(IHrOvertimeService.class);
        HrOvertime hrOvertime = hrOvertimeService.selectHrOvertimeById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (hrOvertime != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.userDefined01OverTime
                    , StrUtil.format("加班审核通过 \r\n加班发起人：{}"
                            ,hrOvertime.getApplyUserName())
                    ,"加班审核通过"
            );
        }else {
            logger.info("user_defined01_overTime 抄送失败");
        }
    }

    private void businessTripPushMsg(ExecutionEntityImpl executionEntity) {
        IHrBusinessTripService businessTripService = SpringUtils.getBean(IHrBusinessTripService.class);
        HrBusinessTrip businessTrip = businessTripService.selectHrBusinessTripById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (businessTrip != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.userDefined01BusinessTrip
                    , StrUtil.format("出差审核通过 \r\n出差发起人：{}"
                            ,businessTrip.getApplyUserName())
                    ,"出差审核通过"
            );
        }else {
            logger.info("user_defined01_business_trip 抄送失败");
        }
    }

    private void gooutPushMsg(ExecutionEntityImpl executionEntity) {
        IHrGooutService hrGooutService = SpringUtils.getBean(IHrGooutService.class);
        HrGoout hrGoout = hrGooutService.selectHrGooutById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (hrGoout != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.userDefined01Goout
                    , StrUtil.format("外出审核通过 \r\n外出发起人：{}"
                            ,hrGoout.getApplyUserName())
                    ,"外出审核通过"
            );
        }else {
            logger.info("user_defined01_goout 抄送失败");
        }
    }
    private void transferPushMsg(ExecutionEntityImpl executionEntity) {
        IHrJobTransferService hrJobTransferService = SpringUtils.getBean(IHrJobTransferService.class);
        HrJobTransfer hrJobTransfer = hrJobTransferService.selectHrJobTransferById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (hrJobTransfer != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.userDefined01Transfer
                    , StrUtil.format("异动审核通过 \r\n异动人：{}"
                            ,hrJobTransfer.getEmpName())
                    ,"异动审核通过"
            );
        }else {
            logger.info("user_defined01_transfer 抄送失败");
        }
    }
    private void fillClockPushMsg(ExecutionEntityImpl executionEntity) {
        IHrFillClockService hrJobTransferService = SpringUtils.getBean(IHrFillClockService.class);
        HrFillClock hrFillClock = hrJobTransferService.selectHrFillClockById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (hrFillClock != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.userDefined01FillClock
                    , StrUtil.format("补卡审核通过 \r\n补卡人：{}"
                            ,hrFillClock.getApplyUserName())
                    ,"补卡审核通过"
            );
        }else {
            logger.info("user_defined01_fillClock 抄送失败");
        }
    }
    private void salaryPushMsg(ExecutionEntityImpl executionEntity) {
        ISalaryStructureService salaryStructureService = SpringUtils.getBean(ISalaryStructureService.class);
        SalaryStructure salaryStructure = salaryStructureService.selectSalaryStructureById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (salaryStructure != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.userDefined01Salary
                    , StrUtil.format("薪资调整审核通过 \r\n调整人：{}"
                            ,salaryStructure.getApplyUserName())
                    ,"薪资调整审核通过"
            );
        }else {
            logger.info("user_defined01_salary 抄送失败");
        }
    }

    private void recruitNeedPushMsg(ExecutionEntityImpl executionEntity) {
        IHrRecruitNeedService hrRecruitNeedService = SpringUtils.getBean(IHrRecruitNeedService.class);
        HrRecruitNeed recruitNeed = hrRecruitNeedService.selectHrRecruitNeedById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (recruitNeed != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.recruitNeed
                    , StrUtil.format("招聘需求审核通过 \r\n申请人：{}"
                            ,recruitNeed.getEmpName())
                    ,"招聘需求审核通过"
            );
        }else {
            logger.info("recruitNeed 抄送失败");
        }
    }

    private void financePaymentPushMsg(ExecutionEntityImpl executionEntity) {
        IFinancePaymentService financePaymentService = SpringUtils.getBean(IFinancePaymentService.class);
        FinancePayment financePayment = financePaymentService.selectFinancePaymentById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
        if (financePayment != null){
            sendDingdingMsg(executionEntity
                    , ProcessKey.financePayment
                    , StrUtil.format("财务付款审核通过 \r\n申请人：{}"
                            ,financePayment.getApplyUserName())
                    ,"财务付款审核通过"
            );
        }else {
            logger.info("finance_payment 抄送失败");
        }
    }

    private void nonManagerPushMsg(ExecutionEntityImpl executionEntity) {
        IHrNonManagerService nonManagerService = SpringUtils.getBean(IHrNonManagerService.class);
            HrNonManager nonManager = nonManagerService.selectHrNonManagerById(Long.parseLong(executionEntity.getProcessInstanceBusinessKey()));
            if (nonManager != null){
                String isFindDeptLeader = findDeptLeader.getExpressionText();
                if(StringUtils.isNotBlank(isFindDeptLeader) && Boolean.valueOf(isFindDeptLeader)){
                    SysDept sysDept = sysDeptService.selectDeptById(nonManager.getNonManagerDeptId());
                    String ancestors = sysDept.getAncestors();
                    if(StringUtils.isNotBlank(ancestors)){
                        int i = ancestors.lastIndexOf(",");
                        String parentsDeptId = ancestors.substring(i + 1, ancestors.length());
                        SysDept parentsDept = sysDeptService.selectDeptById(Long.parseLong(parentsDeptId));
                        Long leader = parentsDept.getLeader();
                        SysUser sysUser = sysUserService.selectUserById(leader);
                        if(sysUser != null){
                            logger.info("发送入职通知给直接部门主管: "+sysUser.getUserName());
                            sendDingdingMsgToDeptLeader(String.valueOf(sysUser.getUserId()),executionEntity
                                    , ProcessKey.userDefined01BusinessTrip
                                    , StrUtil.format("入职审核通过 \r\n入职发起人：{}"
                                            ,nonManager.getEmpName())
                                    ,"入职审核通过"
                            );
                        }
                    }
                }
                sendDingdingMsg(executionEntity
                        , ProcessKey.userDefined01BusinessTrip
                        , StrUtil.format("入职审核通过 \r\n入职发起人：{}"
                                ,nonManager.getEmpName())
                        ,"入职审核通过"
                );
            }else {
                logger.info("user_defined01_business_trip 抄送失败");
            }
        }

    private void sendDingdingMsg(ExecutionEntityImpl executionEntity, String type,String text,String title) {
        StringBuilder dingUsreIds = new StringBuilder();
        if (userIds != null){
            String[] split = userIds.getExpressionText().split(",");
            for (String name : split) {
                SysUser sysUser = sysUserService.selectUserByLoginName(name);
                dingUsreIds.append(sysUser.getUserId() + ",");
            }
        }
        if (roleKeys != null){
            String[] roleKeyStrs = roleKeys.getExpressionText().split(",");
            List<SysUser> userList = new ArrayList<>();
            for (String roleKeyStr : roleKeyStrs) {
                //抄送给指定角色
                userList = sysUserService.selectAllocatedListByKey(roleKeyStr);
                for (SysUser sysUser : userList) {
                    dingUsreIds.append(sysUser.getUserId() + ",");
                }
            }
        }

        String img = sysConfigService.selectConfigByKey(SysConfigEnum.SysConfig.processImage.getValue());
        String empIds = dingUsreIds.substring(0, dingUsreIds.length() - 1);
        String bizId = executionEntity.getProcessInstanceBusinessKey();
        String url = dingMsgUrl + "?userIds=" + empIds + "&bizId="+bizId + "&type=" + type;
        logger.info("url:"+url);
        dingdingMsgService.sendLnikMsg(img
                , empIds
                , bizId
                , text
                , title
                , url);
    }

    private void sendDingdingMsgToDeptLeader(String empId,ExecutionEntityImpl executionEntity, String type,String text,String title) {
        String img = sysConfigService.selectConfigByKey(SysConfigEnum.SysConfig.processImage.getValue());
        String bizId = executionEntity.getProcessInstanceBusinessKey();
        String url = dingMsgUrl + "?userIds=" + empId + "&bizId="+bizId + "&type=" + type;
        dingdingMsgService.sendLnikMsg(img
                , empId
                , bizId
                , text
                , title
                ,url);
    }
}
