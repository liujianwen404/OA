package com.ruoyi.hr.service.impl;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.base.domain.DTO.HolidayDTO;
import com.ruoyi.base.domain.DTO.HolidayDTOError;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.utils.HoildayAndLeaveType;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HolidayMapper;
import com.ruoyi.base.domain.Holiday;
import com.ruoyi.hr.service.IHolidayService;
import com.ruoyi.common.core.text.Convert;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;

/**
 * 员工假期Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-06-05
 */
@Service
public class HolidayServiceImpl implements IHolidayService 
{

    private static final Logger logger = LoggerFactory.getLogger(HolidayServiceImpl.class);

    @Resource
    private HolidayMapper holidayMapper;

    @Autowired
    private HrEmpService tHrEmpService;

    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysPostService sysPostService;

    /**
     * 查询员工假期
     * 
     * @param id 员工假期ID
     * @return 员工假期
     */
    @Override
    public Holiday selectHolidayById(Long id)
    {
        return holidayMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询员工假期列表
     * 
     * @param holiday 员工假期
     * @return 员工假期
     */
    @Override
    @DataScope(deptAlias = "d", menuAlias = "hr:holiday:view")
    public List<Holiday> selectHolidayList(Holiday holiday)
    {
        holiday.setDelFlag("0");
        List<Holiday> holidays = holidayMapper.selectHolidayList(holiday);
        HrEmp hrEmp = null;
        SysPost sysPost = null;
        SysDept sysDept = null;
        for (Holiday hd : holidays) {
            hrEmp = tHrEmpService.selectTHrEmpById(hd.getEmpId());
            if (hrEmp != null){
                sysPost = sysPostService.selectPostById(hrEmp.getPostId());
                if (sysPost != null){
                    hd.setPostName(sysPost.getPostName());
                }

                sysDept = sysDeptService.selectDeptById(hrEmp.getDeptId());
                if (sysDept != null){
                    hd.setDeptName(sysDept.getShowName());
                }

                hd.setTypeStr(HoildayAndLeaveType.getHoildayAndLeaveStr(hd.getType()));

                hd.setAvailableTime(hd.getHours() - hd.getUseHours());

            }
        }
        return holidays;
    }

    /**
     * 新增员工假期
     * 
     * @param holiday 员工假期
     * @return 结果
     */
    @Override
    public int insertHoliday(Holiday holiday)
    {

        return holidayMapper.insertSelective(holiday);
    }

    /**
     * 修改员工假期
     * 
     * @param holiday 员工假期
     * @return 结果
     */
    @Override
    public int updateHoliday(Holiday holiday)
    {
        holiday.setUpdateId(ShiroUtils.getUserId());
        holiday.setUpdateBy(ShiroUtils.getLoginName());
        holiday.setUpdateTime(DateUtils.getNowDate());
        return holidayMapper.updateByPrimaryKeySelective(holiday);
    }

    /**
     * 删除员工假期对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHolidayByIds(String ids)
    {
        return holidayMapper.deleteHolidayByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除员工假期信息
     * 
     * @param id 员工假期ID
     * @return 结果
     */
    @Override
    public int deleteHolidayById(Long id)
    {
        return holidayMapper.deleteHolidayById(id);
    }

    @Override
    public int deleteHolidayByIdClassInfoId(Long classInfoId) {
        return holidayMapper.deleteHolidayByIdClassInfoId(classInfoId);
    }

    @Override
    public int initHoliday(int days) {
        List<HrEmp> empList = tHrEmpService.selectEmpList();
        List<Holiday> holidayList = empList.parallelStream().map(emp -> {
            Date nonManagerDate = emp.getNonManagerDate();
            String nonManagerStr = DateUtil.format(nonManagerDate,"yyyy-MM-dd");
            String monthAndDay = nonManagerStr.substring(5,10);
            int i = DateUtil.ageOfNow(nonManagerDate);
            int year = DateUtil.year(DateUtil.date());
            String startYMD = year + "-" + monthAndDay;
            String endYMD = (year + 1) + "-" + monthAndDay;
            Date startDate = DateUtil.parse(startYMD);
            Date endDate = DateUtil.parse(endYMD);
            if(i >= 1){
                Holiday holiday = new Holiday().builder().empId(emp.getEmpId())
                        .empName(emp.getEmpName())
                        .type("4")
                        .hours(Double.valueOf(days*8))
                        .useHours(0d)
                        .startDate(startDate)
                        .endDate(endDate)
                        .source("初始化"+year+"年假")
                        .build();
                holiday.setCreateId(ShiroUtils.getUserId());
                holiday.setCreateBy(ShiroUtils.getUserName());
                holiday.setDelFlag("0");
                return holiday;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        if(holidayList != null){
            holidayList.forEach(holiday->{
                Example example = new Example(Holiday.class);
                example.createCriteria().andEqualTo("empId",holiday.getEmpId())
                        .andEqualTo("type",holiday.getType())
                        .andEqualTo("startDate",holiday.getStartDate());
                Holiday oldHoliday = holidayMapper.selectSingleOneByExample(example);
                if(oldHoliday == null){
                    holidayMapper.insertSelective(holiday);
                }else {
                    holiday.setId(oldHoliday.getId());
                    holidayMapper.updateByPrimaryKeySelective(holiday);
                }
            });
            return 1;
        }
        return 0;
    }

    public static String holidayType1 = "holidayType1";
    public static String holidayType4 = "holidayType4";

    @Override
    public Map<String, Double> getHolidayHours(Long userId) {
        Map<String, Double> holidayHours = holidayMapper.getHolidayHours(userId);
        if (holidayHours == null){
            holidayHours = new HashMap<>();
            holidayHours.put(holidayType1,0D);
            holidayHours.put(holidayType4,0D);
        }
        return holidayHours;
    }

    @Override
    public List<Holiday> selectHolidayType1ByUserId(Long userId, String type) {
        return holidayMapper.selectHolidayType1ByUserId(userId,type);
    }

    @Override
    public Double selectRemainingAnuualLeave(Long empId, int year) {
        return holidayMapper.selectRemainingAnuualLeave(empId,year);
    }

    @Override
    public Double selectPublicOvertime(Long empId, int year, int month) {
        return holidayMapper.selectPublicOvertime(empId,year,month);
    }

    @Override
    public Double selectGeneralOvertime(Long empId, int year, int month) {
        return holidayMapper.selectGeneralOvertime(empId,year,month);
    }

    @Override
    public Double selectLegalOvertime(Long empId, int year, int month) {
        return holidayMapper.selectLegalOvertime(empId,year,month);
    }

    @Override
    public void insertSalaryStructureDTO(HolidayDTO holidayDTO, AtomicInteger successCount, List<HolidayDTOError> errorList) {

        String startDateStr = holidayDTO.getStartDate();
        String endDateStr = holidayDTO.getEndDate();

        Date startDate = null;
        if (StringUtil.isNotEmpty(startDateStr)){
            if (startDateStr.equals("不限")){
                startDate = DateUtil.parseDate("2018-01-01 00:00:00");
            }else {
                try {
                    startDate = DateUtil.parseDate(startDateStr);
                }catch (Exception e){
                    addErrorDTO(holidayDTO, errorList, "无效的开始时间");
                    return;
                }
            }
        }


        Date endDate = null;
        if (StringUtil.isNotEmpty(endDateStr)){
            if (endDateStr.equals("不限")){
                endDate = DateUtil.parseDate("2118-01-01 00:00:00");
            }else {
                try {
                    endDate = DateUtil.parseDate(endDateStr);
                }catch (Exception e){
                    addErrorDTO(holidayDTO, errorList, "无效的结束时间");
                    return;
                }
            }
        }



        if (StringUtil.isEmpty(holidayDTO.getEmpNum()) ||
                StringUtil.isEmpty(holidayDTO.getTypeStr()) || holidayDTO.getHours() == null
                || startDate == null || endDate == null ){
            addErrorDTO(holidayDTO, errorList, "不能存在空的数据");
            return;
        }
        String hoildayAndLeaveType = HoildayAndLeaveType.getHoildayAndLeaveType(holidayDTO.getTypeStr());
        try {
            Integer type = Integer.parseInt(hoildayAndLeaveType);
        }catch (Exception e){
            addErrorDTO(holidayDTO, errorList, "无效的假期类型");
            return;
        }


        if (holidayDTO.getHours().compareTo(0.5D) < 0 ){
            addErrorDTO(holidayDTO, errorList, "时长不能小于半小时");
            return;
        }



        HrEmp hrEmp = tHrEmpService.selectTHrEmpByNumber(holidayDTO.getEmpNum());
        if (hrEmp != null){
            holidayDTO.setEmpId(hrEmp.getEmpId());

            Holiday holiday = new Holiday();
            holiday.setEmpId(holidayDTO.getEmpId());
            holiday.setHours(holidayDTO.getHours());
            holiday.setEmpName(hrEmp.getEmpName());
            holiday.setUserId(hrEmp.getUserId());
            holiday.setType(hoildayAndLeaveType);
            holiday.setStartDate(startDate);
            holiday.setEndDate(endDate);

            List<Holiday> select = holidayMapper.select(holiday);
            if (select == null || select.isEmpty()){
                holiday.setEmpName(hrEmp.getEmpName());
                holiday.setAttendanceDate(holiday.getStartDate());
                holiday.setRemark("导入假期数据");
                int i = holidayMapper.insertSelective(holiday);
                if (i > 0){
                    //记录一条成功数据
                    successCount.incrementAndGet();
                }else {
                    addErrorDTO(holidayDTO, errorList, "数据库插入失败，请重新上传");
                }
            }else {
                addErrorDTO(holidayDTO, errorList, "重复的数据，无需重复上传，请确认");
            }
        }else {
            addErrorDTO(holidayDTO, errorList, "找不到员工数据");
        }
    }

    private void addErrorDTO(HolidayDTO holidayDTO, List<HolidayDTOError> errorList, String remark) {
        HolidayDTOError holidayDTOError = new HolidayDTOError();
        BeanUtil.copyProperties(holidayDTO, holidayDTOError);
        holidayDTOError.setErrorInfo(remark);
        errorList.add(holidayDTOError);
    }
}
