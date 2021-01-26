package com.ruoyi.hr.mapper;

import com.ruoyi.base.domain.DTO.ProjectPlanDTO;
import com.ruoyi.base.domain.VO.ProjectPlanSelectVO;
import com.ruoyi.base.domain.VO.ProjectPlanVO;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.ProjectPlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * 项目计划 数据层
 *
 * @author xt
 * @date 2020-06-30
 */
public interface ProjectPlanMapper extends MyBaseMapper<ProjectPlan> {

    /**
     * 查询项目计划列表
     *
     * @param projectPlan 项目计划
     * @return 项目计划集合
     */
    public List<ProjectPlan> selectProjectPlanList(ProjectPlan projectPlan);

    /**
     * 删除项目计划
     *
     * @param id 项目计划ID
     * @return 结果
     */
    public int deleteProjectPlanById(Long id);

    /**
     * 批量删除项目计划
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectPlanByIds(String[] ids);

    List<Long> selectProjectPlanEmps(@Param("projectId") Long projectId);

    public List<ProjectPlanVO> getProjectPlanList(ProjectPlanDTO dto);

    /**
     * 获取项目下所有迭代
     * @param projectId
     * @return
     */
    @Select({"<script>",
            "SELECT id,name FROM t_project_plan",
            "WHERE del_flag='0'",
            "<when test='projectId!=null'>",
            "AND project_id = #{projectId}",
            "</when>",
            "</script>"})
    List<ProjectPlanSelectVO> queryProjectPlanList(@Param("projectId") Long projectId);

}