package com.lzly.crm.service.impl;

import com.lzly.crm.entity.User;
import com.lzly.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 嘟嘟~
 * @version 1.0
 * @date 2020/2/11 19:45
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public User login(String userName, String userPassword) {
        return null;
    }
    @Override
    public String loginValdate(String userName) {
        int mun=5;
        String key ="user:loginCount:fail:"+userName;
        Map<String,Object> map=new HashMap<String,Object>();
        if (!redisTemplate.hasKey(key)){
            redisTemplate.opsForValue().set(key,"1");
            redisTemplate.expire(key,2, TimeUnit.MINUTES);
            return "登录失败，当前还允许输入错误"+(mun-1)+"次";
        }else {
            long loginFailCount = Long.parseLong((String) redisTemplate.opsForValue().get(key));
            if (loginFailCount<mun-1){
                redisTemplate.opsForValue().increment(key,1);
               long seconds = redisTemplate.getExpire(key,TimeUnit.SECONDS);
               return userName+"登录失败，在"+seconds+"秒内允许输入错误"+(mun-1-loginFailCount)+"次";
           }else {
                redisTemplate.opsForValue().set("user:loginTime:lock:"+userName,"1");
                redisTemplate.expire("user:loginTime:lock:"+userName,1,TimeUnit.HOURS);
               return "因为登录失败次数超过限制"+mun+"次，已经对其限制登录";
           }
        }

    }
    @Override
    public Map<String,Object> loginUserLock(String userName) {
        String key ="user:loginTime:lock:"+userName;
        Map<String,Object> map=new HashMap<String,Object>();
        System.out.println();
        if (redisTemplate.hasKey(key)){
            long lockTime = redisTemplate.getExpire(key,TimeUnit.MINUTES);
            map.put("flag",true);
            map.put("lockTime",lockTime);
        }else {
            map.put("flag",false);
        }
        return map;
    }
}
