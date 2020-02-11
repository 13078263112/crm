package com.lzly.crm.service;

import com.lzly.crm.entity.User;

import java.util.Map;

/**
 * @author 嘟嘟~
 * @version 1.0
 * @date 2020/2/11 19:45
 */
public interface UserService {
        /**
         *
         * @param userName 用户名
         * @param userPassword 密码
         * @return
         */
        User login(String userName,String userPassword);

        /**
         *
         * @param userName 用户名
         * @return 给用户详细信息提示
         */
        String  loginValdate(String userName);

        /**
         * 判断当前用户是否被限制登录
         * @param userName 用户名
         * @return
         */
        Map<String,Object> loginUserLock(String userName);


}
