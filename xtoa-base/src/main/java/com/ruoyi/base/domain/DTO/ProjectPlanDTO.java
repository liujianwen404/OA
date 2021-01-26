package com.ruoyi.base.domain.DTO;

import lombok.Data;
import java.io.Serializable;

/**
 *  迭代计划DTO
 *
 */
@Data
public class ProjectPlanDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long projectId;

    private Long empId;

    private String empName;

    private Integer status;

}
