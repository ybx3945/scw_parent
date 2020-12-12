package com.yb.order.service.impl;

import com.yb.common.enums.OrderStatusEnumes;
import com.yb.common.response.AppResponse;
import com.yb.common.utils.AppDateUtils;
import com.yb.order.pojo.TOrder;
import com.yb.order.service.OrderService;
import com.yb.order.service.ProjectServiceFeign;
import com.yb.order.vo.req.OrderInfoVo;
import com.yb.order.vo.resp.TReturn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectServiceFeign projectService;

    @Override
    public TOrder saveOrder(OrderInfoVo infoVo) {
        TOrder order = new TOrder();
        // 将AccessToken转为用户ID存储到order中
        String memberId = redisTemplate.opsForValue().get(infoVo.getAccessToken());
        order.setMemberid(Integer.parseInt(memberId));
        // 将infoVo中用户提交的数据复制到order中
        BeanUtils.copyProperties(infoVo, order);
        // 生成订单编号
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        order.setOrdernum(orderNum);
        // 创建时间
        order.setCreatedate(AppDateUtils.getFormatTime());
        // 订单状态
        order.setStatus(OrderStatusEnumes.UNPAY.getCode() + "");
        // money：回报个数 * 金额 + 运费
        /*AppResponse<List<TReturn>> returnsResp = projectService.getReturnListByPid(infoVo.getProjectid());
        List<TReturn> returns = returnsResp.getData();
        // 默认获取第一个回报
        //TReturn tReturn = returns.get(0);
        // 保证returnId是属于projectId的回报
        for (TReturn tReturn : returns) {
            if (tReturn.getId().equals(infoVo.getReturnid())) {
                Integer money = order.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
                order.setMoney(money);
            }
        }*/
        // 直接通过returnId获取数据，这种方法没有验证当前的returnId是否属于projectId对应的回报
        TReturn tReturn = projectService.findReturnById(infoVo.getReturnid());
        Integer money = order.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
        order.setMoney(money);
        return order;
    }
}
