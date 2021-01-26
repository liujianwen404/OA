package com.ruoyi.hr.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ruoyi.base.domain.DTO.SalaryDTOError;
import com.ruoyi.base.domain.DTO.SalaryStructureDTO;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.SalaryStructure;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;

/**
 * 薪资结构Service接口
 * 
 * @author xt
 * @date 2020-06-18
 */
public interface ISalaryStructureService 
{
    /**
     * 查询薪资结构
     * 
     * @param id 薪资结构ID
     * @return 薪资结构
     */
    public SalaryStructure selectSalaryStructureById(Long id);

    /**
     * 查询薪资结构列表
     * 
     * @param salaryStructure 薪资结构
     * @return 薪资结构集合
     */
    public List<SalaryStructure> selectSalaryStructureList(SalaryStructure salaryStructure);

    /**
     * 查询所有员工最近的调薪列表数据
     * @param salaryStructure
     * @return
     */
    public List<SalaryStructure> selectEmpSalaryStructureList(SalaryStructure salaryStructure,String startNonManagerDate,String endNonManagerDate
            ,String startAdjustDate,String endAdjustDate);

    List<SalaryStructure> selectSalaryStructureListInfo(SalaryStructure salaryStructure);

    public List<SalaryStructure> selectSalaryStructureListEx(SalaryStructure salaryStructure);

    /**
     * 新增薪资结构
     * 
     * @param salaryStructure 薪资结构
     * @return 结果
     */
    public int insertSalaryStructure(SalaryStructure salaryStructure);

    /**
     * 修改薪资结构
     * 
     * @param salaryStructure 薪资结构
     * @return 结果
     */
    public int updateSalaryStructure(SalaryStructure salaryStructure);

    /**
     * 强行更新不跳过null
     * @param salaryStructure
     * @return
     */
    public int updateSalary(SalaryStructure salaryStructure);

    /**
     * 批量删除薪资结构
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSalaryStructureByIds(String ids);

    /**
     * 删除薪资结构信息
     * 
     * @param id 薪资结构ID
     * @return 结果
     */
    public int deleteSalaryStructureById(Long id);

    int insertSalaryStructureProcess(SalaryStructure salaryStructure);

    AjaxResult complete(SalaryStructure salaryStructure, String taskId, HttpServletRequest request);

    AjaxResult repeal(String instanceId, HttpServletRequest request, String message);

    SalaryStructure selectOneBySalaryStructure(SalaryStructure salaryStructure);

    void insertSalaryStructureDTO(SalaryStructureDTO salaryStructureDTO, AtomicInteger successCount, List<SalaryDTOError> errorList);

    List<SalaryStructure> selectSalaryStructuresByExample(Example example);

    List<SalaryStructure> selectSalaryStructureListNew(SalaryStructure selectSalary);
}
