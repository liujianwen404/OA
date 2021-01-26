package com.xtoa.web.controller;

import com.ruoyi.base.domain.HrEmp;
import com.ruoyi.base.provider.webApi.EmpApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/webApi")
public class EmpController {

    @Reference(retries = 0,check = false)
    private EmpApi empApi;

    @GetMapping("/getEmp")
    public HrEmp getEmp(@RequestParam Long empId){
        return empApi.findById(empId);
    }
}
