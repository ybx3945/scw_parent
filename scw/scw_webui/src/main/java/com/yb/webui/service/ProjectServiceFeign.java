package com.yb.webui.service;

import com.yb.common.response.AppResponse;
import com.yb.webui.service.impl.ProjectServiceFeignImpl;
import com.yb.webui.vo.resp.ProjectDetailVo;
import com.yb.webui.vo.resp.ProjectVo;
import com.yb.webui.vo.resp.ReturnPayConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "SCW-PROJECT", fallback = ProjectServiceFeignImpl.class)
public interface ProjectServiceFeign {

    @GetMapping("/project/all")
    AppResponse<List<ProjectVo>> findAllProject();

    @GetMapping("/project/findProjectInfo/{projectId}")
    AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable("projectId") Integer projectId);

    @GetMapping("/project/findReturnById/{returnId}")
    AppResponse<ReturnPayConfirmVo> findReturnById(@PathVariable("returnId") Integer returnId);
}
