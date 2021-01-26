package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.DTO.GooutDTO;
import com.ruoyi.base.domain.HrFillClock;
import com.ruoyi.base.domain.HrGoout;

import javax.servlet.http.HttpServletRequest;

/**
 * 外出申请Service接口
 * 
 * @author liujianwen
 * @date 2020-07-06
 */
public interface IHrGooutService 
{
    /**
     * 查询外出申请
     * 
     * @param id 外出申请ID
     * @return 外出申请
     */
    public HrGoout selectHrGooutById(Long id);

    /**
     * 查询外出申请列表
     * 
     * @param hrGoout 外出申请
     * @return 外出申请集合
     */
    public List<HrGoout> selectHrGooutList(HrGoout hrGoout);

    /**
     * 新增外出申请
     * 
     * @param hrGoout 外出申请
     * @return 结果
     */
    public int insertHrGoout(HrGoout hrGoout);

    /**
     * 修改外出申请
     * 
     * @param hrGoout 外出申请
     * @return 结果
     */
    public int updateHrGoout(HrGoout hrGoout);

    /**
     * 批量删除外出申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrGooutByIds(String ids);

    /**
     * 删除外出申请信息
     * 
     * @param id 外出申请ID
     * @return 结果
     */
    public int deleteHrGooutById(Long id);

    AjaxResult submitApply(HrGoout hrGoout, String applyUserId);

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);

    AjaxResult complete(HrGoout hrGoout, String taskId, HttpServletRequest request);

    int insertGooutDTO(GooutDTO gooutDTO);

    List<HrGoout> selectGooutManageList(HrGoout hrGoout);
}
