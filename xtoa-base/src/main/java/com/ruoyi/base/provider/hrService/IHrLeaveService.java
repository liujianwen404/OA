package com.ruoyi.base.provider.hrService;

import com.ruoyi.base.domain.DTO.DateOperation;
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.HrLeave;
import com.ruoyi.base.domain.DTO.HrLeaveDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 请假业务Service接口
 *
 * @author Xianlu Tech
 * @date 2019-10-11
 */
public interface IHrLeaveService {
    /**
     * 查询请假业务
     *
     * @param id 请假业务ID
     * @return 请假业务
     */
    public HrLeave selectHrLeaveById(Long id);

    /**
     * 查询请假业务列表
     *
     * @param hrLeave 请假业务
     * @return 请假业务集合
     */
    public List<HrLeave> selectHrLeaveViewList(HrLeave hrLeave);
    public List<HrLeave> selectHrLeaveViewListExcel(HrLeave hrLeave);
    public List<HrLeave> selectHrLeaveList(HrLeave hrLeave);

    /**
     * 新增请假业务
     *
     * @param hrLeave 请假业务
     * @return 结果
     */
    public int insertHrLeave(HrLeave hrLeave);

    public int insertHrLeaveDTO(HrLeaveDTO hrLeaveDTO);

    /**
     * 修改请假业务
     *
     * @param hrLeave 请假业务
     * @return 结果
     */
    public int updateHrLeave(HrLeave hrLeave);

    /**
     * 批量删除请假业务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrLeaveByIds(String ids);

    /**
     * 删除请假业务信息
     *
     * @param id 请假业务ID
     * @return 结果
     */
    public int deleteHrLeaveById(Long id);

    /**
     * 启动流程
     * @param entity
     * @param applyUserId
     * @return
     */
    AjaxResult submitApply(HrLeave entity, String applyUserId);

    /**
     * 查询我的待办列表
     * @param userId
     * @return
     */
    List<HrLeave> findTodoTasks(HrLeave leave, String userId);

    /**
     * 审批流程
     * @param hrLeave
     * @param taskId
     * @param request
     */
    AjaxResult complete(HrLeave hrLeave, String taskId, HttpServletRequest request);
    void saveLeaveInfo(HrLeave hrLeave, DateOperation dateOperation);

    /**
     * 通过日期获取班次的各个时间
     * @param offsetDay 那一天
     * @param attendanceClass 哪一个班次数据
     * @param empId 谁
     */
    DateOperation getDateOperation(Date offsetDay, HrAttendanceClass attendanceClass, Long empId);

    List<HrLeave> findDoneTasks(HrLeave hrLeave, String userId);

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);

    AjaxResult getLeaveTime(HttpServletRequest request);

    AjaxResult editDelImg(String img, Long id);

    Double selectLeaveByType(Long empId, int year, int month, String type);

    Double attendanceGroupHandle(String startStr , String endStr,Long userId);
}
