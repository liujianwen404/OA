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
import com.ruoyi.base.domain.HrFillClockRule;
import com.ruoyi.hr.service.IHrFillClockRuleService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 补卡规则Controller
 * 
 * @author xt
 * @date 2020-11-21
 */
@Controller
@RequestMapping("/hr/clock/rule")
public class HrFillClockRuleController extends BaseController
{
    private String prefix = "hr/clock/rule";

    @Autowired
    private IHrFillClockRuleService hrFillClockRuleService;

    @GetMapping()
    @RequiresPermissions("hr:clock:rule:view")
    public String rule()
    {
        return prefix + "/rule";
    }

    /**
     * 查询补卡规则列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrFillClockRule hrFillClockRule)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrFillClockRule.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrFillClockRule> list = hrFillClockRuleService.selectHrFillClockRuleList(hrFillClockRule);
        return getDataTable(list);
    }

    /**
     * 导出补卡规则列表
     */
    @Log(title = "补卡规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    @RequiresPermissions("hr:clock:rule:export")
    public AjaxResult export(HrFillClockRule hrFillClockRule)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrFillClockRule.setCreateId(ShiroUtils.getUserId());
        }
        List<HrFillClockRule> list = hrFillClockRuleService.selectHrFillClockRuleList(hrFillClockRule);
        ExcelUtil<HrFillClockRule> util = new ExcelUtil<HrFillClockRule>(HrFillClockRule.class);
        return util.exportExcel(list, "rule");
    }

    /**
     * 新增补卡规则
     */
    @RequiresPermissions("hr:clock:rule:add")
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        HrFillClockRule hrFillClockRule = new HrFillClockRule();
        List<HrFillClockRule> list = hrFillClockRuleService.selectHrFillClockRuleList(hrFillClockRule);
        if(list.size()>0){
            mmap.put("hrFillClockRule", list.get(0));
        }else{
            mmap.put("hrFillClockRule", hrFillClockRule);
        }
        return prefix + "/add";
    }

    /**
     * 新增保存补卡规则
     */
    @Log(title = "补卡规则", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrFillClockRule hrFillClockRule)
    {
        if(hrFillClockRule.getId() != null){
            return toAjax(hrFillClockRuleService.updateHrFillClockRule(hrFillClockRule));
        }
        return toAjax(hrFillClockRuleService.insertHrFillClockRule(hrFillClockRule));
    }

    /**
     * 修改补卡规则
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("hr:clock:rule:edit")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        HrFillClockRule hrFillClockRule = hrFillClockRuleService.selectHrFillClockRuleById(id);
        mmap.put("hrFillClockRule", hrFillClockRule);
        return prefix + "/edit";
    }

    /**
     * 修改保存补卡规则
     */
    @Log(title = "补卡规则", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrFillClockRule hrFillClockRule)
    {
        return toAjax(hrFillClockRuleService.updateHrFillClockRule(hrFillClockRule));
    }

    /**
     * 删除补卡规则
     */
    @Log(title = "补卡规则", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    @RequiresPermissions("hr:clock:rule:remove")
    public AjaxResult remove(String ids)
    {
        return toAjax(hrFillClockRuleService.deleteHrFillClockRuleByIds(ids));
    }
}
