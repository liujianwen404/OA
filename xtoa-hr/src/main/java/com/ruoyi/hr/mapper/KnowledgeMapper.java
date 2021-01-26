package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.hr.domain.Knowledge;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 知识 数据层
 *
 * @author xt
 * @date 2020-06-05
 */
public interface KnowledgeMapper extends MyBaseMapper<Knowledge> {

    /**
     * 查询知识列表
     *
     * @param knowledge 知识
     * @return 知识集合
     */
    public List<Knowledge> selectKnowledgeList(Knowledge knowledge);

    /**
     * 删除知识
     *
     * @param id 知识ID
     * @return 结果
     */
    public int deleteKnowledgeById(Long id);

    /**
     * 批量删除知识
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteKnowledgeByIds(String[] ids);

    List<Knowledge> selectKnowledgeVisitList(@Param("tableName") String tableName,
                                             @Param("p1") Integer pageNum,
                                             @Param("p2") Integer pageSize,
                                             @Param("createId") Long createId);
    List<Knowledge> selectKnowledgeEnshrineList(@Param("p1") Integer pageNum,
                                             @Param("p2") Integer pageSize,
                                             @Param("createId") Long createId);

    Map<String,Object> getRanking(@Param("userId") Long userId);

    Integer getRankingDepthead(@Param("userId") Long userId);

    Integer getRankingDept(@Param("deptId") Long deptId, @Param("userId") Long userId);
}