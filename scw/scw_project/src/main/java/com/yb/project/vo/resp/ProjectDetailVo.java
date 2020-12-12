package com.yb.project.vo.resp;

import com.yb.project.pojo.TReturn;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProjectDetailVo implements Serializable {

    private Integer id; // 项目ID

    private String name; // 项目名

    private String remark; // 项目简介

    private Long money; // 已经筹到的资金

    private Integer day; // 众筹天数

    private String status; // 当前状态（0 - 即将开始， 1 - 众筹中， 2 - 众筹成功， 3 - 众筹失败）

    private String deploydate; // 发布日期

    private Long supportmoney; // 支持金额

    private Integer supporter; // 支持者数量

    private Integer completion; // 完成度

    private Integer memberid; // 会员ID

    private String createdate; // 创建日期

    private Integer follower; // 关注者数量

    private String headerImage; // 头图片

    private List<String> detailsImage; // 详情图片

    private List<TReturn> projectReturn; // 项目回报
}
