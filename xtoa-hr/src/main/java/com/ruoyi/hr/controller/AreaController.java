package com.ruoyi.hr.controller;

import com.ruoyi.base.domain.Area;
import com.ruoyi.base.domain.ProjectEmp;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.service.IAreaService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.vo.UserTreeVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 地区Controller
 * 
 * @author xt
 * @date 2020-10-26
 */
@Controller
@RequestMapping("/hr/area")
public class AreaController extends BaseController
{
    private String prefix = "hr/area";

    @Autowired
    private IAreaService areaService;

    @RequiresPermissions("hr:area:view")
    @GetMapping()
    public String area()
    {
        return prefix + "/area";
    }

    /**
     * 查询地区列表
     */
    @RequiresPermissions("hr:area:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Area area)
    {
        startPage();
        List<Area> list = areaService.selectAreaList(area);
        return getDataTable(list);
    }

    /**
     * 导出地区列表
     */
    @RequiresPermissions("hr:area:export")
    @Log(title = "地区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Area area)
    {
        List<Area> list = areaService.selectAreaList(area);
        ExcelUtil<Area> util = new ExcelUtil<Area>(Area.class);
        return util.exportExcel(list, "area");
    }

    /**
     * 新增地区
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存地区
     */
    @RequiresPermissions("hr:area:add")
    @Log(title = "地区", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Area area)
    {
        return toAjax(areaService.insertArea(area));
    }

    /**
     * 修改地区
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Area area = areaService.selectAreaById(id);
        mmap.put("area", area);
        return prefix + "/edit";
    }

    /**
     * 修改保存地区
     */
    @RequiresPermissions("hr:area:edit")
    @Log(title = "地区", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Area area)
    {
        return toAjax(areaService.updateArea(area));
    }

    /**
     * 删除地区
     */
    @RequiresPermissions("hr:area:remove")
    @Log(title = "地区", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(areaService.deleteAreaByIds(ids));
    }



    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(Area area)
    {
        List<Ztree> ztrees = areaService.selectAreaListTree(area);
        return ztrees;
    }
    /**
     * 加载部门列表树
     */
    @GetMapping("/selectTree")
    public String selectTree(Long id,ModelMap modelMap)
    {
        modelMap.put("id",id);
        return prefix + "/tree";
    }



    /**
     * 加载部门列表树CheckBox
     */
    @GetMapping("/treeDataCheckBox")
    @ResponseBody
    public List<Ztree> treeDataCheckBox(Long roleId)
    {
        List<Ztree> ztrees = areaService.selectAreaListTreeCheckBox(new Area(),roleId);
        return ztrees;
    }
    /**
     * 加载部门列表树CheckBox
     */
    @GetMapping("/selectTreeCheckBox")
    public String selectTreeCheckBox(Long roleId,ModelMap modelMap)
    {
        modelMap.put("roleId",roleId);
        return prefix + "/treeCheckBox";
    }

}
