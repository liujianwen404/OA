package com.ruoyi.base.domain.VO;

import com.ruoyi.system.domain.SysUser;

public class UserVO extends SysUser {
    /**
     * map.put("loginName",sysUser.getLoginName());
     *         map.put("userName",sysUser.getUserName());
     *         map.put("deptName",sysUser.getDept().getDeptName());
     *         map.put("nonManagerDate",hrEmp.getNonManagerDate());
     *         map.put("parentDeptName",sysDept.getDeptName());
     *         map.put("postNmae",postNmae);
     */
    private String deptName;
    private String nonManagerDate;
    private String parentDeptName;
    private String postNmae;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getNonManagerDate() {
        return nonManagerDate;
    }

    public void setNonManagerDate(String nonManagerDate) {
        this.nonManagerDate = nonManagerDate;
    }

    public String getParentDeptName() {
        return parentDeptName;
    }

    public void setParentDeptName(String parentDeptName) {
        this.parentDeptName = parentDeptName;
    }

    public String getPostNmae() {
        return postNmae;
    }

    public void setPostNmae(String postNmae) {
        this.postNmae = postNmae;
    }
}
