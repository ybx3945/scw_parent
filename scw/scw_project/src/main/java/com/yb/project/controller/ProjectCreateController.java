package com.yb.project.controller;

import com.alibaba.fastjson.JSON;
import com.yb.common.enums.ProjectStatusEnume;
import com.yb.common.response.AppResponse;
import com.yb.common.vo.BaseVo;
import com.yb.project.pojo.TReturn;
import com.yb.project.service.ProjectCreateService;
import com.yb.project.vo.req.ProjectBaseInfoVo;
import com.yb.project.vo.req.ProjectRedisStoreVo;
import com.yb.project.vo.req.ProjectReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "新建项目的四个步骤")
@RestController
@RequestMapping("/createProject")
public class ProjectCreateController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectCreateService projectCreateService;

    @ApiOperation(value = "项目新建的第一步：初始化项目")
    @PostMapping("/init")
    public AppResponse<String> init(BaseVo baseVo) {
        // 可以通过baseVo中的用户令牌获取用户信息
        String accessToken = baseVo.getAccessToken();
        // 从Redis中获取memberId
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null || memberId.length() == 0) {
            AppResponse<String> response = AppResponse.fail(null);
            response.setMsg("该用户尚未登录，请登录后再次尝试");
            return response;
        }
        // 通过数据库获取member信息
        String projectToken = projectCreateService.initCreateProject(Integer.parseInt(memberId));
        return AppResponse.ok(projectToken);
    }

    @ApiOperation(value = "项目新建的第二步：添加项目的基本信息")
    @PostMapping("/saveBaseInfo")
    public AppResponse<String> saveBaseInfo(ProjectBaseInfoVo infoVo) {
        // 从Redis中获取第一步存入的对象
        String projectToken = infoVo.getProjectToken();
        // 从Redis中获取的String类型的JSON值
        String redisVoStr = redisTemplate.opsForValue().get(projectToken);
        // 将String类型的JSON值转换为对象
        ProjectRedisStoreVo redisVo = JSON.parseObject(redisVoStr, ProjectRedisStoreVo.class);
        // 将参数infoVo对象中的数据复制到redisVo中，完成基本数据的添加
        BeanUtils.copyProperties(infoVo, redisVo);
        // 将添加完基本数据redisVo鞋回到Redis中
        redisVoStr = JSON.toJSONString(redisVo);
        redisTemplate.opsForValue().set(projectToken, redisVoStr);
        return AppResponse.ok(projectToken);
    }

    @ApiOperation(value = "项目新建的第三步：添加项目的回报信息")
    @PostMapping("/saveReturn")
    public AppResponse<String> saveReturn(@RequestBody List<ProjectReturnVo> returnVoList) {
        // 从参数中获取项目的临时令牌
        if (returnVoList != null && returnVoList.size() > 0) {
            // 获取第一个元素中的项目临时令牌
            String projectToken = returnVoList.get(0).getProjectToken();
            // 通过临时令牌获取项目数据
            String redisVoStr = redisTemplate.opsForValue().get(projectToken);
            // 将redisStr转换为ProjectRedisStoreVo对象
            ProjectRedisStoreVo redisVo = JSON.parseObject(redisVoStr, ProjectRedisStoreVo.class);
            // 将页面收集的数据添加到redisVo中
            // 将returnVoList中的数据导入到List<TReturn>中
            List tReturns = new ArrayList();
            for (ProjectReturnVo returnVo : returnVoList) {
                TReturn tReturn = new TReturn();
                BeanUtils.copyProperties(returnVo, tReturn);
                tReturns.add(tReturn);
            }
            // 添加到redisVo
            redisVo.setProjectReturns(tReturns);
            // 将加入完成的数据写入到Redis中
            redisVoStr = JSON.toJSONString(redisVo);
            redisTemplate.opsForValue().set(projectToken, redisVoStr);
            return AppResponse.ok(projectToken);
        }
        return AppResponse.fail("请输入参数");
    }

    @ApiOperation(value = "项目新建的第四步：添加项目到MySQL数据库")
    @PostMapping("/saveToMysql")
    public AppResponse<Object> saveToMysql(String projectToken, String accessToken, String status) {
        // 1. 获取当前用户信息
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null || memberId.length() == 0) {
            return AppResponse.fail("当前用户没有登录，请登录后再次尝试");
        }
        // 2. 获取前三步完成的项目
        String redisStrVo = redisTemplate.opsForValue().get(projectToken);
        ProjectRedisStoreVo redisVo = JSON.parseObject(redisStrVo, ProjectRedisStoreVo.class);
        // 3. 判断数据非空
        if (redisVo != null) {
            if ("1".equals(status)) {
                // 获取添加的操作
                projectCreateService.saveProjectInfo(ProjectStatusEnume.SUBMIT_AUTH, redisVo);
                return AppResponse.ok("添加成功！");
            }
        }
        return AppResponse.fail(null);
    }
}
