package com.ruoyi.base.domain.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectPlanSelectVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 项目迭代计划id
     */
    private Long id;

    /**
     * 项目迭代计划名称
     */
    private String name;

}
