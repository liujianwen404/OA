package com.ruoyi.hr.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.CopyToMapper;
import com.ruoyi.base.domain.CopyTo;
import com.ruoyi.hr.service.ICopyToService;
import com.ruoyi.common.core.text.Convert;

/**
 * 流程抄送关系Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-06-06
 */
@Service
public class CopyToServiceImpl implements ICopyToService 
{

    private static final Logger logger = LoggerFactory.getLogger(CopyToServiceImpl.class);

    @Autowired
    private CopyToMapper copyToMapper;

    /**
     * 查询流程抄送关系
     * 
     * @param id 流程抄送关系ID
     * @return 流程抄送关系
     */
    @Override
    public CopyTo selectCopyToById(Long id)
    {
        return copyToMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询流程抄送关系列表
     * 
     * @param copyTo 流程抄送关系
     * @return 流程抄送关系
     */
    @Override
    public List<CopyTo> selectCopyToList(CopyTo copyTo)
    {
        copyTo.setDelFlag("0");
        return copyToMapper.selectCopyToList(copyTo);
    }

    /**
     * 新增流程抄送关系
     * 
     * @param copyTo 流程抄送关系
     * @return 结果
     */
    @Override
    public int insertCopyTo(CopyTo copyTo)
    {
        copyTo.setCreateId(ShiroUtils.getUserId());
        copyTo.setCreateBy(ShiroUtils.getLoginName());
        copyTo.setCreateTime(DateUtils.getNowDate());
        return copyToMapper.insertSelective(copyTo);
    }

    /**
     * 修改流程抄送关系
     * 
     * @param copyTo 流程抄送关系
     * @return 结果
     */
    @Override
    public int updateCopyTo(CopyTo copyTo)
    {
        copyTo.setUpdateId(ShiroUtils.getUserId());
        copyTo.setUpdateBy(ShiroUtils.getLoginName());
        copyTo.setUpdateTime(DateUtils.getNowDate());
        return copyToMapper.updateByPrimaryKeySelective(copyTo);
    }

    /**
     * 删除流程抄送关系对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCopyToByIds(String ids)
    {
        return copyToMapper.deleteCopyToByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除流程抄送关系信息
     * 
     * @param id 流程抄送关系ID
     * @return 结果
     */
    @Override
    public int deleteCopyToById(Long id)
    {
        return copyToMapper.deleteCopyToById(id);
    }

    @Override
    public Long selectProcessCopyerCount(CopyTo copyTo) {
        return copyToMapper.selectProcessCopyerCount(copyTo);
    }
}
