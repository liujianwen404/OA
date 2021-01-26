package com.ruoyi.hr.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.Resume;
import com.ruoyi.hr.service.IResumeService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 简历管理
 *
 * @author liujianwen
 */
@Controller
@RequestMapping("/hr/resume")
public class HrResumeController extends BaseController {
    private String prefix = "hr/resume";

    @Autowired
    private IResumeService resumeService;

    @RequiresPermissions("hr:resume:view")
    @GetMapping()
    public String resume() {
        return prefix + "/resume";
    }

    @RequiresPermissions("hr:resume:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Resume resume) {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            resume.setCreateBy(ShiroUtils.getLoginName());
        }
        startPage();
        List<Resume> list = resumeService.selectResumeList(resume);
        return getDataTable(list);
    }

    @Log(title = "简历管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("hr:resume:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Resume resume) {
        List<Resume> list = resumeService.selectResumeList(resume);
        ExcelUtil<Resume> util = new ExcelUtil<>(Resume.class);
        return util.exportExcel(list, "简历数据");
    }

    @Log(title = "简历管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("hr:resume:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Resume> util = new ExcelUtil<>(Resume.class);
        List<Resume> resumeList = util.importExcel(file.getInputStream());
        String operName = ShiroUtils.getSysUser().getLoginName();
        String message = resumeService.importResume(resumeList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @RequiresPermissions("hr:resume:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<Resume> util = new ExcelUtil<>(Resume.class);
        return util.importTemplateExcel("简历数据");
    }


//    /**
//     * 新增保存简历
//     */
//    @RequiresPermissions("hr:resume:add")
//    @Log(title = "简历管理", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(@Validated Resume resume) {
//
//        if (ResumeConstants.RESUME_PHONE_NOT_UNIQUE.equals(resumeService.checkPhoneUnique(resume))) {
//            return error("新增简历'" + resume.getResumeName() + "'失败，手机号码已存在");
//        } else if (ResumeConstants.RESUME_EMAIL_NOT_UNIQUE.equals(resumeService.checkEmailUnique(resume))) {
//            return error("新增简历'" + resume.getResumeName() + "'失败，邮箱账号已存在");
//        }
//        resume.setCreateBy(ShiroUtils.getLoginName());
//        return toAjax(resumeService.insertRrsume(resume));
//    }


//    /**
//     * 修改保存简历
//     */
//    @RequiresPermissions("hr:resume:edit")
//    @Log(title = "简历管理", businessType = BusinessType.UPDATE)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(@Validated Resume resume) {
//        resumeService.checkResumeAllowed(resume);
//        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(resumeService.checkPhoneUnique(resume))) {
//            return error("修改简历'" + resume.getResumeName() + "'失败，手机号码已存在");
//        } else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(resumeService.checkEmailUnique(resume))) {
//            return error("修改简历'" + resume.getResumeName() + "'失败，邮箱账号已存在");
//        }
//        resume.setUpdateBy(ShiroUtils.getLoginName());
//        return toAjax(resumeService.updateResume(resume));
//    }

//    /**
//     * 校验手机号码
//     */
//    @PostMapping("/checkPhoneUnique")
//    @ResponseBody
//    public String checkPhoneUnique(Resume resume) {
//        return resumeService.checkPhoneUnique(resume);
//    }
//
//    /**
//     * 校验email邮箱
//     */
//    @PostMapping("/checkEmailUnique")
//    @ResponseBody
//    public String checkEmailUnique(Resume resume) {
//        return resumeService.checkEmailUnique(resume);
//    }
//
//    /**
//     * 简历状态修改
//     */
//    @Log(title = "简历管理", businessType = BusinessType.UPDATE)
//    @RequiresPermissions("hr:resume:edit")
//    @PostMapping("/changeStatus")
//    @ResponseBody
//    public AjaxResult changeStatus(Resume resume) {
//        resumeService.checkResumeAllowed(resume);
//        return toAjax(resumeService.changeStatus(resume));
//    }
}