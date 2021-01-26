package com.ruoyi.hr.service.impl;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.HrQuit;
import com.ruoyi.hr.manager.ProcessManager;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.mapper.HrQuitMapper;
import com.ruoyi.base.provider.hrService.IHrQuitService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.process.todoitem.mapper.BizTodoItemMapper;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 离职申请Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-05-15
 */
@Service
@org.apache.dubbo.config.annotation.Service
public class HrQuitServiceImpl implements IHrQuitService
{

    private static final Logger logger = LoggerFactory.getLogger(HrQuitServiceImpl.class);

    @Resource
    private BizTodoItemMapper bizTodoItemMapper;

    @Resource
    private HrEmpMapper hrEmpMapper;

    @Resource
    private HrQuitMapper hrQuitMapper;

    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Resource
    private ISysUserService userService;

    @Autowired
    private ProcessManager processManager;

    /**
     * 查询离职申请
     * 
     * @param id 离职申请ID
     * @return 离职申请
     */
    @Override
    public HrQuit selectHrQuitById(Long id)
    {
        HrQuit hrQuit = hrQuitMapper.selectByPrimaryKey(id);
        SysUser sysUser = userMapper.selectUserByLoginName(hrQuit.getApplyUser());
        if (sysUser != null) {
            hrQuit.setApplyUserName(sysUser.getUserName());
        }
        return hrQuit;
    }

    /**
     * 查询离职申请列表
     * 
     * @param quit 离职申请
     * @return 离职申请
     */
    @Override
    public List<HrQuit> selectHrQuitList(HrQuit quit)
    {
        List<HrQuit> list = hrQuitMapper.selectHrQuitList(quit);
        for (HrQuit hrQuit: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(hrQuit.getCreateBy());
            if (sysUser != null) {
                hrQuit.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(hrQuit.getApplyUser());
            if (sysUser2 != null) {
                hrQuit.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(hrQuit.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(hrQuit.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    hrQuit.setTaskId(task.getId());
                    hrQuit.setTaskName(task.getName());
                    hrQuit.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    hrQuit.setTaskName("已办结");
                }
            } else {
                hrQuit.setTaskName("未启动");
            }
        }
        return list;
    }

    /**
     * 新增离职申请
     * 
     * @param hrQuit 离职申请
     * @return 结果
     */
    @Override
    public int insertHrQuit(HrQuit hrQuit)
    {
        hrQuit.setApplyUser(ShiroUtils.getLoginName());
        hrQuit.setApplyUserName(ShiroUtils.getUserName());
        hrQuit.setApplyTime(DateUtils.getNowDate());
        hrQuit.setEmpId(ShiroUtils.getUserId());
        hrQuit.setCreateId(ShiroUtils.getUserId());
        hrQuit.setCreateBy(ShiroUtils.getLoginName());
        hrQuit.setCreateTime(DateUtils.getNowDate());

        if (StringUtil.isNotEmpty(hrQuit.getAttachment())){
            String[] split = hrQuit.getAttachment().split(",");

            split = ArrayUtil.filter(split, (Filter<String>) s -> {
                if (StringUtil.isEmpty(s)){
                    return false;
                }
                return true;
            });

            String join = ArrayUtil.join(split, ",");
            hrQuit.setAttachment(join);
        }

        return hrQuitMapper.insertSelective(hrQuit);
    }

    /**
     * 修改离职申请
     * 
     * @param hrQuit 离职申请
     * @return 结果
     */
    @Override
    public int updateHrQuit(HrQuit hrQuit)
    {
        hrQuit.setUpdateId(ShiroUtils.getUserId());
        hrQuit.setUpdateBy(ShiroUtils.getLoginName());
        hrQuit.setUpdateTime(DateUtils.getNowDate());

        return hrQuitMapper.updateByPrimaryKeySelective(hrQuit);
    }

    /**
     * 删除离职申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrQuitByIds(String ids)
    {
        return hrQuitMapper.deleteHrQuitByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除离职申请信息
     * 
     * @param id 离职申请ID
     * @return 结果
     */
    @Override
    public int deleteHrQuitById(Long id)
    {
        return hrQuitMapper.deleteHrQuitById(id);
    }

    /**
     * 启动流程
     * @param hrQuit
     * @return
     */
    @Override
    @Transactional
    public AjaxResult submitApply(HrQuit hrQuit) {
        Long userId = hrQuit.getCreateId();
        SysUser sysUser = userService.selectUserById(userId);
        String loginName = sysUser.getLoginName();

        hrQuit.setApplyUser(loginName);
        hrQuit.setApplyTime(DateUtils.getNowDate());
        hrQuit.setUpdateBy(loginName);
        //修改流程状态为审核中
        hrQuit.setAuditStatus("1");
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrQuit.getId().toString();
        HashMap<String, Object> variables = new HashMap<>();

        List deptLeaderLoginNames = processManager.getDeptLeaderLoginNames(userId,3);
        List directLeaderLoginNames = processManager.getDeptLeaderLoginNames(userId,1);
        boolean isLeader = processManager.isDeptLeader(userId);
        int companyNum = processManager.getCompany(userId);
        int firstDeptNum = processManager.getFirstDept(userId);
        boolean isEast = processManager.isEast(userId);

        variables.put("isEast",isEast);
        variables.put("rank",hrQuit.getRank());
        variables.put("isLeader",isLeader);
        variables.put("directLeader",directLeaderLoginNames);
        variables.put("deptLeaders",deptLeaderLoginNames);
        variables.put("companyNum",companyNum);
        variables.put("firstDeptNum",firstDeptNum);

        // 建立双向关系
        hrQuit.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01Quit,businessKey,loginName,hrQuit.getTitle(),variables).getProcessInstanceId());
        hrQuitMapper.updateByPrimaryKey(hrQuit);
        return AjaxResult.success();
    }

    /**
     * 审批流程
     * @param hrQuit
     * @param taskId
     * @param request
     */
    @Transactional
    public AjaxResult complete(HrQuit hrQuit, String taskId, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();


        processHandleService.setAssignee(ProcessKey.userDefined01Quit,variables,hrQuit.getDeptId(),taskId, (JSONObject)JSON.toJSON(hrQuit));


        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean toBoolean = BooleanUtils.toBoolean(p_B_approved);

        if (toBoolean){
            //审批通过
            hrQuit.setAuditStatus("2");

            processHandleService.complete(hrQuit.getInstanceId(),ProcessKey.userDefined01Quit,hrQuit.getTitle(),taskId,variables,
                    hrQuit.getAuditStatus().equals("2"),comment,BooleanUtils.toBoolean(p_B_approved));

            List<Task> list = taskService.createTaskQuery().processInstanceId(hrQuit.getInstanceId()).active().list();
            if (list.size() < 1){
                //流程结束了跟新下数据状态
                //流程走完，修改员工状态为离职状态
                HrEmp hrEmp = new HrEmp();
                hrEmp.setEmpId(hrQuit.getCreateId());
                hrEmp.setEmpStatus(4);
                hrEmp.setQuitDate(hrQuit.getQuitTime());
                hrEmp.setIsQuit("1");
                hrEmp.setQuitDate(hrQuit.getQuitTime());
                hrEmp.setQuitReason(hrQuit.getReason());
                hrEmpMapper.updateTHrEmp(hrEmp);

                hrQuitMapper.updateByPrimaryKey(hrQuit);

            }
        }else {
            //审批拒绝
            hrQuit.setAuditStatus("3");
            hrQuitMapper.updateByPrimaryKey(hrQuit);

            processHandleService.completeDown(hrQuit.getInstanceId(),taskId,variables,
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
        HrQuit hrQuit = hrQuitMapper.selectSingleOneByExample(example);
        hrQuit.setAuditStatus("4");
        hrQuitMapper.updateByPrimaryKeySelective(hrQuit);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    @Override
    public List<HrQuit> selectQuitManageList(HrQuit quit) {
        List<HrQuit> list = hrQuitMapper.selectQuitManageList(quit);
        for (HrQuit hrQuit: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(hrQuit.getCreateBy());
            if (sysUser != null) {
                hrQuit.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(hrQuit.getApplyUser());
            if (sysUser2 != null) {
                hrQuit.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(hrQuit.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(hrQuit.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    hrQuit.setTaskId(task.getId());
                    hrQuit.setTaskName(task.getName());
                    hrQuit.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    hrQuit.setTaskName("已办结");
                }
            } else {
                hrQuit.setTaskName("未启动");
            }
        }
        return list;
    }
}
