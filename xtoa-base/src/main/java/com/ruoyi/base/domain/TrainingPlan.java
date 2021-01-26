package com.ruoyi.base.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 培训迭代对象 t_hr_training_plan
 * 
 * @author liujianwen
 * @date 2020-05-08
 */
public class TrainingPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 迭代编号 */
    @Excel(name = "迭代编号")
    private String planId;

    /** 封面图 */
    @Excel(name = "封面图")
    private String img;

    /** 培训名称 */
    @Excel(name = "培训名称")
    private String name;

    /** 培训分类 */
    @Excel(name = "培训分类")
    private String type;

    /** 负责人 */
    @Excel(name = "负责人")
    private String principal;

    /** 培训说明 */
    @Excel(name = "培训说明")
    private String trainingExplain;

    /** 发布时间 */
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date releaseTime;

    /** 培训迭代 */
    @Excel(name = "培训迭代")
    private String plan;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPlanId(String planId) 
    {
        this.planId = planId;
    }

    public String getPlanId() 
    {
        return planId;
    }
    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setPrincipal(String principal) 
    {
        this.principal = principal;
    }

    public String getPrincipal() 
    {
        return principal;
    }

    public String getTrainingExplain() {
        return trainingExplain;
    }

    public void setTrainingExplain(String trainingExplain) {
        this.trainingExplain = trainingExplain;
    }

    public void setReleaseTime(Date releaseTime)
    {
        this.releaseTime = releaseTime;
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getReleaseTime() 
    {
        return releaseTime;
    }
    public void setPlan(String plan) 
    {
        this.plan = plan;
    }

    public String getPlan() 
    {
        return plan;
    }
    @Override
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planId", getPlanId())
            .append("img", getImg())
            .append("name", getName())
            .append("type", getType())
            .append("principal", getPrincipal())
            .append("trainingExplain", getTrainingExplain())
            .append("releaseTime", getReleaseTime())
            .append("plan", getPlan())
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
