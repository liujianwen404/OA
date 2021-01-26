package com.ruoyi.base.domain.VO;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import java.util.Date;

@Data
public class ProjectPlanVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 计划id */
    private Long id;

    /** 项目id */
    private Long projectId;

    private String projectName;
    /** 迭代计划名称 */
    private String name;

    /** 描述 */
    private String contentDescribe;

    /** 负责人 */
    private Long empId;

    private String empName;

    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    // 迭代周期
    private String iterationsTime;

    /** 状态(0:创建，1:完成，2:关闭) */
    private Integer status;
}
