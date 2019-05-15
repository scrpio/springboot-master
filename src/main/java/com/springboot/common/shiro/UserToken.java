package com.springboot.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 自定义token,用于确定登陆者类型
 */
public class UserToken extends UsernamePasswordToken {
    private String loginType;
    private String phone;

    public UserToken() {
    }

    public UserToken(final String phone, final String loginType) {
        this.phone = phone;
        this.loginType = loginType;
    }

    public UserToken(final String username, final String password, final String loginType) {
        super(username, password);
        this.loginType = loginType;
    }

    public UserToken(final String username, final String password,
                     final boolean rememberMe, final String loginType) {
        super(username, password, rememberMe);
        this.loginType = loginType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
