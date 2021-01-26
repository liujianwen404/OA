package com.ruoyi.hr.utils;

import com.ruoyi.base.domain.HrAttendanceClass;
import com.ruoyi.hr.service.IHrAttendanceClassService;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RuoYi首创 html调用 thymeleaf 实现班次读取
 *
 * @author ruoyi
 */
@Service("attendanceThymeleafService")
public class AttendanceThymeleafService {

    @Autowired
    private IHrAttendanceClassService attendanceClassService;

    public List<HrAttendanceClass> getAttendanceClassAll(){
        List<HrAttendanceClass> list = attendanceClassService.selectHrAttendanceClassAll();
        return list;
    }

}
