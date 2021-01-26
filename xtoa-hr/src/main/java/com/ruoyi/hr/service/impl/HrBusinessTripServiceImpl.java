package com.ruoyi.hr.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.base.domain.*;
import com.ruoyi.base.domain.DTO.BusinessTripDTO;
import com.ruoyi.hr.manager.IHrDomainConvert;
import com.ruoyi.hr.mapper.HrBusinessTripSonMapper;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
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
import com.ruoyi.hr.mapper.HrBusinessTripMapper;
import com.ruoyi.hr.service.IHrBusinessTripService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 出差申请Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-06-30
 */
@Service
public class HrBusinessTripServiceImpl implements IHrBusinessTripService, IHrDomainConvert<BusinessTripDTO,HrBusinessTrip>
{

    private static final Logger logger = LoggerFactory.getLogger(HrBusinessTripServiceImpl.class);

    @Resource
    private HrBusinessTripMapper hrBusinessTripMapper;

    @Resource
    private HrBusinessTripSonMapper hrBusinessTripSonMapper;

    @Resource
    private HrEmpMapper hrEmpMapper;

    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessHandleService processHandleService;

    /**
     * 查询出差申请
     * 
     * @param id 出差申请ID
     * @return 出差申请
     */
    @Override
    public HrBusinessTrip selectHrBusinessTripById(Long id)
    {
        return hrBusinessTripMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询出差申请列表
     * 
     * @param businessTrip 出差申请
     * @return 出差申请
     */
    @Override
    public List<HrBusinessTrip> selectHrBusinessTripList(HrBusinessTrip businessTrip)
    {
        List<HrBusinessTrip> list = hrBusinessTripMapper.selectHrBusinessTripList(businessTrip);
        for (HrBusinessTrip hrBusinessTrip: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(hrBusinessTrip.getCreateBy());
            if (sysUser != null) {
                hrBusinessTrip.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(hrBusinessTrip.getApplyUser());
            if (sysUser2 != null) {
                hrBusinessTrip.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(hrBusinessTrip.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(hrBusinessTrip.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    hrBusinessTrip.setTaskId(task.getId());
                    hrBusinessTrip.setTaskName(task.getName());
                    hrBusinessTrip.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    hrBusinessTrip.setTaskName("已办结");
                }
            } else {
                hrBusinessTrip.setTaskName("未启动");
            }
        }
        return list;
    }

    /**
     * 新增出差申请
     * 
     * @param hrBusinessTrip 出差申请
     * @return 结果
     */
    @Override
    @Transactional
    public int insertHrBusinessTrip(HrBusinessTrip hrBusinessTrip, HttpServletRequest request)
    {
        hrBusinessTrip.setApplyUser(ShiroUtils.getLoginName());
        hrBusinessTrip.setApplyUserName(ShiroUtils.getUserName());
        hrBusinessTrip.setApplyTime(DateUtils.getNowDate());
        hrBusinessTrip.setCreateId(ShiroUtils.getUserId());
        hrBusinessTrip.setCreateBy(ShiroUtils.getLoginName());
        hrBusinessTrip.setCreateTime(DateUtils.getNowDate());
        hrBusinessTripMapper.insertSelective(hrBusinessTrip);

        //出差行程数据插入到子表
        int count = Integer.parseInt(request.getParameter("count"));
        for (int i = 1; i <= count; i++) {
            HrBusinessTripSon hrBusinessTripSon = new HrBusinessTripSon();
            hrBusinessTripSon.setCreateId(ShiroUtils.getUserId());
            hrBusinessTripSon.setCreateBy(ShiroUtils.getLoginName());
            hrBusinessTripSon.setCreateTime(DateUtils.getNowDate());
            hrBusinessTripSon.setParentId(hrBusinessTrip.getId());
            String vehicle = request.getParameter("vehicle" + i);
            String journey = request.getParameter("journey" + i);
            String departCity = request.getParameter("departCity" + i);
            String destinationCity = request.getParameter("destinationCity" + i);
            String startTime = request.getParameter("startTime" + i);
            String endTime = request.getParameter("endTime" + i);
            String totalTimes = request.getParameter("totalTimes" + i);
            if (StringUtils.isNotBlank(vehicle)) {
                hrBusinessTripSon.setVehicle(vehicle);
            }
            if (StringUtils.isNotBlank(journey)) {
                hrBusinessTripSon.setJourney(journey);
            }
            if (StringUtils.isNotBlank(departCity)) {
                hrBusinessTripSon.setDepartCity(departCity);
            }
            if (StringUtils.isNotBlank(vehicle)) {
                hrBusinessTripSon.setDestinationCity(destinationCity);
            }
            if (StringUtils.isNotBlank(startTime)) {
                hrBusinessTripSon.setStartTime(DateUtil.parse(startTime));
            }
            if (StringUtils.isNotBlank(endTime)) {
                hrBusinessTripSon.setEndTime(DateUtil.parse(endTime));
            }
            if (StringUtils.isNotBlank(totalTimes)) {
                hrBusinessTripSon.setTotalTimes(Double.valueOf(totalTimes));
            }
            hrBusinessTripSonMapper.insertSelective(hrBusinessTripSon);
        }
        return count;
    }

    /**
     * 修改出差申请
     * 
     * @param hrBusinessTrip 出差申请
     * @return 结果
     */
    @Override
    @Transactional
    public int updateHrBusinessTrip(HrBusinessTrip hrBusinessTrip,HttpServletRequest request)
    {
        hrBusinessTrip.setUpdateId(ShiroUtils.getUserId());
        hrBusinessTrip.setUpdateBy(ShiroUtils.getLoginName());
        hrBusinessTrip.setUpdateTime(DateUtils.getNowDate());
        hrBusinessTrip.setApplyTime(DateUtils.getNowDate());
        hrBusinessTripMapper.updateByPrimaryKeySelective(hrBusinessTrip);
        //出差行程数据更新到子表
        //提交的行程数量
        int count = Integer.parseInt(request.getParameter("count"));
        //遍历所有行程
        for (int i = 1; i <= count; i++) {
            HrBusinessTripSon hrBusinessTripSon = new HrBusinessTripSon();
            hrBusinessTripSon.setUpdateId(ShiroUtils.getUserId());
            hrBusinessTripSon.setUpdateBy(ShiroUtils.getLoginName());
            hrBusinessTripSon.setUpdateTime(DateUtils.getNowDate());
            String hrBusinessTripSonId = request.getParameter("hrBusinessTripSon"+i);
            String vehicle = request.getParameter("vehicle" + i);
            String journey = request.getParameter("journey" + i);
            String departCity = request.getParameter("departCity" + i);
            String destinationCity = request.getParameter("destinationCity" + i);
            String startTime = request.getParameter("startTime" + i);
            String endTime = request.getParameter("endTime" + i);
            String totalTimes = request.getParameter("totalTimes" + i);
            if (StringUtils.isNotBlank(hrBusinessTripSonId)) {
                //如果行程ID不为空，则更新原行程
                hrBusinessTripSon.setId(Long.valueOf(hrBusinessTripSonId));
                if (StringUtils.isNotBlank(vehicle)) {
                    hrBusinessTripSon.setVehicle(vehicle);
                }
                if (StringUtils.isNotBlank(journey)) {
                    hrBusinessTripSon.setJourney(journey);
                }
                if (StringUtils.isNotBlank(departCity)) {
                    hrBusinessTripSon.setDepartCity(departCity);
                }
                if (StringUtils.isNotBlank(vehicle)) {
                    hrBusinessTripSon.setDestinationCity(destinationCity);
                }
                if (StringUtils.isNotBlank(startTime)) {
                    hrBusinessTripSon.setStartTime(DateUtil.parse(startTime));
                }
                if (StringUtils.isNotBlank(endTime)) {
                    hrBusinessTripSon.setEndTime(DateUtil.parse(endTime));
                }
                if (StringUtils.isNotBlank(totalTimes)) {
                    hrBusinessTripSon.setTotalTimes(Double.valueOf(totalTimes));
                }
                hrBusinessTripSonMapper.updateByPrimaryKeySelective(hrBusinessTripSon);
            } else {
                //否则插入新行程
                hrBusinessTripSon.setCreateId(ShiroUtils.getUserId());
                hrBusinessTripSon.setCreateBy(ShiroUtils.getLoginName());
                hrBusinessTripSon.setCreateTime(DateUtils.getNowDate());
                hrBusinessTripSon.setParentId(hrBusinessTrip.getId());
                if (StringUtils.isNotBlank(vehicle)) {
                    hrBusinessTripSon.setVehicle(vehicle);
                }
                if (StringUtils.isNotBlank(journey)) {
                    hrBusinessTripSon.setJourney(journey);
                }
                if (StringUtils.isNotBlank(departCity)) {
                    hrBusinessTripSon.setDepartCity(departCity);
                }
                if (StringUtils.isNotBlank(vehicle)) {
                    hrBusinessTripSon.setDestinationCity(destinationCity);
                }
                if (StringUtils.isNotBlank(startTime)) {
                    hrBusinessTripSon.setStartTime(DateUtil.parse(startTime));
                }
                if (StringUtils.isNotBlank(endTime)) {
                    hrBusinessTripSon.setEndTime(DateUtil.parse(endTime));
                }
                if (StringUtils.isNotBlank(totalTimes)) {
                    hrBusinessTripSon.setTotalTimes(Double.valueOf(totalTimes));
                }
                hrBusinessTripSonMapper.insertSelective(hrBusinessTripSon);
            }
        }
        return count;
    }

    /**
     * 删除出差申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrBusinessTripByIds(String ids)
    {
        return hrBusinessTripMapper.deleteHrBusinessTripByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除出差申请信息
     * 
     * @param id 出差申请ID
     * @return 结果
     */
    @Override
    public int deleteHrBusinessTripById(Long id)
    {
        return hrBusinessTripMapper.deleteHrBusinessTripById(id);
    }

    @Override
    @Transactional
    public AjaxResult submitApply(HrBusinessTrip hrBusinessTrip, String applyUserId) {
        hrBusinessTrip.setApplyUser(applyUserId);
        hrBusinessTrip.setApplyTime(DateUtils.getNowDate());
        hrBusinessTrip.setUpdateBy(applyUserId);
        //修改流程状态为审核中
        hrBusinessTrip.setAuditStatus("1");
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrBusinessTrip.getId().toString();
        HashMap<String, Object> variables = new HashMap<>();
        processHandleService.setAssignee(ProcessKey.userDefined01BusinessTrip,variables,hrBusinessTrip.getDeptId(),(JSONObject) JSON.toJSON(hrBusinessTrip));
//        variables.put("deptLeader",userMapper.selectDeptLeaderByLoginName(applyUserId));
        // 建立双向关系
        hrBusinessTrip.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01BusinessTrip,businessKey,applyUserId,hrBusinessTrip.getTitle(),variables).getProcessInstanceId());
        hrBusinessTripMapper.updateByPrimaryKey(hrBusinessTrip);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        //维护业务数据
        Example example = new Example(HrBusinessTrip.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        HrBusinessTrip hrBusinessTrip = hrBusinessTripMapper.selectSingleOneByExample(example);
        hrBusinessTrip.setAuditStatus("4");
        hrBusinessTripMapper.updateByPrimaryKeySelective(hrBusinessTrip);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult complete(HrBusinessTrip hrBusinessTrip, String taskId, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();
        processHandleService.setAssignee(ProcessKey.userDefined01BusinessTrip,variables,hrBusinessTrip.getDeptId(),taskId, (JSONObject)JSON.toJSON(hrBusinessTrip));

        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean approved = BooleanUtils.toBoolean(p_B_approved);
        if(approved){
            //审批通过
            hrBusinessTrip.setAuditStatus("2");
            processHandleService.complete(hrBusinessTrip.getInstanceId(),ProcessKey.userDefined01BusinessTrip,hrBusinessTrip.getTitle(),taskId,variables,
                    hrBusinessTrip.getAuditStatus().equals("2"),comment,BooleanUtils.toBoolean(p_B_approved));
            List<Task> list = taskService.createTaskQuery().processInstanceId(hrBusinessTrip.getInstanceId()).active().list();
            if(list.size() < 1) {
                //流程的活动节点数为0，流程走完，修改流程状态为通过
                hrBusinessTrip.setAuditStatus("2");
                hrBusinessTripMapper.updateByPrimaryKey(hrBusinessTrip);
            }
        } else {
            //页面拒绝流程后，修改流程状态为拒绝
            hrBusinessTrip.setAuditStatus("3");
            hrBusinessTripMapper.updateByPrimaryKey(hrBusinessTrip);
            processHandleService.completeDown(hrBusinessTrip.getInstanceId(),taskId,variables,
                    comment,BooleanUtils.toBoolean(p_B_approved));
        }
        return AjaxResult.success("任务已完成");
    }

    @Override
    @Transactional
    public int insertBusinessTripDTO(BusinessTripDTO businessTripDTO) {
        int i = 0;
        String applyUserNum = businessTripDTO.getApplyUserNum();
        String status = businessTripDTO.getStatus();
        if(StringUtils.isNotBlank(status) && !"已撤销".equals(status) && StringUtils.isNotBlank(applyUserNum)){
            HrEmp hrEmp = hrEmpMapper.selectHrEmpByEmpNum(applyUserNum);
            if(hrEmp != null){
                Long userId = hrEmp.getUserId();
                if(userId != null){
                    HrBusinessTrip hrBusinessTrip = convert(businessTripDTO);
                    SysUser sysUser = userMapper.selectUserById(userId);
                    hrBusinessTrip.setApplyUser(sysUser.getLoginName());
                    hrBusinessTrip.setApplyUserName(sysUser.getUserName());
                    hrBusinessTrip.setCreateId(userId);
                    hrBusinessTrip.setCreateBy(ShiroUtils.getLoginName());
                    hrBusinessTrip.setCreateTime(DateUtils.getNowDate());
                    hrBusinessTrip.setInstanceId("文件导入流程");
                    hrBusinessTrip.setAuditStatus("2");
                    hrBusinessTrip.setDelFlag("0");

                    //往子表插入行程数据
                    HrBusinessTripSon hrBusinessTripSon = convertSon(businessTripDTO);
                    String vehicle = businessTripDTO.getVehicle();
                    String journey = businessTripDTO.getJourney();
                    //交通工具
                    if(vehicle.contains("飞机")){
                        vehicle = "1";
                    }
                    if(vehicle.contains("火车")){
                        vehicle = "2";
                    }
                    if(vehicle.contains("汽车")){
                        vehicle = "3";
                    }
                    if(vehicle.contains("其他")){
                        vehicle = "4";
                    }
                    hrBusinessTripSon.setVehicle(vehicle);
                    //单程往返
                    if("单程".equals(journey)){
                        journey = "1";
                    }
                    if("往返".equals(journey)){
                        journey = "2";
                    }
                    hrBusinessTripSon.setJourney(journey);
                    hrBusinessTripSon.setCreateId(userId);
                    hrBusinessTripSon.setCreateBy(ShiroUtils.getLoginName());
                    hrBusinessTripSon.setCreateTime(DateUtils.getNowDate());

                    Example example = new Example(HrBusinessTrip.class);
                    example.createCriteria().andEqualTo("number", hrBusinessTrip.getNumber());
                    HrBusinessTrip oldBusinessTrip = hrBusinessTripMapper.selectSingleOneByExample(example);
                    //存在则往出差流程子表插入行程，不存在则新增出差流程数据
                    if ( oldBusinessTrip == null) {
                        i = hrBusinessTripMapper.insert(hrBusinessTrip);
                        hrBusinessTripSon.setParentId(hrBusinessTrip.getId());
                        hrBusinessTripSonMapper.insertSelective(hrBusinessTripSon);
                    } else {
                        //如果存在相同的审批编号（钉钉流程编号）则往子表插入行程
                        hrBusinessTrip.setId(oldBusinessTrip.getId());
                        hrBusinessTripMapper.updateByPrimaryKey(hrBusinessTrip);

                        hrBusinessTripSon.setParentId(oldBusinessTrip.getId());
                        Example exampleSon = new Example(HrBusinessTripSon.class);
                        exampleSon.createCriteria().andEqualTo("startTime", hrBusinessTripSon.getStartTime())
                                        .andEqualTo("endTime", hrBusinessTripSon.getEndTime())
                                        .andEqualTo("createId", hrBusinessTripSon.getCreateId());
                        HrBusinessTripSon oldBusinessTripSon = hrBusinessTripSonMapper.selectSingleOneByExample(exampleSon);
                        //如果不存在相同的子行程，插入新的行程数据
                        if(oldBusinessTripSon == null){
                            i = hrBusinessTripSonMapper.insertSelective(hrBusinessTripSon);
                        }else{
                            //如果存在相同的子行程，更新该行程数据
                            hrBusinessTripSon.setId(oldBusinessTripSon.getId());
                            i = hrBusinessTripSonMapper.updateByPrimaryKey(hrBusinessTripSon);
                        }

                    }
                }
            }
        }
        return i;
    }

    @Override
    public List<HrBusinessTrip> selectBusinessTripManageList(HrBusinessTrip businessTrip) {
        List<HrBusinessTrip> list = hrBusinessTripMapper.selectBusinessTripManageList(businessTrip);
        for (HrBusinessTrip hrBusinessTrip: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(hrBusinessTrip.getCreateBy());
            if (sysUser != null) {
                hrBusinessTrip.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(hrBusinessTrip.getApplyUser());
            if (sysUser2 != null) {
                hrBusinessTrip.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(hrBusinessTrip.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(hrBusinessTrip.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    hrBusinessTrip.setTaskId(task.getId());
                    hrBusinessTrip.setTaskName(task.getName());
                    hrBusinessTrip.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    hrBusinessTrip.setTaskName("已办结");
                }
            } else {
                hrBusinessTrip.setTaskName("未启动");
            }
        }
        return list;
    }

    @Override
    public HrBusinessTrip convert(BusinessTripDTO businessTripDTO) {
        HrBusinessTrip hrBusinessTrip = new HrBusinessTrip();
        try {
            BeanUtils.copyProperties(businessTripDTO,hrBusinessTrip);
        } catch (Exception e) {
            logger.error("对象属性拷贝异常");
            e.printStackTrace();
        }
        return hrBusinessTrip;
    }

    public HrBusinessTripSon convertSon(BusinessTripDTO businessTripDTO) {
        HrBusinessTripSon hrBusinessTripSon = new HrBusinessTripSon();
        try {
            BeanUtils.copyProperties(businessTripDTO,hrBusinessTripSon);
        } catch (Exception e) {
            logger.error("对象属性拷贝异常");
            e.printStackTrace();
        }
        return hrBusinessTripSon;
    }
}
