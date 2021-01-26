package com.ruoyi.base.dingTalk.dingCallBackDTO;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class RobotSendMsgDTO {

    /**
     * 標題
     */
    private String title;
    /**
     * 所屬項目
     */
    private String projectName;
    /**
     * 負責人
     */
    private String empName;

    private Long empId;

    /**
     * 钉钉用户ＩＤ
     */
    private String userId;

    /**
     * 開始時間
     */
    private String startTime;
    /**
     * 結束時間
     */
    private String endTime;
    /**
     * 狀態
     */
    private String status;


    private String chatId;
}
