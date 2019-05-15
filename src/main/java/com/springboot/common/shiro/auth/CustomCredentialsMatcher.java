package com.springboot.common.shiro.auth;

import com.springboot.common.shiro.ShiroKit;
import com.springboot.common.shiro.UserToken;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 自定义密码验证
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UserToken token = (UserToken) authcToken;
        Object tokenCredentials = null;
        Object accountCredentials = getCredentials(info);
        if (token.getLoginType().equals("Account")) {
            tokenCredentials = ShiroKit.encrypt(String.valueOf(token.getUsername()), String.valueOf(token.getPassword()));
        } else {
            tokenCredentials = String.valueOf(token.getPhone());
        }
        //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
        return equals(tokenCredentials, accountCredentials);
    }
}
