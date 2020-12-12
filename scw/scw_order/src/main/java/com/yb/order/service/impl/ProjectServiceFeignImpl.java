package com.yb.order.service.impl;

import com.yb.common.response.AppResponse;
import com.yb.order.service.ProjectServiceFeign;
import com.yb.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceFeignImpl implements ProjectServiceFeign {

    @Override
    public AppResponse<List<TReturn>> getReturnListByPid(Integer projectId) {
        AppResponse<List<TReturn>> fail = AppResponse.fail(null);
        fail.setMsg("远程调用失败【订单】");
        return fail;
    }

    @Override
    public TReturn findReturnById(Integer returnId) {
        return new TReturn();
    }
}
