package com.yb.webui.vo.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddressVo implements Serializable {

    private Integer id;
    private Integer memberId;
    private String address;
}
