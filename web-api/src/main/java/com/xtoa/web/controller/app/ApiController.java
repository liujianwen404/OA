package com.xtoa.web.controller.app;

import cn.hutool.http.HttpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/webApi/api")
public class ApiController {

    @RequestMapping("map")
    public String apiMapQQ(String location,String key){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("location", location);
        paramMap.put("key", key);
        String result= HttpUtil.get("https://apis.map.qq.com/ws/geocoder/v1/", paramMap);
        return result;
    }
}
