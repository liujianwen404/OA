package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrGoout;
import java.util.List;
/**
 * 外出申请 数据层
 *
 * @author liujianwen
 * @date 2020-07-06
 */
public interface HrGooutMapper extends MyBaseMapper<HrGoout> {

    /**
     * 查询外出申请列表
     *
     * @param hrGoout 外出申请
     * @return 外出申请集合
     */
    public List<HrGoout> selectHrGooutList(HrGoout hrGoout);

//    @DataScope(deptAlias = "d", menuAlias = "hr:goout:gooutList")
    public List<HrGoout> selectGooutManageList(HrGoout hrGoout);

    /**
     * 删除外出申请
     *
     * @param id 外出申请ID
     * @return 结果
     */
    public int deleteHrGooutById(Long id);

    /**
     * 批量删除外出申请
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrGooutByIds(String[] ids);

}