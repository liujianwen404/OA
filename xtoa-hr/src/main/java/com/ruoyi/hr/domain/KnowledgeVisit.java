package com.ruoyi.hr.domain;

import com.ruoyi.framework.util.ShiroUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 知识访问对象 t_knowledge_visit_0
 *
 * @author xt
 * @date 2020-06-08
 */
@Data
@Document(collection = "knowledge.visit")
public class KnowledgeVisit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String id;

    /** 知识id */
    private Long knowledgeId;

    private String tableName;

    public String getTableName() {

        if (StringUtils.isNotEmpty(this.tableName)){
            return this.tableName;
        }else {
            String userId = ShiroUtils.getSysUser().getUserId().toString();
            this.tableName = "t_knowledge_visit_" + userId.substring(userId.length()-1,userId.length());
        }

        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


}