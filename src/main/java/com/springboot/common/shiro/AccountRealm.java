package com.springboot.common.shiro;

import com.springboot.common.enums.ManagerStatus;
import com.springboot.model.User;
import com.springboot.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AccountRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRealm.class);
    @Autowired
    private IUserService userService;

    /**
     * 权限认证
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LOGGER.info("开始权限认证");
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(shiroUser.getUrls());
        info.addRoles(shiroUser.getRoles());
        return info;
    }

    /**
     * 登录认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        LOGGER.info("开始登录认证");
        String username = (String) token.getPrincipal();
        User user = userService.getUserByUsername(username);
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

        String credentials = user.getPassword();
        ByteSource credentialsSalt = new Md5Hash(username);
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, super.getName());
    }
}
