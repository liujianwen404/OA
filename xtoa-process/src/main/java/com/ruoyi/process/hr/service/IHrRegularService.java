//package com.ruoyi.process.hr.service;
//
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.process.hr.domain.HrRegular;
//import org.activiti.engine.runtime.ProcessInstance;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.Map;
//
///**
// * Service接口
// *
// * @author liujianwen
// * @date 2020-05-15
// */
//public interface IHrRegularService
//{
//    /**
//     * 查询
//     *
//     * @param id 离职申请ID
//     * @return 离职申请
//     */
//    public HrRegular selectHrRegularById(Long id);
//
//    /**
//     * 查询列表
//     * @return 离职申请集合
//     */
//    public List<HrRegular> selectHrRegularList(HrRegular hrRegular);
//
//    /**
//     * 新增
//     *
//     * @return 结果
//     */
//    public int insertHrRegular(HrRegular hrRegular);
//
//    /**
//     * 修改
//     * @return 结果
//     */
//    public int updateHrRegular(HrRegular hrRegular);
//
//    /**
//     * 批量删除
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    public int deleteHrRegularByIds(String ids);
//
//    /**
//     * 删除
//     *
//     * @param id 申请ID
//     * @return 结果
//     */
//    public int deleteHrRegularById(Long id);
//
//    /**
//     * 启动流程
//     * @param applyUserId
//     * @return
//     */
//    ProcessInstance submitApply(HrRegular hrRegular, String applyUserId);
//
//    /**
//     * 完成任务
//     */
//    void complete(HrRegular hrRegular, Boolean saveEntity, String taskId, Map<String, Object> variables,String starUserName);
//
//    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);
//
//}
