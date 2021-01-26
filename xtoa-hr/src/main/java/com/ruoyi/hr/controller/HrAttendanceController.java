package com.ruoyi.hr.controller;

import java.util.List;

import com.alibaba.excel.EasyExcel;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.DTO.*;
import com.ruoyi.base.provider.hrService.IHrOvertimeService;
import com.ruoyi.hr.utils.*;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.base.domain.HrAttendance;
import com.ruoyi.hr.service.IHrAttendanceService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;


/**
 * 考勤统计Controller
 * 
 * @author liujianwen
 * @date 2020-06-09
 */
@Controller
@RequestMapping("/hr/attendance")
public class HrAttendanceController extends BaseController
{
    private String prefix = "hr/attendance";

    @Autowired
    private IHrAttendanceService hrAttendanceService;

    @Autowired
    private ImportAttendanceUtil importAttendanceUtil;

    @Autowired
    private IHrOvertimeService overtimeService;

    @RequiresPermissions("hr:attendance:view")
    @GetMapping()
    public String attendance()
    {
        return prefix + "/attendance";
    }

    /**
     * 查询考勤统计列表
     */
    @RequiresPermissions("hr:attendance:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrAttendance hrAttendance)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrAttendance.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrAttendance> list = hrAttendanceService.selectHrAttendanceList(hrAttendance);
        return getDataTable(list);
    }

    /**
     * 导入数据
     */
    @RequiresPermissions("hr:attendance:import")
    @PostMapping("/importData")
    @ResponseBody
    @Transactional
    public AjaxResult importData(MultipartFile[] files)
    {
        if (files != null) {
            try {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getOriginalFilename();
                    if("考勤假期数据.xlsx".equals(fileName) || "考勤假期数据.xls".equals(fileName)){
                        //先读取复杂表头数据
                        EasyExcel.read(files[i].getInputStream(), AttendanceExtraDTO.class, new AttendanceExtraEasyExcelListener(hrAttendanceService))
                                .sheet().doRead();
                        //再读取从第二行开始的正式列数据
                        EasyExcel.read(files[i].getInputStream(), AttendanceExtraDTO.class, new AttendanceExtraEasyExcelListener(hrAttendanceService))
                                .sheet().headRowNumber(2).doRead();
                        message.append("导入成功");
                    }
                    if("月度汇总.xlsx".equals(fileName) || "月度汇总.xls".equals(fileName)){
                        //先读取复杂表头数据
                        EasyExcel.read(files[i].getInputStream(), AttendanceExtraDTO.class, new AttendanceDetaiEasyExcelListener(hrAttendanceService))
                                .sheet().doRead();
                        //再读取从第三行开始的正式列数据
                        EasyExcel.read(files[i].getInputStream(), AttendanceDetailDTO.class, new AttendanceDetaiEasyExcelListener(hrAttendanceService))
                                .sheet().headRowNumber(3).doRead();
                        message.append("导入成功");
                    }
                    if("每日统计.xlsx".equals(fileName) || "每日统计.xls".equals(fileName)){
                        EasyExcel.read(files[i].getInputStream(), HrAttendanceDTO.class, new AttendanceEveryDayEasyExcelListener(hrAttendanceService))
                                .sheet().headRowNumber(3).doRead();
                        message.append("导入成功");
                    }
                    if("请假.xls".equals(fileName)  || "请假.xlsx".equals(fileName)){
                        ExcelUtil<HrLeaveDTO> util = new ExcelUtil<>(HrLeaveDTO.class);
                        List<HrLeaveDTO> list = util.importExcel(files[i].getInputStream());
                        String info = importAttendanceUtil.importLeaveAttendance(list);
                        message.append(info);
                    }
                    if("加班.xls".equals(fileName)  || "加班.xlsx".equals(fileName)){
                        //doReadAll()方法导入所有sheet
                        EasyExcel.read(files[i].getInputStream(), OverTimeDTO.class, new OverTimeEasyExcelListener(overtimeService))
                                .doReadAll();
                        message.append("导入成功");
                        /*ExcelUtil<OverTimeDTO> util = new ExcelUtil<>(OverTimeDTO.class);
                        List<OverTimeDTO> list = util.importExcel(files[i].getInputStream());
                        String info = importAttendanceUtil.importOverTimeAttendance(list);
                        message.append(info);*/
                    }
                    if("外出.xls".equals(fileName)  || "外出.xlsx".equals(fileName)){

                        ExcelUtil<GooutDTO> util = new ExcelUtil<>(GooutDTO.class);
                        List<GooutDTO> list = util.importExcel(files[i].getInputStream());
                        String info = importAttendanceUtil.importGooutAttendance(list);
                        message.append(info);
                    }
                    if("出差.xls".equals(fileName)  || "出差.xlsx".equals(fileName)){
                        ExcelUtil<BusinessTripDTO> util = new ExcelUtil<>(BusinessTripDTO.class);
                        List<BusinessTripDTO> list = util.importExcel(files[i].getInputStream());
                        String info = importAttendanceUtil.importBusinessTripAttendance(list);
                        message.append(info);
                    }
                    if("补卡.xls".equals(fileName)  || "补卡.xlsx".equals(fileName)){
                        ExcelUtil<FillClockDTO> util = new ExcelUtil<>(FillClockDTO.class);
                        List<FillClockDTO> list = util.importExcel(files[i].getInputStream());
                        String info = importAttendanceUtil.importFillClockAttendance(list);
                        message.append(info);
                    }

                }
                return AjaxResult.success(message.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return AjaxResult.error("系统异常，请通知系统管理员！");
            }
        }
        return AjaxResult.error("没有检测到文件！");
    }


    /**
     * 导出考勤统计列表
     */
    @RequiresPermissions("hr:attendance:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrAttendance hrAttendance) {
        // 查数据写EXCEL
        List<HrAttendance> list = hrAttendanceService.selectHrAttendanceList(hrAttendance);
        String fileName = "鲜恬科技考勤明细.xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        EasyExcel.write(filePath, HrAttendance.class).sheet("sheet1").doWrite(list);
        return AjaxResult.success(fileName);
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
    @RequiresPermissions("hr:attendance:add")
    @Log(title = "考勤统计", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrAttendance hrAttendance)
    {
        return toAjax(hrAttendanceService.insertHrAttendance(hrAttendance));
    }

    /**
     * 修改考勤统计
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrAttendance hrAttendance = hrAttendanceService.selectHrAttendanceById(id);
        mmap.put("hrAttendance", hrAttendance);
        return prefix + "/edit";
    }

    /**
     * 修改保存考勤统计
     */
    @RequiresPermissions("hr:attendance:edit")
    @Log(title = "考勤统计", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrAttendance hrAttendance)
    {
        return toAjax(hrAttendanceService.updateHrAttendance(hrAttendance));
    }

    /**
     * 删除考勤统计
     */
    @RequiresPermissions("hr:attendance:remove")
    @Log(title = "考勤统计", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrAttendanceService.deleteHrAttendanceByIds(ids));
    }

}
