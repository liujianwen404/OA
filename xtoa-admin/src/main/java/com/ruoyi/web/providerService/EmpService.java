package com.ruoyi.web.providerService;

import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.provider.webApi.EmpApi;
import com.ruoyi.hr.service.HrEmpService;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;

@Service
public class EmpService implements EmpApi {

    @Resource
    private HrEmpService hrEmpService;
    @Override
    public HrEmp findById(Long empId) {
        return hrEmpService.selectTHrEmpById(empId);
    }
}
