package com.yb.project.service;

import com.yb.common.enums.ProjectStatusEnume;
import com.yb.project.pojo.*;
import com.yb.project.vo.req.ProjectRedisStoreVo;

import java.util.List;

public interface ProjectCreateService {

    // 1.初始化项目
    String initCreateProject(Integer memberId);

    // 4.保存项目
    void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStoreVo redisVo);

    // 展示所有项目
    List<TProject> findAllProject();

    // 根据项目ID展示项目图片
    List<TProjectImages> getProjectImagesByProjectId(Integer id);

    // 根据项目ID获取项目的详细信息
    TProject findProjectInfo(Integer projectId);

    // 根据项目ID获取回报数据
    List<TReturn> findTReturnByProjectId(Integer id);

    // 添加项目标签
    List<TTag> findAllTag();

    // 获取所有的项目分类
    List<TType> findAllType();

    // 根据回报ID获取回报信息
    TReturn findReturnInfo(Integer returnId);
}
