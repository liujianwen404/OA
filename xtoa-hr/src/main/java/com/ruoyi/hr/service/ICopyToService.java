package com.ruoyi.hr.service;

import java.util.List;
import com.ruoyi.base.domain.CopyTo;

/**
 * 流程抄送关系Service接口
 * 
 * @author liujianwen
 * @date 2020-06-06
 */
public interface ICopyToService 
{
    /**
     * 查询流程抄送关系
     * 
     * @param id 流程抄送关系ID
     * @return 流程抄送关系
     */
    public CopyTo selectCopyToById(Long id);

    /**
     * 查询流程抄送关系列表
     * 
     * @param copyTo 流程抄送关系
     * @return 流程抄送关系集合
     */
    public List<CopyTo> selectCopyToList(CopyTo copyTo);

    /**
     * 新增流程抄送关系
     * 
     * @param copyTo 流程抄送关系
     * @return 结果
     */
    public int insertCopyTo(CopyTo copyTo);

    /**
     * 修改流程抄送关系
     * 
     * @param copyTo 流程抄送关系
     * @return 结果
     */
    public int updateCopyTo(CopyTo copyTo);

    /**
     * 批量删除流程抄送关系
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCopyToByIds(String ids);

    /**
     * 删除流程抄送关系信息
     * 
     * @param id 流程抄送关系ID
     * @return 结果
     */
    public int deleteCopyToById(Long id);

    Long selectProcessCopyerCount(CopyTo copyTo);
}
