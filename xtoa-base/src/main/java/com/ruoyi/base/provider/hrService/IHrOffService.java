package com.ruoyi.base.provider.hrService;

import java.util.List;
import com.ruoyi.base.domain.HrOff;
import com.ruoyi.common.core.domain.AjaxResult;

import javax.servlet.http.HttpServletRequest;

/**
 * off流程Service接口
 * 
 * @author xt
 * @date 2020-07-28
 */
public interface IHrOffService 
{
    /**
     * 查询off流程
     * 
     * @param id off流程ID
     * @return off流程
     */
    public HrOff selectHrOffById(Long id);

    /**
     * 查询off流程列表
     * 
     * @param hrOff off流程
     * @return off流程集合
     */
    public List<HrOff> selectHrOffList(HrOff hrOff);

    public List<HrOff> selectHrOffListManage(HrOff hrOff);

    /**
     * 新增off流程
     * 
     * @param hrOff off流程
     * @return 结果
     */
    public int insertHrOff(HrOff hrOff);

    /**
     * 修改off流程
     * 
     * @param hrOff off流程
     * @return 结果
     */
    public int updateHrOff(HrOff hrOff);

    /**
     * 批量删除off流程
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrOffByIds(String ids);

    /**
     * 删除off流程信息
     * 
     * @param id off流程ID
     * @return 结果
     */
    public int deleteHrOffById(Long id);

    AjaxResult submitApply(HrOff hrOff, String applyUserId);

    AjaxResult complete(HrOff hrOff, String taskId, HttpServletRequest request);

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);
}
