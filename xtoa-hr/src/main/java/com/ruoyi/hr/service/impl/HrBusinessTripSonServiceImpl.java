package com.ruoyi.hr.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrBusinessTripSonMapper;
import com.ruoyi.base.domain.HrBusinessTripSon;
import com.ruoyi.hr.service.IHrBusinessTripSonService;
import com.ruoyi.common.core.text.Convert;

/**
 * 出差申请子Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-07-01
 */
@Service
public class HrBusinessTripSonServiceImpl implements IHrBusinessTripSonService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrBusinessTripSonServiceImpl.class);

    @Autowired
    private HrBusinessTripSonMapper hrBusinessTripSonMapper;

    /**
     * 查询出差申请子
     * 
     * @param id 出差申请子ID
     * @return 出差申请子
     */
    @Override
    public HrBusinessTripSon selectHrBusinessTripSonById(Long id)
    {
        return hrBusinessTripSonMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询出差申请子列表
     * 
     * @param hrBusinessTripSon 出差申请子
     * @return 出差申请子
     */
    @Override
    public List<HrBusinessTripSon> selectHrBusinessTripSonList(HrBusinessTripSon hrBusinessTripSon)
    {
        hrBusinessTripSon.setDelFlag("0");
        return hrBusinessTripSonMapper.selectHrBusinessTripSonList(hrBusinessTripSon);
    }

    /**
     * 新增出差申请子
     * 
     * @param hrBusinessTripSon 出差申请子
     * @return 结果
     */
    @Override
    public int insertHrBusinessTripSon(HrBusinessTripSon hrBusinessTripSon)
    {
        hrBusinessTripSon.setCreateId(ShiroUtils.getUserId());
        hrBusinessTripSon.setCreateBy(ShiroUtils.getLoginName());
        hrBusinessTripSon.setCreateTime(DateUtils.getNowDate());
        return hrBusinessTripSonMapper.insertSelective(hrBusinessTripSon);
    }

    /**
     * 修改出差申请子
     * 
     * @param hrBusinessTripSon 出差申请子
     * @return 结果
     */
    @Override
    public int updateHrBusinessTripSon(HrBusinessTripSon hrBusinessTripSon)
    {
        hrBusinessTripSon.setUpdateId(ShiroUtils.getUserId());
        hrBusinessTripSon.setUpdateBy(ShiroUtils.getLoginName());
        hrBusinessTripSon.setUpdateTime(DateUtils.getNowDate());
        return hrBusinessTripSonMapper.updateByPrimaryKeySelective(hrBusinessTripSon);
    }

    /**
     * 删除出差申请子对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrBusinessTripSonByIds(String ids)
    {
        return hrBusinessTripSonMapper.deleteHrBusinessTripSonByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除出差申请子信息
     * 
     * @param id 出差申请子ID
     * @return 结果
     */
    @Override
    public int deleteHrBusinessTripSonById(Long id)
    {
        return hrBusinessTripSonMapper.deleteHrBusinessTripSonById(id);
    }
}
