package com.rempms.rempms_notification_service.enums;

import lombok.Getter;

@Getter
public enum Status {

    SENT("SENT"),
    FAILED("FAILED"),
    RETRIED("RETRIED"),
    PENDING("PENDING"),
    INIT_ATTEMPT_FAILED("INIT_ATTEMPT_FAILED");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
