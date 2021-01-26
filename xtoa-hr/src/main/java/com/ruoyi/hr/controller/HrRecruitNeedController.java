package com.ruoyi.hr.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.domain.HrRecruitNeed;
import com.ruoyi.base.domain.VO.UserVO;
import com.ruoyi.hr.service.IHrInterpolateService;
import com.ruoyi.hr.service.IHrRecruitNeedService;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 招聘需求Controller
 * 
 * @author xt
 * @date 2020-05-28
 */
@Controller
@RequestMapping("/hr/recruitNeed")
public class HrRecruitNeedController extends BaseController
{
    private String prefix = "hr/recruitNeed";

    @Autowired
    private IHrRecruitNeedService hrRecruitNeedService;

    @Autowired
    private IHrInterpolateService hrInterpolateService;

    @RequiresPermissions("hr:recruitNeed:view")
    @GetMapping()
    public String recruitNeed()
    {
        return prefix + "/recruitNeed";
    }

    /**
     * 查询招聘需求列表
     */
    @RequiresPermissions("hr:recruitNeed:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrRecruitNeed hrRecruitNeed)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrRecruitNeed.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrRecruitNeed> list = hrRecruitNeedService.selectHrRecruitNeedList(hrRecruitNeed);
        return getDataTable(list);
    }

    /**
     * 导出招聘需求列表
     */
    @RequiresPermissions("hr:recruitNeed:export")
    @Log(title = "招聘需求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrRecruitNeed hrRecruitNeed)
    {
        List<HrRecruitNeed> list = hrRecruitNeedService.selectHrRecruitNeedList(hrRecruitNeed);
        ExcelUtil<HrRecruitNeed> util = new ExcelUtil<HrRecruitNeed>(HrRecruitNeed.class);
        return util.exportExcel(list, "recruitNeed");
    }

    /**
     * 新增招聘需求
     */
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {
        UserVO userVO = hrInterpolateService.getInfoForAdd();

        mmap.put("userVO",userVO);
        return prefix + "/add";
    }

    /**
     * 新增保存招聘需求
     */
    @RequiresPermissions("hr:recruitNeed:add")
    @Log(title = "招聘需求", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrRecruitNeed hrRecruitNeed)
    {
        return toAjax(hrRecruitNeedService.insertHrRecruitNeed(hrRecruitNeed));
    }

    /**
     * 详情招聘申请
     */
    @GetMapping("/detail/{recruitId}")
    public String detail(@PathVariable("recruitId") Long recruitId, ModelMap mmap)
    {
        HrRecruitNeed hrRecruitNeed = hrRecruitNeedService.selectHrRecruitNeedById(recruitId);
        mmap.put("hrRecruitNeed", hrRecruitNeed);
        return prefix + "/detail";
    }

    /**
     * 修改招聘需求
     */
    @GetMapping("/edit/{recruitNeedId}")
    public String edit(@PathVariable("recruitNeedId") Long recruitNeedId, ModelMap mmap)
    {
        HrRecruitNeed hrRecruitNeed = hrRecruitNeedService.selectHrRecruitNeedById(recruitNeedId);
        mmap.put("hrRecruitNeed", hrRecruitNeed);
        return prefix + "/edit";
    }

    /**
     * 修改保存招聘需求
     */
    @RequiresPermissions("hr:recruitNeed:edit")
    @Log(title = "招聘需求", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrRecruitNeed hrRecruitNeed)
    {
        return toAjax(hrRecruitNeedService.updateHrRecruitNeed(hrRecruitNeed));
    }

    /**
     * 删除招聘需求
     */
    @RequiresPermissions("hr:recruitNeed:remove")
    @Log(title = "招聘需求", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrRecruitNeedService.deleteHrRecruitNeedByIds(ids));
    }


    /**
     * 加载办理弹窗
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap,
                                   String module, String activitiId,String instanceId) {

        hrRecruitNeedService.showVerifyDialog(taskId,module,activitiId,mmap,instanceId);

        return prefix + "/recruitNeedTask";
    }


    /**
     * 完成任务
     *
     * @return
     */
    @RequestMapping(value = "/complete/{taskId}", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult complete(@PathVariable("taskId") String taskId,
                               HttpServletRequest request,
                               HrRecruitNeed hrRecruitNeed,
                               String comment,
                               String p_b_Approved,
                               String endHr) {
        try {
            return hrRecruitNeedService.complete(taskId, request, hrRecruitNeed, comment, p_b_Approved,endHr);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{taskId, e});
            return AjaxResult.error(e.getMessage());
        }
    }

}
