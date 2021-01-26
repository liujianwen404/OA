package com.ruoyi.base.provider.appApi;

import com.ruoyi.system.domain.SysUser;

public interface UserApi {

    public SysUser getSysUserByDingUserID(String dingUserID);

    public SysUser getSysUserByUserID(Long userId);
}
