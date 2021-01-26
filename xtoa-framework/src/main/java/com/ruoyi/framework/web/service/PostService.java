package com.ruoyi.framework.web.service;

import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RuoYi首创 html调用 thymeleaf 实现岗位读取
 *
 * @author ruoyi
 */
@Service("postService")
public class PostService {

    @Autowired
    private ISysPostService iSysPostService;

    public List<SysPost> getPostAll(){
        return iSysPostService.selectPostAll();
    }

}
