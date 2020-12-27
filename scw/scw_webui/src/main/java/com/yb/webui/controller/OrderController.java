package com.yb.webui.controller;

import com.yb.common.response.AppResponse;
import com.yb.webui.service.OrderServiceFeign;
import com.yb.webui.vo.resp.OrderFormInfoSubmitVo;
import com.yb.webui.vo.resp.ReturnPayConfirmVo;
import com.yb.webui.vo.resp.TOrder;
import com.yb.webui.vo.resp.UserRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class OrderController {

    @Resource
    private OrderServiceFeign orderService;

    @RequestMapping("/order/save")
    public String showOrder(OrderFormInfoSubmitVo vo, HttpSession session) {
        ReturnPayConfirmVo returnConfirm = (ReturnPayConfirmVo) session.getAttribute("returnConfirm");
        UserRespVo sessionMember = (UserRespVo) session.getAttribute("sessionMember");
        if (returnConfirm == null || sessionMember == null) {
            return "redirect:/login.html";
        }
        // 设定用户令牌
        vo.setAccessToken(sessionMember.getAccessToken());
        // 设定项目ID
        vo.setProjectid(returnConfirm.getProjectId());
        // 设定回报ID
        vo.setReturnid(returnConfirm.getId());
        // 设定回报数量
        vo.setRtncount(returnConfirm.getNum());
        // 设定返回值
        AppResponse<TOrder> orderResponse = orderService.createOrder(vo);
        TOrder order = orderResponse.getData();
        if (order != null) {
            log.info("orderNum:{}", order.getOrdernum());
            log.info("money:{}", order.getMoney());
            log.info("remark:{}", order.getRemark());
        }
        return "member/minecrowdfunding";
    }
}
