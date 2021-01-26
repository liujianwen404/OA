package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.ProjectOperation;
import java.util.List;
/**
 * 项目计划任务操作记录 数据层
 *
 * @author xt
 * @date 2020-06-30
 */
public interface ProjectOperationMapper extends MyBaseMapper<ProjectOperation> {

    /**
     * 查询项目计划任务操作记录列表
     *
     * @param projectOperation 项目计划任务操作记录
     * @return 项目计划任务操作记录集合
     */
    public List<ProjectOperation> selectProjectOperationList(ProjectOperation projectOperation);

    /**
     * 删除项目计划任务操作记录
     *
     * @param id 项目计划任务操作记录ID
     * @return 结果
     */
    public int deleteProjectOperationById(Long id);

    /**
     * 批量删除项目计划任务操作记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectOperationByIds(String[] ids);

}