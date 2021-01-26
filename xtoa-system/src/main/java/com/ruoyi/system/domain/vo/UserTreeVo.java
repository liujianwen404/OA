package com.ruoyi.system.domain.vo;

import com.ruoyi.common.core.domain.Ztree;
import lombok.Data;

@Data
public class UserTreeVo extends Ztree {

    private Long deptId;

    private String empName;

    private String phonenumber;

    private String email;

}
