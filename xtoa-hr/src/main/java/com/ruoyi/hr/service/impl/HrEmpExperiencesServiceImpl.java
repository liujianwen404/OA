package com.ruoyi.hr.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrEmpExperiencesMapper;
import com.ruoyi.base.domain.HrEmpExperiences;
import com.ruoyi.hr.service.IHrEmpExperiencesService;
import com.ruoyi.common.core.text.Convert;

/**
 * 员工工作经历Service业务层处理
 * 
 * @author liujianwen
 * @date 2021-01-11
 */
@Service
public class HrEmpExperiencesServiceImpl implements IHrEmpExperiencesService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrEmpExperiencesServiceImpl.class);

    @Autowired
    private HrEmpExperiencesMapper hrEmpExperiencesMapper;

    /**
     * 查询员工工作经历
     * 
     * @param id 员工工作经历ID
     * @return 员工工作经历
     */
    @Override
    public HrEmpExperiences selectHrEmpExperiencesById(Long id)
    {
        return hrEmpExperiencesMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询员工工作经历列表
     * 
     * @param hrEmpExperiences 员工工作经历
     * @return 员工工作经历
     */
    @Override
    public List<HrEmpExperiences> selectHrEmpExperiencesList(HrEmpExperiences hrEmpExperiences)
    {
        hrEmpExperiences.setDelFlag("0");
        return hrEmpExperiencesMapper.selectHrEmpExperiencesList(hrEmpExperiences);
    }

    /**
     * 新增员工工作经历
     * 
     * @param hrEmpExperiences 员工工作经历
     * @return 结果
     */
    @Override
    public int insertHrEmpExperiences(HrEmpExperiences hrEmpExperiences)
    {
        hrEmpExperiences.setCreateId(ShiroUtils.getUserId());
        hrEmpExperiences.setCreateBy(ShiroUtils.getLoginName());
        hrEmpExperiences.setCreateTime(DateUtils.getNowDate());
        return hrEmpExperiencesMapper.insertSelective(hrEmpExperiences);
    }

    /**
     * 修改员工工作经历
     * 
     * @param hrEmpExperiences 员工工作经历
     * @return 结果
     */
    @Override
    public int updateHrEmpExperiences(HrEmpExperiences hrEmpExperiences)
    {
        hrEmpExperiences.setUpdateId(ShiroUtils.getUserId());
        hrEmpExperiences.setUpdateBy(ShiroUtils.getLoginName());
        hrEmpExperiences.setUpdateTime(DateUtils.getNowDate());
        return hrEmpExperiencesMapper.updateByPrimaryKeySelective(hrEmpExperiences);
    }

    /**
     * 删除员工工作经历对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrEmpExperiencesByIds(String ids)
    {
        return hrEmpExperiencesMapper.deleteHrEmpExperiencesByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除员工工作经历信息
     * 
     * @param id 员工工作经历ID
     * @return 结果
     */
    @Override
    public int deleteHrEmpExperiencesById(Long id)
    {
        return hrEmpExperiencesMapper.deleteHrEmpExperiencesById(id);
    }



    @Override
    public HrEmpExperiences selectSingleOneByExample(Example example){
        return hrEmpExperiencesMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<HrEmpExperiences> selectByExample(Example example){
        return hrEmpExperiencesMapper.selectByExample(example);
    }

}
