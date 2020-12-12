package com.yb.user.exception;

import com.yb.user.enums.UserExceptionEnum;

public class UserException extends RuntimeException {

    public UserException(UserExceptionEnum userEnum) {
        super(userEnum.getMsg());
    }
}
