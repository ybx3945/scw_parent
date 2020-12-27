package com.yb.webui.service;

import com.yb.common.response.AppResponse;
import com.yb.webui.service.impl.OrderServiceFeignImpl;
import com.yb.webui.vo.resp.OrderFormInfoSubmitVo;
import com.yb.webui.vo.resp.TOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "SCW-ORDER", fallback = OrderServiceFeignImpl.class)
public interface OrderServiceFeign {

    @PostMapping("/createOrder")
    AppResponse<TOrder> createOrder(@RequestBody OrderFormInfoSubmitVo orderInfoVo);
}
