package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.domain.HrNonManager;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

/**
 * 入职申请Service接口
 * 
 * @author xt
 * @date 2020-05-14
 */
public interface IHrNonManagerService 
{
    /**
     * 查询入职申请
     * 
     * @param id 入职申请ID
     * @return 入职申请
     */
    public HrNonManager selectHrNonManagerById(Long id);

    /**
     * 查询入职申请列表
     * 
     * @param hrNonManager 入职申请
     * @return 入职申请集合
     */
    public List<HrNonManager> selectHrNonManagerList(HrNonManager hrNonManager);

    public List<HrNonManager> selectHrNonManagerListManage(HrNonManager hrNonManager);

    public List<HrNonManager> selectHrNonManagerListExport(HrNonManager hrNonManager);

    /**
     * 新增入职申请
     * 
     * @param hrNonManager 入职申请
     * @return 结果
     */
    public int insertHrNonManager(HrNonManager hrNonManager);

    /**
     * 修改入职申请
     * 
     * @param hrNonManager 入职申请
     * @return 结果
     */
    public int updateHrNonManager(HrNonManager hrNonManager);

    /**
     * 批量删除入职申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrNonManagerByIds(String ids);

    /**
     * 删除入职申请信息
     * 
     * @param id 入职申请ID
     * @return 结果
     */
    public int deleteHrNonManagerById(Long id);

    void showVerifyDialog(String taskId, String module, String formPageName, ModelMap mmap, String instanceId);

    AjaxResult complete(String taskId, HttpServletRequest request, HrNonManager hrNonManager, String comment, String p_B_hrApproved);

    AjaxResult repeal(String taskId, HttpServletRequest request, String message);

    AjaxResult commit(Long id);
}
