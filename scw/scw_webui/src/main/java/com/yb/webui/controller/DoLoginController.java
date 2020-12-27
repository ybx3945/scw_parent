package com.yb.webui.controller;

import com.yb.common.response.AppResponse;
import com.yb.webui.service.UserServiceFeign;
import com.yb.webui.vo.resp.UserRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class DoLoginController {

    @Resource
    private UserServiceFeign userServiceFeign;

    @RequestMapping("/doLogin")
    public String doLogin(String loginacct, String password, HttpSession session) {
        // 调用服务
        AppResponse<UserRespVo> login = userServiceFeign.login(loginacct, password);
        // 获取返回结果
        UserRespVo data = login.getData();
        if (data == null) {
            // 继续登录
            return "redirect:/login.html";
        }
        // 登录成功
        session.setAttribute("sessionMember", data);
        String url = (String) session.getAttribute("url");
        if (url == null) {
            return "redirect:/";
        } else {
            return "redirect:/" + url;
        }
    }
}
