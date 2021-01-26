package com.ruoyi.web.controller.hr;

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
import com.ruoyi.base.domain.KnowledgeEnshrine;
import com.ruoyi.hr.service.IKnowledgeEnshrineService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 知识访问Controller
 * 
 * @author xt
 * @date 2020-06-09
 */
@Controller
@RequestMapping("/hr/enshrine")
public class KnowledgeEnshrineController extends BaseController
{
    private String prefix = "hr/enshrine";

    @Autowired
    private IKnowledgeEnshrineService knowledgeEnshrineService;

    @RequiresPermissions("hr:knowledge:view")
    @GetMapping()
    public String enshrine()
    {
        return prefix + "/enshrine";
    }

    /**
     * 查询知识访问列表
     */
    @RequiresPermissions("hr:knowledge:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(KnowledgeEnshrine knowledgeEnshrine)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            knowledgeEnshrine.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<KnowledgeEnshrine> list = knowledgeEnshrineService.selectKnowledgeEnshrineList(knowledgeEnshrine);
        return getDataTable(list);
    }

    /**
     * 导出知识访问列表
     */
    @RequiresPermissions("hr:knowledge:export")
    @Log(title = "知识访问", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(KnowledgeEnshrine knowledgeEnshrine)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            knowledgeEnshrine.setCreateId(ShiroUtils.getUserId());
        }
        List<KnowledgeEnshrine> list = knowledgeEnshrineService.selectKnowledgeEnshrineList(knowledgeEnshrine);
        ExcelUtil<KnowledgeEnshrine> util = new ExcelUtil<KnowledgeEnshrine>(KnowledgeEnshrine.class);
        return util.exportExcel(list, "enshrine");
    }

    /**
     * 新增知识访问
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存知识访问
     */
    @RequiresPermissions("hr:knowledge:add")
    @Log(title = "知识访问", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(KnowledgeEnshrine knowledgeEnshrine)
    {
        return toAjax(knowledgeEnshrineService.insertKnowledgeEnshrine(knowledgeEnshrine));
    }

    /**
     * 修改知识访问
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {

        KnowledgeEnshrine knowledgeEnshrine = knowledgeEnshrineService.selectKnowledgeEnshrineByExpm(id);
        mmap.put("knowledgeEnshrine", knowledgeEnshrine);
        return prefix + "/edit";
    }

    /**
     * 修改保存知识访问
     */
    @RequiresPermissions("hr:knowledge:edit")
    @Log(title = "知识访问", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(KnowledgeEnshrine knowledgeEnshrine)
    {
        return toAjax(knowledgeEnshrineService.updateKnowledgeEnshrine(knowledgeEnshrine));
    }

    /**
     * 删除知识访问
     */
    @RequiresPermissions("hr:knowledge:remove")
    @Log(title = "知识访问", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(knowledgeEnshrineService.deleteKnowledgeEnshrineByIds(ids));
    }
}
