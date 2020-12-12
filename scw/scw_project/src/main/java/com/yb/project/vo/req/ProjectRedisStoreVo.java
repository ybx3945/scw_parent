package com.yb.project.vo.req;

import com.yb.project.pojo.TReturn;
import lombok.Data;

import java.util.List;

// 在Redis中存储的数据
@Data
public class ProjectRedisStoreVo {

    // 第一步：项目的临时令牌（作为Redis中获取数据的key）
    private String projectToken;
    private Integer memberid; // 会员id

    // 第二步：project表中的数据
    private String name; // 项目名称
    private String remark; // 项目简介
    private Long money; // 筹措的金额
    private Integer day; // 天数
    private String createdate; // 创建的时间
    // 头部图片 一张
    private String headerImage;
    // 详情图片 多张
    private List<String> detailImages;
    // 标签 多个
    private List<Integer> tagIds;
    // 类型 多个
    private List<Integer> typeIds;

    // 第三步：回报数据
    private List<TReturn> projectReturns;
}
