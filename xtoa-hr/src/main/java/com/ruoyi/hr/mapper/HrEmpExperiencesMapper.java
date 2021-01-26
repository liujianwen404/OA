package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrEmpExperiences;
import java.util.List;
/**
 * 员工工作经历 数据层
 *
 * @author liujianwen
 * @date 2021-01-11
 */
public interface HrEmpExperiencesMapper extends MyBaseMapper<HrEmpExperiences> {

    /**
     * 查询员工工作经历列表
     *
     * @param hrEmpExperiences 员工工作经历
     * @return 员工工作经历集合
     */
    public List<HrEmpExperiences> selectHrEmpExperiencesList(HrEmpExperiences hrEmpExperiences);

    /**
     * 删除员工工作经历
     *
     * @param id 员工工作经历ID
     * @return 结果
     */
    public int deleteHrEmpExperiencesById(Long id);

    /**
     * 批量删除员工工作经历
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrEmpExperiencesByIds(String[] ids);

}