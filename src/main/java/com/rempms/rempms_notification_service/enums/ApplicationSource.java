package com.rempms.rempms_notification_service.enums;

import lombok.Getter;

@Getter
public enum ApplicationSource {

    PDEV_USER("PDEV_USER"),
    REMPMS_CANDIDATE("REMPMS_CANDIDATE"),
    REMPMS_RECRUITMENT("REMPMS_RECRUITMENT");

    private final String appSource;

    ApplicationSource(String appSource) {
        this.appSource = appSource;
    }
}
