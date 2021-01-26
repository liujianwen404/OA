package com.ruoyi.hr.controller;

import java.util.List;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.base.domain.VO.UserVO;
import com.ruoyi.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.hr.domain.HrInterpolate;
import com.ruoyi.hr.service.IHrInterpolateService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 内推申请Controller
 * 
 * @author vivi07
 * @date 2020-05-12
 */
@Controller
@RequestMapping("/hr/interpolate")
public class HrInterpolateController extends BaseController
{
    private String prefix = "hr/interpolate";

    @Autowired
    private IHrInterpolateService hrInterpolateService;

    @RequiresPermissions("hr:interpolate:view")
    @GetMapping()
    public String interpolate()
    {
        return prefix + "/interpolate";
    }

    /**
     * 查询内推申请列表
     */
    @RequiresPermissions("hr:interpolate:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrInterpolate hrInterpolate)
    {
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            hrInterpolate.setCreateId(ShiroUtils.getUserId());
        }
        startPage();
        List<HrInterpolate> list = hrInterpolateService.selectHrInterpolateList(hrInterpolate);
        return getDataTable(list);
    }

    /**
     * 导出内推申请列表
     */
    @RequiresPermissions("hr:interpolate:export")
    @Log(title = "内推申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrInterpolate hrInterpolate)
    {
        List<HrInterpolate> list = hrInterpolateService.selectHrInterpolateList(hrInterpolate);
        ExcelUtil<HrInterpolate> util = new ExcelUtil<HrInterpolate>(HrInterpolate.class);
        return util.exportExcel(list, "interpolate");
    }

    /**
     * 新增内推申请
     */
    @GetMapping("/add")
    public String add( ModelMap mmap)
    {
        UserVO userVO = hrInterpolateService.getInfoForAdd();
        mmap.put("infoForAdd",userVO);
        return prefix + "/add";
    }

    /**
     * 新增保存内推申请
     */
    @RequiresPermissions("hr:interpolate:add")
    @Log(title = "内推申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrInterpolate hrInterpolate,@RequestParam(value = "resumeFile", required = false) MultipartFile multipart)
    {
        try {
            return hrInterpolateService.insertHrInterpolate(hrInterpolate,multipart);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改内推申请
     */
    @GetMapping("/edit/{interpolateId}")
    public String edit(@PathVariable("interpolateId") Long interpolateId, ModelMap mmap)
    {
        HrInterpolate hrInterpolate = hrInterpolateService.selectHrInterpolateById(interpolateId);
        mmap.put("hrInterpolate", hrInterpolate);
        return prefix + "/edit";
    }

    /**
     * 修改保存内推申请
     */
    @RequiresPermissions("hr:interpolate:edit")
    @Log(title = "内推申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrInterpolate hrInterpolate)
    {
        return toAjax(hrInterpolateService.updateHrInterpolate(hrInterpolate));
    }

    /**
     * 删除内推申请
     */
    @RequiresPermissions("hr:interpolate:remove")
    @Log(title = "内推申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrInterpolateService.deleteHrInterpolateByIds(ids));
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

        hrInterpolateService.showVerifyDialog(taskId,module,activitiId,mmap,instanceId);

        return "process/" + module + "/taskHrVerify" ;
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
                               HrInterpolate hrInterpolate,
                               String comment,
                               String p_B_hrApproved) {
        try {
            return hrInterpolateService.complete(taskId, request, hrInterpolate, comment, p_B_hrApproved);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{taskId, e});
            return AjaxResult.error("完成任务失败");
        }
    }

}
