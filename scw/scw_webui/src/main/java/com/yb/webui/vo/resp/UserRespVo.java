package com.yb.webui.vo.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRespVo implements Serializable {

    private String accessToken; // 用户令牌
    private Integer id;
    private String loginacct;
    private String username;
    private String email;
    private String authstatus;
    private String usertype;
    private String realname;
    private String cardnum;
    private String accttype;
}
