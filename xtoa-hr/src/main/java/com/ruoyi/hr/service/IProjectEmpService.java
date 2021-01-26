package com.ruoyi.hr.service;

import java.util.List;
import java.util.Set;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.dingTalk.dingCallBackDTO.ChatCallBackDTO;
import com.ruoyi.base.domain.ProjectEmp;
import com.ruoyi.system.domain.vo.UserTreeVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.entity.Example;

/**
 * 项目计划任务成员Service接口
 * 
 * @author xt
 * @date 2020-06-30
 */
public interface IProjectEmpService 
{
    /**
     * 查询项目计划任务成员
     * 
     * @param id 项目计划任务成员ID
     * @return 项目计划任务成员
     */
    public ProjectEmp selectProjectEmpById(Long id);

    /**
     * 查询项目计划任务成员列表
     * 
     * @param projectEmp 项目计划任务成员
     * @return 项目计划任务成员集合
     */
    public List<ProjectEmp> selectProjectEmpList(ProjectEmp projectEmp);

    /**
     * 查询项目是否已存中此成员
     * @param projectId
     * @param empId
     * @return
     */
    int queryCountByEmpId(Long projectId,Long empId);

    /**
     * 新增项目计划任务成员
     * 
     * @param projectEmp 项目计划任务成员
     * @return 结果
     */
    public int insertProjectEmp(ProjectEmp projectEmp);

    /**
     * 修改项目计划任务成员
     * 
     * @param projectEmp 项目计划任务成员
     * @return 结果
     */
    public int updateProjectEmp(ProjectEmp projectEmp);

    /**
     * 批量删除项目计划任务成员
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteProjectEmpByIds(String ids);

    /**
     * 删除项目计划任务成员信息
     * 
     * @param id 项目计划任务成员ID
     * @return 结果
     */
    public int deleteProjectEmpById(Long id);

    AjaxResult insertProjectEmps(ProjectEmp projectEmp, Set<String> collect);

    List<UserTreeVo> selectProjectEmpToUserTreeVo(ProjectEmp projectEmp);

    /**
     * 钉钉回调，群会话添加人员
     * @param chatCallBackDTO
     */
    void chatAddMember(ChatCallBackDTO chatCallBackDTO);
    /**
     * 钉钉回调，群会话删除人员
     * @param chatCallBackDTO
     */
    void chatRemoveMember(ChatCallBackDTO chatCallBackDTO);
    /**
     * 钉钉回调，群会话用户主动退群
     * @param chatCallBackDTO
     */
    void chatQuit(ChatCallBackDTO chatCallBackDTO);
    /**
     * 钉钉回调，群会话解散群
     * @param chatCallBackDTO
     */
    void chatDisband(ChatCallBackDTO chatCallBackDTO);

    /**
     * 强制同步群回话数据,该方法只能在insertProjectEmps外层调用否则递归，（注意：目前只做添加成员时调用）
     * @param chatId
     */
    void synProjectChat(String chatId);

    List<ProjectEmp> selectByExample(Example example);

    /**
     * 重置用户仅为项目成员
     * @param collect
     */
    void resetEmpToProject(Set<Long> empIds);
    /**
     * 钉钉回调，群会话更换群主
     * @param chatCallBackDTO
     */
//    void chatUpdateOwner(ChatCallBackDTO chatCallBackDTO);

}
