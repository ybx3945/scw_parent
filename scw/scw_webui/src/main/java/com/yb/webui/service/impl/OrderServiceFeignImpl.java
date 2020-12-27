package com.yb.webui.service.impl;

import com.yb.common.response.AppResponse;
import com.yb.webui.service.OrderServiceFeign;
import com.yb.webui.vo.resp.OrderFormInfoSubmitVo;
import com.yb.webui.vo.resp.TOrder;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceFeignImpl implements OrderServiceFeign {

    @Override
    public AppResponse<TOrder> createOrder(OrderFormInfoSubmitVo orderInfoVo) {
        AppResponse<TOrder> fail = AppResponse.fail(null);
        fail.setMsg("远程调用【订单创建】失败！");
        return fail;
    }
}
