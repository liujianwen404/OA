package com.ruoyi.hr.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTO;
import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTOError;
import com.ruoyi.base.domain.HrAttendanceStatistics;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.IHrAttendanceStatisticsService;
import com.ruoyi.hr.utils.AttendanceStatisticsEasyExcelListener;
import com.ruoyi.hr.utils.ImportInfoEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * 考勤统计Controller
 * 
 * @author liujianwen
 * @date 2020-08-11
 */
@Controller
@RequestMapping("/hr/attendanceStatistics")
public class HrAttendanceStatisticsController extends BaseController
{
    private String prefix = "hr/attendanceStatistics";

    @Autowired
    private IHrAttendanceStatisticsService hrAttendanceStatisticsService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HrEmpService hrEmpService;

    @RequiresPermissions("hr:attendanceStatistics:view")
    @GetMapping()
    public String statistics()
    {
        return prefix + "/statistics";
    }

    /**
     * 查询考勤统计列表
     */
    @RequiresPermissions("hr:attendanceStatistics:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrAttendanceStatistics hrAttendanceStatistics)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrAttendanceStatistics.setCreateId(ShiroUtils.getUserId());
//        }
        startPage();
        List<HrAttendanceStatistics> list = hrAttendanceStatisticsService.selectHrAttendanceStatisticsList(hrAttendanceStatistics);
        return getDataTable(list);
    }

  

    /**
     * 新增考勤统计
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存考勤统计
     */
    @RequiresPermissions("hr:attendanceStatistics:add")
    @Log(title = "考勤统计", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrAttendanceStatistics hrAttendanceStatistics)
    {
        return toAjax(hrAttendanceStatisticsService.insertHrAttendanceStatistics(hrAttendanceStatistics));
    }

    /**
     * 修改考勤统计
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrAttendanceStatistics hrAttendanceStatistics = hrAttendanceStatisticsService.selectHrAttendanceStatisticsById(id);
        mmap.put("hrAttendanceStatistics", hrAttendanceStatistics);
        return prefix + "/edit";
    }

    /**
     * 修改保存考勤统计
     */
    @RequiresPermissions("hr:attendanceStatistics:edit")
    @Log(title = "考勤统计", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrAttendanceStatistics hrAttendanceStatistics)
    {
        return toAjax(hrAttendanceStatisticsService.updateHrAttendanceStatistics(hrAttendanceStatistics));
    }

    /**
     * 删除考勤统计
     */
    @RequiresPermissions("hr:attendanceStatistics:remove")
    @Log(title = "考勤统计", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrAttendanceStatisticsService.deleteHrAttendanceStatisticsByIds(ids));
    }


    /**   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~以下是导入导出相关接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   */


    /**
     * 导出列表
     */
    @RequiresPermissions("hr:holiday:export")
    @Log(title = "导出错误数据", businessType = BusinessType.EXPORT)
    @PostMapping("/exportErrorList")
    @ResponseBody
    public AjaxResult exportErrorList()
    {
        Set<Object> objects = redisTemplate.opsForZSet().reverseRange(AttendanceStatisticsEasyExcelListener.redisKeyData, 0, -1);
        List<ImportInfoEntity<AttendanceStatisticsDTOError>> list = new ArrayList<>();
        for (Object object : objects) {
            ImportInfoEntity<AttendanceStatisticsDTOError> importInfoEntity = (ImportInfoEntity<AttendanceStatisticsDTOError>)object;
            list.add(importInfoEntity);
        }
        return AjaxResult.success(list);
    }

    /**
     * 导出错误数据
     */
    @Log(title = "导出错误数据", businessType = BusinessType.EXPORT)
    @PostMapping("/exportError")
    @ResponseBody
    public AjaxResult exportError(Date date) throws IOException
    {
        //数量大一五条删除多余的数据
        List<AttendanceStatisticsDTOError> list = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(AttendanceStatisticsEasyExcelListener.redisKeyData,0,-1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
            ImportInfoEntity<AttendanceStatisticsDTOError> importInfoEntity = (ImportInfoEntity<AttendanceStatisticsDTOError>)typedTuple.getValue();
            long value = importInfoEntity.getImportDate().getTime() - date.getTime();
            //时间匹配可能会有毫秒级的差距
            if ( -1000 <= value && value <= 1000 ){
                list = importInfoEntity.getImportInfoList();
            }
        }
        String fileName = "导出错误数据_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, AttendanceStatisticsDTO.class).sheet("错误数据").doWrite(list);
        return AjaxResult.success(fileName);
    }

    /**
     * 清除错误数据
     */
    @Log(title = "清除错误数据", businessType = BusinessType.DELETE)
    @GetMapping("/exportError")
    @ResponseBody
    public AjaxResult clearError()
    {
        Set<Object> objects = redisTemplate.opsForZSet().reverseRange(AttendanceStatisticsEasyExcelListener.redisKeyData, 0, -1);
        for (Object object : objects) {
            ImportInfoEntity<AttendanceStatisticsDTOError> importInfoEntity = (ImportInfoEntity<AttendanceStatisticsDTOError>)object;
            redisTemplate.opsForZSet().remove(AttendanceStatisticsEasyExcelListener.redisKeyData,importInfoEntity);
        }
        return AjaxResult.success();
    }

    
    /**
     * 导出模板
     */
    @Log(title = "导出模板", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadTemplate")
    @ResponseBody
    public AjaxResult export() throws IOException
    {
        List<AttendanceStatisticsDTO> list = new ArrayList<>();

        AttendanceStatisticsDTO attendanceStatisticsDTO = new AttendanceStatisticsDTO();
        attendanceStatisticsDTO.setEmpId(000L);
        attendanceStatisticsDTO.setStatisticsDate("yyyy-MM-dd");
        attendanceStatisticsDTO.setPreviousHoliday(0D);
        attendanceStatisticsDTO.setOvertimeHoliday(0D);
        attendanceStatisticsDTO.setAllHoliday(0D);

        attendanceStatisticsDTO.setAttendanceBonus(BigDecimal.ZERO);
        attendanceStatisticsDTO.setNightSubsidy(BigDecimal.ZERO);
        attendanceStatisticsDTO.setMealSubsidy(BigDecimal.ZERO);
        attendanceStatisticsDTO.setOtherSubsidy(BigDecimal.ZERO);
        attendanceStatisticsDTO.setLateDeduct(BigDecimal.ZERO);
        attendanceStatisticsDTO.setNotClockDeduct(BigDecimal.ZERO);
        attendanceStatisticsDTO.setEarlyDeduct(BigDecimal.ZERO);
        attendanceStatisticsDTO.setWaterElectricityDeduct(BigDecimal.ZERO);
        attendanceStatisticsDTO.setOtherDeduct(BigDecimal.ZERO);


        list.add(attendanceStatisticsDTO);

        String fileName = "薪资导入模板_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, AttendanceStatisticsDTO.class).sheet("薪资导入").doWrite(list);
        return AjaxResult.success(fileName);
    }


    /**
     * 检查是否可以导入
     */
    @RequiresPermissions("hr:holiday:export")
    @GetMapping("/checkUrl")
    @ResponseBody
    public AjaxResult checkUrl()
    {
        try {
            if (redisTemplate.hasKey(AttendanceStatisticsEasyExcelListener.redisKey)){
                return AjaxResult.error();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResult.success();
    }
    /**
     * 导入数据
     */
    @RequiresPermissions("hr:attendanceStatistics:export")
    @PostMapping("/importData")
    @ResponseBody
    @Transactional
    public AjaxResult importData(@RequestParam("importUtilFile") MultipartFile[] files)
    {
        if (files != null) {
            try {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getOriginalFilename();
                    //读取从第二行开始的正式列数据
                    EasyExcel.read(files[i].getInputStream(), AttendanceStatisticsDTO.class,
                            new AttendanceStatisticsEasyExcelListener(hrAttendanceStatisticsService,redisTemplate))
                            .doReadAll();
                    message.append("导入成功");
                }
                return AjaxResult.success(message.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error(e.getMessage());
            }
        }
        return AjaxResult.error("没有检测到文件！");
    }

    /**
     * 导出考勤统计列表
     */
    @RequiresPermissions("hr:attendanceStatistics:export")
    @Log(title = "考勤统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrAttendanceStatistics hrAttendanceStatistics)
    {
        List<HrAttendanceStatistics> list = hrAttendanceStatisticsService.selectHrAttendanceStatisticsList(hrAttendanceStatistics);
        String fileName = "考勤统计报表.xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        EasyExcel.write(filePath, HrAttendanceStatistics.class).sheet("sheet1").doWrite(list);
        return AjaxResult.success(fileName);
    }

    /**
     * 开始统计
     */
    @RequiresPermissions("hr:attendanceStatistics:export")
    @Log(title = "考勤统计", businessType = BusinessType.INSERT)
    @PostMapping("/start")
    @ResponseBody
    public AjaxResult start(Long id)
    {
        HrAttendanceStatistics hrAttendanceStatistics = hrAttendanceStatisticsService.selectHrAttendanceStatisticsById(id);
        return AjaxResult.success();
    }

    /**
     * 重试
     */
    @RequiresPermissions("hr:attendanceStatistics:export")
    @Log(title = "考勤统计", businessType = BusinessType.UPDATE)
    @PostMapping("/restart")
    @ResponseBody
    public AjaxResult restart(Long id)
    {
        HrAttendanceStatistics hrAttendanceStatistics = hrAttendanceStatisticsService.selectHrAttendanceStatisticsById(id);
        hrAttendanceStatisticsService.restart(hrAttendanceStatistics);
        return AjaxResult.success();
    }


    /**
     * 初始化所有员工统计数据
     */
    @RequiresPermissions("hr:attendanceStatistics:export")
    @Log(title = "考勤统计", businessType = BusinessType.INSERT)
    @PostMapping("/init")
    @ResponseBody
    public AjaxResult init()
    {
        return hrAttendanceStatisticsService.init();
    }

}
