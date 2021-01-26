package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.HrContract;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.Resume;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.IDeptReportService;
import com.ruoyi.hr.service.IInfoReportService;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 转正报表管理
 *
 * @author liujianwen
 */
@Controller
@RequestMapping("/hr/report/regular")
public class ReportRegularController extends BaseController {
    private String prefix = "hr/report/regular";

    @Autowired
    private IInfoReportService infoReportService;

    @RequiresPermissions("hr:report:regular:view")
    @GetMapping()
    public String resume() {
        return prefix + "/regular";
    }

    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list() {
//        startPage();
        List<Map<Integer, String>> list = infoReportService.selectRegularList();
        return AjaxResult.success(list);
    }

    /**
     * 返回转正人数报表的list列表
     * @return
     */
    @PostMapping("/listPage")
    @ResponseBody
    public TableDataInfo listPage(HrEmp emp) {
        startPage();
        List<HrEmp> list = infoReportService.selectEmpRegularList(emp);
        return getDataTable(list);
    }

    /**
     * 导出
     */
    @RequiresPermissions("hr:report:regular:export")
    @Log(title = "转正报表导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrEmp emp)
    {
        List<HrEmp> list = infoReportService.selectEmpRegularList(emp);
        ExcelUtil<HrEmp> util = new ExcelUtil<HrEmp>(HrEmp.class);
        return util.exportExcel(list, "regular");
    }


}