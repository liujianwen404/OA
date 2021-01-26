package com.ruoyi.hr.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.base.domain.HrOff;
import com.ruoyi.base.domain.HrQuit;
import com.ruoyi.base.provider.hrService.IHrOffService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ProcessKey;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrOffMapper;
import com.ruoyi.hr.service.ProcessHandleService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * off流程Service业务层处理
 * 
 * @author xt
 * @date 2020-07-28
 */
@Service
@org.springframework.stereotype.Service
public class HrOffServiceImpl implements IHrOffService 
{

    @Resource
    private HrOffMapper hrOffMapper;
    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessHandleService processHandleService;

    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysPostService sysPostService;

    /**
     * 查询off流程
     * 
     * @param id off流程ID
     * @return off流程
     */
    @Override
    public HrOff selectHrOffById(Long id)
    {
        return hrOffMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询off流程列表
     * 
     * @param hrOff off流程
     * @return off流程
     */
    @Override
    public List<HrOff> selectHrOffList(HrOff hrOff)
    {
        hrOff.setDelFlag("0");
        List<HrOff> hrOffs = hrOffMapper.selectHrOffList(hrOff);
        hrOffs.forEach(offer -> {
            // 当前环节
            setInfo(offer);
        });
        return hrOffs;
    }
    /**
     * 查询off流程列表
     *
     * @param hrOff off流程
     * @return off流程
     */
    @Override
    public List<HrOff> selectHrOffListManage(HrOff hrOff)
    {
        hrOff.setDelFlag("0");
        List<HrOff> hrOffs = hrOffMapper.selectHrOffListManage(hrOff);
        hrOffs.forEach(offer -> {
            // 当前环节
            setInfo(offer);
        });
        return hrOffs;
    }

    private void setInfo(HrOff offer) {
        if (StringUtils.isNotBlank(offer.getInstanceId())) {
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(offer.getInstanceId())
//                        .singleResult();
                    .list();    // 例如请假会签，会同时拥有多个任务
            if (!CollectionUtils.isEmpty(taskList)) {
                Task task = taskList.get(0);
                offer.setTaskId(task.getId());
                offer.setTaskName(task.getName());
                if (taskList.size() > 1){
                    StringBuilder names = new StringBuilder();
                    taskList.stream().map(task1 -> processHandleService.selectToUserNameByAssignee(task1)).collect(Collectors.toSet()).forEach(name -> names.append(name+" "));
                    offer.setTodoUserName(names.toString());

                }else {
                    offer.setTodoUserName(processHandleService.selectToUserNameByAssignee(task));
                }
            } else {
                offer.setTaskName("已办结");
            }
        } else {
            offer.setTaskName("未启动");
        }
        offer.setTodoUserNameExcel(offer.getTodoUserName());
        Integer auditStatus = offer.getAuditStatus();
        switch (auditStatus){
            case 0:
                offer.setAuditStatusExcel("待提交");
                break;
            case 1:
                offer.setAuditStatusExcel("审核中");
                break;
            case 2:
                offer.setAuditStatusExcel("通过");
                break;
            case 3:
                offer.setAuditStatusExcel("拒绝");
                break;
            case 4:
                offer.setAuditStatusExcel("撤销");
                break;
        }
        SysDept sysDept = sysDeptService.selectDeptById(offer.getDeptId());
        SysPost sysPost = sysPostService.selectPostById(offer.getPostId());

        offer.setDeptName(sysDept == null ? "" : sysDept.getShowName());
        offer.setPostName(sysPost == null ? "" : sysPost.getPostName());


    }

    /**
     * 新增off流程
     * 
     * @param hrOff off流程
     * @return 结果
     */
    @Override
    public int insertHrOff(HrOff hrOff)
    {
        hrOff.setCreateId(ShiroUtils.getUserId());
        hrOff.setCreateBy(ShiroUtils.getLoginName());
        hrOff.setCreateTime(DateUtils.getNowDate());
        return hrOffMapper.insertSelective(hrOff);
    }

    /**
     * 修改off流程
     * 
     * @param hrOff off流程
     * @return 结果
     */
    @Override
    public int updateHrOff(HrOff hrOff)
    {
        hrOff.setUpdateId(ShiroUtils.getUserId());
        hrOff.setUpdateBy(ShiroUtils.getLoginName());
        hrOff.setUpdateTime(DateUtils.getNowDate());
        return hrOffMapper.updateByPrimaryKeySelective(hrOff);
    }

    /**
     * 删除off流程对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrOffByIds(String ids)
    {
        return hrOffMapper.deleteHrOffByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除off流程信息
     * 
     * @param id off流程ID
     * @return 结果
     */
    @Override
    public int deleteHrOffById(Long id)
    {
        return hrOffMapper.deleteHrOffById(id);
    }

    @Override
    @Transactional
    public AjaxResult submitApply(HrOff hrOff, String applyUserId) {
        hrOff.setUpdateBy(applyUserId);
        //修改流程状态为审核中
        hrOff.setAuditStatus(1);
        hrOff.setApplyUser(ShiroUtils.getLoginName());
        hrOff.setApplyUserName(ShiroUtils.getUserName());
        hrOff.setApplyTime(new Date());
        // 实体类 ID，作为流程的业务 key
        String businessKey = hrOff.getId().toString();

        Map<String,Object> values = new HashMap<>();
        processHandleService.setAssignee(ProcessKey.userDefined01Offer,values,hrOff.getDeptId(),(JSONObject) JSON.toJSON(hrOff));

        // 建立双向关系
        hrOff.setInstanceId(processHandleService.submitApply(ProcessKey.userDefined01Offer,businessKey,applyUserId,hrOff.getToOffName()+"offer流程",values).getProcessInstanceId());
        hrOffMapper.updateByPrimaryKey(hrOff);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult complete(HrOff hrOff, String taskId, HttpServletRequest request) {
        Map<String, Object> variables = new HashMap<String, Object>();
        hrOff = hrOffMapper.selectByPrimaryKey(hrOff.getId());
        processHandleService.setAssignee(ProcessKey.userDefined01Offer,variables,hrOff.getDeptId(),taskId, (JSONObject)JSON.toJSON(hrOff));
        // 审批意见(true或者是false)
        String p_B_approved = request.getParameter("p_B_approved");
        boolean toBoolean = BooleanUtils.toBoolean(p_B_approved);
        // 批注
        String comment = request.getParameter("comment");
        if (toBoolean){
            hrOff.setAuditStatus(2);
            //审批通过
            processHandleService.complete(hrOff.getInstanceId(),ProcessKey.userDefined01Offer,hrOff.getToOffName()+"offer流程",taskId,variables,
                    hrOff.getAuditStatus().equals(2),comment,BooleanUtils.toBoolean(p_B_approved));

            List<Task> list = taskService.createTaskQuery().processInstanceId(hrOff.getInstanceId()).active().list();
            if (list.size() < 1){
                //流程结束了跟新下数据状态
                hrOffMapper.updateByPrimaryKeySelective(hrOff);
            }
        }else {
            hrOff.setAuditStatus(3);
            hrOffMapper.updateByPrimaryKeySelective(hrOff);
            //审批拒绝
            processHandleService.completeDown(hrOff.getInstanceId(),taskId,variables,comment,BooleanUtils.toBoolean(p_B_approved));
        }

        return AjaxResult.success("任务已完成");
    }

    @Override
    @Transactional
    public AjaxResult repeal(String instanceId, HttpServletRequest request, String message) {
        //维护业务数据
        Example example = new Example(HrQuit.class);
        example.createCriteria().andEqualTo("instanceId",instanceId);
        HrOff hrOff = hrOffMapper.selectSingleOneByExample(example);
        hrOff.setAuditStatus(4);
        hrOffMapper.updateByPrimaryKeySelective(hrOff);

        //撤销操作
        processHandleService.repeal(instanceId,request,message);
        return AjaxResult.success();
    }
}
