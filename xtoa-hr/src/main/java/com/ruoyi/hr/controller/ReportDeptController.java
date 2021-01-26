package com.ruoyi.hr.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.Resume;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.hr.service.IDeptReportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 部门人数报表管理
 *
 * @author liujianwen
 */
@Controller
@RequestMapping("/hr/report/dept")
public class ReportDeptController extends BaseController {
    private String prefix = "hr/report/dept";

    @Autowired
    private IDeptReportService deptRrportService;

    @RequiresPermissions("hr:report:dept:view")
    @GetMapping()
    public String resume() {
        return prefix + "/dept";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrEmp emp) {
        startPage();
        List<HrEmp> list = deptRrportService.selectList(emp);
        return getDataTable(list);
    }

    @Log(title = "部门报表导出", businessType = BusinessType.EXPORT)
    @RequiresPermissions("hr:report:dept:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrEmp emp) {
        List<HrEmp> list = deptRrportService.selectList(emp);
        ExcelUtil<HrEmp> util = new ExcelUtil<>(HrEmp.class);
        return util.exportExcel(list, "报表数据");
    }

//    @Log(title = "报表管理", businessType = BusinessType.IMPORT)
//    @RequiresPermissions("hr:report:dept:import")
//    @PostMapping("/importData")
//    @ResponseBody
//    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
//        ExcelUtil<Resume> util = new ExcelUtil<>(Resume.class);
//        List<Resume> resumeList = util.importExcel(file.getInputStream());
//        String operName = ShiroUtils.getSysUser().getLoginName();
//        String message = deptRrportService.importResume(resumeList, updateSupport, operName);
//        return AjaxResult.success(message);
//    }

    @RequiresPermissions("hr:report:dept:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<Resume> util = new ExcelUtil<>(Resume.class);
        return util.importTemplateExcel("报表数据");
    }


//    /**
//     * 新增保存报表
//     */
//    @RequiresPermissions("hr:report:dept:add")
//    @Log(title = "报表管理", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(@Validated Resume resume) {
//
//        if (ResumeConstants.RESUME_PHONE_NOT_UNIQUE.equals(deptRrportService.checkPhoneUnique(resume))) {
//            return error("新增报表'" + resume.getResumeName() + "'失败，手机号码已存在");
//        } else if (ResumeConstants.RESUME_EMAIL_NOT_UNIQUE.equals(deptRrportService.checkEmailUnique(resume))) {
//            return error("新增报表'" + resume.getResumeName() + "'失败，邮箱账号已存在");
//        }
//        resume.setCreateBy(ShiroUtils.getLoginName());
//        return toAjax(deptRrportService.insertRrsume(resume));
//    }


//    /**
//     * 修改保存报表
//     */
//    @RequiresPermissions("hr:report:dept:edit")
//    @Log(title = "报表管理", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(@Validated Resume resume) {
//        deptRrportService.checkResumeAllowed(resume);
//        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(deptRrportService.checkPhoneUnique(resume))) {
//            return error("修改报表'" + resume.getResumeName() + "'失败，手机号码已存在");
//        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(deptRrportService.checkEmailUnique(resume))) {
//            return error("修改报表'" + resume.getResumeName() + "'失败，邮箱账号已存在");
//        }
//        resume.setUpdateBy(ShiroUtils.getLoginName());
//        return toAjax(deptRrportService.updateResume(resume));
//    }

//    /**
//     * 校验手机号码
//     */
//    @PostMapping("/checkPhoneUnique")
//    @ResponseBody
//    public String checkPhoneUnique(Resume resume) {
//        return deptRrportService.checkPhoneUnique(resume);
//    }
//
//    /**
//     * 校验email邮箱
//     */
//    @PostMapping("/checkEmailUnique")
//    @ResponseBody
//    public String checkEmailUnique(Resume resume) {
//        return deptRrportService.checkEmailUnique(resume);
//    }
//
//    /**
//     * 报表状态修改
//     */
//    @Log(title = "报表管理", businessType = BusinessType.UPDATE)
//    @RequiresPermissions("hr:report:dept:edit")
//    @PostMapping("/changeStatus")
//    @ResponseBody
//    public AjaxResult changeStatus(Resume resume) {
//        deptRrportService.checkResumeAllowed(resume);
//        return toAjax(deptRrportService.changeStatus(resume));
//    }
}