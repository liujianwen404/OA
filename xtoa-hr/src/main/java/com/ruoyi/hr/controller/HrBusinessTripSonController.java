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
import com.ruoyi.base.domain.HrBusinessTripSon;
import com.ruoyi.hr.service.IHrBusinessTripSonService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 出差申请子Controller
 * 
 * @author liujianwen
 * @date 2020-07-03
 */
@Controller
@RequestMapping("/hr/businessTripSon")
public class HrBusinessTripSonController extends BaseController
{
    private String prefix = "hr/businessTripSon";

    @Autowired
    private IHrBusinessTripSonService hrBusinessTripSonService;

    @RequiresPermissions("hr:businessTripSon:view")
    @GetMapping()
    public String son()
    {
        return prefix + "/son";
    }

    /**
     * 查询出差申请子列表
     */
    @RequiresPermissions("hr:businessTripSon:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrBusinessTripSon hrBusinessTripSon)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrBusinessTripSon.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrBusinessTripSon> list = hrBusinessTripSonService.selectHrBusinessTripSonList(hrBusinessTripSon);
        return getDataTable(list);
    }

    /**
     * 导出出差申请子列表
     */
    @RequiresPermissions("hr:businessTripSon:export")
    @Log(title = "出差申请子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrBusinessTripSon hrBusinessTripSon)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrBusinessTripSon.setCreateId(ShiroUtils.getUserId());
        }
        List<HrBusinessTripSon> list = hrBusinessTripSonService.selectHrBusinessTripSonList(hrBusinessTripSon);
        ExcelUtil<HrBusinessTripSon> util = new ExcelUtil<HrBusinessTripSon>(HrBusinessTripSon.class);
        return util.exportExcel(list, "son");
    }

    /**
     * 新增出差申请子
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存出差申请子
     */
    @RequiresPermissions("hr:businessTripSon:add")
    @Log(title = "出差申请子表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrBusinessTripSon hrBusinessTripSon)
    {
        return toAjax(hrBusinessTripSonService.insertHrBusinessTripSon(hrBusinessTripSon));
    }

    /**
     * 修改出差申请子
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrBusinessTripSon hrBusinessTripSon = hrBusinessTripSonService.selectHrBusinessTripSonById(id);
        mmap.put("hrBusinessTripSon", hrBusinessTripSon);
        return prefix + "/edit";
    }

    /**
     * 修改保存出差申请子
     */
    @RequiresPermissions("hr:businessTripSon:edit")
    @Log(title = "出差申请子表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrBusinessTripSon hrBusinessTripSon)
    {
        return toAjax(hrBusinessTripSonService.updateHrBusinessTripSon(hrBusinessTripSon));
    }

    /**
     * 删除出差申请子
     */
    @RequiresPermissions("hr:businessTripSon:remove")
    @Log(title = "出差申请子表", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrBusinessTripSonService.deleteHrBusinessTripSonByIds(ids));
    }

    /**
     * 出差申请修改页面，删除出差申请行程信息
     */
    @Log(title = "出差申请子表", businessType = BusinessType.DELETE)
    @PostMapping( "/removeOld")
    @ResponseBody
    public AjaxResult removeOld(Long id)
    {
        return toAjax(hrBusinessTripSonService.deleteHrBusinessTripSonById(id));
    }
}
