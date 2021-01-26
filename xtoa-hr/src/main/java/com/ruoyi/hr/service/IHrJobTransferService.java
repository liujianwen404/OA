package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.domain.HrJobTransfer;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

/**
 * 调动申请Service接口
 * 
 * @author xt
 * @date 2020-05-22
 */
public interface IHrJobTransferService 
{
    /**
     * 查询调动申请
     * 
     * @param jobTransferId 调动申请ID
     * @return 调动申请
     */
    public HrJobTransfer selectHrJobTransferById(Long jobTransferId);

    /**
     * 查询调动申请列表
     * 
     * @param hrJobTransfer 调动申请
     * @return 调动申请集合
     */
    public List<HrJobTransfer> selectHrJobTransferList(HrJobTransfer hrJobTransfer);
    public List<HrJobTransfer> selectHrJobTransferListManage(HrJobTransfer hrJobTransfer);
    public List<HrJobTransfer> selectHrJobTransferList2(HrJobTransfer hrJobTransfer);

    /**
     * 新增调动申请
     * 
     * @param hrJobTransfer 调动申请
     * @return 结果
     */
    public int insertHrJobTransfer(HrJobTransfer hrJobTransfer);

    /**
     * 修改调动申请
     * 
     * @param hrJobTransfer 调动申请
     * @return 结果
     */
    public int updateHrJobTransfer(HrJobTransfer hrJobTransfer);

    /**
     * 批量删除调动申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrJobTransferByIds(String ids);

    /**
     * 删除调动申请信息
     * 
     * @param jobTransferId 调动申请ID
     * @return 结果
     */
    public int deleteHrJobTransferById(Long jobTransferId);

    AjaxResult commit(Long jobTransferId);


    void showVerifyDialog(String taskId, String module, String formPageName, ModelMap mmap, String instanceId, HrJobTransfer jobTransfer);

    AjaxResult complete(String taskId, HttpServletRequest request, HrJobTransfer hrJobTransfer, String comment, String p_B_hrApproved);

    AjaxResult repeal(String taskId, HttpServletRequest request, String message);
}
