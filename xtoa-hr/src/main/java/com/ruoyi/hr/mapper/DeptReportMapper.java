package com.ruoyi.hr.mapper;

import com.ruoyi.base.domain.Resume;
import com.ruoyi.base.domain.HrEmp;

import java.util.List;

/**
 * Mapper接口
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public interface DeptReportMapper
{
//    /**
//     * 查询
//     *
//     * @param id ID
//     * @return
//     */
//    public Resume selectResumeById(Long id);

    /**
     * 查询列表
     * 
     * @param emp
     * @return 集合
     */
    public List<HrEmp> selectList(HrEmp emp);

//    /**
//     * 新增
//     *
//     * @param resume
//     * @return 结果
//     */
//    public int insertResume(Resume resume);
//
//    /**
//     * 修改
//     *
//     * @param Resume
//     * @return 结果
//     */
//    public int updateResume(Resume resume);
//
//    /**
//     * 删除
//     *
//     * @param id ID
//     * @return 结果
//     */
//    public int deleteResumeById(Long id);
//
//    /**
//     * 批量删除
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    public int deleteResumeByIds(String[] ids);
//
//    /**
//     * 作废
//     * @param Resume
//     * @return
//     */
//    public int invalidResume(Resume resume);
}
