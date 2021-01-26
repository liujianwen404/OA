package com.ruoyi.hr.service.impl;

import java.beans.Transient;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.base.domain.HrAttendanceInfo;
import com.ruoyi.common.config.Global;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.data.domain.TDate;
import com.ruoyi.common.utils.data.service.ITDateService;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.base.domain.HrAttendanceGroupSon;
import com.ruoyi.hr.mapper.HrAttendanceGroupSonMapper;
import com.ruoyi.hr.mapper.HrEmpMapper;
import com.ruoyi.hr.service.IHrAttendanceClassService;
import com.ruoyi.hr.service.IHrAttendanceGroupSonService;
import com.ruoyi.hr.service.IHrAttendanceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.HrAttendanceGroupMapper;
import com.ruoyi.base.domain.HrAttendanceGroup;
import com.ruoyi.hr.service.IHrAttendanceGroupService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * 考勤组Service业务层处理
 * 
 * @author liujianwen
 * @date 2020-07-29
 */
@Service
public class HrAttendanceGroupServiceImpl implements IHrAttendanceGroupService 
{

    private static final Logger logger = LoggerFactory.getLogger(HrAttendanceGroupServiceImpl.class);

    @Resource
    private HrAttendanceGroupMapper hrAttendanceGroupMapper;

    @Resource
    private HrAttendanceGroupSonMapper hrAttendanceGroupSonMapper;

    @Resource
    private HrEmpMapper HrEmpMapper;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private IHrAttendanceClassService hrAttendanceClassService;

    @Resource
    private IHrAttendanceInfoService hrAttendanceInfoService;

    @Autowired
    private ITDateService dateService;

    /**
     * 查询考勤组
     * 
     * @param id 考勤组ID
     * @return 考勤组
     */
    @Override
    public HrAttendanceGroup selectHrAttendanceGroupById(Long id)
    {
        return hrAttendanceGroupMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询考勤组列表
     * 
     * @param hrAttendanceGroup 考勤组
     * @return 考勤组
     */
    @Override
    public List<HrAttendanceGroup> selectHrAttendanceGroupList(HrAttendanceGroup hrAttendanceGroup)
    {
        hrAttendanceGroup.setDelFlag("0");
        return hrAttendanceGroupMapper.selectHrAttendanceGroupList(hrAttendanceGroup);
    }

    /**
     * 新增考勤组
     * 
     * @param hrAttendanceGroup 考勤组
     * @return 结果
     */
    @Override
    @Transactional
    public int insertHrAttendanceGroup(HrAttendanceGroup hrAttendanceGroup)
    {
        hrAttendanceGroup.setCreateId(ShiroUtils.getUserId());
        hrAttendanceGroup.setCreateBy(ShiroUtils.getLoginName());
        hrAttendanceGroup.setCreateTime(DateUtils.getNowDate());
        int num = hrAttendanceGroupMapper.insertSelective(hrAttendanceGroup);
        //如果是排班制
        if("1".equals(hrAttendanceGroup.getType())){
            String scheduDate = DateUtils.dateTimeNow("yyyy-MM");
            String[] empIds = hrAttendanceGroup.getEmpId().split(",");
            String[] empNames = hrAttendanceGroup.getEmpName().split(",");
            List<HrAttendanceGroupSon> groupSons = new ArrayList<>();
            for(int i=0;i<empIds.length;i++){
                HrAttendanceGroupSon groupSon = new HrAttendanceGroupSon();
                groupSon.setCreateId(ShiroUtils.getUserId());
                groupSon.setCreateBy(ShiroUtils.getLoginName());
                groupSon.setCreateTime(DateUtils.getNowDate());
                groupSon.setEmpId(Long.valueOf(empIds[i]));
                groupSon.setEmpName(empNames[i]);
                groupSon.setParentId(hrAttendanceGroup.getId());
                //默认存储当前年月
                groupSon.setScheduDate(scheduDate);
                groupSon.setDelFlag("0");
                groupSons.add(groupSon);
            }
            hrAttendanceGroupSonMapper.insertList(groupSons);
        }
        return num;
    }

    /**
     * 修改考勤组
     * 
     * @param hrAttendanceGroup 考勤组
     * @return 结果
     */
    @Override
    public int updateHrAttendanceGroup(HrAttendanceGroup hrAttendanceGroup)
    {
        hrAttendanceGroup.setUpdateId(ShiroUtils.getUserId());
        hrAttendanceGroup.setUpdateBy(ShiroUtils.getLoginName());
        hrAttendanceGroup.setUpdateTime(DateUtils.getNowDate());
        //如果是排班制
        if("1".equals(hrAttendanceGroup.getType())){
            HrAttendanceGroupSon hrAttendanceGroupSon = new HrAttendanceGroupSon();
            //先删除子表所有parentId为当前考勤组id的字段
            hrAttendanceGroupSon.setUpdateId(ShiroUtils.getUserId());
            hrAttendanceGroupSon.setUpdateBy(ShiroUtils.getLoginName());
            hrAttendanceGroupSon.setUpdateTime(DateUtils.getNowDate());
            hrAttendanceGroupSon.setDelFlag("1");
            Example example = new Example(HrAttendanceGroupSon.class);
            example.createCriteria().andEqualTo("parentId",hrAttendanceGroup.getId())
                    .andEqualTo("delFlag","0");
            hrAttendanceGroupSonMapper.updateByExampleSelective(hrAttendanceGroupSon,example);

            //往子表中插入新的数据
            String[] empIds = hrAttendanceGroup.getEmpId().split(",");
            String[] empNames = hrAttendanceGroup.getEmpName().split(",");
            List<HrAttendanceGroupSon> groupSons = new ArrayList<>();
            for(int i=0;i<empIds.length;i++){
                HrAttendanceGroupSon groupSon = new HrAttendanceGroupSon();
                groupSon.setCreateId(ShiroUtils.getUserId());
                groupSon.setCreateBy(ShiroUtils.getLoginName());
                groupSon.setCreateTime(DateUtils.getNowDate());
                groupSon.setEmpId(Long.valueOf(empIds[i]));
                groupSon.setEmpName(empNames[i]);
                groupSon.setParentId(hrAttendanceGroup.getId());
                groupSon.setDelFlag("0");
                groupSons.add(groupSon);
            }
            hrAttendanceGroupSonMapper.insertList(groupSons);
        }
        return hrAttendanceGroupMapper.updateByPrimaryKeySelective(hrAttendanceGroup);
    }

    /**
     * 删除考勤组对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceGroupByIds(String ids)
    {
        return hrAttendanceGroupMapper.deleteHrAttendanceGroupByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除考勤组信息
     * 
     * @param id 考勤组ID
     * @return 结果
     */
    @Override
    public int deleteHrAttendanceGroupById(Long id)
    {
        return hrAttendanceGroupMapper.deleteHrAttendanceGroupById(id);
    }

    @Override
    public Map<String, Object> selectGroupAndClass(Long userId,int week) {
        return hrAttendanceGroupMapper.selectGroupAndClass(userId,week);
    }

    @Override
    public List<String> compareEmp(String empId,Long groupId) {
        Set<Long> set = new HashSet<>();
        List<String> returnList = new ArrayList<>();
        //逗号分隔字符串String转List<Long>
        if(!StringUtils.isNotBlank(empId)){
            return returnList;
        }
        List<Long> empIdList1 = Arrays.asList(empId.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        empIdList1.stream().forEach(System.out::println);

        List<String> listAll= hrAttendanceGroupMapper.selectAllEmpId(groupId);
        for (int i = 0; i <listAll.size() ; i++) {
            String id = listAll.get(i);
            List<Long> empIdList2 = Arrays.asList(id.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            //获取两个list中的交集
            List<Long> empIdList3 = empIdList1.stream().filter(t -> empIdList2.contains(t)).collect(Collectors.toList());
            if(empIdList3 != null && empIdList3.size() > 0) {
                set.addAll(empIdList3);
            }
        }
        if (set != null && set.size() > 0) {
            for (Long id:set) {
                String empNames = HrEmpMapper.selectEmpNameByEmpId(id);
                returnList.add(empNames);
            }
        }
        returnList.stream().forEach(System.out::println);
        return returnList;
    }

    @Override
    public String uploadImg(MultipartFile file) {
        // 上传文件路径
        String filePath = Global.getUploadPath();
        // 上传并返回新文件名称
        String fileName = null;
        try {
            fileName = FileUploadUtils.upload(filePath, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = serverConfig.getUrl() + fileName;
        return url;
    }

    @Override
    public Double selectDayOfWorkHours(Long userId,int week) {
        return hrAttendanceGroupMapper.selectDayOfWorkHours(userId,week);
    }

    @Override
    public Integer selectDayIsNeedNotDate(Long empId, String day) {
        return hrAttendanceGroupMapper.selectDayIsNeedNotDate(empId,day);
    }

    @Override
    public String selectDayIsMustDate(Long empId, String day) {
        return hrAttendanceGroupMapper.selectDayIsMustDate(empId,day);
    }

    @Override
    public Integer selectDayIsClass(Long empId, int week) {
        return hrAttendanceGroupMapper.selectDayIsClass(empId,week);
    }

    @Override
    public Map<String, Object> selectGruopByEmpIdFromApi(Long userId) {
        return hrAttendanceGroupMapper.selectGruopByEmpIdFromApi(userId);
    }

    @Override
    public Double todayIsWorkHour(Date start, Date end,
                                  Long userId) {
        DateTime startTime = DateUtil.date(start);
        DateTime endTime = DateUtil.date(end);

        HrAttendanceGroup hrAttendanceGroup = hrAttendanceGroupMapper.selectGroupByEmpId(userId);

        if (hrAttendanceGroup == null){
            return 0D;
        }
        //当天的开始时间和结束时间
        DateTime beginOfDay = DateUtil.beginOfDay(startTime);
        DateTime endOfDay = DateUtil.endOfDay(startTime);

        HrAttendanceClass hrAttendanceClassYesterday = null;
        HrAttendanceClass hrAttendanceClass = null;
        HrAttendanceClass hrAttendanceClassTomorrow = null;
        DateTime yesterDay = DateUtil.offsetDay(startTime, -1);
        DateTime tomorrowDay = DateUtil.offsetDay(startTime, 1);
        String type = hrAttendanceGroup.getType();
        //如果是排班制
        if(StringUtils.isNotBlank(type) && "1".equals(type)){
            //前一天的班次数据
            hrAttendanceClassYesterday = getScheduHrAttendanceClass(yesterDay, hrAttendanceGroup ,userId);
            //当天的班次数据
            hrAttendanceClass = getScheduHrAttendanceClass(startTime, hrAttendanceGroup,userId);
            //后一天的班次数据
            hrAttendanceClassTomorrow = getScheduHrAttendanceClass(tomorrowDay, hrAttendanceGroup,userId);
        }else {
            //必须打卡的日期
            String mustDate = hrAttendanceGroup.getMustDate();
            List<String> mustDateList = null;
            if (StringUtils.isNotEmpty(mustDate)){
                String[] mustDateArr = mustDate.split(",");
                mustDateList = Arrays.asList(mustDateArr);
            }
            //不用打卡的日期
            String needNotDate = hrAttendanceGroup.getNeedNotDate();
            List<String> needNotDateList = null;
            if (StringUtils.isNotEmpty(needNotDate)){
                String[] needNotDateArr = needNotDate.split(",");
                needNotDateList = Arrays.asList(needNotDateArr);
            }
            //前一天的班次数据
            hrAttendanceClassYesterday = getHrAttendanceClass(yesterDay, hrAttendanceGroup, mustDateList,needNotDateList);
            //当天的班次数据
            hrAttendanceClass = getHrAttendanceClass(startTime, hrAttendanceGroup, mustDateList, needNotDateList);
            //后一天的班次数据
            hrAttendanceClassTomorrow = getHrAttendanceClass(tomorrowDay, hrAttendanceGroup, mustDateList,needNotDateList);
        }

        Double resultDouble = 0D;

        //上班时段容器
        List<Map<String,DateTime>> dateList = new ArrayList<>();
        Map<String,DateTime> seDateMap;
        if (hrAttendanceClassYesterday != null){
            String format = DateUtil.format(yesterDay, "yyyy-MM-dd ");

            //前一天的班次数据
            String workTime = hrAttendanceClassYesterday.getWorkTime();
            String closingTime = hrAttendanceClassYesterday.getClosingTime();
            String restStartTime = hrAttendanceClassYesterday.getRestStartTime();
            String restEndTime = hrAttendanceClassYesterday.getRestEndTime();


            if (Integer.parseInt(workTime.replace(":",""))
                    > Integer.parseInt(closingTime.replace(":","")) ){

                //夜班
                String formatN = DateUtil.format(DateUtil.offsetDay(yesterDay,1), "yyyy-MM-dd ");
                //上班时间
                DateTime workDate = DateUtil.parse(format + workTime, "yyyy-MM-dd HH:mm");
                //下班时间
                DateTime closingDate = DateUtil.parse(formatN + closingTime, "yyyy-MM-dd HH:mm");
                if (StringUtils.isNotEmpty(restStartTime)){
                    //有休息时间

                    //休息开始时间
                    DateTime restStartDate = DateUtil.parse(format + restStartTime, "yyyy-MM-dd HH:mm");
                    //休息结束时间
                    DateTime restEndDate = DateUtil.parse(format + restEndTime, "yyyy-MM-dd HH:mm");
                    if (Integer.parseInt(restStartTime.replace(":",""))
                            < Integer.parseInt(workTime.replace(":",""))){
                        //休息小于开始时间，有隔天
                        //休息开始时间
                        restStartDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(yesterDay,1), "yyyy-MM-dd ") + restStartTime, "yyyy-MM-dd HH:mm");
                        //休息结束时间
                        restEndDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(yesterDay,1), "yyyy-MM-dd ") + restEndTime, "yyyy-MM-dd HH:mm");
                    }else if (Integer.parseInt(restEndTime.replace(":",""))
                            < Integer.parseInt(workTime.replace(":",""))){
                        //休息小于开始时间，有隔天
                        //休息结束时间
                        restEndDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(yesterDay,1), "yyyy-MM-dd ") + restEndTime, "yyyy-MM-dd HH:mm");
                    }


                    if (hrAttendanceClassYesterday.getElasticityFlag().equals("1")){
                        //弹性计算
                        Map<String,Object> map = elasticityOperation(userId, hrAttendanceClassYesterday, format, workDate, closingDate, restStartDate, restEndDate);
                        workDate = (DateTime) map.get("workDate");
                        closingDate = (DateTime) map.get("closingDate");
                        restStartDate = (DateTime) map.get("restStartDate");
                        restEndDate = (DateTime) map.get("restEndDate");
                    }

                    if (restStartDate == null){
                        //弹性计算结束后没有休息时间
                        //是否在范围内
                        if (DateUtil.isIn(startTime,workDate,closingDate)){
                            //0点---下班时间
                            seDateMap = new HashMap<>();
                            seDateMap.put("s",beginOfDay);
                            seDateMap.put("e",closingDate);
                            dateList.add(seDateMap);
                        }
                    }else {
                        //是否在范围内
                        if (DateUtil.isIn(startTime,workDate,closingDate)){
                            if (restStartDate.isAfter(beginOfDay)){
                                //休息开始时间在0点之后

                                //0点---休息开始时间
                                seDateMap = new HashMap<>();
                                seDateMap.put("s",beginOfDay);
                                seDateMap.put("e",restStartDate);
                                dateList.add(seDateMap);
                            }else {
                                //休息结束时间在0点之前

                                //休息结束时间---下班时间
                                seDateMap = new HashMap<>();
                                seDateMap.put("s",beginOfDay);
                                seDateMap.put("e",closingDate);
                                dateList.add(seDateMap);
                            }

                            if (restEndDate.isAfter(beginOfDay) && restStartDate.isAfter(beginOfDay)){
                                //休息结束时间在0点之后

                                //休息结束时间---下班时间
                                seDateMap = new HashMap<>();
                                seDateMap.put("s",restEndDate);
                                seDateMap.put("e",closingDate);
                                dateList.add(seDateMap);
                            }
                        }
                    }


                }else {

                    if (hrAttendanceClassYesterday.getElasticityFlag().equals("1")){
                        //弹性计算
                        Map<String,Object> map = elasticityOperation(userId, hrAttendanceClassYesterday, format, workDate, closingDate);
                        workDate = (DateTime) map.get("workDate");
                        closingDate = (DateTime) map.get("closingDate");
                    }

                    //没有休息时间
                    //是否在范围内
                    if (DateUtil.isIn(startTime,workDate,closingDate)){
                        //0点---下班时间
                        seDateMap = new HashMap<>();
                        seDateMap.put("s",beginOfDay);
                        seDateMap.put("e",closingDate);
                        dateList.add(seDateMap);
                    }
                }
            }

        }
        if (hrAttendanceClass != null){
            //当天的班次数据

            String format = DateUtil.format(startTime, "yyyy-MM-dd ");
            //当天的班次数据
            String workTime = hrAttendanceClass.getWorkTime();
            String closingTime = hrAttendanceClass.getClosingTime();
            String restStartTime = hrAttendanceClass.getRestStartTime();
            String restEndTime = hrAttendanceClass.getRestEndTime();

            if (Integer.parseInt(workTime.replace(":",""))
                    > Integer.parseInt(closingTime.replace(":","")) ){
                //夜班
                String formatN = DateUtil.format(DateUtil.offsetDay(startTime,1), "yyyy-MM-dd ");
                //上班时间
                DateTime workDate = DateUtil.parse(format + workTime, "yyyy-MM-dd HH:mm");
                //下班时间
                DateTime closingDate = DateUtil.parse(formatN + closingTime, "yyyy-MM-dd HH:mm");
                if (StringUtils.isNotEmpty(restStartTime)){
                    //有休息时间

                    //休息开始时间
                    DateTime restStartDate = DateUtil.parse(format + restStartTime, "yyyy-MM-dd HH:mm");
                    //休息结束时间
                    DateTime restEndDate = DateUtil.parse(format + restEndTime, "yyyy-MM-dd HH:mm");
                    if (Integer.parseInt(restStartTime.replace(":",""))
                            < Integer.parseInt(workTime.replace(":",""))){
                        //休息小于开始时间，有隔天
                        //休息开始时间
                        restStartDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(startTime,1), "yyyy-MM-dd ") + restStartTime, "yyyy-MM-dd HH:mm");
                        //休息结束时间
                        restEndDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(startTime,1), "yyyy-MM-dd ") + restEndTime, "yyyy-MM-dd HH:mm");
                    }else if (Integer.parseInt(restEndTime.replace(":",""))
                            < Integer.parseInt(workTime.replace(":",""))){
                        //休息小于开始时间，有隔天
                        //休息结束时间
                        restEndDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(startTime,1), "yyyy-MM-dd ") + restEndTime, "yyyy-MM-dd HH:mm");
                    }

                    if (hrAttendanceClass.getElasticityFlag().equals("1")){
                        //弹性计算
                        Map<String,Object> map = elasticityOperation(userId, hrAttendanceClass, format, workDate, closingDate, restStartDate, restEndDate);
                        workDate = (DateTime) map.get("workDate");
                        closingDate = (DateTime) map.get("closingDate");
                        restStartDate = (DateTime) map.get("restStartDate");
                        restEndDate = (DateTime) map.get("restEndDate");
                    }
                    if (restStartDate == null){
                        //弹性计算后没有休息时间
                        seDateMap = new HashMap<>();
                        seDateMap.put("s",workDate);
                        seDateMap.put("e",endOfDay);
                        dateList.add(seDateMap);
                    }else {
                        if (restStartDate.isAfter(endOfDay)){
                            //休息开始时间在24点之后

                            seDateMap = new HashMap<>();
                            seDateMap.put("s",workDate);
                            seDateMap.put("e",endOfDay);
                            dateList.add(seDateMap);
                        }else {
                            //休息结束时间在24点之前

                            //上班时间---休息时间
                            seDateMap = new HashMap<>();
                            seDateMap.put("s",workDate);
                            seDateMap.put("e",restStartDate);
                            dateList.add(seDateMap);
                        }

                        if (endOfDay.isAfter(restEndDate)){
                            //休息结束时间---24点
                            seDateMap = new HashMap<>();
                            seDateMap.put("s",restEndDate);
                            seDateMap.put("e",endOfDay);
                            dateList.add(seDateMap);
                        }
                    }



                }else {

                    if (hrAttendanceClass.getElasticityFlag().equals("1")){
                        //弹性计算
                        Map<String,Object> map = elasticityOperation(userId, hrAttendanceClass, format, workDate, closingDate);
                        workDate = (DateTime) map.get("workDate");
                        closingDate = (DateTime) map.get("closingDate");
                    }

                    //没有休息时间
                    seDateMap = new HashMap<>();
                    seDateMap.put("s",workDate);
                    seDateMap.put("e",endOfDay);
                    dateList.add(seDateMap);
                }
            }else {
                //白班
                //上班时间
                DateTime workDate = DateUtil.parse(format + workTime, "yyyy-MM-dd HH:mm", Locale.CHINA);
                //下班时间
                DateTime closingDate = DateUtil.parse(format + closingTime, "yyyy-MM-dd HH:mm", Locale.CHINA);

                if (StringUtils.isNotEmpty(restStartTime)){
                    //休息开始时间
                    DateTime restStartDate = DateUtil.parse(format + restStartTime, "yyyy-MM-dd HH:mm", Locale.CHINA);
                    //休息结束时间
                    DateTime restEndDate = DateUtil.parse(format + restEndTime, "yyyy-MM-dd HH:mm", Locale.CHINA);

                    if (hrAttendanceClass.getElasticityFlag().equals("1")){
                        //弹性计算
                        Map<String,Object> map = elasticityOperation(userId, hrAttendanceClass, format, workDate, closingDate, restStartDate, restEndDate);
                        workDate = (DateTime) map.get("workDate");
                        closingDate = (DateTime) map.get("closingDate");
                        restStartDate = (DateTime) map.get("restStartDate");
                        restEndDate = (DateTime) map.get("restEndDate");
                    }
                    if (restStartDate == null){
                        //弹性计算后没有休息时间
                        seDateMap = new HashMap<>();
                        seDateMap.put("s",workDate);
                        seDateMap.put("e",closingDate);
                        dateList.add(seDateMap);
                    }else {
                        seDateMap = new HashMap<>();
                        seDateMap.put("s",workDate);
                        seDateMap.put("e",restStartDate);
                        dateList.add(seDateMap);

                        seDateMap = new HashMap<>();
                        seDateMap.put("s",restEndDate);
                        seDateMap.put("e",closingDate);
                        dateList.add(seDateMap);
                    }



                }else {
                    if (hrAttendanceClass.getElasticityFlag().equals("1")){
                        //弹性计算
                        Map<String,Object> map = elasticityOperation(userId, hrAttendanceClass, format, workDate, closingDate);
                        workDate = (DateTime) map.get("workDate");
                        closingDate = (DateTime) map.get("closingDate");
                    }

                    //没有休息时间
                    seDateMap = new HashMap<>();
                    seDateMap.put("s",workDate);
                    seDateMap.put("e",closingDate);
                    dateList.add(seDateMap);
                }
            }
        }
        if (hrAttendanceClassTomorrow != null){
            //后一天的班次数据

            String format = DateUtil.format(tomorrowDay, "yyyy-MM-dd ");
            //当天的班次数据
            String workTime = hrAttendanceClassTomorrow.getWorkTime();
            String closingTime = hrAttendanceClassTomorrow.getClosingTime();
            String restStartTime = hrAttendanceClassTomorrow.getRestStartTime();
            String restEndTime = hrAttendanceClassTomorrow.getRestEndTime();

            if (Integer.parseInt(workTime.replace(":",""))
                    > Integer.parseInt(closingTime.replace(":","")) ){
                //夜班
                String formatN = DateUtil.format(DateUtil.offsetDay(tomorrowDay,1), "yyyy-MM-dd ");
                //上班时间
                DateTime workDate = DateUtil.parse(format + workTime, "yyyy-MM-dd HH:mm");
                //下班时间
                DateTime closingDate = DateUtil.parse(formatN + closingTime, "yyyy-MM-dd HH:mm");
                if (StringUtils.isNotEmpty(restStartTime)){
                    //有休息时间

                    //休息开始时间
                    DateTime restStartDate = DateUtil.parse(format + restStartTime, "yyyy-MM-dd HH:mm");
                    //休息结束时间
                    DateTime restEndDate = DateUtil.parse(format + restEndTime, "yyyy-MM-dd HH:mm");
                    if (Integer.parseInt(restStartTime.replace(":",""))
                            < Integer.parseInt(workTime.replace(":",""))){
                        //休息小于开始时间，有隔天
                        //休息开始时间
                        restStartDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(tomorrowDay,1), "yyyy-MM-dd ") + restStartTime, "yyyy-MM-dd HH:mm");
                        //休息结束时间
                        restEndDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(tomorrowDay,1), "yyyy-MM-dd ") + restEndTime, "yyyy-MM-dd HH:mm");
                    }else if (Integer.parseInt(restEndTime.replace(":",""))
                            < Integer.parseInt(workTime.replace(":",""))){
                        //休息小于开始时间，有隔天
                        //休息结束时间
                        restEndDate = DateUtil.parse(DateUtil.format(DateUtil.offsetDay(tomorrowDay,1), "yyyy-MM-dd ") + restEndTime, "yyyy-MM-dd HH:mm");
                    }


                    if (hrAttendanceClass.getElasticityFlag().equals("1")){
                        //弹性计算
                        Map<String,Object> map = elasticityOperation(userId, hrAttendanceClass, format, workDate, closingDate, restStartDate, restEndDate);
                        workDate = (DateTime) map.get("workDate");
                        closingDate = (DateTime) map.get("closingDate");
                        restStartDate = (DateTime) map.get("restStartDate");
                        restEndDate = (DateTime) map.get("restEndDate");
                    }

                    if (restStartDate == null){
                        if (DateUtil.isIn(endOfDay,workDate,closingDate)){
                            //弹性计算后没有休息时间
                            seDateMap = new HashMap<>();
                            seDateMap.put("s",workDate);
                            seDateMap.put("e",endOfDay);
                            dateList.add(seDateMap);
                        }
                    }else {
                        if (DateUtil.isIn(endOfDay,workDate,closingDate)){

                            if (endOfDay.before(restStartDate)){
                                seDateMap = new HashMap<>();
                                seDateMap.put("s",workDate);
                                seDateMap.put("e",endOfDay);
                                dateList.add(seDateMap);
                            }else {
                                seDateMap = new HashMap<>();
                                seDateMap.put("s",workDate);
                                seDateMap.put("e",restStartDate);
                                dateList.add(seDateMap);

                                if (restEndDate.before(endOfDay)){
                                    seDateMap = new HashMap<>();
                                    seDateMap.put("s",restEndDate);
                                    seDateMap.put("e",endOfDay);
                                    dateList.add(seDateMap);
                                }
                            }

                        }
                    }


                }else {

                    if (hrAttendanceClass.getElasticityFlag().equals("1")){
                        //弹性计算
                        Map<String,Object> map = elasticityOperation(userId, hrAttendanceClass, format, workDate, closingDate);
                        workDate = (DateTime) map.get("workDate");
                        closingDate = (DateTime) map.get("closingDate");
                    }

                    if (DateUtil.isIn(endOfDay,workDate,closingDate)){
                        //没有休息时间
                        seDateMap = new HashMap<>();
                        seDateMap.put("s",workDate);
                        seDateMap.put("e",endOfDay);
                        dateList.add(seDateMap);
                    }
                }
            }

        }

        //所以有的工资时间段
        resultDouble = getResultDouble(startTime, endTime, resultDouble, dateList);

        logger.info(StrUtil.format("userId:{};startTime:{};endTime:{};hour:{}",userId,startTime,endTime,resultDouble));

        return resultDouble;
    }

    @Override
    public Double getResultDouble(DateTime startTime, DateTime endTime, Double resultDouble, List<Map<String, DateTime>> dateList) {
        DateTime startDate;
        DateTime endDate;
        for (Map<String, DateTime> map : dateList) {
            startDate = map.get("s");
            endDate = map.get("e");


            if (startTime.isBeforeOrEquals(startDate) && endDate.isBeforeOrEquals(endTime)){
                //startTime < startDate < endDate < endTime
                resultDouble = resultDouble + getBetween(startDate,endDate);
            } else if (startDate.isBeforeOrEquals(startTime) && endTime.isBeforeOrEquals(endDate)){
                //startDate < startTime < endTime < endDate
                resultDouble = resultDouble + getBetween(startTime,endTime);
            }  else if (startDate.before(startTime) && !startTime.after(endDate)){
                //startDate < startTime < endDate < endTime && startTime !> endDate
                resultDouble = resultDouble + getBetween(startTime,endDate);
            } else if ( startTime.before(startDate) && endTime.before(endDate) && !endTime.before(startDate) ){
                //startTime < startDate < endTime < endDate && endTime !< startDate
                resultDouble = resultDouble + getBetween(startDate,endTime);
            }
        }
        return resultDouble;
    }

    @Override
    public Boolean selectIsExistGroup(Long userId) {
        int i = hrAttendanceGroupMapper.selectIsExistGroup(userId);
        if(i > 0){
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> selectScheduGroupAndClass(Long groupId, Long userId, String day) {
        Date date = DateUtil.parse(day);
        int dayNum = DateUtil.dayOfMonth(date);//获得指定日期是这个日期所在月份的第几天
        String scheduDate = DateUtil.format(date,"yyyy-MM");
        return hrAttendanceGroupMapper.selectScheduGroupAndClass(groupId,userId,scheduDate,dayNum);
    }

    @Override
    public List<HrAttendanceGroup> selectGroupByClassId(String classId) {
        return hrAttendanceGroupMapper.selectGroupByClassId(classId);
    }

    public HrAttendanceGroup selectGroupByEmpId(Long userId) {
        HrAttendanceGroup hrAttendanceGroup = hrAttendanceGroupMapper.selectGroupByEmpId(userId);
        return hrAttendanceGroup;
    }

    private Map<String,Object> elasticityOperation(Long userId, HrAttendanceClass hrAttendanceClass, String dateFormat,
                                                   DateTime workDate,
                                                   DateTime closingDate){
        return elasticityOperation(userId,hrAttendanceClass,dateFormat,workDate,closingDate,null,null);
    }

    /**
     * 弹性计算
     * @param userId 员工
     * @param hrAttendanceClass 班次
     * @param dateFormat 日期
     * @param workDate 上班时间
     * @param closingDate 下班
     * @param restStartDate 休息开始
     * @param restEndDate 休息结束
     */
    public Map<String,Object> elasticityOperation(Long userId, HrAttendanceClass hrAttendanceClass, String dateFormat,
                                                   DateTime workDate,
                                                   DateTime closingDate,
                                                   DateTime restStartDate,
                                                   DateTime restEndDate) {
        Map<String,Object> map = new HashMap<>();
        map.put("workDate",workDate);
        map.put("closingDate",closingDate);
        map.put("restStartDate",restStartDate);
        map.put("restEndDate",restEndDate);


        List<Map<String,Object>> list = hrAttendanceInfoService.getClockIn(userId,dateFormat);
        if (list != null && !list.isEmpty()){
            for (Map<String, Object> mapInfo : list) {
                Object check_type = mapInfo.get("check_type");
                if (StringUtils.isNotEmpty(check_type+"") && check_type.equals(HrAttendanceInfoServiceImpl.check_type_OnDuty)){
                    Date user_check_time = (Date)mapInfo.get("user_check_time");
//                    elasticityDate(user_check_time,workDate,closingDate,restStartDate,restEndDate,hrAttendanceClass);

                    Long minuteNum = 0L;

                    //打卡时间在上班时间之后
                    if (user_check_time.after(workDate)){
                        //打卡时间在休息开始时间之后
                        if (restStartDate != null && user_check_time.after(restStartDate)){

                            //打卡时间在休息结束时间之后
                            if (restEndDate.isBeforeOrEquals(user_check_time)){
                                //整体后推 去掉休息时间
                                minuteNum = DateUtil.between(workDate, user_check_time, DateUnit.MINUTE, false);
                            }else {
                                // 去掉上班时间到休息结束时间之间的分钟数
                                minuteNum = DateUtil.between(workDate, restEndDate, DateUnit.MINUTE, false);
                            }
                            //打卡时间在休息时间之前
                            workDate = DateUtil.offsetMinute(workDate,minuteNum.intValue());
                            closingDate = DateUtil.offsetMinute(closingDate,minuteNum.intValue());
                            //休息时间置空
                            restStartDate = null;
                            restEndDate = null;


                        }else {
                            minuteNum = DateUtil.between(workDate, user_check_time, DateUnit.MINUTE, false);
                            //打卡时间在休息时间之前
                            workDate = DateUtil.offsetMinute(workDate,minuteNum.intValue());
                            closingDate = DateUtil.offsetMinute(closingDate,minuteNum.intValue());

                        }
                    }else {
                        //打卡时间在上班时间之前
                        minuteNum = DateUtil.between(user_check_time, workDate, DateUnit.MINUTE, false);
                        workDate = DateUtil.offsetMinute(workDate, 0 - minuteNum.intValue());
                        closingDate = DateUtil.offsetMinute(closingDate,0 - minuteNum.intValue());
                    }
                }
            }
        }
        map.put("workDate",workDate);
        map.put("closingDate",closingDate);
        map.put("restStartDate",restStartDate);
        map.put("restEndDate",restEndDate);
        return map;

    }

    private HrAttendanceClass getHrAttendanceClass(Date startTime, HrAttendanceGroup hrAttendanceGroup,
                                                   List<String> mustDateList, List<String> needNotDateList) {



        //这个员工今天要上班吗
        String startDayStr = DateUtil.format(startTime,"yyyy-MM-dd");

        //这个开始日期需要打卡吗
        if (needNotDateList != null ){
            for (String dateStr : needNotDateList) {
                if (dateStr.contains(startDayStr)){
                    //不用打卡的日期
                    return null;
                }
            }
        }

        //是否是国假
        TDate tDate = new TDate();
        tDate.setDay(DateUtil.parse(startDayStr,"yyyy-MM-dd"));
        tDate.setFlag("1");
        List<TDate> tDates = dateService.selectTDateList(tDate);
        if (tDates != null && !tDates.isEmpty()){
            return null;
        }

        int dayOfWeek = DateUtil.dayOfWeek(startTime);
        //1表示周日，2表示周一
        String classId = null;
        switch (dayOfWeek){
            case 1:
                classId = hrAttendanceGroup.getSunClassId();
                break;
            case 2:
                classId = hrAttendanceGroup.getMonClassId();
                break;
            case 3:
                classId = hrAttendanceGroup.getTueClassId();
                break;
            case 4:
                classId = hrAttendanceGroup.getWedClassId();
                break;
            case 5:
                classId = hrAttendanceGroup.getThuClassId();
                break;
            case 6:
                classId = hrAttendanceGroup.getFriClassId();
                break;
            default:
                classId = hrAttendanceGroup.getSatClassId();
                break;
        }

        HrAttendanceClass hrAttendanceClass = null;
        if (StringUtils.isNotEmpty(classId)){
            hrAttendanceClass = hrAttendanceClassService.selectHrAttendanceClassById(Long.parseLong(classId));
        }
        if (hrAttendanceClass == null || hrAttendanceClass.getDelFlag().equals("1") ){
            if (mustDateList != null && mustDateList.size() > 0){
                for (String mustDateStr : mustDateList){
                    if (mustDateStr.contains(startDayStr)){
                        //必须上班
                        hrAttendanceClass = hrAttendanceClassService.selectHrAttendanceClassById(Long.parseLong(mustDateStr.split(":")[1]));
                        break;
                    }
                }
            }
        }

        if (StringUtils.isEmpty(classId) && hrAttendanceClass == null){
            return null;
        }



        return hrAttendanceClass != null && hrAttendanceClass.getDelFlag().equals("0") ? hrAttendanceClass : null;
    }

    /**
     * 获取该员工当日排班制的班次信息
     * @param userId
     * @param day
     * @param hrAttendanceGroup
     * @return
     */
    private HrAttendanceClass getScheduHrAttendanceClass(DateTime day, HrAttendanceGroup hrAttendanceGroup,Long userId) {
        int dayNum = DateUtil.dayOfMonth(day);//获得指定日期是这个日期所在月份的第几天
        String scheduDate = DateUtil.format(day,"yyyy-MM");
        Map<String, Object> map = hrAttendanceGroupMapper.selectScheduGroupAndClass(hrAttendanceGroup.getId(),userId,scheduDate,dayNum);
        //当天班次信息为空，则是公休日
        if(map == null || map.isEmpty()){
            return null;
        }
        Long classId = (Long) map.get("class_id");
        String className = (String) map.get("class_name");
        String workTime = (String) map.get("work_time");
        String closingTime = (String) map.get("closing_time");
        String restStartTime = (String) map.get("rest_start_time");
        String restEndTime = (String) map.get("rest_end_time");
        String hours = (String) map.get("hours");
        String elasticityFlag = (String) map.get("elasticity_flag");
        HrAttendanceClass hrAttendanceClass = new HrAttendanceClass();
        hrAttendanceClass.setId(classId);
        hrAttendanceClass.setName(className);
        hrAttendanceClass.setWorkTime(workTime);
        hrAttendanceClass.setClosingTime(closingTime);
        hrAttendanceClass.setRestStartTime(restStartTime);
        hrAttendanceClass.setRestEndTime(restEndTime);
        hrAttendanceClass.setHours(hours);
        hrAttendanceClass.setElasticityFlag(elasticityFlag);
        return hrAttendanceClass;
    }


    @Override
    public Double getBetween(Date startTime, Date restEndDate) {
        long hourNum = DateUtil.between(startTime, restEndDate, DateUnit.HOUR, false);
        long betweenMs = DateUtil.betweenMs(DateUtil.offsetHour(startTime,Integer.parseInt(hourNum+"")),restEndDate);
        if (betweenMs > 1800000){
            return Double.valueOf(hourNum) + 1D;
        }else if (betweenMs > 60000){
            return Double.valueOf(hourNum) + 0.5D;
        }
        return Double.valueOf(hourNum);
    }


}
