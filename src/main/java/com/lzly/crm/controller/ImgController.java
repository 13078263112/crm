package com.lzly.crm.controller;

import com.lzly.crm.entity.ImgResult;
import com.lzly.crm.utility.VerifyCodeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 嘟嘟~
 * @version 1.0
 * @date 2020/2/10 15:07
 */

@RestController
@RequestMapping("/img")
//解决跨域问题
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ImgController {

    //获取验证码Img
    @GetMapping("/verify")
    public Map<String, Object> getYZM(HttpServletRequest request, HttpServletResponse response) {
        ImgResult rs = null;
        try {
            rs = VerifyCodeUtils.VerifyCode(74, 38, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpSession session = request.getSession();
        session.setAttribute("verify", rs.getCode());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result ", "200");
        map.put("msg", "查询成功");
        map.put("list", rs.getImg());
        return map;
    }
    //验证验证码的正确
    @PostMapping("/verify/{verify}")
    public Map<String, Object> verifyYZM(HttpServletRequest request,@PathVariable("verify") String verify) {
        System.out.println("执行验证验证码的正确");
        String sessionVerify = (String) request.getSession().getAttribute("verify");
        Map<String, Object> map = new HashMap<String, Object>();

        if (sessionVerify.equalsIgnoreCase(verify)) {
            map.put("result ", "200");
            map.put("msg", "验证正确");
            request.getSession().removeAttribute("yzm");
        } else {
            map.put("result ", "200");
            map.put("msg", "验证错误");
        }
        return map;
    }
}
