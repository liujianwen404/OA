package com.ruoyi.hr.mapper;

import com.ruoyi.common.mapper.MyBaseMapper;
import com.ruoyi.base.domain.DingdingMsg;
import java.util.List;
/**
 * 钉钉工作通知发送记录 数据层
 *
 * @author xt
 * @date 2020-07-16
 */
public interface DingdingMsgMapper extends MyBaseMapper<DingdingMsg> {

    /**
     * 查询钉钉工作通知发送记录列表
     *
     * @param dingdingMsg 钉钉工作通知发送记录
     * @return 钉钉工作通知发送记录集合
     */
    public List<DingdingMsg> selectDingdingMsgList(DingdingMsg dingdingMsg);

    /**
     * 删除钉钉工作通知发送记录
     *
     * @param id 钉钉工作通知发送记录ID
     * @return 结果
     */
    public int deleteDingdingMsgById(Long id);

    /**
     * 批量删除钉钉工作通知发送记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDingdingMsgByIds(String[] ids);

}