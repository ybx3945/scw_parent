package com.yb.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.netflix.discovery.converters.Auto;
import com.yb.common.enums.ProjectStatusEnume;
import com.yb.project.enums.ProjectImageTypeEnume;
import com.yb.project.mapper.*;
import com.yb.project.pojo.*;
import com.yb.project.service.ProjectCreateService;
import com.yb.project.vo.req.ProjectRedisStoreVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    private TProjectMapper projectMapper;

    @Autowired(required = false)
    private TProjectTagMapper tagMapper;

    @Autowired(required = false)
    private TProjectTypeMapper typeMapper;

    @Autowired(required = false)
    private TProjectImagesMapper imagesMapper;

    @Autowired(required = false)
    private TReturnMapper returnMapper;

    @Autowired(required = false)
    private TTagMapper tTagMapper;

    @Autowired(required = false)
    private TTypeMapper tTypeMapper;

    @Override
    public String initCreateProject(Integer memberId) {
        // 为项目创建一个临时令牌，方便后续进行存取操作
        String projectToken = UUID.randomUUID().toString().replace("-", "") + "_Project";
        // 创建一个ProjectRedisStoreVo的空对象
        ProjectRedisStoreVo redisVo = new ProjectRedisStoreVo();
        // 将memberId加入到初始化的项目对象中
        redisVo.setMemberid(memberId);
        // 将临时token引入到Redis中
        String redisVoStr = JSON.toJSONString(redisVo);
        redisTemplate.opsForValue().set(projectToken, redisVoStr);
        return projectToken;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStoreVo redisVo) {
        TProject tProject = new TProject();
        BeanUtils.copyProperties(redisVo, tProject);
        tProject.setStatus(status.getCode() + "");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tProject.setCreatedate(dateFormat.format(new Date()));
        // 将数据插入到数据库中
        projectMapper.insert(tProject);
        // 获取刚刚插入数据的ID
        Integer projectId = tProject.getId();
        // 插入头图片
        String headerImage = redisVo.getHeaderImage();
        if (headerImage != null) {
            TProjectImages tProjectImages = new TProjectImages(null, projectId, headerImage, ProjectImageTypeEnume.HEADER.getCode());
            imagesMapper.insert(tProjectImages);
        }
        // 详情图片
        List<String> detailImages = redisVo.getDetailImages();
        if (detailImages != null && detailImages.size() > 0) {
            for (String detailImage : detailImages) {
                TProjectImages detailImagObj = new TProjectImages(null, projectId, headerImage, ProjectImageTypeEnume.DETAILS.getCode());
                imagesMapper.insert(detailImagObj);
            }
        }
        // 标签信息
        List<Integer> tagIds = redisVo.getTagIds();
        if (tagIds != null && tagIds.size() > 0) {
            for (Integer tagId : tagIds) {
                TProjectTag tProjectTag = new TProjectTag(null, projectId, tagId);
                tagMapper.insert(tProjectTag);
            }
        }
        // 分类信息
        List<Integer> typeIds = redisVo.getTypeIds();
        if (typeIds != null && typeIds.size() > 0) {
            for (Integer typeId : typeIds) {
                TProjectType tProjectType = new TProjectType(null, projectId, typeId);
                typeMapper.insert(tProjectType);
            }
        }
        // 回报数据
        List<TReturn> projectReturns = redisVo.getProjectReturns();
        if (projectReturns != null && projectReturns.size() > 0) {
            for (TReturn tReturn : projectReturns) {
                tReturn.setProjectid(projectId);
                returnMapper.insert(tReturn);
            }
        }
        // 清空Redis
        //redisTemplate.delete(redisVo.getProjectToken());
    }

    @Override
    public List<TProject> findAllProject() {
        return projectMapper.selectByExample(null);
    }

    @Override
    public List<TProjectImages> getProjectImagesByProjectId(Integer id) {
        TProjectImagesExample example = new TProjectImagesExample();
        TProjectImagesExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(id);
        return imagesMapper.selectByExample(example);
    }

    @Override
    public TProject findProjectInfo(Integer projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    @Override
    public List<TReturn> findTReturnByProjectId(Integer id) {
        TReturnExample example = new TReturnExample();
        TReturnExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(id);
        return returnMapper.selectByExample(example);
    }

    @Override
    public List<TTag> findAllTag() {
        return tTagMapper.selectByExample(null);
    }

    @Override
    public List<TType> findAllType() {
        return tTypeMapper.selectByExample(null);
    }

    @Override
    public TReturn findReturnInfo(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }
}
