package com.lzly.crm.controller;

import com.lzly.crm.entity.User;
import com.lzly.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                                     @RequestParam("userPassword") final String userPassword){
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
            }else {
                String loginValdate = userService.loginValdate(userName);
                map.put("code ", "200");
                map.put("msg", "登录失败");
                map.put("data",loginValdate);
            }
        }
        return map ;
    }
}
