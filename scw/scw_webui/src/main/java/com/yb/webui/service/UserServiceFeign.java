package com.yb.webui.service;

import com.yb.common.response.AppResponse;
import com.yb.webui.service.impl.UserServiceFeignImpl;
import com.yb.webui.vo.resp.UserAddressVo;
import com.yb.webui.vo.resp.UserRespVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "SCW-USER", fallback = UserServiceFeignImpl.class)
public interface UserServiceFeign {

    @PostMapping("/login")
    AppResponse<UserRespVo> login(@RequestParam("loginacct") String loginacct,
                                  @RequestParam("password") String password);

    @GetMapping("/findUser/{id}")
    AppResponse<UserRespVo> findUserById(@PathVariable("id") Integer id);

    @GetMapping("/findAddressList")
    AppResponse<List<UserAddressVo>> findAddressList(@RequestParam String accessToken);
}
