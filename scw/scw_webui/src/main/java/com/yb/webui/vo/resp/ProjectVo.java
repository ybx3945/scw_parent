package com.yb.webui.vo.resp;

import lombok.Data;

import java.io.Serializable;

// 用来批量显示项目的信息
@Data
public class ProjectVo implements Serializable {

    // 会员ID
    private Integer memberid;
    // 项目名称
    private String name;
    // 项目ID
    private Integer id;
    // 项目简介
    private String remark;
    // 头图片
    private String headerImage;
}
