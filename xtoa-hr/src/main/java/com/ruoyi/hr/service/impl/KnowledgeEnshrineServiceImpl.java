package com.ruoyi.hr.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.KnowledgeEnshrineMapper;
import com.ruoyi.base.domain.KnowledgeEnshrine;
import com.ruoyi.hr.service.IKnowledgeEnshrineService;
import com.ruoyi.common.core.text.Convert;
import tk.mybatis.mapper.entity.Example;

/**
 * 知识访问Service业务层处理
 * 
 * @author xt
 * @date 2020-06-09
 */
@Service
public class KnowledgeEnshrineServiceImpl implements IKnowledgeEnshrineService 
{

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeEnshrineServiceImpl.class);

    @Autowired
    private KnowledgeEnshrineMapper knowledgeEnshrineMapper;

    /**
     * 查询知识访问
     *
     * @param id 知识访问ID
     * @return 知识访问
     */
    @Override
    public KnowledgeEnshrine selectKnowledgeEnshrineByExpm(Long id)
    {
        return knowledgeEnshrineMapper.selectByPrimaryKey(id);
    }


    /**
     * 查询知识访问
     * 
     * @param id 知识访问ID
     * @return 知识访问
     */
    @Override
    public KnowledgeEnshrine selectKnowledgeEnshrineByExpm(KnowledgeEnshrine knowledgeEnshrine)
    {
        Example example = new Example(KnowledgeEnshrine.class);
        example.createCriteria()
                .andEqualTo("knowledgeId",knowledgeEnshrine.getKnowledgeId())
                .andEqualTo("createId",knowledgeEnshrine.getCreateId());
        return knowledgeEnshrineMapper.selectSingleOneByExample(example);
    }

    /**
     * 查询知识访问列表
     * 
     * @param knowledgeEnshrine 知识访问
     * @return 知识访问
     */
    @Override
    public List<KnowledgeEnshrine> selectKnowledgeEnshrineList(KnowledgeEnshrine knowledgeEnshrine)
    {
        knowledgeEnshrine.setDelFlag("0");
        return knowledgeEnshrineMapper.selectKnowledgeEnshrineList(knowledgeEnshrine);
    }

    /**
     * 新增知识访问
     * 
     * @param knowledgeEnshrine 知识访问
     * @return 结果
     */
    @Override
    public int insertKnowledgeEnshrine(KnowledgeEnshrine knowledgeEnshrine)
    {
        knowledgeEnshrine.setCreateId(ShiroUtils.getUserId());
        knowledgeEnshrine.setCreateBy(ShiroUtils.getLoginName());
        knowledgeEnshrine.setCreateTime(DateUtils.getNowDate());
        return knowledgeEnshrineMapper.insertSelective(knowledgeEnshrine);
    }

    /**
     * 修改知识访问
     * 
     * @param knowledgeEnshrine 知识访问
     * @return 结果
     */
    @Override
    public int updateKnowledgeEnshrine(KnowledgeEnshrine knowledgeEnshrine)
    {
        knowledgeEnshrine.setUpdateId(ShiroUtils.getUserId());
        knowledgeEnshrine.setUpdateBy(ShiroUtils.getLoginName());
        knowledgeEnshrine.setUpdateTime(DateUtils.getNowDate());
        return knowledgeEnshrineMapper.updateByPrimaryKeySelective(knowledgeEnshrine);
    }

    /**
     * 删除知识访问对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeEnshrineByIds(String ids)
    {
        return knowledgeEnshrineMapper.deleteKnowledgeEnshrineByIds(Convert.toStrArray(ids),ShiroUtils.getUserId());
    }

    /**
     * 删除知识访问信息
     * 
     * @param id 知识访问ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeEnshrineById(Long id)
    {
        return knowledgeEnshrineMapper.deleteKnowledgeEnshrineById(id);
    }
}
