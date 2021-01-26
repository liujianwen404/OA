package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.HrOff;
import java.util.List;
/**
 * off流程 数据层
 *
 * @author xt
 * @date 2020-07-28
 */
public interface HrOffMapper extends MyBaseMapper<HrOff> {

    /**
     * 查询off流程列表
     *
     * @param hrOff off流程
     * @return off流程集合
     */
    public List<HrOff> selectHrOffList(HrOff hrOff);

    @DataScope(deptAlias = "d", menuAlias = "hr:hrOff:viewManage")
    public List<HrOff> selectHrOffListManage(HrOff hrOff);

    /**
     * 删除off流程
     *
     * @param id off流程ID
     * @return 结果
     */
    public int deleteHrOffById(Long id);

    /**
     * 批量删除off流程
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrOffByIds(String[] ids);

}