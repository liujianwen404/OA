package com.ruoyi.hr.service;

import java.util.List;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.base.domain.HrEmpExperiences;

/**
 * 员工工作经历Service接口
 * 
 * @author liujianwen
 * @date 2021-01-11
 */
public interface IHrEmpExperiencesService 
{
    /**
     * 查询员工工作经历
     * 
     * @param id 员工工作经历ID
     * @return 员工工作经历
     */
    public HrEmpExperiences selectHrEmpExperiencesById(Long id);

    /**
     * 查询员工工作经历列表
     * 
     * @param hrEmpExperiences 员工工作经历
     * @return 员工工作经历集合
     */
    public List<HrEmpExperiences> selectHrEmpExperiencesList(HrEmpExperiences hrEmpExperiences);

    /**
     * 新增员工工作经历
     * 
     * @param hrEmpExperiences 员工工作经历
     * @return 结果
     */
    public int insertHrEmpExperiences(HrEmpExperiences hrEmpExperiences);

    /**
     * 修改员工工作经历
     * 
     * @param hrEmpExperiences 员工工作经历
     * @return 结果
     */
    public int updateHrEmpExperiences(HrEmpExperiences hrEmpExperiences);

    /**
     * 批量删除员工工作经历
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrEmpExperiencesByIds(String ids);

    /**
     * 删除员工工作经历信息
     * 
     * @param id 员工工作经历ID
     * @return 结果
     */
    public int deleteHrEmpExperiencesById(Long id);

    HrEmpExperiences selectSingleOneByExample(Example example);

    List<HrEmpExperiences> selectByExample(Example example);

}
