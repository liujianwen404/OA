package com.ruoyi.dingTalk.dingCallBackDTO;

import lombok.Data;

import java.util.List;

@Data
public class AttendanceCallBackDTO {

    /**
     * 事件类型
     */
    private String EventType;

    /**
     * 数据列表
     */
    private List<String> DataList;

    /**
     * 打卡地址
     */
    private String address;

    /**
     * 打卡时间
     */
    private String checkTime;

    /**
     * 企业id
     */
    private String corpId;

    /**
     * 考勤组id
     */
    private String groupId;

    /**
     * 经度信息
     */
    private String latitude;

    /**
     * 纬度信息
     */
    private String longitude;

    /**
     * 打卡方式。MAP表示定位打卡，WIFI表示wifi打卡。
     */
    private String locationMethod;

    /**
     * 员工id
     */
    private String userId;


}
