package com.ruoyi.framework.shiro.token;

import com.ruoyi.framework.util.LoginType;
import org.apache.shiro.authc.UsernamePasswordToken;

public class CustomUsernamePasswordToken extends UsernamePasswordToken {
    private LoginType type;

    public CustomUsernamePasswordToken() {
        super();
    }

    /**
     * 免密登录
     */
    public CustomUsernamePasswordToken(String username) {
        super(username, "", false, null);
        this.type = LoginType.NOPASSWORD;
    }

    /**
     * 账号密码登录
     */
    public CustomUsernamePasswordToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe, null);
        this.type = LoginType.PASSWORD;
    }

    public LoginType getType() {
        return type;
    }

    public void setType(LoginType type) {
        this.type = type;
    }

}
