package com.ruoyi.framework.web.service;

import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.domain.TCity;
import com.ruoyi.system.service.ITCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RuoYi首创 html调用 thymeleaf 实现岗位读取
 *
 * @author ruoyi
 */
@Service("cityService")
public class CityService {

    @Autowired
    private ITCityService itCityService;

    public List<TCity> getCityAll(){
        return itCityService.selectAll();
    }

}
