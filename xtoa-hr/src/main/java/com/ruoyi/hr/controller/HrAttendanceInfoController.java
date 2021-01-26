package com.ruoyi.hr.controller;

import com.ruoyi.base.dingTalk.DingAttendanceApi;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.base.domain.HrAttendanceInfo;
import com.ruoyi.hr.service.IHrAttendanceInfoService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 个人考勤记录Controller
 * 
 * @author liujianwen
 * @date 2020-07-13
 */
@Controller
@RequestMapping("/hr/attendanceInfo")
public class HrAttendanceInfoController extends BaseController
{
    private String prefix = "hr/attendanceInfo";

    @Autowired
    private IHrAttendanceInfoService hrAttendanceInfoService;

    @RequiresPermissions("hr:attendanceInfo:view")
    @GetMapping()
    public String attendanceInfo()
    {
        return prefix + "/attendanceInfo";
    }

    @RequiresPermissions("hr:attendanceInfoAll:view")
    @GetMapping("/attendanceInfoAll")
    public String attendanceInfoAll()
    {
        return prefix + "/attendanceInfoAll";
    }

    /**
     * 查询我的考勤记录列表
     */
    @RequiresPermissions("hr:attendanceInfo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrAttendanceInfo hrAttendanceInfo)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrAttendanceInfo.setEmpId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrAttendanceInfo> list = hrAttendanceInfoService.selectHrAttendanceInfoList(hrAttendanceInfo);
        return getDataTable(list);
    }

    /**
     * 查询员工考勤记录列表
     */
    @PostMapping("/attendanceInfoAllList")
    @ResponseBody
    public TableDataInfo attendanceInfoAllList(HrAttendanceInfo hrAttendanceInfo)
    {
        startPage();
        List<HrAttendanceInfo> list = hrAttendanceInfoService.selectHrAttendanceInfoAllList(hrAttendanceInfo);
        return getDataTable(list);
    }

    /**
     * 导出个人考勤记录列表
     */
    @RequiresPermissions("hr:attendanceInfo:export")
    @Log(title = "个人考勤记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrAttendanceInfo hrAttendanceInfo)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrAttendanceInfo.setCreateId(ShiroUtils.getUserId());
        }
        List<HrAttendanceInfo> list = hrAttendanceInfoService.selectHrAttendanceInfoList(hrAttendanceInfo);
        ExcelUtil<HrAttendanceInfo> util = new ExcelUtil<HrAttendanceInfo>(HrAttendanceInfo.class);
        return util.exportExcel(list, "attendanceInfo");
    }

    /**
     * 新增个人考勤记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存个人考勤记录
     */
    @RequiresPermissions("hr:attendanceInfo:add")
    @Log(title = "个人考勤记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrAttendanceInfo hrAttendanceInfo)
    {
        return toAjax(hrAttendanceInfoService.insertHrAttendanceInfo(hrAttendanceInfo));
    }

    /**
     * 修改个人考勤记录
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrAttendanceInfo hrAttendanceInfo = hrAttendanceInfoService.selectHrAttendanceInfoById(id);
        mmap.put("hrAttendanceInfo", hrAttendanceInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存个人考勤记录
     */
    @RequiresPermissions("hr:attendanceInfo:edit")
    @Log(title = "个人考勤记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrAttendanceInfo hrAttendanceInfo)
    {
        return toAjax(hrAttendanceInfoService.updateHrAttendanceInfo(hrAttendanceInfo));
    }

    /**
     * 删除个人考勤记录
     */
    @RequiresPermissions("hr:attendanceInfo:remove")
    @Log(title = "个人考勤记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrAttendanceInfoService.deleteHrAttendanceInfoByIds(ids));
    }
}
