package com.ruoyi.centre.controller;

import com.ruoyi.centre.domain.CentreSysConfig;
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
import com.ruoyi.centre.domain.CentrePublicConfig;
import com.ruoyi.centre.service.ICentrePublicConfigService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 中台配置公共服务Controller
 * 
 * @author xt
 * @date 2020-10-23
 */
@Controller
@RequestMapping("/centre/centrePublicConfig")
public class CentrePublicConfigController extends BaseController
{
    private String prefix = "centre/centrePublicConfig";

    @Autowired
    private ICentrePublicConfigService centrePublicConfigService;

    @RequiresPermissions("centre:centrePublicConfig:view")
    @GetMapping()
    public String centrePublicConfig()
    {
        return prefix + "/centrePublicConfig";
    }

    /**
     * 查询中台配置公共服务列表
     */
    @RequiresPermissions("centre:centrePublicConfig:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CentrePublicConfig centrePublicConfig)
    {
        startPage();
        List<CentrePublicConfig> list = centrePublicConfigService.selectCentrePublicConfigList(centrePublicConfig);
        return getDataTable(list);
    }

    /**
     * 导出中台配置公共服务列表
     */
    @RequiresPermissions("centre:centrePublicConfig:export")
    @Log(title = "中台配置公共服务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CentrePublicConfig centrePublicConfig)
    {
        List<CentrePublicConfig> list = centrePublicConfigService.selectCentrePublicConfigList(centrePublicConfig);
        ExcelUtil<CentrePublicConfig> util = new ExcelUtil<CentrePublicConfig>(CentrePublicConfig.class);
        return util.exportExcel(list, "centrePublicConfig");
    }

    /**
     * 新增中台配置公共服务
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存中台配置公共服务
     */
    @RequiresPermissions("centre:centrePublicConfig:add")
    @Log(title = "中台配置公共服务", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CentrePublicConfig centrePublicConfig)
    {
        return toAjax(centrePublicConfigService.insertCentrePublicConfig(centrePublicConfig));
    }

    /**
     * 修改中台配置公共服务
     */
    @GetMapping("/edit/{serverId}")
    public String edit(@PathVariable("serverId") Integer serverId, ModelMap mmap)
    {
        CentrePublicConfig centrePublicConfig = centrePublicConfigService.selectCentrePublicConfigById(serverId);
        mmap.put("centrePublicConfig", centrePublicConfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存中台配置公共服务
     */
    @RequiresPermissions("centre:centrePublicConfig:edit")
    @Log(title = "中台配置公共服务", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CentrePublicConfig centrePublicConfig)
    {
        return toAjax(centrePublicConfigService.updateCentrePublicConfig(centrePublicConfig));
    }

    /**
     * 中台配置公共服务状态切换
     */
    @RequiresPermissions("centre:centrePublicConfig:edit")
    @Log(title = "中台配置公共服务状态切换", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(CentrePublicConfig centrePublicConfig)
    {
        return toAjax(centrePublicConfigService.updateCentrePublicConfig(centrePublicConfig));
    }
    /**
     * 删除中台配置公共服务
     */
    @RequiresPermissions("centre:centrePublicConfig:remove")
    @Log(title = "中台配置公共服务", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(centrePublicConfigService.deleteCentrePublicConfigByIds(ids));
    }
}
