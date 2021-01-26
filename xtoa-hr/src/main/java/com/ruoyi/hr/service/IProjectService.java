package com.ruoyi.hr.service;

import java.util.List;

import com.ruoyi.base.domain.VO.ProjectSelectVO;
import com.ruoyi.base.domain.VO.ProjectVO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.Project;

/**
 * 项目Service接口
 * 
 * @author xt
 * @date 2020-06-30
 */
public interface IProjectService 
{
    /**
     * 查询项目
     * 
     * @param id 项目ID
     * @return 项目
     */
    public Project selectProjectById(Long id);

    /**
     * 查询项目列表
     * 
     * @param project 项目
     * @return 项目集合
     */
    public List<Project> selectProjectList(Project project);

    /**
     * 新增项目
     * 
     * @param project 项目
     * @return 结果
     */
    public int insertProject(Project project);

    /**
     * 修改项目
     * 
     * @param project 项目
     * @return 结果
     */
    public int updateProject(Project project);

    /**
     * 批量删除项目
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectByIds(String ids);

    /**
     * 删除项目信息
     * 
     * @param id 项目ID
     * @return 结果
     */
    public int deleteProjectById(Long id);

    AjaxResult getMyCount();

    List<ProjectVO> projectTableInfo(Integer status, String empName);

    Project selectProjectByChatId(String chatId);

    AjaxResult finishProject(Long id);

    /**
     * 获取项目下拉列表
     * @return
     */
    List<ProjectSelectVO> getProjectSelectList();

    /**
     * 根据项目id获取项目群组会话id
     * @param id
     * @return
     */
    String queryProjectChatIdById(Long id);
}
