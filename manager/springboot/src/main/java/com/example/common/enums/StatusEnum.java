package com.example.common.enums;

public enum StatusEnum {
    YES("Approved"),
    NO("Rejected"),
    ;
    public String status;
    StatusEnum(String status) {
        this.status = status;
    }
}
