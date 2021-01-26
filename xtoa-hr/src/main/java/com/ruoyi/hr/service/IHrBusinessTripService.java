package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.DTO.BusinessTripDTO;
import com.ruoyi.base.domain.HrBusinessTrip;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 出差申请Service接口
 * 
 * @author liujianwen
 * @date 2020-06-30
 */
public interface IHrBusinessTripService 
{
    /**
     * 查询出差申请
     * 
     * @param id 出差申请ID
     * @return 出差申请
     */
    public HrBusinessTrip selectHrBusinessTripById(Long id);

    /**
     * 查询出差申请列表
     * 
     * @param hrBusinessTrip 出差申请
     * @return 出差申请集合
     */
    public List<HrBusinessTrip> selectHrBusinessTripList(HrBusinessTrip hrBusinessTrip);

    /**
     * 新增出差申请
     * 
     * @param hrBusinessTrip 出差申请
     * @return 结果
     */
    public int insertHrBusinessTrip(HrBusinessTrip hrBusinessTrip, HttpServletRequest request);

    /**
     * 修改出差申请
     * 
     * @param hrBusinessTrip 出差申请
     * @return 结果
     */
    public int updateHrBusinessTrip(HrBusinessTrip hrBusinessTrip,HttpServletRequest request);

    /**
     * 批量删除出差申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrBusinessTripByIds(String ids);

    /**
     * 删除出差申请信息
     * 
     * @param id 出差申请ID
     * @return 结果
     */
    public int deleteHrBusinessTripById(Long id);

    AjaxResult submitApply(HrBusinessTrip hrBusinessTrip, String applyUserId);

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);

    AjaxResult complete(HrBusinessTrip hrBusinessTrip, String taskId, HttpServletRequest request);

    int insertBusinessTripDTO(BusinessTripDTO businessTripDTO);

    List<HrBusinessTrip> selectBusinessTripManageList(HrBusinessTrip hrBusinessTrip);
}
