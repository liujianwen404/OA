package com.ruoyi.hr.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.framework.web.service.DictService;
import com.ruoyi.system.domain.SysDictData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrEmpTransfersMapper;
import com.ruoyi.base.domain.HrEmpTransfers;
import com.ruoyi.hr.service.IHrEmpTransfersService;
import com.ruoyi.common.core.text.Convert;

/**
 * 员工状态异动信息Service业务层处理
 * 
 * @author liujianwen
 * @date 2021-01-11
 */
@Service
public class HrEmpTransfersServiceImpl implements IHrEmpTransfersService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrEmpTransfersServiceImpl.class);

    @Autowired
    private HrEmpTransfersMapper hrEmpTransfersMapper;

    @Autowired
    private DictService dictService;

    /**
     * 查询员工状态异动信息
     * 
     * @param id 员工状态异动信息ID
     * @return 员工状态异动信息
     */
    @Override
    public HrEmpTransfers selectHrEmpTransfersById(Long id)
    {
        return hrEmpTransfersMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询员工状态异动信息列表
     * 
     * @param hrEmpTransfers 员工状态异动信息
     * @return 员工状态异动信息
     */
    @Override
    public List<HrEmpTransfers> selectHrEmpTransfersList(HrEmpTransfers hrEmpTransfers)
    {
        hrEmpTransfers.setDelFlag("0");
        return hrEmpTransfersMapper.selectHrEmpTransfersList(hrEmpTransfers);
    }

    /**
     * 新增员工状态异动信息
     * 
     * @param hrEmpTransfers 员工状态异动信息
     * @return 结果
     */
    @Override
    public int insertHrEmpTransfers(HrEmpTransfers hrEmpTransfers)
    {
        hrEmpTransfers.setCreateId(ShiroUtils.getUserId());
        hrEmpTransfers.setCreateBy(ShiroUtils.getLoginName());
        hrEmpTransfers.setCreateTime(DateUtils.getNowDate());
        return hrEmpTransfersMapper.insertSelective(hrEmpTransfers);
    }

    /**
     * 修改员工状态异动信息
     * 
     * @param hrEmpTransfers 员工状态异动信息
     * @return 结果
     */
    @Override
    public int updateHrEmpTransfers(HrEmpTransfers hrEmpTransfers)
    {
        hrEmpTransfers.setUpdateId(ShiroUtils.getUserId());
        hrEmpTransfers.setUpdateBy(ShiroUtils.getLoginName());
        hrEmpTransfers.setUpdateTime(DateUtils.getNowDate());
        return hrEmpTransfersMapper.updateByPrimaryKeySelective(hrEmpTransfers);
    }

    /**
     * 删除员工状态异动信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrEmpTransfersByIds(String ids)
    {
        return hrEmpTransfersMapper.deleteHrEmpTransfersByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除员工状态异动信息信息
     * 
     * @param id 员工状态异动信息ID
     * @return 结果
     */
    @Override
    public int deleteHrEmpTransfersById(Long id)
    {
        return hrEmpTransfersMapper.deleteHrEmpTransfersById(id);
    }



    @Override
    public HrEmpTransfers selectSingleOneByExample(Example example){
        return hrEmpTransfersMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<HrEmpTransfers> selectByExample(Example example){
        return hrEmpTransfersMapper.selectByExample(example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertEmpTransfers(HrEmp oldEmp, HrEmp nowEmp) {
        if (!nowEmp.getEmpStatus().equals(oldEmp.getEmpStatus())) {
            List<SysDictData> sysDictDatas = dictService.getType("sys_emp_status");
            if (sysDictDatas != null && !sysDictDatas.isEmpty()){
                HrEmpTransfers hrEmpTransfers = new HrEmpTransfers();
                hrEmpTransfers.setParentId(nowEmp.getEmpId());
                hrEmpTransfers.setTransferTime(DateUtils.getNowDate());

                List<String> beforeStatus = sysDictDatas.stream().filter(sysDictData -> String.valueOf(oldEmp.getEmpStatus()).equals(sysDictData.getDictValue()))
                        .map(SysDictData::getDictLabel).collect(Collectors.toList());
                hrEmpTransfers.setBeforeStatus(beforeStatus.get(0));

                List<String> afterStatus = sysDictDatas.stream().filter(sysDictData -> String.valueOf(nowEmp.getEmpStatus()).equals(sysDictData.getDictValue()))
                        .map(SysDictData::getDictLabel).collect(Collectors.toList());
                hrEmpTransfers.setAfterStatus(afterStatus.get(0));

                insertHrEmpTransfers(hrEmpTransfers);
            }
        }
    }

}
