package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.hr.domain.HrInterpolate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 内推申请 数据层
 *
 * @author vivi07
 * @date 2020-05-12
 */
public interface HrInterpolateMapper extends MyBaseMapper<HrInterpolate> {

    /**
     * 查询内推申请列表
     *
     * @param hrInterpolate 内推申请
     * @param hrFlag
     * @param deptId
     * @param userId
     * @return 内推申请集合
     */
    public List<HrInterpolate> selectHrInterpolateList(@Param("hrInterpolate") HrInterpolate hrInterpolate,
                                                       @Param("hrFlag") Integer hrFlag, @Param("deptId") Long deptId,
                                                       @Param("userId") Long userId);

    /**
     * 删除内推申请
     *
     * @param interpolateId 内推申请ID
     * @return 结果
     */
    public int deleteHrInterpolateById(Long interpolateId);

    /**
     * 批量删除内推申请
     *
     * @param interpolateIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteHrInterpolateByIds(String[] interpolateIds);

}