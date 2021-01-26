package com.ruoyi.hr.controller;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.ruoyi.framework.util.ShiroUtils;
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
import com.ruoyi.hr.domain.Knowledge;
import com.ruoyi.hr.service.IKnowledgeService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 信息Controller
 * 
 * @author xt
 * @date 2020-06-05
 */
@Controller
@RequestMapping("/hr/knowledge")
public class KnowledgeController extends BaseController
{
    private String prefix = "hr/knowledge";

    @Autowired
    private IKnowledgeService knowledgeService;

    @RequiresPermissions("hr:knowledge:view")
    @GetMapping()
    public String knowledge(ModelMap modelMap)
    {

        Random random = new Random();
        modelMap.put("random",random.nextInt(2));
        return prefix + "/knowledge";
    }

    @RequiresPermissions("hr:knowledge:view")
    @GetMapping("/knowledgeInfo")
    public String knowledgeInfo(ModelMap modelMap,String isFrom)
    {
        modelMap.put("isFrom",isFrom);
        return prefix + "/knowledgeInfo";
    }


    /**
     * 查询信息列表
     */
//    @RequiresPermissions("hr:knowledge:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Knowledge knowledge,String isFrom)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId()) && !"main".equals(isFrom)) {
            knowledge.setCreateId(ShiroUtils.getUserId());
        }else {
            knowledge.setStatus("0");
        }

        startPage();
        List<Knowledge> list = knowledgeService.selectKnowledgeList(knowledge);
        return getDataTable(list);
    }

    /**
     *
     */
    @PostMapping("/getRanking")
    @ResponseBody
    public TableDataInfo getRanking()
    {
        try {
            return knowledgeService.getRanking();
        }catch (Exception e){
            e.printStackTrace();
            TableDataInfo tableDataInfo = new TableDataInfo();
            tableDataInfo.setContent(e.getMessage());
            return tableDataInfo;
        }
    }

    /**
     * 查询信息列表
     */
    @RequiresPermissions("hr:knowledge:list")
    @PostMapping("/listBrowse")
    @ResponseBody
    public TableDataInfo listBrowse(Knowledge knowledge,String isFrom,Integer pageSize,Integer pageNum)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            knowledge.setCreateId(ShiroUtils.getUserId());
        }

//        startPage();
        pageNum = (pageNum - 1) * pageSize;
        knowledge.setPageNum(pageNum);
        knowledge.setPageSize(pageSize);
        List<Knowledge> list = knowledgeService.selectKnowledgeVisitList(knowledge);

        return getDataTable(list);
    }


    /**
     * 查询信息列表
     */
    @RequiresPermissions("hr:knowledge:list")
    @PostMapping("/listEnshrine")
    @ResponseBody
    public TableDataInfo listEnshrine(Knowledge knowledge,String isFrom,Integer pageSize,Integer pageNum)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            knowledge.setCreateId(ShiroUtils.getUserId());
        }

//        startPage();
        pageNum = (pageNum - 1) * pageSize;
        knowledge.setPageNum(pageNum);
        knowledge.setPageSize(pageSize);
        List<Knowledge> list = knowledgeService.selectKnowledgeEnshrineList(knowledge);

        return getDataTable(list);
    }


    @PostMapping("/selectOne")
    @ResponseBody
    public TableDataInfo selectOne(Knowledge knowledge,String isLook)
    {
        TableDataInfo rspData = new TableDataInfo();
        try {
            Knowledge know = knowledgeService.selectById(knowledge.getId(),isLook);
            rspData.setCode(0);
            rspData.setData(know);
            return rspData;
        }catch (Exception e) {
            rspData.setCode(1);
            rspData.setContent(e.getMessage());
            return rspData;
        }
    }

    /**
     * 获取知识详情，并增加一条查看记录
     * @param knowledge
     * @param isLook
     * @return
     */
    @PostMapping("/getAndAddVisitRecord")
    @ResponseBody
    public TableDataInfo getAndAddVisitRecord(Knowledge knowledge,String isLook)
    {
        TableDataInfo rspData = new TableDataInfo();
        try {
            Knowledge know = knowledgeService.getAndAddVisitRecord(knowledge.getId(),isLook);
            rspData.setCode(0);
            rspData.setData(know);
            return rspData;
        }catch (Exception e) {
            rspData.setCode(1);
            rspData.setContent(e.getMessage());
            return rspData;
        }
    }

    /**
     * 导出信息列表
     */
    @RequiresPermissions("hr:knowledge:export")
    @Log(title = "信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Knowledge knowledge)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            knowledge.setCreateId(ShiroUtils.getUserId());
        }
        List<Knowledge> list = knowledgeService.selectKnowledgeList(knowledge);
        ExcelUtil<Knowledge> util = new ExcelUtil<Knowledge>(Knowledge.class);
        return util.exportExcel(list, "knowledge");
    }

    /**
     * 新增信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存信息
     */
    @RequiresPermissions("hr:knowledge:add")
    @Log(title = "信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Knowledge knowledge,MultipartFile coverFile)
    {
        try {
            return toAjax(knowledgeService.insertKnowledge(knowledge,coverFile));
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Knowledge knowledge = knowledgeService.selectKnowledgeById(id);
        mmap.put("knowledge", knowledge);
        return prefix + "/edit";
    }

    /**
     * 修改保存信息
     */
    @RequiresPermissions("hr:knowledge:edit")
    @Log(title = "信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Knowledge knowledge)
    {
        return toAjax(knowledgeService.updateKnowledge(knowledge));
    }

    /**
     * 删除信息
     */
    @RequiresPermissions("hr:knowledge:remove")
    @Log(title = "删除信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(knowledgeService.deleteKnowledgeByIds(ids));
    }
    /**
     * 删除信息
     */
    @RequiresPermissions("hr:knowledge:remove")
    @Log(title = "删除信息浏览记录", businessType = BusinessType.OTHER)
    @PostMapping( "/removeBrowse")
    @ResponseBody
    public AjaxResult removeBrowse(String ids)
    {
        return toAjax(knowledgeService.deleteKnowledgeVisitByIds(ids));
    }


    /**
     * 信息
     */
    @RequiresPermissions("hr:knowledge:list")
    @Log(title = "收藏信息", businessType = BusinessType.OTHER)
    @PostMapping( "/enshrine")
    @ResponseBody
    public AjaxResult enshrine(Knowledge knowledge)
    {
        return toAjax(knowledgeService.enshrine(knowledge));
    }
}
