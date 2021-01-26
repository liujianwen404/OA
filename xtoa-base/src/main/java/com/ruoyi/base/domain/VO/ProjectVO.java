package com.ruoyi.base.domain.VO;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 计划id */
    private Long id;

    /** 迭代计划名称 */
    private String name;

    /** 描述 */
    private String contentDescribe;

    /** 负责人 */
    private Long empId;

    private String empName;

    /** 状态(0:创建，1:完成，2:关闭) */
    private Integer status;

}
