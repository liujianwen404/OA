package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.CopyTo;
import java.util.List;
/**
 * 流程抄送关系 数据层
 *
 * @author liujianwen
 * @date 2020-06-06
 */
public interface CopyToMapper extends MyBaseMapper<CopyTo> {

    /**
     * 查询流程抄送关系列表
     *
     * @param copyTo 流程抄送关系
     * @return 流程抄送关系集合
     */
    public List<CopyTo> selectCopyToList(CopyTo copyTo);

    /**
     * 删除流程抄送关系
     *
     * @param id 流程抄送关系ID
     * @return 结果
     */
    public int deleteCopyToById(Long id);

    /**
     * 批量删除流程抄送关系
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCopyToByIds(String[] ids);

    Long selectProcessCopyerCount(CopyTo copyTo);
}