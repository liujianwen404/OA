//package com.ruoyi.process.hr.service;
//
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.process.hr.domain.HrQuit;
//import com.ruoyi.process.leave.domain.BizLeaveVo;
//import org.activiti.engine.runtime.ProcessInstance;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.Map;
//
///**
// * 离职申请Service接口
// *
// * @author liujianwen
// * @date 2020-05-15
// */
//public interface IHrQuitService
//{
//    /**
//     * 查询离职申请
//     *
//     * @param id 离职申请ID
//     * @return 离职申请
//     */
//    public HrQuit selectHrQuitById(Long id);
//
//    /**
//     * 查询离职申请列表
//     *
//     * @param hrQuit 离职申请
//     * @return 离职申请集合
//     */
//    public List<HrQuit> selectHrQuitList(HrQuit hrQuit);
//
//    /**
//     * 新增离职申请
//     *
//     * @param hrQuit 离职申请
//     * @return 结果
//     */
//    public int insertHrQuit(HrQuit hrQuit);
//
//    /**
//     * 修改离职申请
//     *
//     * @param hrQuit 离职申请
//     * @return 结果
//     */
//    public int updateHrQuit(HrQuit hrQuit);
//
//    /**
//     * 批量删除离职申请
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    public int deleteHrQuitByIds(String ids);
//
//    /**
//     * 删除离职申请信息
//     *
//     * @param id 离职申请ID
//     * @return 结果
//     */
//    public int deleteHrQuitById(Long id);
//
//    /**
//     * 启动流程
//     * @param hrQuit
//     * @param applyUserId
//     * @return
//     */
//    ProcessInstance submitApply(HrQuit hrQuit, String applyUserId);
//
//    /**
//     * 完成任务
//     * @param hrQuit
//     * @param saveEntity
//     * @param taskId
//     * @param variables
//     */
//    void complete(HrQuit hrQuit, Boolean saveEntity, String taskId, Map<String, Object> variables,String starUserName);
//
//    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);
//
//}
