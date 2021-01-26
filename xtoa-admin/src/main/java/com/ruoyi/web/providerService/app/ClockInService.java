package com.ruoyi.web.providerService.app;

import com.ruoyi.base.domain.HrAttendanceInfo;
import com.ruoyi.base.provider.appApi.ClockInApi;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hr.service.IHrAttendanceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class ClockInService implements ClockInApi {

    @Autowired
    private IHrAttendanceInfoService attendanceInfoService;

    @Override
    public AjaxResult saveClockIn(HrAttendanceInfo hrAttendanceInfo,Boolean isAm,Boolean isPm) {
        if (attendanceInfoService.insertByApp(hrAttendanceInfo, isAm, isPm) == 0) {
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    public List<Map<String,Object>> getClockIn(Long userId, String date) {
        return attendanceInfoService.getClockIn(userId,date);
    }

    @Override
    public int delClockIn(Long userId, String today) {
        return attendanceInfoService.delClockIn(userId,today);
    }
}
