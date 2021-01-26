package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.HrRecruit;
import com.ruoyi.hr.service.HrRecruitService;
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
 * 招聘报表管理
 *
 * @author liujianwen
 */
@Controller
@RequestMapping("/hr/report/recruit")
public class ReportRecruitController extends BaseController {
    private String prefix = "hr/report/recruit";

    @Autowired
    private IInfoReportService infoReportService;

    @Autowired
    private HrRecruitService tHrRecruitService;

    @RequiresPermissions("hr:report:recruit:view")
    @GetMapping()
    public String resume() {
        return prefix + "/recruit";
    }

    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list() {
//        startPage();
        List<Map<Integer, String>> list = infoReportService.selectRecruitList();
        return AjaxResult.success(list);
    }

    /**
     * 查询招聘申请列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrRecruit hrRecruit)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrRecruit.setCreateId(ShiroUtils.getUserId());
//        }
        startPage();
        List<HrRecruit> list = tHrRecruitService.selectTHrRecruitList(hrRecruit);
        return getDataTable(list);
    }

    /**
     * 导出
     */
    @RequiresPermissions("hr:report:recruit:export")
    @Log(title = "招聘报表导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrRecruit hrRecruit)
    {
        List<HrRecruit> list = tHrRecruitService.selectTHrRecruitList(hrRecruit);
        ExcelUtil<HrRecruit> util = new ExcelUtil<>(HrRecruit.class);
        return util.exportExcel(list, "recruit");
    }

}