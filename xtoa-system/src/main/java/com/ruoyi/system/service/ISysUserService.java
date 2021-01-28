package com.ruoyi.system.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.vo.UserTreeVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户 业务层
 * 
 * @author ruoyi
 */
public interface ISysUserService
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * 根据条件分页查询已分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    List<SysUser> selectAllocatedListByKey(String roleKey);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByLoginName(String userName);

    /**
     * 通过手机号码查询用户
     * 
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    public SysUser selectUserByPhoneNumber(String phoneNumber);

    /**
     * 通过邮箱查询用户
     * 
     * @param email 邮箱
     * @return 用户对象信息
     */
    public SysUser selectUserByEmail(String email);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);

    /**
     * 通过用户ID查询用户和角色关联
     * 
     * @param userId 用户ID
     * @return 用户和角色关联列表
     */
    public List<SysUserRole> selectUserRoleByUserId(Long userId);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
//    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteUserByIds(String ids) throws Exception;

    /**
     * 保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 注册用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public boolean registerUser(SysUser user);

    /**
     * 保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);

    int updateUserFromImport(SysUser user);

    /**
     * 修改用户详细信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUserInfo(SysUser user);

    /**
     * 用户授权角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色组
     */
    public void insertUserAuth(Long userId, Long[] roleIds);

    /**
     * 修改用户密码信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int resetUserPwd(SysUser user);

    /**
     * 校验用户名称是否唯一
     * 
     * @param loginName 登录名称
     * @return 结果
     */
    public String checkLoginNameUnique(String loginName);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public String checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    public String checkEmailUnique(SysUser user);

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    public void checkUserAllowed(SysUser user);

    /**
     * 根据用户ID查询用户所属角色组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public String selectUserRoleGroup(Long userId);

    /**
     * 根据用户ID查询用户所属岗位组
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public String selectUserPostGroup(Long userId);

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    /**
     * 用户状态修改
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int changeStatus(SysUser user);

    List<UserTreeVo> selectUserTree(SysUser sysUser);

    LinkedList<UserTreeVo> getUserTreeVos(SysUser sysUser);

    LinkedList<UserTreeVo> getUserTrees(List<SysUser> userList);

    List<UserTreeVo> selectUserByIds(Set<Long> collect);

    List<SysUser> selectUserListZtree(SysUser sysUser);

    /**
     * 通过员工登录用户名查询该员工部门负责人的用户名
     *
     * @param loginName 员工登录用户名
     * @return 结果
     */
    public String selectDeptLeaderByLoginName(String loginName);

    /**
     * 检查用户是否可用
     * @param userId
     * @return
     */
    public Boolean checkUserIsOK(Long userId);

    /**
     * 修改用户部门与岗位信息
     * @param jobTransferDeptId
     * @param jobTransferPostId
     * @param userId
     */
    void updateUserDeptPost(Long jobTransferDeptId, Long jobTransferPostId, Long userId);

    List<SysUser> getUserRoleList(String hr);

    SysUser getSysUserByDingUserId(String dingUserId);

    /**
     * 导入员工数据时有些是有多个的，like处理
     * @param mobile
     * @return
     */
    SysUser selectUserByPhoneNumberLike(String mobile);

    /**
     * 查询所有dingUserId为空的用户
     * @return
     */
    List<SysUser> selectUserListWithDingUserIdNull();
}
