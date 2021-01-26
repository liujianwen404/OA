package com.ruoyi.base.domain.VO;

import lombok.Data;
import java.io.Serializable;

@Data
public class ProjectSelectVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 项目id
     */
    private Long id;

    /**
     * 项目名称
     */
    private String name;

}
