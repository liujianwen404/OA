package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.UserTreeVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 用户 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private ISysPostService iSysPostService;


    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user)
    {
        return userMapper.selectAllocatedList(user);
    }
    @Override
    public List<SysUser> selectAllocatedListByKey(String roleKey)
    {
        return userMapper.selectAllocatedListByKey(roleKey);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByLoginName(String userName)
    {
        return userMapper.selectUserByLoginName(userName);
    }

    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByPhoneNumber(String phoneNumber)
    {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByEmail(String email)
    {
        return userMapper.selectUserByEmail(email);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 通过用户ID查询用户和角色关联
     * 
     * @param userId 用户ID
     * @return 用户和角色关联列表
     */
    @Override
    public List<SysUserRole> selectUserRoleByUserId(Long userId)
    {
        return userRoleMapper.selectUserRoleByUserId(userId);
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    /*@Override
    @Transactional
    public int deleteUserById(Long userId)
    {
        iSysDeptService.checkUserIsLeader(userId+"");
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }*/

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(String ids) throws BusinessException
    {
        Long[] userIds = Convert.toLongArray(ids);
        for (Long userId : userIds)
        {
            iSysDeptService.checkUserIsLeader(userId+"");

            checkUserAllowed(new SysUser(userId));

            //插入历史数据
            /*SysUser sysUser = userMapper.selectUserById(userId);
            if (sysUser != null){
                SysUserHistory sysUserHistory = new SysUserHistory();
                BeanUtil.copyProperties(sysUser,sysUserHistory);
                sysUserHistoryMapper.insertSelective(sysUserHistory);
            }*/
        }


        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {

        if (StringUtils.isEmpty(user.getPhonenumber())){
            if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(checkPhoneUnique(user)))
            {
                throw  new RuntimeException("手机号码已存在:"+user.getPhonenumber());
            }
        }

        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user.getUserId(), user.getRoleIds());
        return rows;
    }

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUser user)
    {
        user.setUserType(UserConstants.REGISTER_USER_TYPE);
        return userMapper.insertUser(user) > 0;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        Long userId = user.getUserId();

        checkIsLeaderForUpate(user);

        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user.getUserId(), user.getRoleIds());
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUserFromImport(SysUser user)
    {
        Long userId = user.getUserId();

        checkIsLeaderForUpate(user);

        /*// 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user.getUserId(), user.getRoleIds());*/
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }


    private void checkIsLeaderForUpate(SysUser userSrc) {
        SysUser sysUser = userMapper.selectUserById(userSrc.getUserId());
        if (sysUser != null && !sysUser.getStatus().equals(userSrc.getStatus())){
            iSysDeptService.checkUserIsLeader(userSrc.getUserId()+"");
        }
    }

    /**
     * 修改用户个人详细信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 用户授权角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    @Override
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetUserPwd(SysUser user)
    {
        return updateUserInfo(user);
    }

    /**
     * 新增用户角色信息
     *
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotNull(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     * 
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 校验登录名称是否唯一
     * 
     * @param loginName 用户名
     * @return
     */
    @Override
    public String checkLoginNameUnique(String loginName)
    {
        int count = userMapper.checkLoginNameUnique(loginName);
        if (count > 0)
        {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }
        return UserConstants.USER_PHONE_UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user)
    {
        /*Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }*/
        return UserConstants.USER_EMAIL_UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new BusinessException("不允许操作超级管理员用户");
        }
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(Long userId)
    {
        List<SysRole> list = roleMapper.selectRolesByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list)
        {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(Long userId)
    {
        List<SysPost> list = postMapper.selectPostsByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list)
        {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new BusinessException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByLoginName(user.getLoginName());
                if (StringUtils.isNull(u))
                {
                    user.setPassword(Md5Utils.hash(user.getLoginName() + password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getLoginName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getLoginName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getLoginName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 用户状态修改
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int changeStatus(SysUser user)
    {
        checkIsLeaderForUpate(user);

        return userMapper.updateUser(user);
    }

    @Override
    public List<UserTreeVo> selectUserTree(SysUser sysUser ) {
        LinkedList<UserTreeVo> ztrees = getUserTreeVos(sysUser);
        //添加部门
        List<SysDept> sysDepts = iSysDeptService.selectDeptList(new SysDept());
        UserTreeVo ztree2 = null;
        for (int i = sysDepts.size() - 1; i >= 0; i--) {
            ztree2 = new UserTreeVo();
            ztree2.setDeptId(sysDepts.get(i).getDeptId());
            ztree2.setpId(sysDepts.get(i).getParentId());
            ztree2.setName(sysDepts.get(i).getDeptName());
            ztrees.addFirst(ztree2);
        }

        return ztrees;
    }

    @Override
    public LinkedList<UserTreeVo> getUserTreeVos(SysUser sysUser) {
        List<SysUser> userList = selectUserListZtree(sysUser);
        return getUserTrees(userList);
    }
    @Override
    public LinkedList<UserTreeVo> getUserTrees(List<SysUser> userList) {
        LinkedList<UserTreeVo> ztrees = new LinkedList<>();
        SysDept sysDept = null;
        for (SysUser user : userList)
        {
            if (UserConstants.DICT_NORMAL.equals(user.getStatus()))
            {
                UserTreeVo ztree = new UserTreeVo();
                ztree.setId(user.getUserId());
                ztree.setEmail(user.getEmail());
                ztree.setPhonenumber(user.getPhonenumber());
                sysDept = user.getDept();
                String name = "";
                if (sysDept != null){
//                    name = sysDept.getDeptName();
//                    ztree.setDeptId(sysDept.getDeptId());
                    ztree.setpId(sysDept.getDeptId());

                }
                ztree.setEmpName(user.getUserName());
//                ztree.setName(name +":"+ user.getUserName());
                ztree.setName(user.getUserName());
                ztree.setTitle(user.getLoginName());
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    @Override
    public List<UserTreeVo> selectUserByIds(Set<Long> collect) {

        List<SysUser> userList = userMapper.selectUserByIds(collect);
        return getUserTrees(userList);
    }


    @Override
    public List<SysUser> selectUserListZtree(SysUser sysUser) {
        return userMapper.selectUserListZtree(sysUser);
    }

    /**
     * 通过员工登录用户名查询该员工部门负责人的用户名
     *
     * @param loginName 员工登录用户名
     * @return 结果
     */
    @Override
    public String selectDeptLeaderByLoginName(String loginName) {
        return userMapper.selectDeptLeaderByLoginName(loginName);
    }

    @Override
    public Boolean checkUserIsOK(Long userId){
        SysUser sysUser = userMapper.selectUserById(userId);
        if (sysUser == null || !sysUser.getStatus().equals("0") || !sysUser.getDelFlag().equals("0")){
            return  false;
        }
        return true;
    }

    @Override
    public void updateUserDeptPost(Long jobTransferDeptId, Long jobTransferPostId, Long userId) {
        SysUser sysUser = selectUserById(userId);
        if (sysUser != null){
            sysUser.setDeptId(jobTransferDeptId);
            updateUserInfo(sysUser);
            //查询岗位信息
            List<SysPost> sysPosts = postMapper.selectPostsByUserId(userId);
            boolean postFlag = false;
            for (SysPost sysPost : sysPosts) {
                if (sysPost.getPostId().equals(jobTransferPostId)){
                    postFlag = true;
                    break;
                }
            }
            if (!postFlag){
                //没有该岗位的数据就插入
                SysUserPost sysUserPost = new SysUserPost();
                sysUserPost.setPostId(jobTransferPostId);
                sysUserPost.setUserId(userId);
                iSysPostService.insertUserPost(sysUserPost);
            }

        }
    }

    @Override
    public List<SysUser> getUserRoleList(String roleName) {
        return userMapper.getUserRoleList(roleName);
    }

    @Override
    public SysUser getSysUserByDingUserId(String dingUserId) {
        return userMapper.getSysUserByDingUserId(dingUserId);
    }

    @Override
    public SysUser selectUserByPhoneNumberLike(String mobile) {
        return userMapper.selectUserByPhoneNumberLike(mobile);
    }

    @Override
    public List<SysUser> selectUserListWithDingUserIdNull() {
        return userMapper.selectUserListWithDingUserIdNull();
    }
}
