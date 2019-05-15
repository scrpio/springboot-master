package com.springboot.common.shiro;

import com.springboot.common.enums.ManagerStatus;
import com.springboot.model.User;
import com.springboot.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class PhoneRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneRealm.class);

    @Autowired
    private IUserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LOGGER.info("开始权限认证");
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(shiroUser.getUrls());
        authorizationInfo.addRoles(shiroUser.getRoles());
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        LOGGER.info("开始手机号登录认证");

        UserToken token = (UserToken)authenticationToken;
        String phone = token.getPhone();
        User user = userService.getUserByPhone(phone);
        // 账号不存在
        if (null == user) {
            throw new UnknownAccountException();
        }
        // 账号被冻结
        if (user.getStatus().equals(ManagerStatus.FREEZE.getCode())) {
            throw new LockedAccountException();
        }
        // 账号未启用
        if (user.getStatus().equals(ManagerStatus.DELETE.getCode())) {
            throw new DisabledAccountException();
        }

        ShiroUser shiroUser = new ShiroUser();

        shiroUser.setId(user.getId());
        shiroUser.setUsername(user.getUsername());
        shiroUser.setNickname(user.getNickname());
        shiroUser.setDeptId(user.getDeptId());
        shiroUser.setRoles(userService.getRolesByUsername(user.getUsername()));
        shiroUser.setUrls(userService.getMenusByUsername(user.getUsername()));

        return new SimpleAuthenticationInfo(shiroUser, phone, super.getName());
    }
}
