package com.ruoyi.hr.service.impl;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.entity.Example;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrAttendanceGroupSonMapper;
import com.ruoyi.base.domain.HrAttendanceGroupSon;
import com.ruoyi.hr.service.IHrAttendanceGroupSonService;
import com.ruoyi.common.core.text.Convert;

import javax.annotation.Resource;

/**
 * 考勤组排班子Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-10-29
 */
@Service
public class HrAttendanceGroupSonServiceImpl implements IHrAttendanceGroupSonService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrAttendanceGroupSonServiceImpl.class);

    @Resource
    private HrAttendanceGroupSonMapper hrAttendanceGroupSonMapper;

    /**
     * 查询考勤组排班子
     * 
     * @param id 考勤组排班子ID
     * @return 考勤组排班子
     */
    @Override
    public HrAttendanceGroupSon selectHrAttendanceGroupSonById(Long id)
    {
        return hrAttendanceGroupSonMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询考勤组排班子列表
     * 
     * @param hrAttendanceGroupSon 考勤组排班子
     * @return 考勤组排班子
     */
    @Override
    public List<HrAttendanceGroupSon> selectHrAttendanceGroupSonList(HrAttendanceGroupSon hrAttendanceGroupSon)
    {
        hrAttendanceGroupSon.setDelFlag("0");
        return hrAttendanceGroupSonMapper.selectHrAttendanceGroupSonList(hrAttendanceGroupSon);
    }

    /**
     * 新增考勤组排班子
     * 
     * @param hrAttendanceGroupSon 考勤组排班子
     * @return 结果
     */
    @Override
    public int insertHrAttendanceGroupSon(HrAttendanceGroupSon hrAttendanceGroupSon)
    {
        hrAttendanceGroupSon.setCreateId(ShiroUtils.getUserId());
        hrAttendanceGroupSon.setCreateBy(ShiroUtils.getLoginName());
        hrAttendanceGroupSon.setCreateTime(DateUtils.getNowDate());
        return hrAttendanceGroupSonMapper.insertSelective(hrAttendanceGroupSon);
    }

    /**
     * 修改考勤组排班子
     * 
     * @param hrAttendanceGroupSon 考勤组排班子
     * @return 结果
     */
    @Override
    public int updateHrAttendanceGroupSon(HrAttendanceGroupSon hrAttendanceGroupSon)
    {
        hrAttendanceGroupSon.setUpdateId(ShiroUtils.getUserId());
        hrAttendanceGroupSon.setUpdateBy(ShiroUtils.getLoginName());
        hrAttendanceGroupSon.setUpdateTime(DateUtils.getNowDate());
        String classId1 = hrAttendanceGroupSon.getClassId1();
        if(StringUtils.isBlank(classId1)){
            hrAttendanceGroupSon.builder().classId1(classId1)
                    .classId2(classId1).classId3(classId1)
                    .classId4(classId1).classId5(classId1)
                    .classId6(classId1).classId7(classId1)
                    .classId8(classId1).classId9(classId1)
                    .classId10(classId1).classId11(classId1)
                    .classId12(classId1).classId13(classId1)
                    .classId14(classId1).classId15(classId1)
                    .classId16(classId1).classId17(classId1)
                    .classId18(classId1).classId19(classId1)
                    .classId20(classId1).classId21(classId1)
                    .classId22(classId1).classId23(classId1)
                    .classId24(classId1).classId25(classId1)
                    .classId26(classId1).classId27(classId1)
                    .classId28(classId1).classId29(classId1)
                    .classId30(classId1).classId31(classId1)
                    .build();
            return hrAttendanceGroupSonMapper.updateByPrimaryKey(hrAttendanceGroupSon);
        }
        HrAttendanceGroupSon son = new HrAttendanceGroupSon().builder()
                                    .id(hrAttendanceGroupSon.getId()).classId1(hrAttendanceGroupSon.getClassId1())
                .classId2(hrAttendanceGroupSon.getClassId1()).classId3(hrAttendanceGroupSon.getClassId1())
                .classId4(hrAttendanceGroupSon.getClassId1()).classId5(hrAttendanceGroupSon.getClassId1())
                .classId6(hrAttendanceGroupSon.getClassId1()).classId7(hrAttendanceGroupSon.getClassId1())
                .classId8(hrAttendanceGroupSon.getClassId1()).classId9(hrAttendanceGroupSon.getClassId1())
                .classId10(hrAttendanceGroupSon.getClassId1()).classId11(hrAttendanceGroupSon.getClassId1())
                .classId12(hrAttendanceGroupSon.getClassId1()).classId13(hrAttendanceGroupSon.getClassId1())
                .classId14(hrAttendanceGroupSon.getClassId1()).classId15(hrAttendanceGroupSon.getClassId1())
                .classId16(hrAttendanceGroupSon.getClassId1()).classId17(hrAttendanceGroupSon.getClassId1())
                .classId18(hrAttendanceGroupSon.getClassId1()).classId19(hrAttendanceGroupSon.getClassId1())
                .classId20(hrAttendanceGroupSon.getClassId1()).classId21(hrAttendanceGroupSon.getClassId1())
                .classId22(hrAttendanceGroupSon.getClassId1()).classId23(hrAttendanceGroupSon.getClassId1())
                .classId24(hrAttendanceGroupSon.getClassId1()).classId25(hrAttendanceGroupSon.getClassId1())
                .classId26(hrAttendanceGroupSon.getClassId1()).classId27(hrAttendanceGroupSon.getClassId1())
                .classId28(hrAttendanceGroupSon.getClassId1()).classId29(hrAttendanceGroupSon.getClassId1())
                .classId30(hrAttendanceGroupSon.getClassId1()).classId31(hrAttendanceGroupSon.getClassId1())
                .build();
        son.setUpdateId(ShiroUtils.getUserId());
        son.setUpdateBy(ShiroUtils.getLoginName());
        son.setUpdateTime(DateUtils.getNowDate());
        return hrAttendanceGroupSonMapper.updateByPrimaryKeySelective(son);
    }

    @Override
    public int updateGroupSonOne(HrAttendanceGroupSon hrAttendanceGroupSon)
    {
        hrAttendanceGroupSon.setUpdateId(ShiroUtils.getUserId());
        hrAttendanceGroupSon.setUpdateBy(ShiroUtils.getLoginName());
        hrAttendanceGroupSon.setUpdateTime(DateUtils.getNowDate());
        return hrAttendanceGroupSonMapper.updateByPrimaryKeySelective(hrAttendanceGroupSon);
    }

    /**
     * 删除考勤组排班子对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceGroupSonByIds(String ids)
    {
        return hrAttendanceGroupSonMapper.deleteHrAttendanceGroupSonByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除考勤组排班子信息
     * 
     * @param id 考勤组排班子ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceGroupSonById(Long id)
    {
        return hrAttendanceGroupSonMapper.deleteHrAttendanceGroupSonById(id);
    }



    @Override
    public HrAttendanceGroupSon selectSingleOneByExample(Example example){
        return hrAttendanceGroupSonMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<HrAttendanceGroupSon> selectByExample(Example example){
        return hrAttendanceGroupSonMapper.selectByExample(example);
    }

    @Override
    public int insertList(List<HrAttendanceGroupSon> returnList) {
        return hrAttendanceGroupSonMapper.insertList(returnList);
    }

    @Override
    public int importHrAttendanceGroupSon(HrAttendanceGroupSon son) {
        son.setCreateId(ShiroUtils.getUserId());
        son.setCreateBy(ShiroUtils.getLoginName());
        son.setCreateTime(DateUtils.getNowDate());
        HrAttendanceGroupSon insertSon = transform(son);

        Long parentId = son.getParentId();
        String scheduDate = son.getScheduDate();
        Long empId = son.getEmpId();
        Example example = new Example(HrAttendanceGroupSon.class);
        example.createCriteria().andEqualTo("parentId",parentId).andEqualTo("empId",empId)
                .andEqualTo("scheduDate",scheduDate).andEqualTo("delFlag","0");
        HrAttendanceGroupSon oldSon = hrAttendanceGroupSonMapper.selectSingleOneByExample(example);

        if (oldSon == null) {
            return hrAttendanceGroupSonMapper.insert(insertSon);
        } else {
            insertSon.setDelFlag(oldSon.getDelFlag());
            insertSon.setId(oldSon.getId());
            insertSon.setCreateId(oldSon.getCreateId());
            insertSon.setCreateBy(oldSon.getCreateBy());
            insertSon.setUpdateId(ShiroUtils.getUserId());
            insertSon.setUpdateBy(ShiroUtils.getLoginName());
            insertSon.setUpdateTime(DateUtils.getNowDate());
            //只更新实体对象中不为空的字段
            return hrAttendanceGroupSonMapper.updateByPrimaryKey(insertSon);
        }
    }

    private HrAttendanceGroupSon transform(HrAttendanceGroupSon son) {
        HrAttendanceGroupSon returnSon = new HrAttendanceGroupSon();

        if(son.getParentId()!=null){
            returnSon.setParentId(son.getParentId());
        }if(son.getScheduDate()!=null){
            returnSon.setScheduDate(son.getScheduDate());
        }if(son.getEmpId()!=null){
            returnSon.setEmpId(son.getEmpId());
        }if(son.getEmpName()!=null){
            returnSon.setEmpName(son.getEmpName());
        }if(son.getClassId1()!=null){
            returnSon.setClassId1(son.getClassId1().split(":")[1]);
        }if(son.getClassId2()!=null){
            returnSon.setClassId2(son.getClassId2().split(":")[1]);
        }if(son.getClassId3()!=null){
            returnSon.setClassId3(son.getClassId3().split(":")[1]);
        }if(son.getClassId4()!=null){
            returnSon.setClassId4(son.getClassId4().split(":")[1]);
        }if(son.getClassId5()!=null){
            returnSon.setClassId5(son.getClassId5().split(":")[1]);
        }if(son.getClassId6()!=null){
            returnSon.setClassId6(son.getClassId6().split(":")[1]);
        }if(son.getClassId7()!=null){
            returnSon.setClassId7(son.getClassId7().split(":")[1]);
        }if(son.getClassId8()!=null){
            returnSon.setClassId8(son.getClassId8().split(":")[1]);
        }if(son.getClassId9()!=null){
            returnSon.setClassId9(son.getClassId9().split(":")[1]);
        }if(son.getClassId10()!=null){
            returnSon.setClassId10(son.getClassId10().split(":")[1]);
        }if(son.getClassId11()!=null){
            returnSon.setClassId11(son.getClassId11().split(":")[1]);
        }if(son.getClassId12()!=null){
            returnSon.setClassId12(son.getClassId12().split(":")[1]);
        }if(son.getClassId13()!=null){
            returnSon.setClassId13(son.getClassId13().split(":")[1]);
        }if(son.getClassId14()!=null){
            returnSon.setClassId14(son.getClassId14().split(":")[1]);
        }if(son.getClassId15()!=null){
            returnSon.setClassId15(son.getClassId15().split(":")[1]);
        }if(son.getClassId16()!=null){
            returnSon.setClassId16(son.getClassId16().split(":")[1]);
        }if(son.getClassId17()!=null){
            returnSon.setClassId17(son.getClassId17().split(":")[1]);
        }if(son.getClassId18()!=null){
            returnSon.setClassId18(son.getClassId18().split(":")[1]);
        }if(son.getClassId19()!=null){
            returnSon.setClassId19(son.getClassId19().split(":")[1]);
        }if(son.getClassId20()!=null){
            returnSon.setClassId20(son.getClassId20().split(":")[1]);
        }if(son.getClassId21()!=null){
            returnSon.setClassId21(son.getClassId21().split(":")[1]);
        }if(son.getClassId22()!=null){
            returnSon.setClassId22(son.getClassId22().split(":")[1]);
        }if(son.getClassId23()!=null){
            returnSon.setClassId23(son.getClassId23().split(":")[1]);
        }if(son.getClassId24()!=null){
            returnSon.setClassId24(son.getClassId24().split(":")[1]);
        }if(son.getClassId25()!=null){
            returnSon.setClassId25(son.getClassId25().split(":")[1]);
        }if(son.getClassId26()!=null){
            returnSon.setClassId26(son.getClassId26().split(":")[1]);
        }if(son.getClassId27()!=null){
            returnSon.setClassId27(son.getClassId27().split(":")[1]);
        }if(son.getClassId28()!=null){
            returnSon.setClassId28(son.getClassId28().split(":")[1]);
        }if(son.getClassId29()!=null){
            returnSon.setClassId29(son.getClassId29().split(":")[1]);
        }if(son.getClassId30()!=null){
            returnSon.setClassId30(son.getClassId30().split(":")[1]);
        }if(son.getClassId31()!=null){
            returnSon.setClassId31(son.getClassId31().split(":")[1]);
        }

        return returnSon;
    }

}
