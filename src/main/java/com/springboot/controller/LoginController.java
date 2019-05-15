package com.springboot.controller;

import com.google.gson.Gson;
import com.springboot.common.annotation.SystemLog;
import com.springboot.common.enums.LogType;
import com.springboot.common.shiro.ShiroKit;
import com.springboot.common.shiro.UserToken;
import com.springboot.common.util.*;
import com.springboot.model.User;
import com.springboot.model.vo.LoginVo;
import com.springboot.model.vo.Result;
import com.springboot.service.IRedisService;
import com.springboot.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
public class LoginController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IRedisService redisService;

    /**
     * 初始化验证码
     *
     * @return
     */
    @GetMapping("/captcha/init")
    public Result<Object> initCaptcha() {
        String captchaId = UUID.randomUUID().toString();
        String code = ToolUtil.getRandomString(4);
        //缓存验证码
        redisService.set("captcha:img:" + captchaId, code);
        redisService.expire("captcha:img:" + captchaId, 60 * 30);
        return new ResultUtil<Object>().setData(captchaId);
    }

    /**
     * 生成验证码图片
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/captcha/draw/{captchaId}")
    public void drawCaptcha(@PathVariable("captchaId") String captchaId, HttpServletResponse response) throws IOException {
        //得到验证码 生成指定验证码
        String code = redisService.get("captcha:img:" + captchaId);
        CaptchaUtil vCode = new CaptchaUtil(116, 36, 4, 10, code);
        vCode.write(response.getOutputStream());
    }

    @SystemLog(value = "账号密码登录", type = LogType.LOGIN)
    @PostMapping("/login/account")
    public Result<Object> accountLogin(@RequestBody LoginVo loginVo) {
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        boolean rememberMe = loginVo.isRememberMe();
        String captchaId = loginVo.getCaptchaId();
        String captcha = loginVo.getCaptcha();

        //验证码
        String code = redisService.get("captcha:img:" + captchaId);
        if (StringUtils.isBlank(code)) {
            return new ResultUtil<Object>().setErrorMsg("验证码已过期，请重新获取");
        }

        if (!captcha.equalsIgnoreCase(code)) {
            return new ResultUtil<Object>().setErrorMsg("验证码输入错误");
        }

        Subject subject = ShiroKit.getSubject();
        UserToken token = new UserToken(username, password, rememberMe, "Account");
        try {
            subject.login(token);
            redisService.del("captcha:img:" + captchaId);
            ShiroKit.getSession().setAttribute("sessionFlag", true);
            return new ResultUtil<Object>().setData(username);
        } catch (UnknownAccountException e) {
            return new ResultUtil<Object>().setErrorMsg("账号不存在！");
        } catch (IncorrectCredentialsException e) {
            return new ResultUtil<Object>().setErrorMsg("密码错误！");
        } catch (LockedAccountException e) {
            return new ResultUtil<Object>().setErrorMsg("账号已冻结！");
        } catch (DisabledAccountException e) {
            return new ResultUtil<Object>().setErrorMsg("账号未启用！");
        } catch (Throwable e) {
            return new ResultUtil<Object>().setErrorMsg(e.getMessage());
        }
    }

    @GetMapping(value = "/captcha/sms")
    public Result<Object> getSMSCode(@RequestParam String phone) {
        String param = ToolUtil.getRandomNumber();
        String captchaId = UUID.randomUUID().toString();
        //缓存验证码
        redisService.set("captcha:sms:" + captchaId, param);
        redisService.expire("captcha:sms:" + captchaId, 60 * 30);
        if (NoteUtil.checkNum(phone, param)) {
            return new ResultUtil<Object>().setData(captchaId);
        }
        return new ResultUtil<Object>().setErrorMsg("发送失败");
    }

    @SystemLog(value = "手机短信登录", type = LogType.LOGIN)
    @PostMapping("/login/phone")
    public Result<Object> phoneLogin(@RequestBody LoginVo loginVo) {
        String code = redisService.get("captcha:sms:" + loginVo.getCaptchaId());
        if (StringUtils.isBlank(code)) {
            return new ResultUtil<Object>().setErrorMsg("验证码已过期，请重新获取");
        }
        if (loginVo.getCaptcha().equals(code)) {
            Subject current = ShiroKit.getSubject();
            UserToken token = new UserToken(loginVo.getPhone(), "Phone");
            try {
                current.login(token);
                redisService.del("captcha:sms:" + loginVo.getCaptchaId());
                User user = userService.getUserByPhone(loginVo.getPhone());
                ShiroKit.getSession().setAttribute("sessionFlag", true);
                return new ResultUtil<Object>().setData(user.getUsername());
            } catch (UnknownAccountException e) {
                return new ResultUtil<Object>().setErrorMsg("账号不存在！");
            } catch (IncorrectCredentialsException e) {
                return new ResultUtil<Object>().setErrorMsg("密码错误！");
            } catch (LockedAccountException e) {
                return new ResultUtil<Object>().setErrorMsg("账号已冻结！");
            } catch (DisabledAccountException e) {
                return new ResultUtil<Object>().setErrorMsg("账号未启用！");
            } catch (Throwable e) {
                return new ResultUtil<Object>().setErrorMsg(e.getMessage());
            }
        } else {
            return new ResultUtil<Object>().setErrorMsg("验证码错误");
        }
    }

    /**
     * 天气
     *
     * @param request
     * @return
     */
    @GetMapping("/weather")
    public Result<Object> weatherInfo(HttpServletRequest request) {
        String ip = IpInfoUtil.getIpAddr(request);
        String result = WeatherUtil.getWeatherInfo(ip);
        return new ResultUtil<Object>().setData(new Gson().fromJson(result, Object.class));
    }

    /**
     * 获取省份城市
     * @return
     */
    @GetMapping("/city")
    public Result<Object> city() {
        String result = WeatherUtil.getCitys();
        return new ResultUtil<Object>().setData(new Gson().fromJson(result, Object.class));
    }

    /**
     * 城市天气
     *
     * @param city
     * @param province
     * @return
     */
    @GetMapping("/city/weather")
    public Result<Object> weatherByCity(String city, String province) {
        String result = WeatherUtil.getWeatherByCity(city,province);
        return new ResultUtil<Object>().setData(new Gson().fromJson(result, Object.class));
    }

    /**
     * 注销
     */
    @GetMapping("/logout")
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        ShiroKit.getSubject().logout();

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            Cookie temp = new Cookie(cookie.getName(), "");
            temp.setMaxAge(0);
            response.addCookie(temp);
        }
        return new ResultUtil<Object>().setSuccessMsg("注销成功");
    }
}
