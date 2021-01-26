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
import com.ruoyi.base.domain.HrRegular;
import com.ruoyi.hr.manager.ProcessManager;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.mapper.HrRegularMapper;
import com.ruoyi.base.provider.hrService.IHrRegularService;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.process.todoitem.mapper.BizTodoItemMapper;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
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
import java.util.stream.Collectors;

/**
 * 离职申请Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-05-15
 */
@Service
@org.apache.dubbo.config.annotation.Service
public class HrRegularServiceImpl implements IHrRegularService
{

    private static final Logger logger = LoggerFactory.getLogger(HrRegularServiceImpl.class);

    @Resource
    private BizTodoItemMapper bizTodoItemMapper;

    @Resource
    private HrEmpMapper hrEmpMapper;

    @Resource
    private HrRegularMapper hrRegularMapper;

    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IBizTodoItemService bizTodoItemService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Resource
    private ISysUserService userService;
    
    @Autowired
    private ProcessManager processManager;
    


    /**
     * 查询申请
     * 
     * @param id 申请ID
     */
    @Override
    public HrRegular selectHrRegularById(Long id)
    {
        HrRegular hrRegular = hrRegularMapper.selectByPrimaryKey(id);
        SysUser sysUser = userMapper.selectUserByLoginName(hrRegular.getApplyUser());
        if (sysUser != null) {
            hrRegular.setApplyUserName(sysUser.getUserName());
        }
        return hrRegular;
    }

    /**
     * 查询申请列表
     */
    @Override
    public List<HrRegular> selectHrRegularList(HrRegular regular)
    {
        List<HrRegular> list = hrRegularMapper.selectHrRegularList(regular);
        for (HrRegular hrRegular: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(hrRegular.getCreateBy());
            if (sysUser != null) {
                hrRegular.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(hrRegular.getApplyUser());
            if (sysUser2 != null) {
                hrRegular.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(hrRegular.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(hrRegular.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    hrRegular.setTaskId(task.getId());
                    hrRegular.setTaskName(task.getName());
                    hrRegular.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    hrRegular.setTaskName("已办结");
                }
            } else {
                hrRegular.setTaskName("未启动");
            }
        }
        return list;
    }

    /**
     * 新增申请
     */
    @Override
    public int insertHrRegular(HrRegular hrRegular)
    {
        hrRegular.setApplyUser(ShiroUtils.getLoginName());
        hrRegular.setApplyUserName(ShiroUtils.getUserName());
        hrRegular.setApplyTime(DateUtils.getNowDate());
        hrRegular.setCreateId(ShiroUtils.getUserId());
        hrRegular.setEmpId(ShiroUtils.getUserId());
        hrRegular.setCreateBy(ShiroUtils.getLoginName());
        hrRegular.setCreateTime(DateUtils.getNowDate());

        if (StringUtil.isNotEmpty(hrRegular.getAttachment())){
            String[] split = hrRegular.getAttachment().split(",");

            split = ArrayUtil.filter(split, (Filter<String>) s -> {
                if (StringUtil.isEmpty(s)){
                    return false;
                }
                return true;
            });

            String join = ArrayUtil.join(split, ",");
            hrRegular.setAttachment(join);
        }else {
            hrRegular.setAttachment(null);
        }

        return hrRegularMapper.insertSelective(hrRegular);
    }

    /**
     * 修改申请
     */
    @Override
    public int updateHrRegular(HrRegular hrRegular)
    {
        hrRegular.setUpdateId(ShiroUtils.getUserId());
        hrRegular.setUpdateBy(ShiroUtils.getLoginName());
        hrRegular.setUpdateTime(DateUtils.getNowDate());

        if (StringUtil.isNotEmpty(hrRegular.getAttachment())){
            String[] split = hrRegular.getAttachment().split(",");

            split = ArrayUtil.filter(split, (Filter<String>) s -> {
                if (StringUtil.isEmpty(s)){
                    return false;
                }
                return true;
            });

            String join = ArrayUtil.join(split, ",");
            hrRegular.setAttachment(join);
        }else {
            hrRegular.setAttachment(null);
        }

        return hrRegularMapper.updateByPrimaryKeySelective(hrRegular);
    }

    /**
     * 删除申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrRegularByIds(String ids)
    {
        return hrRegularMapper.deleteHrRegularByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除离职申请信息
     * 
     * @param id 离职申请ID
     * @return 结果
     */
    @Override
    public int deleteHrRegularById(Long id)
    {
        return hrRegularMapper.deleteHrRegularById(id);
    }

    /**
     * 启动流程
     * @param hrRegular
     * @return
     */
    @Override
    @Transactional
    public AjaxResult submitApply(HrRegular hrRegular) {
        Long userId = hrRegular.getCreateId();
        SysUser sysUser = userService.selectUserById(userId);
        String loginName = sysUser.getLoginName();

        hrRegular.setApplyUser(loginName);
        hrRegular.setApplyTime(DateUtils.getNowDate());
        hrRegular.setUpdateBy(loginName);
        //修改流程状态为审核中
        hrRegular.setAuditStatus("1");
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrRegular.getId().toString();
        Map<String, Object> variables = new HashMap<>();

        List deptLeaderLoginNames = processManager.getDeptLeaderLoginNames(userId,3);
        List directLeaderLoginNames = processManager.getDeptLeaderLoginNames(userId,1);
        int companyNum = processManager.getCompany(userId);
        int firstDeptNum = processManager.getFirstDept(userId);
        boolean isEast = processManager.isEast(userId);

        variables.put("isEast",isEast);
        variables.put("directLeader",directLeaderLoginNames);
        variables.put("deptLeaders",deptLeaderLoginNames);
        variables.put("companyNum",companyNum);
        variables.put("firstDeptNum",firstDeptNum);

        // 启动流程时设置业务 key
        // 建立双向关系
        hrRegular.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01Regular,businessKey,loginName,hrRegular.getTitle(),variables).getProcessInstanceId());
        hrRegularMapper.updateByPrimaryKey(hrRegular);
        return AjaxResult.success();
    }

    /**
     * 审批流程
     * @param hrRegular
     * @param taskId
     * @param request
     */
    @Override
    @Transactional
    public AjaxResult complete(HrRegular hrRegular, String taskId, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();
        hrRegular = hrRegularMapper.selectByPrimaryKey(hrRegular.getId());

        processHandleService.setAssignee(ProcessKey.userDefined01Regular,variables,hrRegular.getDeptId(),taskId, (JSONObject)JSON.toJSON(hrRegular));

        // 批注
        String comment = request.getParameter("comment");
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean approved = BooleanUtils.toBoolean(p_B_approved);

        if (approved){
            //审批通过
            hrRegular.setAuditStatus("2");

            processHandleService.complete(hrRegular.getInstanceId(),ProcessKey.userDefined01Regular,hrRegular.getTitle(),taskId,variables,
                    hrRegular.getAuditStatus().equals("2"),comment,BooleanUtils.toBoolean(p_B_approved));

            List<Task> list = taskService.createTaskQuery().processInstanceId(hrRegular.getInstanceId()).active().list();
            if (list.size() < 1){
                //流程结束了跟新下数据状态
                hrRegularMapper.updateByPrimaryKey(hrRegular);

                HrEmp hrEmp = new HrEmp();
                hrEmp.setEmpId(hrRegular.getCreateId());
                hrEmp.setEmpStatus(1);
                hrEmp.setPositiveDate(hrRegular.getRegularTime());
                hrEmpMapper.updateTHrEmp(hrEmp);

            }
        }else {
            //审批拒绝
            hrRegular.setAuditStatus("3");
            hrRegularMapper.updateByPrimaryKey(hrRegular);

            processHandleService.completeDown(hrRegular.getInstanceId(),taskId,variables,
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
        HrRegular hrRegular = hrRegularMapper.selectSingleOneByExample(example);
        hrRegular.setAuditStatus("4");
        hrRegularMapper.updateByPrimaryKeySelective(hrRegular);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }

    @Override
    public List<HrRegular> selectRegularManageList(HrRegular regular) {
        List<HrRegular> list = hrRegularMapper.selectRegularManageList(regular);
        for (HrRegular hrRegular: list) {
            SysUser sysUser = userMapper.selectUserByLoginName(hrRegular.getCreateBy());
            if (sysUser != null) {
                hrRegular.setCreateUserName(sysUser.getUserName());
            }
            SysUser sysUser2 = userMapper.selectUserByLoginName(hrRegular.getApplyUser());
            if (sysUser2 != null) {
                hrRegular.setApplyUserName(sysUser2.getUserName());
            }
            // 当前环节
            if (StringUtils.isNotBlank(hrRegular.getInstanceId())) {
                List<Task> taskList = taskService.createTaskQuery()
                        .processInstanceId(hrRegular.getInstanceId())
//                        .singleResult();
                        .list();    // 例如请假会签，会同时拥有多个任务
                if (!CollectionUtils.isEmpty(taskList)) {
                    Task task = taskList.get(0);
                    hrRegular.setTaskId(task.getId());
                    hrRegular.setTaskName(task.getName());
                    hrRegular.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                } else {
                    hrRegular.setTaskName("已办结");
                }
            } else {
                hrRegular.setTaskName("未启动");
            }
        }
        return list;
    }
}
