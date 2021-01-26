package com.ruoyi.hr.mapper;

import java.util.List;

import com.ruoyi.hr.domain.HrInterpolate;
import com.ruoyi.hr.domain.HrNonManager;
import com.ruoyi.base.domain.HrRecruit;
import com.ruoyi.base.domain.VO.HrRecruitVO;
import org.apache.ibatis.annotations.Param;

/**
 * 招聘申请Mapper接口
 * 
 * @author cmw
 * @date 2020-05-11
 */
public interface HrRecruitMapper
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
     * 删除招聘申请
     * 
     * @param recruitId 招聘申请ID
     * @return 结果
     */
    public int deleteTHrRecruitById(Long recruitId);

    /**
     * 批量删除招聘申请
     * 
     * @param recruitIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteTHrRecruitByIds(String[] recruitIds);

    /**
     * 招聘看板数据
     *
     * @return 结果
     * @param hrFlag
     * @param deptId
     */
    public List<HrRecruitVO> findAllInfo(@Param("hrFlag") Integer hrFlag, @Param("deptId") Long deptId);

    Integer findAllInfoCount(@Param("hrFlag") Integer hrFlag, @Param("deptId") Long deptId);

    HrRecruit selectTHrRecruitByHrNonManager(HrNonManager hrNonManager);

    HrRecruit updateRecruitCountByHrInterpolate(HrInterpolate hrInterpolate);
}
