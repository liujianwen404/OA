package com.ruoyi.hr.service;

import java.util.List;
import com.ruoyi.base.domain.KnowledgeEnshrine;

/**
 * 知识访问Service接口
 * 
 * @author xt
 * @date 2020-06-09
 */
public interface IKnowledgeEnshrineService 
{
    /**
     * 查询知识访问
     * 
     * @param id 知识访问ID
     * @return 知识访问
     */
    public KnowledgeEnshrine selectKnowledgeEnshrineByExpm(Long id);

    public KnowledgeEnshrine selectKnowledgeEnshrineByExpm(KnowledgeEnshrine knowledgeEnshrine);

    /**
     * 查询知识访问列表
     * 
     * @param knowledgeEnshrine 知识访问
     * @return 知识访问集合
     */
    public List<KnowledgeEnshrine> selectKnowledgeEnshrineList(KnowledgeEnshrine knowledgeEnshrine);

    /**
     * 新增知识访问
     * 
     * @param knowledgeEnshrine 知识访问
     * @return 结果
     */
    public int insertKnowledgeEnshrine(KnowledgeEnshrine knowledgeEnshrine);

    /**
     * 修改知识访问
     * 
     * @param knowledgeEnshrine 知识访问
     * @return 结果
     */
    public int updateKnowledgeEnshrine(KnowledgeEnshrine knowledgeEnshrine);

    /**
     * 批量删除知识访问
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteKnowledgeEnshrineByIds(String ids);

    /**
     * 删除知识访问信息
     * 
     * @param id 知识访问ID
     * @return 结果
     */
    public int deleteKnowledgeEnshrineById(Long id);
}
