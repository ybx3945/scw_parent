package com.yb.webui.service.impl;

import com.yb.common.response.AppResponse;
import com.yb.webui.service.UserServiceFeign;
import com.yb.webui.vo.resp.UserAddressVo;
import com.yb.webui.vo.resp.UserRespVo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public class UserServiceFeignImpl implements UserServiceFeign {

    @Override
    public AppResponse<UserRespVo> login(String loginacct, String password) {
        AppResponse<UserRespVo> fail = AppResponse.fail(new UserRespVo());
        fail.setMsg("远程调用[用户注册]失败");
        return fail;
    }

    @Override
    public AppResponse<UserRespVo> findUserById(Integer id) {
        AppResponse<UserRespVo> fail = AppResponse.fail(new UserRespVo());
        fail.setMsg("远程调用[获取会员信息]失败");
        return fail;
    }

    @Override
    public AppResponse<List<UserAddressVo>> findAddressList(String accessToken) {
        AppResponse<List<UserAddressVo>> fail = AppResponse.fail(null);
        fail.setMsg("远程调用[获取会员信息]失败");
        return fail;
    }
}
