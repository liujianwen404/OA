package com.ruoyi.web.providerService.app;

import com.ruoyi.base.provider.appApi.UserApi;
import com.ruoyi.hr.service.HrEmpService;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class DingUserService implements UserApi {

    @Autowired
    private HrEmpService tHrEmpService;

    @Autowired
    private ISysUserService sysUserService;


    @Override
    public SysUser getSysUserByDingUserID(String dingUserID) {
        return tHrEmpService.getSysUserByDingUserId(dingUserID,false);
    }

    @Override
    public SysUser getSysUserByUserID(Long userId) {
        return sysUserService.selectUserById(userId);
    }
}
