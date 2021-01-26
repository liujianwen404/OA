package com.ruoyi.hr.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.base.domain.DTO.GooutDTO;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.hr.manager.IHrDomainConvert;
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
import com.ruoyi.hr.mapper.HrGooutMapper;
import com.ruoyi.base.domain.HrGoout;
import com.ruoyi.hr.service.IHrGooutService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 外出申请Service业务层处理
 *
 * @author liujianwen
 * @date 2020-07-06
 */
@Service
public class HrGooutServiceImpl implements IHrGooutService, IHrDomainConvert<GooutDTO,HrGoout>
{

    private static final Logger logger = LoggerFactory.getLogger(HrGooutServiceImpl.class);

    @Resource
    private HrGooutMapper hrGooutMapper;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private HrEmpMapper hrEmpMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessHandleService processHandleService;

    /**
     * 查询外出申请
     *
     * @param id 外出申请ID
     * @return 外出申请
     */
    @Override
    public HrGoout selectHrGooutById(Long id)
    {
        return hrGooutMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询外出申请列表
     *
     * @param hrGoout 外出申请
     * @return 外出申请
     */
    @Override
    public List<HrGoout> selectHrGooutList(HrGoout hrGoout)
    {
        List<HrGoout> list = hrGooutMapper.selectHrGooutList(hrGoout);
        for (HrGoout goout: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(goout.getCreateBy());
            if (sysUser != null) {
                goout.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(goout.getApplyUser());
            if (sysUser2 != null) {
                goout.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(goout.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(goout.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    goout.setTaskId(task.getId());
                    goout.setTaskName(task.getName());
                    goout.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    goout.setTaskName("已办结");
                }
            } else {
                goout.setTaskName("未启动");
            }
        }
        return list;
    }

    /**
     * 新增外出申请
     *
     * @param hrGoout 外出申请
     * @return 结果
     */
    @Override
    public int insertHrGoout(HrGoout hrGoout)
    {
        hrGoout.setApplyUser(ShiroUtils.getLoginName());
        hrGoout.setApplyUserName(ShiroUtils.getUserName());
        hrGoout.setApplyTime(DateUtils.getNowDate());
        hrGoout.setCreateId(ShiroUtils.getUserId());
        hrGoout.setCreateBy(ShiroUtils.getLoginName());
        hrGoout.setCreateTime(DateUtils.getNowDate());
        return hrGooutMapper.insertSelective(hrGoout);
    }

    /**
     * 修改外出申请
     *
     * @param hrGoout 外出申请
     * @return 结果
     */
    @Override
    public int updateHrGoout(HrGoout hrGoout)
    {
        hrGoout.setUpdateId(ShiroUtils.getUserId());
        hrGoout.setUpdateBy(ShiroUtils.getLoginName());
        hrGoout.setUpdateTime(DateUtils.getNowDate());
        return hrGooutMapper.updateByPrimaryKeySelective(hrGoout);
    }

    /**
     * 删除外出申请对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrGooutByIds(String ids)
    {
        return hrGooutMapper.deleteHrGooutByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除外出申请信息
     *
     * @param id 外出申请ID
     * @return 结果
     */
    @Override
    public int deleteHrGooutById(Long id)
    {
        return hrGooutMapper.deleteHrGooutById(id);
    }

    @Override
    @Transactional
    public AjaxResult submitApply(HrGoout hrGoout, String applyUserId) {
        hrGoout.setApplyUser(applyUserId);
        hrGoout.setApplyTime(DateUtils.getNowDate());
        hrGoout.setUpdateBy(applyUserId);
        //修改流程状态为审核中
        hrGoout.setAuditStatus("1");
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrGoout.getId().toString();
        HashMap<String, Object> variables = new HashMap<>();
        processHandleService.setAssignee(ProcessKey.userDefined01Goout,variables,hrGoout.getDeptId(),(JSONObject) JSON.toJSON(hrGoout));
        // 建立双向关系
        hrGoout.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01Goout,businessKey,applyUserId,hrGoout.getTitle(),variables).getProcessInstanceId());
        hrGooutMapper.updateByPrimaryKey(hrGoout);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        //维护业务数据
        Example example = new Example(HrGoout.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        HrGoout hrGoout = hrGooutMapper.selectSingleOneByExample(example);
        hrGoout.setAuditStatus("4");
        hrGooutMapper.updateByPrimaryKeySelective(hrGoout);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult complete(HrGoout hrGoout, String taskId, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();
        processHandleService.setAssignee(ProcessKey.userDefined01Goout,variables,hrGoout.getDeptId(),taskId, (JSONObject)JSON.toJSON(hrGoout));

        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean approved = BooleanUtils.toBoolean(p_B_approved);
        if(approved){
            //审批通过
            hrGoout.setAuditStatus("2");
            processHandleService.complete(hrGoout.getInstanceId(),ProcessKey.userDefined01Goout,hrGoout.getTitle(),taskId,variables,
                    hrGoout.getAuditStatus().equals("2"),comment, approved);
            List<Task> list = taskService.createTaskQuery().processInstanceId(hrGoout.getInstanceId()).active().list();
            if(list.size() < 1) {
                //流程的活动节点数为0，流程走完，修改流程状态为通过
                hrGoout.setAuditStatus("2");
                hrGooutMapper.updateByPrimaryKey(hrGoout);
            }
        } else {
            //页面拒绝流程后，修改流程状态为拒绝
            hrGoout.setAuditStatus("3");
            hrGooutMapper.updateByPrimaryKey(hrGoout);
            processHandleService.completeDown(hrGoout.getInstanceId(),taskId,variables,
                    comment, approved);
        }
        return AjaxResult.success("任务已完成");
    }

    @Override
    public int insertGooutDTO(GooutDTO gooutDTO) {
        int i = 0;
        String applyUserNum = gooutDTO.getApplyUserNum();
        String status = gooutDTO.getStatus();
        if(StringUtils.isNotBlank(status) && !"已撤销".equals(status) && StringUtils.isNotBlank(applyUserNum)){
            HrEmp hrEmp = hrEmpMapper.selectHrEmpByEmpNum(applyUserNum);
            if(hrEmp != null){
                Long userId = hrEmp.getUserId();
                if(userId != null){
                    HrGoout hrGoout = convert(gooutDTO);
                    SysUser sysUser = userMapper.selectUserById(userId);
                    hrGoout.setApplyUser(sysUser.getLoginName());
                    hrGoout.setApplyUserName(sysUser.getUserName());
                    hrGoout.setCreateBy(ShiroUtils.getLoginName());
                    hrGoout.setCreateTime(DateUtils.getNowDate());
                    hrGoout.setInstanceId("文件导入流程");
                    hrGoout.setAuditStatus("2");
                    hrGoout.setDelFlag("0");

                    //存在则更新，不存在则新增
                    Example example = new Example(HrGoout.class);
                    example.createCriteria().andEqualTo("startTime", hrGoout.getStartTime())
                                                                        .andEqualTo("endTime", hrGoout.getEndTime())
                                                                        .andEqualTo("totalTimes", hrGoout.getTotalTimes());
                    HrGoout oldGoout = hrGooutMapper.selectSingleOneByExample(example);
                    if ( oldGoout == null) {
                        i = hrGooutMapper.insert(hrGoout);
                    } else {
                        hrGoout.setId(oldGoout.getId());
                        i = hrGooutMapper.updateByPrimaryKey(hrGoout);
                    }
                }
            }
        }
        return i;
    }

    @Override
    public List<HrGoout> selectGooutManageList(HrGoout hrGoout) {
        List<HrGoout> list = hrGooutMapper.selectGooutManageList(hrGoout);
        for (HrGoout goout: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(goout.getCreateBy());
            if (sysUser != null) {
                goout.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(goout.getApplyUser());
            if (sysUser2 != null) {
                goout.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(goout.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(goout.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    goout.setTaskId(task.getId());
                    goout.setTaskName(task.getName());
                    goout.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    goout.setTaskName("已办结");
                }
            } else {
                goout.setTaskName("未启动");
            }
        }
        return list;
    }

    /**
     * 将数据接受对象的属性拷贝到对应的实体类
     * */
    @Override
    public HrGoout convert(GooutDTO gooutDTO) {
        HrGoout hrGoout = new HrGoout();
        try {
            BeanUtils.copyProperties(gooutDTO,hrGoout);
        } catch (Exception e) {
            logger.error("对象属性拷贝异常");
            e.printStackTrace();
        }
        return hrGoout;
    }
}
