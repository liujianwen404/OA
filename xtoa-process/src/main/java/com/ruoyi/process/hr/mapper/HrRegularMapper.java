//package com.ruoyi.process.hr.mapper;
//
//import com.ruoyi.common.mapper.MyBaseMapper;
//import com.ruoyi.process.hr.domain.HrRegular;
//
//import java.util.List;
//
///**
// * 离职申请 数据层
// *
// * @author liujianwen
// * @date 2020-05-15
// */
//public interface HrRegularMapper extends MyBaseMapper<HrRegular> {
//
//    /**
//     * 查询离职申请列表
//     *
//     * @param hrRegular 离职申请
//     * @return 离职申请集合
//     */
//    public List<HrRegular> selectHrRegularList(HrRegular hrRegular);
//
//    /**
//     * 删除离职申请
//     *
//     * @param id 离职申请ID
//     * @return 结果
//     */
//    public int deleteHrRegularById(Long id);
//
//    /**
//     * 批量删除离职申请
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    public int deleteHrRegularByIds(String[] ids);
//
//}