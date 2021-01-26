package com.ruoyi.hr.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.domain.HrInterpolate;
import com.ruoyi.hr.domain.HrNonManager;
import com.ruoyi.base.domain.HrRecruit;
import com.ruoyi.base.domain.VO.HrRecruitVO;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hr.mapper.HrRecruitMapper;
import com.ruoyi.hr.service.HrRecruitService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 招聘申请Service业务层处理
 * 
 * @author cmw
 * @date 2020-05-11
 */
@Service
public class HrRecruitServiceImpl implements HrRecruitService
{
    @Autowired
    private HrRecruitMapper hrRecruitMapper;
    @Autowired
    private ISysDeptService iSysDeptService;

    /**
     * 查询招聘申请
     * 
     * @param recruitId 招聘申请ID
     * @return 招聘申请
     */
    @Override
    public HrRecruit selectTHrRecruitById(Long recruitId)
    {
        return hrRecruitMapper.selectTHrRecruitById(recruitId);
    }

    /**
     * 查询招聘申请列表
     * 
     * @param hrRecruit 招聘申请
     * @return 招聘申请
     */
    @Override
    @DataScope(deptAlias = "d", menuAlias = "hr:report:recruit:view")
    public List<HrRecruit> selectTHrRecruitList(HrRecruit hrRecruit)
    {
        hrRecruit.setDelFlag("0");
        return hrRecruitMapper.selectTHrRecruitList(hrRecruit);
    }

    /**
     * 新增招聘申请
     * 
     * @param hrRecruit 招聘申请
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTHrRecruit(HrRecruit hrRecruit)
    {
        hrRecruit.setCreateTime(DateUtils.getNowDate());
        hrRecruit.setCreateId(ShiroUtils.getUserId());
        hrRecruit.setCreateBy(ShiroUtils.getLoginName());
        return hrRecruitMapper.insertTHrRecruit(hrRecruit);
    }

    /**
     * 修改招聘申请
     * 
     * @param hrRecruit 招聘申请
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTHrRecruit(HrRecruit hrRecruit)
    {
        hrRecruit.setUpdateTime(DateUtils.getNowDate());
        hrRecruit.setUpdateId(ShiroUtils.getUserId());
        hrRecruit.setUpdateBy(ShiroUtils.getLoginName());
        return hrRecruitMapper.updateTHrRecruit(hrRecruit);
    }

    /**
     * 删除招聘申请对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTHrRecruitByIds(String ids)
    {
        return hrRecruitMapper.deleteTHrRecruitByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除招聘申请信息
     * 
     * @param recruitId 招聘申请ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTHrRecruitById(Long recruitId)
    {
        return hrRecruitMapper.deleteTHrRecruitById(recruitId);
    }

    @Override
    @Transactional
    public AjaxResult submitRecruit(Long recruitId) {
        HrRecruit hrRecruit = hrRecruitMapper.selectTHrRecruitById(recruitId);
        if (hrRecruit == null || !hrRecruit.getDelFlag().equals("0")){
            return AjaxResult.error("数据不存在！");
        }

        hrRecruit.setReleaseStatus(1);
        hrRecruit.setUpdateTime(DateUtils.getNowDate());
        hrRecruit.setUpdateId(ShiroUtils.getUserId());
        hrRecruit.setUpdateBy(ShiroUtils.getLoginName());
        hrRecruitMapper.updateTHrRecruit(hrRecruit);

        return AjaxResult.success();
    }

    @Override
    public List<HrRecruitVO> findAllInfo() {
        SysUser sysUser = ShiroUtils.getSysUser();
        Long deptId = sysUser.getDeptId();
        Integer hrFlag = sysUser.hrFlag();
        List<HrRecruitVO> allInfo = hrRecruitMapper.findAllInfo(hrFlag,deptId);

        for (HrRecruitVO hrRecruitVO : allInfo) {
            SysDept sysDept = iSysDeptService.selectDeptById(hrRecruitVO.getRecruitDeptId());
            if (sysDept != null && sysDept.getParentId() != null){
                SysDept parentDept = iSysDeptService.selectDeptById(sysDept.getParentId());
                if (parentDept != null){
                    hrRecruitVO.setDeptName( parentDept.getDeptName() + ":" + hrRecruitVO.getDeptName());
                }
            }

        }

        return allInfo;
    }

    @Override
    public Integer findAllInfoCount() {

        SysUser sysUser = ShiroUtils.getSysUser();
        Long deptId = sysUser.getDeptId();
        Integer hrFlag = sysUser.hrFlag();
        return hrRecruitMapper.findAllInfoCount(hrFlag,deptId);
    }

    @Override
    public void updateRecruitCountByHrNonManager(HrNonManager hrNonManager) {
        HrRecruit hrRecruit = hrRecruitMapper.selectTHrRecruitByHrNonManager(hrNonManager);
        if (hrRecruit != null){
            hrRecruit.setNonManagerCount(hrRecruit.getNonManagerCount()+1);
            hrRecruit.setAwaitCount(hrRecruit.getNonManagerCount() - 1);

            if (hrRecruit.getNonManagerCount() >= hrRecruit.getRecruitCount()){
                hrRecruit.setNonManagerCount(hrRecruit.getRecruitCount());
            }

            if (hrRecruit.getAwaitCount() <= 0){
                hrRecruit.setAwaitCount(0);
            }

            hrRecruitMapper.updateTHrRecruit(hrRecruit);
        }
    }

    @Override
    public void updateRecruitCountByHrInterpolate(HrInterpolate hrInterpolate) {
        HrRecruit hrRecruit = hrRecruitMapper.updateRecruitCountByHrInterpolate(hrInterpolate);
        if (hrRecruit != null){
            hrRecruit.setAwaitCount(hrRecruit.getNonManagerCount() + 1);

            if (hrRecruit.getAwaitCount() <= 0){
                hrRecruit.setAwaitCount(0);
            }

            hrRecruitMapper.updateTHrRecruit(hrRecruit);
        }
    }
}
