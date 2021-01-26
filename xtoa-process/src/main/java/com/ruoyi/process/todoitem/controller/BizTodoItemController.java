package com.ruoyi.process.todoitem.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.process.leave.service.IBizLeaveService;
import com.ruoyi.process.todoitem.domain.BizTodoItem;
import com.ruoyi.process.todoitem.service.IBizTodoItemService;
import com.ruoyi.system.domain.SysUser;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 待办事项Controller
 *
 * @author Xianlu Tech
 * @date 2019-11-08
 */
@Controller
@RequestMapping("/process/todoitem")
public class BizTodoItemController extends BaseController {
    private String prefix = "process/todoitem";

    @Autowired
    private IBizTodoItemService bizTodoItemService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IBizLeaveService bizLeaveService;


    @GetMapping("/todoListView")
    public String todoListView(ModelMap mmap) {
        BizTodoItem todoItem = new BizTodoItem();
        todoItem.setTodoUserId(ShiroUtils.getLoginName());
        todoItem.setIsHandle("0");
        List<BizTodoItem> todoItemList = bizTodoItemService.selectBizTodoItemList(todoItem);
        mmap.put("todoItemList", todoItemList);
        return prefix + "/todoList";
    }

    /**
     * 加载办理弹窗
     * @param taskId
     * @param mmap
     * @return
     */
    @GetMapping("/showVerifyDialog/{taskId}")
    public String showVerifyDialog(@PathVariable("taskId") String taskId, ModelMap mmap,
                                   String module, String formPageName) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        mmap.put("taskId", taskId);
        String verifyName = task.getTaskDefinitionKey().substring(0, 1).toUpperCase() + task.getTaskDefinitionKey().substring(1);
        return "process/" + module + "/" + formPageName;
    }

    @RequiresPermissions("process:todoitem:view")
    @GetMapping()
    public String todoitem(ModelMap mmap,String module,String isStarUserName,String isFrom) {
        mmap.put("currentUser", ShiroUtils.getSysUser());
        mmap.put("module",module);
        mmap.put("isStarUserName",isStarUserName);
        mmap.put("isFrom",isFrom);
        return prefix + "/todoitem";
    }

    /**
     * 查询待办事项列表
     */
    @RequiresPermissions("process:todoitem:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BizTodoItem bizTodoItem,String isStarUserName,String isFrom) {
        bizTodoItem.setIsHandle("0");
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            List<String> moduleList = bizTodoItemService.selectModules(ShiroUtils.getUserId());
            logger.info("查询当前登录用户抄送人设置的流程模块[{}]",moduleList);
            if (CollectionUtils.isNotEmpty(moduleList)) {
                if (StringUtils.isNotBlank(isFrom) && isFrom.equals("main")) {
                    startPage();
                    String[] mudoles = new String[moduleList.size()];
                    bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
                    List<BizTodoItem> list = bizTodoItemService.selectMainCopyTodoItemList(bizTodoItem,moduleList.toArray(mudoles));
                    return getDataTable(list);
                }
                startPage();
                String[] mudoles = new String[moduleList.size()];
                bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
                List<BizTodoItem> list = bizTodoItemService.selectCopyTodoItemList(bizTodoItem,moduleList.toArray(mudoles));
                return getDataTable(list);
            }
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
        }
        if (StringUtils.isNotEmpty(isStarUserName)){
            bizTodoItem.setStarUserName(ShiroUtils.getLoginName());
        }

        if (StringUtils.isNotEmpty(isFrom) && "main".equals(isFrom)){
            //admin主页查看只查看自己代办的
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
        }

        startPage();
        List<BizTodoItem> list = bizTodoItemService.selectBizTodoItemList(bizTodoItem);
        return getDataTable(list);
    }

    @RequiresPermissions("process:todoitem:doneView")
    @GetMapping("/doneitemView")
    public String doneitem(ModelMap mmap,String status,String isStarUserName) {
        mmap.put("status",status);
        mmap.put("isStarUserName",isStarUserName);
        return prefix + "/doneitem";
    }

    /**
     * 查询已办事项列表
     */
    @RequiresPermissions("process:todoitem:doneList")
    @PostMapping("/doneList")
    @ResponseBody
    public TableDataInfo doneList(BizTodoItem bizTodoItem,String isStarUserName) {
        bizTodoItem.setIsHandle("1");
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            /*List<String> moduleList = bizTodoItemService.selectModules(ShiroUtils.getUserId());
            logger.info("查询当前登录用户抄送人设置的流程模块[{}]",moduleList);
            if (CollectionUtils.isNotEmpty(moduleList)) {
                startPage();
                String[] mudoles = new String[moduleList.size()];
                bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
                List<BizTodoItem> list = bizTodoItemService.selectCopyTodoItemList(bizTodoItem,moduleList.toArray(mudoles));
                return getDataTable(list);
            }*/
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
        }
        if (StringUtils.isNotEmpty(isStarUserName)){
            bizTodoItem.setStarUserName(ShiroUtils.getLoginName());
        }
        startPage();
        List<BizTodoItem> list = bizTodoItemService.selectBizTodoItemList(bizTodoItem);
        return getDataTable(list);
    }

    /**
     * 导出待办事项列表
     */
    @RequiresPermissions("process:todoitem:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BizTodoItem bizTodoItem,String isStarUserName,String isFrom) {

        bizTodoItem.setIsHandle("0");
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            List<String> moduleList = bizTodoItemService.selectModules(ShiroUtils.getUserId());
            logger.info("查询当前登录用户抄送人设置的流程模块[{}]",moduleList);
            if (CollectionUtils.isNotEmpty(moduleList)) {
                startPage();
                String[] mudoles = new String[moduleList.size()];
                bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
                List<BizTodoItem> list = bizTodoItemService.selectCopyTodoItemList(bizTodoItem,moduleList.toArray(mudoles));
                ExcelUtil<BizTodoItem> util = new ExcelUtil<BizTodoItem>(BizTodoItem.class);
                return util.exportExcel(list, "todoitem");
            }
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
        }
        if (StringUtils.isNotEmpty(isStarUserName)){
            bizTodoItem.setStarUserName(ShiroUtils.getLoginName());
        }

        if (StringUtils.isNotEmpty(isFrom) && "main".equals(isFrom)){
            //admin主页查看只查看自己代办的
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
        }
        List<BizTodoItem> list = bizTodoItemService.selectBizTodoItemList(bizTodoItem);
        ExcelUtil<BizTodoItem> util = new ExcelUtil<BizTodoItem>(BizTodoItem.class);
        return util.exportExcel(list, "todoitem");
    }

    /**
     * 导出已办事项列表
     */
    @RequiresPermissions("process:todoitem:doneExport")
    @PostMapping("/doneExport")
    @ResponseBody
    public AjaxResult doneExport(BizTodoItem bizTodoItem,String isStarUserName) {
        bizTodoItem.setIsHandle("1");
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
        }
        if (StringUtils.isNotEmpty(isStarUserName)){
            bizTodoItem.setStarUserName(ShiroUtils.getLoginName());
        }
        List<BizTodoItem> list = bizTodoItemService.selectBizTodoItemList(bizTodoItem);
        ExcelUtil<BizTodoItem> util = new ExcelUtil<BizTodoItem>(BizTodoItem.class);
        return util.exportExcel(list, "todoitem");
    }

    /**
     * 新增待办事项
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存待办事项
     */
    @RequiresPermissions("process:todoitem:add")
    @Log(title = "待办事项", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BizTodoItem bizTodoItem) {
        return toAjax(bizTodoItemService.insertBizTodoItem(bizTodoItem));
    }

    /**
     * 修改待办事项
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        BizTodoItem bizTodoItem = bizTodoItemService.selectBizTodoItemById(id);
        mmap.put("bizTodoItem", bizTodoItem);
        return prefix + "/edit";
    }

    /**
     * 修改保存待办事项
     */
    @RequiresPermissions("process:todoitem:edit")
    @Log(title = "待办事项", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BizTodoItem bizTodoItem) {
        return toAjax(bizTodoItemService.updateBizTodoItem(bizTodoItem));
    }

    /**
     * 删除待办事项
     */
    @RequiresPermissions("process:todoitem:remove")
    @Log(title = "待办事项", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bizTodoItemService.deleteBizTodoItemByIds(ids));
    }

    /**
     * 查询待办事项列表数量
     */
    @PostMapping("/listCount")
    @ResponseBody
    public TableDataInfo listCount(BizTodoItem bizTodoItem) {
        bizTodoItem.setIsHandle("0");
//        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            bizTodoItem.setTodoUserId(ShiroUtils.getLoginName());
//        }
        Map<String,Object> map = bizTodoItemService.selectBizTodoItemListCount(bizTodoItem);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setData(map);
        return rspData;
    }

    /**
     * 查询待办事项列表数量
     */
    @PostMapping("/listCountForEmp")
    @ResponseBody
    public TableDataInfo listCountForEmp(BizTodoItem bizTodoItem) {
        bizTodoItem.setIsHandle("0");
        if (!SysUser.isAdmin(ShiroUtils.getUserId())) {
            bizTodoItem.setStarUserName(ShiroUtils.getLoginName());
            bizTodoItem.setTodoUserName(ShiroUtils.getLoginName());
        }
        Map<String,Object> map = bizTodoItemService.listCountForEmp(bizTodoItem);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setData(map);
        return rspData;
    }

    @GetMapping("/forward")
    public String forward(String taskId,String todoUserId,ModelMap map) {
        map.put("taskId", taskId);
        map.put("todoUserId", todoUserId);
        return prefix + "/forward";
    }

    /**
     * 添加转发
     */
    @PostMapping("/addForward")
    @ResponseBody
    public AjaxResult addForward(HttpServletRequest request,BizTodoItem bizTodoItem) {
        String taskId = request.getParameter("taskId");
        String todoUserId = request.getParameter("todoUserId");
        String userName = request.getParameter("userName");
        String empName = request.getParameter("empName");
        String userId = request.getParameter("userId");
        return toAjax(bizTodoItemService.taskEntrust(taskId,todoUserId,userId,userName,empName));
    }

    /**
     * 加载部门员工列表树
     */
    @GetMapping("/tree")
    public String selectTree()
    {
        return prefix + "/tree";
    }

}
