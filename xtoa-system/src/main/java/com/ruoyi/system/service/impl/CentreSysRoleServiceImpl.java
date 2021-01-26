package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.CentreSysRole;
import com.ruoyi.system.mapper.CentreSysRoleMapper;
import com.ruoyi.system.service.ICentreSysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;

/**
 * OA角色映射对接系统Service业务层处理
 * 
 * @author xt
 * @date 2020-10-28
 */
@Service
public class CentreSysRoleServiceImpl implements ICentreSysRoleService 
{

    private static final Logger logger = LoggerFactory.getLogger(CentreSysRoleServiceImpl.class);

    @Autowired
    private CentreSysRoleMapper centreSysRoleMapper;

    /**
     * 查询OA角色映射对接系统
     * 
     * @param id OA角色映射对接系统ID
     * @return OA角色映射对接系统
     */
    @Override
    public CentreSysRole selectCentreSysRoleById(Long id)
    {
        return centreSysRoleMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询OA角色映射对接系统列表
     * 
     * @param centreSysRole OA角色映射对接系统
     * @return OA角色映射对接系统
     */
    @Override
    public List<CentreSysRole> selectCentreSysRoleList(CentreSysRole centreSysRole)
    {
        return centreSysRoleMapper.selectCentreSysRoleList(centreSysRole);
    }

    /**
     * 新增OA角色映射对接系统
     * 
     * @param centreSysRole OA角色映射对接系统
     * @return 结果
     */
    @Override
    public int insertCentreSysRole(CentreSysRole centreSysRole)
    {
        checkUnique(centreSysRole);
        centreSysRole.setCreateTime(DateUtils.getNowDate());
        return centreSysRoleMapper.insertSelective(centreSysRole);
    }

    /**
     * 修改OA角色映射对接系统
     * 
     * @param centreSysRole OA角色映射对接系统
     * @return 结果
     */
    @Override
    public int updateCentreSysRole(CentreSysRole centreSysRole)
    {
        checkUnique(centreSysRole);
        centreSysRole.setUpdateTime(DateUtils.getNowDate());
        return centreSysRoleMapper.updateByPrimaryKeySelective(centreSysRole);
    }

    /**
     * 删除OA角色映射对接系统对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCentreSysRoleByIds(String ids)
    {
        return centreSysRoleMapper.deleteCentreSysRoleByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除OA角色映射对接系统信息
     * 
     * @param id OA角色映射对接系统ID
     * @return 结果
     */
    @Override
    public int deleteCentreSysRoleById(Long id)
    {
        return centreSysRoleMapper.deleteCentreSysRoleById(id);
    }



    @Override
    public CentreSysRole selectSingleOneByExample(Example example){
        return centreSysRoleMapper.selectSingleOneByExample(example);
    }

    @Override
    public List<CentreSysRole> selectByExample(Example example){
        return centreSysRoleMapper.selectByExample(example);
    }


    private void checkUnique(CentreSysRole centreSysRole){
        Example example = new Example(CentreSysRole.class);
        example.createCriteria()
                .andEqualTo("oaRoleId",centreSysRole.getOaRoleId())
                .andEqualTo("centreSysId",centreSysRole.getCentreSysId());
        CentreSysRole oaRole = selectSingleOneByExample(example);
        if (oaRole != null && centreSysRole.getId() != null
                && !Objects.equals(oaRole.getId(),centreSysRole.getId())){
            throw new BusinessException("本系统这个OA角色已经存在，不能重试映射");
        }

        example = new Example(CentreSysRole.class);
        example.createCriteria()
                .andEqualTo("roleId",centreSysRole.getRoleId())
                .andEqualTo("centreSysId",centreSysRole.getCentreSysId());
        CentreSysRole role = selectSingleOneByExample(example);
        if (role != null  && centreSysRole.getId() != null
                && !Objects.equals(role.getId(),centreSysRole.getId())){
            throw new RuntimeException("这个系统角色ID已经存在，不能重试映射");
        }

    }

}
