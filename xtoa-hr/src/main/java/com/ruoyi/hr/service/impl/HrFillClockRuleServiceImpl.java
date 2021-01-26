package com.ruoyi.hr.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrFillClockRuleMapper;
import com.ruoyi.base.domain.HrFillClockRule;
import com.ruoyi.hr.service.IHrFillClockRuleService;
import com.ruoyi.common.core.text.Convert;

/**
 * 补卡规则Service业务层处理
 * 
 * @author xt
 * @date 2020-11-21
 */
@Service
public class HrFillClockRuleServiceImpl implements IHrFillClockRuleService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrFillClockRuleServiceImpl.class);

    @Autowired
    private HrFillClockRuleMapper hrFillClockRuleMapper;

    /**
     * 查询补卡规则
     * 
     * @param id 补卡规则ID
     * @return 补卡规则
     */
    @Override
    public HrFillClockRule selectHrFillClockRuleById(Long id)
    {
        return hrFillClockRuleMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询补卡规则列表
     * 
     * @param hrFillClockRule 补卡规则
     * @return 补卡规则
     */
    @Override
    public List<HrFillClockRule> selectHrFillClockRuleList(HrFillClockRule hrFillClockRule)
    {
        hrFillClockRule.setDelFlag("0");
        return hrFillClockRuleMapper.selectHrFillClockRuleList(hrFillClockRule);
    }

    /**
     * 新增补卡规则
     * 
     * @param hrFillClockRule 补卡规则
     * @return 结果
     */
    @Override
    public int insertHrFillClockRule(HrFillClockRule hrFillClockRule)
    {
        hrFillClockRule.setCreateId(ShiroUtils.getUserId());
        hrFillClockRule.setCreateBy(ShiroUtils.getLoginName());
        hrFillClockRule.setCreateTime(DateUtils.getNowDate());
        return hrFillClockRuleMapper.insertSelective(hrFillClockRule);
    }

    /**
     * 修改补卡规则
     * 
     * @param hrFillClockRule 补卡规则
     * @return 结果
     */
    @Override
    public int updateHrFillClockRule(HrFillClockRule hrFillClockRule)
    {
        hrFillClockRule.setDelFlag("0");
        hrFillClockRule.setUpdateId(ShiroUtils.getUserId());
        hrFillClockRule.setUpdateBy(ShiroUtils.getLoginName());
        hrFillClockRule.setUpdateTime(DateUtils.getNowDate());
        return hrFillClockRuleMapper.updateByPrimaryKey(hrFillClockRule);
    }

    /**
     * 删除补卡规则对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrFillClockRuleByIds(String ids)
    {
        return hrFillClockRuleMapper.deleteHrFillClockRuleByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除补卡规则信息
     * 
     * @param id 补卡规则ID
     * @return 结果
     */
    @Override
    public int deleteHrFillClockRuleById(Long id)
    {
        return hrFillClockRuleMapper.deleteHrFillClockRuleById(id);
    }



    @Override
    public HrFillClockRule selectSingleOneByExample(Example example){
        return hrFillClockRuleMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<HrFillClockRule> selectByExample(Example example){
        return hrFillClockRuleMapper.selectByExample(example);
    }

}
