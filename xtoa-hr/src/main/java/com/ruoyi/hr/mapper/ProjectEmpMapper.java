package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.ProjectEmp;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 项目计划任务成员 数据层
 *
 * @author xt
 * @date 2020-06-30
 */
public interface ProjectEmpMapper extends MyBaseMapper<ProjectEmp> {

    /**
     * 查询项目计划任务成员列表
     *
     * @param projectEmp 项目计划任务成员
     * @return 项目计划任务成员集合
     */
    public List<ProjectEmp> selectProjectEmpList(ProjectEmp projectEmp);

    /**
     * 查询项目成员是否已存中
     * @param projectId
     * @param empId
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_project_emp entity WHERE type=0 AND entity.del_flag = '0'  AND entity.project_id=#{projectId} and entity.emp_id=#{empId}")
    int queryCountByEmpId(@Param("projectId")Long projectId,@Param("empId")Long empId);

    /**
     * 删除项目计划任务成员
     *
     * @param id 项目计划任务成员ID
     * @return 结果
     */
    public int deleteProjectEmpById(Long id);

    /**
     * 批量删除项目计划任务成员
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectEmpByIds(String[] ids);

    void deleteProjectEmpByCallBack(ProjectEmp projectEmp);

    void resetEmpToProject(@Param("empIds") Set<Long> empIds);

    void deleteProjectEmpByIdsByProjectId(@Param("pId") Long pId,@Param("empIds") Set<Long> empIds);
}