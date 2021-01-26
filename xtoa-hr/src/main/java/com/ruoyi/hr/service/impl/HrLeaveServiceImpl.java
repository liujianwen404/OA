package com.ruoyi.hr.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.ruoyi.base.domain.*;
import com.ruoyi.base.domain.DTO.DateOperation;
import com.ruoyi.base.utils.HoildayAndLeaveType;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.Arith;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.data.service.ITDateService;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.DTO.HrLeaveDTO;
import com.ruoyi.hr.manager.IHrDomainConvert;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.mapper.HrLeaveMapper;
import com.ruoyi.base.provider.hrService.IHrLeaveService;
import com.ruoyi.hr.service.*;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysConfigService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 请假业务Service业务层处理
 *
 * @author Xianlu Tech
 * @date 2019-10-11
 */
@Service
@org.apache.dubbo.config.annotation.Service
@Transactional
public class HrLeaveServiceImpl implements IHrLeaveService , IHrDomainConvert<HrLeaveDTO,HrLeave> {

    private static final Logger logger =  LoggerFactory.getLogger(HrLeaveServiceImpl.class);

    @Resource
    private HrLeaveMapper hrLeaveMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private ProcessHandleService processHandleService;

    @Resource
    private HrEmpMapper hrEmpMapper;

    @Autowired
    private IHolidayService holidayService;

    @Autowired
    private IHrAttendanceGroupService hrAttendanceGroupService;

    @Autowired
    private IHrAttendanceClassService hrAttendanceClassService;

    @Autowired
    private ITHrLeaveInfoService hrLeaveInfoService;


    /**
     * 查询请假业务
     *
     * @param id 请假业务ID
     * @return 请假业务
     */
    @Override
    public HrLeave selectHrLeaveById(Long id) {
        HrLeave leave = hrLeaveMapper.selectHrLeaveById(id);
        SysUser sysUser = userMapper.selectUserByLoginName(leave.getCreateBy());
        if (sysUser != null) {
            leave.setApplyUserName(sysUser.getUserName());
        }
        if (StringUtils.isNotEmpty(leave.getImgUrls())){
            leave.setImgList(Arrays.asList(leave.getImgUrls().split(",")));
        }

        return leave;
    }

    /**
     * 查询请假业务列表
     *
     * @param hrLeave 请假业务
     * @return 请假业务
     */
    @Override
    public List<HrLeave> selectHrLeaveViewList(HrLeave hrLeave) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        // PageHelper 仅对第一个 List 分页
        Page<HrLeave> list = (Page<HrLeave>) hrLeaveMapper.selectHrLeaveViewList(hrLeave);
        Page<HrLeave> returnList = new Page<>();
//        List<HrLeave> list = hrLeaveMapper.selectHrLeaveList(hrLeave);
//        List<HrLeave> returnList = new ArrayList<>();
        for (HrLeave leave: list) {
            setInfo(leave);

            returnList.add(leave);
        }
        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
        returnList.setPageNum(pageNum);
        returnList.setPageSize(pageSize);
        return returnList;
    }
    /**
     * 查询请假业务列表
     *
     * @param hrLeave 请假业务
     * @return 请假业务
     */
    @Override
    public List<HrLeave> selectHrLeaveViewListExcel(HrLeave hrLeave) {

        // PageHelper 仅对第一个 List 分页
        List<HrLeave> list = hrLeaveMapper.selectHrLeaveViewList(hrLeave);
        List<HrLeave> returnList = new ArrayList<>();
        for (HrLeave leave: list) {
            setInfo(leave);
            returnList.add(leave);
        }

        return returnList;
    }

    private void setInfo(HrLeave leave) {
        SysUser sysUser = userMapper.selectUserByLoginName(leave.getCreateBy());
        if (sysUser != null) {
            leave.setCreateUserName(sysUser.getUserName());
        }
        SysUser sysUser2 = userMapper.selectUserByLoginName(leave.getApplyUser());
        if (sysUser2 != null) {
            leave.setApplyUserName(sysUser2.getUserName());
        }
        // 当前环节
        if (StringUtils.isNotBlank(leave.getInstanceId())) {
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(leave.getInstanceId())
//                        .singleResult();
                    .list();    // 例如请假会签，会同时拥有多个任务
            if (!CollectionUtils.isEmpty(taskList)) {
                Task task = taskList.get(0);
                if(StringUtils.isNotBlank(task.getAssignee())){
                    SysUser sysUser3 = userMapper.selectUserByLoginName(task.getAssignee());
                    leave.setTaskId(task.getId());
                    leave.setTaskName(task.getName());
                    if (sysUser3 != null) {
                        leave.setTodoUserName(sysUser3.getUserName());
                    }
                }
                leave.setTaskId(task.getId());
                leave.setTaskName(task.getName());
                leave.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
            } else {
                leave.setTaskName("已办结");
            }
        } else {
            leave.setTaskName("未启动");
        }

        leave.setTypeExcel(HoildayAndLeaveType.getHoildayAndLeaveStr(leave.getType()));

        String auditStatus = leave.getAuditStatus();
        switch (auditStatus){
            case "0":
                leave.setAuditStatusExcel("待提交");
                break;
            case "1":
                leave.setAuditStatusExcel("审核中");
                break;
            case "2":
                leave.setAuditStatusExcel("通过");
                break;
            case "3":
                leave.setAuditStatusExcel("拒绝");
                break;
            case "4":
                leave.setAuditStatusExcel("撤销");
                break;
        }

        leave.setTodoUserNameExcel(leave.getTodoUserName());
        leave.setTaskNameExcel(leave.getTaskName());
    }

    /**
     * 查询请假业务列表
     *
     * @param hrLeave 请假业务
     * @return 请假业务
     */
    @Override
    public List<HrLeave> selectHrLeaveList(HrLeave hrLeave) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        // PageHelper 仅对第一个 List 分页
        Page<HrLeave> list = (Page<HrLeave>) hrLeaveMapper.selectHrLeaveList(hrLeave);
        Page<HrLeave> returnList = new Page<>();
//        List<HrLeave> list = hrLeaveMapper.selectHrLeaveList(hrLeave);
//        List<HrLeave> returnList = new ArrayList<>();
        for (HrLeave leave: list) {
            setInfo(leave);
            returnList.add(leave);
        }
        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
        returnList.setPageNum(pageNum);
        returnList.setPageSize(pageSize);
        return returnList;
    }

    /**
     * 新增请假业务
     *
     * @param hrLeave 请假业务
     * @return 结果
     */
    @Override
    public int insertHrLeave(HrLeave hrLeave) {
        hrLeave.setApplyUser(ShiroUtils.getLoginName());
        hrLeave.setApplyUserName(ShiroUtils.getUserName());
        hrLeave.setApplyTime(DateUtils.getNowDate());
        hrLeave.setCreateId(ShiroUtils.getUserId());
        hrLeave.setEmpId(ShiroUtils.getUserId());
        hrLeave.setCreateBy(ShiroUtils.getLoginName());
        hrLeave.setCreateTime(DateUtils.getNowDate());

        if (StringUtils.isNotEmpty(hrLeave.getImgUrls())){
            //去掉前面的逗号
            hrLeave.setImgUrls( hrLeave.getImgUrls().startsWith(",") ? hrLeave.getImgUrls().substring(1) : hrLeave.getImgUrls());
        }

        return hrLeaveMapper.insertHrLeave(hrLeave);
    }

    /**
     * 新增请假业务
     *
     * @param hrLeaveDTO 请假业务
     * @return 结果
     */
    @Override
    @Transactional
    public int insertHrLeaveDTO(HrLeaveDTO hrLeaveDTO) {
        int i = 0;
        String applyUserNum = hrLeaveDTO.getApplyUserNum();
        String status = hrLeaveDTO.getStatus();
        if(StringUtils.isNotBlank(status) && !"已撤销".equals(status) && StringUtils.isNotBlank(applyUserNum)){
            HrEmp hrEmp = hrEmpMapper.selectHrEmpByEmpNum(applyUserNum);
            if(hrEmp != null){
                Long userId = hrEmp.getUserId();
                if(userId != null){
                    SysUser sysUser = userMapper.selectUserById(userId);
                    HrLeave hrLeave = convert(hrLeaveDTO);
                    hrLeave.setApplyUser(sysUser.getLoginName());
                    hrLeave.setApplyUserName(sysUser.getUserName());
                    hrLeave.setCreateBy(ShiroUtils.getLoginName());
                    hrLeave.setCreateTime(DateUtils.getNowDate());
                    String type = hrLeaveDTO.getType();
                    type = HoildayAndLeaveType.getHoildayAndLeaveType(type);
                    hrLeave.setType(type);
                    String totalHours = hrLeaveDTO.getTotalHours();
                    String hours = totalHours.substring(0,totalHours.length()-2);
                    if(StringUtils.isEmpty(hours)){
                        //如果这里为空，说明请假时长的单位是天而不是小时，所以截取的时候为空串
                        hours = totalHours.substring(0,1);
                        hrLeave.setTotalHours(Arith.mul(Double.valueOf(hours),7.5));
                    }else{
                        hrLeave.setTotalHours(Double.valueOf(hours));
                    }
                    hrLeave.setInstanceId("文件导入流程");
                    hrLeave.setAuditStatus("2");
                    hrLeave.setDelFlag("0");

                    //存在则更新，不存在则新增
                    Example example = new Example(HrLeave.class);
                    example.createCriteria().andEqualTo("applyUser",hrLeave.getApplyUser())
                                            .andEqualTo("applyTime",hrLeave.getApplyTime())
                                            .andEqualTo("startTime",hrLeave.getStartTime())
                                            .andEqualTo("endTime",hrLeave.getEndTime())
                                            .andEqualTo("type",hrLeave.getType());
                    HrLeave oldHrLeave = hrLeaveMapper.selectSingleOneByExample(example);
                    if ( oldHrLeave == null) {
                        i = hrLeaveMapper.insert(hrLeave);
                    } else {
                        hrLeave.setId(oldHrLeave.getId());
                        i = hrLeaveMapper.updateByPrimaryKey(hrLeave);
                    }
                }
            }
        }
        return i;
    }



    /**
     * 修改请假业务
     *
     * @param hrLeave 请假业务
     * @return 结果
     */
    @Override
    public int updateHrLeave(HrLeave hrLeave) {
        hrLeave.setUpdateId(ShiroUtils.getUserId());
        hrLeave.setUpdateBy(ShiroUtils.getLoginName());
        hrLeave.setUpdateTime(DateUtils.getNowDate());


        if (StringUtils.isNotEmpty(hrLeave.getImgUrls())){
            //前面拼接逗号
            String newImgs = hrLeave.getImgUrls().startsWith(",") ? hrLeave.getImgUrls() : ","+hrLeave.getImgUrls();
            HrLeave leave = hrLeaveMapper.selectHrLeaveById(hrLeave.getId());
            String imgUrls = leave.getImgUrls() + newImgs;
            imgUrls = imgUrls.startsWith(",") ? imgUrls.substring(1) : imgUrls;
            logger.info("新增图片：" + imgUrls);
            hrLeave.setImgUrls(imgUrls);
        }else {
            //不跟新图片
            hrLeave.setImgUrls(null);
        }


        return hrLeaveMapper.updateByPrimaryKeySelective(hrLeave);
    }

    /**
     * 删除请假业务对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrLeaveByIds(String ids) {
        return hrLeaveMapper.deleteHrLeaveByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除请假业务信息
     *
     * @param id 请假业务ID
     * @return 结果
     */
    @Override
    public int deleteHrLeaveById(Long id) {
        return hrLeaveMapper.deleteHrLeaveById(id);
    }

    /**
     * 启动流程
     * @param hrLeave
     * @param applyUserId
     * @return
     */
    @Override
    @Transactional
    public synchronized AjaxResult submitApply(HrLeave hrLeave, String applyUserId) {

        Map<String,Double> holidayHours = holidayService.getHolidayHours(ShiroUtils.getUserId());


        if (hrLeave.getType().equals("1")){
            //调休
            Double holidayType1 = holidayHours.get(HolidayServiceImpl.holidayType1);
            if (holidayType1 < hrLeave.getTotalHours() ){
                return AjaxResult.error("调休余额不足!");
            }
        }else if (hrLeave.getType().equals("4")){
            //年假
            Double holidayType4 = holidayHours.get(HolidayServiceImpl.holidayType4);
            if (holidayType4 < hrLeave.getTotalHours() ){
                return AjaxResult.error("年假余额不足!");
            }
        }

//        hrLeave = hrLeaveMapper.selectHrLeaveById(hrLeave.getId());
        hrLeave.setApplyUser(applyUserId);
        hrLeave.setApplyTime(DateUtils.getNowDate());
        hrLeave.setUpdateBy(applyUserId);
        //修改流程状态为待审核
        hrLeave.setAuditStatus("1");
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrLeave.getId().toString();

        Map<String,Object> values = new HashMap<>();
        processHandleService.setAssignee(ProcessKey.userDefined01Leave,values,hrLeave.getDeptId(),(JSONObject) JSON.toJSON(hrLeave));

        hrLeave.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01Leave,businessKey,applyUserId,hrLeave.getTitle(),values).getProcessInstanceId());

        if (hrLeave.getType().equals("1") || hrLeave.getType().equals("4")){
            //调休或年假
            StringBuilder hIds = new StringBuilder("");
            Double leaveTotalHours = hrLeave.getTotalHours();
            List<Holiday> list =  holidayService.selectHolidayType1ByUserId(ShiroUtils.getUserId(),hrLeave.getType());
            for (Holiday holiday : list) {
                if (leaveTotalHours <= 0D){
                    break;
                }
                Double hours = holiday.getHours();
                Double useHours = holiday.getUseHours();
                //剩余时长
                Double residueHours = hours - useHours;
                if (residueHours >= leaveTotalHours){
                    //剩余的足够了
                    holiday.setUseHours(useHours + leaveTotalHours );

                    hIds.append( holiday.getId() + ":" + leaveTotalHours + "," );
                    leaveTotalHours = 0D;
                }else {
                    //剩余的不够啦
                    holiday.setUseHours(hours);
                    hIds.append( holiday.getId() + ":" + residueHours + "," );
                    leaveTotalHours = leaveTotalHours - residueHours;
                }
                holidayService.updateHoliday(holiday);
            }

            if (hIds.length() > 0){
                hrLeave.setHolidayIds( hIds.substring(0,hIds.length() - 1));
            }

        }

        hrLeaveMapper.updateHrLeave(hrLeave);

        return AjaxResult.success();
    }

    /**
     * 查询待办任务
     */
    @Override
    @Transactional(readOnly = true)
    public List<HrLeave> findTodoTasks(HrLeave leave, String userId) {
        List<HrLeave> results = new ArrayList<>();
        List<Task> tasks = new ArrayList<Task>();

        // 根据当前人的ID查询
        List<Task> todoList = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee(userId).list();

        // 根据当前人未签收的任务
        List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey("leave").taskCandidateUser(userId).list();

        // 合并
        tasks.addAll(todoList);
        tasks.addAll(unsignedTasks);

        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
            String processInstanceId = task.getProcessInstanceId();

            // 条件过滤 1
            if (StringUtils.isNotBlank(leave.getInstanceId()) && !leave.getInstanceId().equals(processInstanceId)) {
                continue;
            }

            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String businessKey = processInstance.getBusinessKey();
            HrLeave leave2 = hrLeaveMapper.selectHrLeaveById(new Long(businessKey));

            // 条件过滤 2
            if (StringUtils.isNotBlank(leave.getType()) && !leave.getType().equals(leave2.getType())) {
                continue;
            }

            leave2.setTaskId(task.getId());
            leave2.setTaskName(task.getName());

            SysUser sysUser = userMapper.selectUserByLoginName(leave2.getApplyUser());
            leave2.setApplyUserName(sysUser.getUserName());

            results.add(leave2);
        }
        return results;
    }


//    @Override
//    public void complete(HrLeave leave, String taskId, Map<String, Object> variables) {
//        processHandleService.complete(leave.getInstanceId(),"leave",leave.getTitle(),taskId,variables,leave.getAuditStatus().equals("2"));
//    }

    /**
     * 审批流程
     * @param hrLeave
     * @param taskId
     * @param request
     */
    @Override
    @Transactional
    public synchronized AjaxResult complete(HrLeave hrLeave, String taskId, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();
        hrLeave = hrLeaveMapper.selectHrLeaveById(hrLeave.getId());

        processHandleService.setAssignee(ProcessKey.userDefined01Leave,variables,hrLeave.getDeptId(),taskId, (JSONObject)JSON.toJSON(hrLeave));

        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean toBoolean = BooleanUtils.toBoolean(p_B_approved);

        if (toBoolean){
            hrLeave.setAuditStatus("2");
            //审批通过
            processHandleService.complete(hrLeave.getInstanceId(),ProcessKey.userDefined01Leave,hrLeave.getTitle(),taskId,variables,
                    hrLeave.getAuditStatus().equals("2"),comment,BooleanUtils.toBoolean(p_B_approved));

            List<Task> list = taskService.createTaskQuery().processInstanceId(hrLeave.getInstanceId()).active().list();
            if (list.size() < 1){
                //流程结束了跟新下数据状态
                hrLeaveMapper.updateByPrimaryKey(hrLeave);


//                Integer shiftCriticalPoint = Integer.parseInt(sysConfigService.selectConfigByKey(SysConfigEnum.SysConfig.shift_critical_point.getValue()));

                //构建时间运算对象
                DateOperation dateOperation = new DateOperation(hrLeave.getStartTime(), hrLeave.getEndTime(),
                                                                null, null,
                                                                hrLeave.getStartTime(),hrLeave.getEndTime());
                saveLeaveInfo(hrLeave, dateOperation);


            }
        }else {
            hrLeave.setAuditStatus("3");

            if (hrLeave.getType().equals("1") || hrLeave.getType().equals("4")){
                //退回调休或年假
                if (StringUtils.isNotEmpty(hrLeave.getHolidayIds())){
                    String[] split = hrLeave.getHolidayIds().split(",");
                    Holiday holiday = null;
                    for (String hid : split) {
                        String[] split1 = hid.split(":");
                        holiday = holidayService.selectHolidayById(Long.parseLong(split1[0]));
                        if (holiday != null){
                            double v = holiday.getUseHours() - Double.parseDouble(split1[1]);
                            holiday.setUseHours(v < 0 ? 0 : v);
                            holidayService.updateHoliday(holiday);
                        }
                    }
                }
            }


            hrLeaveMapper.updateByPrimaryKey(hrLeave);
            //审批拒绝
            processHandleService.completeDown(hrLeave.getInstanceId(),taskId,variables,
                    comment,BooleanUtils.toBoolean(p_B_approved));
        }
        return AjaxResult.success("任务已完成");
    }

    @Override
    public void saveLeaveInfo(HrLeave hrLeave, DateOperation dateOperation) {
        //根据开始时间获取班次的DateOperation对象
        DateOperation classDateOperation = getClassDateOperation(hrLeave.getEmpId(), dateOperation);
        if (classDateOperation == null){
            //找不到考勤数据了，计算结束
            return;
        }

        //根据请假时间对象和班次时间对象计算
        List<Map<String,DateTime>> dateList = new ArrayList<>();
        if (classDateOperation.getOriginalRestStartDate() == null){
            //没有休息时间
            Map<String,DateTime> mapDate = new HashMap();
            mapDate.put("s",DateUtil.date(classDateOperation.getOriginalStart()));
            mapDate.put("e",DateUtil.date(classDateOperation.getOriginalEnd()));
            dateList.add(mapDate);
        }else {
            Map<String,DateTime> mapDate = new HashMap();
            mapDate.put("s",DateUtil.date(classDateOperation.getOriginalStart()));
            mapDate.put("e",DateUtil.date(classDateOperation.getOriginalRestStartDate()));
            dateList.add(mapDate);
            mapDate = new HashMap();
            mapDate.put("s",DateUtil.date(classDateOperation.getOriginalRestEndDate()));
            mapDate.put("e",DateUtil.date(classDateOperation.getOriginalEnd()));
            dateList.add(mapDate);
        }

        Double resultDouble = 0D;
        resultDouble = hrAttendanceGroupService.getResultDouble(DateUtil.date(hrLeave.getStartTime())
                ,DateUtil.date(hrLeave.getEndTime())
                ,resultDouble,dateList);

        //插入请假详情数据
        THrLeaveInfo hrLeaveInfo = new THrLeaveInfo();
        BeanUtil.copyProperties(hrLeave,hrLeaveInfo);
        hrLeaveInfo.setId(null);
        hrLeaveInfo.setLeaveId(hrLeave.getId());
        hrLeaveInfo.setEmpId(hrLeave.getEmpId());
        hrLeaveInfo.setEmpName(hrLeave.getApplyUserName());
        hrLeaveInfo.setType(hrLeave.getType());
        hrLeaveInfo.setStartTime(dateOperation.getStartTime().after(classDateOperation.getOriginalStart())
                ? dateOperation.getStartTime() : classDateOperation.getOriginalStart());
        hrLeaveInfo.setEndTime(dateOperation.getOriginalEnd().before(classDateOperation.getOriginalEnd())
                ? dateOperation.getOriginalEnd() : classDateOperation.getOriginalEnd());
        hrLeaveInfo.setTotalHours(resultDouble);
        hrLeaveInfo.setDeptId(hrLeave.getDeptId());
        hrLeaveInfo.setPostId(hrLeave.getPostId());
        hrLeaveInfo.setCreateTime(new Date());
        hrLeaveInfo.setUpdateTime(new Date());
        hrLeaveInfo.setRemark("请假审批通过后插入请假详情数据");
        hrLeaveInfoService.insertTHrLeaveInfo(hrLeaveInfo);
//        System.out.println(hrLeaveInfo);
        //一笔数据已切割，需要重置开始时间
        dateOperation.setStartTime(classDateOperation.getOriginalEnd());
        if (!DateUtil.date(dateOperation.getOriginalEnd()).isBeforeOrEquals(classDateOperation.getOriginalStart())){
//        if (!DateUtil.date(dateOperation.getOriginalEnd()).isBeforeOrEquals(classDateOperation.getOriginalEnd())){
            //请假结束时间没有比寻找到的班次结束时间小
            //需要递归调用算法
            saveLeaveInfo(hrLeave,dateOperation);
        }
    }

    private DateOperation getClassDateOperation(Long empId, DateOperation dateOperation) {
        //获取前一天的班次信息
        DateTime offsetDay = DateUtil.offsetDay(dateOperation.getStartTime(), -1);
        HrAttendanceClass attendanceClass = hrAttendanceClassService.getAttendanceClass(empId, offsetDay);
        DateOperation classDateOperation = null;
        if (attendanceClass != null){
            classDateOperation = getDateOperation(offsetDay, attendanceClass,empId);
        }else {
            //使用当天是班次数据
            classDateOperation = null;
            attendanceClass = hrAttendanceClassService.getAttendanceClass(empId, dateOperation.getStartTime());
            if (attendanceClass != null){
                classDateOperation = getDateOperation(dateOperation.getStartTime(), attendanceClass, empId);
            }
        }

        if (classDateOperation != null){
            if (DateUtil.date(classDateOperation.getOriginalEnd()).isBeforeOrEquals(dateOperation.getStartTime())) {
                //前一天的班次已经结束，使用当天是班次数据
                classDateOperation = null;
                attendanceClass = hrAttendanceClassService.getAttendanceClass(empId, dateOperation.getStartTime());
                if (attendanceClass != null){
                    classDateOperation = getDateOperation(dateOperation.getStartTime(), attendanceClass, empId);
                }
            }
        }

        //班次数据为空或者班次时间范围不在请假时间范围内
        if (classDateOperation == null ||
                DateUtil.date(classDateOperation.getOriginalEnd()).isBeforeOrEquals(dateOperation.getStartTime())){
            classDateOperation = null;//置空不符合条件的班次时间数据
            Integer offsetDayInt = 0;
            while (classDateOperation == null){
                //还是没有找到可用的班次数据就继续往后面找
                offsetDayInt = offsetDayInt + 1;
                offsetDay = DateUtil.offsetDay(dateOperation.getStartTime(), offsetDayInt);
                attendanceClass = hrAttendanceClassService.getAttendanceClass(empId, offsetDay);
                if (attendanceClass != null){
                    classDateOperation = getDateOperation(offsetDay, attendanceClass, empId);
                }
                if (classDateOperation != null){

                    if (DateUtil.date(dateOperation.getOriginalEnd()).before(classDateOperation.getOriginalStart())){
//                    if (DateUtil.date(dateOperation.getOriginalEnd()).before(classDateOperation.getOriginalEnd())){
                        //请假结束时间比寻找到的班次结束时间还要小，说明已经计算结束了
                        classDateOperation = null;
                        break;
                    }

                    if (classDateOperation.getOriginalEnd().before(dateOperation.getStartTime())){
                        //请假开始时间不在这个班次的范围内，继续寻找，，，
                        classDateOperation = null;
                    }
                }
            }
        }

        if (classDateOperation != null && DateUtil.date(dateOperation.getOriginalEnd()).before(classDateOperation.getOriginalStart())){
//                    if (DateUtil.date(dateOperation.getOriginalEnd()).before(classDateOperation.getOriginalEnd())){
            //请假结束时间比寻找到的班次结束时间还要小，说明已经计算结束了
            classDateOperation = null;
        }
        return classDateOperation;
    }

    /**
     * 通过日期获取班次的各个时间
     * @param offsetDay 那一天
     * @param attendanceClass 哪一个班次数据
     * @param empId 谁
     */
    @Override
    public DateOperation getDateOperation(Date offsetDay, HrAttendanceClass attendanceClass, Long empId) {
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

            dateOperation = new DateOperation(workDate, closingDate,
                    restStartDate, restEndDate,
                    workDate, closingDate);
        }


        return dateOperation;


    }

    /**
     * 查询已办列表
     * @param hrLeave
     * @param userId
     * @return
     */
    @Override
    public List<HrLeave> findDoneTasks(HrLeave hrLeave, String userId) {
        List<HrLeave> results = new ArrayList<>();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey("leave")
                .taskAssignee(userId)
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .list();

        // 根据流程的业务ID查询实体并关联
        for (HistoricTaskInstance instance : list) {
            String processInstanceId = instance.getProcessInstanceId();

            // 条件过滤 1
            if (StringUtils.isNotBlank(hrLeave.getInstanceId()) && !hrLeave.getInstanceId().equals(processInstanceId)) {
                continue;
            }

            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

            String businessKey = processInstance.getBusinessKey();
            HrLeave leave2 = hrLeaveMapper.selectHrLeaveById(new Long(businessKey));

            // 条件过滤 2
            if (StringUtils.isNotBlank(hrLeave.getType()) && !hrLeave.getType().equals(leave2.getType())) {
                continue;
            }

            leave2.setTaskId(instance.getId());
            leave2.setTaskName(instance.getName());
            leave2.setDoneTime(instance.getEndTime());

            SysUser sysUser = userMapper.selectUserByLoginName(leave2.getApplyUser());
            leave2.setApplyUserName(sysUser.getUserName());

            results.add(leave2);
        }
        return results;
    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {

        //维护业务数据
        Example example = new Example(HrLeave.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        HrLeave leave = hrLeaveMapper.selectSingleOneByExample(example);
        leave.setAuditStatus("4");

        if (leave.getType().equals("1") || leave.getType().equals("4")){
            //退回调休或年假
            if (StringUtils.isNotEmpty(leave.getHolidayIds())){
                String[] split = leave.getHolidayIds().split(",");
                Holiday holiday = null;
                for (String hid : split) {
                    String[] split1 = hid.split(":");
                    holiday = holidayService.selectHolidayById(Long.parseLong(split1[0]));
                    if (holiday != null){
                        double v = holiday.getUseHours() - Double.parseDouble(split1[1]);
                        holiday.setUseHours(v < 0 ? 0 : v);
                        holidayService.updateHoliday(holiday);
                    }
                }
            }
        }


        hrLeaveMapper.updateByPrimaryKeySelective(leave);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult getLeaveTime(HttpServletRequest request) {
        String s1 = request.getParameter("startTime");
        String s2 = request.getParameter("endTime");
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotBlank(s1) && StringUtils.isNotBlank(s2)) {

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDate startDate = LocalDate.parse(s1,df);
            LocalDate endDate = LocalDate.parse(s2,df);
            LocalDateTime startDateTime = LocalDateTime.parse(s1,df);
            LocalDateTime endDateTime = LocalDateTime.parse(s2,df);

            Map<String, Object> map = formatStartHours(startDateTime, endDateTime);
            Double startHour = (Double) map.get("startHour");
            Double endHour = (Double) map.get("endHour");
            long days = endDate.toEpochDay() - startDate.toEpochDay();
//            Duration duration = java.time.Duration.between(startDateTime,  endDateTime);
//            long days = duration.toDays();
//            long hours = duration.toHours();
            Double hours = endHour - startHour;
            //如果是同一天
            if (days == 0) {
                resultMap.put("days",days);
                //如果都是半天
                if((startHour <= 12 && endHour <= 12) || (startHour >= 13.5 && endHour >= 13.5)){
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }
                //如果在午休期间请假，直接从下午一点半开始计算
                if(startHour > 12 && startHour < 13.5){
                    hours = (Double) (endHour-13.5);
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }
                //否则减去午休的一个半小时
                hours -= 1.5;
                resultMap.put("hours",hours);
                return AjaxResult.success("success",resultMap);
            }
            //不是同一天
            //如果都是半天
            if((startHour <= 12 && endHour <= 12) || (startHour >= 13.5 && endHour >= 13.5)){

                if(startHour == 18.5 && endHour >= 13.5){
                    days -= 1;
                    hours = (Double) (endHour - 9.5 - 1.5+(days*7.5));
                    resultMap.put("days",days);
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }
                if(startHour <= 12 && endHour == 9.5){
                    days -= 1;
                    hours = (Double) (18.5-startHour-1.5+(days*7.5));
                    resultMap.put("days",days);
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }
                hours = (Double) (hours+(days*7.5));
                resultMap.put("days",days);
                resultMap.put("hours",hours);
                return AjaxResult.success("success",resultMap);
            }
            //如果开始是上午，结束是下午
            if(startHour <= 12 && endHour >= 13.5){
                hours = (Double) (hours-1.5+(days*7.5));
                resultMap.put("days",days);
                resultMap.put("hours",hours);
                return AjaxResult.success("success",resultMap);
            }
            //如果开始是下午，结束是上午
            if(startHour > 12 && endHour <= 13.5){
                if(startHour == 18.5 && endHour == 9.5){
                    days -= 1;
                    resultMap.put("days",days);
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }
                if(startHour == 18.5 && endHour <= 12){
                    days -= 1;
                    hours = (Double) (endHour - 9.5+(days*7.5));
                    resultMap.put("days",days);
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }
                if(startHour == 18.5 && endHour > 12){
                    days -= 1;
                    hours = (Double) (2.5+(days*7.5));
                    resultMap.put("days",days);
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }
                if(endHour == 9.5){
                    days -= 1;
                    hours = (Double) (18.5 - startHour+(days*7.5));
                    resultMap.put("days",days);
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }
                if(startHour <= 13.5){
                    days -= 1;
                    hours = (Double) (5 + endHour-9.5+(days*7.5));
                    resultMap.put("days",days);
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }
                if(endHour > 12){
                    days -= 1;
                    hours = (Double) (18.5-startHour + 2.5+(days*7.5));
                    resultMap.put("days",days);
                    resultMap.put("hours",hours);
                    return AjaxResult.success("success",resultMap);
                }

                days -= 1;
                hours = (Double) (18.5-startHour + endHour-9.5+(days*7.5));
                resultMap.put("days",days);
                resultMap.put("hours",hours);
                return AjaxResult.success("success",resultMap);
            }
            hours = (Double) (hours+(days*7.5));
            resultMap.put("days",days);
            resultMap.put("hours",hours);

            return AjaxResult.success("success",resultMap);
        }
        return AjaxResult.success("error",resultMap);
    }

    @Override
    @Transactional
    public AjaxResult editDelImg(String img, Long id) {
        HrLeave hrLeave = hrLeaveMapper.selectHrLeaveById(id);
        String imgUrls = hrLeave.getImgUrls();
        if (imgUrls.contains(","+img)){
            hrLeave.setImgUrls(imgUrls.replace(","+img,""));
        }else if (imgUrls.contains(img)){
            String replace = imgUrls.replace(img, "");
            replace = replace.startsWith(",") ? replace.substring(1) : replace;
            hrLeave.setImgUrls(replace);
        }
        hrLeaveMapper.updateHrLeave(hrLeave);

        String filePath = Global.getProfile() + img.replace("/profile","");
        boolean b = FileUtils.deleteFile(filePath);
        logger.info("editDelImg: "+img + " ,result:" + b);
        return AjaxResult.success();
    }

    public Map<String,Object> formatStartHours(LocalDateTime startDateTime, LocalDateTime endDateTime){
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

        if(startHour <= 9.5){
            startHour = (Double) 9.5;
        }
        if(startHour >= 18.5){
            startHour = (Double) 18.5;
        }
        if(days==0){
            if(endHour >= 18.5){
                endHour = (Double) 18.5;
            }
        }else {
            if(endHour <= 9.5){
                endHour = (Double) 9.5;
            }
            if(endHour>=18.5){
                endHour = (Double) 18.5;
            }
        }
        map.put("startHour",startHour);
        map.put("endHour",endHour);
        return map;
    }

    @Override
    public HrLeave convert(HrLeaveDTO hrLeaveDTO) {
        HrLeave hrLeave = new HrLeave();
        try {
            BeanUtils.copyProperties(hrLeaveDTO,hrLeave);
        } catch (Exception e) {
            logger.error("对象属性拷贝异常");
            e.printStackTrace();
        }
        return hrLeave;
    }

    @Override
    public Double selectLeaveByType(Long empId, int year, int month, String type) {
        return hrLeaveMapper.selectLeaveByType(empId,year,month,type);
    }

    @Override
    public Double attendanceGroupHandle(String startStr , String endStr,Long userId) {
        DateTime startTime = DateUtil.parse(startStr, "yyyy-MM-dd HH:mm");
        DateTime endTime = DateUtil.parse(endStr, "yyyy-MM-dd HH:mm");
        //相差多少天
        long betweenDay = DateUtil.betweenDay(startTime, endTime, true);
        Double workHour = 0D;
        if (betweenDay < 1){

                    workHour = hrAttendanceGroupService.todayIsWorkHour(startTime,endTime,
                            userId);
        }else {
            for (long i = 0; i <= betweenDay; i++) {
                if (i== 0){

                    Double hour = hrAttendanceGroupService.todayIsWorkHour(startTime, DateUtil.endOfDay(startTime),
                            userId);
                    workHour = workHour + hour;

                }else if (i == betweenDay ){

                            workHour = workHour + hrAttendanceGroupService.todayIsWorkHour(DateUtil.beginOfDay(endTime),endTime,
                                    userId);

                }else {
                    //中间的天数
                    DateTime dateTime = DateUtil.offsetDay(startTime, Integer.parseInt(i + "") );

                            workHour = workHour + hrAttendanceGroupService.todayIsWorkHour(
                                    DateUtil.beginOfDay(dateTime),
                                    DateUtil.endOfDay(dateTime),
                                    userId);
                }
            }
        }

        return workHour;
    }

}
