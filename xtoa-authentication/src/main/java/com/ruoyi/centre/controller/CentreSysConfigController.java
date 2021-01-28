package com.ruoyi.centre.controller;

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
import com.ruoyi.centre.domain.CentreSysConfig;
import com.ruoyi.centre.service.ICentreSysConfigService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 中台配置系统Controller
 * 
 * @author xt
 * @date 2020-10-22
 */
@Controller
@RequestMapping("/centre/centreSysConfig")
public class CentreSysConfigController extends BaseController
{
    private String prefix = "centre/centreSysConfig";

    @Autowired
    private ICentreSysConfigService centreSysConfigService;

    @RequiresPermissions("centre:centreSysConfig:view")
    @GetMapping()
    public String centreSysConfig()
    {
        return prefix + "/centreSysConfig";
    }

    /**
     * 查询中台配置系统列表
     */
    @RequiresPermissions("centre:centreSysConfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CentreSysConfig centreSysConfig)
    {
        /*if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            centreSysConfig.setCreateId(ShiroUtils.getUserId());
        }*/
        startPage();
        List<CentreSysConfig> list = centreSysConfigService.selectCentreSysConfigList(centreSysConfig);
        return getDataTable(list);
    }

    /**
     * 导出中台配置系统列表
     */
    @RequiresPermissions("centre:centreSysConfig:export")
    @Log(title = "中台配置系统", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CentreSysConfig centreSysConfig)
    {
        /*if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            centreSysConfig.setCreateId(ShiroUtils.getUserId());
        }*/
        List<CentreSysConfig> list = centreSysConfigService.selectCentreSysConfigList(centreSysConfig);
        ExcelUtil<CentreSysConfig> util = new ExcelUtil<CentreSysConfig>(CentreSysConfig.class);
        return util.exportExcel(list, "centreSysConfig");
    }

    /**
     * 新增中台配置系统
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存中台配置系统
     */
    @RequiresPermissions("centre:centreSysConfig:add")
    @Log(title = "中台配置系统", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CentreSysConfig centreSysConfig)
    {
        return toAjax(centreSysConfigService.insertCentreSysConfig(centreSysConfig));
    }

    /**
     * 修改中台配置系统
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        CentreSysConfig centreSysConfig = centreSysConfigService.selectCentreSysConfigById(id);
        mmap.put("centreSysConfig", centreSysConfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存中台配置系统
     */
    @RequiresPermissions("centre:centreSysConfig:edit")
    @Log(title = "中台配置系统", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CentreSysConfig centreSysConfig)
    {
        return toAjax(centreSysConfigService.updateCentreSysConfig(centreSysConfig));
    }

    /**
     * 中台配置系统状态切换
     */
    @RequiresPermissions("centre:centreSysConfig:edit")
    @Log(title = "中台配置系统状态切换", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(CentreSysConfig centreSysConfig)
    {
        return toAjax(centreSysConfigService.updateCentreSysConfig(centreSysConfig));
    }

    /**
     * 删除中台配置系统
     */
    @RequiresPermissions("centre:centreSysConfig:remove")
    @Log(title = "中台配置系统", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(centreSysConfigService.deleteCentreSysConfigByIds(ids));
    }
}
