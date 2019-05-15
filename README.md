## 项目介绍

* 基于SpringBoot + mybatis-plus开发的项目。
* 使用Maven对项目进行模块化管理，提高项目的易开发性、扩展性。
* 采用mysql作为底层数据库。
* 系统管理：包括用户管理、角色管理、部门管理、菜单管理、数据字典、日志管理等等。

## 主要功能

1. 数据库：Druid数据库连接池，监控数据库访问性能，统计SQL的执行性能。
2. 持久层：mybatis持久化，使用 MyBatis-Plus优化，减少sql开发量；aop切换数据库实现读写分离。Transtraction注解事务。
3. MVC： 基于Spring MVC注解，Rest风格Controller。Exception统一管理。
4. Shiro安全登录、URL权限管理、Role角色管理。
5. 缓存和Session：shiro + redis实现session共享，重启服务会话不丢失。
6. 文件上传：ftp发送文件到独立服务器，使文件服务分离。
7. 前后端分离：没有权限的文件只用nginx代理即可。
8. 可用账号密码或手机短信方式登录。

## 项目结构

![image](https://github.com/scrpio/images/blob/master/project.png)

##### 前端所用技术

* VueQuillEditor：富文本编辑器
* ECharts：图表库
* VueParticles：粒子特效
* Vue2 + Vuex + Vue Router + Element UI + ES6 + webpack + axios + Node.js

##### 后端所用技术

* Spring Boot
* SpringMVC
* MyBatis-Plus
* MySQL
* Redis：缓存
* Druid：数据库连接池
* Shiro：安全框架
* Nginx
* Maven
* Swagger2：Api文档生成
* 第三方SDK或服务
    - 云之讯短信服务
    - Mob全国天气预报接口
     

## 截图预览

![image](https://github.com/scrpio/images/blob/master/login.png)

![image](https://github.com/scrpio/images/blob/master/index.png)

![image](https://github.com/scrpio/images/blob/master/menu.png)

![image](https://github.com/scrpio/images/blob/master/user.png)

![image](https://github.com/scrpio/images/blob/master/dept.png)

![image](https://github.com/scrpio/images/blob/master/blog.png)

![image](https://github.com/scrpio/images/blob/master/edit.png)

![image](https://github.com/scrpio/images/blob/master/redis.png)

![image](https://github.com/scrpio/images/blob/master/log.png)

![image](https://github.com/scrpio/images/blob/master/redis_log.png)

![image](https://github.com/scrpio/images/blob/master/dict.png)
