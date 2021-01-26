package com.ruoyi.hr.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.THrLeaveInfoMapper;
import com.ruoyi.base.domain.THrLeaveInfo;
import com.ruoyi.hr.service.ITHrLeaveInfoService;
import com.ruoyi.common.core.text.Convert;

/**
 * 请假数据Service业务层处理
 * 
 * @author xt
 * @date 2020-09-10
 */
@Service
public class THrLeaveInfoServiceImpl implements ITHrLeaveInfoService 
{

    private static final Logger logger = LoggerFactory.getLogger(THrLeaveInfoServiceImpl.class);

    @Autowired
    private THrLeaveInfoMapper tHrLeaveInfoMapper;

    /**
     * 查询请假数据
     * 
     * @param id 请假数据ID
     * @return 请假数据
     */
    @Override
    public THrLeaveInfo selectTHrLeaveInfoById(Long id)
    {
        return tHrLeaveInfoMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询请假数据列表
     * 
     * @param tHrLeaveInfo 请假数据
     * @return 请假数据
     */
    @Override
    public List<THrLeaveInfo> selectTHrLeaveInfoList(THrLeaveInfo tHrLeaveInfo)
    {
        tHrLeaveInfo.setDelFlag("0");
        return tHrLeaveInfoMapper.selectTHrLeaveInfoList(tHrLeaveInfo);
    }

    /**
     * 新增请假数据
     * 
     * @param tHrLeaveInfo 请假数据
     * @return 结果
     */
    @Override
    public int insertTHrLeaveInfo(THrLeaveInfo tHrLeaveInfo)
    {
        tHrLeaveInfo.setCreateId(ShiroUtils.getUserId());
        tHrLeaveInfo.setCreateBy(ShiroUtils.getLoginName());
        tHrLeaveInfo.setCreateTime(DateUtils.getNowDate());
        return tHrLeaveInfoMapper.insertSelective(tHrLeaveInfo);
    }

    /**
     * 修改请假数据
     * 
     * @param tHrLeaveInfo 请假数据
     * @return 结果
     */
    @Override
    public int updateTHrLeaveInfo(THrLeaveInfo tHrLeaveInfo)
    {
        tHrLeaveInfo.setUpdateId(ShiroUtils.getUserId());
        tHrLeaveInfo.setUpdateBy(ShiroUtils.getLoginName());
        tHrLeaveInfo.setUpdateTime(DateUtils.getNowDate());
        return tHrLeaveInfoMapper.updateByPrimaryKeySelective(tHrLeaveInfo);
    }

    /**
     * 删除请假数据对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTHrLeaveInfoByIds(String ids)
    {
        return tHrLeaveInfoMapper.deleteTHrLeaveInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除请假数据信息
     * 
     * @param id 请假数据ID
     * @return 结果
     */
    @Override
    public int deleteTHrLeaveInfoById(Long id)
    {
        return tHrLeaveInfoMapper.deleteTHrLeaveInfoById(id);
    }



    @Override
    public THrLeaveInfo selectSingleOneByExample(Example example){
        return tHrLeaveInfoMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<THrLeaveInfo> selectByExample(Example example){
        return tHrLeaveInfoMapper.selectByExample(example);
    }

}
