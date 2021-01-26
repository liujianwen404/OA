package com.ruoyi.hr.service;



import com.ruoyi.base.domain.Resume;
import com.ruoyi.system.domain.SysUser;

import java.util.List;

/**
 * 员工Service接口
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public interface IResumeService
{
    /**
     * 查询员工
     * 
     * @param id 员工ID
     * @return 员工
     */
    public Resume selectResumeById(Long id);

    /**
     * 查询员工列表
     * 
     * @param resume 员工
     * @return 员工集合
     */
    public List<Resume> selectResumeList(Resume resume);

    /**
     * 新增员工
     * 
     * @param resume 员工
     * @return 结果
     */
    public int insertResume(Resume resume);

    /**
     * 修改员工
     * 
     * @param resume 员工
     * @return 结果
     */
    public int updateResume(Resume resume);

    /**
     * 批量删除员工
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteResumeByIds(String ids);

    /**
     * 删除员工信息
     * 
     * @param id 员工ID
     * @return 结果
     */
    public int deleteResumeById(Long id);
    
    /**
     * 作废
     * @param Resume
     * @return
     */
    public int invalidResume(Resume resume);

    /**
     * 导入用户数据
     *
     * @param resumeList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importResume(List<Resume> resumeList, Boolean isUpdateSupport, String operName);
}
