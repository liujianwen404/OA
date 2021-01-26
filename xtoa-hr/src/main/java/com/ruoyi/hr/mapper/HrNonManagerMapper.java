package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.hr.domain.HrNonManager;
import java.util.List;
/**
 * 入职申请 数据层
 *
 * @author xt
 * @date 2020-05-14
 */
public interface HrNonManagerMapper extends MyBaseMapper<HrNonManager> {

    /**
     * 查询入职申请列表
     *
     * @param hrNonManager 入职申请
     * @return 入职申请集合
     */
    public List<HrNonManager> selectHrNonManagerList( HrNonManager hrNonManager);

    @DataScope(deptAlias = "d", menuAlias = "hr:manager:viewManage")
    public List<HrNonManager> selectHrNonManagerListManage(HrNonManager hrNonManager);

    /**
     * 删除入职申请
     *
     * @param id 入职申请ID
     * @return 结果
     */
    public int deleteHrNonManagerById(Long id);

    /**
     * 批量删除入职申请
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrNonManagerByIds(String[] ids);

}