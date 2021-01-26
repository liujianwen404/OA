package com.ruoyi.process.leave.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.process.leave.domain.BizLeaveVo;
import org.activiti.engine.runtime.ProcessInstance;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 请假业务Service接口
 *
 * @author Xianlu Tech
 * @date 2019-10-11
 */
public interface IBizLeaveService {
    /**
     * 查询请假业务
     *
     * @param id 请假业务ID
     * @return 请假业务
     */
    public BizLeaveVo selectBizLeaveById(Long id);

    /**
     * 查询请假业务列表
     *
     * @param BizLeaveVo 请假业务
     * @return 请假业务集合
     */
    public List<BizLeaveVo> selectBizLeaveList(BizLeaveVo BizLeaveVo);

    /**
     * 新增请假业务
     *
     * @param BizLeaveVo 请假业务
     * @return 结果
     */
    public int insertBizLeave(BizLeaveVo BizLeaveVo);

    /**
     * 修改请假业务
     *
     * @param BizLeaveVo 请假业务
     * @return 结果
     */
    public int updateBizLeave(BizLeaveVo BizLeaveVo);

    /**
     * 批量删除请假业务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizLeaveByIds(String ids);

    /**
     * 删除请假业务信息
     *
     * @param id 请假业务ID
     * @return 结果
     */
    public int deleteBizLeaveById(Long id);

    /**
     * 启动流程
     * @param entity
     * @param applyUserId
     * @return
     */
    ProcessInstance submitApply(BizLeaveVo entity, String applyUserId);

    /**
     * 查询我的待办列表
     * @param userId
     * @return
     */
    List<BizLeaveVo> findTodoTasks(BizLeaveVo leave, String userId);

    /**
     * 完成任务
     * @param leave
     * @param saveEntity
     * @param taskId
     * @param variables
     */
    void complete(BizLeaveVo leave, Boolean saveEntity, String taskId, Map<String, Object> variables);

    List<BizLeaveVo> findDoneTasks(BizLeaveVo BizLeaveVo, String userId);

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);
}
