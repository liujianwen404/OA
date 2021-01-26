package com.ruoyi.hr.mapper;

import com.ruoyi.base.domain.HrEmp;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Mapper接口
 * 
 * @author vivi07
 * @date 2020-04-04
 */
public interface InfoReportMapper
{
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
