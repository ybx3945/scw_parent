package com.yb.order.service;

import com.yb.order.pojo.TOrder;
import com.yb.order.vo.req.OrderInfoVo;

public interface OrderService {

    TOrder saveOrder(OrderInfoVo infoVo);

}
