package com.ruoyi.hr.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.base.domain.SysJob;
import com.ruoyi.base.provider.quartzService.ISysJobService;
import com.ruoyi.common.exception.job.TaskException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrWorkPlanMapper;
import com.ruoyi.base.domain.HrWorkPlan;
import com.ruoyi.hr.service.IHrWorkPlanService;
import com.ruoyi.common.core.text.Convert;

import javax.annotation.Resource;

/**
 * 工作计划Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-08-20
 */
@Service
public class HrWorkPlanServiceImpl implements IHrWorkPlanService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrWorkPlanServiceImpl.class);

    @Resource
    private HrWorkPlanMapper hrWorkPlanMapper;

    @Autowired
    private ISysJobService jobService;


    /**
     * 查询工作计划
     * 
     * @param id 工作计划ID
     * @return 工作计划
     */
    @Override
    public HrWorkPlan selectHrWorkPlanById(Long id)
    {
        return hrWorkPlanMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询工作计划列表
     * 
     * @param hrWorkPlan 工作计划
     * @return 工作计划
     */
    @Override
    public List<HrWorkPlan> selectHrWorkPlanList(HrWorkPlan hrWorkPlan)
    {
        hrWorkPlan.setDelFlag("0");
        return hrWorkPlanMapper.selectHrWorkPlanList(hrWorkPlan);
    }

    /**
     * 新增工作计划
     * 
     * @param hrWorkPlan 工作计划
     * @return 结果
     */
    @Override
    @Transactional
    public int insertHrWorkPlan(HrWorkPlan hrWorkPlan) throws SchedulerException, TaskException
    {
        hrWorkPlan.setCreateId(ShiroUtils.getUserId());
        hrWorkPlan.setCreateBy(ShiroUtils.getLoginName());
        hrWorkPlan.setCreateTime(DateUtils.getNowDate());
        hrWorkPlan.setEmpId(ShiroUtils.getUserId());
        hrWorkPlan.setEmpName(ShiroUtils.getUserName());
        if("1".equals(hrWorkPlan.getIsRemind())){
            DateTime notificationTime = DateUtil.offsetHour(hrWorkPlan.getStartTime(), -1);//提前一个小时通知，如果不满一个小时，直接通知
            String dateToStr = "";
            if(notificationTime.isAfter(new Date())){
                //当通知时间大于当前时间
                dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", notificationTime);
            }else{
                dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", hrWorkPlan.getStartTime());
            }
            int month = Integer.parseInt(dateToStr.substring(5,7));
            int day = Integer.parseInt(dateToStr.substring(8,10));
            int hours = Integer.parseInt(dateToStr.substring(11,13));
            int minute = Integer.parseInt(dateToStr.substring(14,dateToStr.length()));
            String cron = "0 "+ minute +" "+ hours +" "+ day +" "+ month +" ?";
            SysJob sysJob = new SysJob();
            sysJob.setJobName(ShiroUtils.getUserName()+DateUtils.parseDateToStr("yyyy-MM-dd HH:mm",hrWorkPlan.getStartTime())+"工作计划通知");
            sysJob.setJobGroup("DEFAULT");
            sysJob.setInvokeTarget("workPlanTask.excute("+hrWorkPlan.getEmpId()+",'"+hrWorkPlan.getEmpName()+",'"+dateToStr+"')");
            sysJob.setCronExpression(cron);
            sysJob.setMisfirePolicy("1");
            sysJob.setConcurrent("1");
            sysJob.setStatus("0");
            Long jobId = jobService.insertJobBySystem(sysJob);
            hrWorkPlan.setJobId(jobId);
        }
        return hrWorkPlanMapper.insertSelective(hrWorkPlan);
    }

    /**
     * 修改工作计划
     * 
     * @param hrWorkPlan 工作计划
     * @return 结果
     */
    @Override
    @Transactional
    public int updateHrWorkPlan(HrWorkPlan hrWorkPlan) throws SchedulerException, TaskException
    {
        hrWorkPlan.setUpdateId(ShiroUtils.getUserId());
        hrWorkPlan.setUpdateBy(ShiroUtils.getLoginName());
        hrWorkPlan.setUpdateTime(DateUtils.getNowDate());
        if("1".equals(hrWorkPlan.getIsRemind())){
            DateTime notificationTime = DateUtil.offsetHour(hrWorkPlan.getStartTime(), -1);//提前一个小时通知，如果不满一个小时，直接通知
            String dateToStr = "";
            if(notificationTime.isAfter(new Date())){
                //当通知时间大于当前时间
                dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", notificationTime);
            }else{
                dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm", hrWorkPlan.getStartTime());
            }
            int month = Integer.parseInt(dateToStr.substring(5,7));
            int day = Integer.parseInt(dateToStr.substring(8,10));
            int hours = Integer.parseInt(dateToStr.substring(11,13));
            int minute = Integer.parseInt(dateToStr.substring(14,dateToStr.length()));
            String cron = "0 "+ minute +" "+ hours +" "+ day +" "+ month +" ?";
            SysJob sysJob = new SysJob();
            sysJob.setJobName(ShiroUtils.getUserName()+DateUtils.parseDateToStr("yyyy-MM-dd HH:mm",hrWorkPlan.getStartTime())+"工作计划通知");
            sysJob.setJobGroup("DEFAULT");
            sysJob.setInvokeTarget("workPlanTask.excute("+hrWorkPlan.getEmpId()+",'"+hrWorkPlan.getEmpName()+",'"+dateToStr+"')");
            sysJob.setCronExpression(cron);
            sysJob.setMisfirePolicy("1");
            sysJob.setConcurrent("1");
            sysJob.setStatus("0");
            Long jobId = jobService.insertJobBySystem(sysJob);
            hrWorkPlan.setJobId(jobId);
        }
        return hrWorkPlanMapper.updateByPrimaryKeySelective(hrWorkPlan);
    }

    /**
     * 删除工作计划对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrWorkPlanByIds(String ids)
    {
        String[] idArray = Convert.toStrArray(ids);
        Arrays.asList(idArray).forEach((id)->{
            HrWorkPlan hrWorkPlan = hrWorkPlanMapper.selectByPrimaryKey(id);
            if("1".equals(hrWorkPlan.getIsRemind())){
                try {
                    jobService.deleteJobByIds(String.valueOf(hrWorkPlan.getJobId()));
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        });

        return hrWorkPlanMapper.deleteHrWorkPlanByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除工作计划信息
     * 
     * @param id 工作计划ID
     * @return 结果
     */
    @Override
    public int deleteHrWorkPlanById(Long id)
    {
        return hrWorkPlanMapper.deleteHrWorkPlanById(id);
    }



    @Override
    public HrWorkPlan selectSingleOneByExample(Example example){
        return hrWorkPlanMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<HrWorkPlan> selectByExample(Example example){
        return hrWorkPlanMapper.selectByExample(example);
    }

}
