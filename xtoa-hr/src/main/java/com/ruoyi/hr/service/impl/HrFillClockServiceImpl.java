package com.ruoyi.hr.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.ruoyi.base.domain.DTO.FillClockDTO;
import com.ruoyi.base.domain.HrAttendanceInfo;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.HrFillClock;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.manager.IHrDomainConvert;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.mapper.HrFillClockMapper;
import com.ruoyi.hr.service.IHrAttendanceInfoService;
import com.ruoyi.hr.service.IHrFillClockService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import org.activiti.engine.TaskService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 补卡申请Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-06-24
 */
@Service
public class HrFillClockServiceImpl implements IHrFillClockService , IHrDomainConvert<FillClockDTO, HrFillClock>
{

    private static final Logger logger = LoggerFactory.getLogger(HrFillClockServiceImpl.class);

    @Resource
    private HrFillClockMapper hrFillClockMapper;

    @Resource
    private HrEmpMapper hrEmpMapper;

    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Resource
    private IHrAttendanceInfoService hrAttendanceInfoService;

    /**
     * 查询补卡申请
     * 
     * @param id 补卡申请ID
     * @return 补卡申请
     */
    @Override
    public HrFillClock selectHrFillClockById(Long id)
    {
        return hrFillClockMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询补卡申请列表
     * 
     * @param hrFillClock 补卡申请
     * @return 补卡申请
     */
    @Override
    public List<HrFillClock> selectHrFillClockList(HrFillClock hrFillClock)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        // PageHelper 仅对第一个 List 分页
        Page<HrFillClock> list = (Page<HrFillClock>) hrFillClockMapper.selectHrFillClockList(hrFillClock);
        Page<HrFillClock> returnList = new Page<>();
        for (HrFillClock fillClock: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(fillClock.getCreateBy());
            if (sysUser != null) {
                fillClock.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(fillClock.getApplyUser());
            if (sysUser2 != null) {
                fillClock.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(fillClock.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(fillClock.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    fillClock.setTaskId(task.getId());
                    fillClock.setTaskName(task.getName());
                    fillClock.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    fillClock.setTaskName("已办结");
                }
            } else {
                fillClock.setTaskName("未启动");
            }


            /*fillClock.setExpires(0);
            if (DateUtil.offsetMonth(fillClock.getClassDate(),1)
                    .isBefore(DateUtil.offsetDay(DateUtil.beginOfMonth(new Date()),6))){
                fillClock.setExpires(1);
            }*/
            fillClock.setCount(selectHrFillClockCount(ShiroUtils.getUserId(), fillClock.getClassDate()));

            returnList.add(fillClock);
        }
        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
        returnList.setPageNum(pageNum);
        returnList.setPageSize(pageSize);
        return returnList;
    }

    /**
     * 新增补卡申请
     * 
     * @param hrFillClock 补卡申请
     * @return 结果
     */
    @Override
    public int insertHrFillClock(HrFillClock hrFillClock)
    {

        return hrFillClockMapper.insertSelective(hrFillClock);
    }

    /**
     * 修改补卡申请
     * 
     * @param hrFillClock 补卡申请
     * @return 结果
     */
    @Override
    public int updateHrFillClock(HrFillClock hrFillClock)
    {
        hrFillClock.setUpdateId(ShiroUtils.getUserId());
        hrFillClock.setUpdateBy(ShiroUtils.getLoginName());
        hrFillClock.setUpdateTime(DateUtils.getNowDate());
        return hrFillClockMapper.updateByPrimaryKeySelective(hrFillClock);
    }

    /**
     * 删除补卡申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrFillClockByIds(String ids)
    {
        return hrFillClockMapper.deleteHrFillClockByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除补卡申请信息
     * 
     * @param id 补卡申请ID
     * @return 结果
     */
    @Override
    public int deleteHrFillClockById(Long id)
    {
        return hrFillClockMapper.deleteHrFillClockById(id);
    }

    @Override
    public int insertFillClockDTO(FillClockDTO fillClockDTO) {
        int i = 0;
        String applyUserNum = fillClockDTO.getApplyUserNum();
        String status = fillClockDTO.getStatus();
        if(StringUtils.isNotBlank(status) && !"已撤销".equals(status) && StringUtils.isNotBlank(applyUserNum)){
            HrEmp hrEmp = hrEmpMapper.selectHrEmpByEmpNum(applyUserNum);
            if(hrEmp != null){
                Long userId = hrEmp.getUserId();
                if(userId != null){
                    HrFillClock fillClock = convert(fillClockDTO);
                    SysUser sysUser = userMapper.selectUserById(userId);
                    fillClock.setApplyUser(sysUser.getLoginName());
                    fillClock.setApplyUserName(sysUser.getUserName());
                    fillClock.setCreateBy(ShiroUtils.getLoginName());
                    fillClock.setCreateTime(DateUtils.getNowDate());
                    fillClock.setInstanceId("文件导入流程");
                    fillClock.setAuditStatus("2");
                    fillClock.setDelFlag("0");

                    //存在则更新，不存在则新增
                    Example example = new Example(HrFillClock.class);
                    example.createCriteria().andEqualTo("applyUser",fillClock.getApplyUser())
                                            .andEqualTo("dates",fillClock.getDates())
                                            .andEqualTo("classes",fillClock.getClasses());
                    HrFillClock oldClock = hrFillClockMapper.selectSingleOneByExample(example);
                    if ( oldClock == null) {
                        i = hrFillClockMapper.insertSelective(fillClock);
                    } else {
                        fillClock.setId(oldClock.getId());
                        i = hrFillClockMapper.updateByPrimaryKeySelective(fillClock);
                    }
                }
            }
        }
        return i;
    }

    @Override
    @Transactional
    public AjaxResult submitApply(HrFillClock hrFillClock, String applyUserLoginName) {
        hrFillClock.setApplyUser(applyUserLoginName);
        hrFillClock.setApplyTime(DateUtils.getNowDate());
        hrFillClock.setUpdateBy(applyUserLoginName);
        //修改流程状态为审核中
        hrFillClock.setAuditStatus("1");
        // 实体类 ID，作为流程的业务 key
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrFillClock.getId().toString();

        HashMap<String, Object> variables = new HashMap<>();

        processHandleService.setAssignee(ProcessKey.userDefined01FillClock,variables,hrFillClock.getDeptId(),(JSONObject) JSON.toJSON(hrFillClock));

        // 建立双向关系
        hrFillClock.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01FillClock,businessKey,applyUserLoginName,"补卡申请",variables).getProcessInstanceId());
        hrFillClockMapper.updateByPrimaryKey(hrFillClock);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        //维护业务数据
        Example example = new Example(HrFillClock.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        HrFillClock hrFillClock = hrFillClockMapper.selectSingleOneByExample(example);
        hrFillClock.setAuditStatus("4");
        hrFillClockMapper.updateByPrimaryKeySelective(hrFillClock);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult complete(HrFillClock hrFillClock, String taskId, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();

        processHandleService.setAssignee(ProcessKey.userDefined01FillClock,variables,hrFillClock.getDeptId(),taskId, (JSONObject)JSON.toJSON(hrFillClock));

        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean toBoolean = BooleanUtils.toBoolean(p_B_approved);

        if (toBoolean){
            //审批通过
            hrFillClock.setAuditStatus("2");

            processHandleService.complete(hrFillClock.getInstanceId(),ProcessKey.userDefined01FillClock,"补卡申请",taskId,variables,
                    hrFillClock.getAuditStatus().equals("2"),comment,BooleanUtils.toBoolean(p_B_approved));

            List<Task> list = taskService.createTaskQuery().processInstanceId(hrFillClock.getInstanceId()).active().list();
            if (list.size() < 1){
                //流程结束了跟新下数据状态
                hrFillClockMapper.updateByPrimaryKeySelective(hrFillClock);

                //打卡数据数据
                HrAttendanceInfo hrAttendanceInfo = hrAttendanceInfoService.selectHrAttendanceInfoById(hrFillClock.getAttendanceInfoId());
                if (hrAttendanceInfo != null){
                    hrAttendanceInfo.setUserCheckTime(hrFillClock.getDates());
                    hrAttendanceInfo.setTimeResult(HrAttendanceInfoServiceImpl.time_result_Normal);
                    hrAttendanceInfo.setSourceType(HrAttendanceInfoServiceImpl.source_type_APPROVE);
                    hrAttendanceInfoService.updateHrAttendanceInfo(hrAttendanceInfo);
                }

            }
        }else {
            //审批拒绝
            hrFillClock.setAuditStatus("3");
            hrFillClockMapper.updateByPrimaryKeySelective(hrFillClock);

            processHandleService.completeDown(hrFillClock.getInstanceId(),taskId,variables,
                    comment,BooleanUtils.toBoolean(p_B_approved));
        }
        return AjaxResult.success("任务已完成");
    }

    @Override
    public Integer selectHrFillClockCount(Long userId, Date classDate) {
        return hrFillClockMapper.selectHrFillClockCount(userId,classDate);
    }

    @Override
    public List<HrFillClock> selectHrFillClockListManage(HrFillClock hrFillClock) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        // PageHelper 仅对第一个 List 分页
        Page<HrFillClock> list = (Page<HrFillClock>) hrFillClockMapper.selectHrFillClockListManage(hrFillClock);
        Page<HrFillClock> returnList = new Page<>();
        for (HrFillClock fillClock: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(fillClock.getCreateBy());
            if (sysUser != null) {
                fillClock.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(fillClock.getApplyUser());
            if (sysUser2 != null) {
                fillClock.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(fillClock.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(fillClock.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    fillClock.setTaskId(task.getId());
                    fillClock.setTaskName(task.getName());
                    fillClock.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    fillClock.setTaskName("已办结");
                }
            } else {
                fillClock.setTaskName("未启动");
            }

            returnList.add(fillClock);
        }
        returnList.setTotal(CollectionUtils.isEmpty(list) ? 0 : list.getTotal());
        returnList.setPageNum(pageNum);
        returnList.setPageSize(pageSize);
        return returnList;
    }

    @Override
    public int selectHrFillClockNum(Long empId, Date classDate) {
        return hrFillClockMapper.selectHrFillClockNum(empId,classDate);
    }

    @Override
    public Integer selectHrFillClockCompleteCount(Long empId, Date classDate) {
        return hrFillClockMapper.selectHrFillClockCompleteCount(empId,classDate);
    }

    /**
     * 将数据接受对象的属性拷贝到对应的实体类
     * */
    @Override
    public HrFillClock convert(FillClockDTO fillClockDTO) {
        HrFillClock fillClock = new HrFillClock();
        try {
            BeanUtils.copyProperties(fillClockDTO,fillClock);
        } catch (Exception e) {
            logger.error("对象属性拷贝异常");
            e.printStackTrace();
        }
        return fillClock;
    }
}
