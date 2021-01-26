package com.ruoyi.hr.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.base.domain.KnowledgeEnshrine;
import com.ruoyi.hr.domain.KnowledgeVisit;
import com.ruoyi.hr.mapper.KnowledgeVisitMapper;
import com.ruoyi.hr.service.IKnowledgeEnshrineService;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.hr.mapper.KnowledgeMapper;
import com.ruoyi.hr.domain.Knowledge;
import com.ruoyi.hr.service.IKnowledgeService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.web.multipart.MultipartFile;

/**
 * 知识Service业务层处理
 * 
 * @author xt
 * @date 2020-06-05
 */
@Service
public class KnowledgeServiceImpl implements IKnowledgeService 
{

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeServiceImpl.class);

    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @Autowired
    private ISysPostService sysPostService;

    @Autowired
    private IKnowledgeEnshrineService knowledgeEnshrineService;

    @Autowired
    private KnowledgeVisitMapper knowledgeVisitMapper;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询知识
     * 
     * @param id 知识ID
     * @return 知识
     */
    @Override
    public Knowledge selectKnowledgeById(Long id)
    {
        return knowledgeMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询知识列表
     * 
     * @param knowledge 知识
     * @return 知识
     */
    @Override
    public List<Knowledge> selectKnowledgeList(Knowledge knowledge)
    {
        knowledge.setDelFlag("0");
        return knowledgeMapper.selectKnowledgeList(knowledge);
    }

    /**
     * 新增知识
     * 
     * @param knowledge 知识
     * @param coverFile
     * @return 结果
     */
    @Override
    public int insertKnowledge(Knowledge knowledge, MultipartFile coverFile) throws IOException {

        /*String fileName = FileUploadUtils.upload(Global.getProfile() + "/knowledge", coverFile);
        knowledge.setCover(fileName);*/

        knowledge.setDeptId(ShiroUtils.getSysUser().getDeptId());
        List<SysPost> sysPosts = sysPostService.selectPostsByUserId(ShiroUtils.getSysUser().getUserId());

        knowledge.setPostId(sysPosts.get(0).getPostId());

        knowledge.setCreateId(ShiroUtils.getUserId());
        knowledge.setCreateBy(ShiroUtils.getLoginName());
        knowledge.setCreateTime(DateUtils.getNowDate());
        return knowledgeMapper.insertSelective(knowledge);
    }

    /**
     * 修改知识
     * 
     * @param knowledge 知识
     * @return 结果
     */
    @Override
    public int updateKnowledge(Knowledge knowledge)
    {
        knowledge.setUpdateId(ShiroUtils.getUserId());
        knowledge.setUpdateBy(ShiroUtils.getLoginName());
        knowledge.setUpdateTime(DateUtils.getNowDate());
        return knowledgeMapper.updateByPrimaryKeySelective(knowledge);
    }

    /**
     * 删除知识对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeByIds(String ids)
    {
        return knowledgeMapper.deleteKnowledgeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除知识信息
     * 
     * @param id 知识ID
     * @return 结果
     */
    @Override
    public int deleteKnowledgeById(Long id)
    {
        return knowledgeMapper.deleteKnowledgeById(id);
    }

    @Override
    public Knowledge selectById(Long id, String isLook) {
        Knowledge knowledge = knowledgeMapper.selectByPrimaryKey(id);
        if (knowledge == null || !knowledge.getDelFlag().equals("0") || !knowledge.getStatus().equals("0")){
            throw new BusinessException("信息已关闭不能浏览了");
        }

        if (StringUtils.isNotEmpty(isLook) && "isLook".equals(isLook)){
            KnowledgeVisit knowledgeVisit = new KnowledgeVisit();
            knowledgeVisit.setCreateId(ShiroUtils.getUserId());
            knowledgeVisit.setKnowledgeId(id);
            KnowledgeVisit knowledgeVisit1 = knowledgeVisitMapper.selectKnowledgeVisitOne(knowledgeVisit);
            if (knowledgeVisit1 != null){
                knowledgeVisit1.setDelFlag("0");
                updateKnowledgeVisit(knowledgeVisit1);
            }else {
                insertKnowledgeVisit(knowledgeVisit);
            }
        }
        return knowledge;
    }

    @Override
    public Knowledge getAndAddVisitRecord(Long id, String isLook) {
        Knowledge knowledge = knowledgeMapper.selectByPrimaryKey(id);
        if (knowledge == null || !knowledge.getDelFlag().equals("0") || !knowledge.getStatus().equals("0")){
            throw new BusinessException("信息已关闭不能浏览了");
        }

        if (StringUtils.isNotEmpty(isLook) && "isLook".equals(isLook)){
            KnowledgeVisit knowledgeVisit = new KnowledgeVisit();
            knowledgeVisit.setCreateId(ShiroUtils.getUserId());
            knowledgeVisit.setKnowledgeId(id);
            knowledgeVisit.setCreateTime(DateUtils.getNowDate());
            mongoTemplate.save(knowledgeVisit);
            Query query=new Query(Criteria.where("createId").is(ShiroUtils.getUserId()).and("knowledgeId").is(id));
            Update update = new Update();
            update.set("updateTime", DateUtils.getNowDate());
            update.set("updateBy", ShiroUtils.getLoginName());
            mongoTemplate.upsert(query,update,KnowledgeVisit.class);
        }
        return knowledge;
    }

    @Override
    public List<Knowledge> selectKnowledgeVisitList(Knowledge knowledge) {

        List<Knowledge> knowledges = knowledgeMapper.selectKnowledgeVisitList(knowledge.getTableName(),
                                                                    knowledge.getPageNum(),
                                                                    knowledge.getPageSize(),
                                                                    knowledge.getCreateId());

        return knowledges;
    }

    @Override
    public List<Knowledge> selectKnowledgeEnshrineList(Knowledge knowledge) {

        List<Knowledge> knowledges = knowledgeMapper.selectKnowledgeEnshrineList(
                                                                    knowledge.getPageNum(),
                                                                    knowledge.getPageSize(),
                                                                    knowledge.getCreateId());

        return knowledges;
    }

    @Override
    public int deleteKnowledgeVisitByIds(String ids) {
        KnowledgeVisit knowledgeVisit = new KnowledgeVisit();
        return knowledgeVisitMapper.deleteKnowledgeVisitByIds(Convert.toStrArray(ids),knowledgeVisit.getTableName(),ShiroUtils.getUserId());
    }

    @Override
    public int enshrine(Knowledge knowledge) {
        KnowledgeEnshrine knowledgeEnshrine = new KnowledgeEnshrine();
        knowledgeEnshrine.setKnowledgeId(knowledge.getId());
        knowledgeEnshrine.setCreateId(ShiroUtils.getUserId());
        KnowledgeEnshrine selectKnowledgeEnshrineById = knowledgeEnshrineService.selectKnowledgeEnshrineByExpm(knowledgeEnshrine);
        if (selectKnowledgeEnshrineById != null){
            selectKnowledgeEnshrineById.setDelFlag("0");
            return knowledgeEnshrineService.updateKnowledgeEnshrine(selectKnowledgeEnshrineById);
        }

        return knowledgeEnshrineService.insertKnowledgeEnshrine(knowledgeEnshrine);
    }

    @Override
    public TableDataInfo getRanking() {
        Map<String,Object> map = new HashMap<>();
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(0);

        map = knowledgeMapper.getRanking(ShiroUtils.getUserId());

        if (map.get("myCount") == null){
            map.put("myCount",0);
        }


        SysUser sysUser = ShiroUtils.getSysUser();
        Integer headCount = knowledgeMapper.getRankingDepthead(sysUser.getUserId());
        //总部
        map.put("headCount",headCount);

        SysDept dept = sysUser.getDept();
        Integer deptCount = knowledgeMapper.getRankingDept(dept.getDeptId(),sysUser.getUserId());
        //部门
        map.put("deptCount",deptCount);
        String[] split = dept.getAncestors().split(",");
        for (String dId : split) {
            if (!dId.equals("0") && !dId.equals(dept.getDeptId()) ){
                Integer dCount = knowledgeMapper.getRankingDept(Long.parseLong(dId),sysUser.getUserId());
                //部门
                map.put("dCount",dCount);
            }
        }
        tableDataInfo.setData(map);
        return tableDataInfo;
    }


    public int updateKnowledgeVisit(KnowledgeVisit knowledgeVisit)
    {
        knowledgeVisit.setUpdateId(ShiroUtils.getUserId());
        knowledgeVisit.setUpdateBy(ShiroUtils.getLoginName());
        knowledgeVisit.setUpdateTime(DateUtils.getNowDate());
        return knowledgeVisitMapper.updateKnowledgeVisit(knowledgeVisit);
    }

    public int insertKnowledgeVisit(KnowledgeVisit knowledgeVisit){
        knowledgeVisit.setCreateId(ShiroUtils.getUserId());
        knowledgeVisit.setCreateBy(ShiroUtils.getLoginName());
        knowledgeVisit.setCreateTime(DateUtils.getNowDate());
        return knowledgeVisitMapper.insertKnowledgeVisit(knowledgeVisit);
    }
}
