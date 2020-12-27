package com.yb.user.controller;

import com.yb.common.response.AppResponse;
import com.yb.user.pojo.TMember;
import com.yb.user.pojo.TMemberAddress;
import com.yb.user.service.UserService;
import com.yb.user.utils.SmsTemplate;
import com.yb.user.vo.UserRegistVo;
import com.yb.user.vo.response.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.management.resource.internal.ApproverGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "用户登录和注册模块")
public class UserLoginController {

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取验证码信息")
    @PostMapping("/sendSms")
    public AppResponse<Object> sendSms(String phoneNum) {
        // 生成验证码
        String code = UUID.randomUUID().toString().substring(0, 4);
        System.out.println("当前验证码是：" + code);
        // 将验证码存储在Redis中，5分钟有效
        redisTemplate.opsForValue().set(phoneNum, code, 5, TimeUnit.MINUTES);
        // 短信发送
        try {
            String okMsg = "ok"; //smsTemplate.sendCode(phoneNum, code);
            return AppResponse.ok(okMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail("短信发送失败！");
        }
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/regist")
    public AppResponse<Object> register(UserRegistVo userRegistVo) {
        // 1.获取验证码
        String code = redisTemplate.opsForValue().get(userRegistVo.getLoginacct());
        if (code != null && code.length() > 0) {
            boolean flag = code.equalsIgnoreCase(userRegistVo.getCode());
            if (flag) {
                // 完成注册
                TMember member = new TMember();
                BeanUtils.copyProperties(userRegistVo, member);
                userService.registerUser(member);
                // 删除验证码
                redisTemplate.delete(member.getLoginacct());
                return AppResponse.ok("注册成功");
            } else {
                return AppResponse.fail("验证码错误");
            }
        } else {
            return AppResponse.fail("验证码已失效");
        }
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(@RequestParam("loginacct") String loginacct,
                                         @RequestParam("password") String password) {
        // 使用service完成登录
        TMember member = userService.login(loginacct, password);
        if (member == null) {
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户名或密码错误");
            return fail;
        }
        // 登录成功，通过UUID为登录之后的对象创建令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        UserRespVo userRespVo = new UserRespVo();
        userRespVo.setAccessToken(token);
        // 将service返回的tMember中的属性赋值给当前userRespVo
        BeanUtils.copyProperties(member, userRespVo);
        // 将数据存储到Redis中
        redisTemplate.opsForValue().set(token, member.getId() + "", 2, TimeUnit.HOURS);
        // 返回结果
        return AppResponse.ok(userRespVo);
    }

    @ApiOperation(value = "通过ID获取用户信息")
    @GetMapping("/findUser/{id}")
    public AppResponse<UserRespVo> findUserById(@PathVariable("id") Integer id) {
        TMember tMember = userService.findTMemberById(id);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(tMember, userRespVo);
        return AppResponse.ok(userRespVo);
    }

    @ApiOperation(value = "获取当前登录的用户收货地址")
    @GetMapping("/findAddressList")
    public AppResponse<List<TMemberAddress>> findAddressList(@RequestParam String accessToken) {
        // 根据用户令牌获取用户ID
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null || memberId.length() == 0) {
            AppResponse response = AppResponse.fail(null);
            response.fail("当前用户尚未登录");
            return response;
        }
        // 查询
        List<TMemberAddress> address = userService.findAddressByMemberId(Integer.parseInt(memberId));
        return AppResponse.ok(address);
    }
}
