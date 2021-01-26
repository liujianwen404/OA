package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysRole;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.redisService.repository.SysDeptRedisRepository;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 部门管理 服务实现
 * 
 * @author ruoyi
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService
{
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private SysDeptRedisRepository sysDeptRedisRepository;

    @PostConstruct
    public void closeRedis(){
        sysDeptRedisRepository.deleteAll();
    }
    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept)
    {
        return deptMapper.selectDeptList(dept);
    }

    public List<SysDept> selectDeptListAll(SysDept dept)
    {
        return deptMapper.selectDeptListAll(dept);
    }

    /**
     * 查询部门管理树
     * 
     * @param dept 部门信息
     * @return 所有部门信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<Ztree> selectDeptTree(SysDept dept)
    {
        List<SysDept> deptList = deptMapper.selectDeptList(dept);
        List<Ztree> ztrees = initZtree(deptList);
        return ztrees;
    }

    /**
     * 根据角色ID查询部门（数据权限）
     *
     * @param role 角色对象
     * @return 部门列表（数据权限）
     */
    @Override
    public List<Ztree> roleDeptTreeData(SysRole role)
    {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysDept> deptList = deptMapper.selectFirstDeptList(new SysDept());
        if (StringUtils.isNotNull(roleId))
        {
            List<String> roleDeptList = deptMapper.selectRoleDeptTree(roleId);
            ztrees = initZtree(deptList, roleDeptList);
        }
        else
        {
            ztrees = initZtree(deptList);
        }
        return ztrees;
    }

    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList)
    {
        return initZtree(deptList, null);
    }

    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysDept> deptList, List<String> roleDeptList)
    {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (SysDept dept : deptList)
        {
            if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()))
            {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getDeptId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getDeptName());
                ztree.setTitle(dept.getDeptName());
                if (isCheck)
                {
                    ztree.setChecked(roleDeptList.contains(dept.getDeptId() + dept.getDeptName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }

    /**
     * 查询部门人数
     * 
     * @param parentId 部门ID
     * @return 结果
     */
    @Override
    public int selectDeptCount(Long parentId)
    {
        SysDept dept = new SysDept();
        dept.setParentId(parentId);
        return deptMapper.selectDeptCount(dept);
    }

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId)
    {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0 ? true : false;
    }

    /**
     * 删除部门管理信息
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteDeptById(Long deptId)
    {

        int i = deptMapper.deleteDeptById(deptId);

        sysDeptRedisRepository.deleteById(deptId);

        return i;
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteById(Long deptId)
    {

        int i = deptMapper.deleteById(deptId);

        sysDeptRedisRepository.deleteById(deptId);

        return i;
    }

    /**
     * 新增保存部门信息
     * 
     * @param dept 部门信息
     * @param loginName
     * @return 结果
     */
    @Override
    public AjaxResult insertDept(SysDept dept, String loginName)
    {
        if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(checkDeptNameUnique(dept)))
        {
            throw new BusinessException("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(loginName);

        SysDept info = selectDeptByIdSimple(dept.getParentId());
        // 如果父节点不为"正常"状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new BusinessException("部门停用，不允许新增");
        }
        if (!iSysUserService.checkUserIsOK(dept.getLeader())){
            throw new BusinessException("部门负责人不可用，请重新指定负责人");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        int i = deptMapper.insertDept(dept);

        return i > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 修改保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDept(SysDept dept)
    {
        if (!iSysUserService.checkUserIsOK(dept.getLeader())){
            throw new BusinessException("部门负责人不可用，请重新指定负责人");
        }

        SysDept newParentDept = selectDeptByIdSimple(dept.getParentId());
        SysDept oldDept = selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);

        sysDeptRedisRepository.deleteById(dept.getDeptId());

        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()))
        {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatus(dept);
        }
        return result;
    }

    /**
     * 修改该部门的父级部门状态
     * 
     * @param dept 当前部门
     */
    private void updateParentDeptStatus(SysDept dept)
    {
        String updateBy = dept.getUpdateBy();
        dept = selectDeptByIdSimple(dept.getDeptId());
        dept.setUpdateBy(updateBy);
        deptMapper.updateDeptStatus(dept);

        sysDeptRedisRepository.deleteById(dept.getDeptId());

    }

    /**
     * 修改子元素关系
     * 
     * @param deptId 被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors)
    {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children)
        {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            deptMapper.updateDeptChildren(children);
            for (SysDept child : children)
            {
                sysDeptRedisRepository.deleteById(child.getDeptId());
            }
        }
    }

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Override
    public SysDept selectDeptById(Long deptId)
    {
        SysDept sysDept = selectDeptByIdSimple(deptId);
        if (sysDept != null){

            String depdName = sysDept.getDeptName();
            String[] split = sysDept.getAncestors().split(",");
            SysDept sysDeptparen = null;
            for (int i = split.length - 1; i > 1; i--) {
                sysDeptparen = selectDeptByIdSimple(Long.parseLong(split[i]));
                if (sysDeptparen != null){
                    depdName = sysDeptparen.getDeptName() + "-" + depdName;
                }
            }
            sysDept.setShowName(depdName);

            SysUser sysUser = iSysUserService.selectUserById(sysDept.getLeader());
            if (sysUser != null) {
//                SysDept dept = sysUser.getDept();
                sysDept.setLeaderName((sysDept == null ? "" : sysDept.getShowName()+ "(负责人：") + sysUser.getUserName() + ")");
            }
        }
        return sysDept;
    }

    @Override
    public SysDept selectDeptByIdSimple(Long deptId)
    {
        SysDept sysDept = null;
        Optional<SysDept> sysDeptRedis = sysDeptRedisRepository.findById(deptId);
        if (sysDeptRedis.isPresent()){
            sysDept = sysDeptRedis.get();
        }else {
            sysDept = deptMapper.selectDeptById(deptId);
            if (sysDept != null){
                sysDeptRedisRepository.save(sysDept);
            }
        }
        return sysDept;
    }
    

    /**
     * 校验部门名称是否唯一
     * 
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public String checkDeptNameUnique(SysDept dept)
    {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()
                && info.getDelFlag().equals("0"))
        {
            return UserConstants.DEPT_NAME_NOT_UNIQUE;
        }
        return UserConstants.DEPT_NAME_UNIQUE;
    }

    @Override
    public List<SysDept> getDeptAll() {

        List<SysDept> deptAll = deptMapper.getDeptAll();
        Iterator<SysDept> iterator = deptAll.iterator();
        SysDept sysDept = null;
        //缓存查询结果
        Map<String,SysDept> sysDeptMap = new HashMap<>();
        while (iterator.hasNext()){
            sysDept = iterator.next();
            if (sysDept.getParentId() == 0){
                iterator.remove();
                continue;
            }
            sysDeptMap.put(sysDept.getDeptId()+"",sysDept);

        }
        Iterator<SysDept> iterator2 = deptAll.iterator();
        SysDept sysDeptparen = null;
        while (iterator2.hasNext()){
            sysDept = iterator2.next();
            if (sysDept.getParentId() == 0){
                iterator2.remove();
                continue;
            }
            /*sysDept.setShowName(sysDept.getDeptName());*/

            String depdName = sysDept.getDeptName();
            String[] split = sysDept.getAncestors().split(",");
            for (int i = split.length - 1; i > 1; i--) {
                sysDeptparen = sysDeptMap.get(split[i]);
                if (sysDeptparen == null){
                    sysDeptparen = selectDeptByIdSimple(Long.parseLong(split[i]));
                    if (sysDeptparen != null){
                        //添加到map
                        sysDeptMap.put(split[i],sysDeptparen);
                    }
                }
                if (sysDeptparen != null){
                    depdName = sysDeptparen.getDeptName() + "-" + depdName;
                }
            }
            sysDept.setShowName(depdName);

        }

        return deptAll;
    }


    @Override
    public void checkUserIsLeader(String leader) {
        List<SysDept> sysDepts = deptMapper.selectDeptByLeader(leader);
        if (!sysDepts.isEmpty()){
            String dN = "";
            for (SysDept sysDept : sysDepts) {
                dN = dN + sysDept.getDeptName() + ";  ";
            }
            SysUser sysUser = iSysUserService.selectUserById(Long.parseLong(leader));
            throw new BusinessException("该用户是部门负责人不能直接修改用户状态 ,用户名：" + sysUser.getUserName() + "  。部门名称：" + dN);
        }
    }

    @Override
    public SysDept selectOneDeptByName(String deptName, Long pId) {

        return deptMapper.selectOneDeptByName(deptName,pId);
    }

    @Override
    public List<SysDept> roleDeptListById(Long deptId) {
        if (deptId == null){
            deptId = 100L;
        }
        return deptMapper.roleDeptListById(deptId);
    }

    @Override
    public List<SysDept> selectFirstDeptData() {
        return deptMapper.selectFirstDeptList(new SysDept());
    }

    @Override
    public List<SysDept> roleMenuDeptListById(Long roleId, Long menuId) {
        return deptMapper.roleMenuDeptListById(roleId,menuId);
    }

    @Override
    public List<Long> selectFirstDeptIdAndSon(Long deptId) {
        return deptMapper.selectFirstDeptIdAndSon(deptId);
    }

    @Override
    public SysDept selectCompanyByUserDeptId(Long deptId) {
        return deptMapper.selectCompanyByUserDeptId(deptId);
    }

    @Override
    public List<SysDept> selectDeptByUserDeptId(Long deptId) {
        return deptMapper.selectDeptByUserDeptId(deptId);
    }
}
