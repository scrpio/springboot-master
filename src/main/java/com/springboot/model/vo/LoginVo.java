package com.springboot.model.vo;

public class LoginVo {
    private String username;
    private String password;
    private String captchaId;
    private String captcha;
    private boolean rememberMe;
    private String phone;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public String getCaptcha() {
        return captcha;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public String getPhone() {
        return phone;
    }
}
