package com.ruoyi.framework.util;

public enum LoginType {
    PASSWORD("password"),
    NOPASSWORD("nopassword");

    private String code;
    private LoginType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
