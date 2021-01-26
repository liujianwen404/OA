package com.ruoyi.hr.utils;

import com.ruoyi.base.domain.DTO.HolidayDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ImportInfoEntity<E> {

    private Date importDate;

    private String importInfo;

    private List<E> importInfoList;
}
