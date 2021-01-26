package com.ruoyi.hr.service;

import java.io.IOException;
import java.util.List;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.hr.domain.Knowledge;
import org.springframework.web.multipart.MultipartFile;

/**
 * 知识Service接口
 * 
 * @author xt
 * @date 2020-06-05
 */
public interface IKnowledgeService 
{
    /**
     * 查询知识
     * 
     * @param id 知识ID
     * @return 知识
     */
    public Knowledge selectKnowledgeById(Long id);

    /**
     * 查询知识列表
     * 
     * @param knowledge 知识
     * @return 知识集合
     */
    public List<Knowledge> selectKnowledgeList(Knowledge knowledge);

    /**
     * 新增知识
     * 
     * @param knowledge 知识
     * @param coverFile
     * @return 结果
     */
    public int insertKnowledge(Knowledge knowledge, MultipartFile coverFile) throws IOException;

    /**
     * 修改知识
     * 
     * @param knowledge 知识
     * @return 结果
     */
    public int updateKnowledge(Knowledge knowledge);

    /**
     * 批量删除知识
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteKnowledgeByIds(String ids);

    /**
     * 删除知识信息
     * 
     * @param id 知识ID
     * @return 结果
     */
    public int deleteKnowledgeById(Long id);

    Knowledge selectById(Long id, String isLook);

    /**
     * 获取知识详情，并增加一条查看记录
     * @param id
     * @param isLook
     * @return
     */
    Knowledge getAndAddVisitRecord(Long id, String isLook);

    List<Knowledge> selectKnowledgeVisitList(Knowledge knowledge);

    List<Knowledge> selectKnowledgeEnshrineList(Knowledge knowledge);

    int deleteKnowledgeVisitByIds(String ids);

    int enshrine(Knowledge knowledge);

    TableDataInfo getRanking();
}
