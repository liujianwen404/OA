package com.ruoyi.hr.domain;

import com.ruoyi.framework.util.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 知识对象 t_knowledge
 * 
 * @author xt
 * @date 2020-06-05
 */
@Table(name = "t_knowledge")
public class Knowledge extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 知识id */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /** 类型(1:专业，2生活) */
    @Column(name = "type")
    @Excel(name = "类型(1:专业，2生活)")
    private String type;

    /** 标题 */
    @Column(name = "titel")
    @Excel(name = "标题")
    private String titel;

    /** 封面 */
    @Column(name = "cover")
    @Excel(name = "封面")
    private String cover;

    /** 内容 */
    @Column(name = "content")
    @Excel(name = "内容")
    private String content;

    /** 状态（0正常 1关闭） */
    @Column(name = "status")
    @Excel(name = "状态", readConverterExp = "0=正常,1=关闭")
    private String status;

    /** 部门id */
    private Long deptId;

    /** 岗位id */
    private Long postId;
    @Transient
    private Integer pageNum;
    @Transient
    private Integer pageSize;
    @Transient
    private String tableName;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getTableName() {

        if (StringUtils.isNotEmpty(this.tableName)){
            return this.tableName;
        }else {
            String userId = ShiroUtils.getSysUser().getUserId().toString();
            this.tableName = "t_knowledge_visit_" + userId.substring(userId.length()-1,userId.length());
        }

        return tableName;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setTitel(String titel) 
    {
        this.titel = titel;
    }

    public String getTitel() 
    {
        return titel;
    }
    public void setCover(String cover) 
    {
        this.cover = cover;
    }

    public String getCover() 
    {
        return cover;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }
    public void setPostId(Long postId) 
    {
        this.postId = postId;
    }

    public Long getPostId() 
    {
        return postId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("titel", getTitel())
            .append("cover", getCover())
            .append("content", getContent())
            .append("status", getStatus())
            .append("deptId", getDeptId())
            .append("postId", getPostId())
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
