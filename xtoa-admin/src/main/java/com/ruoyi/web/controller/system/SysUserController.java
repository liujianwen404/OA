package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.OnlineStatus;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.shiro.session.OnlineSession;
import com.ruoyi.framework.shiro.session.OnlineSessionDAO;
import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.domain.ProjectEmp;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.IProjectEmpService;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.UserTreeVo;
import com.ruoyi.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.framework.util.ShiroUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    private String prefix = "system/user";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    @Autowired
    private IProjectEmpService projectEmpService;

    @Autowired
    private ISysDeptService deptService;


    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    @RequiresPermissions("system:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user)
    {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = ShiroUtils.getSysUser().getLoginName();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @RequiresPermissions("system:user:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

   /* *//**
     * 新增用户
     *//*
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("roles", roleService.selectRoleAll());
        mmap.put("posts", postService.selectPostAll());
        return prefix + "/add";
    }

    *//**
     * 新增保存用户
     *//*
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysUser user)
    {
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName())))
        {
            return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(userService.insertUser(user));
    }*/

    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.selectUserById(userId));
        mmap.put("roles", roleService.selectRolesByUserId(userId));
        mmap.put("posts", postService.selectPostsByUserId(userId));
        return prefix + "/edit";
    }

    /**
     * 修改保存用户
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional
    public AjaxResult editSave(@Validated SysUser user,HttpServletRequest request)
    {
        userService.checkUserAllowed(user);
        if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return error("修改用户'" + user.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return error("修改用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(ShiroUtils.getLoginName());

        int i = userService.updateUser(user);
        if (i > 0){
            SysUser sysUser = userService.selectUserById(user.getUserId());
            HrEmp hrEmp = hrEmpService.selectTHrEmpByUserId(sysUser.getUserId());

            hrEmp.setDeptId(user.getDeptId());
            //查询岗位信息
            List<SysPost> sysPosts = postService.selectPostsByUserId(user.getUserId());
//            boolean postFlag = false;
            for (SysPost sysPost : sysPosts) {
                if (sysPost.isFlag() && !sysPost.getPostId().equals(hrEmp.getPostId())){
//                    postFlag = true;
                    hrEmp.setPostId(sysPost.getPostId());
                    break;
                }
            }
           /* if (!sysPosts.isEmpty() && !postFlag){
                hrEmp.setPostId(sysPosts.get(0).getPostId());
            }*/
            hrEmp.setSex(user.getSex());
            hrEmp.setPhonenumber(user.getPhonenumber());
            hrEmp.setEmail(user.getEmail());
            hrEmp.setEmpName(user.getUserName());
            hrEmpService.updateTHrEmp(hrEmp,request);

            logoutUser(sysUser.getLoginName());
        }

        return toAjax(i);
    }

    public void logoutUser(String nameUser) {
        //修改权限强退用户
        SysUserOnline userOnline = new SysUserOnline();
        userOnline.setLoginName(nameUser);
        List<SysUserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
        for (SysUserOnline sessionId : list)
        {
            SysUserOnline online = userOnlineService.selectOnlineById(sessionId.getSessionId());
            if (online != null)
            {
                OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
                if (onlineSession != null && !sessionId.equals(ShiroUtils.getSessionId()))
                {
                    onlineSession.setStatus(OnlineStatus.off_line);
                    onlineSessionDAO.update(onlineSession);
                    online.setStatus(OnlineStatus.off_line);
                    userOnlineService.saveOnline(online);
                }
            }
        }
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwdSave(SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        if (userService.resetUserPwd(user) > 0)
        {
            if (ShiroUtils.getUserId() == user.getUserId())
            {
                ShiroUtils.setSysUser(userService.selectUserById(user.getUserId()));
            }
            return success();
        }
        return error();
    }

    /**
     * 进入授权角色页
     */
    @GetMapping("/authRole/{userId}")
    public String authRole(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        SysUser user = userService.selectUserById(userId);
        // 获取用户所属的角色列表
        List<SysUserRole> userRoles = userService.selectUserRoleByUserId(userId);
        mmap.put("user", user);
        mmap.put("userRoles", userRoles);
        return prefix + "/authRole";
    }

    /**
     * 用户授权角色
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PostMapping("/authRole/insertAuthRole")
    @ResponseBody
    @Transactional
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds)
    {
        userService.insertUserAuth(userId, roleIds);
        SysUser sysUser = userService.selectUserById(userId);
        logoutUser(sysUser.getLoginName());
        return success();
    }

    @RequiresPermissions("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            int i = userService.deleteUserByIds(ids);
            if (i > 0){
                Long[] userIds = Convert.toLongArray(ids);
                hrEmpService.deleteTHrEmpByUserIds(userIds);
            }
             return toAjax(i);
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysUser user)
    {
        return userService.checkLoginNameUnique(user.getLoginName());
    }

    /**
     * 校验手机号码
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(SysUser user)
    {
        return userService.checkPhoneUnique(user);
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(SysUser user)
    {
        return userService.checkEmailUnique(user);
    }

    /**
     * 用户状态修改
     */
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysUser user)
    {
        userService.checkUserAllowed(user);
        return toAjax(userService.changeStatus(user));
    }

    @RequestMapping("/selectUserById")
    @ResponseBody
    public AjaxResult selectUserById(Long userId)
    {

        SysUser sysUser = userService.selectUserById(userId);

        StringBuilder stringBuilder = new StringBuilder("");
        if (sysUser.getDept() != null){
            String[] split = sysUser.getDept().getAncestors().split(",");
            SysDept sysDept = null;
            for (int i = 1; i < split.length; i++) {
                sysDept = deptService.selectDeptById(Long.parseLong(split[i]));
                if (sysDept != null){
                    stringBuilder.append(sysDept.getDeptName() + ":");
                }
            }
            stringBuilder.append(sysUser.getDept().getDeptName());
            sysUser.getDept().setDeptName(stringBuilder.toString());
        }

        List<SysPost> sysPosts = postService.selectPostsByUserIdSimple(sysUser.getUserId());
        if (sysPosts != null && sysPosts.size() > 0){
            sysUser.setSysPostVO(sysPosts.get(0));
        }

        HrEmp hrEmp = hrEmpService.selectTHrEmpById(sysUser.getUserId());
        if (hrEmp != null){
            sysUser.setNonManagerDateVO(hrEmp.getNonManagerDate());
        }


        return AjaxResult.success(sysUser);
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<UserTreeVo> treeData(HttpServletRequest request)
    {

        String projectId = request.getParameter("projectId");
        String projectPlanId = request.getParameter("projectPlanId");
        String type = request.getParameter("type")+"";
        if (StringUtils.isNotEmpty(projectId) && !projectId.equals("null") && !type.equals("0")){

            if (StringUtils.isNotEmpty(projectPlanId) && !projectPlanId.equals("null")){
                //只查询项目成员
                ProjectEmp projectEmp = new ProjectEmp();
                projectEmp.setProjectId(Long.parseLong(projectId));
                projectEmp.setProjectPlanId(Long.parseLong(projectPlanId));
                List<UserTreeVo> ztrees = projectEmpService.selectProjectEmpToUserTreeVo(projectEmp);
                return ztrees;
            }else {
                //只查询项目成员
                ProjectEmp projectEmp = new ProjectEmp();
                projectEmp.setProjectId(Long.parseLong(projectId));
                List<UserTreeVo> ztrees = projectEmpService.selectProjectEmpToUserTreeVo(projectEmp);
                return ztrees;
            }

        }

        List<UserTreeVo> ztrees = userService.selectUserTree(new SysUser());
        return ztrees;
    }
    /**
     * 加载部门列表树
     */
    @GetMapping("/selectUserTree")
    public String selectUserTree(ModelMap modelMap,Long projectId,Long projectPlanId)
    {
        modelMap.put("projectId",projectId);
        modelMap.put("projectPlanId",projectPlanId);
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/selectUserTreeCheckbox")
    public String selectUserTreeCheckbox(HttpServletRequest request, ModelMap mmap)
    {
        String userIds = request.getParameter("userIds");
        String msg = request.getParameter("msg");
        String countStr = request.getParameter("count");
        String projectId = request.getParameter("projectId");
        String type = request.getParameter("type");
        Integer count = 6;
        if (StringUtils.isEmpty(msg)){
            msg = "每套流程最多只能设置6名抄送人！";
        }
        if (StringUtils.isNotEmpty(countStr)){
            count = Integer.parseInt(countStr);
        }

        mmap.put("userIds",userIds);
        mmap.put("count",count);
        mmap.put("msg",msg);
        mmap.put("projectId",projectId);

        mmap.put("type",type);
        return prefix + "/treeCheckbox";
    }

    /**
     * 加载部门员工列表树
     */
    @PostMapping("/selectTree")
    public String selectTree(String userIds,ModelMap mmap)
    {
        mmap.put("userIds",userIds);
        return prefix + "/deptAndUserTree";
    }

    /**
     * 加载部门员工列表树
     */
    @GetMapping("/selectTree")
    public String selectTree()
    {
        return prefix + "/deptAndUserTree";
    }

}