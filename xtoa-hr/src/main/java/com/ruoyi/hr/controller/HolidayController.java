package com.ruoyi.hr.controller;

import com.alibaba.excel.EasyExcel;
import com.ruoyi.base.domain.DTO.AttendanceStatisticsDTOError;
import com.ruoyi.base.domain.DTO.HolidayDTO;
import com.ruoyi.base.domain.DTO.HolidayDTOError;
import com.ruoyi.base.domain.Holiday;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.IHolidayService;
import com.ruoyi.hr.utils.AttendanceStatisticsEasyExcelListener;
import com.ruoyi.hr.utils.HolidayExcelListener;
import com.ruoyi.hr.utils.ImportInfoEntity;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 员工假期Controller
 * 
 * @author liujianwen
 * @date 2020-06-05
 */
@Controller
@RequestMapping("/hr/holiday")
public class HolidayController extends BaseController
{
    private String prefix = "hr/holiday";

    @Autowired
    private IHolidayService holidayService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private HrEmpService tHrEmpService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequiresPermissions("hr:holiday:view")
    @GetMapping()
    public String holiday()
    {
        return prefix + "/holiday";
    }

    /**
     * 查询员工假期列表
     */
    @RequiresPermissions("hr:holiday:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Holiday holiday)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            holiday.setCreateId(ShiroUtils.getUserId());
//        }
        startPage();
        List<Holiday> list = holidayService.selectHolidayList(holiday);
        return getDataTable(list);
    }

    /**
     * 新增员工假期
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存员工假期
     */
    @RequiresPermissions("hr:holiday:add")
    @Log(title = "员工假期", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Holiday holiday)
    {
        HrEmp hrEmp = tHrEmpService.selectTHrEmpById(holiday.getEmpId());
        SysUser sysUser = sysUserService.selectUserById(hrEmp.getEmpId());
        holiday.setCreateId(sysUser.getUserId());
        holiday.setCreateBy(sysUser.getLoginName());
        return toAjax(holidayService.insertHoliday(holiday));
    }

    /**
     * 修改员工假期
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Holiday holiday = holidayService.selectHolidayById(id);
//        HrEmp hrEmp = tHrEmpService.selectTHrEmpById(holiday.getEmpId());
        mmap.put("holiday", holiday);
//        mmap.put("empName", hrEmp.getEmpName());
        return prefix + "/edit";
    }

    /**
     * 修改保存员工假期
     */
    @RequiresPermissions("hr:holiday:edit")
    @Log(title = "员工假期", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Holiday holiday)
    {
        return toAjax(holidayService.updateHoliday(holiday));
    }

    /**
     * 删除员工假期
     */
    @RequiresPermissions("hr:holiday:remove")
    @Log(title = "员工假期", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(holidayService.deleteHolidayByIds(ids));
    }

    /**
     * 跳转初始化年假配置页面
     */
    @RequiresPermissions("hr:holiday:add")
    @GetMapping("/init")
    public String init()
    {
        return prefix + "/init";
    }

    /**
     * 初始化所有员工年假
     */
    @Log(title = "初始化员工年假", businessType = BusinessType.INSERT)
    @PostMapping("/initHoliday")
    @ResponseBody
    public AjaxResult initHoliday(int days)
    {
        return toAjax(holidayService.initHoliday(days));
    }


/**   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~以下是导入导出相关接口~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   */


    /**
     * 导出员工假期列表
     */
    @RequiresPermissions("hr:holiday:export")
    @Log(title = "员工假期导出错误数据", businessType = BusinessType.EXPORT)
    @PostMapping("/exportErrorList")
    @ResponseBody
    public AjaxResult exportErrorList()
    {
        Set<Object> objects = redisTemplate.opsForZSet().reverseRange(HolidayExcelListener.redisKeyData, 0, -1);
        List<ImportInfoEntity<HolidayDTOError>> list = new ArrayList<>();
        for (Object object : objects) {
            ImportInfoEntity<HolidayDTOError> importInfoEntity = (ImportInfoEntity<HolidayDTOError>)object;
            list.add(importInfoEntity);
        }
        return AjaxResult.success(list);
    }

    /**
     * 导出错误数据
     */
    @Log(title = "员工假期导出错误数据", businessType = BusinessType.EXPORT)
    @PostMapping("/exportError")
    @ResponseBody
    public AjaxResult exportError(Date date) throws IOException
    {
        //数量大一五条删除多余的数据
        List<HolidayDTOError> list = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet().reverseRangeWithScores(HolidayExcelListener.redisKeyData,0,-1);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
        while (iterator.hasNext())
        {
            ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
            ImportInfoEntity<HolidayDTOError> importInfoEntity = (ImportInfoEntity<HolidayDTOError>)typedTuple.getValue();
            long value = importInfoEntity.getImportDate().getTime() - date.getTime();
            if ( -1000 <= value && value <= 1000 ){
                list = importInfoEntity.getImportInfoList();
            }
        }
        String fileName = "导出错误数据_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, HolidayDTO.class).sheet("错误数据").doWrite(list);
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
        Set<Object> objects = redisTemplate.opsForZSet().reverseRange(HolidayExcelListener.redisKeyData, 0, -1);
        for (Object object : objects) {
            ImportInfoEntity<HolidayDTOError> importInfoEntity = (ImportInfoEntity<HolidayDTOError>)object;
            redisTemplate.opsForZSet().remove(HolidayExcelListener.redisKeyData,importInfoEntity);
        }
        return AjaxResult.success();
    }

    /**
     * 导出员工假期列表
     */
    @RequiresPermissions("hr:holiday:export")
    @Log(title = "员工假期", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Holiday holiday)
    {
        startPage();
        List<Holiday> list = holidayService.selectHolidayList(holiday);

        String fileName = "假期导出_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, Holiday.class).sheet("假期导入").doWrite(list);
        return AjaxResult.success(fileName);
    }

    /**
     * 导出模板
     */
    @Log(title = "员工假期导出模板", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadTemplate")
    @ResponseBody
    public AjaxResult export() throws IOException
    {
        List<HolidayDTO> list = new ArrayList<>();

        HolidayDTO holidayDTO = new HolidayDTO();
        holidayDTO.setEmpNum("XT00000");
        holidayDTO.setTypeStr("调休,事假,病假,年假,丧假,婚假,产假,陪产假,哺乳假,例假");
        holidayDTO.setHours(999D);
        holidayDTO.setStartDate("yyyy/MM/dd");
        holidayDTO.setEndDate("yyyy/MM/dd");
        list.add(holidayDTO);

        String fileName = "假期导入模板_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, HolidayDTO.class).sheet("假期导入").doWrite(list);
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
            if (redisTemplate.hasKey(HolidayExcelListener.redisKey)){
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
    @RequiresPermissions("hr:holiday:export")
    @PostMapping("/importData")
    @ResponseBody
    @Transactional
    public AjaxResult importData(@RequestParam("importUtilFile") MultipartFile[] files ,HttpServletRequest request)
    {
        if (files != null) {
            if (files.length > 1){
                return AjaxResult.error("不支持多文件！");
            }
            if (StringUtils.isEmpty(files[0].getOriginalFilename())){
                return AjaxResult.error("没有检测到文件！");
            }
            try {
                try {
                    //开始导入设置标签，3分钟后过期
                    redisTemplate.opsForValue().set(HolidayExcelListener.redisKey,"1",3,TimeUnit.MINUTES);
                }catch (Exception e){
                    e.printStackTrace();
                }
                StringBuilder message = new StringBuilder();
                EasyExcel.read(files[0].getInputStream(), HolidayDTO.class,
                        new HolidayExcelListener(holidayService,redisTemplate,request))
                        .doReadAll();
                message.append("导入成功");
                return AjaxResult.success(message.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error(e.getMessage());
            }
        }
        return AjaxResult.error("没有检测到文件！");
    }



}
