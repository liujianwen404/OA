package com.ruoyi.hr.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.base.domain.DTO.DateOperation;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.base.domain.*;
import com.ruoyi.base.domain.DTO.OverTimeDTO;
import com.ruoyi.common.utils.data.domain.TDate;
import com.ruoyi.common.utils.data.service.ITDateService;
import com.ruoyi.common.utils.enums.SysConfigEnum;
import com.ruoyi.hr.manager.IHrDomainConvert;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.service.IHolidayService;
import com.ruoyi.hr.service.IHrAttendanceClassService;
import com.ruoyi.hr.service.IHrAttendanceGroupService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysConfigService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrOvertimeMapper;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 加班申请Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-06-11
 */
@Service
@org.apache.dubbo.config.annotation.Service
public class HrOvertimeServiceImpl implements IHrOvertimeService , IHrDomainConvert<OverTimeDTO,HrOvertime>
{

    private static final Logger logger = LoggerFactory.getLogger(HrOvertimeServiceImpl.class);

    @Resource
    private HrOvertimeMapper hrOvertimeMapper;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private HrEmpMapper hrEmpMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private IHolidayService holidayService;

    @Autowired
    private IHrAttendanceGroupService hrAttendanceGroupService;

    @Autowired
    private ITDateService dateService;

    @Autowired
    private IHrAttendanceClassService hrAttendanceClassService;
    
    @Autowired
    private ISysConfigService sysConfigService;

    /**
     * 查询加班申请
     * 
     * @param id 加班申请ID
     * @return 加班申请
     */
    @Override
    public HrOvertime selectHrOvertimeById(Long id)
    {
        HrOvertime hrOvertime = hrOvertimeMapper.selectByPrimaryKey(id);
        SysUser sysUser = userMapper.selectUserByLoginName(hrOvertime.getCreateBy());
        if (sysUser != null) {
            hrOvertime.setApplyUserName(sysUser.getUserName());
        }
        return hrOvertime;
    }

    /**
     * 查询加班申请列表
     * 
     * @param hrOvertime 加班申请
     * @return 加班申请
     */
    @Override
    public List<HrOvertime> selectHrOvertimeList(HrOvertime hrOvertime)
    {
        List<HrOvertime> hrOvertimes = hrOvertimeMapper.selectHrOvertimeList(hrOvertime);
        for (HrOvertime overtime: hrOvertimes) {
            SysUser sysUser = userMapper.selectUserByLoginName(overtime.getCreateBy());
            if (sysUser != null) {
                overtime.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(overtime.getApplyUser());
            if (sysUser2 != null) {
                overtime.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(overtime.getInstanceId())) {
                // 例如加班会签，会同时拥有多个任务
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(overtime.getInstanceId()).list();
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    String assignee = processHandleService.selectToUserNameByAssignee(task);
                    overtime.setTaskId(task.getId());
                    overtime.setTaskName(task.getName());
                    overtime.setTodoUserName(assignee);
                } else {
                    overtime.setTaskName("已办结");
                }
            } else {
                overtime.setTaskName("未启动");
            }
        }
        return hrOvertimes;
    }

    /**
     * 新增加班申请
     * 
     * @param hrOvertime 加班申请
     * @return 结果
     */
    @Override
    public int insertHrOvertime(HrOvertime hrOvertime)
    {
        hrOvertime.setApplyUser(ShiroUtils.getLoginName());
        hrOvertime.setApplyUserName(ShiroUtils.getUserName());
        hrOvertime.setApplyTime(DateUtils.getNowDate());
        hrOvertime.setCreateId(ShiroUtils.getUserId());
        hrOvertime.setEmpId(ShiroUtils.getUserId());
        hrOvertime.setCreateBy(ShiroUtils.getLoginName());
        hrOvertime.setCreateTime(DateUtils.getNowDate());

//        saveHoliday(hrOvertime,ShiroUtils.getUserId());

        return hrOvertimeMapper.insertSelective(hrOvertime);
    }

    /**
     * 是否为公休加班
     * @param hrOvertime
     */
    public void saveHoliday(HrOvertime hrOvertime) {
        //构建时间运算对象
        DateOperation dateOperation = new DateOperation(hrOvertime.getStartTime(), hrOvertime.getEndTime(),
                null, null,
                hrOvertime.getStartTime(),hrOvertime.getEndTime());
        //考虑到夜班所有这里从前一天开始
        dateOperation.setAttendanceDay(DateUtil.beginOfDay(DateUtil.offsetDay(dateOperation.getStartTime(),-1)));
        Integer shiftCriticalPoint = Integer.parseInt(sysConfigService.selectConfigByKey(SysConfigEnum.SysConfig.shift_critical_point.getValue()));

        saveHoildayInfo(hrOvertime,dateOperation,shiftCriticalPoint);

    }

    public void saveHoildayInfo(HrOvertime hrOvertime, DateOperation dateOperation, Integer shiftCriticalPoint) {
        //根据开始时间获取班次的DateOperation对象
        List<DateOperation> dateOperationList = new ArrayList<>();
        getClassDateOperation(hrOvertime.getEmpId(),
                dateOperation, shiftCriticalPoint, dateOperationList);
        DateOperation operationI = null;
        DateOperation operationJ = null;
        for (int i = 0; i < dateOperationList.size() - 1; i++) {
            operationI = dateOperationList.get(i);
            operationJ = dateOperationList.get(i + 1);
            if (!operationI.getShiftCriticalPointEndDate().equals(operationJ.getShiftCriticalPointStartDate())){
                //前一个的日期对象的结束时间不等于下一天的开始时间,就手动设置为一致
                operationJ.setShiftCriticalPointStartDate(operationI.getShiftCriticalPointEndDate());
            }
        }

        DateOperation operation = null;

        Boolean isOne = false;
        for (int i = 0; i < dateOperationList.size(); i++) {
            operation = dateOperationList.get(i);
            if (i == 0 &&
                    DateUtil.date(operation.getShiftCriticalPointEndDate()).isBeforeOrEquals(dateOperation.getOriginalStart())){
                //第一条数据是考虑夜班所有添加的前一天是数据，结束时间在加班开始时间之前所有跳过
                isOne = true;
                continue;
            }
            if (i == 0 || (isOne && i == 1)){
                //开头的日期对象
                operation.setShiftCriticalPointStartDate(dateOperation.getOriginalStart());
            }else if (i == dateOperationList.size() - 1){
                //结尾的日期对象
                operation.setShiftCriticalPointEndDate(dateOperation.getOriginalEnd());
            }
            //插入加班数据
            //添加加班数据
            Holiday holiday = new Holiday();
            BeanUtils.copyProperties(hrOvertime,holiday);
            holiday.setId(null);
            holiday.setType("1");//加班工时
            holiday.setEmpName(hrOvertime.getApplyUserName());
            holiday.setUserId(hrOvertime.getEmpId());
            holiday.setSource(hrOvertime.getInstanceId());//流程实例ID

            holiday.setStartDate(operation.getShiftCriticalPointStartDate());
            holiday.setEndDate(DateUtil.offsetMonth(operation.getShiftCriticalPointEndDate(),3));
            holiday.setHours(getHours(operation.getShiftCriticalPointEndDate(),operation.getShiftCriticalPointStartDate()));

            holiday.setOvertimeId(hrOvertime.getId());
            holiday.setIsPublic(operation.getIsPublic() ? 1 : 0);
            if (hrOvertime.getType().equals("1")){
                //法定假加班
                holiday.setIsPublic(2);
            }
            holiday.setAttendanceDate(operation.getAttendanceDay());
            holiday.setRemark("加班审批通过后插入加班详情数据");
            holiday.setCreateId(hrOvertime.getEmpId());
            holiday.setCreateBy(hrOvertime.getApplyUser());
            holidayService.insertHoliday(holiday);
        }
    }


    /**
     * 获取加班时间段内所有的班次时间段
     * @param empId
     * @param dateOperation
     * @param shiftCriticalPoint
     * @param dateOperationList
     * @return
     */
    private List<DateOperation> getClassDateOperation(Long empId, DateOperation dateOperation,
                                                Integer shiftCriticalPoint, List<DateOperation> dateOperationList) {

        HrAttendanceClass attendanceClass = hrAttendanceClassService.getAttendanceClass(empId, dateOperation.getAttendanceDay());
        DateOperation classDateOperation = null;
        if (attendanceClass != null){
            classDateOperation = hrAttendanceClassService.getDateOperation(dateOperation.getAttendanceDay(), attendanceClass,empId,shiftCriticalPoint);
            dateOperationList.add(classDateOperation);
            //重设置开始时间和班次日期
            dateOperation.setStartTime(classDateOperation.getShiftCriticalPointEndDate());
            dateOperation.setAttendanceDay(DateUtil.offsetDay(dateOperation.getAttendanceDay(),1));
        }else {
            //没有班次数据手动添加一个数据
            classDateOperation = new DateOperation();
            classDateOperation.setShiftCriticalPointStartDate(dateOperation.getAttendanceDay());
            classDateOperation.setShiftCriticalPointEndDate(DateUtil.beginOfDay(DateUtil.offsetDay(dateOperation.getAttendanceDay(),1)));
            classDateOperation.setIsPublic(true);
            classDateOperation.setAttendanceDay(dateOperation.getAttendanceDay());
            dateOperationList.add(classDateOperation);
            //重设置开始时间和班次日期
            dateOperation.setStartTime(classDateOperation.getShiftCriticalPointEndDate());
            dateOperation.setAttendanceDay(DateUtil.offsetDay(dateOperation.getAttendanceDay(),1));
        }
        if (DateUtil.date(dateOperation.getOriginalEnd()).isAfterOrEquals(dateOperation.getAttendanceDay())){
            //加班时间已经递归运算到了最后
            getClassDateOperation(empId,
                    dateOperation,shiftCriticalPoint,dateOperationList);
        }

        return dateOperationList;
    }

    @Override
    public Double getHours(Date endTime, Date dateTime) {
        long hourNum = DateUtil.between(dateTime, endTime, DateUnit.HOUR, false);
        long betweenMs = DateUtil.betweenMs(DateUtil.offsetHour(dateTime,Integer.parseInt(hourNum+"")),endTime);
        Double publicHours = 0D;
        if (betweenMs >= 3599999){
            //工具类精度问题，这里直接算一个小时
            publicHours = Double.valueOf(hourNum) + 1D;
        } else if (betweenMs >= 1800000){
            publicHours = Double.valueOf(hourNum) + 0.5D;

            /*publicHours = Double.valueOf(hourNum) + new BigDecimal(( betweenMs / 3600000 ))
                    .setScale(1, BigDecimal.ROUND_DOWN).doubleValue();*/
        }else {
            publicHours = Double.valueOf(hourNum);
        }
        return publicHours;
    }


    /**
     * 新增加班申请
     *
     * @param overTimeDTO 加班申请
     * @return 结果
     */
    @Override
    @Transactional
    public int insertOvertimeDTO(OverTimeDTO overTimeDTO) {
        int i = 0;
        String applyUserNum = overTimeDTO.getApplyUserNum();
        String status = overTimeDTO.getStatus();
        if(StringUtils.isNotBlank(status) && !"已撤销".equals(status) && StringUtils.isNotBlank(applyUserNum)){
            HrEmp hrEmp = hrEmpMapper.selectHrEmpByEmpNum(applyUserNum);
            if(hrEmp != null){
                Long userId = hrEmp.getUserId();
                if(userId != null){
                    SysUser sysUser = userMapper.selectUserById(userId);
                    HrOvertime overtime = convert(overTimeDTO);
                    overtime.setApplyUser(sysUser.getLoginName());
                    overtime.setApplyUserName(sysUser.getUserName());
                    overtime.setCreateBy(ShiroUtils.getLoginName());
                    overtime.setCreateTime(DateUtils.getNowDate());
                    overtime.setInstanceId("文件导入流程");
                    overtime.setAuditStatus("2");
                    overtime.setDelFlag("0");
                    String type = overTimeDTO.getType();
                    //是否法定节假日加班
                    if("否".equals(type)){
                        type = "2";
                        overtime.setType(type);
                    } else {
                        type = "1";
                        overtime.setType(type);
                    }
                    overtime.setApplyTime(DateUtils.parseDate(overTimeDTO.getApplyTime()));
                    overtime.setStartTime(DateUtils.parseDate(overTimeDTO.getStartTime()));
                    overtime.setEndTime(DateUtils.parseDate(overTimeDTO.getEndTime()));
                    String totalTimes = overTimeDTO.getTotalTimes();
                    overtime.setTotalTimes(Double.valueOf(totalTimes));

                    Example example = new Example(HrOvertime.class);
                    example.createCriteria().andEqualTo("applyUser",overtime.getApplyUser())
                            .andEqualTo("applyTime",overtime.getApplyTime())
                            .andEqualTo("startTime",overtime.getStartTime())
                            .andEqualTo("endTime",overtime.getEndTime());
                    //存在则更新，不存在则新增
                    HrOvertime oldOvertime = hrOvertimeMapper.selectSingleOneByExample(example);
                    if ( oldOvertime == null) {
                        saveHoliday(overtime);
                        i = hrOvertimeMapper.insert(overtime);
                    } else {
                        overtime.setId(oldOvertime.getId());
                        i = hrOvertimeMapper.updateByPrimaryKey(overtime);
                    }
                }
            }
        }
        return i;
    }

    /**
     * 修改加班申请
     * 
     * @param hrOvertime 加班申请
     * @return 结果
     */
    @Override
    public int updateHrOvertime(HrOvertime hrOvertime)
    {
        hrOvertime.setUpdateId(ShiroUtils.getUserId());
        hrOvertime.setUpdateBy(ShiroUtils.getLoginName());
        hrOvertime.setUpdateTime(DateUtils.getNowDate());
        return hrOvertimeMapper.updateByPrimaryKeySelective(hrOvertime);
    }

    /**
     * 删除加班申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrOvertimeByIds(String ids)
    {
        return hrOvertimeMapper.deleteHrOvertimeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除加班申请信息
     * 
     * @param id 加班申请ID
     * @return 结果
     */
    @Override
    public int deleteHrOvertimeById(Long id)
    {
        return hrOvertimeMapper.deleteHrOvertimeById(id);
    }

    /**
     * 启动流程
     * @param hrOvertime
     * @param applyUserId
     * @return
     */
    @Override
    @Transactional
    public AjaxResult submitApply(HrOvertime hrOvertime, String applyUserId) {
        hrOvertime.setApplyUser(applyUserId);
        hrOvertime.setApplyTime(DateUtils.getNowDate());
        hrOvertime.setUpdateBy(applyUserId);
        //修改流程状态为审核中
        hrOvertime.setAuditStatus("1");
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrOvertime.getId().toString();

        HashMap<String, Object> variables = new HashMap<>();

        processHandleService.setAssignee(ProcessKey.userDefined01OverTime,variables,hrOvertime.getDeptId(),(JSONObject) JSON.toJSON(hrOvertime));


        // 建立双向关系
        hrOvertime.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01OverTime,businessKey,applyUserId,hrOvertime.getTitle(),variables).getProcessInstanceId());
        hrOvertimeMapper.updateByPrimaryKey(hrOvertime);
        return AjaxResult.success();
    }

    /**
     * 审批流程
     * @param hrOvertime
     * @param taskId
     * @param request
     */
    @Transactional
    public AjaxResult complete(HrOvertime hrOvertime, String taskId, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();

        processHandleService.setAssignee(ProcessKey.userDefined01OverTime,variables,hrOvertime.getDeptId(),taskId, (JSONObject)JSON.toJSON(hrOvertime));

        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean toBoolean = BooleanUtils.toBoolean(p_B_approved);

        if (toBoolean){
            //审批通过
            hrOvertime.setAuditStatus("2");

            processHandleService.complete(hrOvertime.getInstanceId(),ProcessKey.userDefined01OverTime,hrOvertime.getTitle(),taskId,variables,
                    hrOvertime.getAuditStatus().equals("2"),comment,BooleanUtils.toBoolean(p_B_approved));

            List<Task> list = taskService.createTaskQuery().processInstanceId(hrOvertime.getInstanceId()).active().list();
            if (list.size() < 1){
                //流程结束了跟新下数据状态
                hrOvertimeMapper.updateByPrimaryKey(hrOvertime);

                //维护加班数据
                saveHoliday(hrOvertime);
            }
        }else {
            //审批拒绝
            hrOvertime.setAuditStatus("3");
            hrOvertimeMapper.updateByPrimaryKey(hrOvertime);

            processHandleService.completeDown(hrOvertime.getInstanceId(),taskId,variables,
                    comment,BooleanUtils.toBoolean(p_B_approved));
        }
        return AjaxResult.success("任务已完成");
    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {

        //维护业务数据
        Example example = new Example(HrQuit.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        HrOvertime hrOvertime = hrOvertimeMapper.selectSingleOneByExample(example);
        hrOvertime.setAuditStatus("4");
        hrOvertimeMapper.updateByPrimaryKeySelective(hrOvertime);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    /**
     * 计算加班时长
     * @param request
     */
    @Override
    public AjaxResult getOverTimes(HttpServletRequest request) {
        String s1 = request.getParameter("startTime");
        String s2 = request.getParameter("endTime");
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotBlank(s1) && StringUtils.isNotBlank(s2)) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDateTime startDateTime = LocalDateTime.parse(s1,df);
            LocalDateTime endDateTime = LocalDateTime.parse(s2,df);

            Duration duration = Duration.between(startDateTime,endDateTime);
            Double hours = Double.valueOf(duration.toHours());//相差的小时数
            long minutes = duration.toMinutes();//相差的分钟数
            long minute = minutes % 60;
            if(minute>=30){
                hours += 0.5;
            }
            resultMap.put("hours",hours);

            return AjaxResult.success("success",resultMap);
        }
        return AjaxResult.success("error",resultMap);
    }

    public Map<String,Object> formatHours(LocalDateTime startDateTime, LocalDateTime endDateTime){
        Map<String,Object> map = new HashMap<>();
        Double startHour = Double.valueOf(startDateTime.getHour());
        Double endHour = Double.valueOf(endDateTime.getHour());
        int startMinute = startDateTime.getMinute();
        int endMinute = endDateTime.getMinute();
        Duration duration = java.time.Duration.between(startDateTime,  endDateTime);
        long days = duration.toDays();
        if(startMinute > 0 && startMinute <= 30){
            startHour += 0.5;
        }
        if(endMinute > 0 && endMinute <= 30){
            endHour += 0.5;
        }
        if(startMinute > 30 && startMinute < 60){
            startHour += 1;
        }
        if(endMinute > 30 && endMinute < 60){
            endHour += 1;
        }

        map.put("startHour",startHour);
        map.put("endHour",endHour);
        return map;
    }

    @Override
    public HrOvertime convert(OverTimeDTO overTimeDTO) {
        HrOvertime overtime = new HrOvertime();
        try {
            BeanUtils.copyProperties(overTimeDTO,overtime);
        } catch (Exception e) {
            logger.error("对象属性拷贝异常");
            e.printStackTrace();
        }
        return overtime;
    }

    @Override
    public HrOvertime selectSingleOneByExample(Example example){
        return hrOvertimeMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<HrOvertime> selectByExample(Example example){
        return hrOvertimeMapper.selectByExample(example);
    }

    @Override
    public Double selectOvertimeByType(Long empId,int year,int month,String type) {
        return hrOvertimeMapper.selectOvertimeByType(empId,year,month,type);
    }

    @Override
    public List<HrOvertime> selectOvertimeManageList(HrOvertime hrOvertime) {
        List<HrOvertime> hrOvertimes = hrOvertimeMapper.selectOvertimeManageList(hrOvertime);
        for (HrOvertime overtime: hrOvertimes) {
            SysUser sysUser = userMapper.selectUserByLoginName(overtime.getCreateBy());
            if (sysUser != null) {
                overtime.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(overtime.getApplyUser());
            if (sysUser2 != null) {
                overtime.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(overtime.getInstanceId())) {
                // 例如加班会签，会同时拥有多个任务
                List<Task> taskList = taskService.createTaskQuery().processInstanceId(overtime.getInstanceId()).list();
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    String assignee = processHandleService.selectToUserNameByAssignee(task);
                    overtime.setTaskId(task.getId());
                    overtime.setTaskName(task.getName());
                    overtime.setTodoUserName(assignee);
                } else {
                    overtime.setTaskName("已办结");
                }
            } else {
                overtime.setTaskName("未启动");
            }
        }
        return hrOvertimes;
    }

}
