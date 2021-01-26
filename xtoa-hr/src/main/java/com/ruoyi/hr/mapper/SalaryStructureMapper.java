package com.ruoyi.hr.mapper;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.SalaryStructure;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 薪资结构 数据层
 *
 * @author xt
 * @date 2020-06-18
 */
public interface SalaryStructureMapper extends MyBaseMapper<SalaryStructure> {

    /**
     * 查询薪资结构列表
     *
     * @param salaryStructure 薪资结构
     * @return 薪资结构集合
     */
//    @DataScope(deptAlias = "d", menuAlias = "hr:salaryStructure:view")
    public List<SalaryStructure> selectSalaryStructureList(SalaryStructure salaryStructure);

    @DataScope(deptAlias = "d", menuAlias = "hr:salaryStructure:view")
    public List<SalaryStructure> selectEmpSalaryStructureList(@Param("salaryStructure")SalaryStructure salaryStructure, @Param("startNonManagerDate") String startNonManagerDate,
               @Param("endNonManagerDate") String endNonManagerDate, @Param("startAdjustDate") String startAdjustDate, @Param("endAdjustDate") String endAdjustDate);

    public List<SalaryStructure> selectSalaryStructureListNew(SalaryStructure salaryStructure);

    @DataScope(deptAlias = "d", menuAlias = "hr:salaryStructure:view")
    public List<SalaryStructure> selectSalaryStructureListEx(SalaryStructure salaryStructure);

    /**
     * 删除薪资结构
     *
     * @param id 薪资结构ID
     * @return 结果
     */
    public int deleteSalaryStructureById(Long id);

    /**
     * 批量删除薪资结构
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSalaryStructureByIds(String[] ids);

    SalaryStructure selectOneBySalaryStructure(SalaryStructure salaryStructure);
    @DataScope(deptAlias = "d", menuAlias = "hr:salaryStructure:viewInfo")
    List<SalaryStructure> selectSalaryStructureListInfo(SalaryStructure salaryStructure);
}