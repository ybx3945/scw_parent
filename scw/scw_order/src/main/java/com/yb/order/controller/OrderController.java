package com.yb.order.controller;

import com.yb.common.response.AppResponse;
import com.yb.order.pojo.TOrder;
import com.yb.order.service.OrderService;
import com.yb.order.vo.req.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/order")
public class OrderController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder")
    public AppResponse<TOrder> createOrder(@RequestBody OrderInfoVo orderInfoVo) {
        // 获取当前用户的id
        String accessToken = orderInfoVo.getAccessToken();
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null) {
            return AppResponse.fail(null);
        }
        try {
            TOrder tOrder = orderService.saveOrder(orderInfoVo);
            return AppResponse.ok(tOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }
    }
}
