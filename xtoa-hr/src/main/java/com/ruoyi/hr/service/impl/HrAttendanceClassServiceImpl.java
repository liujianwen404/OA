package com.ruoyi.hr.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.base.domain.DTO.DateOperation;
import com.ruoyi.base.domain.HrAttendanceGroup;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.data.domain.TDate;
import com.ruoyi.common.utils.data.service.ITDateService;
import com.ruoyi.hr.service.IHrAttendanceGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrAttendanceClassMapper;
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.hr.service.IHrAttendanceClassService;
import com.ruoyi.common.core.text.Convert;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;

/**
 * 考勤班次Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-07-27
 */
@Service
public class HrAttendanceClassServiceImpl implements IHrAttendanceClassService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrAttendanceClassServiceImpl.class);

    @Resource
    private HrAttendanceClassMapper hrAttendanceClassMapper;

    @Autowired
    private IHrAttendanceGroupService hrAttendanceGroupService;

    @Autowired
    private ITDateService dateService;

    /**
     * 查询考勤班次
     * 
     * @param id 考勤班次ID
     * @return 考勤班次
     */
    @Override
    public HrAttendanceClass selectHrAttendanceClassById(Long id)
    {
        return hrAttendanceClassMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询考勤班次列表
     * 
     * @param hrAttendanceClass 考勤班次
     * @return 考勤班次
     */
    @Override
    public List<HrAttendanceClass> selectHrAttendanceClassList(HrAttendanceClass hrAttendanceClass)
    {
        hrAttendanceClass.setDelFlag("0");
        return hrAttendanceClassMapper.selectHrAttendanceClassList(hrAttendanceClass);
    }

    /**
     * 新增考勤班次
     * 
     * @param hrAttendanceClass 考勤班次
     * @return 结果
     */
    @Override
    public int insertHrAttendanceClass(HrAttendanceClass hrAttendanceClass)
    {
        hrAttendanceClass.setCreateId(ShiroUtils.getUserId());
        hrAttendanceClass.setCreateBy(ShiroUtils.getLoginName());
        hrAttendanceClass.setCreateTime(DateUtils.getNowDate());
        return hrAttendanceClassMapper.insertSelective(hrAttendanceClass);
    }

    /**
     * 修改考勤班次
     * 
     * @param hrAttendanceClass 考勤班次
     * @return 结果
     */
    @Override
    public int updateHrAttendanceClass(HrAttendanceClass hrAttendanceClass)
    {
        hrAttendanceClass.setUpdateId(ShiroUtils.getUserId());
        hrAttendanceClass.setUpdateBy(ShiroUtils.getLoginName());
        hrAttendanceClass.setUpdateTime(DateUtils.getNowDate());
        return hrAttendanceClassMapper.updateByPrimaryKeySelective(hrAttendanceClass);
    }

    /**
     * 删除考勤班次对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceClassByIds(String ids)
    {
        String[] idArray = Convert.toStrArray(ids);
        Arrays.asList(idArray).forEach(classId -> {
            List<HrAttendanceGroup> list = hrAttendanceGroupService.selectGroupByClassId(classId);
            if(!list.isEmpty() && list.size() > 0){
                throw new BusinessException("该班次正在使用中！");
            }
        });
        return hrAttendanceClassMapper.deleteHrAttendanceClassByIds(idArray);
    }

    /**
     * 删除考勤班次信息
     * 
     * @param id 考勤班次ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceClassById(Long id)
    {
        return hrAttendanceClassMapper.deleteHrAttendanceClassById(id);
    }

    @Override
    public List<HrAttendanceClass> selectHrAttendanceClassAll() {
        return hrAttendanceClassMapper.selectHrAttendanceClassAll();
    }

    /**
     * 获取用户这个时间的班次信息
     * @param empId
     * @param startTime
     * @return
     */
    @Override
    public HrAttendanceClass getAttendanceClass(Long empId, Date startTime) {
        HrAttendanceGroup hrAttendanceGroup = hrAttendanceGroupService.selectGroupByEmpId(empId);
        if (hrAttendanceGroup == null){
            //没有考勤组信息
            logger.info("没有考勤组信息");
            return null;
        }
        String startDayStr = DateUtil.format(startTime,"yyyy-MM-dd");
        String type = hrAttendanceGroup.getType();
        //如果是排班制
        if(StringUtils.isNotBlank(type) && "1".equals(type)){
            Map<String,Object> scheduGroupAndClassMap = hrAttendanceGroupService.selectScheduGroupAndClass(hrAttendanceGroup.getId(),empId,startDayStr);
            //当天班次信息为空，为休息日
            if(scheduGroupAndClassMap == null || scheduGroupAndClassMap.isEmpty()){
                logger.info("当天排班为休息日");
                return null;
            }
            Long classId = (Long) scheduGroupAndClassMap.get("class_id");
            return hrAttendanceClassMapper.selectByPrimaryKey(classId);
        }

        //如果是固定班制
        //这个开始日期需要打卡吗
        for (String dateStr : hrAttendanceGroup.getNeedNotDate().split(",")) {
            if (dateStr.contains(startDayStr)){
                //不用打卡的日期
                logger.info("不用打卡的日期");
                return null;
            }
        }


        //是否是国假
        TDate tDate = new TDate();
        tDate.setDay(DateUtil.parse(startDayStr,"yyyy-MM-dd"));
        tDate.setFlag("1");
        List<TDate> tDates = dateService.selectTDateList(tDate);
        if (tDates != null && !tDates.isEmpty()){
            logger.info(tDate + "是国假");
            return null;
        }

        int dayOfWeek = DateUtil.dayOfWeek(startTime);
        String classId = null;
        switch (dayOfWeek){
            case 2:
                //周一
                classId = hrAttendanceGroup.getMonClassId();
                break;
            case 3:
                //周二
                classId = hrAttendanceGroup.getTueClassId();
                break;
            case 4:
                classId = hrAttendanceGroup.getWedClassId();
                break;
            case 5:
                classId = hrAttendanceGroup.getThuClassId();
                break;
            case 6:
                classId = hrAttendanceGroup.getFriClassId();
                break;
            case 7:
                classId = hrAttendanceGroup.getSatClassId();
                break;
            case 1:
                classId = hrAttendanceGroup.getSunClassId();
                break;
        }
        HrAttendanceClass hrAttendanceClass = null;
        if (StringUtil.isNotEmpty(classId)){
            logger.info("classId:"+classId);
            hrAttendanceClass = hrAttendanceClassMapper.selectByPrimaryKey(Long.parseLong(classId));
        }
        if (hrAttendanceClass != null && hrAttendanceClass.getDelFlag().equals("1")){
            logger.info("hrAttendanceClass.getDelFlag().equals(1)");
            hrAttendanceClass = null;
        }

        if (hrAttendanceClass == null){
            for (String mustDateStr : hrAttendanceGroup.getMustDate().split(",")){
                if (mustDateStr.contains(startDayStr)){
                    //必须上班
                    hrAttendanceClass = hrAttendanceClassMapper.selectByPrimaryKey(Long.parseLong(mustDateStr.split(":")[1]));
                    break;
                }
            }
        }

        return hrAttendanceClass != null && hrAttendanceClass.getDelFlag().equals("0") ? hrAttendanceClass : null;
    }

    /**
     * 通过日期获取班次的各个时间
     * @param offsetDay
     * @param attendanceClass
     * @param empId
     * @param shiftCriticalPoint
     */
    @Override
    public DateOperation getDateOperation(Date offsetDay, HrAttendanceClass attendanceClass, Long empId, Integer shiftCriticalPoint) {

        if (attendanceClass == null){
            //此处仅仅是为了定时任务所添加的判断
            logger.info("此处仅仅是为了定时任务所添加的判断");
            return null;
        }

        String format = DateUtil.format(offsetDay, "yyyy-MM-dd ");
        //打卡时间
        String workTime = attendanceClass.getWorkTime();
        String closingTime = attendanceClass.getClosingTime();
        int workInt = Integer.parseInt(workTime.split(":")[0]);
        int closingInt = Integer.parseInt(closingTime.split(":")[0]);
        Date workDate = DateUtil.parse(format + workTime,"yyyy-MM-dd HH:mm");
        Date closingDate = DateUtil.parse(format + closingTime,"yyyy-MM-dd HH:mm");
        if (workInt >= closingInt){
            //是跨天的班次
            closingDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(offsetDay,1), "yyyy-MM-dd ")
                    + closingTime,"yyyy-MM-dd HH:mm");
        }

        //休息时间
        String restStartTime = attendanceClass.getRestStartTime();
        String restEndTime = attendanceClass.getRestEndTime();
        Date restStartDate = null;
        Date restEndDate = null;
        if (StringUtil.isNotEmpty(restStartTime) && StringUtil.isNotEmpty(restEndTime)){
            int restStartInt = Integer.parseInt(restStartTime.split(":")[0]);
            int restEndInt = Integer.parseInt(restEndTime.split(":")[0]);
            restStartDate = DateUtil.parse(format + restStartTime,"yyyy-MM-dd HH:mm");
            restEndDate = DateUtil.parse(format + restEndTime,"yyyy-MM-dd HH:mm");
            if (restStartInt >= restEndInt){
                //是跨天的班次
                restEndDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(offsetDay,1), "yyyy-MM-dd ")
                        + restEndTime,"yyyy-MM-dd HH:mm");
            }
            if (workInt >= restStartInt){
                //是跨天的班次
                restStartDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(offsetDay,1), "yyyy-MM-dd ")
                        + restStartTime,"yyyy-MM-dd HH:mm");
            }
        }


        DateOperation dateOperation = new DateOperation(workDate, closingDate,
                restStartDate, restEndDate,
                workDate, closingDate);

        //班次临界点时间
        dateOperation.setShiftCriticalPointStartDate(DateUtil.offsetHour(dateOperation.getOriginalStart(),0 - shiftCriticalPoint));
        dateOperation.setShiftCriticalPointEndDate(DateUtil.offsetHour(dateOperation.getOriginalEnd(),shiftCriticalPoint));


        //弹性上班
        if (attendanceClass.getElasticityFlag().equals("1")){
            //弹性计算
            Map<String,Object> map = hrAttendanceGroupService.elasticityOperation(empId, attendanceClass, format,
                    DateUtil.date(workDate), DateUtil.date(closingDate),
                    DateUtil.date(restStartDate), DateUtil.date(restEndDate));
            workDate = (DateTime) map.get("workDate");
            closingDate = (DateTime) map.get("closingDate");
            restStartDate = (DateTime) map.get("restStartDate");
            restEndDate = (DateTime) map.get("restEndDate");

            dateOperation.setOriginalStart(workDate);
            dateOperation.setOriginalEnd(closingDate);
            dateOperation.setOriginalRestStartDate(restStartDate);
            dateOperation.setOriginalRestEndDate(restEndDate);
            dateOperation.setStartTime(workDate);
            dateOperation.setEndTime(closingDate);

        }
        dateOperation.setAttendanceDay(DateUtil.parse(format));
        dateOperation.setIsPublic(false);
        return dateOperation;


    }

}
