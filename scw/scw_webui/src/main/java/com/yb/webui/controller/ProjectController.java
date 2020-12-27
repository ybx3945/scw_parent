package com.yb.webui.controller;

import com.google.inject.internal.cglib.reflect.$FastMember;
import com.yb.common.response.AppResponse;
import com.yb.webui.service.ProjectServiceFeign;
import com.yb.webui.service.UserServiceFeign;
import com.yb.webui.vo.resp.ProjectDetailVo;
import com.yb.webui.vo.resp.ReturnPayConfirmVo;
import com.yb.webui.vo.resp.UserAddressVo;
import com.yb.webui.vo.resp.UserRespVo;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectServiceFeign projectService;

    @Autowired
    private UserServiceFeign userService;

    @RequestMapping("/projectInfo")
    public String getProjectInfo(Integer id, Model model, HttpSession session) {
        AppResponse<ProjectDetailVo> vo = projectService.findProjectInfo(id);
        ProjectDetailVo data = vo.getData();
        model.addAttribute("DetailVo", data);
        session.setAttribute("DetailVo", data);
        return "project/project";
    }

    @RequestMapping("/confirm/returns/{projectId}/{returnId}")
    public String findReturnInfo(@PathVariable("projectId") Integer projectId,
                                 @PathVariable("returnId") Integer returnId,
                                 HttpSession session,
                                 Model model) {
        // 通过session获取数据
        ProjectDetailVo detailVo = (ProjectDetailVo) session.getAttribute("DetailVo");
        // 获取return的数据
        AppResponse<ReturnPayConfirmVo> returnResponse = projectService.findReturnById(returnId);
        ReturnPayConfirmVo data = returnResponse.getData();
        // 完善data的数据
        data.setProjectId(projectId);
        data.setProjectName(detailVo.getName());
        data.setId(returnId);
        // 查询用户数据
        Integer memberid = detailVo.getMemberid();
        AppResponse<UserRespVo> memberResponse = userService.findUserById(memberid);
        UserRespVo member = memberResponse.getData();
        data.setMemberId(memberid);
        data.setMemberName(member.getUsername());
        // 存
        model.addAttribute("returnConfirm", data);
        session.setAttribute("returnConfirm", data);
        return "project/pay-step-1";
    }

    @RequestMapping("/confirm/order/{num}")
    public String payOrder(@PathVariable("num") Integer num, Model model, HttpSession session) {
        // 判断当前用户是否登录
        UserRespVo userRespVo = (UserRespVo) session.getAttribute("sessionMember");
        if (userRespVo == null) {
            // 存储当前路径
            session.setAttribute("url", "project/confirm/order/" + num);
            return "redirect:/login.html";
        }
        // 获取当前登录用户的ID
        String accessToken = userRespVo.getAccessToken();
        AppResponse<List<UserAddressVo>> addressResponse = userService.findAddressList(accessToken);
        List<UserAddressVo> addresses = addressResponse.getData();
        model.addAttribute("addresses", addresses);
        // 将上一个请求的session获取过来
        ReturnPayConfirmVo returnConfirm = (ReturnPayConfirmVo) session.getAttribute("returnConfirm");
        returnConfirm.setNum(num);
        returnConfirm.setTotalPrice(new BigDecimal(num * returnConfirm.getSupportmoney() + returnConfirm.getFreight()));
        session.setAttribute("returnConfirm", returnConfirm);
        return "project/pay-step-2";
    }
}
