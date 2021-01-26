package com.ruoyi.framework.web.service;

import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.TCity;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ITCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RuoYi首创 html调用 thymeleaf 实现岗位读取
 *
 * @author ruoyi
 */
@Service("deptService")
public class DeptService {

    @Autowired
    private ISysDeptService iSysDeptService;

    public List<SysDept> getDeptAll(){

        return iSysDeptService.getDeptAll();
    }

}
