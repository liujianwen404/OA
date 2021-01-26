package com.ruoyi.base.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 知识访问对象 t_knowledge_enshrine
 * 
 * @author xt
 * @date 2020-06-09
 */
@Table(name = "t_knowledge_enshrine")
public class KnowledgeEnshrine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 知识收藏id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 知识id */
    @Column(name = "knowledge_id")
    @Excel(name = "知识id")
    private Long knowledgeId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setKnowledgeId(Long knowledgeId) 
    {
        this.knowledgeId = knowledgeId;
    }

    public Long getKnowledgeId() 
    {
        return knowledgeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("knowledgeId", getKnowledgeId())
            .append("createId", getCreateId())
            .append("updateId", getUpdateId())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
