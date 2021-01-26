package com.ruoyi.hr.service;

import java.util.List;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.base.domain.HrFillClockRule;

/**
 * 补卡规则Service接口
 * 
 * @author xt
 * @date 2020-11-21
 */
public interface IHrFillClockRuleService 
{
    /**
     * 查询补卡规则
     * 
     * @param id 补卡规则ID
     * @return 补卡规则
     */
    public HrFillClockRule selectHrFillClockRuleById(Long id);

    /**
     * 查询补卡规则列表
     * 
     * @param hrFillClockRule 补卡规则
     * @return 补卡规则集合
     */
    public List<HrFillClockRule> selectHrFillClockRuleList(HrFillClockRule hrFillClockRule);

    /**
     * 新增补卡规则
     * 
     * @param hrFillClockRule 补卡规则
     * @return 结果
     */
    public int insertHrFillClockRule(HrFillClockRule hrFillClockRule);

    /**
     * 修改补卡规则
     * 
     * @param hrFillClockRule 补卡规则
     * @return 结果
     */
    public int updateHrFillClockRule(HrFillClockRule hrFillClockRule);

    /**
     * 批量删除补卡规则
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrFillClockRuleByIds(String ids);

    /**
     * 删除补卡规则信息
     * 
     * @param id 补卡规则ID
     * @return 结果
     */
    public int deleteHrFillClockRuleById(Long id);

    HrFillClockRule selectSingleOneByExample(Example example);

    List<HrFillClockRule> selectByExample(Example example);

}
