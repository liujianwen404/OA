package com.ruoyi.base.domain.VO;

import lombok.Data;

import java.io.Serializable;


@Data
public class DingDeptVO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long id;


    private String name;

    private Long parentid;
}
