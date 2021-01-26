package com.ruoyi.hr.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.VO.HrEmpVo;
import com.ruoyi.system.domain.SysUser;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 员工Service接口
 * 
 * @author vivi07
 * @date 2020-05-07
 */
public interface HrEmpService
{
    /**
     * 查询员工
     * 
     * @param empId 员工ID
     * @return 员工
     */
    public HrEmp selectTHrEmpById(Long empId);

    /**
     * 根据empID获取员工名称
     * @param empId
     * @return
     */
    String queryEmpNameByEmpId(Long empId);

    /**
     * 查询员工信息包括已删除的历史数据
     *
     * @param empId 员工ID
     * @return 员工
     */
    public HrEmp selectHrEmpInfoByIdandFromHistory(Long empId);

    /**
     * 查询员工
     *
     * @param phonenumber 员工手机
     * @return 员工
     */
    public HrEmp selectTHrEmpByPhonenumber(String phonenumber);

    /**
     * 查询员工
     *
     * @param userId 用户userId
     * @return 员工
     */
    public HrEmp selectTHrEmpByUserId(Long userId);

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
    public AjaxResult insertTHrEmp(HrEmp hrEmp, HttpServletRequest request);

    /**
     * 修改员工
     * 
     * @param hrEmp 员工
     * @return 结果
     */
    public int updateTHrEmp(HrEmp hrEmp,HttpServletRequest request);

    void logoutUser(String nameUser);

    /**
     * 批量删除员工
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTHrEmpByIds(String ids) throws Exception;


//    public int deleteTHrEmpById(Long empId) throws Exception;

    List<HrEmpVo> selectUserTree(HrEmp hrEmp);

    void updateEmpDeptAndPost(Long empId, Long jobTransferDeptId, Long jobTransferPostId,HttpServletRequest request);

    /**
     * 删除员工信息
     *
     * @param userIds 员工ID
     * @return 结果
     */
    int deleteTHrEmpByUserIds(Long[] userIds);

    String importUser(MultipartFile file, HttpServletRequest request) throws Exception;

    /**
     * 获取用户的钉钉userId ，因为配额限制 40/秒 ,不在导入员工是维护，需要用到时维护即可
     * @return
     */
    String getDingUserId(SysUser sysUser, boolean isSleep);
    /**
     * 通过钉钉userId获取用户，
     * @return
     */
    SysUser getSysUserByDingUserId(String operator, boolean isSleep);

    List<HrEmp> selectEmpList();

    /**
     * 查询所有在职，及当月离职，并且dingUserId不为空的员工数据
     * @return
     */
    List<Map<String,Object>> selectOnTheJobEmpList(int year, int month);

    /**
     * 查询所有在职，及当月离职的员工数据
     * @return
     */
    List<HrEmp> selectCountAttEmpList(int year, int month);

    List<HrEmp> selectOnTheEmpList();

    HrEmp selectTHrEmpByNumber(String empName);

    List<HrEmp> importPhotos(MultipartFile file);
}
