package com.example.common.enums;

public enum ResultCodeEnum {
    SUCCESS("200", "Success"),

    PARAM_ERROR("400", "Param Error"),
    TOKEN_INVALID_ERROR("401", "Invalid token"),
    TOKEN_CHECK_ERROR("401", "token check error，please login again"),
    PARAM_LOST_ERROR("4001", "Missing Param"),

    SYSTEM_ERROR("500", "Error"),
    USER_EXIST_ERROR("5001", "User already exist"),
    USER_NOT_LOGIN("5002", "Not logged in"),
    USER_ACCOUNT_ERROR("5003", "Account or password error"),
    USER_NOT_EXIST_ERROR("5004", "User unfounded"),
    PARAM_PASSWORD_ERROR("5005", "Password error"),
    RECOMMEND_EXIST_ERROR("5005", "Recommend already exist"),
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
