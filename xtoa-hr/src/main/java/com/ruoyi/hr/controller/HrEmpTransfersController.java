package com.ruoyi.hr.controller;

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
import com.ruoyi.base.domain.HrEmpTransfers;
import com.ruoyi.hr.service.IHrEmpTransfersService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工状态异动信息Controller
 * 
 * @author liujianwen
 * @date 2021-01-11
 */
@Controller
@RequestMapping("/hr/empTransfers")
public class HrEmpTransfersController extends BaseController
{
    private String prefix = "hr/empTransfers";

    @Autowired
    private IHrEmpTransfersService hrEmpTransfersService;

    @RequiresPermissions("hr:empTransfers:view")
    @GetMapping()
    public String empTransfers()
    {
        return prefix + "/empTransfers";
    }

    /**
     * 查询员工状态异动信息列表
     */
    @RequiresPermissions("hr:empTransfers:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrEmpTransfers hrEmpTransfers)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrEmpTransfers.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrEmpTransfers> list = hrEmpTransfersService.selectHrEmpTransfersList(hrEmpTransfers);
        return getDataTable(list);
    }

    /**
     * 导出员工状态异动信息列表
     */
    @RequiresPermissions("hr:empTransfers:export")
    @Log(title = "员工状态异动信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrEmpTransfers hrEmpTransfers)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrEmpTransfers.setCreateId(ShiroUtils.getUserId());
        }
        List<HrEmpTransfers> list = hrEmpTransfersService.selectHrEmpTransfersList(hrEmpTransfers);
        ExcelUtil<HrEmpTransfers> util = new ExcelUtil<HrEmpTransfers>(HrEmpTransfers.class);
        return util.exportExcel(list, "empTransfers");
    }

    /**
     * 新增员工状态异动信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存员工状态异动信息
     */
    @RequiresPermissions("hr:empTransfers:add")
    @Log(title = "员工状态异动信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrEmpTransfers hrEmpTransfers)
    {
        return toAjax(hrEmpTransfersService.insertHrEmpTransfers(hrEmpTransfers));
    }

    /**
     * 修改员工状态异动信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrEmpTransfers hrEmpTransfers = hrEmpTransfersService.selectHrEmpTransfersById(id);
        mmap.put("hrEmpTransfers", hrEmpTransfers);
        return prefix + "/edit";
    }

    /**
     * 修改保存员工状态异动信息
     */
    @RequiresPermissions("hr:empTransfers:edit")
    @Log(title = "员工状态异动信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrEmpTransfers hrEmpTransfers)
    {
        return toAjax(hrEmpTransfersService.updateHrEmpTransfers(hrEmpTransfers));
    }

    /**
     * 删除员工状态异动信息
     */
    @RequiresPermissions("hr:empTransfers:remove")
    @Log(title = "员工状态异动信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrEmpTransfersService.deleteHrEmpTransfersByIds(ids));
    }
}
