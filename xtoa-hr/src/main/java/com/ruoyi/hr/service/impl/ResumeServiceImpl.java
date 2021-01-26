package com.ruoyi.hr.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.base.domain.Resume;
import com.ruoyi.hr.mapper.ResumeMapper;
import com.ruoyi.hr.service.IResumeService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.impl.SysUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 员工Service业务层处理
 * 
 * @author vivi07
 * @date 2020-04-04
 */
@Service
public class ResumeServiceImpl implements IResumeService
{
    private static final Logger log = LoggerFactory.getLogger(ResumeServiceImpl.class);

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private ISysConfigService configService;

    /**
     * 查询员工
     * 
     * @param id 员工ID
     * @return 员工
     */
    @Override
    public Resume selectResumeById(Long id)
    {
        return resumeMapper.selectResumeById(id);
    }

    /**
     * 查询员工列表
     * 
     * @param resume 员工
     * @return 员工
     */
    @Override
    public List<Resume> selectResumeList(Resume resume)
    {
        return resumeMapper.selectResumeList(resume);
    }

    /**
     * 新增员工
     * 
     * @param resume 员工
     * @return 结果
     */
    @Override
    public int insertResume(Resume resume)
    {
        resume.setCreateTime(DateUtils.getNowDate());
        int r =  resumeMapper.insertResume(resume);
        return r;
    }

    /**
     * 修改员工
     * 
     * @param resume 员工
     * @return 结果
     */
    @Override
    public int updateResume(Resume resume)
    {
        resume.setUpdateTime(DateUtils.getNowDate());
        int r =  resumeMapper.updateResume(resume);
        return r;
    }

    /**
     * 删除员工对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteResumeByIds(String ids)
    {
        return resumeMapper.deleteResumeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除员工信息
     * 
     * @param id 员工ID
     * @return 结果
     */
    @Override
    public int deleteResumeById(Long id)
    {
        return resumeMapper.deleteResumeById(id);
    }

	@Override
	public int invalidResume(Resume resume) {
		return resumeMapper.invalidResume(resume);
	}

    /**
     * 导入简历数据
     *
     * @param resumeList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importResume(List<Resume> resumeList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(resumeList) || resumeList.size() == 0)
        {
            throw new BusinessException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (Resume resume : resumeList)
        {
            try
            {
               if (isUpdateSupport)
                {
                    resume.setUpdateBy(operName);
                    this.updateResume(resume);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + resume.getName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + resume.getName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + resume.getName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
