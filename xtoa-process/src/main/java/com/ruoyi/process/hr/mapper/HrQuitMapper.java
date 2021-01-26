//package com.ruoyi.process.hr.mapper;
//
//import com.ruoyi.common.mapper.MyBaseMapper;
//import com.ruoyi.process.hr.domain.HrQuit;
//
//import java.util.List;
//
///**
// * 离职申请 数据层
// *
// * @author liujianwen
// * @date 2020-05-15
// */
//public interface HrQuitMapper extends MyBaseMapper<HrQuit> {
//
//    /**
//     * 查询离职申请列表
//     *
//     * @param hrQuit 离职申请
//     * @return 离职申请集合
//     */
//    public List<HrQuit> selectHrQuitList(HrQuit hrQuit);
//
//    /**
//     * 删除离职申请
//     *
//     * @param id 离职申请ID
//     * @return 结果
//     */
//    public int deleteHrQuitById(Long id);
//
//    /**
//     * 批量删除离职申请
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    public int deleteHrQuitByIds(String[] ids);
//
//}