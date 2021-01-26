package com.ruoyi.process.general.service;

import com.ruoyi.process.general.domain.HistoricActivity;
import org.activiti.engine.history.HistoricProcessInstance;

import java.util.List;

public interface IProcessService {

    /**
     * 查询审批历史列表
     * @param processInstanceId
     * @param historicActivity
     * @return
     */
    List<HistoricActivity> selectHistoryList(String processInstanceId, HistoricActivity historicActivity);

    /**
     * 获取实例被删除理由
     * @param processInstanceId
     * @return
     */
    HistoricProcessInstance getDeleteReason(String processInstanceId);

}
