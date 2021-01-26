package com.ruoyi.process.todoitem.service;

import com.ruoyi.process.leave.domain.BizLeaveVo;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 待办事项Service接口
 *
 * @author Xianlu Tech
 * @date 2019-11-08
 */
public interface IBizTodoItemService {
    /**
     * 查询待办事项
     *
     * @param id 待办事项ID
     * @return 待办事项
     */
    public BizTodoItem selectBizTodoItemById(Long id);

    /**
     * 查询待办事项列表
     *
     * @param bizTodoItem 待办事项
     * @return 待办事项集合
     */
    public List<BizTodoItem> selectBizTodoItemList(BizTodoItem bizTodoItem);

    /**
     * 新增待办事项
     *
     * @param bizTodoItem 待办事项
     * @return 结果
     */
    public int insertBizTodoItem(BizTodoItem bizTodoItem);

    /**
     * 修改待办事项
     *
     * @param bizTodoItem 待办事项
     * @return 结果
     */
    public int updateBizTodoItem(BizTodoItem bizTodoItem);

    /**
     * 批量删除待办事项
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizTodoItemByIds(String ids);

    /**
     * 删除待办事项信息
     *
     * @param id 待办事项ID
     * @return 结果
     */
    public int deleteBizTodoItemById(Long id);

    int insertTodoItem(String instanceId, String title, String module,String starUserName);

    int insertTodoItem(String instanceId, String title, String module,Boolean isOK,Long todoId,String starUserName);

    Map<String,Object> selectBizTodoItemListCount(BizTodoItem bizTodoItem);

    Map<String,Object> listCountForEmp(BizTodoItem bizTodoItem);

    /**
     * 审批任务更新状态
     * @param taskId
     * @return
     */
    Long updateTodoItem(String taskId,String isApproved);

    List<BizTodoItem> selectByTaskId( String taskId);
    List<BizTodoItem> selectByTaskIdAll( String taskId);

    String getEndHandleName(String instanceId);

    List<String> selectModules(Long userId);

    /**
     * 在登录用户是流程抄送人的情况下 查询这些流程的所有待办事项列表
     *
     * @param modules 模块数组
     * @return 待办事项集合
     */
    List<BizTodoItem> selectCopyTodoItemList(BizTodoItem bizTodoItem,String[] modules);

    /**
     * 在登录用户是流程抄送人的情况下 查询这些流程的所有待办事项列表 首页接口只查询抄送事项 不查询待办事项
     *
     * @param modules 模块数组
     * @return 待办事项集合
     */
    List<BizTodoItem> selectMainCopyTodoItemList(BizTodoItem bizTodoItem,String[] modules);

    int taskEntrust(String taskId,String todoUserId, String userId, String loginName,String empName);
}
