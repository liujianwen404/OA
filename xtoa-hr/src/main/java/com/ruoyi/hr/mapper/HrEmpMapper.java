package com.ruoyi.hr.mapper;

import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.VO.HrEmpVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 员工Mapper接口
 * 
 * @author vivi07
 * @date 2020-05-07
 */
public interface HrEmpMapper
{
    /**
     * 查询员工
     * 
     * @param empId 员工ID
     * @return 员工
     */
    public HrEmp selectTHrEmpById(Long empId);

    /**
     * 根据empId获取员工名称
     * @param empId
     * @return
     */
    @Select("SELECT emp_name FROM t_hr_emp WHERE emp_id=#{empId}")
    String queryEmpNameByEmpId(Long empId);

    /**
     * 根据员工工号判定是否存在
     * @param empNum
     * @return
     */
/*
    @Select("SELECT COUNT(*) FROM t_hr_emp WHERE emp_num=#{empNum} AND del_flag = 0")
    int queryCountByEmpNum(@Param("empNum") String empNum);
*/

    public HrEmp selectHrEmpByEmpNum(String empNum);

    /**
     * 查询员工列表
     *
     * @param hrEmp 员工
     * @return 员工集合
     */
    public List<HrEmp> selectTHrEmpList(HrEmp hrEmp);

    /**
     * 新增员工
     * 
     * @param hrEmp 员工
     * @return 结果
     */
    public int insertTHrEmp(HrEmp hrEmp);

    /**
     * 修改员工
     * 
     * @param hrEmp 员工
     * @return 结果
     */
     int updateTHrEmp(HrEmp hrEmp);

    /**
     * 更新员工信息的用户id
     * @param userId
     * @param empId
     * @return
     */
    @Update("UPDATE t_hr_emp SET user_id = #{userId} WHERE emp_id = #{empId}")
    int updateTHrEmpByEmpId(@Param("userId") Long userId,@Param("empId")Long empId);

    /**
     * 删除员工
     * 
     * @param empId 员工ID
     * @return 结果
     */
//    public int deleteTHrEmpById(Long empId);

    /**
     * 批量删除员工
     * 
     * @param empIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteTHrEmpByIds(String[] empIds);

    HrEmp selectByPhonenumber(@Param("phonenumber") String phonenumber);

    HrEmp selectByEmail(@Param("email") String email);

    HrEmp selectByIdNumber(@Param("idNumber") String idNumber);

    HrEmp selectByUserId(@Param("userId") Long userId);

    List<HrEmpVo> selectListZtree(HrEmp hrEmp);

    int deleteTHrEmpByUserIds(Long[] userIds);

    String selectEmpNameByEmpId(Long id);

    List<HrEmp> selectEmpList();

    List<Map<String,Object>> selectOnTheJobEmpList(@Param("year")int year, @Param("month")int month);

    List<HrEmp> selectCountAttEmpList(@Param("year")int year, @Param("month")int month);

    List<HrEmp> selectOnTheEmpList();

    HrEmp selectByIdNumberAndDelete(@Param("idNumber") String idNumber);
}
