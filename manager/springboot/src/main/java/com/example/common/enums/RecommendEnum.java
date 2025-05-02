package com.example.common.enums;

public enum RecommendEnum {
    YES("Yes"),
    NO("No"),
    ;
    public String status;
    RecommendEnum(String status) {
        this.status = status;
    }
}
