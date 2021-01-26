package com.ruoyi.common.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;

/**
 * Entity基类
 * 
 * @author ruoyi
 */
@Data
public class BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 搜索值 */
    @Transient
    @ExcelIgnore
    private String searchValue;

    /** 创建者ID */
    @ExcelIgnore
    private Long createId;

    /** 创建者 */
    @ExcelIgnore
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", insertable = false, updatable = false)
    @ExcelIgnore
    private Date createTime;

    /** 更新者ID */
    @ExcelIgnore
    private Long updateId;

    /** 更新者 */
    @ExcelIgnore
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time" ,insertable = false, updatable = false)
    @ExcelIgnore
    private Date updateTime;

    /** 备注 */
    @ExcelIgnore
    private String remark;

    /** 删除标志（0代表存在 1代表删除） */
    @ExcelIgnore
    private String delFlag;

    /** 请求参数 */
    @Transient
    @ExcelIgnore
    private Map<String, Object> params;

    @Transient
    /** 申请人姓名 */
    @ExcelIgnore
    private String applyUserName;

    @Transient
    /** 任务ID */
    @ExcelIgnore
    private String taskId;

    @Transient
    /** 任务名称 */
    @ExcelIgnore
    private String taskName;

    @Transient
    /** 办理时间 */
    @ExcelIgnore
    private Date doneTime;

    @Transient
    /** 创建人 */
    @ExcelIgnore
    private String createUserName;

    @Transient
    /** 待办人名称 */
    @ExcelIgnore
    private String todoUserName;



    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
}
