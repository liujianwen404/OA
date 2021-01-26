package com.ruoyi.hr.service;

import java.util.List;
import com.ruoyi.base.domain.DingdingMsg;

/**
 * 钉钉工作通知发送记录Service接口
 * 
 * @author xt
 * @date 2020-07-16
 */
public interface IDingdingMsgService 
{
    /**
     * 查询钉钉工作通知发送记录
     * 
     * @param id 钉钉工作通知发送记录ID
     * @return 钉钉工作通知发送记录
     */
    public DingdingMsg selectDingdingMsgById(Long id);

    /**
     * 查询钉钉工作通知发送记录列表
     * 
     * @param dingdingMsg 钉钉工作通知发送记录
     * @return 钉钉工作通知发送记录集合
     */
    public List<DingdingMsg> selectDingdingMsgList(DingdingMsg dingdingMsg);

    /**
     * 新增钉钉工作通知发送记录
     * 
     * @param dingdingMsg 钉钉工作通知发送记录
     * @return 结果
     */
    public int insertDingdingMsg(DingdingMsg dingdingMsg);

    /**
     * 修改钉钉工作通知发送记录
     * 
     * @param dingdingMsg 钉钉工作通知发送记录
     * @return 结果
     */
    public int updateDingdingMsg(DingdingMsg dingdingMsg);

    /**
     * 批量删除钉钉工作通知发送记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDingdingMsgByIds(String ids);

    /**
     * 删除钉钉工作通知发送记录信息
     * 
     * @param id 钉钉工作通知发送记录ID
     * @return 结果
     */
    public int deleteDingdingMsgById(Long id);

    /**
     * 发送钉钉工作link通知
     * @param img 图片
     * @param empIds 接收者的企业内部用户的userid列表，最大用户列表长度：100   123,345
     * @param bizId 业务数据id
     * @param text 通知内容
     * @param title 通知标题
     * @param url 链接地址
     */
    void sendLnikMsg(String img, String empIds, String bizId, String text, String title, String url);
}
