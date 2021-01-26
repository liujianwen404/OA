package com.ruoyi.framework.web.service;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RuoYi首创 html调用 thymeleaf 实现岗位读取
 *
 * @author ruoyi
 */
@Service("otherService")
public class OtherService {

    @Autowired
    private ISysUserService sysUserService;
    /**
     * 获取所有HR
     * @return
     */
    public List<SysUser> getHrList(){
        List<SysUser> list = sysUserService.getUserRoleList("hr");
        return list;
    }

    /**
     * 获取所有HR
     * @return
     */
    public SysUser getSysUser(){

        return ShiroUtils.getSysUser();
    }


}
