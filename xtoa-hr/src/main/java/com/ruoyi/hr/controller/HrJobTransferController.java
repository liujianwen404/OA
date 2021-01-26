package com.ruoyi.hr.controller;

import java.util.List;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.excel.EasyExcel;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.framework.web.service.DictService;
import com.ruoyi.hr.domain.HrNonManager;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysPostService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.hr.domain.HrJobTransfer;
import com.ruoyi.hr.service.IHrJobTransferService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 调动申请Controller
 * 
 * @author xt
 * @date 2020-05-22
 */
@Controller
@RequestMapping("/hr/jobTransfer")
public class HrJobTransferController extends BaseController
{
    private String prefix = "hr/jobTransfer";

    @Autowired
    private IHrJobTransferService hrJobTransferService;

    @Autowired
    private DictService dictService;

    @Autowired
    private ISysPostService postService;

    @RequiresPermissions("hr:jobTransfer:view")
    @GetMapping()
    public String jobTransfer(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/jobTransfer";
    }

    /**
     * 查询调动申请列表
     */
    @RequiresPermissions("hr:jobTransfer:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HrJobTransfer hrJobTransfer)
    {
        hrJobTransfer.setCreateId(ShiroUtils.getUserId());
        startPage();
        List<HrJobTransfer> list = hrJobTransferService.selectHrJobTransferList2(hrJobTransfer);
        return getDataTable(list);
    }

    @RequiresPermissions("hr:jobTransfer:viewManage")
    @GetMapping("/viewManage")
    public String jobTransferViewManage(ModelMap mmap)
    {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        return prefix + "/jobTransferViewManage";
    }

    /**
     * 查询调动申请列表
     */
//    @RequiresPermissions("hr:jobTransfer:listManage")
    @PostMapping("/listManage")
    @ResponseBody
    public TableDataInfo listManage(HrJobTransfer hrJobTransfer)
    {

        startPage();
        List<HrJobTransfer> list = hrJobTransferService.selectHrJobTransferListManage(hrJobTransfer);
        return getDataTable(list);
    }

    /**
     * 导出调动申请列表
     */
    @RequiresPermissions("hr:jobTransfer:export")
    @Log(title = "调动申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(HrJobTransfer hrJobTransfer)
    {
        List<HrJobTransfer> list = hrJobTransferService.selectHrJobTransferListManage(hrJobTransfer);

        for (HrJobTransfer jobTransfer : list) {
            jobTransfer.setCurrent(jobTransfer.getNowLeaderName() + postService.selectPostById(jobTransfer.getCurrentPostId()).getPostName());
            jobTransfer.setTransfer(jobTransfer.getNewleaderName() + postService.selectPostById(jobTransfer.getJobTransferPostId()).getPostName());

            jobTransfer.setTodoUserNameExcel(jobTransfer.getTodoUserName());

            jobTransfer.setAuditStatusExcel(dictService.getLabel("auditStatus", jobTransfer.getAuditStatus()+""));

        }

        String fileName = "异动_" + System.currentTimeMillis() + ".xlsx";
        String filePath = ExcelUtil.getAbsoluteFile(fileName);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(filePath, HrJobTransfer.class).sheet("异动").doWrite(list);
        return AjaxResult.success(fileName);
    }

    /**
     * 新增调动申请
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("userId",ShiroUtils.getSysUser().getUserId());
        mmap.put("hrAdminFlag",1);
        List<SysRole> roles = ShiroUtils.getSysUser().getRoles();
        if(roles != null && !roles.isEmpty()){
            for (SysRole role : roles) {
                if (role != null && role.getRoleKey().contains("hrAdmin")){
                    mmap.put("hrAdminFlag",0);
                }
            }
        }

        return prefix + "/add";
    }

    /**
     * 新增保存调动申请
     */
    @RequiresPermissions("hr:jobTransfer:add")
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
    @RequiresPermissions("hr:jobTransfer:edit")
    @Log(title = "调动申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(HrJobTransfer hrJobTransfer)
    {

        if (StringUtil.isNotEmpty(hrJobTransfer.getAttachment())){
            String[] split = hrJobTransfer.getAttachment().split(",");

            split = ArrayUtil.filter(split, (Filter<String>) s -> {
                if (StringUtil.isEmpty(s)){
                    return false;
                }
                return true;
            });

            String join = ArrayUtil.join(split, ",");
            hrJobTransfer.setAttachment(join);
        }else {
            hrJobTransfer.setAttachment(null);
        }

        return toAjax(hrJobTransferService.updateHrJobTransfer(hrJobTransfer));
    }

    /**
     * 删除调动申请
     */
    @RequiresPermissions("hr:jobTransfer:remove")
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
    @RequiresPermissions("hr:jobTransfer:remove")
    @RequestMapping(value = "/repeal", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult repeal(String instanceId,
                             HttpServletRequest request, String message) {
        try {
            return hrJobTransferService.repeal(instanceId, request, message);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{instanceId, e});
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     *提交
     * @return
     */
    @RequiresPermissions("hr:jobTransfer:edit")
    @RequestMapping(value = "/commit", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult commit( Long jobTransferId) {
        try {
            return hrJobTransferService.commit(jobTransferId);
        } catch (Exception e) {
            logger.error("error on complete task {}", new Object[]{jobTransferId, e});
            return AjaxResult.error("repeal失败");
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
                                   String module, String activitiId,String instanceId,HrJobTransfer jobTransfer) {

        hrJobTransferService.showVerifyDialog(taskId,module,activitiId,mmap,instanceId,jobTransfer);

        return "process/transfer" + "/taskHrVerify" ;
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
