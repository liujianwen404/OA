package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.DataToExcel;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.IInfoReportService;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 转正报表管理
 *
 * @author liujianwen
 */
@Controller
@RequestMapping("/hr/report/quit")
public class ReportQuitController extends BaseController {
    private String prefix = "hr/report/quit";

    @Autowired
    private IInfoReportService infoReportService;

    @RequiresPermissions("hr:report:quit:view")
    @GetMapping()
    public String resume() {
        return prefix + "/quit";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrEmp emp) {

        startPage();
        List<HrEmp> list = infoReportService.selectQuitList(emp);
        return getDataTable(list);
    }

    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list() {
//        startPage();
        List<Map<Integer, String>> list = infoReportService.selectQuitReportList();
        return AjaxResult.success(list);
    }

    /**
     * 导出离职申请报表
     */
    @RequiresPermissions("hr:report:quit:export")
    @Log(title = "离职申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrEmp emp)
    {
        List<HrEmp> list = infoReportService.selectQuitList(emp);
        ExcelUtil<HrEmp> util = new ExcelUtil<HrEmp>(HrEmp.class);
        return util.exportExcel(list, "quit");
    }

}