package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.domain.HrInterpolate;
import com.ruoyi.hr.domain.HrNonManager;
import com.ruoyi.base.domain.HrRecruit;
import com.ruoyi.base.domain.VO.HrRecruitVO;

/**
 * 招聘申请Service接口
 * 
 * @author cmw
 * @date 2020-05-11
 */
public interface HrRecruitService
{
    /**
     * 查询招聘申请
     * 
     * @param recruitId 招聘申请ID
     * @return 招聘申请
     */
    public HrRecruit selectTHrRecruitById(Long recruitId);

    /**
     * 查询招聘申请列表
     * 
     * @param hrRecruit 招聘申请
     * @return 招聘申请集合
     */
    public List<HrRecruit> selectTHrRecruitList(HrRecruit hrRecruit);

    /**
     * 新增招聘申请
     * 
     * @param hrRecruit 招聘申请
     * @return 结果
     */
    public int insertTHrRecruit(HrRecruit hrRecruit);

    /**
     * 修改招聘申请
     * 
     * @param hrRecruit 招聘申请
     * @return 结果
     */
    public int updateTHrRecruit(HrRecruit hrRecruit);

    /**
     * 批量删除招聘申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTHrRecruitByIds(String ids);

    /**
     * 删除招聘申请信息
     * 
     * @param recruitId 招聘申请ID
     * @return 结果
     */
    public int deleteTHrRecruitById(Long recruitId);

    AjaxResult submitRecruit(Long recruitId);

    List<HrRecruitVO> findAllInfo();

    Integer findAllInfoCount();

    void updateRecruitCountByHrNonManager(HrNonManager hrNonManager);

    void updateRecruitCountByHrInterpolate(HrInterpolate hrInterpolate);
}
