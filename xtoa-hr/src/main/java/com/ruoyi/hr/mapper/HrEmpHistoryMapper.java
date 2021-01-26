package com.ruoyi.hr.mapper;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrEmpHistory;
import java.util.List;
/**
 * 员工历史 数据层
 *
 * @author xt
 * @date 2020-06-23
 */
public interface HrEmpHistoryMapper extends MyBaseMapper<HrEmpHistory> {

    /**
     * 查询员工历史列表
     *
     * @param hrEmpHistory 员工历史
     * @return 员工历史集合
     */
    public List<HrEmpHistory> selectHrEmpHList(HrEmpHistory hrEmpHistory);

    /**
     * 删除员工历史
     *
     * @param id 员工历史ID
     * @return 结果
     */
    public int deleteHrEmpHById(Long id);

    /**
     * 批量删除员工历史
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrEmpHByIds(String[] ids);

}
