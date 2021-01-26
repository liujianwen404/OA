package com.ruoyi.hr.controller;

import java.util.List;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.ICopyToService;
import com.ruoyi.system.domain.SysUser;
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
import com.ruoyi.base.domain.CopyTo;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 流程抄送关系Controller
 * 
 * @author liujianwen
 * @date 2020-06-06
 */
@Controller
@RequestMapping("/hr/copyTo")
public class CopyToController extends BaseController
{
    private String prefix = "hr/copyTo";

    @Autowired
    private ICopyToService copyToService;

    @RequiresPermissions("hr:copyTo:view")
    @GetMapping()
    public String copyTo()
    {
        return prefix + "/copyTo";
    }

    /**
     * 查询流程抄送关系列表
     */
    @RequiresPermissions("hr:copyTo:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CopyTo copyTo)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            copyTo.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<CopyTo> list = copyToService.selectCopyToList(copyTo);
        return getDataTable(list);
    }

    /**
     * 导出流程抄送关系列表
     */
    @RequiresPermissions("hr:copyTo:export")
    @Log(title = "流程抄送关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CopyTo copyTo)
    {
        List<CopyTo> list = copyToService.selectCopyToList(copyTo);
        ExcelUtil<CopyTo> util = new ExcelUtil<CopyTo>(CopyTo.class);
        return util.exportExcel(list, "copyTo");
    }

    /**
     * 新增流程抄送关系
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存流程抄送关系
     */
    @RequiresPermissions("hr:copyTo:add")
    @Log(title = "流程抄送关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CopyTo copyTo)
    {
        Long count = copyToService.selectProcessCopyerCount(copyTo);
        if (count >= 1) {
            return AjaxResult.warn("该流程抄送人配置已存在，请重新编辑！");
        }
        return toAjax(copyToService.insertCopyTo(copyTo));
    }

    /**
     * 修改流程抄送关系
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        CopyTo copyTo = copyToService.selectCopyToById(id);
        mmap.put("copyTo", copyTo);
        return prefix + "/edit";
    }

    /**
     * 修改保存流程抄送关系
     */
    @RequiresPermissions("hr:copyTo:edit")
    @Log(title = "流程抄送关系", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HttpServletRequest request, CopyTo copyTo)
    {
        String processKeyOld = request.getParameter("processKeyOld");
        Long count = copyToService.selectProcessCopyerCount(copyTo);
        if(copyTo.getProcessKey().equals(processKeyOld)){
            return toAjax(copyToService.updateCopyTo(copyTo));
        }
        if (count >= 1) {
            return AjaxResult.warn("该流程抄送人配置已存在，请重新编辑！");
        }
        return toAjax(copyToService.updateCopyTo(copyTo));
    }

    /**
     * 删除流程抄送关系
     */
    @RequiresPermissions("hr:copyTo:remove")
    @Log(title = "流程抄送关系", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(copyToService.deleteCopyToByIds(ids));
    }
}
