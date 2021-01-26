package com.ruoyi.hr.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.TTrainingRecords;
import com.ruoyi.common.enums.ExamFlagEnum;
import com.ruoyi.common.enums.PassFlagEnum;
import com.ruoyi.common.enums.TeachingTypeEnum;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hr.mapper.HrEmpMapper;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.TTrainingRecordsMapper;
import com.ruoyi.hr.service.ITTrainingRecordsService;
import com.ruoyi.common.core.text.Convert;
import javax.annotation.Resource;

/**
 *
 * 
 * @author xt
 * @date 2021-01-04
 */
@Slf4j
@Service
public class TTrainingRecordsServiceImpl implements ITTrainingRecordsService 
{
    @Resource
    private HrEmpMapper hrEmpMapper;

    @Resource
    private TTrainingRecordsMapper tTrainingRecordsMapper;

    /**
     * 培训档案详情
     * 
     * @param id
     * @return
     */
    @Override
    public TTrainingRecords selectTTrainingRecordsById(Long id)
    {
        return tTrainingRecordsMapper.selectByPrimaryKey(id);
    }

    /**
     * 培训档案列表
     * 
     * @param tTrainingRecords
     * @return
     */
    @Override
    public List<TTrainingRecords> selectTTrainingRecordsList(TTrainingRecords tTrainingRecords)
    {
        tTrainingRecords.setDelFlag("0");
        List<TTrainingRecords> list =tTrainingRecordsMapper.selectTTrainingRecordsList(tTrainingRecords);
        list.stream().forEach(nico-> {
            nico.setExamFlagName(ExamFlagEnum.getValue(nico.getExamFlag()));
            nico.setPassFlagName(PassFlagEnum.getValue(nico.getPassFlag()));
            nico.setTrainingTypeName(TeachingTypeEnum.getValue(nico.getTrainingType()));
            nico.setParticipateTotal(tTrainingRecordsMapper.queryTrainingCount(nico.getEmpNum()));
            nico.setTrainingTimeTotal(tTrainingRecordsMapper.queryTrainingTimeSum(nico.getEmpNum()));
        });
        //条件查询不为空  分组查询
        if(StringUtils.isNotBlank(tTrainingRecords.getEmpNum()) || StringUtils.isNotBlank(tTrainingRecords.getEmpName())
                || Objects.nonNull(tTrainingRecords.getDeptId()) || Objects.nonNull(tTrainingRecords.getPostId())
                || StringUtils.isNotBlank(tTrainingRecords.getTrainingContent()) || Objects.nonNull(tTrainingRecords.getTrainingType())
                || Objects.nonNull(tTrainingRecords.getExamFlag()) || Objects.nonNull(tTrainingRecords.getPassFlag())
                || Objects.nonNull(tTrainingRecords.getStartLongTime()) || Objects.nonNull(tTrainingRecords.getEndLongTime())
                || Objects.nonNull(tTrainingRecords.getStartScore()) || Objects.nonNull(tTrainingRecords.getEndScore())
                || Objects.nonNull(tTrainingRecords.getTrainingStartTime()) || Objects.nonNull(tTrainingRecords.getTrainingEndTime())
        ){
            List<TTrainingRecords> lists =new ArrayList<>();
            Map<String,List<TTrainingRecords>> listMap=list.stream().sorted(Comparator.comparing(TTrainingRecords::getTrainingStartTime)).collect(Collectors.groupingBy(b -> b.getEmpName(),Collectors.toList()));
            listMap.forEach((k,v)->{
                lists.addAll(v);
            });
            return lists;
         }
        return list.stream().sorted(Comparator.comparing(TTrainingRecords::getTrainingStartTime).reversed()).collect(Collectors.toList());
    }

    /**
     * 新增培训档案
     * 
     * @param tTrainingRecords
     * @return 结果
     */
    @Override
    public int insertTTrainingRecords(TTrainingRecords tTrainingRecords)
    {

        HrEmp hrEmp=hrEmpMapper.selectHrEmpByEmpNum(tTrainingRecords.getEmpNum());
        if(Objects.isNull(hrEmp)){
            throw new BusinessException("工号不存在,请重新输入");
        }
        if(tTrainingRecords.getTrainingStartTime().after(tTrainingRecords.getTrainingEndTime())) {
            throw new BusinessException("培训开始时间不能大于结束时间");
        }
        tTrainingRecords.setEmpName(hrEmp.getEmpName());
        tTrainingRecords.setPostId(hrEmp.getPostId());
        tTrainingRecords.setDeptId(hrEmp.getDeptId());
        tTrainingRecords.setCreateId(ShiroUtils.getUserId());
        tTrainingRecords.setCreateBy(ShiroUtils.getLoginName());
        tTrainingRecords.setCreateTime(DateUtils.getNowDate());
        return tTrainingRecordsMapper.insertSelective(tTrainingRecords);
    }

    /**
     * 修改培训档案
     * 
     * @param tTrainingRecords
     * @return 结果
     */
    @Override
    public int updateTTrainingRecords(TTrainingRecords tTrainingRecords)
    {
        tTrainingRecords.setUpdateId(ShiroUtils.getUserId());
        tTrainingRecords.setUpdateBy(ShiroUtils.getLoginName());
        tTrainingRecords.setUpdateTime(DateUtils.getNowDate());
        return tTrainingRecordsMapper.updateByPrimaryKeySelective(tTrainingRecords);
    }

    /**
     * 删除培训档案
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTTrainingRecordsByIds(String ids)
    {
        return tTrainingRecordsMapper.deleteTTrainingRecordsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除培训档案信息
     * 
     * @param id
     * @return 结果
     */
    @Override
    public int deleteTTrainingRecordsById(Long id)
    {
        return tTrainingRecordsMapper.deleteTTrainingRecordsById(id);
    }



    @Override
    public TTrainingRecords selectSingleOneByExample(Example example){
        return tTrainingRecordsMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<TTrainingRecords> selectByExample(Example example){
        return tTrainingRecordsMapper.selectByExample(example);
    }

}
