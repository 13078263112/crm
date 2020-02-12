package com.lzly.crm.controller;

import com.lzly.crm.entity.User;
import com.lzly.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 嘟嘟~
 * @version 1.0
 * @date 2020/2/10 22:17
 */
@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam("userName") final String userName,
                                     @RequestParam("userPassword") final String userPassword,
                                     HttpServletResponse response){
        Map<String,Object> map=new HashMap<String,Object>();
        Map<String, Object> loginUserLock = userService.loginUserLock(userName);
        if ((boolean)loginUserLock.get("flag")){
            map.put("code ", "200");
            map.put("msg", "登录失败");
            map.put("data", "登录失败，因"+userName+"用户超过限制登录，已被禁用。还剩"+loginUserLock.get("lockTime")+"分钟");
        }else {
            User user = userService.login(userName, userPassword);
            if (user!=null){
                map.put("code ", "200");
                map.put("msg", "登录成功");
                Cookie cookie=new Cookie("userName",user.getUserName());
                cookie.setMaxAge(24*60*60);
                cookie.setPath("/");
                response.addCookie(cookie);

            }else {
                String loginValdate = userService.loginValdate(userName);
                map.put("code ", "200");
                map.put("msg", "登录失败");
                map.put("data",loginValdate);
            }
        }
        return map ;
    }
    @GetMapping("/getCookie")
    public Map<String, Object> getCookie(HttpServletRequest request,
                                         HttpServletResponse response) {
        Map<String,Object> map=new HashMap<String,Object>();
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if ("userName".equals(cookie.getName())){
                    map.put("code ", "200");
                    map.put("msg", "登录认证成功");
                    return map ;
                }else {
                    map.put("code ", "200");
                    map.put("msg", "登录认证失败");
                }
            }
        }else {
            map.put("code ", "200");
            map.put("msg", "登录认证失败");
        }
        return map ;
    }

}
