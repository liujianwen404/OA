package com.ruoyi.hr.service;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.DTO.FillClockDTO;
import com.ruoyi.base.domain.HrFillClock;

import javax.servlet.http.HttpServletRequest;

/**
 * 补卡申请Service接口
 * 
 * @author liujianwen
 * @date 2020-06-24
 */
public interface IHrFillClockService 
{
    /**
     * 查询补卡申请
     * 
     * @param id 补卡申请ID
     * @return 补卡申请
     */
    public HrFillClock selectHrFillClockById(Long id);

    /**
     * 查询补卡申请列表
     * 
     * @param hrFillClock 补卡申请
     * @return 补卡申请集合
     */
    public List<HrFillClock> selectHrFillClockList(HrFillClock hrFillClock);

    /**
     * 新增补卡申请
     * 
     * @param hrFillClock 补卡申请
     * @return 结果
     */
    public int insertHrFillClock(HrFillClock hrFillClock);

    /**
     * 修改补卡申请
     * 
     * @param hrFillClock 补卡申请
     * @return 结果
     */
    public int updateHrFillClock(HrFillClock hrFillClock);

    /**
     * 批量删除补卡申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrFillClockByIds(String ids);

    /**
     * 删除补卡申请信息
     * 
     * @param id 补卡申请ID
     * @return 结果
     */
    public int deleteHrFillClockById(Long id);

    int insertFillClockDTO(FillClockDTO fillClockDRO);

    AjaxResult submitApply(HrFillClock hrFillClock, String applyUserId);

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);

    AjaxResult complete(HrFillClock hrFillClock, String taskId, HttpServletRequest request);

    Integer selectHrFillClockCount(Long userId, Date classDate);

    List<HrFillClock> selectHrFillClockListManage(HrFillClock hrFillClock);

    /**
     * 查询所有补卡记录数
     * @return
     */
    int selectHrFillClockNum(Long empId, Date classDate);

    Integer selectHrFillClockCompleteCount(Long empId, Date classDate);
}
