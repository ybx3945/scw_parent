package com.yb.webui.controller;

import com.alibaba.fastjson.JSON;
import com.yb.common.response.AppResponse;
import com.yb.webui.service.ProjectServiceFeign;
import com.yb.webui.vo.resp.ProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class UIController {

    @Autowired
    private ProjectServiceFeign projectService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/")
    public String showIndex(Model model) {
        // 先尝试从Redis中获取数据
        String data = redisTemplate.opsForValue().get("allProject");
        List<ProjectVo> projectVos = JSON.parseArray(data, ProjectVo.class);
        if (projectVos == null || projectVos.size() == 0) {
            // 通过远程调用，从数据库中获取数据
            AppResponse<List<ProjectVo>> allProject = projectService.findAllProject();
            projectVos = allProject.getData();
            // 存入Redis中
            data = JSON.toJSONString(projectVos);
            redisTemplate.opsForValue().set("allProject", data, 2, TimeUnit.HOURS);
        }
        // 展示数据
        model.addAttribute("projectList", projectVos);
        return "index";
    }
}
