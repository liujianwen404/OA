package com.ruoyi.base.provider.appApi;

import com.ruoyi.base.domain.HrAttendanceInfo;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;
import java.util.Map;

public interface ClockInApi {

    public AjaxResult saveClockIn(HrAttendanceInfo hrAttendanceInfo,Boolean isAm,Boolean isPm);

    List<Map<String,Object>> getClockIn(Long userId,String date);

    int delClockIn(Long userId, String today);
}
