package com.ruoyi.hr.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.hr.domain.HrJobTransfer;
import com.ruoyi.hr.service.IHrJobTransferService;
import com.ruoyi.hr.service.IInfoReportService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 调动申请报表Controller
 * 
 * @author liujianwen
 * @date 2020-05-26
 */
@Controller
@RequestMapping("/hr/report/jobTransfer")
public class ReportJobTransferController extends BaseController
{
    private String prefix = "hr/report/jobTransfer";

    @Autowired
    private IInfoReportService infoReportService;

    @Autowired
    private IHrJobTransferService hrJobTransferService;

    @RequiresPermissions("hr:report:jobTransfer:view")
    @GetMapping()
    public String jobTransfer()
    {
        return prefix + "/jobTransfer";
    }

    /**
     * 查询调动申请列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrJobTransfer hrJobTransfer)
    {
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
//            hrJobTransfer.setCreateBy(ShiroUtils.getLoginName());
//        }
        startPage();
        List<HrJobTransfer> list = hrJobTransferService.selectHrJobTransferList(hrJobTransfer);
        return getDataTable(list);
    }

    @GetMapping("/list")
    @ResponseBody
    public AjaxResult list() {
//        startPage();
        List<Map<String, Object>> list = infoReportService.selectJobTransferReportList();
        return AjaxResult.success(list);
    }

    /**
     * 导出调动申请列表
     */
    @RequiresPermissions("hr:report:jobTransfer:export")
    @Log(title = "调动申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrJobTransfer hrJobTransfer)
    {
        List<HrJobTransfer> list = hrJobTransferService.selectHrJobTransferList(hrJobTransfer);
        ExcelUtil<HrJobTransfer> util = new ExcelUtil<HrJobTransfer>(HrJobTransfer.class);
        return util.exportExcel(list, "jobTransfer");
    }

    /**
     * 新增调动申请
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存调动申请
     */
    @RequiresPermissions("hr:report:jobTransfer:add")
    @Log(title = "调动申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(HrJobTransfer hrJobTransfer)
    {
        return toAjax(hrJobTransferService.insertHrJobTransfer(hrJobTransfer));
    }

    /**
     * 修改调动申请
     */
    @GetMapping("/edit/{jobTransferId}")
    public String edit(@PathVariable("jobTransferId") Long jobTransferId, ModelMap mmap)
    {
        HrJobTransfer hrJobTransfer = hrJobTransferService.selectHrJobTransferById(jobTransferId);
        mmap.put("hrJobTransfer", hrJobTransfer);
        return prefix + "/edit";
    }
    /**
     * 修改调动申请
     */
    @GetMapping("/detail/{jobTransferId}")
    public String detail(@PathVariable("jobTransferId") Long jobTransferId, ModelMap mmap)
    {
        HrJobTransfer hrJobTransfer = hrJobTransferService.selectHrJobTransferById(jobTransferId);
        mmap.put("hrJobTransfer", hrJobTransfer);
        return prefix + "/detail";
    }

    /**
     * 修改保存调动申请
     */
    @RequiresPermissions("hr:report:jobTransfer:edit")
    @Log(title = "调动申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrJobTransfer hrJobTransfer)
    {
        return toAjax(hrJobTransferService.updateHrJobTransfer(hrJobTransfer));
    }

    /**
     * 删除调动申请
     */
    @RequiresPermissions("hr:report:jobTransfer:remove")
    @Log(title = "调动申请", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(hrJobTransferService.deleteHrJobTransferByIds(ids));
    }

    /**
     *撤销
     * @return
     */
    @RequiresPermissions("hr:report:jobTransfer:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal(String instanceId,
                             HttpServletRequest request, String message) {
        try {
            return hrJobTransferService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error("repeal失败");
        }
    }
    /**
     *提交
     * @return
     */
    @RequiresPermissions("hr:report:jobTransfer:edit")
    @RequestMapping(value = "/commit", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult commit( Long jobTransferId) {
        try {
            return hrJobTransferService.commit(jobTransferId);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{jobTransferId, e});
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 加载办理弹窗
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap,
                                   String module, String activitiId, String instanceId, HrJobTransfer jobTransfer) {

        hrJobTransferService.showVerifyDialog(taskId,module,activitiId,mmap,instanceId, jobTransfer);

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
                               HrJobTransfer hrJobTransfer,
                               String comment,
                               String p_B_hrApproved) {
        try {
            return hrJobTransferService.complete(taskId, request, hrJobTransfer, comment, p_B_hrApproved);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{taskId, e});
            return AjaxResult.error("完成任务失败");
        }
    }


}
