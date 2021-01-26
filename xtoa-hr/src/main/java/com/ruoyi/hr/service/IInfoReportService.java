package com.ruoyi.hr.service;



import com.ruoyi.base.domain.HrEmp;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 员工Service接口
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public interface IInfoReportService
{
//    /**
//     * 查询员工
//     *
//     * @param id 员工ID
//     * @return 员工
//     */
//    public Resume selectResumeById(Long id);

    /**
     * 查询部门人数统计列表
     *
     * @return 部门人数统计集合
     */
    public List<Map<Integer, String>> selectEmpList();

    /**
     * 查询部门转正人数统计列表
     *
     * @return 部门转正人数统计集合
     */
    public List<Map<Integer, String>> selectRegularList();

    List<Map<Integer, String>> selectRecruitList();

    List<HrEmp> selectQuitList(HrEmp emp);

    List<Map<Integer, String>> selectQuitReportList();

    List<Map<String, Object>> selectJobTransferReportList();

    List<HrEmp> selectEmpRegularList(HrEmp emp);
}
