package com.yb.webui.service.impl;

import com.yb.common.response.AppResponse;
import com.yb.webui.service.ProjectServiceFeign;
import com.yb.webui.vo.resp.ProjectDetailVo;
import com.yb.webui.vo.resp.ProjectVo;
import com.yb.webui.vo.resp.ReturnPayConfirmVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceFeignImpl implements ProjectServiceFeign {
    @Override
    public AppResponse<List<ProjectVo>> findAllProject() {
        AppResponse<List<ProjectVo>> fail = AppResponse.fail(null);
        fail.setMsg("远程调用【查询所有项目】失败");
        return fail;
    }

    @Override
    public AppResponse<ProjectDetailVo> findProjectInfo(Integer projectId) {
        AppResponse<ProjectDetailVo> fail = AppResponse.fail(null);
        fail.setMsg("远程调用【查询项目详细信息】失败");
        return fail;
    }

    @Override
    public AppResponse<ReturnPayConfirmVo> findReturnById(Integer returnId) {
        AppResponse<ReturnPayConfirmVo> fail = AppResponse.fail(null);
        fail.setMsg("远程调用【查询回报信息】失败");
        return fail;
    }
}
