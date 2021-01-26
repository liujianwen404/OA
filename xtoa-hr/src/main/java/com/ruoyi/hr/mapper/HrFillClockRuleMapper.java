package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrFillClockRule;
import java.util.List;
/**
 * 补卡规则 数据层
 *
 * @author xt
 * @date 2020-11-21
 */
public interface HrFillClockRuleMapper extends MyBaseMapper<HrFillClockRule> {

    /**
     * 查询补卡规则列表
     *
     * @param hrFillClockRule 补卡规则
     * @return 补卡规则集合
     */
    public List<HrFillClockRule> selectHrFillClockRuleList(HrFillClockRule hrFillClockRule);

    /**
     * 删除补卡规则
     *
     * @param id 补卡规则ID
     * @return 结果
     */
    public int deleteHrFillClockRuleById(Long id);

    /**
     * 批量删除补卡规则
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrFillClockRuleByIds(String[] ids);

}