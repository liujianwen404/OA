package com.ruoyi.hr.mapper;

import com.ruoyi.base.domain.VO.ProjectSelectVO;
import com.ruoyi.base.domain.VO.ProjectVO;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.Project;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
/**
 * 项目 数据层
 *
 * @author xt
 * @date 2020-06-30
 */
public interface ProjectMapper extends MyBaseMapper<Project> {

    /**
     * 查询项目列表
     *
     * @param project 项目
     * @return 项目集合
     */
    public List<Project> selectProjectList(Project project);

    /**
     * 删除项目
     *
     * @param id 项目ID
     * @return 结果
     */
    public int deleteProjectById(Long id);

    /**
     * 批量删除项目
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectByIds(String[] ids);

    HashMap<String,Integer> getMyCount(@Param("userId") Long userId);

    List<ProjectVO> projectTableInfo(@Param("status") Integer status, @Param("empName") String empName);

    Integer getMyProjectCount();

    Integer getMyProjectPlanCount();

    Integer getMyProjectPlanTaskCount();

    /**
     * 获取项目下拉列表
     * @return
     */
    List<ProjectSelectVO> getProjectSelectList();

    @Select("SELECT chat_id FROM t_project WHERE id=#{id}")
    String queryProjectChatIdById(Long id);
}