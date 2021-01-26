package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.domain.HrRecruitNeed;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

/**
 * 招聘需求Service接口
 * 
 * @author xt
 * @date 2020-05-28
 */
public interface IHrRecruitNeedService 
{
    /**
     * 查询招聘需求
     * 
     * @param recruitNeedId 招聘需求ID
     * @return 招聘需求
     */
    public HrRecruitNeed selectHrRecruitNeedById(Long recruitNeedId);

    /**
     * 查询招聘需求列表
     * 
     * @param hrRecruitNeed 招聘需求
     * @return 招聘需求集合
     */
    public List<HrRecruitNeed> selectHrRecruitNeedList(HrRecruitNeed hrRecruitNeed);

    /**
     * 新增招聘需求
     * 
     * @param hrRecruitNeed 招聘需求
     * @return 结果
     */
    public int insertHrRecruitNeed(HrRecruitNeed hrRecruitNeed);

    /**
     * 修改招聘需求
     * 
     * @param hrRecruitNeed 招聘需求
     * @return 结果
     */
    public int updateHrRecruitNeed(HrRecruitNeed hrRecruitNeed);

    /**
     * 批量删除招聘需求
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrRecruitNeedByIds(String ids);

    /**
     * 删除招聘需求信息
     * 
     * @param recruitNeedId 招聘需求ID
     * @return 结果
     */
    public int deleteHrRecruitNeedById(Long recruitNeedId);

    AjaxResult complete(String taskId, HttpServletRequest request, HrRecruitNeed hrRecruitNeed, String comment, String p_b_hrApproved, String endHr);

    void showVerifyDialog(String taskId, String module, String activitiId, ModelMap mmap, String instanceId);
}
