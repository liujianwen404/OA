package com.ruoyi.web.providerService.app;

import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.base.provider.appApi.AttendanceApi;
import com.ruoyi.hr.service.IHrAttendanceClassService;
import com.ruoyi.hr.service.IHrAttendanceGroupService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Slf4j
@Service
public class AttendanceService implements AttendanceApi {

    @Autowired
    private IHrAttendanceGroupService attendanceGroupService;

    @Autowired
    private IHrAttendanceClassService attendanceClassService;

    @Override
    public Map<String, Object> selectGroupAndClass(Long userId,int week) {
        return attendanceGroupService.selectGroupAndClass(userId,week);
    }

    @SneakyThrows
    @Override
    public String uploadImg(MultipartFile file) {
        return attendanceGroupService.uploadImg(file);
    }

    @Override
    public String selectDayIsMustDate(Long userId, String today) {
        return attendanceGroupService.selectDayIsMustDate(userId,today);
    }

    @Override
    public HrAttendanceClass selectClass(Long classId) {
        return attendanceClassService.selectHrAttendanceClassById(classId);
    }

    @Override
    public Map<String, Object> selectGroup(Long userId) {
        return attendanceGroupService.selectGruopByEmpIdFromApi(userId);
    }

    @Override
    public Integer selectDayIsNeedNotDate(Long userId, String yesterday) {
        return attendanceGroupService.selectDayIsNeedNotDate(userId,yesterday);
    }

    @Override
    public Boolean selectIsExistGroup(Long userId) {
        return attendanceGroupService.selectIsExistGroup(userId);
    }

    @Override
    public Map<String, Object> selectScheduGroupAndClass(Long groupId, Long userId, String day) {
        return attendanceGroupService.selectScheduGroupAndClass(groupId,userId,day);
    }


}
