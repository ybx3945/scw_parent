package com.yb.order.service;

import com.yb.common.response.AppResponse;
import com.yb.order.service.impl.ProjectServiceFeignImpl;
import com.yb.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "SCW-PROJECT", fallback = ProjectServiceFeignImpl.class)
public interface ProjectServiceFeign {

    @GetMapping("/project/details/returns/{projectId}")
    AppResponse<List<TReturn>> getReturnListByPid(@PathVariable("projectId") Integer projectId);

    @GetMapping("/project/findReturnById/{returnId}")
    AppResponse<TReturn> findReturnById(@PathVariable("returnId") Integer returnId);
}
