package com.ruoyi.base.provider.appApi;

import com.ruoyi.base.domain.HrAttendanceClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface AttendanceApi {

    public Map<String, Object> selectGroupAndClass(Long userId,int week);

    public String uploadImg(MultipartFile file);

    String selectDayIsMustDate(Long userId, String today);

    HrAttendanceClass selectClass(Long classId);

    Map<String, Object> selectGroup(Long userId);

    Integer selectDayIsNeedNotDate(Long userId, String yesterday);

    Boolean selectIsExistGroup(Long userId);

    /**
     * 通过考勤组ID，员工ID，日期字符串，获取员工排班制的考勤组班次信息
     * @param groupId
     * @param userId
     * @param day
     * @return
     */
    Map<String, Object> selectScheduGroupAndClass(Long groupId, Long userId, String day);
}
