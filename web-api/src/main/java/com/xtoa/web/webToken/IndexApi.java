package com.xtoa.web.webToken;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexApi {

    @RequestMapping("/")
    public String getToken(){
       return "index";
    }

}
