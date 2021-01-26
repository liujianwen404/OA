package com.ruoyi.hr.service;

import java.io.IOException;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.domain.HrInterpolate;
import com.ruoyi.base.domain.VO.UserVO;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 内推申请Service接口
 * 
 * @author vivi07
 * @date 2020-05-12
 */
public interface IHrInterpolateService 
{
    /**
     * 查询内推申请
     * 
     * @param interpolateId 内推申请ID
     * @return 内推申请
     */
    public HrInterpolate selectHrInterpolateById(Long interpolateId);

    /**
     * 查询内推申请列表
     * 
     * @param hrInterpolate 内推申请
     * @return 内推申请集合
     */
    public List<HrInterpolate> selectHrInterpolateList(HrInterpolate hrInterpolate);

    /**
     * 新增内推申请
     * 
     * @param hrInterpolate 内推申请
     * @param multipart
     * @return 结果
     */
    public AjaxResult insertHrInterpolate(HrInterpolate hrInterpolate, MultipartFile multipart) throws IOException;

    /**
     * 修改内推申请
     * 
     * @param hrInterpolate 内推申请
     * @return 结果
     */
    public int updateHrInterpolate(HrInterpolate hrInterpolate);

    /**
     * 批量删除内推申请
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrInterpolateByIds(String ids);

    /**
     * 删除内推申请信息
     * 
     * @param interpolateId 内推申请ID
     * @return 结果
     */
    public int deleteHrInterpolateById(Long interpolateId);

    UserVO getInfoForAdd();

    void showVerifyDialog(String taskId, String module, String activitiId, ModelMap mmap, String instanceId);

    AjaxResult complete(String taskId, HttpServletRequest request, HrInterpolate hrInterpolate, String comment, String p_b_hrApproved);
}
