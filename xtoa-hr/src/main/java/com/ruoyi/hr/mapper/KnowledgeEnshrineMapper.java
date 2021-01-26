package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.KnowledgeEnshrine;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 知识访问 数据层
 *
 * @author xt
 * @date 2020-06-09
 */
public interface KnowledgeEnshrineMapper extends MyBaseMapper<KnowledgeEnshrine> {

    /**
     * 查询知识访问列表
     *
     * @param knowledgeEnshrine 知识访问
     * @return 知识访问集合
     */
    public List<KnowledgeEnshrine> selectKnowledgeEnshrineList(KnowledgeEnshrine knowledgeEnshrine);

    /**
     * 删除知识访问
     *
     * @param id 知识访问ID
     * @return 结果
     */
    public int deleteKnowledgeEnshrineById(Long id);

    /**
     * 批量删除知识访问
     *
     * @param ids 需要删除的数据ID
     * @param userId
     * @return 结果
     */
    public int deleteKnowledgeEnshrineByIds(@Param("ids") String[] ids, @Param("userId") Long userId);

}