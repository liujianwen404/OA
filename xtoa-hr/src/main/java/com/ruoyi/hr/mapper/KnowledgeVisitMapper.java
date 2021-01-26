package com.ruoyi.hr.mapper;

import com.ruoyi.hr.domain.KnowledgeVisit;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 知识访问 数据层
 *
 * @author xt
 * @date 2020-06-08
 */
public interface KnowledgeVisitMapper  {

    /**
     * 查询知识访问列表
     *
     * @param knowledgeVisit 知识访问
     * @return 知识访问集合
     */
    public List<KnowledgeVisit> selectKnowledgeVisitList(KnowledgeVisit knowledgeVisit);

    public int insertKnowledgeVisit(KnowledgeVisit knowledgeVisit);

    public int updateKnowledgeVisit(KnowledgeVisit knowledgeVisit);

    public KnowledgeVisit selectKnowledgeVisitOne(KnowledgeVisit knowledgeVisit);


    /**
     * 删除知识访问
     *
     * @param id 知识访问ID
     * @return 结果
     */
    public int deleteKnowledgeVisitById(Long id);

    /**
     * 批量删除知识访问
     *
     * @param ids 需要删除的数据ID
     * @param userId
     * @return 结果
     */
    public int deleteKnowledgeVisitByIds(@Param("ids") String[] ids, @Param("tableName") String tableName,
                                         @Param("userId") Long userId);

}