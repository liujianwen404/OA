package com.ruoyi.quartz.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.base.domain.HrAttendanceStatistics;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.SalaryStructure;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.framework.web.service.DeptService;
import com.ruoyi.framework.web.service.PostService;
import com.ruoyi.hr.service.*;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 薪资报表
 */
@Component("salaryStructureTask")
public class SalaryStructureTask {

    private Logger logger = LoggerFactory.getLogger(SalaryStructureTask.class);


    @Autowired
    private IHrAttendanceStatisticsService hrAttendanceStatisticsService;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private PostService postService;

    @Autowired
    private ISalaryStructureService salaryStructureService;

    @Transactional
    public void salaryStructureTaskService(){
        logger.info("薪资报表：salaryStructureTaskService" );
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date();
        DateTime dateTime = null;

        List<HrEmp> hrEmps = hrEmpService.selectEmpList();

        SalaryStructure selectSalary = null;
        SalaryStructure salaryStructure = null;
        SalaryStructure salaryStructureAdjust = null;
        SalaryStructure salaryStructureHistory = null;
        HrEmp hrEmp = null;
        BigDecimal attendanceAate = null;
        HrAttendanceStatistics hrAttendanceStatistics = null;
        Iterator<HrEmp> iterator = hrEmps.iterator();
        while (iterator.hasNext()){
            hrEmp = iterator.next();
            try {
                //当前的考勤报表
                dateTime = DateUtil.date(date);
                hrAttendanceStatistics = hrAttendanceStatisticsService.selectSingleOneByCondition(hrEmp.getEmpId(), dateTime.year(), dateTime.month() + 1);
                if (hrAttendanceStatistics != null){
                    operation(dateTime, hrEmp, hrAttendanceStatistics);
                }

                if (DateUtil.offsetDay(dateTime ,-2).isBefore(DateUtil.beginOfMonth(dateTime))){
                    //当前时间减两天大于本月开始时间，说明是月初的第二天或第一天，由于夜班跨月的缘故，此时需要再次计算一下上个月的考勤薪资
                    dateTime = DateUtil.offsetMonth(DateUtil.endOfMonth(dateTime),-1);
                    hrAttendanceStatistics = hrAttendanceStatisticsService.selectSingleOneByCondition(hrEmp.getEmpId(), dateTime.year(), dateTime.month() + 1);
                    if (hrAttendanceStatistics != null){
                        operation(dateTime, hrEmp, hrAttendanceStatistics);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
                logger.info("薪资核算异常：" + hrEmp.getEmpId() + " - " + hrEmp.getEmpName() );
            }
        }
        logger.info("薪资报表:" + (currentTimeMillis - System.currentTimeMillis()));
    }

    private void operation(DateTime dateTime, HrEmp hrEmp, HrAttendanceStatistics hrAttendanceStatistics) {
        BigDecimal attendanceAate;
        SalaryStructure selectSalary;
        SalaryStructure salaryStructure;
        SalaryStructure salaryStructureAdjust;
        SalaryStructure salaryStructureHistory;
        attendanceAate = new BigDecimal( hrAttendanceStatistics.getShouldAttendance() / hrAttendanceStatistics.getActualAttendance());

        selectSalary = new SalaryStructure();
        selectSalary.setEmpId(hrEmp.getEmpId());
        selectSalary.setDelFlag("0");
        selectSalary.setAuditStatus(2);
        selectSalary.setIsHistory(0);
        selectSalary.setMonthDate(dateTime);
        salaryStructure = salaryStructureService.selectOneBySalaryStructure(selectSalary);
        if (salaryStructure != null){
            //本月有数据

            selectSalary.setIsHistory(1);
            salaryStructureAdjust = salaryStructureService.selectOneBySalaryStructure(selectSalary);
            if (salaryStructureAdjust != null &&
                    !salaryStructure.getAdjustDate().before(DateUtil.offsetDay(DateUtil.beginOfMonth(dateTime),15))){
                //本月有调整薪资，并且在15号之前
                salaryStructure = salaryStructureAdjust;
            }

            operationSalary(salaryStructure, attendanceAate);

        }else {
            //本月没数据，查询历史数据
            selectSalary.setMonthDate(null);
            salaryStructureHistory = salaryStructureService.selectOneBySalaryStructure(selectSalary);
            if (salaryStructureHistory != null){
                //有历史数据
                salaryStructure = new SalaryStructure();
                BeanUtil.copyProperties(salaryStructureHistory,salaryStructure);
                salaryStructure.setCreateTime(dateTime);
                salaryStructure.setMonthDate(dateTime);
                salaryStructure.setInstanceId(null);
                salaryStructure.setApplyTime(null);
                salaryStructure.setSalaryContent(null);

                int i = salaryStructureService.insertSalaryStructure(salaryStructure);
                if (i > 0){
                    operationSalary(salaryStructure, attendanceAate);
                }

            }
        }
    }

    private void operationSalary(SalaryStructure salaryStructure, BigDecimal attendanceAate) {
        salaryStructure.setBasicActual(salaryStructure.getBasic().multiply(attendanceAate));
        salaryStructure.setOvertimePayActual(salaryStructure.getOvertimePay().multiply(attendanceAate));
        salaryStructure.setAllowanceActual(salaryStructure.getAllowance().multiply(attendanceAate));
        //应发合计=（基本工资+加班费+岗位补贴+绩效工资+提成+其他补贴+全勤奖+夜班补贴+平时加班费）
        salaryStructure.setLaballot(salaryStructure.getBasicActual()
                                    .add(salaryStructure.getOvertimePayActual())
                                    .add(salaryStructure.getAllowanceActual())
                                    .add(salaryStructure.getPerformanceBonusActual())
                                    .add(salaryStructure.getAmortization())
                                    .add(salaryStructure.getOtherSubsidiesActual())
                                    .add(salaryStructure.getAttendanceBonus())
                                    .add(salaryStructure.getNightAllowance())
                                    .add(salaryStructure.getOvertimeActual())
                                    );

        salaryStructure.setPretaxIncome(salaryStructure.getLaballot()
                                        .subtract(salaryStructure.getDeductionForUtilities()
                                        .add(salaryStructure.getDeductionForOther()))
                                        );

        salaryStructure.setNetPayroll(salaryStructure.getPretaxIncome()
                                        .subtract(salaryStructure.getSocialSecurity()
                                        .add(salaryStructure.getAccumulationFund())
                                        .add(salaryStructure.getTallage()))
                                        );


        //标记其他数据都为无效的报表数据
        SalaryStructure selectSalary = new SalaryStructure();
        selectSalary.setIsValid(1);
        selectSalary.setEmpId(salaryStructure.getEmpId());
        selectSalary.setMonthDate(salaryStructure.getMonthDate());
        List<SalaryStructure> salaryStructureList = salaryStructureService.selectSalaryStructureListNew(selectSalary);
        for (SalaryStructure structure : salaryStructureList) {
            structure.setIsValid(0);
            salaryStructureService.updateSalaryStructure(structure);
        }

        //本月有效数据
        salaryStructure.setIsValid(1);
        salaryStructureService.updateSalaryStructure(salaryStructure);




    }


}
