package com.ruoyi.base.domain.VO;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.base.domain.HrRecruit;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 招聘申请对象 t_hr_recruit
 * 
 * @author cmw
 * @date 2020-05-11
 */
public class HrRecruitVO extends HrRecruit
{
    private static final long serialVersionUID = 1L;

    private String deptName;
    private String postName;
    //招聘人数
    private Integer recruitCount0;
    //储备人数
    private Integer recruitCount1;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Integer getRecruitCount0() {
        return recruitCount0;
    }

    public void setRecruitCount0(Integer recruitCount0) {
        this.recruitCount0 = recruitCount0;
    }

    public Integer getRecruitCount1() {
        return recruitCount1;
    }

    public void setRecruitCount1(Integer recruitCount1) {
        this.recruitCount1 = recruitCount1;
    }
}
