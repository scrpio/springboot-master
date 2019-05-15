package com.springboot.common.shiro;

import java.io.Serializable;
import java.util.Set;

public class ShiroUser implements Serializable {
    private static final long serialVersionUID = 1L;

    public Long id;             // 主键ID
    public String username;     // 姓名
    public String nickname;     // 昵称
    public Integer deptId;      // 部门ID
    private Set<String> urls;   // 权限集
    private Set<String> roles;  // 角色集

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
