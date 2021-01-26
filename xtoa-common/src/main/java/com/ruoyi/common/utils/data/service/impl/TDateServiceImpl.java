package com.ruoyi.common.utils.data.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.data.domain.TDate;
import com.ruoyi.common.utils.data.mapper.TDateMapper;
import com.ruoyi.common.utils.data.service.ITDateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.ruoyi.common.core.text.Convert;

/**
 * 日期Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-05-19
 */
@Service
public class TDateServiceImpl implements ITDateService
{

    private static final Logger logger = LoggerFactory.getLogger(TDateServiceImpl.class);

    @Autowired
    private TDateMapper tDateMapper;

    /**
     * 查询日期
     * 
     * @param id 日期ID
     * @return 日期
     */
    @Override
    public TDate selectTDateById(Long id)
    {
        return tDateMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询日期列表
     * 
     * @param tDate 日期
     * @return 日期
     */
    @Override
    public List<TDate> selectTDateList(TDate tDate)
    {
        tDate.setDelFlag("0");
        return tDateMapper.selectTDateList(tDate);
    }

    @Override
    public int insertTDate(TDate tDate) {
        return 0;
    }

    @Override
    public int updateTDate(TDate tDate) {
        return tDateMapper.updateByPrimaryKey(tDate);
    }


    /**
     * 删除日期对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTDateByIds(String ids)
    {
        return tDateMapper.deleteTDateByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除日期信息
     * 
     * @param id 日期ID
     * @return 结果
     */
    @Override
    public int deleteTDateById(Long id)
    {
        return tDateMapper.deleteTDateById(id);
    }

    @Override
    public int selectDateCounts(Date startTime, Date endTime) {
        return tDateMapper.selectDateCounts(startTime,endTime);
    }

    @Override
    public int changeStatus(TDate tDate) {
        return tDateMapper.changeStatus(tDate);
    }

    @Override
    public Double selectAllLegalPublicHoliday(int year, int month) {
        return tDateMapper.selectAllLegalPublicHoliday(year,month);
    }

    @Override
    public Boolean selectIsLegalDay(String day) {
        int num = tDateMapper.selectIsLegalDay(day);
        if(num > 0){
            return true;
        }
        return false;
    }
}
