package com.ruoyi.hr.service.impl;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.ruoyi.base.dingTalk.DingConfig;
import com.ruoyi.base.dingTalk.DingMessageApi;
import com.ruoyi.base.domain.DingdingMsg;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.hr.mapper.DingdingMsgMapper;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.hr.service.IDingdingMsgService;
import com.ruoyi.base.utils.DingdingMsgEnum;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 钉钉工作通知发送记录Service业务层处理
 * 
 * @author xt
 * @date 2020-07-16
 */
@Service
public class DingdingMsgServiceImpl implements IDingdingMsgService 
{

    private static final Logger logger = LoggerFactory.getLogger(DingdingMsgServiceImpl.class);

    @Autowired
    private DingdingMsgMapper dingdingMsgMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private HrEmpService hrEmpService;

    @Autowired
    private DingConfig dingConfig;

    @Autowired
    private DingMessageApi dingMessageApi;

    @Value("${dingWorkRecordUrl}")
    private String dingWorkRecordUrl;

    /**
     * 查询钉钉工作通知发送记录
     * 
     * @param id 钉钉工作通知发送记录ID
     * @return 钉钉工作通知发送记录
     */
    @Override
    public DingdingMsg selectDingdingMsgById(Long id)
    {
        return dingdingMsgMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询钉钉工作通知发送记录列表
     * 
     * @param dingdingMsg 钉钉工作通知发送记录
     * @return 钉钉工作通知发送记录
     */
    @Override
    public List<DingdingMsg> selectDingdingMsgList(DingdingMsg dingdingMsg)
    {
        dingdingMsg.setDelFlag("0");
        return dingdingMsgMapper.selectDingdingMsgList(dingdingMsg);
    }

    /**
     * 新增钉钉工作通知发送记录
     * 
     * @param dingdingMsg 钉钉工作通知发送记录
     * @return 结果
     */
    @Override
    public int insertDingdingMsg(DingdingMsg dingdingMsg)
    {
        /*SysUser sysUser = ShiroUtils.getSysUser();
        dingdingMsg.setCreateId(sysUser == null ? null : ShiroUtils.getUserId());
        dingdingMsg.setCreateBy(sysUser == null ? null : ShiroUtils.getLoginName());*/
        dingdingMsg.setCreateTime(DateUtils.getNowDate());
        return dingdingMsgMapper.insertSelective(dingdingMsg);
    }

    /**
     * 修改钉钉工作通知发送记录
     * 
     * @param dingdingMsg 钉钉工作通知发送记录
     * @return 结果
     */
    @Override
    public int updateDingdingMsg(DingdingMsg dingdingMsg)
    {
        /*dingdingMsg.setUpdateId(ShiroUtils.getUserId());
        dingdingMsg.setUpdateBy(ShiroUtils.getLoginName());*/
        dingdingMsg.setUpdateTime(DateUtils.getNowDate());
        return dingdingMsgMapper.updateByPrimaryKeySelective(dingdingMsg);
    }

    /**
     * 删除钉钉工作通知发送记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDingdingMsgByIds(String ids)
    {
        return dingdingMsgMapper.deleteDingdingMsgByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除钉钉工作通知发送记录信息
     * 
     * @param id 钉钉工作通知发送记录ID
     * @return 结果
     */
    @Override
    public int deleteDingdingMsgById(Long id)
    {
        return dingdingMsgMapper.deleteDingdingMsgById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendLnikMsg(String img, String empIds, String bizId, String text, String title, String url) {
        logger.info("记录发送钉钉工作通知数据 link= "+url);
        //记录发送钉钉工作通知数据
        OapiMessageCorpconversationAsyncsendV2Request req = new OapiMessageCorpconversationAsyncsendV2Request();
        req.setAgentId(dingConfig.getAgentId());

        StringBuilder dingUsreIds = new StringBuilder();
        String[] split = empIds.split(",");
        if (split.length > 100){
            throw new BusinessException("钉钉工作通知发送失败：接收者的企业内部用户的userid列表，最大用户列表长度：100。 实际值："+split.length);
        }
        for (String empid : split) {
            SysUser sysUser = sysUserService.selectUserById(Long.parseLong(empid));
            dingUsreIds.append(hrEmpService.getDingUserId(sysUser,false)+",");
        }
        if (StringUtils.isEmpty(dingUsreIds.toString())){
            throw new BusinessException("钉钉工作通知发送失败：UseridList为空。"+empIds);
        }
        req.setUseridList(dingUsreIds.substring(0,dingUsreIds.length()-1));
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        String msgType = "link";
        msg.setMsgtype(msgType);
        OapiMessageCorpconversationAsyncsendV2Request.Link link = new OapiMessageCorpconversationAsyncsendV2Request.Link();
        link.setPicUrl(img);
        link.setMessageUrl( url );
        link.setText(text);
        link.setTitle(title);
        msg.setLink(link);
        req.setMsg(msg);
        DingdingMsg dingdingMsg = new DingdingMsg();
        dingdingMsg.setBizType(DingdingMsgEnum.MsgBizType.projectTaskMsg.getValue());
        dingdingMsg.setBizId(bizId);
        dingdingMsg.setMsg(req.getMsg());
        dingdingMsg.setMsgType(msgType);
        dingdingMsg.setUseridList(req.getUseridList());
        int i = insertDingdingMsg(dingdingMsg);
        if (i > 0){
            //发送钉钉工作通知
            OapiMessageCorpconversationAsyncsendV2Response oapiMessageCorpconversationAsyncsendV2Response = dingMessageApi.asyncsend_v2(req);
            if (!DingConfig.isSuccess(oapiMessageCorpconversationAsyncsendV2Response)){
//                throw new BusinessException("通知提醒失败：" + url);
                throw new BusinessException("通知提醒失败：" + oapiMessageCorpconversationAsyncsendV2Response.getErrmsg());
            }else {
                dingdingMsg.setTaskId(oapiMessageCorpconversationAsyncsendV2Response.getTaskId().toString());
                updateDingdingMsg(dingdingMsg);
            }
        }
    }

}
