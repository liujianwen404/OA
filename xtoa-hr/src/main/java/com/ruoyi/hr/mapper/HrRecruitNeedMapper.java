package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.hr.domain.HrRecruitNeed;
import java.util.List;
/**
 * 招聘需求 数据层
 *
 * @author xt
 * @date 2020-05-28
 */
public interface HrRecruitNeedMapper extends MyBaseMapper<HrRecruitNeed> {

    /**
     * 查询招聘需求列表
     *
     * @param hrRecruitNeed 招聘需求
     * @return 招聘需求集合
     */
    public List<HrRecruitNeed> selectHrRecruitNeedList(HrRecruitNeed hrRecruitNeed);

    /**
     * 删除招聘需求
     *
     * @param recruitNeedId 招聘需求ID
     * @return 结果
     */
    public int deleteHrRecruitNeedById(Long recruitNeedId);

    /**
     * 批量删除招聘需求
     *
     * @param recruitNeedIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrRecruitNeedByIds(String[] recruitNeedIds);

}