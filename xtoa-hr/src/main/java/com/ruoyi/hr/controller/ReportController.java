package com.ruoyi.hr.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.base.domain.Resume;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.hr.service.IInfoReportService;
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
 * 报表统计
 *
 * @author liujianwen
 */
@Controller
@RequestMapping("/hr/report/info")
public class ReportController extends BaseController {
    private String prefix = "hr/report/info";

    @Autowired
    private IInfoReportService infoReportService;

    @RequiresPermissions("hr:report:info:view")
    @GetMapping()
    public String resume() {
        return prefix + "/info";
    }

    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list() {
        startPage();
        List<Map<Integer, String>> list = infoReportService.selectEmpList();
        return AjaxResult.success(list);
    }

    @RequiresPermissions("hr:report:info:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<Resume> util = new ExcelUtil<>(Resume.class);
        return util.importTemplateExcel("报表数据");
    }


}