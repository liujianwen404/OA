package com.ruoyi.base.domain.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String path;
}
