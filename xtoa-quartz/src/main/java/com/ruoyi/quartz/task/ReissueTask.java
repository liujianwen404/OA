package com.ruoyi.quartz.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.base.domain.*;
import com.ruoyi.base.domain.DTO.DateOperation;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.common.utils.enums.SysConfigEnum;
import com.ruoyi.framework.web.service.DeptService;
import com.ruoyi.framework.web.service.PostService;
import com.ruoyi.hr.service.*;
import com.ruoyi.hr.service.impl.HrAttendanceInfoServiceImpl;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 补卡定时任务
 */
@Component("reissueTask")
public class ReissueTask {

    private Logger logger = LoggerFactory.getLogger(ReissueTask.class);


    @Autowired
    private IHrAttendanceGroupService hrAttendanceGroupService;

    @Autowired
    private IHrAttendanceClassService hrAttendanceClassService;

    @Autowired
    private IHrFillClockService hrFillClockService;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private IHrAttendanceInfoService hrAttendanceInfoService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IHrOvertimeService hrOvertimeService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private PostService postService;

    @Transactional
    public void reissueTaskService(){
        logger.info("补卡开始：reissueTaskService" );
        long currentTimeMillis = System.currentTimeMillis();

        //10小时之前的时间
        DateTime dateTime10 = DateUtil.offsetHour(new Date(), -10);


        List<HrEmp> hrEmps = hrEmpService.selectOnTheEmpList();
        Integer shiftCriticalPoint = Integer.parseInt(sysConfigService.selectConfigByKey(SysConfigEnum.SysConfig.shift_critical_point.getValue()));
        List<SysDept> deptAll = deptService.getDeptAll();
        List<SysPost> postAll = postService.getPostAll();
        Map<Long, String> deptMap = deptAll.stream().collect(Collectors.toMap(SysDept::getDeptId, SysDept::getShowName));
        Map<Long, String> postMap = postAll.stream().collect(Collectors.toMap(SysPost::getPostId, SysPost::getPostName));
        DateTime dateTime;
        HrAttendanceGroup hrAttendanceGroup;
        HrAttendanceClass attendanceClass;
        DateOperation dateOperation;
        for (HrEmp hrEmp : hrEmps) {
            try {
                logger.info("开始补卡逻辑：" + hrEmp.getEmpId());

                dateTime = DateUtil.offsetDay(new Date(), -1);

                hrAttendanceGroup = hrAttendanceGroupService.selectGroupByEmpId(hrEmp.getEmpId());
                if (hrAttendanceGroup == null){
                    logger.info("未找到考勤组信息：" + hrEmp.getEmpId());
                    continue;
                }
                logger.info("考勤组信息：" + hrAttendanceGroup.toString());
                //从昨天开始找
                logger.info("昨天dateTime：" + dateTime);
                attendanceClass = hrAttendanceClassService.getAttendanceClass(hrEmp.getEmpId(), dateTime);
                dateOperation = hrAttendanceClassService.getDateOperation(dateTime, attendanceClass, hrEmp.getEmpId(), shiftCriticalPoint);
                if (attendanceClass != null || dateOperation != null ){
                    logger.info("昨天:" + attendanceClass.toString() + dateOperation.toString());
                    insetFillClock(dateTime10, dateTime, deptMap, postMap, hrEmp, hrAttendanceGroup, attendanceClass, dateOperation);
                }

                //继续维护今天的
                dateTime = DateUtil.offsetDay(dateTime,1);
                logger.info("今天dateTime：" + dateTime);
                attendanceClass = hrAttendanceClassService.getAttendanceClass(hrEmp.getEmpId(), dateTime);
                dateOperation = hrAttendanceClassService.getDateOperation(dateTime, attendanceClass, hrEmp.getEmpId(), shiftCriticalPoint);
                if (attendanceClass != null || dateOperation != null ){
                    logger.info("今天:" + attendanceClass.toString() + dateOperation.toString());
                    insetFillClock(dateTime10, dateTime, deptMap, postMap, hrEmp, hrAttendanceGroup, attendanceClass, dateOperation);
                }

            }catch (Exception e){
                logger.info("补卡报错:" + hrEmp.getEmpId());
                logger.error("补卡报错:" + hrEmp.getEmpId(), e);
                e.printStackTrace();
            }
        }

        logger.info("补卡耗时:" + (currentTimeMillis - System.currentTimeMillis()));
    }

    private void insetFillClock(DateTime dateTime10, DateTime dateTime, Map<Long, String> deptMap, Map<Long, String> postMap, HrEmp hrEmp, HrAttendanceGroup hrAttendanceGroup, HrAttendanceClass attendanceClass, DateOperation dateOperation) {
        String dateStr = DateUtil.format(dateTime, "yyyy-MM-dd");
        HrAttendanceInfo hrAttendanceInfo = new HrAttendanceInfo();
        hrAttendanceInfo.setEmpId(hrEmp.getEmpId());

        logger.info("check_type_OnDuty:" + dateOperation.getOriginalStart() + " : " + dateTime10);
        if (DateUtil.date(dateOperation.getOriginalStart()).isBeforeOrEquals(dateTime10)){
            HrAttendanceInfo preOnDutyInfo = hrAttendanceInfoService.selectOldHrAttendanceInfo(hrAttendanceInfo,HrAttendanceInfoServiceImpl.check_type_OnDuty,dateStr);
            if (preOnDutyInfo == null){
                SysUser sysUser = sysUserService.selectUserById(hrEmp.getEmpId());
                //上班卡为空
                HrAttendanceInfo newOnDutyInfo = new HrAttendanceInfo();
                newOnDutyInfo.setEmpId(hrEmp.getEmpId());
                newOnDutyInfo.setEmpName(hrEmp.getEmpName());
                newOnDutyInfo.setClassId(attendanceClass.getId());
                newOnDutyInfo.setGroupId(hrAttendanceGroup.getId());
                newOnDutyInfo.setWorkDate(dateStr);
                newOnDutyInfo.setCheckType(HrAttendanceInfoServiceImpl.check_type_OnDuty);
                newOnDutyInfo.setTimeResult(HrAttendanceInfoServiceImpl.time_result_NotSigned);//未打卡
                newOnDutyInfo.setIsLegal("N");//不合法
                newOnDutyInfo.setSourceType(HrAttendanceInfoServiceImpl.source_type_SYSTEM);

                newOnDutyInfo.setDeptId(hrEmp.getDeptId());
                newOnDutyInfo.setPostId(hrEmp.getPostId());
                newOnDutyInfo.setCreateId(hrEmp.getEmpId());
                newOnDutyInfo.setCreateBy(sysUser.getLoginName());

                int i = hrAttendanceInfoService.insertHrAttendanceInfo(newOnDutyInfo);
                if (i > 0){
                    //插入补卡数据
                    saveFillClock(dateTime, hrEmp, hrAttendanceGroup, attendanceClass, sysUser, newOnDutyInfo,deptMap,postMap);
                }
            }else {
                logger.info("preOnDutyInfo:"+preOnDutyInfo.toString());
            }
        }

        logger.info("check_type_OnDuty:" + dateOperation.getOriginalEnd() + " : " + dateTime10);
        if (DateUtil.date(dateOperation.getOriginalEnd()).isBeforeOrEquals(dateTime10)){
            HrAttendanceInfo preOffDutyInfo = hrAttendanceInfoService.selectOldHrAttendanceInfo(hrAttendanceInfo,HrAttendanceInfoServiceImpl.check_type_OffDuty,dateStr);

            if (preOffDutyInfo == null){
                SysUser sysUser = sysUserService.selectUserById(hrEmp.getEmpId());
                //下班卡为空
                HrAttendanceInfo newOnDutyInfo = new HrAttendanceInfo();
                newOnDutyInfo.setEmpId(hrEmp.getEmpId());
                newOnDutyInfo.setEmpName(hrEmp.getEmpName());
                newOnDutyInfo.setClassId(attendanceClass.getId());
                newOnDutyInfo.setGroupId(hrAttendanceGroup.getId());
                newOnDutyInfo.setWorkDate(dateStr);
                newOnDutyInfo.setCheckType(HrAttendanceInfoServiceImpl.check_type_OffDuty);
                newOnDutyInfo.setTimeResult(HrAttendanceInfoServiceImpl.time_result_NotSigned);//未打卡
                newOnDutyInfo.setIsLegal("N");//不合法
                newOnDutyInfo.setSourceType(HrAttendanceInfoServiceImpl.source_type_SYSTEM);

                newOnDutyInfo.setDeptId(hrEmp.getDeptId());
                newOnDutyInfo.setPostId(hrEmp.getPostId());
                newOnDutyInfo.setCreateId(hrEmp.getEmpId());
                newOnDutyInfo.setCreateBy(sysUser.getLoginName());

                int i = hrAttendanceInfoService.insertHrAttendanceInfo(newOnDutyInfo);
                if (i > 0){
                    //插入补卡数据
                    saveFillClock(dateTime, hrEmp, hrAttendanceGroup, attendanceClass, sysUser, newOnDutyInfo, deptMap, postMap);
                }
            }else {
                logger.info("preOffDutyInfo:" + preOffDutyInfo.toString());
            }
        }
    }

    private void saveFillClock(DateTime dateTime, HrEmp hrEmp, HrAttendanceGroup hrAttendanceGroup, HrAttendanceClass attendanceClass, SysUser sysUser, HrAttendanceInfo newOnDutyInfo, Map<Long, String> deptMap, Map<Long, String> postMap) {
        HrFillClock hrFillClock = new HrFillClock();
        hrFillClock.setEmpId(hrEmp.getEmpId());
        hrFillClock.setDeptId(hrEmp.getDeptId());
        hrFillClock.setPostId(hrEmp.getPostId());
        hrFillClock.setAttendanceInfoId(newOnDutyInfo.getId());
        hrFillClock.setGroupId(hrAttendanceGroup.getId());
        hrFillClock.setClassDate(dateTime);
        hrFillClock.setClassId(attendanceClass.getId());
        hrFillClock.setCreateId(hrEmp.getEmpId());
        hrFillClock.setCreateBy(sysUser.getLoginName());
        hrFillClock.setCheckType(newOnDutyInfo.getCheckType());
        hrFillClock.setApplyUser(sysUser.getLoginName());
        hrFillClock.setApplyUserName(sysUser.getUserName());
        hrFillClock.setPostName(postMap.get(hrEmp.getPostId()));
        hrFillClock.setDeptName(deptMap.get(hrEmp.getDeptId()));

        hrFillClockService.insertHrFillClock(hrFillClock);
        logger.info("hrFillClock:"+hrFillClock.toString());
    }
}
