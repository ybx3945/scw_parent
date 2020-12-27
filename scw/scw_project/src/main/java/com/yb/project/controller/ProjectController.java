package com.yb.project.controller;

import com.yb.common.response.AppResponse;
import com.yb.common.utils.OSSTemplate;
import com.yb.project.pojo.*;
import com.yb.project.service.ProjectCreateService;
import com.yb.project.vo.resp.ProjectDetailVo;
import com.yb.project.vo.resp.ProjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
@Api(tags = "项目模块")
public class ProjectController {

    @Autowired
    private OSSTemplate ossTemplate;

    @Autowired
    private ProjectCreateService projectService;

    @PostMapping("/upload")
    public AppResponse<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile[] files) throws IOException {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (MultipartFile item : files) {
                // item.getInputStream()：获取文件的文件流
                // item.getOriginalFilename()：获取文件之前的名字
                String fileUrl = ossTemplate.upload(item.getInputStream(), item.getOriginalFilename());
                list.add(fileUrl);
            }
        }
        map.put("urls", list);
        return AppResponse.ok(map);
    }

    @ApiOperation(value = "展示所有的项目")
    @GetMapping("/all")
    public AppResponse<List<ProjectVo>> findAllProject() {
        // 1. 创建集合用户存储所有的项目
        List<ProjectVo> list = new ArrayList<>();
        // 2. 查询
        List<TProject> proVos = projectService.findAllProject();
        for (TProject tProject : proVos) {
            // 获取当前项目ID
            Integer projectId = tProject.getId();
            // 根据项目ID查询头图片
            List<TProjectImages> images = projectService.getProjectImagesByProjectId(projectId);
            ProjectVo projectVo = new ProjectVo();
            // 复制属性
            BeanUtils.copyProperties(tProject, projectVo);
            // 添加头图片
            if (images != null && images.size() > 0) {
                for (TProjectImages imgs : images) {
                    // 获取头图片
                    if (imgs.getImgtype() == 0) {
                        projectVo.setHeaderImage(imgs.getImgurl());
                    }
                }
            }
            list.add(projectVo);
        }
        return AppResponse.ok(list);
    }

    @ApiOperation(value = "查询项目的详细信息")
    @GetMapping("/findProjectInfo/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable("projectId") Integer projectId) {
        ProjectDetailVo detailVo = new ProjectDetailVo();
        TProject projectInfo = projectService.findProjectInfo(projectId);
        BeanUtils.copyProperties(projectInfo, detailVo);
        // 添加图片
        detailVo.setDetailsImage(new ArrayList<>());
        List<TProjectImages> images = projectService.getProjectImagesByProjectId(projectId);
        if (images == null) {
            detailVo.setHeaderImage(null);
        } else {
            for (TProjectImages img : images) {
                if (img.getImgtype() == 0) {
                    // 头图
                    detailVo.setHeaderImage(img.getImgurl());
                } else {
                    detailVo.getDetailsImage().add(img.getImgurl());
                }
            }
        }
        // 添加回报
        List<TReturn> returns = projectService.findTReturnByProjectId(projectId);
        detailVo.setProjectReturns(returns);
        return AppResponse.ok(detailVo);
    }

    @ApiOperation(value = "查询所有的项目标签")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag() {
        List<TTag> tags = projectService.findAllTag();
        return AppResponse.ok(tags);
    }

    @ApiOperation(value = "查询所有的项目分类")
    @GetMapping("/findAllType")
    public AppResponse<List<TType>> findAllType() {
        List<TType> types = projectService.findAllType();
        return AppResponse.ok(types);
    }

    @ApiOperation(value = "根据回报ID获取回报信息")
    @GetMapping("/findReturnById/{returnId}")
    public AppResponse<TReturn> findReturnById(@PathVariable("returnId") Integer returnId) {
        TReturn returnInfo = projectService.findReturnInfo(returnId);
        return AppResponse.ok(returnInfo);
    }

    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnListByPid(@PathVariable("projectId") Integer projectId) {
        List<TReturn> returns = projectService.findTReturnByProjectId(projectId);
        return AppResponse.ok(returns);
    }
}
